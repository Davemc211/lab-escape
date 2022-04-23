package labescape.controller;


import labescape.dao.EnemyDao;
import labescape.model.Enemy;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/enemy")
public class EnemyController {

    @Autowired
    EnemyDao enemyDao;


    @RequestMapping(path = "/{num}", method = RequestMethod.GET)
    public List<Enemy> getRandomEnemies(@PathVariable int num){
        return enemyDao.getRandomEnemies(num);

    }

    @RequestMapping(path = "/super", method = RequestMethod.GET)
    public Enemy callBossEnemy() {
        return enemyDao.getBossEnemy();

    }
}
