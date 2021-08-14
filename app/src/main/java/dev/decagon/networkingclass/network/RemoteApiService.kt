package dev.decagon.networkingclass.network

import dev.decagon.networkingclass.model.request.EmojiPhraseRequest
import dev.decagon.networkingclass.model.request.UserDataRequest
import dev.decagon.networkingclass.model.response.EmojiPhraseResponse
import dev.decagon.networkingclass.model.response.LoginResponse
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface RemoteApiService {

    @POST("/login")
    fun loginUser(@Body userData: UserDataRequest): Call<LoginResponse>

    @POST("/api/v1/phrases")
    fun addEmojiPhrase(@Body emojiPhrase: EmojiPhraseRequest): Call<EmojiPhraseResponse>

    @GET("/api/v1/phrases")
    fun getEmojiPhrases(): Call<List<EmojiPhraseResponse>>
}