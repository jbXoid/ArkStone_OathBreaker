package org.firstinspires.ftc.teamcode.peripheral.sorting;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.peripheral.sorting.sortingHardware.PIDcontroling;
import org.firstinspires.ftc.teamcode.peripheral.sorting.sortingHardware.colorSensors.sortingColorSensor;

@Config
public class sortingModule {

    private DcMotor motorSeparator;
    private sortingColorSensor sortingColor;
    private PIDcontroling motorSeparatorControl;

    public sortingModule (DcMotor motor, AdafruitI2cColorSensor colorSensor) {

        this.motorSeparator = motor;
        this.motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorSeparatorControl = new PIDcontroling(motorSeparator);
        motorSeparatorControl.setPointDegrees = motorSeparator.getCurrentPosition();

        sortingColor = new sortingColorSensor(colorSensor);

    }

    public void tick() {

        boolean PIDstate = motorSeparatorControl.tick();

        if( PIDstate ) {
            if(sortingColor.getColor() == sortingColorSensor.Color.RED) {

                motorSeparatorControl.setPointDegrees += 120;

            }

            else if(sortingColor.getColor() == sortingColorSensor.Color.BLUE) {

                motorSeparatorControl.setPointDegrees -= 120;

            }
        }

    }

}
