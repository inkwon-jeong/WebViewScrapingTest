package com.example.webviewscrapingtest.ui.result

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.liveData
import com.example.webviewscrapingtest.repo.SearchRepo

class ResultViewModel(private val query: String) : ViewModel() {

    val results = liveData {
        emit(SearchRepo.getSearchResults(query))
    }


    class ResultViewModelFactory(private val query: String) : ViewModelProvider.Factory {
        @Suppress("UNCHECKED_CAST")
        override fun <T : ViewModel?> create(modelClass: Class<T>): T {
            if (modelClass.isAssignableFrom(ResultViewModel::class.java)) {
                return ResultViewModel(query) as T
            }
            throw IllegalArgumentException("Unknown ViewModel Class")
        }
    }
}