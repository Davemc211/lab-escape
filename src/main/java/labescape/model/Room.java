package labescape.model;


import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class Room {

    Random random = new Random();

    private String description = "";

    private List<Loot> floorItems = new ArrayList<>();
    private List<Container> lockedContainer = new ArrayList<>();
    private List<Enemy> roomEnemies = new ArrayList<>();

    private boolean enemies, obstacle, hasContainer, current, explored, searched, hasVendingMachine, elevator;


    public Room() {
        roomGenerate();
    }

    public Room(boolean obstacle){
        this.obstacle = obstacle;
    }

    public void roomGenerate() {
        this.enemies = random.nextBoolean();
        if(random.nextInt(10) > 3){
            this.hasContainer = true;
        }
        if(random.nextInt(100) > 95){
            this.hasVendingMachine = true;
            this.setDescription(this.description + " \n\nThere is a ValueRep brand vending machine here!");
        }
        this.searched = false;

    }

    public Room getStartingRoom(){
        Room startingRoom = new Room();
        this.hasContainer = true;
        this.hasVendingMachine = true;
        this.setDescription(this.description += "\n\nThere is a ValueRep brand vending machine here!");
        return startingRoom;
    }


    public String getDescription() {
        return description;
    }


    public boolean hasEnemies() {
        return enemies;
    }

    public boolean isObstacle() {
        return obstacle;
    }

    public boolean hasContainer() {
        return hasContainer;
    }

    public boolean isCurrent() {
        return current;
    }

    public void setCurrent(boolean current) {
        this.current = current;
    }

    public boolean isExplored() {
        return explored;
    }

    public void setExplored(boolean explored) {
        this.explored = explored;
    }

    public List<Loot> getFloorItems() {
        return floorItems;
    }

    public void addFloorItems(Loot drop) {
        floorItems.add(drop);
    }

    public boolean isSearched() {
        return searched;
    }

    public void setSearched(boolean searched) {
        this.searched = searched;
    }

    public List<Container> getLockedContainer() {
        return lockedContainer;
    }

    public void setLockedContainer(Container lockedContainer) {
        this.lockedContainer.add(lockedContainer);
    }

    public List<Enemy> getRoomEnemies() {
        return roomEnemies;
    }

    public void setRoomEnemies(List<Enemy> roomEnemies) {
        this.roomEnemies = roomEnemies;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public void setFloorItems(List<Loot> floorItems) {
        this.floorItems = floorItems;
    }

    public void setLockedContainer(List<Container> lockedContainer) {
        this.lockedContainer = lockedContainer;
    }

    public boolean isEnemies() {
        return enemies;
    }

    public void setEnemies(boolean enemies) {
        this.enemies = enemies;
    }

    public boolean isHasContainer() {
        return hasContainer;
    }

    public void setHasContainer(boolean hasContainer) {
        this.hasContainer = hasContainer;
    }

    public void removeFloorItem(int index){
        floorItems.remove(index);

    }

    public boolean hasVendingMachine() {
        return hasVendingMachine;
    }

    public boolean isElevator() {
        return elevator;
    }

    public void setElevator(boolean elevator) {
        this.elevator = elevator;
    }
}
