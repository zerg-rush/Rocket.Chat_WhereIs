package pl.aszul.hot.rwb.common;

import java.time.Duration;

/**
 * Helper class with utility methods
 */
public class Util {

    public static String positionToOrdinal(int position) {
        position++;
        String[] suffixes = new String[]{"th", "st", "nd", "rd", "th", "th", "th", "th", "th", "th"};
        switch (position % 100) {
            case 11:
            case 12:
            case 13:
                return position + "th";
            default:
                return position + suffixes[position % 10];
        }
    }

    private static String plurarOrNot(final long number) {
        return number > 1 ? "s" : "";
    }

    public static String durationFormat(Duration duration) {
        var days = duration.toDays();
        duration = duration.minusDays(days);

        var hours = duration.toHours();
        duration = duration.minusHours(hours);

        var minutes = duration.toMinutes();
        duration = duration.minusMinutes(minutes);

        var seconds = duration.getSeconds();

        return
                (days == 0 ? "" : days + " day" + plurarOrNot(days) + ", ") +
                        (hours == 0 ? "" : hours + " hour" + plurarOrNot(hours) + ", ") +
                        (minutes == 0 ? "" : minutes + " minute" + plurarOrNot(minutes) + ", ") +
                        (seconds == 0 ? "" : seconds + " second" + plurarOrNot(seconds));
    }

}
