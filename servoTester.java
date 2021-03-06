package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.Servo;

@TeleOp(name="servoTester", group="Test")
public class servoTester extends LinearOpMode {
    private Servo servo = null;
    private CRServo crservo1 = null;
    private CRServo crservo2 = null;
    @Override
    public void runOpMode() {
        servo = hardwareMap.servo.get("servo"); //Change "servo" to servo's name in hardware map
        crservo1 = hardwareMap.crservo.get("servo1");
        crservo2 = hardwareMap.crservo.get("servo2");
        while(!opModeIsActive()){}
        
        while(opModeIsActive()){
            servoAdjust();
            servoToLimits();
            servoRun();
            telemetry.addData("Servo Pos", servo.getPosition());
            telemetry.update();
        }
    }
    public void servoRun(){
        crservo1.setPower(1);
        crservo2.setPower(-1);
    }
    public void servoAdjust(){
        servo.setPosition(servo.getPosition() + (-gamepad1.left_stick_y + gamepad1.left_stick_x - gamepad1.right_stick_y + gamepad1.right_stick_x)/1000);
    }
    public void servoToLimits(){
        if(gamepad1.b){
            servo.setPosition(1);
        }
        else if(gamepad1.a){
            servo.setPosition(0);
        }
    }
}