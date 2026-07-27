package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.peripheral.modules.brushesModule;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fixColorSensors;
import org.firstinspires.ftc.teamcode.peripheral.modules.sortingModule;

@Autonomous
@Config
public class MainOpMode extends LinearOpMode {

    private DcMotorEx motorBrushes;
    private Servo servoL;
    private Servo servoR;
    private DcMotor motorL;
    private DcMotor motorR;
    public DcMotor motorSeparator;

    private AdafruitI2cColorSensor sortingColor;

    private sortingModule sorting;
    private brushesModule brushes;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());



        motorBrushes = hardwareMap.get(DcMotorEx.class, "motorBrushes");
        servoL = hardwareMap.get(Servo.class,"brushesServoL");
        servoR = hardwareMap.get(Servo.class,"brushesServoR");

        brushes = new brushesModule(motorBrushes,servoL,servoR);


        motorL = hardwareMap.get(DcMotor.class,"motorL");
        motorR = hardwareMap.get(DcMotor.class,"motorR");



        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");


        sortingColor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("sortingColor")
        );


        sorting = new sortingModule(motorSeparator,sortingColor);


        waitForStart();

        brushes.startBrushes();

        while(opModeIsActive()) {

            sorting.tick();

            brushes.tick();

            telemetry.update();

        }

    }

}
