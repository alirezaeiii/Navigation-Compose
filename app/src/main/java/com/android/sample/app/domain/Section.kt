package com.android.sample.app.domain

import com.android.sample.app.database.section.SectionEntity

class Section(
    val sectionId: String,
    val title: String,
    val description: String
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return sectionId == (other as Section).sectionId
    }

    override fun hashCode(): Int {
        return sectionId.hashCode()
    }
}

fun Section.asDatabaseModel(): SectionEntity =
    SectionEntity(id = this.sectionId, title = this.title, description = this.description)