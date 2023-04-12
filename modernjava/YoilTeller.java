import java.time.LocalDate;
import java.util.InputMismatchException;
import java.util.Scanner;

public class YoilTeller {

    private static final String[] WEEK = new String[]{"일", "월", "화", "수", "목", "금", "토"};

    public static void main(String[] args) {


        int inputYear = getIntFromScannerWithRange(1900, 2000);
        int inputMonth = Integer.parseInt(String.format("%02d", getIntFromScannerWithRange(1, 12)));
        int inputDay = getIntFromScannerWithRange(1, 31);

        LocalDate localDate = LocalDate.of(inputYear, inputMonth, inputDay);
        System.out.println("해당 요일은 " + WEEK[localDate.getDayOfWeek().getValue()] + "요일 입니다");
    }

    /**
     * Scanner를 사용하여 사용자로부터 정수를 입력받는 메소드입니다.
     * 입력 가능한 범위는 인자로 전달된 min과 max 사이의 값입니다.
     * 범위를 벗어난 값이 입력되면, 오류 메시지를 출력하고 다시 입력을 받습니다.
     * @param min 입력 가능한 최소값
     * @param max 입력 가능한 최대값
     * @return 사용자가 입력한 정수
     */
    private static int getIntFromScannerWithRange(int min, int max) {
            Scanner sc = new Scanner(System.in);
            int num = 0;
            do {
                try {
                    num = sc.nextInt();
                } catch (InputMismatchException e) {
                    System.out.println("숫자만 입력가능합니다.");
                    sc.next();
                    continue;
                }
                if (isNotCorrectNum(num, min, max)) {
                    System.out.println(min + " ~ " + max + " 사이만 가능합니다");
                }
            } while (isNotCorrectNum(num, min, max));
            return num;
        }
    public static boolean isNotCorrectNum(int num, int min, int max) {
        return min > num || max < num;
    }
}
