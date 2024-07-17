import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.TextField
import androidx.compose.runtime.*
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application

@Composable
@Preview
fun App() {
    val tracker = remember { TrackerViewHelper() }
    var trackingID by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
                .background(Color.Red)
                .background(Color.Blue),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = trackingID,
                onValueChange = { trackingID = it}
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = { tracker.trackShipment(trackingID) }) {
                    Text("Track Shipment")
                }
                Spacer(modifier = Modifier.width(8.dp))
                Button(onClick = { tracker.stopTracking() }) {
                    Text("Stop Tracking")
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
            Text("Status: ${tracker.shipmentStatus}")
            Text("Location: ${tracker.shipmentNotes.joinToString(", ")}")
            Text("Expected Delivery Date: ${tracker.expectedShipmentDeliveryDate}")
            Text("Updates: ${tracker.shipmentUpdateHistory.joinToString { "${it.previousStatus} to ${it.newStatus} on ${it.timestamp}" }}")
        }
    }

}
fun main() = application {
    Window(onCloseRequest = ::exitApplication){
        App()
    }
}
