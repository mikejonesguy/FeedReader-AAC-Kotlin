package feedreader.aac.kotlin.viewmodel

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel
import android.os.AsyncTask
import feedreader.aac.kotlin.App
import feedreader.aac.kotlin.api.FeedFetcher
import feedreader.aac.kotlin.model.AppDatabase
import feedreader.aac.kotlin.model.Article
import feedreader.aac.kotlin.model.Feed

class ArticleListViewModel : ViewModel() {
    private val database: AppDatabase = AppDatabase.getDatabase(App.current)
    private val fetcher = FeedFetcher()

    val articles: LiveData<List<Article>> = database.articleModel().loadAll

    val fetching: MutableLiveData<Boolean> = MutableLiveData()

    fun setFetching(isFetching: Boolean) {
        fetching.value = isFetching
    }

    fun fetchArticles() {
        setFetching(true)
        fetcher.start(this)
    }

    fun insertAsync(feed: Feed) {
        InsertTask(feed).execute()
    }

    inner class InsertTask(private val feed: Feed): AsyncTask<Any, Void, Void>() {
        override fun doInBackground(vararg params:Any): Void? {
            AppDatabase.getDatabase(App.current).articleModel().insertAll(feed.articleList)
            return null
        }

        override fun onPostExecute(result: Void?) {
            // tell the view that we're finished fetching articles
            setFetching(false)
        }
    }
}
