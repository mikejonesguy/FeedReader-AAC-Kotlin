package feedreader.aac.kotlin.model

import android.arch.lifecycle.LiveData
import android.arch.persistence.room.Dao
import android.arch.persistence.room.Delete
import android.arch.persistence.room.Insert
import android.arch.persistence.room.OnConflictStrategy.REPLACE
import android.arch.persistence.room.Query

@Dao
interface ArticleDao {
    // this is run async auto-magically because it's wrapped in LiveData
    @get:Query("SELECT * FROM article ORDER BY published DESC")
    val loadAll: LiveData<List<Article>>

    // this is not async by default -- have to put it in an AsyncTask
    @Insert(onConflict = REPLACE)
    fun insertAll(users: List<Article>?)

    // also not async by default
    @Delete
    fun delete(user: Article)
}
