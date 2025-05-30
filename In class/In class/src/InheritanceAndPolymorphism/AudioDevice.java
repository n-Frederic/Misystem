package InheritanceAndPolymorphism;

public class AudioDevice implements ElectricalDevice{

    @Override
    public void turnOn() {
        System.out.println("the AudioDevice is on.");
    }

    public void turnOff(){
        System.out.println("The AudioDevice is off.");
    }
    public void increaseVol(){
        System.out.println("Increase Volume.");
    }
    public void decreaseVol(){
        System.out.println("Decrease Volume.");
    }
}
