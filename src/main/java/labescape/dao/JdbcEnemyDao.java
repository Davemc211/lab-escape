package labescape.dao;


import labescape.model.Enemy;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

@Component
public class JdbcEnemyDao implements EnemyDao {

    private final JdbcTemplate jdbcTemplate;

    public JdbcEnemyDao(DataSource dataSource) {
        this.jdbcTemplate = new JdbcTemplate(dataSource);
    }

    @Override
    public List<Enemy> getRandomEnemies(int playerLevel) {
        List<Enemy> combatants = new ArrayList<>();

        String sql = "SELECT * FROM enemy ORDER BY random() LIMIT ?";
        SqlRowSet enemyInfo = jdbcTemplate.queryForRowSet(sql, playerLevel);

        while(enemyInfo.next()){

            int levelSet = new Random().nextInt(playerLevel) + 2; //--should-- generate enemies within 2 of the player level
            combatants.add(createEnemy(enemyInfo, levelSet));
        }

        return combatants;
    }

    private Enemy createEnemy(SqlRowSet enemyInfo, int level) {
        Enemy newEnemy = null;
        try { //using reflection to randomly generate enemies

            String className = "labescape.model." + enemyInfo.getString("mob_class"); //must use fully-qualified path
            Class myClass = Class.forName(className);
            Constructor<Enemy> constructor = myClass.getConstructor(); // specify constructor<T>
            newEnemy = constructor.newInstance(); //Object to catch constructor
            newEnemy.setName(enemyInfo.getString("base_name"));
            newEnemy.setDescription(enemyInfo.getString("base_description"));
            newEnemy.setStamina(enemyInfo.getInt("mob_stamina"));
            newEnemy.setStrength(enemyInfo.getInt("mob_strength"));
            newEnemy.setAgility(enemyInfo.getInt("mob_agility"));
            newEnemy.setLevel(level);
            newEnemy.setAttackPower();
            newEnemy.setMutated();
            newEnemy.setHp();

        } catch (ClassNotFoundException | NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (InstantiationException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }

        return newEnemy;
    }

    @Override
    public Enemy getBossEnemy() {
        return null;
    }
}
