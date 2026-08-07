package org.firstinspires.ftc.teamcode.OpModes.STRATEGIES;

import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.hardwareMap;
import static org.firstinspires.ftc.robotcore.external.BlocksOpModeCompanion.telemetry;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.Bumper;
import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.TouchState;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fixColorSensors;
import org.firstinspires.ftc.teamcode.peripheral.modules.brushesModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.movingModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.sortingModule;

public class Init extends LinearOpMode {
    public static Color TeamColor = Color.NONE;

    private DcMotorEx motorBrushes;
    private Servo servoL;
    private Servo servoR;

    private DcMotorEx motorL;
    private DcMotorEx motorR;

    private AnalogInput analogTouch1;
    private AnalogInput analogTouch2;
    private Bumper bumper;

    private DcMotor motorSeparator;
    private AdafruitI2cColorSensor sortingColorSensor;
    private AdafruitI2cColorSensor fieldColorSensor;
    private Servo gateServo;

    public sortingModule sorting;
    public brushesModule brushes;
    public movingModule moving;
    private IMU imu;
    private ElapsedTime elapsedTime;


    public void initRobot() {


        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        elapsedTime = new ElapsedTime();
        elapsedTime.reset();

        analogTouch1 = hardwareMap.get(AnalogInput.class,"touch1");
        analogTouch2 = hardwareMap.get(AnalogInput.class, "touch2");
        bumper = new Bumper(analogTouch1,analogTouch2);

        motorBrushes = hardwareMap.get(DcMotorEx.class, "motorBrushes");
        servoL = hardwareMap.get(Servo.class,"brushesServoL");
        servoR = hardwareMap.get(Servo.class,"brushesServoR");

        brushes = new brushesModule(motorBrushes,servoL,servoR);


        motorL = hardwareMap.get(DcMotorEx.class,"motorL");
        motorR = hardwareMap.get(DcMotorEx.class,"motorR");

        imu = hardwareMap.get(IMU.class, "imu");

        moving = new movingModule(motorL,motorR,imu,bumper);

        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");

        sortingColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("sortingColor")
        );

        fieldColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("fieldColor")
        );

        gateServo = hardwareMap.get(Servo.class,"gateServo");

        sorting = new sortingModule(motorSeparator,sortingColorSensor,fieldColorSensor,gateServo);



        moving.setSpeed(1);


        while(true) {

            boolean gotColor = sorting.getTeamColor();
            if( gotColor ) break;
            else if( TeamColor != Color.NONE ) {

                sorting.teamColor = TeamColor;
                break;

            }

        }

        waitForStart();

        while(bumper.getState() == TouchState.RELEASED);
        while(bumper.getState() == TouchState.PRESSED);

        brushes.startBrushes();

    }

    public boolean inPos = true;

    public void updatePeripheral() {

        sorting.tick();
        brushes.tick();
        inPos = moving.tick();

        telemetry.addLine("Robot yaw: " + String.valueOf(moving.getYaw()));
        telemetry.addLine("Action: " + String.valueOf(moving.realTimeAction));
        telemetry.addLine("Left motor: " + String.valueOf(motorL.getPower()));
        telemetry.addLine("Right motor: " + String.valueOf(motorR.getPower()));
        telemetry.addLine("Field color: " + String.valueOf(sorting.fieldColor.getColor()));
        telemetry.addLine("Brushes blocked: " + String.valueOf(brushes.isBlocked));
        telemetry.update();
    }

    @Override
    public void runOpMode() {}

}
