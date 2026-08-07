package org.firstinspires.ftc.teamcode.peripheral.hardware.analogTouch;

import com.qualcomm.robotcore.hardware.AnalogInput;

public class analogTouch {

    private AnalogInput analogTouchSensor;

    public analogTouch(AnalogInput analogTouchSensor) {

        this.analogTouchSensor = analogTouchSensor;

    }

    public AnalogTouchState getState() {

        if(analogTouchSensor.getVoltage()>1.65) return AnalogTouchState.PRESSED;
        else return AnalogTouchState.RELEASED;

    }

}
