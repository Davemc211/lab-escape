package labescape.model;


import java.util.*;

public class Player implements Comparable<Enemy>, Combatant{
    boolean isStamAffected; // prevents double debuff timer
    boolean isStrengthAffected;
    boolean isAgilityAffected;

    boolean agilityBuff; // prevents double food buff timer
    boolean strengthBuff;

    int agilityModifier;
    int strengthModifier;
    String agilityItemName;
    String strengthItemName;

    Timer timer = new Timer();

    TimerTask stamRestoreTask = new TimerTask() {

        @Override
        public void run() {
            updateStamina(+2);
            System.out.println("Your stamina recovers...");
            timer.cancel();
            isStamAffected = false;
        }
    };

    TimerTask agilityRestoreTask = new TimerTask() {

        @Override
        public void run() {
            updateAgility(+2);
            System.out.println("Your agility recovers...");
            timer.cancel();
            isAgilityAffected = false;
        }
    };

    TimerTask strengthRestoreTask = new TimerTask() {

        @Override
        public void run() {
            updateAgility(+2);
            System.out.println("Your strength recovers...");
            timer.cancel();
            isStrengthAffected = false;
        }
    };

    TimerTask stamReduceTask = new TimerTask() {

        @Override
        public void run() {
            updateStamina(+2);
            System.out.println("Your stamina recovers...");
            timer.cancel();
            isStamAffected = false;
        }
    };

    TimerTask agilityReduceTask = new TimerTask() {

        @Override
        public void run() {
            updateAgility(-agilityModifier);
            System.out.println("The effect of " + agilityItemName + " has worn off.");
            timer.cancel();
            agilityBuff = false;
        }
    };

    TimerTask strengthReduceTask = new TimerTask() {

        @Override
        public void run() {
            updateStrength(-strengthModifier);
            System.out.println("The effect of " + strengthItemName + " has worn off.");
            timer.cancel();
            strengthBuff = false;
        }
    };

    private final Random random = new Random();
    private boolean inCombat, isStealthed;
    private int strength, agility, attackPower, stamina, level, XP, bucks, hp;
    private List<Loot> inventory = new ArrayList<>();
    private List<Recipe> knownRecipes = new ArrayList<>();
    private boolean hasKey;
    private Loot equippedItem;
    private double carryWeight = this.strength * 3.0;


    public Player(int strength, int agility) {

        this.strength = strength;
        this.agility = agility;
        this.attackPower = strength * 2;
        this.stamina = strength * 4;
        this.level = 2;
        this.XP = 0;
        this.hasKey = false;
        setCarryWeight();

    }

    public int getStrength() {
        return strength;
    }

    public void setStrength(int strength) {
        this.strength = strength;
    }

    public void updateStrength(int modifier) {
        this.strength += modifier;
    }

    public int getAgility() {
        return agility;
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }


    public void updateAgility(int modifier){
        this. agility += modifier;
    }

    public String getCurrentItem() {
        if (this.equippedItem == null) {
            return "no weapon";
        } else {
            return this.equippedItem.getName();
        }
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower(int attackPower) {
        this.attackPower = attackPower;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina() { // should be used on creation;
        this.stamina = getStrength() * 4;
    }

    public void updateStamina(int modifier) { // should be used for combat
        this.stamina += modifier;
    }

    public void SetInitialStamina(int stamina){ // for rolling new player. setStamina is for combat updates ONLY
        this.stamina = stamina;
    }

    public void setInCombat(boolean inCombat) {
        this.inCombat = inCombat;
    }

    public boolean isStealthed() {
        return isStealthed;
    }

    public void setStealthed(boolean stealthed) {
        isStealthed = stealthed;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public void setXP(int XP) {
        this.XP = XP;
    }

    public int getLevel() {
        return level;
    }

    public int getXP() {
        return XP;
    }

    public List<Loot> getInventory() {
        return inventory;
    }

    public boolean setInventory(List<Loot> items) {
        for(Loot item: items) {

            if (item.getType().equals("Key")) {
                setHasKey(true);
            }
            if (this.getCarryWeight() > 0) {

                inventory.add(item);
                updateCarryWeight(item.getWeight());

            } else {
                System.out.println("You can't carry any more.");
                return false;
            }
        }
        return true;
    }

    public boolean setInventory(Loot item) {

        if (item.getType().equals("Key")) {
            setHasKey(true);
        }
        if (item instanceof Recipe) {
            this.setKnownRecipes((Recipe) item);
        }
        if (this.getCarryWeight() > 0) {
            inventory.add(item);
            updateCarryWeight(item.getWeight());
            return true;
        } else {
            System.out.println("You can't carry any more.");
            return false;
        }

    }

    public void removeItemFromInventory(Loot item){
        this.inventory.remove(item);
    }

    public boolean hasKey() {
        return hasKey;
    }

    public void setHasKey(boolean hasKey) {
        this.hasKey = hasKey;
    }

    public int rollAgility(){
        return random.nextInt(this.agility);
    }

    public int rollStrength(){
        return random.nextInt(this.strength);
    }

    public int rollStamina(){
        return random.nextInt(this.stamina);
    }

    public boolean isInCombat() {
        return inCombat;
    }

    public boolean isHasKey() {
        return hasKey;
    }

    public double getCarryWeight() {
        return this.carryWeight;
    }

    public void setCarryWeight() {
        this.carryWeight = this.strength * 3.0;
    }

    public void updateCarryWeight(double weight) {
        this.carryWeight -= weight;
    }

    @Override
    public int compareTo(Enemy enemy) {
        if(this.rollAgility() > enemy.rollAgility()){
            return -1;
        } else if (this.rollAgility() < enemy.rollAgility()){
            return 1;
        }
        return 0;
    }

    @Override
    public String getDescription() {
        return null;
    }

    @Override
    public String getName() {
        return "You";
    }

    public int rollAttackPower() {
        return random.nextInt(attackPower);
    }

    public int getBucks() {
        return bucks;
    }

    public void setBucks(int bucks) {
        this.bucks = bucks;
    }

    public Loot getEquippedItem() {
        return equippedItem;
    }

    public void setEquippedItem(Loot equippedItem) {
        this.equippedItem = equippedItem;
    }

    public List<Recipe> getKnownRecipes() {
        return knownRecipes;
    }

    public void setKnownRecipes(Recipe recipe) {

        if(this.knownRecipes.contains(recipe)) {

            System.out.println("You already know this recipe.");
        }
        else {
            this.knownRecipes.add(recipe);
        }
    }

    public void stamTimer() {
        timer.schedule(stamRestoreTask, 30000);
        isStamAffected = true;
    }

    public void setAgilityTask() {
        timer.schedule(agilityRestoreTask, 30000);
        isAgilityAffected = true;
    }

    public void setStrengthTask() {
        timer.schedule(strengthRestoreTask, 30000);
        isStrengthAffected = true;
    }

    public boolean isStamAffected() {
        return isStamAffected;
    }

    public boolean isStrengthAffected() {
        return isStrengthAffected;
    }

    public boolean isAgilityAffected() {
        return isAgilityAffected;
    }

    public int getHp() {
        return hp;
    }

    public void setHp() {
        this.hp = this.stamina; // only for setting initial HP
    }

    public boolean updateHp(int hp){

        int hpTest = this.hp += hp;

        if(hpTest > this.getStamina()){
            System.out.println("You are already at full health.");
            return false;
        }

        else{
            this.hp = hpTest;
        }

        return true;
    }

    public void useItem(Loot item) {

        String[] use = item.getEffect().split(",");
        String effect = use[0];
        int value = Integer.parseInt(use[1]);

        if(effect.equals("HP")){
            if(this.updateHp(value)){
                System.out.println("Yum!");
                this.removeItemFromInventory(item);
            }

        }
        else if(effect.equals("A")){
            if(boostAgility(value)){
                System.out.println("You feel more agile...");
                this.removeItemFromInventory(item);
            }
        }
        else if(effect.equals("S")){
            if(boostStrength(value)){
                System.out.println("You feel stronger...");
                this.removeItemFromInventory(item);
            }
        }
        else {
            System.out.println("That can't be used.");
        }

    }

    private boolean boostAgility(int value) {
        if(!agilityBuff) {

            this.updateAgility(+value);
            timer.schedule(agilityReduceTask, 600000);
            agilityBuff = true;
            return true;
        }
        else {
            return false;
        }
    }

    private boolean boostStrength(int value){
        if(!strengthBuff) {

            this.updateStrength(+value);
            timer.schedule(stamReduceTask, 600000);
            strengthBuff = true;
            return true;
        }
        else {
            return false;
        }
    }
}
