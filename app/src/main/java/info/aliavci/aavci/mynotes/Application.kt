package info.aliavci.aavci.mynotes

import android.app.Application
import com.raizlabs.android.dbflow.config.FlowConfig
import com.raizlabs.android.dbflow.config.FlowManager

/**
 * Created by Ali Avci
 * Version
 */
class Application : Application() {
    override fun onCreate() {
        super.onCreate()
        // This instantiates DBFlow
        FlowManager.init(FlowConfig.Builder(this).build())
    }
}