package com.application.stylesync

import android.content.Context
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.firebase.Timestamp
import com.google.firebase.firestore.FieldValue
import kotlinx.parcelize.Parcelize

@Entity
data class Post(
    var content: String,
    var imageUri: String,
    var style: String,
    var color: String,
    var userId: String,
    @PrimaryKey var id: String,
    var lastUpdated: Long? = null
) {
    companion object {
        var lastUpdated: Long
            get() {
                return MyApplication.Globals
                    .appContext?.getSharedPreferences("TAG", Context.MODE_PRIVATE)
                    ?.getLong(GET_LAST_UPDATED, 0) ?: 0
            }
            set(value) {
                MyApplication.Globals
                    ?.appContext
                    ?.getSharedPreferences("TAG", Context.MODE_PRIVATE)?.edit()
                    ?.putLong(GET_LAST_UPDATED, value)?.apply()
            }

        const val USER_ID = "userId"
        const val ID_KEY = "id"
        const val IMAGE_URI = "imageUri"
        const val CONTENT = "content"
        const val STYLE = "style"
        const val COLOR = "color"
        const val LAST_UPDATED = "lastUpdated"
        const val GET_LAST_UPDATED = "get_last_updated"

        fun fromJSON(json: Map<String, Any>): Post {
            val id = json[ID_KEY] as? String ?: ""
            val userId = json[USER_ID] as? String ?: ""
            val imageUri = json[IMAGE_URI] as? String ?: ""
            val content = json[CONTENT] as? String ?: ""
            val style = json[STYLE] as? String ?: ""
            val color = json[COLOR] as? String ?: ""
            val post = Post(content, imageUri, style, color, userId, id)

            val timestamp: Timestamp? = json[LAST_UPDATED] as? Timestamp
            timestamp?.let {
                post.lastUpdated = it.seconds
            }

            return post
        }
    }


    val json: Map<String, Any>
        get() {
            return hashMapOf(
                ID_KEY to id,
                USER_ID to userId,
                IMAGE_URI to imageUri,
                CONTENT to content,
                STYLE to style,
                COLOR to color,
                LAST_UPDATED to FieldValue.serverTimestamp()
            )
        }
}

@Parcelize
data class PostParcelable(
    val content: String,
    val imageUri: String,
    val style: String,
    val color: String,
    val userId: String,
    val id: String
) : Parcelable {
    constructor(post: Post) : this(
        post.content,
        post.imageUri,
        post.style,
        post.color,
        post.userId,
        post.id
    )
}