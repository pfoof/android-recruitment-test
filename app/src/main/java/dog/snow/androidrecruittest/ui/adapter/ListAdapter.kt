package dog.snow.androidrecruittest.ui.adapter

import android.content.res.Configuration
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.engine.DiskCacheStrategy
import dog.snow.androidrecruittest.R
import dog.snow.androidrecruittest.ui.model.ListItem

class ListAdapter(private val onClick: (item: ListItem, position: Int, view: View) -> Unit) :
    androidx.recyclerview.widget.ListAdapter<ListItem, ListAdapter.ViewHolder>(DIFF_CALLBACK) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.list_item, parent, false)
        return ViewHolder(itemView, onClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) =
        holder.bind(getItem(position))

    class ViewHolder(
        itemView: View,
        private val onClick: (item: ListItem, position: Int, view: View) -> Unit
    ) :
        RecyclerView.ViewHolder(itemView) {
        fun bind(item: ListItem) = with(itemView) {
            val ivThumb: ImageView = findViewById(R.id.iv_thumb)
            val tvTitle: TextView = findViewById(R.id.tv_photo_title)
            val tvAlbumTitle: TextView = findViewById(R.id.tv_album_title)
            tvTitle.text = item.title
            tvAlbumTitle.text = item.albumTitle

            val isNight = (context.resources.configuration.uiMode and Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES
            val placeholderResource = if(isNight) R.drawable.ic_placeholder_dark else R.drawable.ic_placeholder

            Glide.with(this.context)
                .load(item.thumbnailUrl)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(placeholderResource)
                .into(ivThumb)

            setOnClickListener { onClick(item, adapterPosition, this) }
        }
    }

    companion object {
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<ListItem>() {
            override fun areItemsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem.id == newItem.id

            override fun areContentsTheSame(oldItem: ListItem, newItem: ListItem): Boolean =
                oldItem == newItem
        }
    }
}