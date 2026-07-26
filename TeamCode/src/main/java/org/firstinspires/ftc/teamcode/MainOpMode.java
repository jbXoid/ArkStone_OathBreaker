package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.config.Config;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.DcMotor;
import org.firstinspires.ftc.teamcode.peripheral.sortingHardware.fixColorSensors;
import org.firstinspires.ftc.teamcode.peripheral.sortingHardware.PIDcontroling;
import org.firstinspires.ftc.teamcode.peripheral.sortingModule;

@Autonomous
@Config
public class MainOpMode extends LinearOpMode {

    public static int friendlyPucks = 1;
    private DcMotor motorL;
    public DcMotor motorSeparator;
    private AdafruitI2cColorSensor colorIntake;
    private sortingModule sorting;

    @Override
    public void runOpMode() {

        colorIntake = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("colorIntake")
        );

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motorL = hardwareMap.get(DcMotor.class,"motorL");
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);



        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");
        motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        sorting = new sortingModule(motorSeparator,colorIntake);


        PIDcontroling motorSeparatorControl = new PIDcontroling(motorSeparator);
        motorSeparatorControl.setPointDegrees = motorSeparator.getCurrentPosition();

        waitForStart();


        while(opModeIsActive()) {

            sorting.tick();

            telemetry.update();

        }

    }

}
