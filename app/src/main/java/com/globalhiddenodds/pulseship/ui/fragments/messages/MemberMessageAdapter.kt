package com.globalhiddenodds.pulseship.ui.fragments.messages

import android.graphics.Color
import android.util.Base64
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView.*
import com.bumptech.glide.Glide
import com.globalhiddenodds.pulseship.R
import com.globalhiddenodds.pulseship.datasource.database.Message
import com.globalhiddenodds.pulseship.datasource.database.getFormattedDate
import de.hdodenhof.circleimageview.CircleImageView

class MemberMessageAdapter(private val currentUserName: String?):
    ListAdapter<Message, ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        return if (viewType == VIEW_TYPE_TEXT) {
            val view = inflater.inflate(R.layout.message, parent, false)

            MessageViewHolder(view)
        } else {
            val view = inflater.inflate(R.layout.image_message, parent, false)
            ImageMessageViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val current = getItem(position)

        if (current.message.isNotEmpty()) {
            (holder as MessageViewHolder).bind(current)
        } else {
            (holder as ImageMessageViewHolder).bind(current)
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (getItem(position).message.isNotEmpty()) VIEW_TYPE_TEXT else VIEW_TYPE_IMAGE
    }

    inner class MessageViewHolder(itemView: View) : ViewHolder(itemView) {
        fun bind(item: Message) {
            itemView.findViewById<TextView>(R.id.messageTextView).text = item.message
            setTextColor(item.name, itemView.findViewById(R.id.messageTextView))
            val label = "${item.name} ${item.getFormattedDate()}"
            itemView.findViewById<TextView>(R.id.messengerTextView).text = label

            if (item.name == currentUserName) {
                itemView.findViewById<CircleImageView>(R.id.messengerImageView).setImageResource(R.mipmap.ic_account_local)

            } else {
                itemView.findViewById<CircleImageView>(R.id.messengerImageView).setImageResource(R.mipmap.ic_account_contact)
            }

            /*if (item.photoB64.isNotEmpty()) {
                loadImageIntoView(itemView.findViewById(R.id.messengerImageView), item.photoB64)

            } else {
                itemView.findViewById<CircleImageView>(R.id.messengerImageView).setImageResource(R.drawable.ic_account)
            }*/
        }

        private fun setTextColor(userName: String?, textView: TextView) {
            if (userName != null && currentUserName == userName) {
                textView.setBackgroundResource(R.drawable.rounded_message_local)
                textView.setTextColor(Color.WHITE)
            } else {
                textView.setBackgroundResource(R.drawable.rounded_message_contact)
                textView.setTextColor(Color.BLACK)
            }
        }
    }

    inner class ImageMessageViewHolder(itemView: View) :
        ViewHolder(itemView) {
        fun bind(item: Message) {
            loadImageIntoView(itemView.findViewById(R.id.messageImageView), item.image)
            val label = "${item.name} ${item.getFormattedDate()}"
            itemView.findViewById<TextView>(R.id.messengerTextView).text = label

            if (item.name == currentUserName) {
                itemView.findViewById<CircleImageView>(R.id.messengerImageView).setImageResource(R.mipmap.ic_account_local)

            } else {
                itemView.findViewById<CircleImageView>(R.id.messengerImageView).setImageResource(R.mipmap.ic_account_contact)
            }
        }
    }

    private fun loadImageIntoView(view: ImageView, img64: String) {
        Glide.with(view.context)
            .load(Base64.decode(img64, Base64.DEFAULT))
            .into(view)
    }

    companion object {
        const val VIEW_TYPE_TEXT = 1
        const val VIEW_TYPE_IMAGE = 2
        private val DiffCallback = object : DiffUtil.ItemCallback<Message>() {
            override fun areItemsTheSame(oldMessage: Message, newMessage: Message): Boolean {
                return oldMessage.id == newMessage.id
            }

            override fun areContentsTheSame(oldMessage: Message, newMessage: Message): Boolean {
                return oldMessage == newMessage
            }
        }
    }
}