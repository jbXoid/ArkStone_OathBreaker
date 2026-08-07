package org.firstinspires.ftc.teamcode.OpModes.TESTS;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.hardware.adafruit.AdafruitI2cColorSensor;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fixColorSensors;

@TeleOp
public class ColorSensorsTest extends LinearOpMode {

    private AdafruitI2cColorSensor sortingColorSensor;
    private AdafruitI2cColorSensor fieldColorSensor;


    @Override
    public void runOpMode() {

        sortingColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("sortingColor")
        );

        fieldColorSensor = fixColorSensors.fix(
                (AdafruitI2cColorSensor) hardwareMap.get("fieldColor")
        );

        sortingColorSensor.initialize();
        sortingColorSensor.setGain(40);

        fieldColorSensor.initialize();
        fieldColorSensor.setGain(40);

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        waitForStart();

        while(opModeIsActive()) {

            int sortingColorRed = sortingColorSensor.red();
            int sortingColorGreen = sortingColorSensor.green();
            int sortingColorBlue = sortingColorSensor.blue();

            int fieldColorRed = fieldColorSensor.red();
            int fieldColorGreen = fieldColorSensor.green();
            int fieldColorBlue = fieldColorSensor.blue();



            telemetry.addLine("Sorting red: " + String.valueOf(sortingColorRed));
            telemetry.addLine("Sorting green: " + String.valueOf(sortingColorGreen));
            telemetry.addLine("Sorting blue: " + String.valueOf(sortingColorBlue));

            telemetry.addLine("Field red: " + String.valueOf(fieldColorRed));
            telemetry.addLine("Field green: " + String.valueOf(fieldColorGreen));
            telemetry.addLine("Field blue: " + String.valueOf(fieldColorBlue));

            telemetry.update();

        }


    }
}
