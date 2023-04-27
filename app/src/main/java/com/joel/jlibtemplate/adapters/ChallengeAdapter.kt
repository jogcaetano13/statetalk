package com.joel.jlibtemplate.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.joel.jlibtemplate.databinding.ItemChallengeBinding
import com.joel.jlibtemplate.models.Challenge

class ChallengeAdapter : PagingDataAdapter<Challenge, ChallengeAdapter.ViewHolder>(object : DiffUtil.ItemCallback<Challenge>() {
    override fun areItemsTheSame(oldItem: Challenge, newItem: Challenge): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: Challenge, newItem: Challenge): Boolean =
        oldItem == newItem
}) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemChallengeBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    inner class ViewHolder(private val binding: ItemChallengeBinding) : RecyclerView.ViewHolder(binding.root) {

        fun bind(challenge: Challenge?) {
            binding.root.text = challenge?.name
        }
    }
}