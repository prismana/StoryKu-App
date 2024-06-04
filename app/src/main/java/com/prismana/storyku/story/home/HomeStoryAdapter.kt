package com.prismana.storyku.story.home

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.Toast
import androidx.core.app.ActivityOptionsCompat
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.prismana.storyku.R
import com.prismana.storyku.data.remote.response.StoryResponse
import com.prismana.storyku.databinding.StoryItemBinding
import com.prismana.storyku.story.detail.DetailStoryActivity

class HomeStoryAdapter(
    private val context: Context,
    private val listStory: ArrayList<StoryResponse.ListStoryItem> = arrayListOf()
) : RecyclerView.Adapter<HomeStoryAdapter.ListViewHolder>(){

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ListViewHolder {
        val binding = StoryItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ListViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ListViewHolder, position: Int) {
        val user = listStory[position]
        holder.bind(user)
    }

    fun setStoryData(storyData: List<StoryResponse.ListStoryItem>) {
        this.listStory.clear()
        this.listStory.addAll(storyData)
    }

    class ListViewHolder(private val binding: StoryItemBinding) : RecyclerView.ViewHolder(binding.root) {
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

    override fun getItemCount(): Int = listStory.size

    companion object {

    }
}