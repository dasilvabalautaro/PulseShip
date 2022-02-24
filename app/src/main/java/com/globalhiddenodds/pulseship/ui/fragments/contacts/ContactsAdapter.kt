package com.globalhiddenodds.pulseship.ui.fragments.contacts

import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.datasource.database.SSetCountNewMessages
import de.hdodenhof.circleimageview.CircleImageView

class ContactsAdapter(private val onItemClicked: (SSetCountNewMessages) -> Unit):
    ListAdapter<SSetCountNewMessages, RecyclerView.ViewHolder>(DiffCallback)
{
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder{
        val inflater = LayoutInflater.from(parent.context)
        val view = inflater.inflate(R.layout.item_contact, parent, false)
        return ContactsViewHolder(view)
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val current = getItem(position)
        holder.itemView.setOnClickListener {
            onItemClicked(current)
        }
        (holder as ContactsViewHolder).bind(current)
    }

    inner class ContactsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        fun bind(item: SSetCountNewMessages) {
            itemView.findViewById<TextView>(R.id.txtName).text = item.name
            itemView.findViewById<TextView>(R.id.txtCount).text = item.quantity.toString()

            if (item.photo.isNotEmpty()) {
                loadImageIntoView(itemView.findViewById(R.id.imageContact), item.photo)
            } else {
                itemView.findViewById<CircleImageView>(R.id.imageContact).setImageResource(R.mipmap.ic_account_local)
            }
        }
    }

    private fun loadImageIntoView(view: ImageView, img64: String) {
        Glide.with(view.context)
            .load(Base64.decode(img64, Base64.DEFAULT))
            .into(view)
    }

    companion object {
        private val DiffCallback = object : DiffUtil.ItemCallback<SSetCountNewMessages>() {
            override fun areItemsTheSame(oldMessage: SSetCountNewMessages, newMessage: SSetCountNewMessages): Boolean {
                return oldMessage.source == newMessage.source
            }

            override fun areContentsTheSame(oldMessage: SSetCountNewMessages, newMessage: SSetCountNewMessages): Boolean {
                return oldMessage == newMessage
            }
        }
    }
}