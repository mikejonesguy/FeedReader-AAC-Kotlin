package feedreader.aac.kotlin.model

import org.simpleframework.xml.Element
import org.simpleframework.xml.ElementList
import org.simpleframework.xml.Path
import org.simpleframework.xml.Root

@Root(name = "rss", strict = false)
class Feed {

    @field:Path("channel")
    @field:Element(name = "title")
    var channelTitle: String? = null

    @field:Path("channel")
    @field:ElementList(name = "item", inline = true)
    var articleList: List<Article>? = null

}