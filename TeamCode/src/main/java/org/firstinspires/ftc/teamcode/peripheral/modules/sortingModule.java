package org.firstinspires.ftc.teamcode.peripheral.modules;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.peripheral.hardware.motors.PIDangleControling;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.sortingColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fieldColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;

@Config
public class sortingModule {

    private DcMotor motorSeparator;
    private Servo gateServo;
    public sortingColorSensor sortingColor;
    public fieldColorSensor fieldColor;
    public PIDangleControling motorSeparatorControl;

    public sortingModule (DcMotor motor, AdafruitI2cColorSensor sortingColorSensor, AdafruitI2cColorSensor fieldColorSensor, Servo gateServo) {

        this.motorSeparator = motor;
        this.motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorSeparatorControl = new PIDangleControling(motorSeparator);
        motorSeparatorControl.setPointDegrees = motorSeparator.getCurrentPosition()/2;

        sortingColor = new sortingColorSensor(sortingColorSensor);
        fieldColor = new fieldColorSensor(fieldColorSensor);

        this.gateServo = gateServo;
        this.gateServo.setPosition(0.55);

    }

    public Color teamColor;

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
            Color realtimeColor = sortingColor.getColor();

            if(realtimeColor == this.teamColor) {

                motorSeparatorControl.setPointDegrees -= 120;

            }

            else if(realtimeColor != Color.NONE) {

                motorSeparatorControl.setPointDegrees += 120;

            }
        }

        if(fieldColor.getColor() == this.teamColor) {
            gateServo.setPosition(0.58);
        }
        else {
            gateServo.setPosition(0.567);
        }

    }

}
