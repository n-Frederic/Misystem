public class TestVehicle {
    public static void main(String[] args) {

        // Create a vehicle that can handle 10,000 kilograms weight
        System.out.println("Creating a vehicle with a 10,000kg maximum load.");
        Vehicle vehicle = new Vehicle(10000.0);

        // Add a few boxes
        System.out.println("Add box #1 (500kg) : " + vehicle.addBox(500.0));
        System.out.println("Add box #2 (250kg) : " + vehicle.addBox(250.0));
        System.out.println("Add box #3 (5000kg) : " + vehicle.addBox(5000.0));
        System.out.println("Add box #4 (4000kg) : " + vehicle.addBox(4000.0));
        System.out.println("Add box #5 (300kg) : " + vehicle.addBox(300.0));

        // Print out the final vehicle load
        System.out.println("Vehicle load is " + vehicle.getLoad() + " kg");
    }
}

class Vehicle {
    private double load; //Kg
    private double maxLoad; //Kg

    public Vehicle(double maxLoad) {
        this.load = 0.0; //设置load初始值
        this.maxLoad = maxLoad; // 设置maxLoad值
    }

    public double getLoad() { //访问load属性的方法
        load=kiloToNewts(load); //此处的单位转换似乎是不必要的，但为了在方法中数据都以牛顿为单位，还是转了
        return newtsToKilo(load); //返回kg
    }

    public double getMaxLoad() {//访问maxLoad属性的方法
        maxLoad=kiloToNewts(maxLoad);
        return newtsToKilo(maxLoad); //返回kg
    }

    public boolean addBox(double weight) { //检验是否超重
        if (kiloToNewts(load) + kiloToNewts(weight) <= kiloToNewts(maxLoad)) { //如果没有超重，返回真
            load += weight;
            return true;
        } else {  //否则返回假
            return false;
        }
    }

    private double kiloToNewts(double weight) { //单位转换，将kg转换为N
        return weight * 9.8;
    }

    private double newtsToKilo(double weight) { //单位转换，将N转换为kg
        return weight / 9.8;
    }
}

