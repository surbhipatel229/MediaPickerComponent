package com.example.mediapickerlib

import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

/**
 * Main API class for easy integration of MediaPickerDialog
 *
 * Usage example:
 * ```
 * var showDialog by remember { mutableStateOf(false) }
 * 
 * MediaPicker(
 *     showDialog = showDialog,
 *     onDismiss = { showDialog = false },
 *     onResult = { result ->
 *         when (result) {
 *             is MediaPickerResult.CameraPhoto -> { /* Handle camera photo */ }
 *             is MediaPickerResult.GalleryImage -> { /* Handle gallery image */ }
 *             is MediaPickerResult.Document -> { /* Handle document */ }
 *             is MediaPickerResult.Cancelled -> { /* Handle cancellation */ }
 *         }
 *     },
 *     fileProviderAuthority = "com.example.app.fileprovider"
 * )
 * ```
 *
 * @param showDialog Boolean state to control dialog visibility
 * @param onDismiss Callback when dialog is dismissed
 * @param onResult Callback when a media is selected
 * @param fileProviderAuthority The authority string for FileProvider (required for camera)
 * @param cameraLabel Optional label for camera option
 * @param galleryLabel Optional label for gallery option
 * @param filesLabel Optional label for files option
 */
@Composable
fun MediaPicker(
    showDialog: Boolean,
    onDismiss: () -> Unit,
    onResult: (MediaPickerResult) -> Unit,
    //fileProviderAuthority: String,
    cameraLabel: String = "Camera",
    galleryLabel: String = "Gallery",
    filesLabel: String = "Files"
) {
    if (showDialog) {
        MediaPickerDialog(
            onDismiss = onDismiss,
            onResult = onResult,
            //fileProviderAuthority = fileProviderAuthority,
            cameraLabel = cameraLabel,
            galleryLabel = galleryLabel,
            filesLabel = filesLabel
        )
    }
}

