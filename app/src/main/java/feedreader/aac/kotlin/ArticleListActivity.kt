package feedreader.aac.kotlin

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.content.Intent
import android.os.Bundle
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import feedreader.aac.kotlin.model.Article
import feedreader.aac.kotlin.viewmodel.ArticleListViewModel
import feedreader.aac.kotlin.viewmodel.SharedViewModel
import kotlinx.android.synthetic.main.activity_article_list.*
import kotlinx.android.synthetic.main.list_article.*

class ArticleListActivity : LifecycleAppCompatActivity() {

    private var adapter: SimpleItemRecyclerViewAdapter? = null
    private var sharedViewModel: SharedViewModel? = null
    private var twoPane: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_article_list)

        setSupportActionBar(toolbar)
        toolbar.title = title

        // shared view model
        sharedViewModel = ViewModelProviders.of(this).get(SharedViewModel::class.java)

        // recycler view
        adapter = SimpleItemRecyclerViewAdapter()
        articleList.adapter = adapter

        // list view model
        val listViewModel = ViewModelProviders.of(this).get(ArticleListViewModel::class.java)
        listViewModel.articles.observe(this, Observer<List<Article>> { articles -> run {
            adapter?.setValues(articles.orEmpty())
        } })

        listViewModel.fetching.observe(this, Observer<Boolean> { fetching -> run {
            // only show the refreshing indicator if the user initiated a refresh
            if (!(fetching ?: false)) {
                swipeContainer.isRefreshing = fetching ?: false
            }
        } })

        // swipe to refresh
        swipeContainer.setOnRefreshListener {
            listViewModel.fetchArticles()
        }

        // support for tablets
        if (detailContainer != null) {
            twoPane = true
        }

        // fresh start -- refresh the feed and add the detail fragment if twoPane == true
        if (savedInstanceState == null) {
            listViewModel.fetchArticles()
            if (twoPane) {
                // Create the detail fragment and add it to the activity using a fragment transaction.
                val fragment = ArticleDetailFragment()
                fragment.arguments = Bundle()
                supportFragmentManager.beginTransaction().add(R.id.detailContainer, fragment).commit()
            }
        }
    }

    inner class SimpleItemRecyclerViewAdapter : RecyclerView.Adapter<SimpleItemRecyclerViewAdapter.ViewHolder>() {
        private val values = mutableListOf<Article>()

        init {
            setHasStableIds(true)
        }

        fun setValues(articles: List<Article>) {
            values.clear()
            values.addAll(articles)
            notifyDataSetChanged()
        }

        override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
            val view = LayoutInflater.from(parent.context).inflate(R.layout.item_article, parent, false)
            return ViewHolder(view)
        }

        override fun onBindViewHolder(holder: ViewHolder, position: Int) {
            val article = values[position]

            holder.publishedView.text = article.relativeDate().orEmpty()
            holder.titleView.text = article.title.orEmpty()
            holder.contentView.setOnClickListener { v ->
                // notify view model of selected article
                sharedViewModel?.select(article)
                if (!twoPane) {
                    val context = v.context
                    val intent = Intent(context, ArticleDetailActivity::class.java)
                    intent.putExtra(ArticleDetailFragment.ARG_TITLE, article.title)
                    intent.putExtra(ArticleDetailFragment.ARG_HTML, article.html)
                    context.startActivity(intent)
                }
            }
        }

        override fun getItemId(position: Int): Long {
            return values[position].id()
        }

        override fun getItemCount(): Int {
            return values.size
        }

        inner class ViewHolder(val contentView: View) : RecyclerView.ViewHolder(contentView) {
            val titleView: TextView = contentView.findViewById(R.id.titleView) as TextView
            val publishedView: TextView = contentView.findViewById(R.id.publishedView) as TextView

            override fun toString(): String {
                return super.toString() + " '" + titleView.text + "'"
            }
        }

    }

}
