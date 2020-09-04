package com.example.smartreplysample

import android.content.Context
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import androidx.appcompat.app.AppCompatActivity
import com.google.mlkit.nl.smartreply.SmartReply
import com.google.mlkit.nl.smartreply.SmartReplySuggestionResult
import com.google.mlkit.nl.smartreply.TextMessage
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    val conversations = mutableListOf<TextMessage>()
    val smartReplyGenerator = SmartReply.getClient()
    val context: Context = this
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        button.setOnClickListener {
            val text = etName.text.toString()
            conversations.clear()
            conversations.add(
                    TextMessage.createForRemoteUser(
                            text,
                            System.currentTimeMillis(),
                            "user1"
                    )
            )
            generateSmartReply()
        }

        clear.setOnClickListener {
            etName.setText("")
        }
    }


    fun generateSmartReply() {

        smartReplyGenerator.suggestReplies(conversations)
                .addOnSuccessListener { result ->
                    if (result.getStatus() == SmartReplySuggestionResult.STATUS_NOT_SUPPORTED_LANGUAGE) {
                        // The conversation's language isn't supported, so
                        // the result doesn't contain any suggestions.
                    } else if (result.getStatus() == SmartReplySuggestionResult.STATUS_SUCCESS) {
                        // Task completed successfully
                        // ..
                        val textList = result.suggestions.map {
                            it.text
                        }

                        val arrayAdapter = ArrayAdapter<String>(
                                context, android.R.layout.simple_expandable_list_item_1, textList
                        )

                        listView.adapter = arrayAdapter
                        Log.e("called", result.suggestions.toString())
                    }
                }
                .addOnFailureListener {
                    // Task failed with an exception

                    Log.e("Exception", it.message.toString())
                    // ...
                }
    }

}