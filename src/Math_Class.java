import java.util.Scanner;

public class Math_Class {
    public static void main(String[] args) {

        double radius;
        double volume;
        double area;
        double circumference;
        Scanner Math1 = new Scanner(System.in);
        System.out.print("Enter radius of circle: ");
        radius = Math1.nextDouble();

        circumference = 2 * Math.PI * radius;
        area = Math.PI * Math.pow(radius, 2);
        volume = (4.0 / 3.0) * Math.PI * Math.pow(radius, 3);
        System.out.println("The circumference is: " + circumference + " cm");
        System.out.println("The area is: " + area + " cm^2");
        System.out.println("The volume is: " + volume + " cm^3");

        Math1.close();

    }
}
