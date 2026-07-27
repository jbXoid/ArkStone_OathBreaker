package org.firstinspires.ftc.teamcode.peripheral.sorting.sortingHardware.colorSensors;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;

public class sortingColorSensor extends fixColorSensors {

    public enum Color
    {
        RED,
        BLUE,
        NONE
    }

    private AdafruitI2cColorSensor colorSorting;

    public sortingColorSensor(AdafruitI2cColorSensor colorSensor) {

        this.colorSorting = colorSensor;

        colorSorting.initialize();
        colorSorting.setGain(40);

    }

    public Color getColor() {

        int colorRed = colorSorting.red();
        int colorGreen = colorSorting.green();
        int colorBlue = colorSorting.blue();

        if(     3200<colorRed && colorRed<4600 &&
                1100<colorGreen && colorGreen<1900 &&
                800<colorBlue && colorBlue<1200) {

            return Color.RED;

        }
        else if (   400<colorRed && colorRed<1000 &&
                    1000<colorGreen && colorGreen<1400 &&
                    1500<colorBlue && colorBlue<2200) {

            return Color.BLUE;

        }
        else {

            return Color.NONE;

        }

    }

}
