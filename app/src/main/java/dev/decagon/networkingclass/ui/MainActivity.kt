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
import dev.decagon.networkingclass.model.request.EmojiPhraseRequest

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
        swipeContainer = findViewById(R.id.swipe_container)
        fab = findViewById(R.id.fab)
        progressBar = findViewById(R.id.progress_bar)
        emptyListMsg = findViewById(R.id.empty_list_text)

        fab.setOnClickListener { showAddEmojiPhrasesDialog() }
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
                    progressBar.visibility = View.VISIBLE
                    val emojiPhrase = EmojiPhraseRequest(emoji, phrase)
                    remoteApi.addEmojiPhrases(
                        emojiPhrase,
                        ::onError
                    ) {
                        progressBar.visibility = View.GONE
                        Snackbar.make(
                            swipeContainer,
                            "New emojiPhrase added! by ${it.userId}",
                            Snackbar.LENGTH_LONG
                        ).show()
                    }

                } else {
                    emojiInput.error = "This field must not be empty"
                    phraseInput.error = "This field must not be empty"
                }

            }.show()
    }

    private fun onError(message: String) {
        progressBar.visibility = View.GONE
        Snackbar.make(swipeContainer, message, Snackbar.LENGTH_LONG).show()
    }
}