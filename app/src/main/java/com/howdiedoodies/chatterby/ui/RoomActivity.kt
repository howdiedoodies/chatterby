package com.howdiedoodies.chatterby.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.webkit.WebChromeClient
import android.webkit.WebViewClient
import androidx.appcompat.app.AppCompatActivity
import com.howdiedoodies.chatterby.R

class RoomActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_room)

        val username = intent.getStringExtra(EXTRA_USERNAME) ?: return finish()
        val chatOnly = intent.getBooleanExtra(EXTRA_CHAT_ONLY, false)
        val url = if (chatOnly) "https://chaturbate.com/$username/?chat_only=1" else "https://chaturbate.com/$username/"

        findViewById<android.webkit.WebView>(R.id.webView).apply {
            settings.javaScriptEnabled = true
            settings.domStorageEnabled = true
            webViewClient = WebViewClient()
            webChromeClient = WebChromeClient()
            loadUrl(url)
        }
    }

    companion object {
        private const val EXTRA_USERNAME = "username"
        private const val EXTRA_CHAT_ONLY = "chat_only"
        fun start(context: Context, username: String, chatOnly: Boolean) {
            context.startActivity(Intent(context, RoomActivity::class.java).apply {
                putExtra(EXTRA_USERNAME, username)
                putExtra(EXTRA_CHAT_ONLY, chatOnly)
            })
        }
    }
}
