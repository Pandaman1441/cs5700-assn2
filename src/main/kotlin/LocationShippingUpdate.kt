class LocationShippingUpdate(previousStatus: String, newStatus: String, id: String, timestamp: Long, location: String): ShippingUpdate(previousStatus, newStatus, id, timestamp) {
    init {
        val shipment = TrackingSimulator.findShipment(id)
        shipment?.currentLocation = location
        shipment?.notifyObservers()
    }
}