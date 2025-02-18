package com.android.sample.app.viewmodel

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.app.TestCoroutineRule
import com.android.sample.app.database.dashboard.DashboardDao
import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.Links
import com.android.sample.app.domain.asDatabaseModel
import com.android.sample.app.network.ApiService
import com.android.sample.app.repository.DashboardRepository
import com.android.sample.app.util.Async
import com.android.sample.app.util.isNetworkAvailable
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
import org.mockito.Mock
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations

@ExperimentalCoroutinesApi
class DashboardViewModelTest {

    @get:Rule
    val rule: TestRule = InstantTaskExecutorRule()

    @get:Rule
    val testCoroutineRule = TestCoroutineRule()

    @Mock
    private lateinit var api: ApiService

    @Mock
    private lateinit var dao: DashboardDao

    @Mock
    private lateinit var context: Context

    @Before
    fun setUp() {
        MockitoAnnotations.initMocks(this)
    }

    @Test
    fun givenServerResponse200_whenFetch_shouldReturnSuccess() {
        val dashboard = Dashboard(Links(emptyList()))
        mockkStatic("com.android.sample.app.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(dao.getDashboard()).thenReturn(dashboard.asDatabaseModel())
            `when`(api.getDashboard()).thenReturn(dashboard)
        }
        val repository = DashboardRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = DashboardViewModel(repository)
        assertThat(viewModel.stateFlow.value, `is`(Async.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(Async.Success(dashboard)))
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
            `when`(api.getDashboard()).thenThrow(RuntimeException(""))
        }
        val repository = DashboardRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = DashboardViewModel(repository)
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
        val repository = DashboardRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = DashboardViewModel(repository)
        assertThat(viewModel.stateFlow.value, `is`(Async.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(Async.Error(errorMsg)))
    }
}