package labescape.dao;

import labescape.model.Enemy;

import java.util.List;

public interface EnemyDao {

    List<Enemy> getRandomEnemies(int playerLevel);

    Enemy getBossEnemy();

}
