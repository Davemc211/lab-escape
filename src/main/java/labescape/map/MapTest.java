package labescape.map;

import java.util.Scanner;

public class MapTest {

    public static void main(String[] args) {
        run();

    }
     static void run(){

        Scanner input = new Scanner(System.in);

        boolean quit = false;

         Map map = new Map();
         map.generateBoard();
         map.getStartCords();
         map.displayBoard();

         do{
             System.out.println("EXITS : ");

             map.getExits().forEach(System.out::println);

             System.out.println("Please enter your move or press 'X' to exit: ");
             String move = input.nextLine().toLowerCase();

             for(Map.Direction dir : Map.Direction.values()){
                 if(move.equals(dir.getAbbreviation()) || move.equals(dir.getFullName())){
                     if(map.getExits().contains(dir)) {
                         map.move(dir);
                     }
                     else {
                         System.out.println("There's nothing in the direction");
                     }
                 }

             }
             if(move.equals("x")){
                 quit = true;
             }

         }while (!quit);
    }
}
