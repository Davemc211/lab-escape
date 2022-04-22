package labescape.model;

import java.util.*;

public abstract class Enemy implements Comparable<Player>, Combatant{

    public static final String RED_BOLD_BRIGHT = "\033[1;91m";   // RED
    public static final String GREEN_BOLD_BRIGHT = "\033[1;92m"; // GREEN
    public static final String YELLOW_BOLD_BRIGHT = "\033[1;93m";// YELLOW
    public static final String BLUE_BOLD_BRIGHT = "\033[1;94m";  // BLUE
    public static final String PURPLE_BOLD_BRIGHT = "\033[1;95m";// PURPLE
    public static final String CYAN_BOLD_BRIGHT = "\033[1;96m";  // CYAN
    public static final String ANSI_RESET = "\u001B[0m";

    private final Random random = new Random();
    private String name;
    private String description;
    private int strength, agility, stamina, attackPower, level, hp;
    private List<Integer> mutationIDs = new ArrayList<>();
    private boolean mutated;
    private Set<String> mutations = new HashSet<>();


    public Enemy() {

    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setStrength(int strength) {
        this.strength = strength;
        setAttackPower();
    }

    public void setAgility(int agility) {
        this.agility = agility;
    }

    public int getStamina() {
        return stamina;
    }

    public void setStamina(int stamina) {
        this.stamina = stamina;
    }

    public int getAttackPower() {
        return attackPower;
    }

    public void setAttackPower() {
        this.attackPower = strength * this.level/2;
    }

    public String getName() {
        return name;
    }

    public int getStrength() {
        return strength;
    }

    public int getAgility() {
        return agility;
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

    public int rollAttackPower(){
        return random.nextInt(this.attackPower);
    }

    @Override
    public int compareTo(Player player) {
        if(this.rollAgility() > player.rollAgility()){
            return -1;
        } else if (this.rollAgility() < player.rollAgility()){
            return 1;
        }
        return 0;
    }

    public void setLevel(int levelNumber){
        this.level = levelNumber;
        this.strength = strength * levelNumber;
        this.agility = agility * levelNumber;
        this.stamina = stamina * levelNumber;

    }

    public int getLevel() {
        return level;
    }

    public boolean isMutated() {
        return mutated;
    }

    public void setMutated(boolean mutated) {
        this.mutated = mutated;
    }

    public Set<String> getMutations() {
        return mutations;
    }

    public void setMutations(String mutation) {
        this.mutations.add(mutation);
    }

    public void setMutated(){
        this.mutated = random.nextBoolean();
        if(this.mutated){
            int ability = random.nextInt(7);
            switch (ability) {
                case 1:
                    this.setMutations("Flying ");
                    this.setName(BLUE_BOLD_BRIGHT + "Flying " + ANSI_RESET + this.getName());
                    this.agility += this.getLevel();
                    this.setMutationIDs(1);
                    break;
                case 2:
                    this.setMutations("Powerful ");
                    this.setName(RED_BOLD_BRIGHT + "Powerful " + ANSI_RESET + this.getName());
                    this.strength += this.getLevel();
                    this.setMutationIDs(2);
                    break;
                case 3:
                    this.setMutations("Shocking ");
                    this.setName(YELLOW_BOLD_BRIGHT + "Shocking " + ANSI_RESET + this.getName());
                    this.attackPower += this.level;
                    this.setMutationIDs(3);
                    break;
                case 4:
                    this.setMutations("Arachna ");
                    this.setName(PURPLE_BOLD_BRIGHT + "Arachna " + ANSI_RESET + this.getName());
                    this.setMutationIDs(4);
                    break;
                case 5:
                    this.setMutations("Slimy ");
                    this.setName(GREEN_BOLD_BRIGHT + "Slimy " + ANSI_RESET + this.getName());
                    this.setMutationIDs(5);
                    break;
                case 6:
                    this.setMutations("Quick ");
                    this.setName(CYAN_BOLD_BRIGHT + "Quick " + ANSI_RESET + this.getName());
                    this.agility += this.getLevel();
                    this.setMutationIDs(6);
                    break;

            }
        }
    }

    @Override
    public String toString() {
        return name;
    }

    public List<Integer> getMutationIDs() {
        return mutationIDs;
    }

    public void setMutationIDs(int mutationID) {
        this.mutationIDs.add(mutationID);
    }

    @Override
    public int getHp() {
        return hp;
    }

    public void setHp() {
        this.hp = this.stamina;
    }

    public void updateHp(int hp){
        this.hp += hp;
    }
}
