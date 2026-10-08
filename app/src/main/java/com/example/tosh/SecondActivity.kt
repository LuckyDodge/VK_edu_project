package com.example.tosh

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.tosh.ui.theme.ToshTheme

class SecondActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        val message = intent.getStringExtra(MainActivity.EXTRA_TEXT).orEmpty()

        setContent {
            ToshTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Text(
                        text = message.ifBlank { getString(R.string.second_activity_empty_text) },
                        style = MaterialTheme.typography.bodyLarge,
                        modifier = Modifier
                            .padding(innerPadding)
                            .padding(16.dp),
                    )
                }
            }
        }
    }
}
