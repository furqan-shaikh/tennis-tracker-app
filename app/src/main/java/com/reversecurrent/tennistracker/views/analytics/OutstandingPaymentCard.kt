package com.reversecurrent.tennistracker.views.analytics



import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.OutstandingPayment
import com.reversecurrent.tennistracker.views.widgets.TextWidget

const val PLAYER_NAME_LABEL = "Player Name: "
const val SESSION_DATE_LABEL = "Session Date: "
const val AMOUNT_DUE_LABEL = "Amount Due: "

@Composable
fun OutstandingPaymentCardLayout(outstandingPayment: OutstandingPayment) {
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
    }
}



@Composable
fun CardContent(outstandingPayment: OutstandingPayment) {
    TextWidget(label = PLAYER_NAME_LABEL, value = outstandingPayment.playerName)
    TextWidget(label = SESSION_DATE_LABEL, value = outstandingPayment.sessionDate)
    TextWidget(label = AMOUNT_DUE_LABEL, value = outstandingPayment.paymentAmount.toString())
}


