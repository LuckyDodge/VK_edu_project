package com.example.tosh

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import com.example.tosh.ui.theme.ToshTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ToshTheme {
                val context = LocalContext.current
                var text by rememberSaveable { mutableStateOf("") }

                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(innerPadding)
                            .padding(16.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                    ) {
                        Text(text = getString(R.string.main_hint))

                        OutlinedTextField(
                            value = text,
                            onValueChange = { text = it },
                            modifier = Modifier.fillMaxWidth(),
                            label = { Text(getString(R.string.input_label)) },
                            singleLine = true,
                        )

                        Button(
                            onClick = {
                                val trimmed = text.trim()
                                if (trimmed.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        R.string.error_empty_text,
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    return@Button
                                }
                                startActivity(
                                    Intent(context, SecondActivity::class.java).apply {
                                        putExtra(EXTRA_TEXT, trimmed)
                                    },
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(getString(R.string.open_second_activity))
                        }

                        Button(
                            onClick = {
                                val phone = normalizePhone(text)
                                if (!isValidPhoneForDial(phone)) {
                                    Toast.makeText(
                                        context,
                                        R.string.error_invalid_phone,
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    return@Button
                                }
                                val dialIntent = Intent(Intent.ACTION_DIAL).apply {
                                    data = Uri.parse("tel:$phone")
                                }
                                startActivity(dialIntent)
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(getString(R.string.call_friend))
                        }

                        Button(
                            onClick = {
                                val trimmed = text.trim()
                                if (trimmed.isEmpty()) {
                                    Toast.makeText(
                                        context,
                                        R.string.error_empty_text,
                                        Toast.LENGTH_SHORT,
                                    ).show()
                                    return@Button
                                }
                                val shareIntent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_TEXT, trimmed)
                                }
                                startActivity(
                                    Intent.createChooser(
                                        shareIntent,
                                        getString(R.string.share_chooser_title),
                                    ),
                                )
                            },
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Text(getString(R.string.share_text))
                        }
                    }
                }
            }
        }
    }

    companion object {
        const val EXTRA_TEXT = "extra_text"

        private fun normalizePhone(input: String): String {
            return input.filter { it.isDigit() || it == '+' || it == '*' || it == '#' || it == ',' }
        }

        private fun isValidPhoneForDial(phone: String): Boolean {
            if (phone.isEmpty()) return false
            val digitCount = phone.count { it.isDigit() }
            return digitCount >= 3
        }
    }
}
