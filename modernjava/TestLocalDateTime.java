import java.time.*;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeFormatterBuilder;
import java.time.format.SignStyle;
import java.time.format.TextStyle;
import java.time.temporal.*;
import java.util.Locale;
import java.util.TimeZone;

public class TestLocalDateTime {

    public static void main(String[] args) {



        LocalDateTime now = LocalDateTime.now();

        LocalDateTime of = LocalDateTime.of(1992, Month.DECEMBER, 22, 0, 0, 0);
        System.out.println("of = " + of);
        LocalDate localDate = LocalDate.of(1992, Month.DECEMBER, 22);

        long l = now.toLocalDate().until(localDate).get(ChronoUnit.YEARS);
        int days = localDate.until(now.toLocalDate()).getDays();
        System.out.println("days = " + days);
        System.out.println("l = " + l);


//        System.out.println("now = " + now);
//        LocalDate localDate = now.toLocalDate();
//        LocalTime localTime = now.toLocalTime();
//
//        Period between = Period.between(LocalDate.of(2022, 4, 7), LocalDate.now());
//        System.out.println("between.getMonths() = " + between.getYears());
//
//        TemporalAdjuster temporalAdjuster = TemporalAdjusters.lastDayOfMonth();
//        System.out.println("temporalAdjuster = " + temporalAdjuster);
//
//        LocalDate with = LocalDate.now().with(temporalAdjuster);
//        with.with(TemporalAdjusters.previous(DayOfWeek.FRIDAY));
//        System.out.println("with = " + with);
//
//        TemporalAdjuster temporalAdjuster1 = (Temporal temporal) -> {
//            DayOfWeek dayOfWeek = DayOfWeek.of(temporal.get(ChronoField.DAY_OF_WEEK));
//            int addToDay = 1;
//            if (dayOfWeek == DayOfWeek.FRIDAY) {
//                addToDay = 3;
//            } else if (dayOfWeek == DayOfWeek.SATURDAY) {
//                addToDay = 2;
//            }
//            return temporal.plus(addToDay, ChronoUnit.DAYS);
//        };
//        LocalDate with1 = with.with(temporalAdjuster1);
//
//
//        DateTimeFormatter dateTimeFormatter = new DateTimeFormatterBuilder()
//                .appendValue(ChronoField.YEAR, 4)
//                .appendLiteral("/")
//                .appendValue(ChronoField.MONTH_OF_YEAR, 1, 2, SignStyle.NOT_NEGATIVE)
//                .appendLiteral("-")
//                .appendValue(ChronoField.DAY_OF_MONTH, 1, 2, SignStyle.NOT_NEGATIVE)
//                .toFormatter(Locale.KOREA);
//
//        LocalDate parse = LocalDate.parse("2022/1-21", dateTimeFormatter);
//        System.out.println("parse = " + parse);
//
//        TimeZone aDefault = TimeZone.getDefault();
//        System.out.println("aDefault = " + aDefault);
//        System.out.println("aDefault.toZoneId() = " + aDefault.toZoneId());

    }
}
