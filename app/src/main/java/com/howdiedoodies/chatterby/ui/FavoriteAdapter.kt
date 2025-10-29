package com.howdiedoodies.chatterby.ui

import android.text.format.DateUtils
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.howdiedoodies.chatterby.R
import com.howdiedoodies.chatterby.data.Favorite

class FavoriteAdapter(
    private val onClick: (String, Boolean) -> Unit
) : ListAdapter<Favorite, FavoriteAdapter.VH>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VH {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_favorite, parent, false)
        return VH(view, onClick)
    }

    override fun onBindViewHolder(holder: VH, position: Int) = holder.bind(getItem(position))

    class VH(
        itemView: View,
        private val onClick: (String, Boolean) -> Unit
    ) : RecyclerView.ViewHolder(itemView) {
        private val username: TextView = itemView.findViewById(R.id.username)
        private val status: TextView = itemView.findViewById(R.id.status)
        private val btnOpen: Button = itemView.findViewById(R.id.btnOpen)
        private val chatOnly: Button = itemView.findViewById(R.id.chatOnly)

        fun bind(fav: Favorite) {
            username.text = fav.username
            val now = System.currentTimeMillis()
            val last = fav.lastOnline
            if (last != null && now - last < 120_000) {
                status.text = "LIVE"
                status.setTextColor(itemView.context.getColor(android.R.color.holo_green_dark))
            } else {
                status.text = last?.let { DateUtils.getRelativeTimeSpanString(it, now, DateUtils.MINUTE_IN_MILLIS) } ?: "Never"
                status.setTextColor(itemView.context.getColor(android.R.color.darker_gray))
            }
            btnOpen.setOnClickListener { onClick(fav.username, false) }
            chatOnly.setOnClickListener { onClick(fav.username, true) }
        }
    }

    object DiffCallback : DiffUtil.ItemCallback<Favorite>() {
        override fun areItemsTheSame(old: Favorite, new: Favorite) = old.username == new.username
        override fun areContentsTheSame(old: Favorite, new: Favorite) = old == new
    }
}
