package labescape.model;

import labescape.view.Menu;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class VendingMachine {

    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String ANSI_RESET = "\u001B[0m";

    private List<Loot> vendItems = new ArrayList<>();

    private final String VENDING_MACHINE_MENU_DISPLAY = "D";
    private final String VENDING_MACHINE_MENU_PURCHASE = "P";
    private final String VENDING_MACHINE_MENU_EXIT = "X";

    private final String [] VENDING_MACHINE_OPTIONS = {VENDING_MACHINE_MENU_DISPLAY,
            VENDING_MACHINE_MENU_EXIT,VENDING_MACHINE_MENU_PURCHASE};


    private final Menu menu = new Menu(System.in, System.out);



    public void vend() {
        boolean quit = false;
        while (!quit) {

            Map<Loot, Long> vendingList = vendItems.stream()
                    .collect(Collectors.groupingBy(x -> x, Collectors.counting()));

            System.out.print(ANSI_CYAN + "-------------------------------------------------------------------------------------------------\n");
            System.out.print("--------------------------------THANK YOU FOR CHOOSING VALUEREP™----------------------------SLOT-\n");

            vendingList.forEach((key, value) -> System.out.printf("|%6s%43s%5s%8s%44s", ANSI_RESET + " ", key.getName(), " | ", " QTY: " + value, ANSI_CYAN + key.getVendSlot() + " |\n"));

            System.out.print("-------------------------------------------------------------------------------------------------\n");
            System.out.print("------------------------- Press (P) to purchase an item, or (X) to exit -------------------------\n");
            System.out.print("-------------------------------------------------------------------------------------------------\n\n" + ANSI_RESET);


            Object choice = (String) menu.getChoiceFromOptions(VENDING_MACHINE_OPTIONS);

            if (choice.equals(VENDING_MACHINE_MENU_DISPLAY)) {

            } else if (choice.equals(VENDING_MACHINE_MENU_PURCHASE)) {

            } else if (choice.equals(VENDING_MACHINE_MENU_EXIT)) {
                quit = true;
            }

        }

    }

    public List<Loot> getVendItems() {
        return vendItems;
    }

    public void setVendItems(List<Loot> vendItems) {
        this.vendItems = vendItems;
    }
}
