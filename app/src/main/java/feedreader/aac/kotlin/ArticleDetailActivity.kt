package feedreader.aac.kotlin

import android.content.Intent
import android.os.Bundle
import android.view.MenuItem
import kotlinx.android.synthetic.main.activity_article_detail.*

class ArticleDetailActivity : LifecycleAppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_article_detail)
        setSupportActionBar(detailToolbar)

        // Show the Up button in the action bar.
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.title = intent.getStringExtra(ArticleDetailFragment.ARG_TITLE).orEmpty()

        if (savedInstanceState == null) {
            // Create the detail fragment and add it to the activity using a fragment transaction.
            val args = Bundle()
            args.putString(ArticleDetailFragment.ARG_HTML, intent.getStringExtra(ArticleDetailFragment.ARG_HTML))
            val fragment = ArticleDetailFragment()
            fragment.arguments = args
            supportFragmentManager.beginTransaction().add(R.id.detailContainer, fragment).commit()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val id = item.itemId
        if (id == android.R.id.home) {
            navigateUpTo(Intent(this, ArticleListActivity::class.java))
            return true
        }
        return super.onOptionsItemSelected(item)
    }
}
