package com.android.sample.app.viewmodel

import androidx.lifecycle.SavedStateHandle
import com.android.sample.app.base.BaseRepository
import com.android.sample.app.base.BaseViewModel
import com.android.sample.app.domain.Link
import com.android.sample.app.domain.Section
import com.android.sample.app.ui.Screens.Companion.LINK
import com.android.sample.app.util.cleanHref
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SectionViewModel @Inject constructor(
    repository: BaseRepository<Section>,
    savedStateHandle: SavedStateHandle
) : BaseViewModel<Section>(
        repository,
        savedStateHandle.get<Link>(LINK)?.id,
        savedStateHandle.get<Link>(LINK)?.href?.cleanHref()
    )