package org.firstinspires.ftc.teamcode.peripheral;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.peripheral.sortingHardware.PIDcontroling;
import org.firstinspires.ftc.teamcode.peripheral.sortingColorSensor;

@Config
public class sortingModule {

    public class puckColors {
        public class redPuck {

            public int redLowerThreshold = 3200;
            public int redUpperThreshold = 4600;
            public int greenLowerThreshold = 1100;
            public int greenUpperThreshold = 1900;
            public int blueLowerThreshold = 800;
            public int blueUpperThreshold = 1200;

        }
        class bluePuck {

        }
    }

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
        }

    }

}
