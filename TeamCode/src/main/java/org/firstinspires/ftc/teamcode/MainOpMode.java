package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fixColorSensors;

import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.analogTouch;

import org.firstinspires.ftc.teamcode.peripheral.modules.movingModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.sortingModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.brushesModule;

import org.firstinspires.ftc.teamcode.peripheral.hardware.moving.PIDwheelbase;

@Autonomous
@Config
public class MainOpMode extends LinearOpMode {

    public static Color TeamColor = Color.NONE;

    public static int AddAngle = 120;

    private DcMotorEx motorBrushes;
    private Servo servoL;
    private Servo servoR;

    private DcMotorEx motorL;
    private DcMotorEx motorR;

    private AnalogInput analogTouch1;
    private AnalogInput analogTouch2;
    private analogTouch touch1;
    private analogTouch touch2;

    private DcMotor motorSeparator;
    private AdafruitI2cColorSensor sortingColorSensor;
    private AdafruitI2cColorSensor fieldColorSensor;
    private Servo gateServo;

    private sortingModule sorting;
    private brushesModule brushes;
    private movingModule moving;
    private IMU imu;
    private PIDwheelbase wheelbaseAngle;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());


        motorBrushes = hardwareMap.get(DcMotorEx.class, "motorBrushes");
        servoL = hardwareMap.get(Servo.class,"brushesServoL");
        servoR = hardwareMap.get(Servo.class,"brushesServoR");

        brushes = new brushesModule(motorBrushes,servoL,servoR);


        motorL = hardwareMap.get(DcMotorEx.class,"motorL");
        motorR = hardwareMap.get(DcMotorEx.class,"motorR");

        imu = hardwareMap.get(IMU.class, "imu");

        moving = new movingModule(motorL,motorR,imu);

        wheelbaseAngle = new PIDwheelbase(motorL,motorR,imu);

        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");

        sortingColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("sortingColor")
        );

        fieldColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("fieldColor")
        );

        gateServo = hardwareMap.get(Servo.class,"gateServo");

        sorting = new sortingModule(motorSeparator,sortingColorSensor,fieldColorSensor,gateServo);


        analogTouch1 = hardwareMap.get(AnalogInput.class,"touch1");
        analogTouch2 = hardwareMap.get(AnalogInput.class, "touch2");

        touch1 = new analogTouch(analogTouch1);
        touch2 = new analogTouch(analogTouch2);


        moving.setSpeed(0.5);

        /*
        while(true) {

            boolean gotColor = sorting.getTeamColor();
            if( gotColor ) break;
            else if( TeamColor != Color.NONE ) {

                sorting.teamColor = TeamColor;
                break;

            }

        }
        */

        waitForStart();

        // Start by bumper
        /*
        while (true) {

            if(touch1.getState() == AnalogTouchState.PRESSED) {

                while(touch1.getState() == AnalogTouchState.PRESSED);
                break;

            }
            else if(touch2.getState() == AnalogTouchState.PRESSED) {

                while(touch2.getState() == AnalogTouchState.PRESSED);
                break;

            }

        }
        */


        //brushes.startBrushes();
        //moving.startMoving();

        moving.addYawAngle(AddAngle);

        while(opModeIsActive()) {

            /*
            moving.tick();
            sorting.tick();
            brushes.tick();
            telemetry.update();
             */

            boolean inPos = moving.tick();

            if(inPos){
                sleep(250);
                moving.addYawAngle(AddAngle);
            }

            telemetry.addLine("Action: " + String.valueOf(moving.realTimeAction));
            telemetry.addLine("In position: " + String.valueOf(inPos) );
            telemetry.addLine("Robot yaw: " + String.valueOf(moving.getYaw()));
            telemetry.addLine("Left motor speed: " + String.valueOf(motorL.getPower()));
            telemetry.addLine("Right motor speed: " + String.valueOf(motorR.getPower()));




            telemetry.update();

        }

    }

}
