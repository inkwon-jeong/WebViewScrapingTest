package com.example.webviewscrapingtest.ui.search

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.webviewscrapingtest.repo.SearchRepo
import kotlinx.coroutines.launch

const val STATUS_DEFAULT = 0
const val STATUS_LOADING = 1
const val STATUS_FINISH = 2

class SearchViewModel : ViewModel() {

    private val _status = MutableLiveData(STATUS_DEFAULT)
    val status: LiveData<Int>
        get() = _status

    fun initStatus() {
        _status.value = STATUS_DEFAULT
    }

    fun search(context: Context, query: String) =
        viewModelScope.launch {
            _status.value = STATUS_LOADING
            SearchRepo.search(context, query)
            _status.value = STATUS_FINISH
        }
}