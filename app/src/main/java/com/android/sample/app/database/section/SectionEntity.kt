package com.android.sample.app.database.section

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.sample.app.domain.Section

@Entity(tableName = "sections")
class SectionEntity(
    @PrimaryKey val id: String,
    val title: String,
    val description: String
)


fun SectionEntity.asDomainModel(): Section =
    Section(sectionId = this.id, title = this.title, description = this.description)