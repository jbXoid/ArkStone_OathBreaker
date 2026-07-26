package org.firstinspires.ftc.teamcode.peripheral;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;

import org.firstinspires.ftc.teamcode.peripheral.sortingHardware.fixColorSensors;

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
        else {

            return Color.NONE;

        }

    }

}
