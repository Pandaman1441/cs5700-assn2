import kotlin.test.*


class TrackingSimulatorTest {

    @Test
    fun testCreateShipment(){
        val shipment = TrackingSimulator.createShipment(listOf("created","s1","standard","0"))
        assertNotNull(shipment)
        assertEquals("s1", shipment.id)
        assertTrue(shipment is StandardShipment)
    }

    @Test
    fun testProcessLine(){
        val line2 = "created,s2,express,0"
        val line3 = "created,s3,overnight,0"
        val line4 = "created,s4,bulk,0"
        TrackingSimulator.processLine(line2)
        val shipment2 = TrackingSimulator.findShipment("s2")
        assertNotNull(shipment2)
        assertTrue(shipment2 is ExpressShipment)

        TrackingSimulator.processLine(line3)
        val shipment3 = TrackingSimulator.findShipment("s3")
        assertNotNull(shipment3)
        assertTrue(shipment3 is OvernightShipment)

        TrackingSimulator.processLine(line4)
        val shipment4 = TrackingSimulator.findShipment("s4")
        assertNotNull(shipment4)
        assertTrue(shipment4 is BulkShipment)
    }

    @Test
    fun testProcessLineInvalidType(){
        val line = "created,s0,unknown,0"
        val exception = assertFailsWith<IllegalArgumentException> {
            TrackingSimulator.processLine(line)
        }
        assertEquals("Unknown shipment type: unknown", exception.message)
    }
}