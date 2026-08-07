package org.firstinspires.ftc.teamcode.peripheral.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.IMU;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.teamcode.peripheral.hardware.moving.PIDwheelbase;

import org.firstinspires.ftc.teamcode.peripheral.hardware.moving.Action;

public class movingModule {

    private DcMotorEx motorL;
    private DcMotorEx motorR;
    public PIDwheelbase PIDcontrol;
    private IMU imu;
    private ElapsedTime elapsedTime;
    private double speed = 0.5;

    public movingModule(DcMotorEx motorL, DcMotorEx motorR, IMU imu) {

        this.motorL = motorL;
        this.motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motorL.setDirection(DcMotorSimple.Direction.FORWARD);

        this.motorR = motorR;
        this.motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        this.motorR.setDirection(DcMotorSimple.Direction.REVERSE);

        this.motorL.setMotorEnable();
        this.motorR.setMotorEnable();

        this.imu = imu;

        this.PIDcontrol = new PIDwheelbase(this.imu);

        elapsedTime = new ElapsedTime();

    }

    public Action realTimeAction = Action.NONE;


    public void setSpeed(double speed) {

        this.speed = speed;
        this.PIDcontrol.speed = speed;

    }

    public void addYawAngle(int angle) {

        realTimeAction = Action.ROTATING;
        PIDcontrol.addAngle(angle);

    }

    public void setYawAngle(int angle) {

        realTimeAction = Action.ROTATING;
        PIDcontrol.setAngle(angle);

    }

    private double forwardTimeOut = 0;
    private double forwardSleep = 0;

    public void forwardByTimer(double ms) {

        if (realTimeAction == Action.NONE) {

            forwardSleep = ms;

            forwardTimeOut = elapsedTime.milliseconds();
            realTimeAction = Action.FORWARD_BY_TIMER;

        }

    }

    private void forwardTick() {

        PIDcontrol.tick();

        if (PIDoutput > 0) {

            this.motorL.setPower(speed);
            this.motorR.setPower(speed - PIDoutput);

        } else if (PIDoutput < 0) {

            this.motorL.setPower(speed + PIDoutput);
            this.motorR.setPower(speed);

        } else {
            this.motorL.setPower(0);
            this.motorR.setPower(0);
        }

    }

    private void stop() {

        motorL.setPower(0);
        motorR.setPower(0);

    }

    public double getYaw() {

        return PIDcontrol.realYaw;

    }

    private double PIDoutput = 0;
    private boolean inPos = false;

    public boolean tick() {

        if (realTimeAction != Action.NONE) PIDoutput = PIDcontrol.tick();


        if (realTimeAction == Action.ROTATING) {

            motorL.setPower(PIDoutput);
            motorR.setPower(-PIDoutput);

            if (PIDoutput == 0) {

                inPos = true;
                realTimeAction = Action.NONE;

            } else inPos = false;

            return inPos;

        } else if (realTimeAction == Action.FORWARD_BY_TIMER) {

            if (elapsedTime.milliseconds() - forwardTimeOut >= forwardSleep) {
                motorL.setPower(0);
                motorR.setPower(0);
                realTimeAction = Action.NONE;
            } else forwardTick();

        }

        return false;

    }


}
