package com.eje.sozip.frameworks.ui

import android.widget.Space
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.IconButton
import androidx.compose.material.Surface
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.eje.sozip.SOZIP.models.SOZIPDataModel
import com.eje.sozip.SOZIP.ui.SOZIPDetailView
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.gray
import com.eje.sozip.userManagement.ui.SignInView

@Composable
fun ModalDialog(
    onDismissRequest : () -> Unit,
    properties : DialogProperties = DialogProperties(),
    modifier: Modifier = Modifier,
    content : @Composable () -> Unit) {
    Dialog(onDismissRequest = onDismissRequest,
        properties = properties) {
        Surface(color = SOZIPColorPalette.current.background,
            modifier = Modifier.clip(RoundedCornerShape(15.dp))) {
            Column {
                Row(horizontalArrangement = Arrangement.End, modifier = Modifier.fillMaxWidth()){
                    IconButton(onClick = { onDismissRequest() }) {
                        Icon(imageVector = Icons.Default.Cancel, contentDescription = null, tint = gray.copy(alpha = 0.5f))
                    }
                }

                Spacer(modifier = Modifier.height(10.dp))

                content()
            }
        }
    }
}

@Preview
@Composable
fun ModalDialog_preview(){
    ModalDialog(onDismissRequest = {

    }, content = { SignInView() })
}