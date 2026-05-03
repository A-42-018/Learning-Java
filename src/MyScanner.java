import java.util.Scanner;

public class MyScanner {
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);

        System.out.println("Enter your Real Name: ");
        String name = scanner.nextLine();
        System.out.println("Enter your age :");
        int age = scanner.nextInt();
        System.out.println("Your name is : "+ name);
        System.out.println("Your age is "+ age + " years old!!");

    }
}