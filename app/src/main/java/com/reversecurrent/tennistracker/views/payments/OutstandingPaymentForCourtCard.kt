package com.reversecurrent.tennistracker.views.payments

import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.annotation.RequiresApi
import com.reversecurrent.tennistracker.models.OutstandingPaymentCourt




import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ElevatedCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.SESSION_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SESSION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.SessionActionEnum
import com.reversecurrent.tennistracker.repository.SessionRepository
import com.reversecurrent.tennistracker.views.sessions.AddSessionActivity
import com.reversecurrent.tennistracker.views.widgets.ClickableTextWidget
import com.reversecurrent.tennistracker.views.widgets.TextWidget
import kotlinx.coroutines.runBlocking

const val VENUE_NAME_LABEL = "Venue Name: "
const val SESSION_DATE_COURT_LABEL = "Session Date: "
const val AMOUNT_DUE_COURT_LABEL = "Amount Due: "

@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun OutstandingPaymentForCourtCardLayout(context: Context, outstandingPaymentForCourt: OutstandingPaymentCourt) {
    Column{
        ElevatedCard(
            elevation = CardDefaults.cardElevation(
                defaultElevation = 6.dp
            ),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
        ) {
            CardContent(context = context, outstandingPaymentForCourt = outstandingPaymentForCourt)
        }
    }
}



@RequiresApi(Build.VERSION_CODES.O)
@Composable
fun CardContent(context: Context, outstandingPaymentForCourt: OutstandingPaymentCourt) {
    TextWidget(label = VENUE_NAME_LABEL, value = outstandingPaymentForCourt.venueName)
    ClickableTextWidget(label = SESSION_DATE_COURT_LABEL,
                        value = outstandingPaymentForCourt.sessionDate,
                        id = outstandingPaymentForCourt.sessionId.toString(),
                        onClick = { id: String ->
                            runBlocking {
                                val session = SessionRepository().getSessionDetails(context = context, sessionId = id.toLong())
                                val intent = Intent(context, AddSessionActivity::class.java)
                                intent.putExtra(SESSION_INTENT_EXTRA, session)
                                intent.putExtra(SESSION_ACTION_INTENT_EXTRA, SessionActionEnum.EDIT)
                                context.startActivity(intent)
                            }
                        })
    TextWidget(label = AMOUNT_DUE_COURT_LABEL, value = outstandingPaymentForCourt.paymentAmount.toString())
}


