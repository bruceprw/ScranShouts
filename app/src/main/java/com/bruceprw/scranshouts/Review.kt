package com.bruceprw.scranshouts
import android.location.Location
import android.os.Parcelable
import android.view.View
import android.widget.ImageView
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.database.IgnoreExtraProperties
import kotlin.collections.ArrayList
import com.squareup.picasso.Picasso
import kotlinx.parcelize.Parcelize
import java.io.Serializable
import java.util.*

@IgnoreExtraProperties
@Parcelize
data class Review(var title: String? = null, var picUrl: String? = null, var estName: String? = null, var rID: String? = null, val author: String? = null, val rating: Int? = null, val reviewBody: String? = null) : Parcelable
