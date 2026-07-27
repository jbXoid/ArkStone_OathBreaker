package org.firstinspires.ftc.teamcode.peripheral.modules;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.TouchSensor;

public class movingModule {

    private DcMotorEx motorL;
    private DcMotorEx motorR;
    private TouchSensor touch1;
    private TouchSensor touch2;

    public movingModule(DcMotorEx motorL, DcMotorEx motorR, TouchSensor touch1, TouchSensor touch2) {

        this.motorL = motorL;
        this.motorL.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorL.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorR = motorR;
        this.motorR.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorR.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        this.motorL.setMotorEnable();
        this.motorR.setMotorEnable();

        this.touch1 = touch1;
        this.touch2 = touch2;

    }

    private boolean isOn = false;

    public void startMoving() {

        isOn = true;

    }


    public void tick() {



    }

}
