package com.example.mediapickerlib

import android.Manifest
import android.app.Activity
import android.content.Context
import android.content.pm.PackageManager
import android.net.Uri
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.runtime.*
import androidx.core.content.ContextCompat
import androidx.core.content.FileProvider
import java.io.File
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

/**
 * Data class to hold launchers and helper functions for media picking
 */
data class MediaPickerLaunchers(
    val cameraLauncher: (Uri) -> Unit,
    val galleryLauncher: () -> Unit,
    val documentLauncher: () -> Unit,
    val createCameraUri: (Activity) -> Uri?,
    val launchCameraWithPermission: (Activity) -> Unit
)

/**
 * Composable function to create media picker launchers
 */
@Composable
fun rememberMediaPickerLaunchers(
    context: Context,
    onResult: (MediaPickerResult) -> Unit
): MediaPickerLaunchers {
    var photoUri by remember { mutableStateOf<Uri?>(null) }
    var pendingCameraUri by remember { mutableStateOf<Uri?>(null) }

    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success && photoUri != null) {
            onResult(MediaPickerResult.CameraPhoto(photoUri!!))
        } else {
            onResult(MediaPickerResult.Cancelled)
        }
        photoUri = null
        pendingCameraUri = null
    }

    val permissionLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission()
    ) { isGranted ->
        if (isGranted && pendingCameraUri != null) {
            cameraLauncher.launch(pendingCameraUri!!)
        } else {
            onResult(MediaPickerResult.Cancelled)
            pendingCameraUri = null
        }
    }

    val galleryLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onResult(MediaPickerResult.GalleryImage(uri))
        } else {
            onResult(MediaPickerResult.Cancelled)
        }
    }

    val documentLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            onResult(MediaPickerResult.Document(uri))
        } else {
            onResult(MediaPickerResult.Cancelled)
        }
    }

    fun createCameraUri(activity: Activity): Uri? {
        val photoFile = createImageFile(context)
        val authority = "${context.packageName}.mediapicker.fileprovider"

        return FileProvider.getUriForFile(
            context,
            authority,
            photoFile
        ).also { uri ->
            photoUri = uri
        }
    }

    fun launchCameraWithPermission(activity: Activity) {
        val uri = createCameraUri(activity)
        if (uri == null) {
            onResult(MediaPickerResult.Cancelled)
            return
        }

        // Check if camera permission is granted
        when {
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED -> {
                // Permission is already granted, launch camera
                cameraLauncher.launch(uri)
            }
            else -> {
                // Request permission first
                pendingCameraUri = uri
                permissionLauncher.launch(Manifest.permission.CAMERA)
            }
        }
    }

    return remember {
        MediaPickerLaunchers(
            cameraLauncher = { uri -> cameraLauncher.launch(uri) },
            galleryLauncher = { galleryLauncher.launch("image/*") },
            documentLauncher = { documentLauncher.launch("*/*") },
            createCameraUri = ::createCameraUri,
            launchCameraWithPermission = ::launchCameraWithPermission
        )
    }
}

/**
 * Create a temporary image file for camera capture
 */
private fun createImageFile(context: Context): File {
    val timeStamp = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.getDefault()).format(Date())
    val imageFileName = "JPEG_${timeStamp}_"
    val storageDir = context.getExternalFilesDir(android.os.Environment.DIRECTORY_PICTURES)
    return File.createTempFile(imageFileName, ".jpg", storageDir)
}
