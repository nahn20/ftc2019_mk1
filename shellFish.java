package org.firstinspires.ftc.teamcode;

import com.qualcomm.hardware.bosch.BNO055IMU;
import com.qualcomm.hardware.bosch.JustLoggingAccelerationIntegrator;
import com.qualcomm.robotcore.hardware.AnalogInput;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.DigitalChannel;
import com.qualcomm.robotcore.hardware.DistanceSensor;
import com.qualcomm.robotcore.hardware.ColorSensor;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.Servo;
import com.qualcomm.robotcore.hardware.ServoControllerEx;
import com.qualcomm.robotcore.hardware.TouchSensor;
import com.qualcomm.robotcore.util.ElapsedTime;

public class shellFish{
    //////////////////
    /* DECLARATIONS */
    //////////////////

    //public ColorSensor colorSensor = null;


    //DRIVE//
    public DcMotor front_left_motor   = null;
    public DcMotor front_right_motor  = null;
    public DcMotor back_left_motor    = null;
    public DcMotor back_right_motor   = null;

    public DcMotor chadArm1 = null;
    public DcMotor chadArm2 = null;

    public DcMotor chadExtend = null;

    public DcMotor hook = null;

    //public DcMotor chungoid = null;
    public Servo chungoid1 = null;
    public Servo chungoid2 = null;
    
    public CRServo intake1 = null;
    public CRServo intake2 = null;

    public AnalogInput encoderX = null;
    public AnalogInput encoderY = null;

    //IMU//
    BNO055IMU imu;

    HardwareMap hwMap           =  null;

    /* Constructor */
    public shellFish(){
    }

    /* Initialize standard Hardware interfaces */
    public void init(HardwareMap hwMap) {
        //////////////////////////////////
        /* RETRIEVING STUFF FROM PHONES */
        //////////////////////////////////

        //colorSensor = hwMap.get(ColorSensor.class, "colorSensor");

        //DRIVE//
        front_left_motor   = hwMap.dcMotor.get("fl");
        back_left_motor    = hwMap.dcMotor.get("bl");
        back_right_motor   = hwMap.dcMotor.get("br");
        front_right_motor  = hwMap.dcMotor.get("fr");
        front_left_motor.setDirection(DcMotor.Direction.REVERSE);
        back_left_motor.setDirection(DcMotor.Direction.FORWARD);
        back_right_motor.setDirection(DcMotor.Direction.FORWARD);
        front_right_motor.setDirection(DcMotor.Direction.REVERSE);
        front_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_left_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        back_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        front_right_motor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        chadArm1 = hwMap.dcMotor.get("chadArm1");
        chadArm2 = hwMap.dcMotor.get("chadArm2");
        chadArm1.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chadArm2.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);
        chadArm1.setDirection(DcMotor.Direction.REVERSE);
        chadArm2.setDirection(DcMotor.Direction.REVERSE);
        // chadArm1.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        // chadArm2.setMode(DcMotor.RunMode.STOP_AND_RESET_ENCODER);
        chadArm1.setMode(DcMotor.RunMode.RUN_TO_POSITION);
        chadArm2.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        chadExtend = hwMap.dcMotor.get("chadExtend");
        chadExtend.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        hook = hwMap.dcMotor.get("hook");
        hook.setMode(DcMotor.RunMode.RUN_TO_POSITION);

        //chungoid = hwMap.dcMotor.get("chungoid");
        chungoid1 = hwMap.servo.get("chungoid1");
        chungoid2 = hwMap.servo.get("chungoid2");
        
        intake1 = hwMap.crservo.get("intake1");
        intake2 = hwMap.crservo.get("intake2");

        encoderX = hwMap.analogInput.get("encoderX");
        encoderY = hwMap.analogInput.get("encoderY");

        //IMU//
        imu = hwMap.get(BNO055IMU.class, "imu");
        //////////////////
        /* STUFFY STUFF */
        //////////////////

    }
    public void imu(){
        /* IMU STUFF */
        BNO055IMU.Parameters parameters = new BNO055IMU.Parameters();

        parameters.mode                = BNO055IMU.SensorMode.IMU;
        parameters.angleUnit           = BNO055IMU.AngleUnit.DEGREES;
        parameters.accelUnit           = BNO055IMU.AccelUnit.METERS_PERSEC_PERSEC;
        parameters.loggingEnabled      = false;
        imu.initialize(parameters);
    }
}
