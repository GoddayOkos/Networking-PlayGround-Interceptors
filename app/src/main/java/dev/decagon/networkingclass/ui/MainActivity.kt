package dev.decagon.networkingclass.ui

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.ProgressBar
import android.widget.TextView
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import dev.decagon.networkingclass.App
import dev.decagon.networkingclass.R
import dev.decagon.networkingclass.adapter.EmojiPhrasesAdapter
import dev.decagon.networkingclass.model.request.EmojiPhraseRequest
import dev.decagon.networkingclass.model.response.EmojiPhraseResponse

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var progressBar: ProgressBar
    private lateinit var emptyListMsg: TextView
    private lateinit var alertDialog: AlertDialog

    companion object {
        fun getIntent(context: Context): Intent {
            val intent = Intent(context, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
            return intent
        }
    }

    private val remoteApi = App.remoteApi
    private val adapter by lazy { EmojiPhrasesAdapter() }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initViews()
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.main_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        return if (item.itemId == R.id.sign_out) {
            App.saveToken("")
            startActivity(LoginActivity.getIntent(this))
            true
        } else {
            super.onOptionsItemSelected(item)
        }
    }

    private fun initViews() {
        title = "EmojiPhrases"
        recyclerView = findViewById(R.id.recycler_view)
        recyclerView.adapter = adapter
        swipeContainer = findViewById(R.id.swipe_container)
        swipeContainer.setColorSchemeColors(
            resources.getColor(android.R.color.holo_blue_bright, null),
            resources.getColor(android.R.color.holo_green_light, null),
            resources.getColor(android.R.color.holo_orange_light, null),
            resources.getColor(android.R.color.holo_red_light, null)
        )
        swipeContainer.setOnRefreshListener { getEmojiPhrases() }

        progressBar = findViewById(R.id.progress_bar)
        emptyListMsg = findViewById(R.id.empty_list_text)

        fab = findViewById(R.id.fab)
        fab.setOnClickListener { showAddEmojiPhrasesDialog() }
        getEmojiPhrases()
    }

    private fun showAddEmojiPhrasesDialog() {
        val dialogView = layoutInflater.inflate(R.layout.dialog_layout, null)
        val emojiInput = dialogView.findViewById<TextInputEditText>(R.id.emoji)
        val phraseInput = dialogView.findViewById<TextInputEditText>(R.id.phrase)

        alertDialog = MaterialAlertDialogBuilder(this)
            .setTitle("Add new emojiPhrase")
            .setView(dialogView)
            .setCancelable(false)
            .setNegativeButton("CANCEL") { _, _ -> }
            .setPositiveButton("DONE") { _, _ ->
                val emoji = emojiInput.text.toString()
                val phrase = phraseInput.text.toString()

                if (emoji.isNotBlank() && phrase.isNotBlank()) {
                    swipeContainer.isRefreshing = true
                    val emojiPhrase = EmojiPhraseRequest(emoji, phrase)
                    remoteApi.addEmojiPhrases(
                        emojiPhrase,
                        ::onError
                    ) {
                        Snackbar.make(
                            swipeContainer,
                            "New emojiPhrase added!\uD83D\uDC4F\uD83D\uDE04",
                            Snackbar.LENGTH_LONG
                        ).show()
                        getEmojiPhrases()
                    }

                } else {
                    Snackbar.make(swipeContainer, "Fields must not be empty!", Snackbar.LENGTH_LONG)
                        .setBackgroundTint(
                            resources.getColor(
                                R.color.design_default_color_error,
                                null
                            )
                        )
                        .show()
                }

            }.show()
    }

    private fun onError(message: String) {
        swipeContainer.isRefreshing = false
        Snackbar.make(swipeContainer, message, Snackbar.LENGTH_LONG).show()
    }

    private fun getEmojiPhrases() {
        swipeContainer.isRefreshing = true
        remoteApi.getEmojiPhrases(::onError) {
            swipeContainer.isRefreshing = false
            adapter.submitList(it)
            if (it.isEmpty()) {
                emptyListMsg.visibility = View.VISIBLE
            } else {
                emptyListMsg.visibility = View.GONE
            }
        }
    }
}