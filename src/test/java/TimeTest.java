import static org.junit.Assert.assertEquals;

import org.junit.Test;

import com.edsv.Time;

public class TimeTest {
 
    @Test
    public void testDifference() {
        Time t1 = new Time(8, 0);
        Time t2 = new Time(7, 56);
        assertEquals(60, t2.getDifference(t1));
    }
}
