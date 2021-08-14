package com.example.webviewscrapingtest.scraping

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Context
import android.graphics.PixelFormat
import android.view.ViewGroup
import android.view.WindowManager
import android.webkit.JavascriptInterface
import android.webkit.WebChromeClient
import android.webkit.WebView
import android.webkit.WebViewClient
import com.example.webviewscrapingtest.model.SearchResult
import org.jsoup.Jsoup
import java.util.concurrent.atomic.AtomicBoolean

@SuppressLint("SetJavaScriptEnabled")
class Scraper(private val context: Context) {

    companion object {
        const val BASE_URL = "https://www.google.com/"
    }

    private var listener: ScrapListener? = null
    private lateinit var webView: WebView

    fun setListener(listener: ScrapListener) {
        this.listener = listener
    }

    private val windowManager: WindowManager by lazy {
        when (context) {
            is Activity -> context.windowManager
            else -> context.getSystemService(Context.WINDOW_SERVICE) as WindowManager
        }
    }

    private val webViewParameters: WindowManager.LayoutParams by lazy {
        WindowManager.LayoutParams(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT,
            WindowManager.LayoutParams.TYPE_APPLICATION,
            WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE.or(WindowManager.LayoutParams.FLAG_NOT_TOUCHABLE),
            PixelFormat.TRANSLUCENT
        ).apply {
            width = 0
            height = 0
        }
    }

    private val isBasePageFinished = AtomicBoolean(false)
    private val isSearchPageFinished = AtomicBoolean(false)

    fun start(query: String) {
        webView = WebView(context).apply {
            addJavascriptInterface(this@Scraper, "Android")
            settings.apply { javaScriptEnabled = true }
            webChromeClient = WebChromeClient()
            webViewClient = object : WebViewClient() {
                override fun onPageFinished(view: WebView, url: String) {
                    when {
                        url == BASE_URL -> {
                            if (isBasePageFinished.getAndSet(true))
                                loadUrl(searchByJavaScriptFunction(query))
                        }
                        url.contains("${BASE_URL}search") -> {
                            if (isSearchPageFinished.getAndSet(true))
                                loadUrl(scrapByJavaScriptFunction())
                        }
                    }
                }
            }
        }

        windowManager.addView(webView, webViewParameters)
        webView.loadUrl(BASE_URL)
    }

    fun stop() {
        windowManager.removeView(webView)
        webView.destroy()
        listener = null
    }

    private fun scrapByJavaScriptFunction() =
        "javascript:window.Android.parse(document.getElementsByTagName('body')[0].innerHTML);"

    private fun searchByJavaScriptFunction(query: String) =
        "javascript:document.getElementsByClassName('gLFyf')[0].value = '$query';" +
                "javascript:document.getElementsByClassName('tsf')[0].submit();"


    @JavascriptInterface
    fun parse(html: String) {
        val document = Jsoup.parse(html)
        val elements = document.select("div[class='mnr-c xpd O9g5cc uUPGi']")
        val results = elements.mapIndexed { index, element ->
            val link = element.select("span[class='Zu0yb UGIkD qzEoUe']").text()
            val title = element.select("div[class='yUTMj MBeuO ynAwRc PpBGzd YcUVQe']").text()
            val content = element.select("div[class='VwiC3b MUxGbd yDYNvb lEBKkf']").text()

            SearchResult(
                id = index,
                link = link,
                title = title,
                content = content
            )
        }

        listener?.onFinish(results)
        stop()
    }
}

interface ScrapListener {
    fun onFinish(results: List<SearchResult>)
}