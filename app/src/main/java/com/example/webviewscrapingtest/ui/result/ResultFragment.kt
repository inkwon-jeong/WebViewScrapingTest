package com.example.webviewscrapingtest.ui.result

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.example.webviewscrapingtest.databinding.FragmentResultBinding

class ResultFragment : Fragment() {

    private var query = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            query = it.getString(ARG_QUERY) ?: ""
        }
    }

    private lateinit var viewModel: ResultViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val binding = FragmentResultBinding.inflate(inflater, container, false)
            .apply {
                viewModel = ViewModelProvider(
                    this@ResultFragment,
                    ResultViewModel.ResultViewModelFactory(query)
                ).get(ResultViewModel::class.java)

                val adapter = ResultViewAdapter()
                list.adapter = adapter
                viewModel.results.observe(viewLifecycleOwner, {
                    adapter.submitList(it)
                })
            }

        return binding.root
    }

    companion object {
        const val ARG_QUERY = "query"

        @JvmStatic
        fun newInstance(query: String) =
            ResultFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_QUERY, query)
                }
            }
    }
}