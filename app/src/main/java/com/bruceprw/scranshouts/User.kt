package com.bruceprw.scranshouts
import com.google.firebase.database.IgnoreExtraProperties
import kotlin.collections.ArrayList

@IgnoreExtraProperties
data class User(val email: String? = null, val name: String? = null, val picUrl : String? = null, val uid: String? = null, val xp : Int? = null, val reviewList: ArrayList<Int>? = null) {

}