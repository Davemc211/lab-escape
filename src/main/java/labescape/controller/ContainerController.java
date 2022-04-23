package labescape.controller;


import labescape.dao.ContainerDao;
import labescape.model.Container;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/box")
public class ContainerController {

    @Autowired
    ContainerDao containerDao;

    @RequestMapping(path = "/random", method = RequestMethod.GET)
    public Container getRandomBox() {
        return containerDao.getRandomBox();

    }

    @RequestMapping(path = "/special", method = RequestMethod.GET)
    public Container getSpecialBox() {
        return containerDao.getSpecialBox();
    }


}
