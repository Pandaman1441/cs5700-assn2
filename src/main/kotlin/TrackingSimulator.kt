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

    suspend fun runSimulation(fileName: String) {
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
                        else {
                            when (parts[0]){
                                "shipped" -> ExpectedShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong(), parts[3])
                                "location" -> LocationShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong(), parts[3])
                                "delayed" -> ExpectedShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong(), parts[3])
                                "noteadded" -> NoteShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong(), parts[3])
                                "lost" -> FinalShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong())
                                "canceled" -> FinalShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong())
                                "delivered" -> FinalShippingUpdate(findShipment(parts[1])?.status ?: "", parts[0], parts[1], parts[2].toLong())
                            }
                        }

                        println(timer)
                        timer--
                        delay(1000)
                    }
                }
        }
    }
}