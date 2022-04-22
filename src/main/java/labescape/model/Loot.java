package labescape.model;

public class Loot {
    public boolean isUsable;
    private String name;
    private String description;
    private int damageValue;
    private final Long id = 1L;
    private double weight;
    private String effect;
    private String type;
    public String vendSlot;

    public Loot() {

    }

    public String getVendSlot() {
        return vendSlot;
    }

    public void setVendSlot(String vendSlot) {
        this.vendSlot = vendSlot;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getDamageValue() {
        return damageValue;
    }

    public void setDamageValue(int damageValue) {
        this.damageValue = damageValue;
    }

    public double getWeight() {
        return weight;
    }

    public void setWeight(double weight) {
        this.weight = weight;
    }

    public String getEffect() {
        return effect;
    }

    public void setEffect(String effect) {
        this.effect = effect;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public boolean isUsable() {
        return isUsable;
    }

    public void setUsable() {
        if(this.getType().equals("Consumable")){
            this.isUsable = true;
        }
    }

    @Override
    public String toString() {
        String list = name + '\'' +
                ", weight = " + weight + "";
                if(getEffect() != null) {
                    list += ", effect : " + effect;
                }

        return list;
    }

    @Override
    public boolean equals(Object obj) {

        if (obj == null) {
            return false;
        }
        if (obj.getClass() != this.getClass()) {
            return false;
        }

        return ((Loot) obj).getName().equals(this.getName());
    }
}
