package feedreader.aac.kotlin

import android.annotation.SuppressLint
import android.arch.lifecycle.LifecycleFragment
import android.arch.lifecycle.LifecycleOwner
import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.webkit.WebChromeClient
import feedreader.aac.kotlin.model.Article
import feedreader.aac.kotlin.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.fragment_article_detail.*


class ArticleDetailFragment : LifecycleFragment() {

    companion object {
        val ARG_TITLE = "title"
        val ARG_HTML = "html"
    }

    override fun onCreateView(inflater: LayoutInflater?, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater!!.inflate(R.layout.fragment_article_detail, container, false)
    }

    @SuppressLint("SetJavaScriptEnabled")
    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)

        // for mobile-friendly viewing and video support
        webView.settings.loadWithOverviewMode = true
        webView.settings.useWideViewPort = true
        webView.settings.javaScriptEnabled = true
        webView.setWebChromeClient(WebChromeClient())

        setDetails(arguments.getString(ARG_HTML))

        val sharedViewModel = ViewModelProviders.of(activity).get(SharedViewModel::class.java)
        sharedViewModel.selected.observe(activity as LifecycleOwner, Observer<Article> {
            article -> setDetails(article?.html)
        })
    }

    fun setDetails(html: String?) {
        // add a little CSS styling to make the content look nice (images and videos fit width)
        val formattedHtml = "<html><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"/>" +
                "<style>body { margin: 0px; padding: 2%; } img { width:100%; } " +
                "img[width] { width:inherit; } div.video_iframe iframe { width:100%; height:56vw; margin: auto; }" +
                "</style></head><body>\n" + html.orEmpty() + "\n</body></html>"

        // load the html content
        webView.loadData(formattedHtml, "text/html; charset=UTF-8", "UTF-8")
    }

}
