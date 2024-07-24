import androidx.compose.ui.window.application
import io.ktor.http.*
import io.ktor.server.application.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.coroutines.*
import java.io.File
import kotlin.reflect.KFunction5

object TrackingSimulator {
    private val shipments = mutableListOf<Shipment>()
    private val updateMapping = mapOf(
        "shipped" to ::ExpectedShippingUpdate,
        "location" to ::LocationShippingUpdate,
        "delayed" to ::ExpectedShippingUpdate,
        "noteadded" to ::NoteShippingUpdate,
        "lost" to ::FinalShippingUpdate,
        "canceled" to ::FinalShippingUpdate,
        "delivered" to ::FinalShippingUpdate
    )

    fun findShipment(id: String): Shipment?{
        return shipments.find { it.id == id }
    }
    fun addShipment(shipment: Shipment){
        shipments.add(shipment)
    }

    suspend fun runSimulation() {
        coroutineScope {
            launch {
                embeddedServer(Netty, 8080){
                    routing {
                        get("/") {
                            call.respondText(File("src/main/assets/index.html").readText(), ContentType.Text.Html)
                        }
                        post("/data") {
                            val data = call.receiveText()
                            println(data)
                            processLine(data)
                            call.respondText { "Received" }
                        }
                    }
                }.start(wait = false)
            }
        }
    }

    fun createShipment(parts: List<String> ): Shipment {
        val shipmentStatus = parts[0]
        val shipmentId = parts[1]
        val timeStamp = parts[3].toLong()
        return when (val type = parts[2]){
            "standard" -> StandardShipment().apply {
                id = shipmentId
                status = shipmentStatus
                createdTimeStamp = timeStamp
            }

            "express" -> ExpressShipment().apply {
                id = shipmentId
                status = shipmentStatus
                createdTimeStamp = timeStamp
            }

            "overnight" -> OvernightShipment().apply {
                id = shipmentId
                status = shipmentStatus
                createdTimeStamp = timeStamp
            }

            "bulk" -> BulkShipment().apply {
                id = shipmentId
                status = shipmentStatus
                createdTimeStamp = timeStamp
            }

            else -> throw IllegalArgumentException("Unknown shipment type: $type")
        }

    }

    fun processLine(line: String) {
        val parts = line.split(",")
        if (parts[0] == "created") {
            addShipment(createShipment(parts))
        }
        else {
            val previousStatus = findShipment(parts[1])?.status
            val updateType = parts[0]
            val updateArguments = listOf(previousStatus, updateType, parts[1],parts[2].toLong(), parts.getOrNull(3) ?: "")
            updateMapping[updateType]?.call(*updateArguments.toTypedArray())
        }
    }
}