package com.android.sample.app

import android.content.Context
import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import com.android.sample.app.database.dashboard.DashboardDao
import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.Links
import com.android.sample.app.network.ApiService
import com.android.sample.app.repository.DashboardRepository
import com.android.sample.app.util.ViewState
import com.android.sample.app.util.isNetworkAvailable
import com.android.sample.app.viewmodel.DashboardViewModel
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
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.MockitoAnnotations
import java.lang.RuntimeException

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
        mockkStatic("com.android.sample.app.util.ContextExtKt")
        every {
            context.isNetworkAvailable()
        } returns true
        testCoroutineRule.runBlockingTest {
            `when`(dao.getDashboard()).thenReturn(null)
            `when`(api.getDashboard()).thenReturn(Dashboard(Links(emptyList())))
        }
        val repository = DashboardRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = DashboardViewModel(repository)
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
            `when`(api.getDashboard()).thenThrow(RuntimeException(""))
        }
        val repository = DashboardRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = DashboardViewModel(repository)
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
        val repository = DashboardRepository(dao, api, context, Dispatchers.Main)
        testCoroutineRule.pauseDispatcher()
        val viewModel = DashboardViewModel(repository)
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Loading))

        testCoroutineRule.resumeDispatcher()
        assertThat(viewModel.stateFlow.value, `is`(ViewState.Error(errorMsg)))
    }
}