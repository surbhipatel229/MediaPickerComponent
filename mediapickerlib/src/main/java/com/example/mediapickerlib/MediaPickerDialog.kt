package com.example.mediapickerlib

import android.app.Activity
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color.Companion.Black
import androidx.compose.ui.graphics.Color.Companion.White
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog

/**
 * Media picker dialog with three options: Camera, Gallery, and Files
 *
 * @param onDismiss Callback when dialog is dismissed
 * @param onResult Callback when a media is selected
 * @param fileProviderAuthority The authority string for FileProvider (required for camera)
 * @param cameraLabel Label for camera option (default: "Camera")
 * @param galleryLabel Label for gallery option (default: "Gallery")
 * @param filesLabel Label for files option (default: "Files")
 */
@Composable
fun MediaPickerDialog(
    onDismiss: () -> Unit,
    onResult: (MediaPickerResult) -> Unit,
    //fileProviderAuthority: String,
    cameraLabel: String = "Camera",
    galleryLabel: String = "Gallery",
    filesLabel: String = "Files"
) {
    val context = LocalContext.current
    val activity = context as? Activity
    val launchers = rememberMediaPickerLaunchers(context) { result ->
        onResult(result)
        onDismiss()
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            modifier = Modifier
                .width(250.dp)
                .heightIn(110.dp),
            shape = MaterialTheme.shapes.large
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Camera option
                ActionButton(
                    text = "Camera",
                    onClick = {
                        if (activity != null) {
                            launchers.launchCameraWithPermission(activity)
                        }
                    },
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 40.dp))

                // Gallery option
                ActionButton(
                    text = "Gallery",
                    onClick = {
                        launchers.galleryLauncher()
                    },
                )

                HorizontalDivider(modifier = Modifier.padding(horizontal = 40.dp))

                // Files option
                ActionButton(
                    text = "Files",
                    onClick = {
                        launchers.documentLauncher()
                    },
                )
            }
        }
    }
}

@Composable
fun ActionButton(
    text: String,
    icon: Int? = null,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        shape = RoundedCornerShape(12.dp),
        colors = ButtonDefaults.buttonColors(
            containerColor = White,
            contentColor = Black,
        ),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start,
            modifier = Modifier.fillMaxWidth()
        ) {
            if (icon != null) {
                Image(
                    painter = painterResource(icon),
                    contentDescription = "User",
                    modifier = Modifier.size(20.dp),

                    )
                Spacer(modifier = Modifier.width(8.dp))
            }
            Box(modifier = Modifier.weight(1f)) {
                Text(
                    text = text,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 14.sp,
                    textAlign = TextAlign.Center,
                    modifier = Modifier.align(Alignment.Center)
                )
            }
        }
    }
}

@Preview
@Composable
private fun MediaPickerDialogPreview(){
    MediaPickerDialog(
        onDismiss = {},
        onResult = {},
        //fileProviderAuthority = "com.example.app.fileprovider"
    )
}
