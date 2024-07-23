import androidx.compose.desktop.ui.tooling.preview.Preview
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.runtime.*
import androidx.compose.ui.unit.dp
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.window.Window
import androidx.compose.ui.window.application
import kotlinx.coroutines.launch


@Composable
@Preview
fun App() {
    val trackers = remember { mutableStateListOf<TrackerViewHelper>() }
    var trackingID by remember { mutableStateOf("") }
    val coroutineScope = rememberCoroutineScope()
    var invalidTrackingId by remember { mutableStateOf("") }

    coroutineScope.launch {
        TrackingSimulator.runSimulation()
    }

    MaterialTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            TextField(
                value = trackingID,
                onValueChange = { trackingID = it }
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row {
                Button(onClick = {
                    val tracker = TrackerViewHelper()
                    tracker.trackShipment(trackingID)
                    if (tracker.invalidMessage.isEmpty()){
                        trackers.add(tracker)
                        invalidTrackingId = ""
                    }
                    else{
                        invalidTrackingId = tracker.invalidMessage
                    }
                }) {
                    Text("Track Shipment")
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            if (invalidTrackingId.isNotEmpty()){
                Text(
                    text = invalidTrackingId,
                    color = MaterialTheme.colors.error,
                )
                Spacer(modifier = Modifier.height(8.dp))
            }

            LazyColumn(
                modifier = Modifier.fillMaxSize()
            ) {
                items(trackers) { tracker ->
                    ShipmentCard(tracker, onRemove = { tracker.stopTracking(); trackers.remove(tracker) })
                    Spacer(modifier = Modifier.height(8.dp))
                }
            }
        }
    }
}

@Composable
fun ShipmentCard(tracker: TrackerViewHelper, onRemove: () -> Unit) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        elevation = 4.dp
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Column {
                    Text("Shipment ID: ${tracker.shipmentId}")
                    Text("Status: ${tracker.shipmentStatus}")
                    Text("Location: ${tracker.location}")
                    Text("Expected Delivery Date: ${tracker.expectedShipmentDeliveryDate}")
                }
                IconButton(onClick = onRemove) {
                    Icon(Icons.Default.Close, contentDescription = "Remove")
                }
            }
            Text("Updates:")
            tracker.shipmentUpdateHistory.forEach { update ->
                Text("- ${update.previousStatus} to ${update.newStatus} on ${update.timestamp}")
            }
            Text("Notes:")
            tracker.shipmentNotes.forEach { note ->
                Text("- $note")
            }
        }
    }
}

fun main() = application {
    Window(onCloseRequest = ::exitApplication){
        App()
    }
}

