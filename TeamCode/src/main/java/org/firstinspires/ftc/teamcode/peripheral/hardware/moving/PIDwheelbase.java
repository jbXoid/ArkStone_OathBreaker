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

    public static double kP = 0.02;
    public static double kI = 0.0;
    public static double kD = 1.25;
    public static double PIDdelay = 5;
    public static double motorMin = 0.15;
    public static double motorMax = 1;
    public static double PIDtimeout = 50;

    public double setPointDegrees = 0;

    public final IMU imu;
    public double speed = 1;
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

    private double prevTime = 0;
    private double resP = 0;
    private double resI = 0;
    private double resD = 0;
    private double prevErr = 0;
    public double PIDoutput = 0;
    boolean prevIsZero = false;
    public double prevZeroCrossingTime = 0;

    public void addAngle(double deg) {

        setPointDegrees += deg;

    }

    public double getYaw() {

        double realYaw = absoluteYaw;
        if(realYaw>0) {
            while (realYaw>360) realYaw -= 360;
        }
        else if(realYaw<0) {
            while (realYaw<360) realYaw += 360;
        }

        return realYaw;
    }


    public void setAngle(double deg) {

        setPointDegrees = getYaw() - deg;

    }

    public double yaw = 0;
    public double absoluteYaw = 0;
    public double offsetYaw = 0;
    public double prevYaw = 0;

    public boolean tick() {

        double dt = runtime.milliseconds() - this.prevTime;


        yaw = imu.getRobotYawPitchRollAngles().getYaw(AngleUnit.DEGREES);
        if (prevYaw - yaw >= 180) {
            offsetYaw += 360;
        } else if (prevYaw - yaw <= -180) {
            offsetYaw -= 360;
        }
        prevYaw = yaw;


        absoluteYaw = yaw + offsetYaw;


        if (dt >= PIDdelay) {

            this.resP = this.setPointDegrees - absoluteYaw;
            if (this.resP != 0) {

                if ( abs(PIDoutput) < motorMax*0.8 ) {
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


            if (abs(this.PIDoutput) == motorMin) {

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

        if (runtime.milliseconds() - this.prevZeroCrossingTime >= PIDtimeout && prevIsZero && abs(setPointDegrees - absoluteYaw) < 2) {

            PIDoutput = 0;
            return true;


        } else {

            return false;
        }

    }


}
