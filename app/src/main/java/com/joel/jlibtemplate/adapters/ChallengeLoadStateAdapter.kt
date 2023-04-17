package com.joel.jlibtemplate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.joel.jlibtemplate.databinding.LoadStateItemBinding

class ChallengeLoadStateAdapter : LoadStateAdapter<ChallengeLoadStateAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, loadState: LoadState): ViewHolder {
        val binding = LoadStateItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, loadState: LoadState) {
        holder.bind(loadState)
    }

    inner class ViewHolder(private val binding: LoadStateItemBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(loadState: LoadState) {
            when(loadState) {
                is LoadState.NotLoading -> {}
                LoadState.Loading -> {
                    binding.loadingPb.isVisible = true
                    binding.errorTextTv.isVisible = false
                }
                is LoadState.Error -> {
                    binding.loadingPb.isVisible = false
                    binding.errorTextTv.also {
                        it.text = "Error load more"
                        it.isVisible = true
                    }
                }
            }
        }
    }
}