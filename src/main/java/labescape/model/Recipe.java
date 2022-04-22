package labescape.model;

import java.util.ArrayList;
import java.util.List;


public class Recipe extends Loot{
    private String name;
    private List<Loot> ingredients = new ArrayList<>();


    public Recipe(String name) {
        this.name = name;
    }

    public Recipe() {
    }


    public void setName(String name) {
        this.name = name;
    }

    public void printRecipes(Recipe recipe) {
        System.out.println(this.name);
        this.ingredients.forEach((entry) ->{
            System.out.printf("%s%s", entry.getName(), " \n");
        });
    }

    @Override
    public String toString() {

        String recipeReturn = this.name + " : ";
        for (Loot ingedient : this.ingredients){
            recipeReturn += ingedient.getName() + ", ";

        }

        return recipeReturn.substring(0, recipeReturn.length()-2);
    }

    public List<Loot> getIngredients(){
        return this.ingredients;
    }

    public void addIngredient(Loot specificItem) {
        ingredients.add(specificItem);
    }

    public String getName() {
        return name;
    }
}
