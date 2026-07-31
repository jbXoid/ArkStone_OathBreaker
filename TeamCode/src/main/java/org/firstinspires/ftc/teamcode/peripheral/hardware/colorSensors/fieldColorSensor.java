package org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors;

import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;

public class fieldColorSensor {

    private AdafruitI2cColorSensor colorField;

    public fieldColorSensor(AdafruitI2cColorSensor colorSensor) {

        this.colorField = colorSensor;

        this.colorField.initialize();
        this.colorField.setGain(40);

    }

    public Color getColor() {

        int colorRed = colorField.red();
        int colorGreen = colorField.green();
        int colorBlue = colorField.blue();

        if(     5000<colorRed && colorRed<8300 &&
                1880<colorGreen && colorGreen<2700 &&
                1600<colorBlue && colorBlue<2500) {

            return Color.RED;

        }
        else if (   1700<colorRed && colorRed<2500 &&
                1800<colorGreen && colorGreen<3100 &&
                2800<colorBlue && colorBlue<5300) {

            return Color.BLUE;

        }
        else {

            return Color.NONE;

        }

    }



}
