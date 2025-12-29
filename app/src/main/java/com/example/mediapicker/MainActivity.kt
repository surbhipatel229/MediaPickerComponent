package com.example.mediapicker

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.mediapicker.ui.theme.MediaPickerTheme
import com.example.mediapickerlib.MediaPicker
import com.example.mediapickerlib.MediaPickerResult

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            MediaPickerTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    MediaPickerExample(
                        modifier = Modifier.padding(innerPadding)
                    )
                }
            }
        }
    }
}

@Composable
fun MediaPickerExample(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null) }
    var resultMessage by remember { mutableStateOf<String?>(null) }
    var selectedDocumentUri by remember { mutableStateOf<Uri?>(null) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "Media Picker Library Demo",
            style = MaterialTheme.typography.headlineMedium,
            modifier = Modifier.padding(bottom = 32.dp)
        )
        Button(onClick = { showDialog = true }) {
            Text("Open Media Picker")
        }

        selectedImageUri?.let { uri ->
            Spacer(modifier = Modifier.height(16.dp))
            AsyncImage(
                model = uri,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(240.dp),
                contentScale = ContentScale.Crop
            )
        }

        selectedDocumentUri?.let { uri ->
            Spacer(modifier = Modifier.height(16.dp))

            TextButton(
                onClick = {
                    try {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            setDataAndType(uri, context.contentResolver.getType(uri))
                            addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION)
                        }
                        context.startActivity(intent)
                    } catch (e: Exception) {
                        Toast.makeText(
                            context,
                            "No app found to open this file",
                            Toast.LENGTH_SHORT
                        ).show()
                    }
                }
            ) {
                Text(
                    text = "View Document",
                    style = MaterialTheme.typography.bodyLarge
                )
            }
        }

        resultMessage?.let {
            Text(
                text = it,
                modifier = Modifier.padding(top = 12.dp)
            )
        }
    }

    // Media Picker Dialog
    MediaPicker(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onResult = { result ->
            showDialog = false
            when (result) {
                is MediaPickerResult.CameraPhoto -> {
                    selectedImageUri = result.uri
                    selectedDocumentUri = null
                    resultMessage = "Camera Photo Selected"
                }

                is MediaPickerResult.GalleryImage -> {
                    selectedImageUri = result.uri
                    selectedDocumentUri = null
                    resultMessage = "Gallery Image Selected"
                }

                is MediaPickerResult.Document -> {
                    selectedImageUri = null
                    selectedDocumentUri = result.uri
                    resultMessage = "Document Selected"
                }

                is MediaPickerResult.Cancelled -> {
                    selectedImageUri = null
                    selectedDocumentUri = null
                    resultMessage = "Operation Cancelled"
                }
            }
        }
    )
}
