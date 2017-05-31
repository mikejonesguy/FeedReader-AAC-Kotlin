package feedreader.aac.kotlin.api

import android.util.Log
import feedreader.aac.kotlin.model.Feed
import feedreader.aac.kotlin.viewmodel.ArticleListViewModel
import org.simpleframework.xml.core.Persister
import org.simpleframework.xml.transform.RegistryMatcher
import org.simpleframework.xml.transform.Transform
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.simplexml.SimpleXmlConverterFactory
import retrofit2.http.GET
import java.text.DateFormat
import java.text.SimpleDateFormat
import java.util.*

class FeedFetcher : Callback<Feed> {
    private val TAG = "AAC.FeedFetcher"

    private val baseUrl = "http://feeds.feedburner.com/"
    private var model: ArticleListViewModel? = null

    fun start(model: ArticleListViewModel) {
        this.model = model

        // Use this DateFormat and the DateFormatTransformer to parse the published date for our Article pojo
        val format = SimpleDateFormat("EEE, dd MMM yyyy HH:mm:ss Z", Locale.ENGLISH)
        val matcher = RegistryMatcher()
        matcher.bind(Date::class.java, DateFormatTransformer(format))
        val serializer = Persister(matcher)

        val retrofit = Retrofit.Builder().baseUrl(baseUrl)
                .addConverterFactory(SimpleXmlConverterFactory.create(serializer)).build()

        val api = retrofit.create(Rest::class.java)

        val call = api.loadFeed()
        call.enqueue(this)
    }

    override fun onResponse(call: Call<Feed>, response: Response<Feed>) {
        if (response.isSuccessful) {
            // now persist the data to the local database
            model?.insertAsync(response.body())
        } else {
            model?.setFetching(false)
            Log.d(TAG, "onResponse: " + response.errorBody())
        }
    }

    override fun onFailure(call: Call<Feed>, t: Throwable) {
        model?.setFetching(false)
        Log.d(TAG, "onFailure: ", t)
    }

    interface Rest {
        @GET("androidcentral")
        fun loadFeed(): Call<Feed>
    }

    internal inner class DateFormatTransformer(private val dateFormat: DateFormat) : Transform<Date> {

        @Throws(Exception::class)
        override fun read(value: String): Date {
            return dateFormat.parse(value)
        }

        @Throws(Exception::class)
        override fun write(value: Date): String {
            return dateFormat.format(value)
        }

    }
}
