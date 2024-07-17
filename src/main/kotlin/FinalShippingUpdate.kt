class FinalShippingUpdate(previousStatus: String, newStatus: String, id: String, timestamp: Long): ShippingUpdate(previousStatus, newStatus, id, timestamp) {
    init {
        val shipment = TrackingSimulator.findShipment(id)
        shipment?.status = newStatus
        shipment?.addUpdate(this)
    }
}
