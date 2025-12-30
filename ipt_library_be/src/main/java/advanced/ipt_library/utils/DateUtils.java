package advanced.ipt_library.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateUtils {

    private static String convertStringToDate(Date date, String pattern) {
        SimpleDateFormat sdf = new SimpleDateFormat(pattern);
        return sdf.format(date);
    }
}
