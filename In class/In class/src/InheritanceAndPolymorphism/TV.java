package InheritanceAndPolymorphism;

public class TV extends AudioDevice {

    public void increaseVol() {
        super.increaseVol();
        System.out.println("Increase TV volume.");
    }

    public void decreaseVol() {
        super.decreaseVol();
        System.out.println("Increase TV volume.");
    }

    public void changeChannel() {
        System.out.println("Change channel.");
    }

    public void adjustColor() {
        System.out.println("Adjust color.");
    }
}
