package com.howdiedoodies.chatterby.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.howdiedoodies.chatterby.R
import com.howdiedoodies.chatterby.data.Favorite
import com.howdiedoodies.chatterby.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onClick: (Favorite) -> Unit
) : ListAdapter<Favorite, FavoriteAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    inner class ViewHolder(private val binding: ItemFavoriteBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(favorite: Favorite) {
            binding.usernameText.text = favorite.username
            binding.subjectText.text = favorite.subject
            binding.ageGenderText.text = "${favorite.age} ${favorite.gender?.uppercase()}"
            binding.locationText.text = favorite.location
            if (favorite.isOnline) {
                binding.statusBadge.text = "LIVE"
                binding.statusBadge.setTextColor(itemView.context.getColor(R.color.md_theme_light_primary))
            } else {
                binding.statusBadge.text = favorite.lastOnline?.let {
                    DateUtils.getRelativeTimeSpanString(it, System.currentTimeMillis(), DateUtils.MINUTE_IN_MILLIS)
                } ?: "Offline"
                binding.statusBadge.setTextColor(itemView.context.getColor(R.color.md_theme_light_onSurfaceVariant))
            }
            Glide.with(itemView.context)
                .load(favorite.thumbnailPath)
                .placeholder(R.drawable.ic_avatar_placeholder)
                .into(binding.thumbnail)
            binding.root.setOnClickListener {
                onClick(favorite)
            }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem.username == newItem.username
        }

        override fun areContentsTheSame(oldItem: Favorite, newItem: Favorite): Boolean {
            return oldItem == newItem
        }
    }
}
