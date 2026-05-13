
import java.util.Scanner;

public class Arithmetic_operator {
    public static void main(String[] args){

        // Shopping Cart Program

        String item;
        int quantity;
        float price;
        char currency = '$';
        float total;

        Scanner cart = new Scanner(System.in);

        System.out.print("What item would you like to buy?: ");
        item = cart.nextLine();

        System.out.print("Whats the price of the item? ");
        price = cart.nextFloat();

        System.out.print("Whats the quantity of the item? ");
        quantity = cart.nextInt();

        total = price * quantity;
        System.out.println("The total price is " + total + " " + currency);

    }
}
