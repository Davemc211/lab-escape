package labescape.model;

import java.util.ArrayList;
import java.util.List;


public  class Container{

    private String name;
    private String type;
    private boolean locked;


    public Container() {
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }
}
