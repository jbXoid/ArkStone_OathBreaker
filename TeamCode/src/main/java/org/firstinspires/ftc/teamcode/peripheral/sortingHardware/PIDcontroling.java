package org.firstinspires.ftc.teamcode.peripheral.sortingHardware;

import static java.lang.Math.abs;

import com.acmerobotics.dashboard.config.Config;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.util.ElapsedTime;

@Config
public class PIDcontroling {

        public static double kP = 0.025;
        public static double kI = 0;
        public static double kD = 0.25;
        public static double PIDdelay = 5;
        public static double motorMin = 0.25;
        public static double motorMax = 0.75;
        public static double PIDtimeout = 10;

        public int setPointDegrees = 0;

        DcMotor motor;
        ElapsedTime runtime = new ElapsedTime();
        public PIDcontroling(DcMotor motor) {
            this.motor = motor;
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

        public boolean tick() {

            double dt = runtime.milliseconds() - this.prevTime;

            int encoderVal = motor.getCurrentPosition();

            if(dt >= PIDdelay) {

                this.resP = this.setPointDegrees - encoderVal/2;
                if (this.resP != 0) {

                    if (abs(PIDoutput) < this.motorMax && abs(PIDoutput) > this.motorMax) {
                        this.resI = this.resP * dt;
                    }

                    this.resD = (this.resP - this.prevErr) / dt;

                    this.PIDoutput = this.kP * resP + this.kI * resI + this.kD * this.resD;
                } else {
                    this.PIDoutput = 0;
                }

                if(PIDoutput > 0){

                    if (this.PIDoutput > this.motorMax) {
                        this.PIDoutput = this.motorMax;
                    } else if (this.PIDoutput < this.motorMin) {
                        this.PIDoutput = this.motorMin;
                    }

                }
                else if (PIDoutput < 0) {

                    if(this.PIDoutput < -this.motorMax) {
                        this.PIDoutput = -this.motorMax;
                    }
                    else if(this.PIDoutput > -this.motorMin) {
                        this.PIDoutput = -this.motorMin;
                    }

                }



                if (this.PIDoutput == 0) {

                    if (!this.prevIsZero) {
                        this.prevZeroCrossingTime = runtime.milliseconds();
                    }
                    this.prevIsZero = true;

                } else {
                    this.prevIsZero = false;
                }

                motor.setPower(this.PIDoutput);

                this.prevErr = this.resP;
                this.prevTime = runtime.milliseconds();

            }

            if (runtime.milliseconds() - this.prevZeroCrossingTime >= this.PIDtimeout && this.prevZeroCrossingTime != 0 && this.prevIsZero) {
                return true;
            }
            else {
                return false;
            }

        }




}
