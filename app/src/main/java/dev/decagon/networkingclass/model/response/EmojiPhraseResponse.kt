package dev.decagon.networkingclass.model.response

data class EmojiPhraseResponse(
    val id: Long,
    val userId: String,
    val emoji: String,
    val phrase: String
)