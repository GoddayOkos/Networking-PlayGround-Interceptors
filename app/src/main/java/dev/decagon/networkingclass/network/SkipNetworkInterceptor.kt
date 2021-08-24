package dev.decagon.networkingclass.network

import com.google.gson.Gson
import dev.decagon.networkingclass.model.request.EmojiPhraseRequest
import dev.decagon.networkingclass.model.response.EmojiPhraseResponse
import okhttp3.*
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.ResponseBody.Companion.toResponseBody
import okio.Buffer
import java.nio.charset.Charset

private val fakeNetworkResults = mutableListOf(
    EmojiPhraseResponse(
        id = 1, userId = "GoddayOkos", "\uD83D\uDC4F", "Clapping for myself"
    ),
    EmojiPhraseResponse(
        id = 2, userId = "GoddayOkos", "\uD83C\uDF2C\uD83D\uDE04", "Mind Blown"
    )
)

class SkipNetworkInterceptor: Interceptor {

    private val gson = Gson()

    override fun intercept(chain: Interceptor.Chain): Response {
        pretendToBlockForNetworkRequest()
        return if (chain.request().method == "POST") {
            makePostRequest(chain.request())
        } else {
            makeGetResult(chain.request())
        }

    }

    private fun pretendToBlockForNetworkRequest() = Thread.sleep(500)

    private fun makeGetResult(request: Request): Response {
        return Response.Builder()
            .code(200)
            .request(request)
            .protocol(Protocol.HTTP_1_1)
            .message("OK")
            .body(
                gson.toJson(fakeNetworkResults)
                    .toResponseBody("application/json".toMediaType())
            )
            .build()
    }

    private fun makePostRequest(request: Request): Response {
        val copy = request.newBuilder().build()
        val requestObject: RequestBody? = copy.body
        if (requestObject != null) {
            val buffer = Buffer()
            requestObject.writeTo(buffer)

            val emojiPhraseRequest = gson.fromJson(buffer.readString(Charset.defaultCharset()),
                EmojiPhraseRequest::class.java)

            val emojiPhraseResponse = EmojiPhraseResponse(
                id = fakeNetworkResults.size.plus(1L),
                userId = "GoddayOkos",
                emoji = emojiPhraseRequest.emoji,
                phrase = emojiPhraseRequest.phrase
            )

            fakeNetworkResults.add(emojiPhraseResponse)

            return Response.Builder()
                .code(200)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("OK")
                .body(gson.toJson(fakeNetworkResults[0])
                    .toResponseBody("application/json".toMediaType()))
                .build()
        } else {
            return Response.Builder()
                .code(500)
                .request(request)
                .protocol(Protocol.HTTP_1_1)
                .message("Internal Server Error")
                .build()
        }
    }
}