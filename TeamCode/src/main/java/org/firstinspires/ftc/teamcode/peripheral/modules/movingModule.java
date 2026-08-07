package org.firstinspires.ftc.teamcode.peripheral.modules;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.Bumper;
import org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch.TouchState;
import org.firstinspires.ftc.teamcode.peripheral.hardware.moving.PIDwheelbase;

import org.firstinspires.ftc.teamcode.peripheral.hardware.moving.Action;

import java.nio.channels.Pipe;

public class movingModule {

    private DcMotorEx motorL;
    private DcMotorEx motorR;
    private IMU imu;
    private AnalogInput touch1;
    private AnalogInput touch2;
    private Bumper bumper;
    public PIDwheelbase PIDcontrol;
    private ElapsedTime elapsedTime;
    private double speed = 0.5;

    public movingModule(DcMotorEx motorL, DcMotorEx motorR, IMU imu, Bumper bumper) {

        this.motorL = motorL;
        this.motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motorL.setDirection(DcMotorSimple.Direction.FORWARD);

        this.motorR = motorR;
        this.motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motorR.setDirection(DcMotorSimple.Direction.REVERSE);

        this.bumper = bumper;

        this.imu = imu;

        this.PIDcontrol = new PIDwheelbase(this.imu);

        elapsedTime = new ElapsedTime();

        this.motorL.setMotorEnable();
        this.motorR.setMotorEnable();

    }

    public Action realTimeAction = Action.NONE;


    public void setSpeed(double speed) {

        this.speed = speed;
        this.PIDcontrol.speed = speed;

    }

    public void addYawAngle(double angle) {

        realTimeAction = Action.ROTATING;
        PIDcontrol.addAngle(angle);

    }

    public void setYawAngle(double angle) {

        realTimeAction = Action.ROTATING;
        PIDcontrol.setAngle(angle);

    }

    private double forwardTimeOut = 0;
    private double forwardSleep = 0;

    public void forwardToTimeout(double ms) {

        if (realTimeAction == Action.NONE) {

            forwardSleep = ms;

            forwardTimeOut = elapsedTime.milliseconds();
            realTimeAction = Action.FORWARD_TO_TIMEOUT;

        }

    }

    public void backwardToTimeout(double ms) {

        if (realTimeAction == Action.NONE) {

            forwardSleep = ms;

            forwardTimeOut = elapsedTime.milliseconds();
            realTimeAction = Action.BACKWARD_TO_TIMEOUT;

        }

    }

    public void forwardToTouchOrTimeout(double ms) {

        if (realTimeAction == Action.NONE) {

            forwardSleep = ms;

            forwardTimeOut = elapsedTime.milliseconds();
            realTimeAction = Action.FORWARD_TO_TOUCH_OR_TIMEOUT;

        }

    }


    private void forwardTick(double speed) {

        PIDcontrol.tick();

        if (PIDoutput > 0) {

            this.motorL.setPower(speed);
            this.motorR.setPower(speed - PIDoutput);

        } else if (PIDoutput < 0) {

            this.motorL.setPower(speed + PIDoutput);
            this.motorR.setPower(speed);

        } else {
            this.motorL.setPower(speed);
            this.motorR.setPower(speed);
        }

    }

    private void stop() {

        motorL.setPower(0);
        motorR.setPower(0);

    }

    public double getYaw() {

        double realYaw = PIDcontrol.absoluteYaw;
        if(realYaw>0) {
            while (realYaw>360) realYaw -= 360;
        }
        else if(realYaw<0) {
            while (realYaw<360) realYaw += 360;
        }

        return realYaw;

    }

    private double PIDoutput = 0;
    private boolean inPos = false;

    public boolean tick() {

        inPos = PIDcontrol.tick();
        PIDoutput = PIDcontrol.PIDoutput;

        if (realTimeAction == Action.ROTATING) {

            motorL.setPower(PIDoutput);
            motorR.setPower(-PIDoutput);

            if (inPos) {
                realTimeAction = Action.NONE;
            }

        } else if (realTimeAction == Action.FORWARD_TO_TIMEOUT) {

            if (elapsedTime.milliseconds() - forwardTimeOut >= forwardSleep) {
                stop();
                realTimeAction = Action.NONE;
            } else forwardTick(speed);

        } else if (realTimeAction == Action.FORWARD_TO_TOUCH_OR_TIMEOUT) {

            if ((elapsedTime.milliseconds() - forwardTimeOut >= forwardSleep) ||
                    (bumper.getState() == TouchState.PRESSED)) {
                stop();
                realTimeAction = Action.NONE;
            } else forwardTick(speed);

        } else if (realTimeAction == Action.BACKWARD_TO_TIMEOUT) {

            if (elapsedTime.milliseconds() - forwardTimeOut >= forwardSleep) {
                stop();
                realTimeAction = Action.NONE;
            } else forwardTick(-speed);

        } else {
            stop();

        }

        if(realTimeAction == Action.NONE) inPos = true;
        else inPos = false;

        return inPos;
    }
}
