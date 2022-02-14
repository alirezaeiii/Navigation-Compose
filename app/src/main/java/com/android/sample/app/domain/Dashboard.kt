package com.android.sample.app.domain

import com.android.sample.app.database.dashboard.DbDashboard
import com.android.sample.app.database.dashboard.DbLink
import com.google.gson.annotations.SerializedName

class Dashboard(
    @SerializedName("_links") val links: Links
)

fun Dashboard.asDatabaseModel(): DbDashboard {
    return DbDashboard(
        sections = this.links.sections.map {
            DbLink(id = it.id, title = it.title, href = it.href)
        }
    )
}