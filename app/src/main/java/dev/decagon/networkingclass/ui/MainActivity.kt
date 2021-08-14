package dev.decagon.networkingclass.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.swiperefreshlayout.widget.SwipeRefreshLayout
import com.google.android.material.floatingactionbutton.FloatingActionButton
import dev.decagon.networkingclass.R

class MainActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var swipeContainer: SwipeRefreshLayout
    private lateinit var fab: FloatingActionButton
    private lateinit var emptyListMsg: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        initViews()
    }

    private fun initViews() {
        title = "EmojiPhrases"
        recyclerView = findViewById(R.id.recycler_view)
        swipeContainer = findViewById(R.id.swipe_container)
        fab = findViewById(R.id.fab)
        emptyListMsg = findViewById(R.id.empty_list_text)
    }
}