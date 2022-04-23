package labescape.controller;

import labescape.dao.RoomDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(path = "/room")
public class RoomController {

    @Autowired
    RoomDao roomDao;

    @RequestMapping(method = RequestMethod.GET)
    public String getRandomDescription() {
        return roomDao.getRandomDescription();
    }

    @RequestMapping(path = "/{id}", method = RequestMethod.GET)
    public String getSpecificDescription(@PathVariable int id) {
        return getSpecificDescription(id);

    }


}
