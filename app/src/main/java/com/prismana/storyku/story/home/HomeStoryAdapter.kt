package com.prismana.storyku.story.home

import android.app.Activity
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prismana.storyku.R
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.databinding.StoryItemBinding
import com.prismana.storyku.story.detail.DetailStoryActivity

class HomeStoryAdapter : PagingDataAdapter<StoryResponse.ListStoryItem, HomeStoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): StoryViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return StoryViewHolder(binding)
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        val user = getItem(position)
        if (user != null) {
            holder.bind(user)
        }

    }

    class StoryViewHolder(private val binding: StoryItemBinding): RecyclerView.ViewHolder(binding.root) {
        fun bind(storyItem: StoryResponse.ListStoryItem) {
            Glide.with(binding.root)
                .load(storyItem.photoUrl)
                .error(R.drawable.ic_launcher_background)
                .into(binding.itemImage)
            binding.itemTitle.text = storyItem.name
            binding.itemDescription.text = storyItem.description

            itemView.setOnClickListener {
                Toast.makeText(itemView.context, "${storyItem.name}", Toast.LENGTH_SHORT).show()
                val detailStoryIntent = Intent(itemView.context, DetailStoryActivity::class.java)
                detailStoryIntent.putExtra(DetailStoryActivity.EXTRA_STORY, storyItem)
                itemView.context.startActivity(detailStoryIntent, ActivityOptionsCompat.makeSceneTransitionAnimation(itemView.context as Activity).toBundle())
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<StoryResponse.ListStoryItem>() {
            override fun areItemsTheSame(oldItem: StoryResponse.ListStoryItem, newItem: StoryResponse.ListStoryItem): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: StoryResponse.ListStoryItem, newItem: StoryResponse.ListStoryItem): Boolean {
                return oldItem.id == newItem.id
            }
        }
    }
}