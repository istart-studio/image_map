package studio.istart.framework.tool;

import java.time.Instant;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;

/**
 * @author Se7en
 * @date 2016/4/29
 * Desc:
 */
public class DateTime {
    private Instant current;


    private DateTime(Instant instant) {
        current = instant;
    }

    public static DateTime now() {
        return new DateTime(Instant.now());
    }

    public static DateTime set(String timeString, String pattern, String timeZone) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(pattern).withZone(ZoneId.of(timeZone));
        Instant instant = formatter.parse(timeString, Instant::from);
        return new DateTime(instant);
    }

    private void setCurrent(Instant current) {
        this.current = current;
    }

    private static final String formatPattern = "yyyy-MM-dd HH:mm:ss:SSS";

    @Override
    public String toString() {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(formatPattern).withZone(ZoneId.systemDefault());
        return formatter.format(current);
    }

    public String toString(String format) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(format).withZone(ZoneId.systemDefault());
        return formatter.format(current);
    }

    public DateTime addDays(int day) {
        this.setCurrent(current.plus(day, ChronoUnit.DAYS));
        return this;
    }

    public long toSeconds() {
        return this.current.getEpochSecond();
    }

    public long toMills() {
        return this.current.toEpochMilli();
    }
}
