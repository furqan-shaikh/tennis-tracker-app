package com.reversecurrent.tennistracker.views.payments



import android.os.Build
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.annotation.RequiresApi
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.models.OutstandingPaymentCourt
import com.reversecurrent.tennistracker.repository.SessionRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import com.reversecurrent.tennistracker.views.widgets.TextWidget
import kotlinx.coroutines.runBlocking

const val LABEL_OUTSTANDING_PAYMENTS_COURTS_COUNT = "Total Count: "
const val LABEL_OUTSTANDING_PAYMENTS__COURTS_TOTAL_AMOUNT = "Total Amount: "

class OutstandingPaymentsCourtActivity : ComponentActivity() {
    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OutstandingPaymentsCourtLayout()
                }
            }
        }
    }
}

@RequiresApi(Build.VERSION_CODES.O)
@Preview
@Composable
fun OutstandingPaymentsCourtLayout() {
    val context = LocalContext.current
    var outstandingPaymentsCourt by remember { mutableStateOf(emptyList<OutstandingPaymentCourt>()) }
    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the list of outstanding payments for court asynchronously
            outstandingPaymentsCourt = SessionRepository().getAllOutstandingPaymentsForCourts(context = context)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            TextWidget(label = LABEL_OUTSTANDING_PAYMENTS_COURTS_COUNT, value = outstandingPaymentsCourt.size.toString())
            TextWidget(label = LABEL_OUTSTANDING_PAYMENTS__COURTS_TOTAL_AMOUNT, value = getTotalAmount(outstandingPaymentsCourt))

            outstandingPaymentsCourt.forEach { outstandingPaymentForCourt ->
                OutstandingPaymentForCourtCardLayout(context = context, outstandingPaymentForCourt = outstandingPaymentForCourt)
            }
        }
    }
}

fun getTotalAmount(outstandingPayments: List<OutstandingPaymentCourt>): String {
    var totalAmount = 0.0f
    for (outstandingPayment in outstandingPayments) {
        totalAmount += outstandingPayment.paymentAmount
    }
    return totalAmount.toString()
}

