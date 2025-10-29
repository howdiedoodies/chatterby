package com.howdiedoodies.chatterby.ui

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.howdiedoodies.chatterby.data.Favorite
import com.howdiedoodies.chatterby.databinding.ItemFavoriteBinding

class FavoriteAdapter(
    private val onClick: (String, Boolean) -> Unit
) : ListAdapter<Favorite, FavoriteAdapter.VH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val binding = ItemFavoriteBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VH(binding, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(
        private val binding: ItemFavoriteBinding,
        private val onClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(fav: Favorite) {
            binding.usernameText.text = fav.username
            binding.ageText.text = fav.age?.toString()
            binding.locationText.text = fav.location
            binding.genderIcon.setImageResource(
                when (fav.gender) {
                    "m" -> R.drawable.ic_male
                    "f" -> R.drawable.ic_female
                    "t" -> R.drawable.ic_trans
                    else -> R.drawable.ic_other
                }
            )
            val now = System.currentTimeMillis()
            val last = fav.lastOnline
            if (fav.isOnline) {
                binding.statusBadge.text = "LIVE"
                binding.statusBadge.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                binding.statusBadge.text = last?.let { DateUtils.getRelativeTimeSpanString(it, now, DateUtils.MINUTE_IN_MILLIS) } ?: "Never"
                binding.statusBadge.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }
            binding.root.setOnClickListener { onClick(fav.username, false) }
            binding.chatOnlyButton.setOnClickListener { onClick(fav.username, true) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(old: Favorite, new: Favorite) = old.username == new.username
        override fun areContentsTheSame(old: Favorite, new: Favorite) = old == new
    }
}
