package feedreader.aac.kotlin.model

import android.arch.persistence.room.Entity
import android.arch.persistence.room.PrimaryKey
import android.support.v7.widget.RecyclerView
import android.text.format.DateUtils
import feedreader.aac.kotlin.App
import org.simpleframework.xml.Element
import org.simpleframework.xml.Namespace
import org.simpleframework.xml.Root
import java.util.*

@Entity
@Root(name = "item", strict = false)
class Article {

    @PrimaryKey
    @field:Element(name = "guid")
    var guid: String? = null

    @field:Element(name = "title")
    var title: String? = null

    @field:Element(name = "link")
    var link: String? = null

    @field:Element(name = "pubDate")
    var published: Date? = null

    @field:Element(name = "encoded", data = true)
    @Namespace(prefix = "content", reference = "http://purl.org/rss/1.0/modules/content/")
    var html: String? = null

    // The guid is in the form of "61046 at http://www.androidcentral.com" -- the number at the beginning is
    // unique and the rest is the same for every article. So, we split the string on the first space and try
    // to parse it as a Long. If that fails for any reason, we use a hashcode of the guid. If guid is null,
    // we return NO_ID. We use this id in the RecyclerView for the itemId.
    fun id(): Long {
        val list = guid?.split(delimiters = ' ', ignoreCase = true, limit = 2).orEmpty()
        return if (list.isNotEmpty()) {
            list[0].toIntOrNull()?.toLong() ?: guid!!.hashCode().toLong()
        } else RecyclerView.NO_ID
    }

    fun relativeDate(): String? {
        val pub = published
        return if (pub != null) {
            DateUtils.getRelativeDateTimeString(App.current, pub.time,
                    DateUtils.MINUTE_IN_MILLIS, DateUtils.DAY_IN_MILLIS, 0).toString()
        } else null
    }
}
