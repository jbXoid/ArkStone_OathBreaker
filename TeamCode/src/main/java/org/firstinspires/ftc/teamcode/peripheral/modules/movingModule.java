package org.firstinspires.ftc.teamcode.peripheral.modules;

import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class movingModule {

    private DcMotorEx motorL;
    private DcMotorEx motorR;
    private AnalogInput touch1;
    private AnalogInput touch2;
    private ElapsedTime elapsedTime;
    private double speed = 0.5;

    public movingModule(DcMotorEx motorL, DcMotorEx motorR, AnalogInput touch1, AnalogInput touch2) {

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

        this.touch1 = touch1;
        this.touch2 = touch2;

        elapsedTime = new ElapsedTime();

    }

    private boolean isOn = false;

    public void startMoving() {

        isOn = true;

    }

    private boolean isRotating = false;
    private boolean isBackward = false;
    private double backwardTimeOut = 0;
    private double rotatingTimeOut = 0;

    public void tick() {

        if(isOn) {

            if(isBackward) {

                if(elapsedTime.milliseconds() - backwardTimeOut >= 250) {

                    isBackward = false;
                    isRotating = true;

                    rotatingTimeOut = elapsedTime.milliseconds();

                }

                else {

                    motorL.setPower(-speed);
                    motorR.setPower(-speed);

                }

            }
            else if(isRotating) {

                if(elapsedTime.milliseconds() - rotatingTimeOut >= 250 ) {

                    isRotating = false;

                }
                else {

                    motorL.setPower(-speed);
                    motorR.setPower(speed);

                }

            }
            else {

                if(touch1.getVoltage()>1 || touch2.getVoltage()>1) {

                    isBackward = true;
                    backwardTimeOut = elapsedTime.milliseconds();

                }

                else {

                    motorL.setPower(speed);
                    motorR.setPower(speed);

                }

            }

        }


    }

     public void stopMoving() {

        isOn = false;

     }

}
