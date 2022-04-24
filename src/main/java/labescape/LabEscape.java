package labescape;

import labescape.combat.Combat;
import labescape.dao.*;
import labescape.model.*;
import labescape.map.Map;
import labescape.view.Menu;
import org.apache.commons.dbcp2.BasicDataSource;
import javax.sql.DataSource;
import java.util.*;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class LabEscape {
    public static int enemyCount = 0;
    public static int craftedCount = 0;
    public static int consumablesUsed = 0;
    public static int petsTamed = 0;
    public static int bossesKilled = 0;



    public static final String ANSI_RESET = "\u001B[0m";
    public static final String ANSI_RED = "\u001B[31m";
    public static final String ANSI_GREEN = "\u001B[32m";
    public static final String ANSI_YELLOW = "\u001B[33m";
    public static final String ANSI_PURPLE = "\u001B[35m";
    public static final String ANSI_CYAN = "\u001B[36m";
    public static final String YELLOW_UNDERLINED = "\033[4;33m";
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m";
    public static final String RED_BOLD_BRIGHT = "\033[1;91m";

    private static final String MAIN_MENU_OPTION_LOOK = "L";
    private static final String MAIN_MENU_OPTION_INVENTORY = "I";
    private static final String MAIN_MENU_OPTION_CHARACTER = "C";
    private static final String MAIN_MENU_OPTION_MAP = "M";
    private static final String MAIN_MENU_OPTION_SEARCH = "V";
    private static final String MAIN_MENU_OPTION_EXIT = "X";
    private static final String MAIN_MENU_OPTION_GET = "G";
    private static final String MAIN_MENU_OPTION_OPEN = "O";
    private static final String MAIN_MENU_OPTIONS_BUY = "B";
    private static final String MAIN_MENU_OPTIONS_ATTACK = "A";



    private static final Object[] MAIN_MENU_OPTIONS = new Object[] { MAIN_MENU_OPTION_LOOK,
            MAIN_MENU_OPTION_INVENTORY, MAIN_MENU_OPTION_CHARACTER, MAIN_MENU_OPTION_MAP,
            MAIN_MENU_OPTION_SEARCH, Map.Direction.SOUTH, Map.Direction.EAST,
            Map.Direction.NORTH, Map.Direction.WEST, MAIN_MENU_OPTION_EXIT, MAIN_MENU_OPTION_GET, MAIN_MENU_OPTION_OPEN, MAIN_MENU_OPTIONS_BUY,
            MAIN_MENU_OPTIONS_ATTACK};

    private static final String LOOT_DROP_MENU_OPTION_INSPECT = "I";
    private static final String LOOT_DROP_MENU_OPTION_DESTROY = "D";
    private static final String LOOT_DROP_MENU_OPTION_PICKUP= "P";
    private static final String LOOT_DROP_MENU_OPTION_EXIT= "X";
    private static final Object[] LOOT_DROP_MENU_OPTIONS = new Object[] {LOOT_DROP_MENU_OPTION_INSPECT,
        LOOT_DROP_MENU_OPTION_PICKUP, LOOT_DROP_MENU_OPTION_DESTROY, LOOT_DROP_MENU_OPTION_EXIT};

    private static final String INVENTORY_INSPECT_MENU_OPTION = "INSPECT";
    private static final String INVENTORY_EQUIP_MENU_OPTION = "EQUIP";
    private static final String INVENTORY_CRAFT_MENU_OPTION = "CRAFT";
    private static final String INVENTORY_EXIT_MENU_OPTION = "EXIT";
    private static final String INVENTORY_USE_MENU_OPTION = "USE";

    private static final Object[] INVENTORY_MENU_OPTIONS = new Object[] {INVENTORY_INSPECT_MENU_OPTION,
            INVENTORY_EQUIP_MENU_OPTION,INVENTORY_CRAFT_MENU_OPTION, INVENTORY_EXIT_MENU_OPTION, INVENTORY_USE_MENU_OPTION };


    private static final String COMBAT_MENU_ATTACK = "A";
    private static final String COMBAT_MENU_FLEE = "F";
    private static final String COMBAT_MENU_INVENTORY = "I";
    private static final String COMBAT_MENU_EXAMINE = "E";

    private static final Object[] COMBAT_MENU_OPTIONS = new Object[] {COMBAT_MENU_ATTACK, COMBAT_MENU_EXAMINE,
    COMBAT_MENU_FLEE, COMBAT_MENU_INVENTORY};

    private static final String CRAFTING_MENU_RECIPE = "R";
    private static final String CRAFTING_MENU_EXIT = "X";

    private static final Object[] CRAFTING_MENU_OPTIONS = new Object[] {CRAFTING_MENU_EXIT,CRAFTING_MENU_RECIPE};





    private final Menu menu;
    private final ContainerDao containerDao;
    private final EnemyDao enemyDao;
    private final LootDao lootDao;
    private final RoomDao roomDao;
    private final Random random = new Random();
    private final VendingMachine vendingMachine;


    public static void main(String[] args) {

        BasicDataSource dataSource = new BasicDataSource();
        dataSource.setUrl("jdbc:postgresql://localhost:5432/LabEscape");
        dataSource.setUsername("postgres");
        dataSource.setPassword("postgres1");

        LabEscape app = new LabEscape(dataSource);
        app.run();

    }
    public LabEscape(DataSource dataSource) {

            this.menu = new Menu(System.in, System.out);
            containerDao = new JdbcContainerDao(dataSource);
            enemyDao = new JdbcEnemyDao(dataSource);
            lootDao = new JdbcLootDao(dataSource);
            roomDao = new JdbcRoomDao(dataSource);
            vendingMachine = new VendingMachine();

        }

    private void run() {
        Combat combat = new Combat();
        Map map = new Map();
        Map map2 = new Map();
        Room room;
        map.generateBoard(); // fills map with room objects and randomly assigns impassable rooms'
        map.getStartCords(); // randomly assigns the starting row to one of the bottom "rooms". Also sets elevator
        Player player = new Player(15, 25);
        player.setStamina();
        player.setHp();



        boolean quit = false;

        displayTitleScreen();
        room = map.getCurrentRoom();
        room.setDescription(roomDao.getSpecificDescription(2));
        System.out.println(room.getDescription() + "\n");

        while (!quit) {


            room = map.getCurrentRoom();

            map.displayBoard();

            System.out.println(room.getDescription() + "\n");

            floorCheck(room); // checks for items previously dropped

            checkLocked(room); // checks for locked containers that need keys

            if(room.hasEnemies()) {

                checkEnemy(room, player, combat); // checks for enemies in the room
            }

            listPlayerOptions(room, player);


            System.out.println("There appear to be EXITS : ");
            map.getExits().forEach(System.out::println);

            Object choice = menu.getChoiceFromOptions(MAIN_MENU_OPTIONS);
            if (choice.equals(MAIN_MENU_OPTION_CHARACTER)) {
                characterSheet(player);

            }
            else if (choice.equals(MAIN_MENU_OPTION_INVENTORY)) {
                displayInventoryOptions(player);

            }
            else if (choice.equals(MAIN_MENU_OPTION_LOOK)) {
                System.out.println(room.getDescription());
                checkLocked(room);

            }
            else if (choice.equals(MAIN_MENU_OPTION_SEARCH)) {
                roomSearch(room, player);

            }
            else if (choice.equals(MAIN_MENU_OPTIONS_ATTACK)) {
                checkEnemy(room, player, combat);

            }
            else if (choice.equals(MAIN_MENU_OPTION_MAP)) {
                map.displayBoard();

            }
            else if (choice.equals(MAIN_MENU_OPTIONS_BUY)) {
                vendingMachineCheck(room);

            }
            else if (choice.equals(MAIN_MENU_OPTION_EXIT)) {
                quit = true;

            }
            else if (choice.equals(MAIN_MENU_OPTION_GET)) {

                if(room.getFloorItems().size() > 0) {
                    for(Loot loot : room.getFloorItems()) {
                        player.setInventory(loot);
                        System.out.println("You picked up the " + loot.getName());
                    }
                    room.setFloorItems(Collections.emptyList());
                    pause(1);
                }
                else{
                    System.out.println("There's nothing to pick up.");
                }

            } else if (choice instanceof Map.Direction) {
                map.move((Map.Direction) choice);
            } else {
                System.out.println("That input is invalid");
            }

        }
    }

    private void vendingMachineCheck(Room room) {
        if(room.hasVendingMachine()){

            vendingMachine.setVendItems(lootDao.getVendingStock(20));
            vendingMachine.vend();

        }

        else System.out.println("There's no ValueRep here.");

    }


    private void checkLocked(Room room) {

        if(room.getLockedContainer().size() > 0) {
            System.out.println("There's a locked " + room.getLockedContainer().get(0).getName() + " here.");
        }
    }

    private void checkEnemy(Room room, Player player, Combat combat) {
        List<Enemy> combatants;

        if (room.getRoomEnemies().size() == 0) {
            room.setRoomEnemies(enemyDao.getRandomEnemies(player.getLevel()));//generates list of enemies up to and including the player level
        }
        combatants = room.getRoomEnemies();


        if (player.isStealthed()) {
            player.setInCombat(true);
            combatMenu(player, combatants, combat, room);

        }

        String enemyOutput = "You see : ";
        for(Enemy enemy : combatants){
            if(enemy.getLevel() > player.getLevel()){
                enemyOutput += "☠" + enemy.getName()+ ", " + ANSI_RESET + "and ";
            }

            else {
                enemyOutput += enemy.getName()+ ", " + ANSI_RESET + "and ";
            }
            System.out.println(enemyOutput.substring(0, enemyOutput.length() - 8));

        }


        if (!combat.stealthCheck(player, combatants)) {
            System.out.printf(RED_BOLD_BRIGHT + "\n\n%5s%s", " ", "*****You've been spotted!*****\n");
            player.setInCombat(true);
            combatMenu(player, combatants, combat, room);
        } else {
            player.setStealthed(true);
            System.out.println(" You manage to remain out of sight.... for now.\n");
        }

    }


    private void floorCheck(Room room) {

        if (room.getFloorItems().size() > 0) {
            String lootOutput = "You see : ";
            for (Loot loot : room.getFloorItems()) {
                lootOutput += loot.getName() + ", ";
            }

            System.out.print(lootOutput.substring(0, lootOutput.length()-2) + " on the ground.");

        }
    }

    private void roomSearch(Room room, Player player) {

        if (!room.isSearched() && !player.isInCombat()) {

            room.setSearched(true);

            if (room.hasContainer()) {

                Container container = containerDao.getRandomBox();
                System.out.println("You search a nearby " + container.getName() + "....");
                checkContainer(room, player, container);

            } else if (!room.hasContainer()) {

                if (random.nextInt(10) > 4) {

                    lootDropMenu(room, player);

                } else {

                    System.out.println("There's nothing of interest here");
                }
            }

        } else if (player.isInCombat()) {

            System.out.println("You're fighting!");
        } else {
            System.out.println("You've already searched this room.");
        }
    }

    private void checkContainer(Room room, Player player, Container container) {

        if (container.isLocked()) {
            if (player.hasKey()) {

                System.out.println("It's locked, but you have a key!");
                System.out.println("You open the " + container.getName() + "...");
                lootDropMenu(room, player);


            } else {
                System.out.println("It's locked. Look around for a key...");
                room.setLockedContainer(container);
            }
        }
        else {
            lootDropMenu(room, player);
        }
    }

    private void lootDropMenu(Room room, Player player) {
        Loot drop;
        boolean quit = false;

        if(random.nextInt(10) >= 8){
            Recipe recipe = lootDao.getRecipe();
            if(!player.getKnownRecipes().contains(recipe)) {
                player.setKnownRecipes(recipe);
                System.out.println("You found a recipe to make a " + recipe.getName());
                quit = true;
            }

            else {
                System.out.println("You already know how to make " + recipe.getName());
                quit = true;
            }

        }

        if (random.nextInt(10) > 3) {

            drop = lootDao.getRandomItem();
            System.out.println("You've found a " + drop.getName() + " !");
            System.out.println();
            pause(2);

            while (!quit) {

                System.out.println("Press " + ANSI_CYAN + "'I'" + ANSI_RESET + " to " + ANSI_CYAN + " inspect\n" + ANSI_RESET);
                System.out.println("Press " + ANSI_RED + "'D'" + ANSI_RESET + " to " + ANSI_RED + " drop " + ANSI_RESET + "item.\n");
                System.out.println("Press " + GREEN_BOLD_BRIGHT + "'P'" + ANSI_RESET + " to " + GREEN_BOLD_BRIGHT + " pick up " + ANSI_RESET + "item.\n");
                System.out.println("Press " + ANSI_RED + "'X'" + ANSI_RESET + " to " + ANSI_RED + " exit" + ANSI_RESET + ".\n");

                var choice = (String) menu.getChoiceFromOptions(LOOT_DROP_MENU_OPTIONS);

                if (choice.equals(LOOT_DROP_MENU_OPTION_INSPECT)) {

                    System.out.println(drop.getDescription());
                    System.out.println();

                } else if (choice.equals(LOOT_DROP_MENU_OPTION_PICKUP)) {

                    System.out.println("You stow the " + drop.getName() + " for later.");
                    player.setInventory(drop);
                    pause(2);
                    quit = true;

                } else if (choice.equals(LOOT_DROP_MENU_OPTION_DESTROY)) {

                    room.addFloorItems(drop);
                    System.out.println("You drop the " + drop.getName() + " on the ground.");
                    pause(2);
                    quit = true;

                } else if (choice.equals(LOOT_DROP_MENU_OPTION_EXIT)) {
                    quit = true;
                }
            }
        } else {

            System.out.println("but there was nothing inside.");
            pause(1);

        }
    }

    public void listPlayerOptions(Room room, Player player){

        System.out.printf(ANSI_YELLOW + "%58s%s","OPTIONS: ", "\n" + ANSI_RESET);
        System.out.printf(ANSI_CYAN + "%9s%s%5s%s|", " ", "**'L' to LOOK **"," ", "--");
        System.out.printf("|%s%3s%s%2s%s|","--", " ", "**'I' for INVENTORY **"," ", "--");
        System.out.printf("|%s%3s%s%2s%s","--", " ", "**'V' to SEARCH the area **"," ", "\n" + ANSI_RESET);
        System.out.printf("%20s%s%s", " ", "-----------------------------------------------------------------","\n");
        System.out.printf(ANSI_GREEN + "%35s|%s%s%s%s%s|%s"," ", "--", " ", "'C' for CHARACTER sheet"," ", "--","\n" + ANSI_RESET);
        System.out.println();
        System.out.printf(ANSI_PURPLE + "%26s%s%s%s%1s%s|"," ","--", " ", "'M' to display MAP "," ", "--");
        System.out.printf("|%s%s%s%s%s%s","--", " ", "Input a direction to move"," ", "--","\n" + ANSI_RESET);

        if(room.getFloorItems().size() > 0){
            System.out.printf("%38s%s%s%s%s", " ",ANSI_RED+ "--(G)to get dropped items"," ", "--","\n" + ANSI_RESET); // this should only display if there are 'dropped' items
        }
        if(room.getLockedContainer().size() > 0) {
            System.out.printf("%38s%s%s%s%s", " ",ANSI_CYAN+ "--(O)to open discovered container."," ", "--","\n" + ANSI_RESET); // this should only display if there are locked containers

        }if(room.hasVendingMachine()){
            System.out.printf("%35s%s%s%s%s", " ",ANSI_YELLOW+ "--(B) to buy from the vending machine"," ", "--","\n" + ANSI_RESET);

        }if(room.getRoomEnemies().size() > 0 && !player.isInCombat() && player.isStealthed()){
            System.out.printf("%35s%s%s%s%s", " ",ANSI_RED+ "--(A) to attack while unseen"," ", "--","\n" + ANSI_RESET);
        }

    }
    public void displayInventoryOptions(Player player) {

        Loot currentItem;

        boolean quit = false;
        while(!quit) {

            List<Loot> inventory = player.getInventory();
            java.util.Map<String, Long> inventoryMap = inventory.stream()
                    .collect(Collectors.groupingBy(Loot::getName, Collectors.counting()));

            System.out.print(ANSI_CYAN + "-------------------------------------------------------------------------------------------------\n");
            System.out.print("---------------------------------------------INVENTORY-------------------------------------------\n");

            inventoryMap.entrySet().forEach(entry -> {
                System.out.printf("|%6s%43s%5s%8s%44s", ANSI_RESET+ " ", entry.getKey(), " | ", " QTY: " + entry.getValue(), ANSI_CYAN +" |\n");
            });

            System.out.print("-------------------------------------------------------------------------------------------------\n");
            System.out.print("-------------------------------------------------------------------------------------------------\n\n" + ANSI_RESET);


            System.out.printf("%s%.2f%s","You can carry : " , player.getCarryWeight()," more pounds.\n");
            System.out.println("You currently have " + ANSI_RED + player.getCurrentItem()
                    + ANSI_RESET + " equipped.\n");


            System.out.println("Type " + ANSI_CYAN + "'inspect'" + ANSI_RESET + " to look closer at an item.\n" +
                    "Type" + ANSI_CYAN + " 'equip' " + ANSI_RESET + "to equip an item.\n" +
                    "Type" + ANSI_CYAN + " 'use' " + ANSI_RESET + "to equip an item.\n" +
                    "Type" + ANSI_CYAN + " 'craft' " + ANSI_RESET + "to access the crafting menu.\n" +
                    "Type" + ANSI_CYAN + " 'exit' " + ANSI_RESET + "to escape the options menu.");

            Object choice = menu.getChoiceFromOptions(INVENTORY_MENU_OPTIONS);


            if (choice.equals(INVENTORY_INSPECT_MENU_OPTION)) {

                currentItem = (Loot)menu.getChoiceInventory(inventory);
                System.out.println(currentItem.getDescription());

            } else if (choice.equals(INVENTORY_CRAFT_MENU_OPTION)) {

                displayCraftingMenu(player, inventory);

            } else if(choice.equals(INVENTORY_USE_MENU_OPTION)) {

                currentItem = (Loot)menu.getChoiceInventory(inventory);

                if(currentItem.isUsable){
                    player.useItem(currentItem);
                }
                else {
                    System.out.println("You can't use that.");
                }

            } else if (choice.equals(INVENTORY_EQUIP_MENU_OPTION)) {

                currentItem = (Loot)menu.getChoiceInventory(inventory);
                player.setEquippedItem(currentItem);
                System.out.println("You equip the " + currentItem.getName());

            }else if (choice.equals(INVENTORY_EXIT_MENU_OPTION)){

                quit = true;
            }
        }
    }

    private void displayCraftingMenu(Player player, List<Loot> inventory) {
        boolean quit = false;
        while(!quit) {

            List<Recipe> playerKnownRecipes = player.getKnownRecipes();


            java.util.Map<String, Long> craftingMap = inventory.stream()
                    .filter(item -> item.getType().equals("Junk"))
                    .collect(Collectors.groupingBy(Loot::getName, Collectors.counting()));

            System.out.print(GREEN_BOLD_BRIGHT + "-----------------------------------------------------------------\n");
            System.out.print("----------------------------JUNK---------------------------------\n");

            craftingMap.entrySet().forEach(entry -> {
                System.out.printf("|%6s%20s%5s%8s%26s", " ", entry.getKey(), " | ", " QTY: " + entry.getValue(), " |\n");
            });

            System.out.print("-----------------------------------------------------------------\n");
            System.out.print("-----------------------------------------------------------------\n\n" + ANSI_RESET);

            System.out.println("Press (R) to try and craft a known recipe.");
            System.out.println("Press (X) to exit.");


            String choice = (String) menu.getChoiceFromOptions(CRAFTING_MENU_OPTIONS);

            if (choice.equals(CRAFTING_MENU_RECIPE)) {
                if (playerKnownRecipes.size() > 0) {

                    Recipe craftingChoice = menu.getChoiceCrafting(playerKnownRecipes);
                    if(craftItem(craftingChoice, inventory)){
                        player.setInventory(lootDao.getSpecificItem(craftingChoice.getName(), 1L)); // adds crafted item
                    }

                } else {
                    System.out.println("You don't know any recipes yet...");
                }
            } else if (choice.equals(CRAFTING_MENU_EXIT)) {
                quit = true;
            }
        }
    }

    private boolean craftItem(Recipe craftingChoice, List<Loot> inventory) {
        List<Loot> recipe = craftingChoice.getIngredients();

        if(inventory.containsAll(recipe)){
            inventory.removeAll(recipe);
            craftedCount++;
            return true;
        }
        else {
            System.out.println("You do not have enough material to craft :\n" + craftingChoice.getName());
            return false;
        }

    }


    public static void characterSheet(Player player){

        System.out.print(GREEN_BOLD_BRIGHT + "**************************\n");
        System.out.printf("*%10s%12s%s"," ", GREEN_BOLD_BRIGHT + "STATS", "         *"
                + ANSI_RESET + "\n");
        System.out.print(GREEN_BOLD_BRIGHT +"*" + ANSI_RESET +
                "    ATTACK POWER : " + player.getAttackPower() + GREEN_BOLD_BRIGHT + "   *"
                + ANSI_RESET + "\n");
        System.out.print(GREEN_BOLD_BRIGHT + "*" + ANSI_RESET + "       AGILITY : " + player.getAgility()
                + GREEN_BOLD_BRIGHT + "      *" +"\n");
        System.out.print(GREEN_BOLD_BRIGHT + "*" + ANSI_RESET + "      STRENGTH : " + player.getStrength() +
                GREEN_BOLD_BRIGHT + "      *" + "\n");
        System.out.print(GREEN_BOLD_BRIGHT + "*" + ANSI_RESET + "         HP : " + player.getHp() +
                GREEN_BOLD_BRIGHT + "         *" + "\n");
        System.out.print(GREEN_BOLD_BRIGHT + "**************************\n");
        System.out.println("   Press " + ANSI_CYAN + "any key to exit" + ANSI_RESET);
        String input = new Scanner(System.in).nextLine();

    }

    private void combatMenu(Player player, List<Enemy> enemies, Combat combat, Room room) {

        System.out.printf(ANSI_RED + "%5s%s"," ", "******PREPARE FOR COMBAT!*****\n\n" + ANSI_RESET);
        pause(3);

        while (player.isInCombat()) {

            if(player.isStealthed()){
                player.setStealthed(false);
                Enemy target = (Enemy) menu.getEnemyFromOptions(COMBAT_MENU_OPTIONS, enemies);
                System.out.println("You spring from the shadows and strike at " + target.getName());
                combat.stealthAttack(target, player);
                reportHealth(target);

                if (target.getHp() <= 0) {
                    enemyDeath(target,enemies,room,player);
                }
            }

            List<Combatant> turnOrder = combat.turnOrder(player, enemies);


            System.out.println(ANSI_RED + "*********************************************" + ANSI_RESET);
            System.out.println(ANSI_RED + "**************" + GREEN_BOLD_BRIGHT + "--TURN ORDER--" + ANSI_RED + "*****************\n" + ANSI_RESET);

            for (int i = 0; i < turnOrder.size(); i++) {

                System.out.println(i + 1 + " " + turnOrder.get(i).getName() + " \n");

            }
            System.out.println(ANSI_RED + "*********************************************" + ANSI_RESET + "\n\n");

            System.out.println("You're currently wielding : " + player.getCurrentItem() + "\n");
            for (Combatant c : turnOrder) {

                if (!(c instanceof Player)) {
                    if(combat.enemyAttack((Enemy) c, player)){
                        reportHealth(player);
                    }
                    pause(1);

                } else {

                    drawCombatMenu();

                    Object choice = menu.getChoiceFromOptions(COMBAT_MENU_OPTIONS);

                    if (choice.equals(COMBAT_MENU_ATTACK)) {

                        Enemy target = (Enemy) menu.getEnemyFromOptions(COMBAT_MENU_OPTIONS, enemies);

                        System.out.println("You strike at " + target.getName() + "!\n");
                        pause(1);

                        if (combat.playerAttack(target, player)) ;
                        reportHealth(target);
                        pause(1);

                        if (target.getHp() <= 0) {

                            enemyDeath(target, enemies, room, player);

                            if (enemies.size() == 0) {
                                player.setInCombat(false);
                                room.setEnemies(false);
                                break;
                            }
                        }
                        pause(1);


                    } else if (choice.equals(COMBAT_MENU_EXAMINE)) {
                        Enemy target = (Enemy) menu.getEnemyFromOptions(COMBAT_MENU_OPTIONS, enemies);
                        System.out.println(target.getDescription() + "\n\n");
                        pause(3);


                    } else if (choice.equals(COMBAT_MENU_FLEE)) {

                        System.out.println("You try to flee...");
                        for (Combatant enemy : turnOrder) {

                            if (player.rollAgility() <= enemy.rollAgility()) {
                                System.out.println("but you were stopped!");
                                break;
                            } else {

                                System.out.println("and you succeed!");
                                player.setInCombat(false);

                            }
                        }

                    } else if (choice.equals(COMBAT_MENU_INVENTORY)) {
                        displayInventoryOptions(player);
                    }
                }
            }
        }
    }

    private void enemyDeath(Enemy target, List<Enemy> enemies, Room room, Player player) {
        enemies.remove(target);
        room.setRoomEnemies(enemies);
        player.setXP(10);
        Loot drop = lootDao.getRandomJunkItem();
        System.out.println(target.getName() + " dropped something on the ground.");
        room.addFloorItems(drop);
        enemyCount++;


    }


    private void reportHealth(Combatant target) {
        boolean isEnemy = (target instanceof Enemy);
        int health = target.getHp();
        int stamina = target.getStamina();

        if(health >= stamina *.90){
            if(isEnemy) {
                System.out.println(ANSI_GREEN + target.getName() + " still looks healthy!" + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_GREEN + target.getName() + " are doing ok." + ANSI_RESET);
            }
        }
        else if(health >= stamina *.50) {
            if (isEnemy){
                System.out.println(ANSI_YELLOW + target.getName() + " looks wounded..." + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_YELLOW + target.getName() + " feel wounded." + ANSI_RESET);
            }
        }
        else if(health >= stamina *.25) {
            if(isEnemy) {
                System.out.println(ANSI_PURPLE + target.getName() + " looks very hurt." + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_PURPLE + target.getName() + " don't feel so good..." + ANSI_RESET);
            }
        }
        else if(health >= 1){
            if(isEnemy) {
                System.out.println(ANSI_RED + target.getName() + " is on death's door... and knocking." + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_RED + target.getName() + " are in danger!" + ANSI_RESET);
            }
        }
        else {
            if(isEnemy) {
                System.out.println(ANSI_CYAN + target.getName() + " falls to the floor, unconscious!" + ANSI_RESET);
            }
            else {
                System.out.println(ANSI_RED + target.getName() + " have died." + ANSI_RESET);
                displayDeathDialogue();
            }
        }
    }

    private void displayDeathDialogue() {
        System.out.println("\n\t\t" + roomDao.getSpecificDescription(7) + "\n");
        System.out.printf("%25s%s"," ","********************************************\n");
        System.out.printf("%25s%s"," ","*******************SUMMARY******************\n");
        System.out.printf("%25s%s%9s%s%4s%-5s%s%s"," ","*"," ", "Total enemies disabled:"," ",enemyCount," ", "*\n");
        System.out.printf("%25s%s%9s%s%7s%-5s%s%s"," ","*"," ", "Total items crafted:"," ",craftedCount," ", "*\n");
        System.out.printf("%25s%s%9s%s%4s%-5s%s%s"," ","*"," ", "Total consumables used:"," ",consumablesUsed," ", "*\n");
        System.out.printf("%25s%s%9s%s%10s%-5s%s%s"," ","*"," ", "Total pets tamed:"," ",petsTamed," ", "*\n");
        System.out.printf("%25s%s%9s%s%7s%-5s%s%s"," ","*"," ", "Total bosses killed:"," ",bossesKilled," ", "*\n");
        System.out.printf("%25s%s"," ","********************************************\n");
        System.out.printf("%25s%s"," ","********************************************\n");
        System.exit(1);

    }

    private void drawCombatMenu() {

        System.out.print(ANSI_RED + "*********************************************" + "\n");
        System.out.print(ANSI_RED + "****************COMBAT MENU******************" + "\n");
        System.out.printf("%s%12s%s%12s%s", "*"," ","Press 'A' to attack"," ","*\n" );
        System.out.printf("%s%5s%s%5s%s", "*"," ","Press 'E' to examine your enemies"," ","*\n" );
        System.out.printf("%s%9s%s%9s%s", "*"," ","Press 'F' to try and flee"," ","*\n" );
        System.out.printf("%s%10s%s%10s%s", "*"," ","Press 'I' for inventory"," ","*\n" );
        System.out.println("*********************************************" + ANSI_RESET + "\n");

    }

    private void pause(int seconds) {
        try {
            TimeUnit.SECONDS.sleep(seconds);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }

    private void displayTitleScreen() {
        Scanner scan = new Scanner(System.in);
        System.out.println("\n" +
                "$$\\                 $$\\             $$$$$$$$\\                                                   $$\\ \n" +
                "$$ |                $$ |            $$  _____|                                                  $$ |\n" +
                "$$ |       $$$$$$\\  $$$$$$$\\        $$ |       $$$$$$$\\  $$$$$$$\\  $$$$$$\\   $$$$$$\\   $$$$$$\\  $$ |\n" +
                "$$ |       \\____$$\\ $$  __$$\\       $$$$$\\    $$  _____|$$  _____| \\____$$\\ $$  __$$\\ $$  __$$\\ $$ |\n" +
                "$$ |       $$$$$$$ |$$ |  $$ |      $$  __|   \\$$$$$$\\  $$ /       $$$$$$$ |$$ /  $$ |$$$$$$$$ |\\__|\n" +
                "$$ |      $$  __$$ |$$ |  $$ |      $$ |       \\____$$\\ $$ |      $$  __$$ |$$ |  $$ |$$   ____|    \n" +
                "$$$$$$$$\\ \\$$$$$$$ |$$$$$$$  |      $$$$$$$$\\ $$$$$$$  |\\$$$$$$$\\ \\$$$$$$$ |$$$$$$$  |\\$$$$$$$\\ $$\\ \n" +
                "\\________| \\_______|\\_______/       \\________|\\_______/  \\_______| \\_______|$$  ____/  \\_______|\\__|\n" +
                "                                                                            $$ |                    \n" +
                "                                                                            $$ |                    \n" +
                "                                                                            \\__|                    \n"

        );
        System.out.println("\033[3m\n\t\t"+roomDao.getSpecificDescription(5) + "\033[0m\n");
        scan.nextLine();
        System.out.println(roomDao.getSpecificDescription(1)); // game intro and credits
        scan.nextLine();
        System.out.println("\t\t" + roomDao.getSpecificDescription(3) + "\n\n");
        scan.nextLine();
    }
}



