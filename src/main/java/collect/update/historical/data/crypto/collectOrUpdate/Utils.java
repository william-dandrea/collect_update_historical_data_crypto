package collect.update.historical.data.crypto.collectOrUpdate;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;

/**
 * @author D'Andréa William
 */
public class Utils {



    public static String generateDateString(long timeStamp) {

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy MMM dd HH:mm:ss");
        Calendar calendar = new GregorianCalendar();
        calendar.setTimeInMillis(timeStamp);
        return sdf.format(calendar.getTime());
    }
}
