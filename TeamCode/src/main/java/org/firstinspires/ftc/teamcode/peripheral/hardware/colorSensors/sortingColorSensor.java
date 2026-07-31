package org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors;

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

        if(     3200<colorRed && colorRed<7400 &&
                1100<colorGreen && colorGreen<2600 &&
                800<colorBlue && colorBlue<1800) {

            return Color.RED;

        }
        else if (   400<colorRed && colorRed<2300 &&
                    1000<colorGreen && colorGreen<3200 &&
                    1500<colorBlue && colorBlue<4800) {

            return Color.BLUE;

        }
        else {

            return Color.NONE;

        }

    }

}
