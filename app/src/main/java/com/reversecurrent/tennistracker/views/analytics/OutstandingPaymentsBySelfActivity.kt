package com.reversecurrent.tennistracker.views.analytics


import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
import com.reversecurrent.tennistracker.models.OutstandingPayment
import com.reversecurrent.tennistracker.repository.SessionRepository
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import com.reversecurrent.tennistracker.views.widgets.TextWidget
import kotlinx.coroutines.runBlocking

const val LABEL_OUTSTANDING_PAYMENTS_COUNT = "Total Count: "
const val LABEL_OUTSTANDING_PAYMENTS_TOTAL_AMOUNT = "Total Amount: "

class OutstandingPaymentsToSelfActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    OutstandingPaymentsLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun OutstandingPaymentsLayout() {
    val context = LocalContext.current
    var outstandingPayments by remember { mutableStateOf(emptyList<OutstandingPayment>()) }
    LaunchedEffect(Unit) {
        runBlocking {
            // Fetch the list of outstanding payments asynchronously
            outstandingPayments = SessionRepository().getAllOutstandingPaymentsToSelf(context = context)
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp)
            .verticalScroll(
                rememberScrollState()
            ),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        Column {
            TextWidget(label = LABEL_OUTSTANDING_PAYMENTS_COUNT, value = outstandingPayments.size.toString())
            TextWidget(label = LABEL_OUTSTANDING_PAYMENTS_TOTAL_AMOUNT, value = getTotalAmount(outstandingPayments))
            
            outstandingPayments.forEach { outstandingPayment -> 
                OutstandingPaymentCardLayout(outstandingPayment = outstandingPayment)
            }
        }
    }
}

fun getTotalAmount(outstandingPayments: List<OutstandingPayment>): String {
    var totalAmount = 0.0f
    for (outstandingPayment in outstandingPayments) {
        totalAmount += outstandingPayment.paymentAmount
    }
    return totalAmount.toString()
}

