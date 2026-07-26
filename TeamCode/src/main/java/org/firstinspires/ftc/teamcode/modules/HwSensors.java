package org.firstinspires.ftc.teamcode.modules;

import static com.qualcomm.hardware.ams.AMSColorSensor.AMS_TCS34725_ADDRESS;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.qualcomm.hardware.ams.AMSColorSensor;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.I2cDeviceSynchDeviceWithParameters;

import java.lang.reflect.Field;



public class HwSensors
{
    public enum Color
    {
        RED,
        BLUE,
        NONE
    }

    AdafruitI2cColorSensor exampleSensor1 = fixSensor(
            (AdafruitI2cColorSensor) hardwareMap.get("MY_COLOR-DEVICE_NAME_1")
    );



    public Color UpdateBaseColors()
    { return Color.NONE; } //  Implementation goes here



    // ------------------------------------------------------------------------//
    public static AdafruitI2cColorSensor fixSensor(AdafruitI2cColorSensor sensor){
        try {
            AMSColorSensor.class.getDeclaredField("AMS_TCS34725_ID").setAccessible(true);

            AMSColorSensor.Parameters parameters = new AMSColorSensor.Parameters(AMS_TCS34725_ADDRESS, 0x4D);

            Field paramField = I2cDeviceSynchDeviceWithParameters.class.getDeclaredField("parameters");

            paramField.setAccessible(true);

            try {
                paramField.set(sensor, parameters);
            } catch (Exception e) {
                throw new RuntimeException(e);
            }

            sensor.initialize();
        } catch (NoSuchFieldException e) {
            throw new RuntimeException("color sensor hack not successful");
        }

        return sensor;
    }
    //  Driver fix snippet by @tikhonsmovzh https://github.com/tikhonsmovzh
}