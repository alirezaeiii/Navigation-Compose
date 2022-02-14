package com.android.sample.app.domain

import com.android.sample.app.database.section.DbSection

class Section(
    val sectionId: String,
    val title: String,
    val description: String
)

fun Section.asDatabaseModel(): DbSection =
    DbSection(id = this.sectionId, title = this.title, description = this.description)