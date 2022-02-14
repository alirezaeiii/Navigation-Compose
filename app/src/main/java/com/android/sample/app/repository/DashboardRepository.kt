package com.android.sample.app.repository

import android.content.Context
import com.android.sample.app.base.BaseRepository
import com.android.sample.app.database.dashboard.DashboardDao
import com.android.sample.app.database.dashboard.asDomainModel
import com.android.sample.app.di.IoDispatcher
import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.asDatabaseModel
import com.android.sample.app.network.ApiService
import kotlinx.coroutines.CoroutineDispatcher
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DashboardRepository @Inject constructor(
    private val dao: DashboardDao,
    private val api: ApiService,
    context: Context,
    @IoDispatcher ioDispatcher: CoroutineDispatcher
) : BaseRepository<Dashboard>(context, ioDispatcher) {

    override suspend fun query(id: String?): Dashboard? =
        dao.getDashboard()?.asDomainModel()

    override suspend fun fetch(url: String?): Dashboard = api.getDashboard()

    override suspend fun saveFetchResult(t: Dashboard) {
        dao.insert(t.asDatabaseModel())
    }
}