package com.android.sample.app.database.dashboard

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.android.sample.app.domain.Dashboard
import com.android.sample.app.domain.Link
import com.android.sample.app.domain.Links


@Entity(tableName = "dashboard")
class DbDashboard(
    @PrimaryKey val primaryKey: String = "dashboard",
    val sections: List<DbLink>
)

fun DbDashboard.asDomainModel(): Dashboard {
    return Dashboard(
        links = Links(sections = this.sections.map {
            Link(id = it.id, title = it.title, href = it.href)
        })
    )
}