package org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch;

import com.qualcomm.robotcore.hardware.AnalogInput;

public class Bumper {

    private AnalogInput analogTouchSensor1;
    private AnalogInput analogTouchSensor2;

    public Bumper(AnalogInput analogTouchSensor1, AnalogInput analogTouchSensor2) {

        this.analogTouchSensor1 = analogTouchSensor1;
        this.analogTouchSensor2 = analogTouchSensor2;

    }

    public TouchState getState() {

        if(analogTouchSensor1.getVoltage()>1.65 || analogTouchSensor2.getVoltage()>1.65) return TouchState.PRESSED;
        else return TouchState.RELEASED;

    }

}
