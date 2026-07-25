package org.firstinspires.ftc.teamcode.peripheral;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.peripheral.sortingHardware.PIDcontroling;
import org.firstinspires.ftc.teamcode.peripheral.sortingHardware.fixColorSensors;

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
    private AdafruitI2cColorSensor colorIntake;
    private PIDcontroling motorSeparatorControl;

    public sortingModule (DcMotor motor, AdafruitI2cColorSensor colorSensor) {

        this.motorSeparator = motor;
        this.motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        PIDcontroling motorSeparatorControl = new PIDcontroling(motorSeparator);
        motorSeparatorControl.setPointDegrees = motorSeparator.getCurrentPosition();

        this.colorIntake = colorSensor;
        this.colorIntake.initialize();
        this.colorIntake.setGain(60);

    }

    public void tick() {

        boolean PIDstate = motorSeparatorControl.tick();

        int colorRed = colorIntake.red();
        int colorGreen = colorIntake.green();
        int colorBlue = colorIntake.blue();

        if( PIDstate ) {
            if(     3200 <colorRed && colorRed<4600 &&
                    1100<colorGreen && colorGreen<1900 &&
                    800<colorBlue && colorBlue<1200) {

                motorSeparatorControl.setPointDegrees += 120;

            }
        }

    }

}
