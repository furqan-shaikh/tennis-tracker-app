package com.reversecurrent.tennistracker.views

import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Send
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import com.reversecurrent.tennistracker.models.PLAYER_ACTION_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.PLAYER_INTENT_EXTRA
import com.reversecurrent.tennistracker.models.PlayerActionEnum
import com.reversecurrent.tennistracker.models.getEmptyPlayer

//const val TEXT_IMPORT = "Import from Existing Contacts"
const val TEXT_MANUAL = "Manual Add"

class AddPlayerActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddPlayerLayout()
                }
            }
        }
    }
}

@Preview
@Composable
fun AddPlayerLayout() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddPlayerButton("Import from Existing Contacts", Icons.Default.Send)
        AddPlayerButton("Manual Add", Icons.Default.Person)
    }
}

@Composable
fun AddPlayerButton(text: String, icon: ImageVector) {
    val context = LocalContext.current
    Button(
        onClick = {
            when(text){
                TEXT_MANUAL -> navigateToAddPlayerFromManualActivity(context=context)
            }
        },
        modifier = Modifier.fillMaxWidth(),
        colors = ButtonDefaults.buttonColors(containerColor = Color.Black),
        contentPadding = PaddingValues(16.dp),
    ) {
        Icon(icon, contentDescription = null) // Display icon
        Spacer(modifier = Modifier.width(8.dp)) // Add space between icon and text
        Text(text)
    }
}

fun navigateToAddPlayerFromManualActivity(context: android.content.Context) {
    val intent = Intent(context, AddPlayerFromManualActivity::class.java)
    intent.putExtra(PLAYER_INTENT_EXTRA, getEmptyPlayer())
    intent.putExtra(PLAYER_ACTION_INTENT_EXTRA, PlayerActionEnum.ADD)
    context.startActivity(intent)
}
