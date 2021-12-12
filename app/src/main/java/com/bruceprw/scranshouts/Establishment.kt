package com.bruceprw.scranshouts
import android.location.Location
import com.google.firebase.database.IgnoreExtraProperties
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class Establishment(val name: String? = null, val eID: String? = null, val picUrl: String? = null, val coordinates: Location? = null, val reviewsList: ArrayList<String>? = null)