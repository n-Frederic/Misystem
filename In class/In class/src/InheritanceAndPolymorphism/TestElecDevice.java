package InheritanceAndPolymorphism;

import java.util.Scanner;

public class TestElecDevice {
    public static void main(String[] args) {
        ElectricalDevice ed;
        AudioDevice ad;
        //测试冰箱
        ed = new Refrigerator();
        ed.turnOn();
        ed.turnOff();

        //根据键盘输入创建TV或Radio实例
        Scanner myscanner = new Scanner(System.in);
        System.out.println("Enter the device type (TV or Radio):");
        String deviceType = myscanner.nextLine();
        if ("TV".equals(deviceType)) {
            ad = new TV();
        } else if ("Radio".equals(deviceType)) {
            ad = new Radio();
        } else {
            System.out.println("Wrong");
            return;
        }

        //测试AudioDevice的方法
        ad.increaseVol();
        ad.decreaseVol();

        //调用TV或Radio特有的方法
        if (ad instanceof TV) {
            TV tv = (TV) ad;
            tv.changeChannel();
            tv.adjustColor();
        } else if (ad instanceof Radio) {
            Radio radio = (Radio) ad;
            radio.adjustWavelength();
        }

        myscanner.close();
    }
}