# MediaPicker Library

A simple and easy-to-use Android library for picking media (camera photos, gallery images, and documents) with a beautiful dialog interface.

## Features

- 📷 **Camera**: Take photos directly from the camera
- 🖼️ **Gallery**: Select images from the device gallery
- 📄 **Files**: Select documents and files from the device storage
- 🎨 **Material Design 3**: Beautiful, modern UI with Material Design 3
- 🔒 **Easy Integration**: Simple API with minimal code required

## Setup

### 1. Add the library module to your project

Include the library module in your `settings.gradle.kts`:

```kotlin
include(":mediapickerlib")
```

### 2. Add dependency

In your app's `build.gradle.kts`:

```kotlin
dependencies {
    implementation(project(":mediapickerlib"))
    // ... other dependencies
}
```

### 3. Add permissions

In your `AndroidManifest.xml`:

```xml
<uses-permission android:name="android.permission.CAMERA" />
<uses-permission android:name="android.permission.READ_EXTERNAL_STORAGE"
    android:maxSdkVersion="32" />
<uses-permission android:name="android.permission.READ_MEDIA_IMAGES" />
```

### 4. Configure FileProvider

In your `AndroidManifest.xml`, inside the `<application>` tag:

```xml
<provider
    android:name="androidx.core.content.FileProvider"
    android:authorities="${applicationId}.fileprovider"
    android:exported="false"
    android:grantUriPermissions="true">
    <meta-data
        android:name="android.support.FILE_PROVIDER_PATHS"
        android:resource="@xml/file_paths" />
</provider>
```

Create `res/xml/file_paths.xml`:

```xml
<?xml version="1.0" encoding="utf-8"?>
<paths xmlns:android="http://schemas.android.com/apk/res/android">
    <external-files-path
        name="my_images"
        path="Pictures" />
    <external-files-path
        name="my_documents"
        path="Documents" />
</paths>
```

## Usage

### Basic Usage

```kotlin
import com.example.mediapickerlib.MediaPicker
import com.example.mediapickerlib.MediaPickerResult
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext

@Composable
fun MyScreen() {
    val context = LocalContext.current
    var showDialog by remember { mutableStateOf(false) }

    Button(onClick = { showDialog = true }) {
        Text("Pick Media")
    }

    MediaPicker(
        showDialog = showDialog,
        onDismiss = { showDialog = false },
        onResult = { result ->
            when (result) {
                is MediaPickerResult.CameraPhoto -> {
                    // Handle camera photo
                    val photoUri = result.uri
                }
                is MediaPickerResult.GalleryImage -> {
                    // Handle gallery image
                    val imageUri = result.uri
                }
                is MediaPickerResult.Document -> {
                    // Handle document
                    val documentUri = result.uri
                }
                is MediaPickerResult.Cancelled -> {
                    // User cancelled
                }
            }
        },
        fileProviderAuthority = "${context.packageName}.fileprovider"
    )
}
```

### Custom Labels

You can customize the button labels:

```kotlin
MediaPicker(
    showDialog = showDialog,
    onDismiss = { showDialog = false },
    onResult = { /* ... */ },
    fileProviderAuthority = "${context.packageName}.fileprovider",
    cameraLabel = "Take Photo",
    galleryLabel = "Choose Image",
    filesLabel = "Select File"
)
```

### Direct Dialog Usage

If you need more control, you can use `MediaPickerDialog` directly:

```kotlin
import com.example.mediapickerlib.MediaPickerDialog

if (showDialog) {
    MediaPickerDialog(
        onDismiss = { showDialog = false },
        onResult = { result ->
            // Handle result
        },
        fileProviderAuthority = "${context.packageName}.fileprovider"
    )
}
```

## Requirements

- Minimum SDK: 24 (Android 7.0)
- Compile SDK: 36
- Kotlin
- Jetpack Compose
- Material Design 3

## License

This library is provided as-is for use in your projects.

