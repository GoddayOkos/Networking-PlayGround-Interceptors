package dev.decagon.networkingclass.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import dev.decagon.networkingclass.R
import dev.decagon.networkingclass.model.quote.QuoteGenerator
import dev.decagon.networkingclass.model.response.EmojiPhraseResponse

class EmojiPhrasesAdapter :
    ListAdapter<EmojiPhraseResponse, EmojiPhrasesAdapter.ViewHolder>(EmojiPhrasesDiffCall()) {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val userName = view.findViewById<TextView>(R.id.user)
        private val phrase = view.findViewById<TextView>(R.id.title)
        private val emoji = view.findViewById<TextView>(R.id.comment)
        private val randomQuote = view.findViewById<TextView>(R.id.post)
        private val userImage = view.findViewById<ImageView>(R.id.profileImage)

        fun onBind(emojiPhrase: EmojiPhraseResponse) {
            if (emojiPhrase.userId == "GoddayOkos") {
                userImage.setImageResource(R.drawable.godday_okos)
            }

            val quote = QuoteGenerator.getQuote(emojiPhrase.id.toInt())
            userName.text = emojiPhrase.userId
            phrase.text = emojiPhrase.phrase
            emoji.text =
                String.format("${quote.text}\n\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t\t" +
                    "\t\t\t\t\t\t\t\t\t\t\t\t\t\t-${quote.author}")
            randomQuote.text = emojiPhrase.emoji
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.recycler_view_items, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.onBind(item)
    }
}


class EmojiPhrasesDiffCall : DiffUtil.ItemCallback<EmojiPhraseResponse>() {
    override fun areItemsTheSame(
        oldItem: EmojiPhraseResponse,
        newItem: EmojiPhraseResponse
    ): Boolean = oldItem.id == newItem.id

    override fun areContentsTheSame(
        oldItem: EmojiPhraseResponse,
        newItem: EmojiPhraseResponse
    ): Boolean = oldItem == newItem

}