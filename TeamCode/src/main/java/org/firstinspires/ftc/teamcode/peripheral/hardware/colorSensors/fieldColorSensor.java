package org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors;

import static java.lang.Double.max;

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

            if(colorRed - max(colorBlue, colorGreen) > 700) {

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
