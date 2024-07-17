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
            updates.add(ShippingUpdate(previousStatus, updateType, shipmentId))
        }
        return updates
    }

    suspend fun runSimulation(fileName: String) {
        val updates = readFile(fileName)
        var timer = 60
        coroutineScope {
                launch {
                    val lines = File(fileName).readLines()
                    lines.forEach { line ->
                        val parts = line.split(",")
                        if (parts[0] == "created"){
                            val newShipment = Shipment().apply {
                                status = parts[0]
                                id = parts[1]
                            }
                            addShipment(newShipment)
                        }

                        println(timer)
                        timer--
                        delay(1000)
                    }
                }
        }
    }
}