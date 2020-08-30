package dog.snow.androidrecruittest.ui.adapter

import android.graphics.drawable.Drawable
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.DataSource
import com.bumptech.glide.load.engine.DiskCacheStrategy
import com.bumptech.glide.load.engine.GlideException
import com.bumptech.glide.load.model.GlideUrl
import com.bumptech.glide.load.model.LazyHeaders
import com.bumptech.glide.request.RequestListener
import com.bumptech.glide.request.target.Target
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

            Glide.with(this.context)
                .load(GlideUrl(item.thumbnailUrl, lazyHeaders))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.ic_placeholder)
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

        private val lazyHeaders = LazyHeaders.Builder()
            .addHeader("User-Agent", "Mozilla/5.0 Snow Dog User Agent 1.0 (Android)")
            .addHeader("App-Agent", "Mozilla/5.0 Snow Dog App Agent 1.0 (Android)")
            .build()
    }
}