class Shipment: Subject {
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

    fun addUpdate(update: ShippingUpdate){
        updateHistory.add(update)
        status = update.newStatus
        if (update.newStatus == "shipped" || update.newStatus == "delayed") {
            expectedDeliveryDateTimestamp = update.timestamp
        }
        notifyObservers()
    }


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