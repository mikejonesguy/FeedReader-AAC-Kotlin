package feedreader.aac.kotlin.model

import android.arch.persistence.room.Database
import android.arch.persistence.room.Room
import android.arch.persistence.room.RoomDatabase
import android.arch.persistence.room.TypeConverters
import android.content.Context

@Database(entities = arrayOf(Article::class), version = 1)
// Use Converters for conversion between Long values in the database and Date values in the pojo
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {

    abstract fun articleModel(): ArticleDao

    companion object {

        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            if (INSTANCE == null) {
                INSTANCE = Room.databaseBuilder(context.applicationContext, AppDatabase::class.java, "app-db").build()
            }
            return INSTANCE!!
        }

        @Suppress("unused")
        fun destroyInstance() {
            INSTANCE = null
        }
    }

}
