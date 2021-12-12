package com.bruceprw.scranshouts
import android.os.Parcelable
import com.google.firebase.database.IgnoreExtraProperties
import kotlinx.parcelize.Parcelize
import kotlin.collections.ArrayList

@IgnoreExtraProperties
@Parcelize
data class User(val email: String? = null, var name: String? = null, var picUrl : String? = null, val uid: String? = null, var xp : Int? = null, val reviewList: MutableList<String>? = null) :
    Parcelable