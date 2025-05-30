package InheritanceAndPolymorphism;

public class Refrigerator implements ElectricalDevice{

    public void turnOn() {
        System.out.println("the Refrigerator is on.");
    }

    public void turnOff(){
        System.out.println("The Refrigerator is off.");
    }
    public void setFreezingLevel(){
        System.out.println("Set freezing level.");
    }
}
