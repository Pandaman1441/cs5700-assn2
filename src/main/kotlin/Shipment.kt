abstract class Shipment: Subject {
    var status = ""
    var id = ""
    val notes = mutableListOf<String>()
    val updateHistory = mutableListOf<ShippingUpdate>()
    var expectedDeliveryDateTimestamp: Long = 0L
    var currentLocation = ""
    var createdTimeStamp: Long = 0L
    private val observers = mutableListOf<Observer>()

    fun addNote(note: String){
        notes.add(note)
        notifyObservers()
    }

    abstract fun addUpdate(update: ShippingUpdate)

    override fun subscribe(observer: Observer) {
        observers.add(observer)
    }

    override fun unsubscribe(observer: Observer) {
        observers.remove(observer)
    }

    override fun notifyObservers() {
        observers.forEach{
            it.update(this)
        }
    }
}

class StandardShipment(): Shipment() {
    override fun addUpdate(update: ShippingUpdate) {
        if (update.newStatus != "location") {
            updateHistory.add(update)
        }
        notifyObservers()
    }
}
class ExpressShipment(): Shipment() {
    override fun addUpdate(update: ShippingUpdate) {
        if (expectedDeliveryDateTimestamp > createdTimeStamp + 3 * 24 * 60 * 60 * 1000){
            addNote("Shipment expected later than the expected 3 days for express shipping")
        }
        notifyObservers()
    }
}
class OvernightShipment(): Shipment() {
    override fun addUpdate(update: ShippingUpdate) {
        if (expectedDeliveryDateTimestamp > createdTimeStamp + 24 * 60 * 60 * 1000){
            addNote("Shipment expected later than the expected 1 day for overnight shipping")
        }
        notifyObservers()
    }
}
class BulkShipment(): Shipment(){
    override fun addUpdate(update: ShippingUpdate) {
        if (expectedDeliveryDateTimestamp < createdTimeStamp + 3 * 24 * 60 * 60 * 1000){
            addNote("Shipment expected sooner than the required 3 days waiting time for bulk shipping")
        }
        notifyObservers()
    }
}
