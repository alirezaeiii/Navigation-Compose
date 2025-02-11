package com.android.sample.app.domain

import com.android.sample.app.database.dashboard.DashboardEntity
import com.android.sample.app.database.dashboard.LinkEntity
import com.google.gson.annotations.SerializedName

class Dashboard(
    @SerializedName("_links") val links: Links
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return links == (other as Dashboard).links
    }

    override fun hashCode(): Int {
        return links.hashCode()
    }
}

fun Dashboard.asDatabaseModel(): DashboardEntity {
    return DashboardEntity(
        sections = this.links.sections.map {
            LinkEntity(id = it.id, title = it.title, href = it.href)
        }
    )
}