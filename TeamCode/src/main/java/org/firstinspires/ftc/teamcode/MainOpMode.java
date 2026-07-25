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

@Autonomous
@Config
public class MainOpMode extends LinearOpMode {

    public static int friendlyPucks = 1;
    private DcMotor motorL;
    public DcMotor motorSeparator;
    private AdafruitI2cColorSensor colorIntake;


    @Override
    public void runOpMode() {

        colorIntake = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("colorIntake")
        );

        colorIntake.initialize();
        colorIntake.setGain(60);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motorL = hardwareMap.get(DcMotor.class,"motorL");
        motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);

        motorSeparator = hardwareMap.get(DcMotor.class,"motorSeparator");
        motorSeparator.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorSeparator.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        PIDcontroling motorSeparatorControl = new PIDcontroling(motorSeparator);
        motorSeparatorControl.setPointDegrees = motorSeparator.getCurrentPosition();

        waitForStart();


        while(opModeIsActive()) {

            boolean PIDstate = motorSeparatorControl.tick();

            int colorRed = colorIntake.red();
            int colorGreen = colorIntake.green();
            int colorBlue = colorIntake.blue();

            telemetry.addLine(String.valueOf(PIDstate));
            telemetry.addLine( "Current motor position: " + String.valueOf( motorSeparator.getCurrentPosition() ) );
            telemetry.addLine( "Motor setpoint: " + String.valueOf( motorSeparatorControl.setPointDegrees ));
            telemetry.addLine( "Red color value: " + String.valueOf( colorRed ) );
            telemetry.addLine( "Green color value: " + String.valueOf( colorGreen ) );
            telemetry.addLine( "Blue color value: " + String.valueOf( colorBlue ) );

            if( PIDstate ) {
                if(     3200<colorRed && colorRed<4600 &&
                        1100<colorGreen && colorGreen<1900 &&
                        800<colorBlue && colorBlue<1200) {

                    motorSeparatorControl.setPointDegrees += 120;

                }
            }

            telemetry.update();

        }

    }

}
