package com.android.sample.app.util

import android.os.Bundle
import androidx.navigation.NavType
import com.android.sample.app.domain.Link
import com.google.gson.Gson

class AssetParamType : NavType<Link>(isNullableAllowed = false) {
    override fun get(bundle: Bundle, key: String): Link? {
        return bundle.getParcelable(key)
    }

    override fun parseValue(value: String): Link {
        return Gson().fromJson(value, Link::class.java)
    }

    override fun put(bundle: Bundle, key: String, value: Link) {
        bundle.putParcelable(key, value)
    }
}