import java.time.LocalDate;
import java.time.LocalTime;

public class TestLocalDate {
    public static void main(String[] args) {
        LocalDate localDate = LocalDate.of(2023, 4, 23);
        System.out.println("localDate.getYear() = " + localDate.getYear());
        System.out.println("localDate.getMonth() = " + localDate.getMonth());
        System.out.println("localDate.getMonthValue() = " + localDate.getMonthValue());
        System.out.println("localDate.getDayOfMonth() = " + localDate.getDayOfMonth());
        System.out.println("localDate.getDayOfWeek() = " + localDate.getDayOfWeek().getValue());
        System.out.println("localDate.lengthOfMonth() = " + localDate.lengthOfMonth());
        System.out.println("localDate.lengthOfYear() = " + localDate.lengthOfYear());
        System.out.println("localDate.isLeapYear() = " + localDate.isLeapYear());
        System.out.println("localDate = " + localDate);
        System.out.println("LocalDate.now() = " + LocalDate.now());

        LocalTime localTime = LocalTime.of(12, 20, 50);
        System.out.println("localTime.getHour() = " + localTime.getHour());
    }
}
