import androidx.compose.ui.window.application
import kotlinx.coroutines.*
import java.io.File

object TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()
    fun findShipment(id: String): Shipment?{
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment){
        shipments.add(shipment)
    }

    fun readFile(fileName: String): List<ShippingUpdate> {
        val updates = mutableListOf<ShippingUpdate>()
        // Logic to read updates from the file and return a list of ShippingUpdate instances
        val lines = File(fileName).readLines()
        lines.forEach { line ->
            val parts = line.split(",")
            val updateType = parts[0]
            val shipmentId = parts[1]
            val timestamp = parts[2].toLong()
            val otherInfo = parts.getOrNull(3) ?: ""
            val previousStatus = when (updateType) {
                "created" -> ""
                "shipped" -> "created"
                "delivered" -> "shipped"
                "lost" -> "shipped"
                "canceled" -> "shipped"
                "noteadded" -> "shipped"
                "location" -> "shipped"
                "delayed" -> "shipped"
                else -> ""
            }
            updates.add(ShippingUpdate(previousStatus, updateType, shipmentId, timestamp))
        }
        return updates
    }

    fun runSimulation(fileName: String) {
        val updates = readFile(fileName)
        GlobalScope.launch {
            updates.forEach { update ->
                val shipment = findShipment(update.id)
                if (shipment != null) {
                    shipment.addUpdate(update)
                } else if (update.newStatus == "created") {
                    val newShipment = Shipment().apply {
                        id = update.id
                        status = update.newStatus
                    }
                    addShipment(newShipment)
                    newShipment.addUpdate(update)
                }
                delay(1000)
            }
        }
    }


}