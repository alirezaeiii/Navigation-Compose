package com.android.sample.app.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.SavedStateHandle
import com.android.sample.app.TestCoroutineRule
import com.android.sample.app.database.section.SectionDao
import com.android.sample.app.domain.Link
import com.android.sample.app.domain.Section
import com.android.sample.app.domain.asDatabaseModel
import com.android.sample.app.network.ApiService
import com.android.sample.app.repository.SectionRepository
import com.android.sample.app.ui.Screens
import com.android.sample.app.util.Async
import com.android.sample.app.util.isNetworkAvailable
import io.mockk.every
import io.mockk.mockk
import io.mockk.mockkStatic
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import org.hamcrest.CoreMatchers.`is`
import org.hamcrest.MatcherAssert.assertThat
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.rules.TestRule
import org.mockito.ArgumentMatchers.anyInt
import org.mockito.ArgumentMatchers.anyString
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class SectionViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: ApiService

    @Mock
    private lateinit var dao: SectionDao

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        val section = Section("id", "title", "description")
        mockkStatic("com.android.sample.app.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.getSection(anyString())).thenReturn(section)
            `when`(dao.getSection(anyString())).thenReturn(section.asDatabaseModel())
        }
        val repository = SectionRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every {
            savedStateHandle.get<Link>(Screens.LINK)
        } returns Link("id", "title", "href")
        val viewModel = SectionViewModel(repository, savedStateHandle)
        assertThat(viewModel.stateFlow.value, `is`(Async.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(Async.Success(section)))
    }

    @Test
    fun givenServerResponseError_whenFetch_shouldReturnError() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)
        mockkStatic("com.android.sample.app.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.getSection(anyString())).thenThrow(RuntimeException(""))
        }
        val repository = SectionRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every {
            savedStateHandle.get<Link>(Screens.LINK)
        } returns Link("id", "title", "href")
        val viewModel = SectionViewModel(repository, savedStateHandle)
        assertThat(viewModel.stateFlow.value, `is`(Async.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(Async.Error(errorMsg)))
    }

    @Test
    fun givenNetworkUnAvailable_whenFetch_shouldReturnError() {
        val errorMsg = "error message"
        `when`(context.getString(anyInt())).thenReturn(errorMsg)
        mockkStatic("com.android.sample.app.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns false
        testCoroutineRule.runBlockingTest {
            `when`(api.getSection(anyString())).thenReturn(
                Section("id", "title", "description")
            )
            `when`(dao.getSection(anyString())).thenReturn(null)
        }
        val repository = SectionRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val savedStateHandle = mockk<SavedStateHandle>(relaxed = true)
        every {
            savedStateHandle.get<Link>(Screens.LINK)
        } returns Link("id", "title", "href")
        val viewModel = SectionViewModel(repository, savedStateHandle)
        assertThat(viewModel.stateFlow.value, `is`(Async.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(Async.Error(errorMsg)))
    }
}