package seedu.navi.canteen.canteenfinder.landmark.canteen.stall;

public class Stall {
    private String name;
    private StallCharacteristic stallCharacteristic;

    public Stall(String name) {
        this.name = name;
        stallCharacteristic = new StallCharacteristic();
    }

    public Stall(String name, boolean halalCertified, boolean muslimOwned,
                 boolean vegetarian, boolean inAirconArea) {
        this.name = name;
        stallCharacteristic = new StallCharacteristic(halalCertified, muslimOwned,
                vegetarian, inAirconArea);
    }

    public String toString() {
        return name + "\n" +
                "  " + stallCharacteristic.getHalalCertifiedIcon() + "\n" +
                "  " + stallCharacteristic.getMuslimOwnedIcon() + "\n" +
                "  " + stallCharacteristic.getVegetarianIcon() + "\n" +
                "  " + stallCharacteristic.getInAirconAreaIcon() + "\n";
    }

    public String getName() {
        return name;
    }

    public StallCharacteristic getStallCharacteristic() {
        return stallCharacteristic;
    }

    public boolean isValidStall(String[] canteenCriteria) {
        return stallCharacteristic.containsFields(canteenCriteria);
    }
}
