abstract class Shipment: Subject {
    var status = ""
    var id = ""
    val notes = mutableListOf<String>()
    val updateHistory = mutableListOf<ShippingUpdate>()
    var expectedDeliveryDateTimestamp: Long = 0L
    var currentLocation = ""
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
        TODO("Not yet implemented")
    }
}
class OvernightShipment(): Shipment() {
    override fun addUpdate(update: ShippingUpdate) {
        TODO("Not yet implemented")
    }
}
class BulkShipment(): Shipment(){
    override fun addUpdate(update: ShippingUpdate) {
        TODO("Not yet implemented")
    }
}
