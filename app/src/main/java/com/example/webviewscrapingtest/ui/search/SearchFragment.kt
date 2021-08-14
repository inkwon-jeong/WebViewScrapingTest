package com.example.webviewscrapingtest.ui.search

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.webviewscrapingtest.MainActivity
import com.example.webviewscrapingtest.databinding.FragmentSearchBinding
import com.example.webviewscrapingtest.ui.dialog.LoadingDialog
import com.example.webviewscrapingtest.utils.hideKeypad

class SearchFragment : Fragment() {

    companion object {
        fun newInstance() = SearchFragment()
    }

    private lateinit var viewModel: SearchViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentSearchBinding.inflate(inflater, container, false)
            .apply {
                viewModel = ViewModelProvider(this@SearchFragment)
                    .get(SearchViewModel::class.java)

                val loadingDialog = LoadingDialog()
                viewModel.status.observe(viewLifecycleOwner, { status ->
                    when (status) {
                        STATUS_LOADING -> {
                            if (!loadingDialog.isAdded)
                                loadingDialog.show(childFragmentManager, tag)
                        }
                        STATUS_FINISH -> {
                            if (loadingDialog.isAdded)
                                loadingDialog.dismiss()

                            val query = editQuery.text.toString().trim()
                            (requireActivity() as MainActivity).navigate(query)
                            viewModel.initStatus()
                        }
                    }
                })

                buttonSearch.setOnClickListener {
                    editQuery.hideKeypad()
                    val query = editQuery.text.toString().trim()
                    viewModel.search(requireActivity(), query)
                }
            }

        return binding.root
    }
}