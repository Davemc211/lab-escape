package labescape.dao;

import labescape.model.Container;

public interface ContainerDao{

    Container getRandomBox();

    Container getSpecialBox();

    Container getUnlockedContainer();

    void unlockContainer(Container box);

}
