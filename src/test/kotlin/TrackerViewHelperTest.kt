import kotlin.test.*


class TrackerViewHelperTest {
    private lateinit var trackerViewHelper: TrackerViewHelper
    @BeforeTest
    fun setUp(){
        trackerViewHelper = TrackerViewHelper()

    }

    @Test
    fun testTrackShipment(){
        val createdLine = "created,s1,standard,0"
        val shippedLine = "shipped,s1,0,123123"
        val locationLine = "location,s1,0,Los Angeles CA"
        val delayedLine = "delayed,s1,0,123123123"
        val noteLine = "noteadded,s1,0,testing note"
        val lostLine = "lost,s1,0"
        val canceledLine = "canceled,s1,0"
        val deliveredLine = "delivered,s1,0"
        TrackingSimulator.processLine(createdLine)
        trackerViewHelper.trackShipment("s1")
        assertEquals("s1", trackerViewHelper.shipmentId)
        assertEquals("created", trackerViewHelper.shipmentStatus)
        assertTrue(trackerViewHelper.shipmentUpdateHistory.isEmpty())

        TrackingSimulator.processLine(shippedLine)
        assertEquals("shipped", trackerViewHelper.shipmentStatus)
        assertEquals(123123, trackerViewHelper.expectedShipmentDeliveryDate)
        assertEquals(1, trackerViewHelper.shipmentUpdateHistory.size)

        TrackingSimulator.processLine(locationLine)
        assertEquals("Los Angeles CA", trackerViewHelper.location)

        TrackingSimulator.processLine(delayedLine)
        assertEquals("delayed", trackerViewHelper.shipmentStatus)
        assertEquals(123123123, trackerViewHelper.expectedShipmentDeliveryDate)
        assertEquals(2, trackerViewHelper.shipmentUpdateHistory.size)

        TrackingSimulator.processLine(noteLine)
        assertTrue(trackerViewHelper.shipmentNotes.isNotEmpty())
        assertEquals("testing note", trackerViewHelper.shipmentNotes[0])

        TrackingSimulator.processLine(lostLine)
        assertEquals("lost", trackerViewHelper.shipmentStatus)

        TrackingSimulator.processLine(canceledLine)
        assertEquals("canceled", trackerViewHelper.shipmentStatus)

        TrackingSimulator.processLine(deliveredLine)
        assertEquals("delivered", trackerViewHelper.shipmentStatus)

    }

}