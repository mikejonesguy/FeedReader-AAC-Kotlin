package feedreader.aac.kotlin

import android.arch.lifecycle.LifecycleRegistry
import android.arch.lifecycle.LifecycleRegistryOwner
import android.support.v7.app.AppCompatActivity

open class LifecycleAppCompatActivity : AppCompatActivity(), LifecycleRegistryOwner {
    @Suppress("LeakingThis")
    private val lifecycleRegistry: LifecycleRegistry = LifecycleRegistry(this)

    override fun getLifecycle(): LifecycleRegistry {
        return this.lifecycleRegistry
    }
}
