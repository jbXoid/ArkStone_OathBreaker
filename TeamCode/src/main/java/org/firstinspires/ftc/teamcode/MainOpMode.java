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

import org.firstinspires.ftc.teamcode.peripheral.brushes.brushesModule;
import org.firstinspires.ftc.teamcode.peripheral.sorting.sortingHardware.colorSensors.fixColorSensors;
import org.firstinspires.ftc.teamcode.peripheral.sorting.sortingHardware.PIDcontroling;
import org.firstinspires.ftc.teamcode.peripheral.sorting.sortingModule;

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

        motorL = hardwareMap.get(DcMotor.class,"motorL");
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorR = hardwareMap.get(DcMotor.class,"motorR");
        motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");
        motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);


        sortingColor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("sortingColor")
        );


        brushes = new brushesModule(motorBrushes,servoL,servoR);
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
