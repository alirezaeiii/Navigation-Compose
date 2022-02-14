package com.android.sample.app.domain

import com.google.gson.annotations.SerializedName

class Links(
    @SerializedName("viaplay:sections") val sections: List<Link>
)