class NoteShippingUpdate(previousStatus: String, newStatus: String, id: String, timestamp: Long, note: String
): ShippingUpdate(previousStatus, newStatus, id, timestamp) {
    init {
        val shipment = TrackingSimulator.findShipment(id)
        shipment?.addNote(note)
    }
}