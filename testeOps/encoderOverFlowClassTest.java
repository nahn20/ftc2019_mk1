package org.firstinspires.ftc.teamcode;

import android.app.Activity;
import android.graphics.Color;
import android.view.View;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name="testeOp", group="Test")
public class testeOp extends LinearOpMode {
    shellFish shell = new shellFish();
    toggleMap toggleMap1 = new toggleMap();
    useMap useMap1 = new useMap();

    
    toggleMap toggleMap2 = new toggleMap();
    useMap useMap2 = new useMap();
    
    encoderClass encoder1 = new encoderClass();

    private ElapsedTime runtime = new ElapsedTime();
    
    @Override
    public void runOpMode() {

        shell.init(hardwareMap);
        shell.imu();
        
        while(!opModeIsActive()){
        }
        encoder1.init(shell.encoder1.getVoltage());
        while(opModeIsActive()){

            encoder1.update(shell.encoder1.getVoltage());
            telemetry.addData("Angle", encoder1.angle);
            updateKeys();
            telemetry.update();//THIS GOES AT THE END
        }
    }
    ///////////
    // TESTS //
    ///////////

    ////////////////////////////////
    // TOGGLES ////////// USE MAP //
    ////////////////////////////////
    public void updateKeys(){ 
        if(gamepad1.a && cdCheck(useMap1.a, 1000)){
            toggleMap1.a = toggle(toggleMap1.a);
            useMap1.a = runtime.milliseconds();
        }
        if(gamepad1.b && cdCheck(useMap1.b, 500)){
            toggleMap1.b = toggle(toggleMap1.b);
            useMap1.b = runtime.milliseconds();
        }
        if(gamepad2.b && cdCheck(useMap2.b, 500)){
            toggleMap2.b = toggle(toggleMap2.b);
            useMap2.b = runtime.milliseconds();
        }
        if(gamepad1.right_stick_x > 0 && cdCheck(useMap1.right_stick_x_pos, 700)){
            toggleMap1.right_stick_x_pos = toggle(toggleMap1.right_stick_x_pos);
            useMap1.right_stick_x_pos = runtime.milliseconds();
        }
    }
    public boolean cdCheck(double key, int cdTime){
        return runtime.milliseconds() - key > cdTime;
    }
    public boolean toggle(boolean variable){
        if(variable == true){
            variable = false;
        }
        else if(variable == false){
            variable = true;
        }
        return variable;
    }
}

