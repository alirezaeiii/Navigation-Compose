package com.android.sample.app.di

import com.android.sample.app.base.BaseRepository
import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.Section
import com.android.sample.app.repository.DashboardRepository
import com.android.sample.app.repository.SectionRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Singleton
    @Binds
    internal abstract fun bindDashboardRepository(dashboardRepository: DashboardRepository): BaseRepository<Dashboard>

    @Singleton
    @Binds
    internal abstract fun bindSectionRepository(sectionRepository: SectionRepository): BaseRepository<Section>
}