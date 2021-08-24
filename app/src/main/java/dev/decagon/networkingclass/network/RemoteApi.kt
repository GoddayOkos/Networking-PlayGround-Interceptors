package dev.decagon.networkingclass.network

import android.os.Handler
import android.os.Looper
import dev.decagon.networkingclass.model.request.EmojiPhraseRequest
import dev.decagon.networkingclass.model.request.UserDataRequest
import dev.decagon.networkingclass.model.response.EmojiPhraseResponse
import dev.decagon.networkingclass.model.response.LoginResponse
import okhttp3.MultipartBody
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

const val BASE_URL = "https://emojiphraseapp.herokuapp.com"
//const val BASE_URL = "http://192.168.0.158:8080"

class RemoteApi(private val apiService: RemoteApiService) {

//    private val fakeResults = mutableListOf(
//        EmojiPhraseResponse(
//        id = 1, userId = "GoddayOkos", "\uD83D\uDC4F", "Clapping for myself"
//        ),
//        EmojiPhraseResponse(
//        id = 2, userId = "GoddayOkos", "\uD83C\uDF2C\uD83D\uDE04", "Mind Blown"
//        ),
//        EmojiPhraseResponse(
//        id = 3, userId = "GoddayOkos", "\uD83E\uDDCF\uD83C\uDFFD\u200D♂️", "If I hear"
//        ),
//        EmojiPhraseResponse(
//        id = 4, userId = "GoddayOkos", "\uD83D\uDC4F\uD83D\uDC4F\uD83D\uDC4F", "Clapping"
//        )
//    )
//
//    fun getFakeResult(): List<EmojiPhraseResponse> = fakeResults
//
//    fun addFakeEmojiPhrases(emojiPhraseRequest: EmojiPhraseRequest) {
//        val emojiPhraseResponse = EmojiPhraseResponse(
//            id = fakeResults.size.plus(1L),
//            userId = "GoddayOkos",
//            emoji = emojiPhraseRequest.emoji,
//            phrase = emojiPhraseRequest.phrase
//        )
//        fakeResults.add(emojiPhraseResponse)
//    }

    fun signInUser(
        userDataRequest: UserDataRequest,
        onError: (String) -> Unit,
        onUserSignedIn: (String) -> Unit
    ) {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("userId", userDataRequest.userId)
            .addFormDataPart("password", userDataRequest.password)
            .build()

        apiService.loginUser(requestBody).enqueue(object : Callback<LoginResponse> {
            override fun onResponse(call: Call<LoginResponse>, response: Response<LoginResponse>) {
                val signedInResponse = response.body()

                if (signedInResponse == null || signedInResponse.token.isEmpty()) {
                    onError("Invalid user, please ensure you entered correct credentials")
                } else {
                    onUserSignedIn(signedInResponse.token)
                }
            }

            override fun onFailure(call: Call<LoginResponse>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }

//    fun addEmojiPhrases(
//        emojiPhraseRequest: EmojiPhraseRequest,
//        onError: (String) -> Unit,
//        onEmojiRequestAdded: (emojiPhrase: EmojiPhraseResponse) -> Unit
//    ) {
//        apiService.addEmojiPhrase(emojiPhraseRequest)
//            .enqueue(object : Callback<EmojiPhraseResponse> {
//                override fun onResponse(
//                    call: Call<EmojiPhraseResponse>,
//                    response: Response<EmojiPhraseResponse>
//                ) {
//                    val data = response.body()
//
//                    if (data == null) {
//                        onError("No response!")
//                        return
//                    } else {
//                        onEmojiRequestAdded(data)
//                    }
//                }
//
//                override fun onFailure(call: Call<EmojiPhraseResponse>, t: Throwable) {
//                    onError(t.message.toString())
//                }
//
//            })
//    }

//    fun getEmojiPhrases(
//        onError: (String) -> Unit,
//        onEmojiPhrasesReceived: (List<EmojiPhraseResponse>) -> Unit
//    ) {
//        apiService.getEmojiPhrases().enqueue(object : Callback<List<EmojiPhraseResponse>> {
//            override fun onResponse(
//                call: Call<List<EmojiPhraseResponse>>,
//                response: Response<List<EmojiPhraseResponse>>
//            ) {
//                val data = response.body()
//
//                if (data != null) {
//                    onEmojiPhrasesReceived(data)
//                } else {
//                    onError("No emoji phrases to display!")
//                }
//            }
//
//            override fun onFailure(call: Call<List<EmojiPhraseResponse>>, t: Throwable) {
//                onError(t.message.toString())
//            }
//
//        })
//    }

    fun getEmojiPhrases(
        onError: (String) -> Unit,
        onEmojiPhrasesReceived: (List<EmojiPhraseResponse>) -> Unit
    ) {
        fakeRetrofit().getEmojiPhrases().enqueue(object : Callback<List<EmojiPhraseResponse>> {
            override fun onResponse(
                call: Call<List<EmojiPhraseResponse>>,
                response: Response<List<EmojiPhraseResponse>>
            ) {
                val data = response.body()

                if (data != null) {
                    onEmojiPhrasesReceived(data)
                } else {
                    onError("No emoji phrases to display!")
                }
            }

            override fun onFailure(call: Call<List<EmojiPhraseResponse>>, t: Throwable) {
                onError(t.message.toString())
            }

        })
    }

    fun addEmojiPhrases(
        emojiPhraseRequest: EmojiPhraseRequest,
        onError: (String) -> Unit,
        onEmojiRequestAdded: (emojiPhrase: EmojiPhraseResponse) -> Unit
    ) {
        fakeRetrofit().addEmojiPhrase(emojiPhraseRequest)
            .enqueue(object : Callback<EmojiPhraseResponse> {
                override fun onResponse(
                    call: Call<EmojiPhraseResponse>,
                    response: Response<EmojiPhraseResponse>
                ) {
                    val data = response.body()

                    if (data == null) {
                        onError("No response!")
                        return
                    } else {
                        onEmojiRequestAdded(data)
                    }
                }

                override fun onFailure(call: Call<EmojiPhraseResponse>, t: Throwable) {
                    onError(t.message.toString())
                }

            })
    }
}
