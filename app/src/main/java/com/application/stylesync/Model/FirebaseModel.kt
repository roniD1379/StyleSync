import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class Post (id: String, title:String,content:String)

class FirestoreManager {

    private val db = FirebaseFirestore.getInstance()
    private val postsCollection = db.collection("posts")

    suspend fun getAllPosts(): List<Post> {
        val querySnapshot = postsCollection.get().await()
        return querySnapshot.documents.map { document ->
            val id = document.id
            val title = document.getString("title") ?: ""
            val content = document.getString("content") ?: ""
            Post(id, title, content)
        }
    }

    suspend fun addNewPost(post: Post) {
        postsCollection.add(post).await()
    }
}
