class NoteShippingUpdate(previousStatus: String, newStatus: String, id: String, val note: String
): ShippingUpdate(previousStatus, newStatus, id) {
    init {
        val shipment = TrackingSimulator.findShipment(id)
        shipment?.addNote(note)
    }
}