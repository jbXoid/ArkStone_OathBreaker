package org.firstinspires.ftc.teamcode;

import com.acmerobotics.dashboard.FtcDashboard;
import com.acmerobotics.dashboard.telemetry.MultipleTelemetry;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

@TeleOp
public class MotorCurrentTest extends LinearOpMode {

    private DcMotorEx motorBrushes;

    @Override
    public void runOpMode() {

        telemetry = new MultipleTelemetry(telemetry, FtcDashboard.getInstance().getTelemetry());

        motorBrushes = hardwareMap.get(DcMotorEx.class,"motorBrushes");
        motorBrushes.setMotorEnable();
        motorBrushes.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        motorBrushes.setDirection(DcMotorSimple.Direction.REVERSE);

        waitForStart();

        motorBrushes.setPower(1);
        while(opModeIsActive()) {

            double brushesCurrent = motorBrushes.getCurrent(CurrentUnit.AMPS);

            telemetry.addLine("Brushes current: " + String.valueOf(brushesCurrent));

            telemetry.update();

        }

    }

}
