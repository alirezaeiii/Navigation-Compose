package com.android.sample.app

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.app.database.section.SectionDao
import com.android.sample.app.domain.Link
import com.android.sample.app.domain.Section
import com.android.sample.app.network.ApiService
import com.android.sample.app.repository.SectionRepository
import com.android.sample.app.util.ViewState
import com.android.sample.app.util.isNetworkAvailable
import com.android.sample.app.viewmodel.SectionViewModel
import io.mockk.every
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
        mockkStatic("com.android.sample.app.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(api.getSection(anyString())).thenReturn(
                Section("id", "title", "description")
            )
            `when`(dao.getSection(anyString())).thenReturn(null)
        }
        val repository = SectionRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = SectionViewModel(repository, Link("id", "title", "href"))
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Success(null)))
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
        val viewModel = SectionViewModel(repository, Link("id", "string", "href"))
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Error(errorMsg)))
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
        val viewModel = SectionViewModel(repository, Link("id", "title", "href"))
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Error(errorMsg)))
    }
}