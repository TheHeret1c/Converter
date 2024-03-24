package org.example.searadar.mr231_3.convert;

import org.example.searadar.mr231_3.station.Mr231_3StationType;
import org.junit.Test;
import ru.oogis.searadar.api.message.InvalidMessage;
import ru.oogis.searadar.api.message.RadarSystemDataMessage;
import ru.oogis.searadar.api.message.SearadarStationMessage;
import ru.oogis.searadar.api.message.TrackedTargetMessage;
import ru.oogis.searadar.api.types.IFF;
import ru.oogis.searadar.api.types.TargetStatus;

import java.util.List;

import static org.junit.Assert.*;

public class Mr231_3ConvertTest {

    Mr231_3StationType mr231_3 = new Mr231_3StationType();
    Mr231_3Converter converter = mr231_3.createConverter();

    @Test
    public void convertTTMMessage() {
        String msg = "$RATTM,45,15.21,245.7,T,34.7,124.1,T,2.2,10.5,N,d,Q,,785146,A*42";
        List<SearadarStationMessage> result = converter.convert(msg);
        assertFalse("Result should not be empty", result.isEmpty());
        assertTrue("Result should be an instance of TrackedTargetMessage", result.get(0) instanceof TrackedTargetMessage);

        TrackedTargetMessage ttm = (TrackedTargetMessage) result.get(0);
        assertEquals(45, ttm.getTargetNumber(), 0.0);
        assertEquals(15.21, ttm.getDistance(), 0.0);
        assertEquals(245.7, ttm.getBearing(), 0.0);
        assertEquals(34.7, ttm.getSpeed(), 0.0);
        assertEquals(124.1, ttm.getCourse(), 0.0);
        assertEquals(TargetStatus.UNRELIABLE_DATA, ttm.getStatus());
        assertEquals(IFF.UNKNOWN, ttm.getIff());
    }

    @Test
    public void convertRSDMessageValid() {
        String rsdMessage = "$RARSD,4.5,0.0,5.1,9.5,,,,,7.5,125.1,12.0,N,H,P*20";
        List<SearadarStationMessage> result = converter.convert(rsdMessage);
        assertFalse("Result should not be empty", result.isEmpty());
        assertTrue("Result should be an instance of RadarSystemDataMessage", result.get(0) instanceof RadarSystemDataMessage);

        RadarSystemDataMessage rsd = (RadarSystemDataMessage) result.get(0);
        assertEquals(4.5, rsd.getInitialDistance(), 0.0);
        assertEquals(0.0, rsd.getInitialBearing(), 0.0);
        assertEquals(5.1, rsd.getMovingCircleOfDistance(), 0.0);
        assertEquals(9.5, rsd.getBearing(), 0.0);
        assertEquals(7.5, rsd.getDistanceFromShip(), 0.0);
        assertEquals(125.1, rsd.getBearing2(), 0.0);
        assertEquals(12.0, rsd.getDistanceScale(), 0.0);
        assertEquals("N", rsd.getDistanceUnit());
        assertEquals("H", rsd.getDisplayOrientation());
        assertEquals("P", rsd.getWorkingMode());
    }

    @Test
    public void convertRSDMessageInvalidScale() {
        String rsdMessage = "$RARSD,4.5,0.0,5.1,9.5,,,,,7.5,125.1,12.1,N,H,P*20";
        List<SearadarStationMessage> result = converter.convert(rsdMessage);
        assertFalse("Result should not be empty", result.isEmpty());
        assertTrue("Result should be an instance of InvalidMessage", result.get(0) instanceof InvalidMessage);

        InvalidMessage invalid = (InvalidMessage) result.get(0);
        assertTrue("Error message should indicate wrong distance scale", invalid.getInfoMsg().contains("Wrong distance scale value"));
    }
}
