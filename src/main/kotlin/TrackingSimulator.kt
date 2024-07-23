import androidx.compose.ui.window.application
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import java.io.File
import kotlin.reflect.KFunction5

object TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()
    private val updateMapping: Map<String, KFunction5<String, String, String, Long, String, ShippingUpdate>> = mapOf(
        "shipped" to ::ExpectedShippingUpdate,
        "location" to ::LocationShippingUpdate,
        "delayed" to ::ExpectedShippingUpdate,
        "noteadded" to ::NoteShippingUpdate

    )

    fun findShipment(id: String): Shipment?{
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment){
        shipments.add(shipment)
    }

    suspend fun runSimulation(fileName: String) {
        embeddedServer(Netty, 8080){
            routing {
                get("/"){

                }
            }
        }
        coroutineScope {
                launch {
                    val lines = File(fileName).readLines()
                    lines.forEach { line ->
                        val parts = line.split(",")
                        if (parts[0] == "created"){
                            addShipment(createShipment(parts[0], parts[1], parts[2]))
                        }
                        else {
                            when (parts[0]){

                            }
                        }
                    }
                }
        }
    }

    private fun createShipment( shipmentStatus: String, shipmentId: String, type: String): Shipment {
        return when (type){
            "standard" -> StandardShipment().apply {
                id = shipmentId
                status = shipmentStatus}

            "express" -> ExpressShipment().apply {
                id = shipmentId
                status = shipmentStatus}

            "overnight" -> OvernightShipment().apply {
                id = shipmentId
                status = shipmentStatus}

            "bulk" -> BulkShipment().apply {
                id = shipmentId
                status = shipmentStatus}

            else -> throw IllegalArgumentException("Unknown shipment type: $type")
        }

    }
}