class ExpectedShippingUpdate(previousStatus: String, newStatus: String, id: String, timestamp: Long, expectedDeliveryDate: String): ShippingUpdate(previousStatus, newStatus, id, timestamp) {
    init {
        val shipment = TrackingSimulator.findShipment(id)
        shipment?.expectedDeliveryDateTimestamp = expectedDeliveryDate.toLong()
        shipment?.status = newStatus
        shipment?.addUpdate(this)
    }
}