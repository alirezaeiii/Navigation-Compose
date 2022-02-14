package com.android.sample.app.viewmodel

import com.android.sample.app.base.BaseRepository
import com.android.sample.app.base.BaseViewModel
import com.android.sample.app.domain.Dashboard
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    repository: BaseRepository<Dashboard>
) : BaseViewModel<Dashboard>(repository)