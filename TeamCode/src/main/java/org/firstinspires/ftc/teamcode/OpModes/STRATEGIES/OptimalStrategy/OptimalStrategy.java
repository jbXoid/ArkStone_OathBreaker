package org.firstinspires.ftc.teamcode.OpModes.STRATEGIES.OptimalStrategy;

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
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.OpModes.STRATEGIES.Init;
import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.TouchState;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.Color;
import org.firstinspires.ftc.teamcode.peripheral.hardware.colorSensors.fixColorSensors;

import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.Bumper;

import org.firstinspires.ftc.teamcode.peripheral.modules.movingModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.sortingModule;
import org.firstinspires.ftc.teamcode.peripheral.modules.brushesModule;

@Autonomous
@Config
public class OptimalStrategy extends Init {

    @Override
    public void runOpMode() {

        this.init();

        int step = 0;

        while (opModeIsActive()) {

            this.updatePeripheral();

            if (this.inPos) {

                if (step == 0) moving.forwardToTimeout(750);
                else if (step == 1) moving.addYawAngle(-90);
                else if (step == 2) moving.forwardToTouchOrTimeout(1500);
                else if (step == 3) moving.backwardToTimeout(200);
                else if (step == 4) moving.addYawAngle(180);
                else if (step == 5) moving.forwardToTouchOrTimeout(2000);
                else if (step == 6) moving.backwardToTimeout(200);
                else if (step == 7) moving.addYawAngle(-180);
                else if (step == 8) moving.forwardToTimeout(750);
                else if (step == 9) moving.forwardToTouchOrTimeout(2000);
                else if (step == 10) {
                    double angle = 45 + Math.random() * (180 - 45);

                    if (Math.random() > 0.5) {
                        moving.addYawAngle(angle);
                    } else {
                        moving.addYawAngle(-angle);
                    }

                    step = 9;

                }

                if(step<=9) step += 1;

            }


        }
    }

}