package dev.decagon.networkingclass.network

import dev.decagon.networkingclass.model.request.EmojiPhraseRequest
import dev.decagon.networkingclass.model.request.UserDataRequest
import dev.decagon.networkingclass.model.response.EmojiPhraseResponse
import dev.decagon.networkingclass.model.response.LoginResponse
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*
import rx.Observable

interface RemoteApiService {

    @POST("/login")
    fun loginUser(@Body userData: RequestBody): Call<LoginResponse>

    @POST("/api/v1/phrases")
    fun addEmojiPhrase(@Body emojiPhrase: EmojiPhraseRequest): Observable<EmojiPhraseResponse>

    @GET("/api/v1/phrases")
    fun getEmojiPhrases(): Observable<List<EmojiPhraseResponse>>
}