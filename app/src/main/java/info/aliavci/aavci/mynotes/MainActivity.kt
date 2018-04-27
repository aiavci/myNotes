package info.aliavci.aavci.mynotes

import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import com.github.kittinunf.fuel.httpGet
import com.github.kittinunf.result.Result
import org.jetbrains.anko.AnkoComponent
import org.jetbrains.anko.AnkoContext
import org.jetbrains.anko.button
import org.jetbrains.anko.dip
import org.jetbrains.anko.padding
import org.jetbrains.anko.sdk25.coroutines.onClick
import org.jetbrains.anko.setContentView
import org.jetbrains.anko.startActivity
import org.jetbrains.anko.verticalLayout
import timber.log.Timber

/**
 * Created by Ali Avci
 * Version
 */
class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        MainActivityUI().setContentView(this)
        downloadContent()
    }
}

fun downloadContent() {
    //an extension over string (support GET, PUT, POST, DELETE with httpGet(), httpPut(), httpPost(), httpDelete())
    "http://httpbin.org/get".httpGet().responseString { request, response, result ->
        //do something with response
        when (result) {
            is Result.Failure -> {
                val ex = result.getException()
                Timber.d("get error $ex")
            }
            is Result.Success -> {
                val data = result.get()
                Timber.d("get success $data")
            }
        }
    }
}

class MainActivityUI : AnkoComponent<MainActivity> {
    override fun createView(ui: AnkoContext<MainActivity>) = with(ui) {
        verticalLayout {
            padding = dip(25)
            button("Text Editor") {
                onClick {
                    startActivity<ContentEditorActivity>()
                }
            }
        }
    }
}