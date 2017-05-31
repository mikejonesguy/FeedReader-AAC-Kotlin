package feedreader.aac.kotlin

import android.app.Application

class App : Application() {
    override fun onCreate() {
        super.onCreate()
        instance = this
    }

    companion object {
        private var instance: App? = null
        val current: App get() = instance!!
    }
}
