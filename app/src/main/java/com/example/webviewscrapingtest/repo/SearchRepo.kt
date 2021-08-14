package com.example.webviewscrapingtest.repo

import android.content.Context
import com.example.webviewscrapingtest.model.SearchResult
import com.example.webviewscrapingtest.scraping.ScrapListener
import com.example.webviewscrapingtest.scraping.Scraper
import kotlinx.coroutines.CompletableDeferred

object SearchRepo {

    private val allResults = mutableMapOf<String, List<SearchResult>>()

    fun getSearchResults(query: String) =
        allResults[query] ?: emptyList()


    fun addSearchResults(query: String, results: List<SearchResult>) {
        allResults[query] = results
    }

    suspend fun search(context: Context, query: String) {
        val deferred = CompletableDeferred<List<SearchResult>>()
        val scraper = Scraper(context).apply {
            setListener(object : ScrapListener {
                override fun onFinish(results: List<SearchResult>) {
                    deferred.complete(results)
                }
            })
        }

        scraper.start(query)

        val results = deferred.await()
        addSearchResults(query, results)
    }
}