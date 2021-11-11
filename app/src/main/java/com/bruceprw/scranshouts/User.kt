package com.bruceprw.scranshouts
import com.google.firebase.database.IgnoreExtraProperties
@IgnoreExtraProperties
data class User(val email: String? = null, val name: String? = null, val picUrl : String? = null, val uid: Int? = null, val xp : Int? = null) {




    //"u0001" : {
//            "email" : "test@test.com",
//            "name" : "test@test.com",
//            "picUrl" : "https://image.com/image123",
//            "uid" : 1,
//            "xp" : 1
//        }
}