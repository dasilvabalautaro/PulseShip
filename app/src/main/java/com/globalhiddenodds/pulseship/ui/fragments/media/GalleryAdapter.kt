package com.globalhiddenodds.pulseship.ui.fragments.media

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.globalhiddenodds.pulseship.R

class GalleryAdapter(private val onClick: (MediaStoreImage) -> Unit) :
    ListAdapter<MediaStoreImage, GalleryAdapter.ImageViewHolder>(MediaStoreImage.DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ImageViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_gallery, parent, false)
        return ImageViewHolder(view, onClick)
    }

    override fun onBindViewHolder(holder: ImageViewHolder, position: Int) {
        val mediaStoreImage = getItem(position)
        holder.rootView.tag = mediaStoreImage

        Glide.with(holder.imageView)
            .load(mediaStoreImage.contentUri)
            .thumbnail(0.33f)
            .apply(RequestOptions()
                .placeholder(R.drawable.loading_animation)
                .error(R.drawable.ic_broken_image))
            .centerCrop()
            .into(holder.imageView)
    }

    inner class ImageViewHolder(view: View, onClick: (MediaStoreImage) -> Unit) :
        RecyclerView.ViewHolder(view) {
        val rootView = view
        val imageView: ImageView = view.findViewById(R.id.image)

        init {
            imageView.setOnClickListener {
                val image = rootView.tag as? MediaStoreImage ?: return@setOnClickListener
                onClick(image)
            }
        }
    }
}