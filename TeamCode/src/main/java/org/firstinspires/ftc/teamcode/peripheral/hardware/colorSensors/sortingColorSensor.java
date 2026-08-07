package org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors;

import static java.lang.Double.max;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;

public class sortingColorSensor extends fixColorSensors {

    private AdafruitI2cColorSensor colorSorting;

    public sortingColorSensor(AdafruitI2cColorSensor colorSensor) {

        this.colorSorting = colorSensor;

        this.colorSorting.initialize();
        this.colorSorting.setGain(40);

    }

    public Color getColor() {

        int colorRed = colorSorting.red();
        int colorGreen = colorSorting.green();
        int colorBlue = colorSorting.blue();

        if(colorRed - max(colorBlue, colorGreen) > 1000) {

            return Color.RED;

        }
        else if (colorBlue - max(colorRed, colorGreen) > 700) {

            return Color.BLUE;

        }
        else {

            return Color.NONE;

        }

    }

}
