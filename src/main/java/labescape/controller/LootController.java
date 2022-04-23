package labescape.controller;


import labescape.dao.LootDao;
import labescape.model.Loot;
import labescape.model.Recipe;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping(path = "/loot")
public class LootController {

    @Autowired
    LootDao lootDao;

    @RequestMapping(path = "/random/{num}", method = RequestMethod.GET)
    public List<Loot> getRandomItem(@PathVariable(required = false) int num){
        return lootDao.getRandomItem(num);

    }

    @RequestMapping(path = "/random/junk/{num}", method = RequestMethod.GET)
    public List<Loot> getRandomJunkItem(@PathVariable int num){
        return lootDao.getRandomJunkItem(num);

    }

    @RequestMapping(path = "/random/recipe", method = RequestMethod.GET)
    public Recipe getRecipe(){
        return lootDao.getRecipe();
    }

    @RequestMapping(path = "/vend", method = RequestMethod.GET)
    public List<Loot> fillVendingMachine(){
        return lootDao.getVendingStock(20);
    }

}
