# fix-chatterby.ps1
# AUTO-FIXES ALL FILES, DELETES BROKEN COMPOSE, ADDS WORKING VIEW CODE

$root = Get-Location
$ui = "$root/app/src/main/java/com/howdiedoodies/chatterby/ui"
$res = "$root/app/src/main/res"

function Write-File($path, $content) {
    $full = Join-Path $root $path
    $dir  = Split-Path $full -Parent
    if (-not (Test-Path $dir)) { New-Item -ItemType Directory -Path $dir -Force | Out-Null }
    Set-Content -Path $full -Value $content -Encoding UTF8
    Write-Host "FIXED: $path"
}

# 1. DELETE BROKEN FILES
$toDelete = @(
    "$ui/theme",
    "$ui/MainScreenKt.kt",
    "$ui/NavGraph.kt",
    "$ui/MainActivity.kt",
    "$ui/FavoriteAdapter.kt",
    "$ui/FavoriteFragment.kt",
    "$ui/RoomActivity.kt"
)
foreach ($item in $toDelete) {
    if (Test-Path $item) {
        Remove-Item -Recurse -Force $item
        Write-Host "DELETED: $item"
    }
}

# 2. MainActivity.kt (VIEW ONLY)
$mainKt = @'
package com.howdiedoodies.chatterby

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.howdiedoodies.chatterby.ui.FavoriteFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, FavoriteFragment())
                .commit()
        }
    }
}
'@
Write-File "app/src/main/java/com/howdiedoodies/chatterby/MainActivity.kt" $mainKt

# 3. activity_main.xml
$mainXml = @'
<?xml version="1.0" encoding="utf-8"?>
<FrameLayout xmlns:android="http://schemas.android.com/apk/res/android"
    android:id="@+id/container"
    android:layout_width="match_parent"
    android:layout_height="match_parent" />
'@
Write-File "app/src/main/res/layout/activity_main.xml" $mainXml

# 4. FavoriteAdapter.kt (NO BINDING)
$adapterKt = @'
package com.howdiedoodies.chatterby.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.howdiedoodies.chatterby.R
import com.howdiedoodies.chatterby.data.Favorite

class FavoriteAdapter(
    private val onClick: (String, Boolean) -> Unit
) : ListAdapter<Favorite, FavoriteAdapter.VH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return VH(view, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(
        itemView: View,
        private val onClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.username)
        private val status: TextView = itemView.findViewById(R.id.status)
        private val btnOpen: Button = itemView.findViewById(R.id.btnOpen)
        private val chatOnly: Button = itemView.findViewById(R.id.chatOnly)

        fun bind(fav: Favorite) {
            username.text = fav.username
            val now = System.currentTimeMillis()
            val last = fav.lastOnline
            if (last != null && now - last < 120_000) {
                status.text = "LIVE"
                status.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                status.text = last?.let { DateUtils.getRelativeTimeSpanString(it, now, DateUtils.MINUTE_IN_MILLIS) } ?: "Never"
                status.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }
            btnOpen.setOnClickListener { onClick(fav.username, false) }
            chatOnly.setOnClickListener { onClick(fav.username, true) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(old: Favorite, new: Favorite) = old.username == new.username
        override fun areContentsTheSame(old: Favorite, new: Favorite) = old == new
    }
}
'@
Write-File "app/src/main/java/com/howdiedoodies/chatterby/ui/FavoriteAdapter.kt" $adapterKt

# 5. FavoriteFragment.kt
$fragmentKt = @'
package com.howdiedoodies.chatterby.ui

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.howdiedoodies.chatterby.R
import com.howdiedoodies.chatterby.viewmodel.FavoriteViewModel

class FavoriteFragment : Fragment() {
    private val viewModel: FavoriteViewModel by viewModels()
    private lateinit var adapter: FavoriteAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        return inflater.inflate(R.layout.fragment_favorite, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        adapter = FavoriteAdapter { username, chatOnly -> RoomActivity.start(requireContext(), username, chatOnly) }

        view.findViewById<androidx.recyclerview.widget.RecyclerView>(R.id.recyclerView).apply {
            layoutManager = LinearLayoutManager(requireContext())
            adapter = this@FavoriteFragment.adapter
        }

        viewModel.favorites.observe(viewLifecycleOwner) { adapter.submitList(it) }

        view.findViewById<SwipeRefreshLayout>(R.id.refresh).setOnRefreshListener {
            viewModel.refreshNow()
            view.findViewById<SwipeRefreshLayout>(R.id.refresh).isRefreshing = false
        }
    }
}
'@
Write-File "app/src/main/java/com/howdiedoodies/chatterby/ui/FavoriteFragment.kt" $fragmentKt

# 6. RoomActivity.kt
$roomKt = @'
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
'@
Write-File "app/src/main/java/com/howdiedoodies/chatterby/ui/RoomActivity.kt" $roomKt

# 7. build.gradle.kts (FIXED – NO KAPT, USE annotationProcessor)
$gradle = @'
plugins {
    id("com.android.application")
    id("kotlin-android")
}

android {
    namespace = "com.howdiedoodies.chatterby"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.howdiedoodies.chatterby"
        minSdk = 21
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
}

dependencies {
    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")

    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.6")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")

    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.8.1")
    implementation("com.squareup.okhttp3:okhttp:4.12.0")
    implementation("androidx.work:work-runtime-ktx:2.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.2")
    implementation("androidx.webkit:webkit:1.12.0")
    implementation("com.google.android.material:material:1.12.0")
    implementation("org.jsoup:jsoup:1.17.2")
}
'@
Write-File "app/build.gradle.kts" $gradle

# 8. themes.xml (FIX MATERIAL3)
$themes = @'
<resources xmlns:tools="http://schemas.android.com/tools">
    <style name="Theme.Chatterby" parent="Theme.Material3.DayNight.NoActionBar">
        <item name="colorPrimary">#FF6200EE</item>
        <item name="colorOnPrimary">#FFFFFFFF</item>
        <item name="colorPrimaryVariant">#FF3700B3</item>
        <item name="colorSecondary">#FF03DAC6</item>
        <item name="colorOnSecondary">#FF000000</item>
        <item name="colorSurface">#FFFFFFFF</item>
        <item name="android:statusBarColor">?attr/colorPrimaryVariant</item>
    </style>
</resources>
'@
Write-File "app/src/main/res/values/themes.xml" $themes

Write-Host "`nALL FILES FIXED! RUN:"
Write-Host "./gradlew clean assembleDebug"