package com.reversecurrent.tennistracker.views.analytics

import com.reversecurrent.tennistracker.models.OutstandingPaymentCourt




import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.views.widgets.TextWidget

const val VENUE_NAME_LABEL = "Venue Name: "
const val SESSION_DATE_COURT_LABEL = "Session Date: "
const val AMOUNT_DUE_COURT_LABEL = "Amount Due: "

@Composable
fun OutstandingPaymentForCourtCardLayout(outstandingPaymentForCourt: OutstandingPaymentCourt) {
    Column{
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            CardContent(outstandingPaymentForCourt = outstandingPaymentForCourt)
        }
    }
}



@Composable
fun CardContent(outstandingPaymentForCourt: OutstandingPaymentCourt) {
    TextWidget(label = VENUE_NAME_LABEL, value = outstandingPaymentForCourt.venueName)
    TextWidget(label = SESSION_DATE_COURT_LABEL, value = outstandingPaymentForCourt.sessionDate)
    TextWidget(label = AMOUNT_DUE_COURT_LABEL, value = outstandingPaymentForCourt.paymentAmount.toString())
}


