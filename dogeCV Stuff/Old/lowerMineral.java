package org.firstinspires.ftc.teamcode;

import com.disnodeteam.dogecv.CameraViewDisplay;
import com.disnodeteam.dogecv.DogeCV;
import com.disnodeteam.dogecv.detectors.roverrukus.GoldAlignDetector;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.util.ElapsedTime;

import org.firstinspires.ftc.robotcore.external.navigation.AngleUnit;
import org.firstinspires.ftc.robotcore.external.navigation.AxesOrder;
import org.firstinspires.ftc.robotcore.external.navigation.AxesReference;
import org.firstinspires.ftc.robotcore.external.navigation.Orientation;

@Autonomous(name="lowerMineral", group="Testy")
public class lowerMineral extends LinearOpMode{

    private ElapsedTime runtime = new ElapsedTime();
    shellFish shell = new shellFish();

    encoderClass encoderX = new encoderClass();
    encoderClass encoderY = new encoderClass();

    private GoldAlignDetector shapeSensor;

    String cheese = "none"; //Better to use int, but here I get to write string cheese

    double x = 0; //From drop, driving out is +x
    double y = 0; //From drop, driving left is +y, driving right is -y
    int fullRotationCount = 0;
    double previousAngle = 0;
    double angle = 0;

    double time;

    //PID STUFF\\
    double kDrive[] = {.3, 10.82, 3.72}; //PID constants for linear drive power
    double kRotate[] = {1, 4.06, 2.31}; //PID constants for rotation

    double errorDrive = 0;
    double errorRotate = 0;
    double integralDrive = 0;
    double derivativeDrive = 0;
    double integralRotate = 0;
    double derivativeRotate = 0;

    double lastTime = 0;
    @Override
    public void runOpMode(){
        telemetry.addData(">", "Wait");
        telemetry.update();
        shell.init(hardwareMap);

        telemetry.addData(">", "Loading IMU.");
        telemetry.update();
        shell.imu();
        telemetry.addData(">", "Preparing DogeCV");
        telemetry.update();

        shapeSensor = new GoldAlignDetector();
        shapeSensor.init(hardwareMap.appContext, CameraViewDisplay.getInstance());
        shapeSensor.useDefaults();

        // Optional Tuning
        shapeSensor.alignSize = 100; // How wide (in pixels) is the range in which the gold object will be aligned. (Represented by green bars in the preview)
        shapeSensor.alignPosOffset = 0; // How far from center frame to offset this alignment zone.
        shapeSensor.downscale = 0.4; // How much to downscale the input frames

        shapeSensor.areaScoringMethod = DogeCV.AreaScoringMethod.MAX_AREA; // Can also be PERFECT_AREA
        //shapeSensor.perfectAreaScorer.perfectArea = 10000; // if using PERFECT_AREA scoring
        shapeSensor.maxAreaScorer.weight = 0.005;

        shapeSensor.ratioScorer.weight = 5;
        shapeSensor.ratioScorer.perfectRatio = 1.0;

        shapeSensor.enable();

        telemetry.addData(">", "Ready ayaya");
        telemetry.update();

        while(!opModeIsActive()){
        }
        ////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////
        // S T A R T /\/\ S T A R T /\/\ S T A R T /\/\ S T A R T \\
        ////////////////////////////////////////////////////////////
        ////////////////////////////////////////////////////////////

        double startTime = runtime.milliseconds();
        encoderX.init(shell.encoderX.getVoltage());
        encoderY.init(shell.encoderY.getVoltage());
        for(int i = 0; i < 3 && cheese == "none"; i++) {
            if(i == 0){
                doublePID(0, 2, 0, false);
            }
            if(i == 1){
                doublePID(0, 2, 0.55, false);
            }
            if(i == 2){
                doublePID(0, 2, -0.55, false);
            }
            checkCheese();
        }
        if(cheese == "none"){
            cheese = "center";
        }
        shapeSensor.disable();
        telemetry.addData("Cheese", cheese);
        telemetry.update();
        if(cheese == "right"){
            doublePID(7.64, 14.37, -0.5, false);
        }
        if(cheese == "center"){
            doublePID(0, 15.66, 0, false);
        }
        if(cheese == "left"){
            doublePID(-7.64, 14.37, 0.5, false);
        }


    }
    public void doublePID(double desiredX, double desiredY, double desiredAngle, boolean driveBy){
        double driveTime = runtime.milliseconds();
        lastTime = runtime.milliseconds();
        do{
            updateCoordinates();
            double errorX = x-desiredX;
            double errorY = desiredY-y;
            double theta = Math.atan2(errorY, errorX);
            if(Math.abs(theta - 0) < 0.5 || Math.abs(theta - Math.PI) < 0.5){
                kDrive[0] = .15;
                kDrive[2] = 2;
            }
            else{
                kDrive[0] = .3;
                kDrive[2] = 3.72;
            }
            if(theta < 0){
                theta += Math.PI;
            }
            theta = -(theta-Math.PI);
            double deltaT = (runtime.milliseconds() - lastTime); //Delta time. Subtracts last time of tick from current time
            double tempErrorDrive = errorDrive;
            double tempErrorRotate = errorRotate;
            errorDrive = Math.sqrt(Math.pow(errorX, 2) + Math.pow(errorY, 2));
            if(errorY < 0){
                errorDrive *= -1;
            }
            errorRotate = angle - desiredAngle;
            double pDrive = 0;
            double pRotate = 0;

            integralDrive += errorDrive*deltaT/10000;
            if(Math.abs(errorDrive) > 1){
                integralDrive = 0;
            }
            derivativeDrive = (tempErrorDrive-errorDrive)/deltaT;
            pDrive = kDrive[0]*errorDrive + kDrive[1]*integralDrive + -10*kDrive[2]*derivativeDrive;

            integralRotate += errorRotate*deltaT/10000;
            if(Math.abs(errorRotate) > Math.PI/8){
                integralRotate = 0;
            }
            derivativeRotate = (tempErrorRotate-errorRotate)/deltaT;
            pRotate = kRotate[0]*errorRotate + kRotate[1]*integralRotate + -10*kRotate[2]*derivativeRotate;

            if(driveBy){
                pDrive = Math.signum(pDrive);
            }
            autoDrive(theta, pDrive, pRotate);
            if(Math.abs(errorDrive) > 0.15 || Math.abs(errorRotate) > 0.04){ //Change 0.1 to error margin if needed
                driveTime = runtime.milliseconds();
            }
            lastTime = runtime.milliseconds();
            telemetry.addData("errorDrive", errorDrive);
            telemetry.update();
        }
        while(((runtime.milliseconds() - driveTime < 200 && !driveBy) || (Math.abs(errorDrive) > 0.8 && driveBy)) && opModeIsActive());
        shell.front_left_motor.setPower(0);
        shell.back_left_motor.setPower(0);
        shell.back_right_motor.setPower(0);
        shell.front_right_motor.setPower(0);
    }
    public void autoDrive(double theta, double magnitude, double Protate){
        double modifiedTheta = theta + Math.PI/4 - angle;

        magnitude *= (1-Math.abs(Protate)); //Multiplied by (1-Protate) so it doesn't go over 1 with rotating
        double Px = magnitude * Math.cos(modifiedTheta);
        double Py = magnitude * Math.sin(modifiedTheta);

        shell.front_left_motor.setPower(Py + Protate);
        shell.back_left_motor.setPower(Px - Protate);
        shell.back_right_motor.setPower(Py - Protate);
        shell.front_right_motor.setPower(Px + Protate);
    }
    public void driveTime(double time, double Px, double Py){
        double start = runtime.milliseconds();
        while(runtime.milliseconds() - start < time && opModeIsActive()){
            shell.front_left_motor.setPower(Py);
            shell.back_left_motor.setPower(Px);
            shell.back_right_motor.setPower(Py);
            shell.front_right_motor.setPower(Px);
        }
        shell.front_left_motor.setPower(0);
        shell.back_left_motor.setPower(0);
        shell.back_right_motor.setPower(0);
        shell.front_right_motor.setPower(0);
    }
    public void checkCheese() {
        double xView = shapeSensor.getXPosition();
        int range = 200;
        telemetry.addData("X Pos", xView);
        telemetry.update();
        double t = runtime.milliseconds();
        while (!(xView > 300 - range && xView < 300 + range) && runtime.milliseconds() - t < 1000) {
        }
        if(xView > 300 - range && xView < 300 + range){
            double roboAngle = getHeading();
            if (roboAngle < Math.PI / 12 && roboAngle > -Math.PI / 12) {
                cheese = "center";
            }
            if (roboAngle > Math.PI / 6 && roboAngle < Math.PI / 2) {
                cheese = "left";
            }
            if (roboAngle < -Math.PI / 6 && roboAngle > -Math.PI / 2) {
                cheese = "right";
            }
        }
    }
    public void sleepNotSleep(double time){
        double start = runtime.milliseconds();
        while(runtime.milliseconds() - start < time && opModeIsActive()){
        }
    }
    public void updateCoordinates(){
        encoderX.update(shell.encoderX.getVoltage());
        encoderY.update(shell.encoderY.getVoltage());
        angleOverflow();
        angle = getHeading();
        double deltaYAngle = -encoderY.deltaAngle;
        double deltaXAngle = encoderX.deltaAngle;
        double movementAngle = Math.atan2(deltaYAngle, deltaXAngle);
        x -= Math.sqrt(Math.pow(deltaXAngle, 2)+Math.pow(deltaYAngle, 2))*Math.cos(movementAngle-angle);
        y += Math.sqrt(Math.pow(deltaXAngle, 2)+Math.pow(deltaYAngle, 2))*Math.sin(movementAngle-angle);
    }
    public void angleOverflow(){ //Increase fullRotationCount when angle goes above 2*PI or below 0 
        double heading = getHeading() - fullRotationCount*(2*Math.PI);
        //Warning: Will break if the robot does a 180 in less thank 1 tick, but that probably won't happen
        if(heading < Math.PI/2 && previousAngle > 3*Math.PI/2){
            fullRotationCount++;
        }
        if(heading > 3*Math.PI/2 && previousAngle < Math.PI/2){
            fullRotationCount--;
        }
        previousAngle = heading;
    }
    public double getHeading(){ //Includes angle subtraction, angle to radian conversion, and 180>-180 to regular system conversion
        Orientation angles = shell.imu.getAngularOrientation(AxesReference.INTRINSIC, AxesOrder.ZYX, AngleUnit.DEGREES);
        double heading = angles.firstAngle;
        heading = (Math.PI/180)*heading;
        if(heading < 0){
            heading = (2*Math.PI) + heading;
        }
        heading += fullRotationCount*(2*Math.PI);
        return heading;
    }
}