package com.reversecurrent.tennistracker.views.payments



import android.content.Context
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ShoppingCart
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.OutstandingPayment
import com.reversecurrent.tennistracker.utils.launchApp
import com.reversecurrent.tennistracker.views.widgets.TextWidget

const val PLAYER_NAME_LABEL = "Player Name: "
const val SESSION_DATE_LABEL = "Session Date: "
const val AMOUNT_DUE_LABEL = "Amount Due: "

@Composable
fun OutstandingPaymentCardLayout(context: Context, outstandingPayment: OutstandingPayment, showPayment: Boolean = true) {
    Column{
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            CardContent(outstandingPayment = outstandingPayment)
        }
        ActionButtons(context = context, showPayment)
    }
}



@Composable
fun CardContent(outstandingPayment: OutstandingPayment) {
    TextWidget(label = PLAYER_NAME_LABEL, value = outstandingPayment.playerName)
    TextWidget(label = SESSION_DATE_LABEL, value = outstandingPayment.sessionDate)
    TextWidget(label = AMOUNT_DUE_LABEL, value = outstandingPayment.paymentAmount.toString())
}

@Composable
fun ActionButtons(context: Context, showPayment: Boolean) {
    Row {
        if (showPayment) {
            Button(
                onClick = {
                    launchApp(context= context, packageName = "com.phonepe.app")
                },
                colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
                contentPadding = PaddingValues(16.dp),
                modifier = Modifier.padding(16.dp)
            ) {
                Icon(Icons.Default.ShoppingCart, contentDescription = null) // Display icon
            }
        }

    }
}


