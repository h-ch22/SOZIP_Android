package com.eje.sozip.userManagement.models

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.eje.sozip.frameworks.helper.AES256Util
import com.eje.sozip.ui.theme.SOZIPColorPalette
import com.eje.sozip.ui.theme.SOZIPTheme
import com.eje.sozip.ui.theme.gray

@Composable
fun AccountInfoListModel(account: AccountDataModel, modifier: Modifier, onClick : () -> Unit){
    SOZIPTheme {
        Card(
            shape = RoundedCornerShape(15.dp),
            colors = CardDefaults.cardColors(containerColor = SOZIPColorPalette.current.btnColor),
            elevation = CardDefaults.cardElevation(
                defaultElevation = 3.dp
            )
        ) {
            Column(modifier = Modifier.padding(15.dp),
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.Start) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "${AES256Util.decrypt(account.bank)} ${AES256Util.decrypt(account.accountNumber)}", fontSize = 15.sp, fontWeight = FontWeight.SemiBold, color = SOZIPColorPalette.current.txtColor)

                    Spacer(modifier = Modifier.weight(1f))

                    IconButton(onClick = { onClick() }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                    }
                }
                Spacer(modifier = Modifier.height(5.dp))
                Divider(modifier = Modifier.fillMaxWidth(), color = gray.copy(alpha = 0.5f))
                Spacer(modifier = Modifier.height(5.dp))
                Text(text = AES256Util.decrypt(account.name), fontSize = 12.sp, color = gray)
            }
        }
    }
}

@Preview
@Composable
fun AccountInfoListModel_previews(){
    AccountInfoListModel(account = AccountDataModel(bank = "Bank", name = "Name", accountNumber = "0000000"), modifier = Modifier){

    }
}