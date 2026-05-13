import java.util.Scanner;

import static java.util.Scanner.*;

public class If_Statement {
    static void main(String[] args){

        String name;
        Scanner Name = new Scanner(System.in);
        System.out.print("Enter your name 🗣️: ");
        name = Name.nextLine();

        int age;
        Scanner Age = new Scanner(System.in);
        System.out.print("Enter your age 🗣️: ");
        age = Age.nextInt();

        // Group 1
        if(name.isEmpty()){
            System.out.println("You did not enter a name!");
        }
        else{
            System.out.println("Hello!👋 "+ name);
        }

        // Group2
    if(age >= 65){
        System.out.println("You are a senior!👳‍♀️");
        }
    else if(age == 0){
        System.out.println("You are a Baby!");
    }
    else if(age >= 18){
        System.out.println("You are an adult.");
    }
    else{
        System.out.println("You are a child!");

         }
    Name.close();
    Age.close();
    }
}
