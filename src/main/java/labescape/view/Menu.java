package labescape.view;


import labescape.map.Map;
import labescape.model.Enemy;
import labescape.model.Loot;
import labescape.model.Player;
import labescape.model.Recipe;


import java.io.InputStream;
import java.io.OutputStream;
import java.io.PrintWriter;
import java.util.List;
import java.util.Scanner;


public class Menu {

    private PrintWriter out;
    private Scanner in;

    public Menu(InputStream input, OutputStream output) {
        this.out = new PrintWriter(output);
        this.in = new Scanner(input);
    }

    public Object getChoiceFromOptions(Object[] options) {
        Object choice = null;
        while (choice == null) {
            choice = getChoiceFromUserInput(options);
        }
        return choice;
    }

    private Object getChoiceFromUserInput(Object[] options) {
        Object choice = null;
        String userInput = in.nextLine().toUpperCase();
        try {
            for (Map.Direction dir : Map.Direction.values()) {
                if (userInput.equals(dir.getAbbreviation()) || userInput.equals(dir.getFullName())) {
                    choice = dir;
                }
            }
            for(Object obj: options) {
                if (obj.equals(userInput)){
                    choice = obj;
                }
            }

        } catch (NumberFormatException e) {

        }
        if (choice == null) {
            System.out.println("\n*** " + userInput + " is not a valid option ***\n");
        }
        return choice;
    }

    private void displayMenuOptions(List options) {
        out.println();
        for (int i = 0; i < options.size(); i++) {
            int optionNum = i + 1;
            out.println(optionNum + ") " + options.get(i).toString());
        }
        out.print("\n: ");
        out.flush();
    }

    public Object getEnemyFromOptions(Object[] combatMenuOptions, List<Enemy> combatants) {

        Object item = null;

        if(combatants.size() == 1){
            return combatants.get(0);
        }
        else {
            displayMenuOptions(combatants);
            try {
                int listChoice = Integer.parseInt(in.nextLine());
                item = combatants.get(listChoice - 1);

            } catch (Exception e) {

                System.out.println("Invalid entry. Please choose an enemy to attack.");
                getEnemyFromOptions(combatMenuOptions, combatants);

            }
        }
        return item;
    }

    public Object getChoiceInventory(List<Loot> inventory) {
        Object item = null;

        if(inventory.size() == 1){
            return inventory.get(0);
        }
        else {
            displayMenuOptions(inventory);
            try {
                int listChoice = Integer.parseInt(in.nextLine());
                item = inventory.get(listChoice - 1);

            } catch (Exception e) {

                System.out.println("Invalid entry.");
                getChoiceInventory(inventory);

            }
        }
        return item;
        
    }

    public Recipe getChoiceCrafting(List<Recipe> playerKnownRecipes) {
        Recipe choice = null;

            displayMenuOptions(playerKnownRecipes);
            try {
                int listChoice = Integer.parseInt(in.nextLine());
                choice = playerKnownRecipes.get(listChoice - 1);

            } catch (Exception e) {

                System.out.println("Invalid entry.");
                getChoiceCrafting(playerKnownRecipes);

            }

        return choice;
    }
}


