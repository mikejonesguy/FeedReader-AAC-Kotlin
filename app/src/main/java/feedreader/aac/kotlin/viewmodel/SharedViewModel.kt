package feedreader.aac.kotlin.viewmodel

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel

import feedreader.aac.kotlin.model.Article

class SharedViewModel : ViewModel() {
    val selected = MutableLiveData<Article>()

    fun select(item: Article) {
        selected.value = item
    }
}
