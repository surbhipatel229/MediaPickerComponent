package com.example.mediapickerlib

import android.net.Uri

/**
 * Sealed class representing the result of media picker operations
 */
sealed class MediaPickerResult {
    /**
     * Result when camera photo is taken
     * @param uri The URI of the captured photo
     */
    data class CameraPhoto(val uri: Uri) : MediaPickerResult()

    /**
     * Result when image is selected from gallery
     * @param uri The URI of the selected image
     */
    data class GalleryImage(val uri: Uri) : MediaPickerResult()

    /**
     * Result when document/file is selected
     * @param uri The URI of the selected document
     */
    data class Document(val uri: Uri) : MediaPickerResult()

    /**
     * Result when user cancels the operation
     */
    object Cancelled : MediaPickerResult()
}

