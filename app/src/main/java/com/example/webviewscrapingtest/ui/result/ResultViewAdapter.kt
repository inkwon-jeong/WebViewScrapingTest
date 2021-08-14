package com.example.webviewscrapingtest.ui.result

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.webviewscrapingtest.databinding.ItemSearchResultBinding
import com.example.webviewscrapingtest.model.SearchResult

class ResultViewAdapter :
    ListAdapter<SearchResult, ResultViewAdapter.ViewHolder>(ResultDiffUtil()) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemSearchResultBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.linkView.text = item.link
        holder.titleView.text = item.title
        holder.contentView.text = item.content
    }

    inner class ViewHolder(binding: ItemSearchResultBinding) :
        RecyclerView.ViewHolder(binding.root) {
        val linkView: TextView = binding.itemLink
        val titleView: TextView = binding.itemTitle
        val contentView: TextView = binding.itemContent
    }
}

class ResultDiffUtil : DiffUtil.ItemCallback<SearchResult>() {
    override fun areItemsTheSame(oldItem: SearchResult, newItem: SearchResult) =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: SearchResult, newItem: SearchResult) =
        oldItem == newItem
}