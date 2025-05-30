package InheritanceAndPolymorphism;

public class Radio extends AudioDevice{

    public void increaseVol() {
        super.increaseVol();
        System.out.println("Increase radio volume.");
    }

    public void decreaseVol() {
        super.decreaseVol();
        System.out.println("Increase radio volume.");
    }

    public void adjustWavelength(){
        System.out.println("Adjust wavelength");
    }
}
