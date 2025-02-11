package com.android.sample.app.domain

import com.google.gson.annotations.SerializedName

class Links(
    @SerializedName("viaplay:sections") val sections: List<Link>
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || javaClass != other.javaClass) return false
        return sections == (other as Links).sections
    }

    override fun hashCode(): Int {
        return sections.hashCode()
    }
}