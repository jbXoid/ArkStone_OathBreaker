package org.firstinspires.ftc.teamcode.peripheral.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;

import org.firstinspires.ftc.teamcode.peripheral.hardware.motors.PIDangleControling;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.sortingColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fieldColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;

@Config
public class sortingModule {

    private DcMotor motorSeparator;
    private sortingColorSensor sortingColor;
    private fieldColorSensor fieldColor;
    private PIDangleControling motorSeparatorControl;

    public sortingModule (DcMotor motor, AdafruitI2cColorSensor sortingColorSensor, AdafruitI2cColorSensor fieldColorSensor) {

        this.motorSeparator = motor;
        this.motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorSeparatorControl = new PIDangleControling(motorSeparator);
        motorSeparatorControl.setPointDegrees = motorSeparator.getCurrentPosition()/2;

        sortingColor = new sortingColorSensor(sortingColorSensor);
        fieldColor = new fieldColorSensor(fieldColorSensor);

    }

    private Color teamColor;

    public boolean getTeamColor() {

        Color gotColor = fieldColor.getColor();

        if( gotColor != Color.NONE ) {

            teamColor = gotColor;
            return true;

        }

        else {

            return false;

        }

    }

    public void tick() {

        boolean PIDstate = motorSeparatorControl.tick();

        if( PIDstate ) {
            if(sortingColor.getColor() == this.teamColor) {

                motorSeparatorControl.setPointDegrees -= 120;

            }

            else if(sortingColor.getColor() == this.teamColor) {

                motorSeparatorControl.setPointDegrees += 120;

            }
        }

    }

}
