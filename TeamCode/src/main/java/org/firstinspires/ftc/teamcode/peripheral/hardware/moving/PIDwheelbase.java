package org.firstinspires.ftc.teamcode.peripheral.hardware.moving;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.hardware.rev.RevHubOrientationOnRobot;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;

@Config
public class PIDwheelbase {

    public static double kP = 0.025;
    public static double kI = 0.00005;
    public static double kD = 0.75;
    public static double PIDdelay = 5;
    public static double motorMin = 0.1;
    public static double motorMax = 0.5;
    public static double PIDtimeout = 50;

    public double setPointDegrees = 0;

    public final IMU imu;
    public double speed = 0.5;
    ElapsedTime runtime = new ElapsedTime();

    public PIDwheelbase(IMU imu) {

        this.imu = imu;

        imu.initialize(
                new IMU.Parameters(
                        new RevHubOrientationOnRobot(
                                RevHubOrientationOnRobot.LogoFacingDirection.UP,
                                RevHubOrientationOnRobot.UsbFacingDirection.FORWARD
                        )
                )
        );


        imu.resetYaw();

        if (speed < motorMax) motorMax = speed;

        runtime.reset();

    }

    double prevTime = 0;
    double resP = 0;
    double resI = 0;
    double resD = 0;
    double prevErr = 0;
    double PIDoutput = 0;
    boolean prevIsZero = false;
    double prevZeroCrossingTime = 0;

    public void addAngle(int deg) {

        setPointDegrees += deg;

    }

    public void setAngle(int deg) {

        setPointDegrees = realYaw - deg;

    }

    public double yaw = 0;

    public double realYaw = 0;
    public double offsetYaw = 0;
    public double prevYaw = 0;

    public double tick() {

        double dt = runtime.milliseconds() - this.prevTime;


        yaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        if (prevYaw - yaw >= 180) {
            offsetYaw += 360;
        } else if (prevYaw - yaw <= -180) {
            offsetYaw -= 360;
        }
        prevYaw = yaw;


        realYaw = yaw + offsetYaw;


        if (dt >= PIDdelay) {

            this.resP = this.setPointDegrees - realYaw;
            if (this.resP != 0) {

                if (abs(PIDoutput) < motorMax && abs(PIDoutput) > motorMin) {
                    this.resI = this.resP * dt;
                }

                this.resD = (this.resP - this.prevErr) / dt;

                this.PIDoutput = kP * resP + kI * resI + kD * this.resD;
            } else {
                this.PIDoutput = 0;
            }

            if (PIDoutput > 0) {

                if (this.PIDoutput > motorMax) this.PIDoutput = motorMax;
                else if (this.PIDoutput < motorMin) {
                    this.PIDoutput = motorMin;
                }

            } else if (PIDoutput < 0) {

                if (this.PIDoutput < -motorMax) {
                    this.PIDoutput = -motorMax;
                } else if (this.PIDoutput > -motorMin) {
                    this.PIDoutput = -motorMin;
                }

            }


            if (this.PIDoutput == motorMin) {

                if (!this.prevIsZero) {
                    this.prevZeroCrossingTime = runtime.milliseconds();
                }
                this.prevIsZero = true;

            } else {
                this.prevIsZero = false;
            }


            this.prevErr = this.resP;
            this.prevTime = runtime.milliseconds();

        }

        if (runtime.milliseconds() - this.prevZeroCrossingTime >= PIDtimeout && this.prevZeroCrossingTime != 0 && abs(setPointDegrees - realYaw) < 1) {

            return 0;

        } else {

            return this.PIDoutput;
        }

    }


}
