package labescape.combat;

import labescape.model.Combatant;
import labescape.model.Enemy;
import labescape.model.Player;

import java.util.*;

public class Combat {

    Random random = new Random();

    public List<Combatant> turnOrder(Player player, List<Enemy> enemies) {

        List<Combatant> turnOrder = new ArrayList<>();
        turnOrder.add(player);
        turnOrder.addAll(enemies);
        turnOrder.sort(Combatant::compareAgility);

        return turnOrder;
    }

    public boolean enemyAttack(Enemy enemy, Player player) { // attack calls FROM the enemy
        int damageDone = 0;
        getAttackMessage(enemy, player);
        if (enemy.rollAgility() > player.rollAgility()) {
            System.out.println("You've been hit!");
            applyMutationEffect(enemy, player);
            damageDone += enemy.getAttackPower() - player.rollStrength();
            if (damageDone > 0) {
                player.updateHp(-damageDone);
                return true;
            } else {
                System.out.println("But you take no damage.");
            }
        } else {
            System.out.println("They missed!");
        }
        return false;
    }

    private void applyMutationEffect(Enemy enemy, Player player) {
        if(enemy.isMutated() && enemy.getMutationIDs().size() > 0){
            int mutation = enemy.getMutationIDs().get(0);
            switch (mutation){
                case 2:
                    if(player.getStamina() > 2 && !player.isStamAffected()) {
                        System.out.println("The powerful blow weakens you!");
                        player.updateStamina(-2);
                        player.stamTimer();
                    }
                    break;
                case 3:

                    break;
                case 4:
                    if(player.getAgility() > 3 && !player.isAgilityAffected()) {
                        System.out.println("The webs slow you to a crawl!");
                        player.updateAgility(-2);
                        player.setAgilityTask();
                    }
                    break;
                case 5:
                    if(player.getCurrentItem().length() > 0) {
                        System.out.println("The acid eats your weapon away!");
                        player.setEquippedItem(null);
                    }
                    break;
            }
        }
    }

    private void getAttackMessage(Enemy enemy, Player player) {

        if(enemy.isMutated() && enemy.getMutationIDs().size() > 0){

            int mutation = enemy.getMutationIDs().get(0);
            String enemyName = enemy.getName();
            String playerName = player.getName();
            switch (mutation){
                case 1:
                    System.out.println(enemyName + " swoops at " + playerName +"!");
                    break;
                case 2:
                    System.out.println(enemyName + " charges angrily at " + playerName + "!");
                    break;
                case 3:
                    System.out.println(enemyName + " throws lightning at " + playerName + "!");
                    break;
                case 4:
                    System.out.println(enemyName + " attempts to web " + playerName + ".");
                    break;
                case 5:
                    System.out.println(enemyName + " spits acid at " + playerName + "!");
                    break;
                case 6:
                    System.out.println(enemyName + " attacks almost too fast to see!");
                    break;
            }
        }
        else {
            System.out.println(enemy.getName() + " attacks!");
        }
    }


    public boolean stealthCheck(Player player, List<Enemy> enemies) {

        for(Enemy enemy: enemies){

            if(player.rollAgility() <= enemy.rollAgility()){
                return false;
            }
        }
        return true;
    }

    public boolean playerAttack(Enemy target, Player player) { // attack calls FROM the player
        int damageDone = 0;
        if(player.rollAgility() > target.rollAgility()){
            System.out.println("Your blow lands!");
            damageDone += player.rollAttackPower() - target.rollStrength();
            if(damageDone > 0) {
                target.updateHp(-damageDone);
                return true;
            } else {
                System.out.println("But you did no damage.");
            }
        }
        else {
            System.out.println("You missed!");
        }

        return false;
    }
}
