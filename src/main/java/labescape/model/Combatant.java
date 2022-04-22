package labescape.model;

public interface Combatant {

    int getStrength();

    int getAgility();

    int getStamina();

    int rollAgility();

    int rollStrength();

    int rollStamina();

    String getDescription();

    String getName();

    int getHp();

    static int compareAgility(Combatant combatant, Combatant combatant1) {
        return combatant.rollAgility() - combatant1.rollAgility();
    }
}
