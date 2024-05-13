package com.reversecurrent.tennistracker.views

import android.annotation.SuppressLint
import android.content.ContentResolver
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.provider.ContactsContract
import android.util.Log
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.unit.dp
import com.reversecurrent.tennistracker.ui.theme.TennisTrackerTheme
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import com.reversecurrent.tennistracker.models.Player
const val READ_CONTACTS_PERMISSION_CODE = 123
const val READ_CONTACTS_PERMISSION = android.Manifest.permission.READ_CONTACTS
class AddPlayerFromContactsActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val requestPermissionLauncher =
            this.registerForActivityResult(ActivityResultContracts.RequestPermission()) { isGranted ->
                if (isGranted) {
                    // Permission granted, proceed with your logic
                    Toast.makeText(this, "Permission granted", Toast.LENGTH_SHORT).show()
                } else {
                    // Permission denied, show a message or handle accordingly
                    Toast.makeText(this, "Permission denied", Toast.LENGTH_SHORT).show()
                }
            }

        setContent {
            TennisTrackerTheme {
                Surface(
                    modifier = Modifier.fillMaxSize(),
                    color = MaterialTheme.colorScheme.background
                ) {
                    AddPlayerFromExistingScreenLayout(this, requestPermissionLauncher)
                }
            }
        }
    }
}

@Composable
fun AddPlayerFromExistingScreenLayout(
    addPlayerFromContactsActivity: AddPlayerFromContactsActivity,
    requestPermissionLauncher: ActivityResultLauncher<String>
) {
    var playersState by remember {
        mutableStateOf(listOf<String>())
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp, vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        AddImportButton("Import Players",
            Icons.Default.AddCircle,
            onPlayersImported = { playersState = it },
            addPlayerFromContactsActivity,
            requestPermissionLauncher)
        LazyColumn {
            items(playersState) { currentPlayer ->
                Text(text = currentPlayer)
            }
        }
    }
}

@Composable
fun AddImportButton(
    text: String,
    icon: ImageVector,
    onPlayersImported: (List<String>) -> Unit,
    addPlayerFromContactsActivity: AddPlayerFromContactsActivity,
    requestPermissionLauncher: ActivityResultLauncher<String>
) {
    val context = LocalContext.current
    Button(
        onClick = {
            requestContactsPermission(context,addPlayerFromContactsActivity,requestPermissionLauncher )
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

fun requestContactsPermission(
    context: Context,
    activity: ComponentActivity,
    requestPermissionLauncher: ActivityResultLauncher<String>
) {
    when {
        ContextCompat.checkSelfPermission(activity, READ_CONTACTS_PERMISSION) ==
                PackageManager.PERMISSION_GRANTED -> {
            // Permission is already granted
            loadPlayers(context)
        }
        else -> {
            // Request the permission using the launcher
            requestPermissionLauncher.launch(READ_CONTACTS_PERMISSION)
        }
    }
}

@SuppressLint("Range")
fun loadPlayers(context: Context): List<String> {
    val resolver: ContentResolver = context.contentResolver
    val projection = arrayOf(
        ContactsContract.Contacts.DISPLAY_NAME
    )

    val contactsCursor = resolver.query(
        ContactsContract.Contacts.CONTENT_URI,
        projection,
        null,
        null,
        null
    )
    Log.i("ImportPlayersActivity", "post query")
//    val players = mutableStateListOf<Player>()
    val players = mutableStateListOf<String>()
    if (contactsCursor != null) {
        Log.i("ImportPlayersActivity", "cursor is not null")
        while (contactsCursor.moveToNext()) {
            val name = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
            val id = contactsCursor.getString(contactsCursor.getColumnIndex(ContactsContract.Contacts._ID))
            Log.i("ImportPlayersActivity", name)

            val player = Player(id=0, displayName=name)
            players.add(name)
        }
    }

    contactsCursor?.close()
    return players.toList()
}

@Composable
fun ShowToast(names: List<Player>) {
    val message = names.joinToString(", ") // Join names into a single string with comma separation
    val context = LocalContext.current
    Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
}
