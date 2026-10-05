import java.util.Random;

public class Random_Number {
    public static void main(String[] args) {

        Random random = new Random();
        int number = random.nextInt(100000, 600000);
        System.out.println(number);

        boolean isHead;
        isHead = random.nextBoolean();
        if (isHead) {
            System.out.println("HEADS!!!😭");
        }
        else {
            System.out.println("TAILS!!!😭");
        }
    }
}