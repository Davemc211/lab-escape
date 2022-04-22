package labescape.dao;

import labescape.model.Container;
import labescape.model.Loot;
import labescape.model.Recipe;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;

import javax.sql.DataSource;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class JdbcLootDao implements LootDao{
    private final JdbcTemplate jdbcTemplate;

    public JdbcLootDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public Loot getRandomItem() {

        Loot drop = null;
        String sql = "SELECT * FROM loot WHERE item_type NOT IN ('Craftable') ORDER BY Random() LIMIT 1";

        SqlRowSet lootInfo = jdbcTemplate.queryForRowSet(sql);

        if (lootInfo.next()){
            drop = createLootFromRow(lootInfo);
        }

        return drop;
    }

    @Override
    public List<Loot> getRandomItem(long numberOfItems) {

        List<Loot> randomLootList = new ArrayList<>();
        String sql = "SELECT * FROM loot WHERE item_type NOT IN ('Craftable') ORDER BY random() LIMIT ?";

        SqlRowSet lootInfo = jdbcTemplate.queryForRowSet(sql, numberOfItems);

        while (lootInfo.next()){

            Loot loot = createLootFromRow(lootInfo);
            randomLootList.add(loot);
        }

        return randomLootList;
    }

    @Override
    public Loot getRandomWeapon() {
        return null;
    }

    @Override
    public List<Loot> openContainer(Container box) {
        List<Loot> lootList = new ArrayList<>();
        int rando = new Random().nextInt(10);
        if (box.getType().equals("mid")){
            if(rando > 5){
                lootList.add(getRandomWeapon());
            }
            else {
                return getRandomJunkItem(3L);
            }
        }
        else if(box.getType().equals("high")){
            if(rando > 3){
                lootList.add(getRecipe());
            }
            else {
                return getRandomJunkItem(8L);
            }
        }
        else {
            return getRandomJunkItem(1L);
        }

        return lootList;
    }

    @Override
    public Loot getRandomJunkItem() {

        Loot loot = null;
        String sql = "SELECT * FROM loot WHERE item_type = 'Junk' ORDER BY Random() LIMIT 1";

        SqlRowSet lootInfo = jdbcTemplate.queryForRowSet(sql);

        if(lootInfo.next()){

            loot = createLootFromRow(lootInfo);
        }

        return loot;
    }

    @Override
    public List<Loot> getRandomJunkItem(long numberOfItems) {
        List<Loot> lootList = new ArrayList<>();
        String sql = "SELECT * FROM loot WHERE item_type = 'Junk' ORDER BY random() LIMIT ?";

        SqlRowSet lootInfo = jdbcTemplate.queryForRowSet(sql, numberOfItems);

        while (lootInfo.next()){
            Loot loot = createLootFromRow(lootInfo);
            lootList.add(loot);
        }

        return lootList;
    }

    @Override
    public Recipe createRecipeFromRow() {
        return null;
    }

    public Loot createLootFromRow(SqlRowSet lootInfo) {
        Loot loot = new Loot();
        loot.setName(lootInfo.getString("item_name"));
        loot.setDescription(lootInfo.getString("item_description"));
        loot.setDamageValue(lootInfo.getInt("damage_value"));
        loot.setWeight(lootInfo.getDouble("item_weight"));
        loot.setEffect(lootInfo.getString("item_effect"));
        loot.setType(lootInfo.getString("item_type"));
        loot.setVendSlot(lootInfo.getString("vendId"));
        loot.setUsable();

        return loot;

    }

    public Recipe createRecipeFromRow(SqlRowSet sqlRecipe){
        Recipe recipe = new Recipe();
        String recipeName = sqlRecipe.getString("recipe_name");
        String sql = "SELECT recipe_name, item_name, item_quantity FROM recipe JOIN recipe_loot AS rl ON recipe.recipe_id = rl.recipe_id JOIN loot ON rl.item_id = loot.item_id WHERE recipe_name = ?";
        SqlRowSet recipeInfo = jdbcTemplate.queryForRowSet(sql, recipeName);


        while (recipeInfo.next()){
            recipe.setName(recipeInfo.getString("recipe_name"));
            String item = recipeInfo.getString("item_name");
            Long value = recipeInfo.getLong("item_quantity");

            recipe.addIngredient(getSpecificItem(item, value));
        }

        return recipe;

    }

    @Override
    public Loot getSpecificItem(String name, Long num) {
        Loot loot = null;
        String sql = "SELECT * FROM loot, generate_series(1,?) WHERE item_name = ?;";

        SqlRowSet lootInfo = jdbcTemplate.queryForRowSet(sql, num, name);

        while(lootInfo.next()){
            loot = createLootFromRow(lootInfo);
        }

        return loot;
    }

    @Override
    public Recipe getRecipe() {
        Recipe recipe = null;
        String sql = "SELECT recipe_name FROM recipe ORDER BY random() LIMIT 1;";

        SqlRowSet recipeName = jdbcTemplate.queryForRowSet(sql);

        if(recipeName.next()){
            recipe = createRecipeFromRow(recipeName);
        }
        return recipe;
    }

    @Override
    public List<Loot> getVendingStock(long numberOfItems) {
        List<Loot> lootList = new ArrayList<>();
        String sql = "SELECT * FROM loot WHERE item_type = 'Consumable' ORDER BY random() LIMIT ?";

        SqlRowSet lootInfo = jdbcTemplate.queryForRowSet(sql, numberOfItems);

        while (lootInfo.next()){
            Loot loot = createLootFromRow(lootInfo);
            lootList.add(loot);
        }

        return lootList;
    }
}
