package labescape.dao;

import labescape.model.Container;
import labescape.model.Loot;
import labescape.model.Recipe;

import java.util.List;

public interface LootDao {

    Loot getRandomItem();

    List<Loot> getRandomItem(long numberOfItems);

    Loot getRandomWeapon();

    List<Loot> openContainer(Container box);

    Loot getRandomJunkItem();

    List<Loot> getRandomJunkItem(long numberOfItems);

    Recipe createRecipeFromRow();

    Loot getSpecificItem(String name, Long number);

    Recipe getRecipe();

    List<Loot> getVendingStock(long numberOfItems);
}
