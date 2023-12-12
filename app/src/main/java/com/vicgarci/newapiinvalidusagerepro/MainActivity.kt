package com.vicgarci.newapiinvalidusagerepro

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            Surface(
                modifier = Modifier.wrapContentSize(),
                color = MaterialTheme.colorScheme.background
            ) {
                Button(onClick = { runRegex() }) {
                    Text(text = "Run regex!")
                }
            }
        }
    }

    private fun runRegex(): String? {
        val myRegex = Regex("title=(?<title>.*)")

        fun getTitle(from: String): String? {
            val titleMatch = myRegex.find(from) ?: return null
            val titleGroup = titleMatch.groups["title"] ?: return null
            return titleGroup.value
        }

        return getTitle("title=Is this an Android Lint bug?")
    }
}