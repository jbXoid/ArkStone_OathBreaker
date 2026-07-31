package org.firstinspires.ftc.teamcode;

import android.text.method.Touch;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fieldColorSensor;
import org.firstinspires.ftc.teamcode.peripheral.modules.brushesModule;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fixColorSensors;
import org.firstinspires.ftc.teamcode.peripheral.modules.movingModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.sortingModule;

@Autonomous
@Config
public class MainOpMode extends LinearOpMode {

    private DcMotorEx motorBrushes;
    private Servo servoL;
    private Servo servoR;

    private DcMotorEx motorL;
    private DcMotorEx motorR;
    private AnalogInput touch1;
    private AnalogInput touch2;

    private DcMotor motorSeparator;
    private AdafruitI2cColorSensor sortingColorSensor;
    private AdafruitI2cColorSensor fieldColorSensor;

    private sortingModule sorting;
    private brushesModule brushes;
    private movingModule moving;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        motorBrushes = hardwareMap.get(DcMotorEx.class, "motorBrushes");
        servoL = hardwareMap.get(Servo.class,"brushesServoL");
        servoR = hardwareMap.get(Servo.class,"brushesServoR");

        brushes = new brushesModule(motorBrushes,servoL,servoR);


        motorL = hardwareMap.get(DcMotorEx.class,"motorL");
        motorR = hardwareMap.get(DcMotorEx.class,"motorR");

        touch1 = hardwareMap.get(AnalogInput.class,"touch1");
        touch2 = hardwareMap.get(AnalogInput.class, "touch2");

        moving = new movingModule(motorL,motorR,touch1,touch2);


        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");

        sortingColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("sortingColor")
        );

        fieldColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("fieldColor")
        );

        sorting = new sortingModule(motorSeparator,sortingColorSensor,fieldColorSensor);


        waitForStart();

        while( !sorting.getTeamColor() );

        brushes.startBrushes();
        moving.startMoving();

        while(opModeIsActive()) {

            moving.tick();

            sorting.tick();

            brushes.tick();

            telemetry.update();

        }

    }

}
