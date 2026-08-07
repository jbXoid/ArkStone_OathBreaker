
package org.firstinspires.ftc.teamcode.peripheral.modules;


import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.Servo;

import org.firstinspires.ftc.robotcore.external.navigation.CurrentUnit;

import com.qualcomm.robotcore.util.ElapsedTime;


public class brushesModule {

    private Servo servoL;
    private Servo servoR;
    private DcMotorEx motorBrushes;
    private ElapsedTime elapsedTime;

    private final double blockReverseTime = 500;
    private final double startTime = 250;
    private final double blockCurrentThreshold = 1.5;
    private final double blockTimeThreshold = 750;


    public boolean isBlocked = false;
    private boolean isLargerThenCurrentThresh = false;
    private double blockTimeOut = 0;
    private double reverseTimeOut = 0;
    private double startTimeOut = 0;
    private boolean isOn = false;

    public brushesModule(DcMotorEx motorBrushes, Servo servoL, Servo servoR) {

        this.motorBrushes = motorBrushes;
        this.motorBrushes.setMode(DcMotor.RunMode.RUN_WITHOUT_ENCODER);
        this.motorBrushes.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.FLOAT);
        this.motorBrushes.setDirection(DcMotor.Direction.FORWARD);

        this.servoL = servoL;
        this.servoL.setDirection(Servo.Direction.REVERSE);
        this.servoR = servoR;
        this.servoR.setDirection(Servo.Direction.FORWARD);

        elapsedTime = new ElapsedTime();

    }

    private void brushesIn() {

        this.servoL.setPosition(1);
        this.servoR.setPosition(1);
        this.motorBrushes.setPower(1);

    }

    private void brushesOut() {

        this.servoL.setPosition(0);
        this.servoR.setPosition(0);
        this.motorBrushes.setPower(-0.5);

    }

    private void brushesStop() {

        this.servoL.setPosition(0.5);
        this.servoR.setPosition(0.5);
        this.motorBrushes.setPower(0);

    }


    public void startBrushes() {

        this.isOn = true;

    }

    public void stopBrushes() {

        this.isOn = false;

    }

    public void tick() {

        if(isOn) {

            if(isBlocked) {

                if(elapsedTime.milliseconds() - reverseTimeOut >= blockReverseTime && motorBrushes.getCurrent(CurrentUnit.AMPS) < this.blockCurrentThreshold ) {

                    isBlocked = false;
                    isLargerThenCurrentThresh = false;
                    this.startTimeOut = elapsedTime.milliseconds();

                }

            }

            else {

                if (elapsedTime.milliseconds() - startTimeOut >= startTime) {

                    if( motorBrushes.getCurrent(CurrentUnit.AMPS) > blockCurrentThreshold ) {

                        if( isLargerThenCurrentThresh ) {

                            if( elapsedTime.milliseconds() - blockTimeOut >= blockTimeThreshold ) {

                                isBlocked = true;
                                reverseTimeOut = elapsedTime.milliseconds();

                            }

                        }
                        else {

                            isLargerThenCurrentThresh = true;
                            blockTimeOut = elapsedTime.milliseconds();

                        }

                    }
                    else {

                        isLargerThenCurrentThresh = false;

                    }

                }

            }

            if(isBlocked) {
                brushesOut();
            }
            else {
                brushesIn();
            }

        }

        else {

            brushesStop();

        }

    }

}
