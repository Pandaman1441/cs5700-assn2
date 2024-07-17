interface Subject {

    fun subscribe(observer: Observer)
    fun unsubscribe(observer: Observer)
    fun notifyObservers()
}