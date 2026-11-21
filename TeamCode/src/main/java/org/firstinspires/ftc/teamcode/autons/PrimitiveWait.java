package org.firstinspires.ftc.teamcode.autons;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.pedropathing.utils.Timer;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

@Autonomous(name="Move forward & shoot after 15 seconds")
public class PrimitiveWait extends LinearOpMode {
    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;
    private DcMotorEx shooterMotor = null;
    private DcMotor intakeMotor = null;
    private Servo gate = null;
    /*private CRServo leftIntake = null;
    private CRServo rightIntake = null;*/
    private int state = 0;
    private Timer timer = new Timer();
    private double SHOOTER_VELOCITY = 2800;
    @Override
    public void runOpMode() throws InterruptedException {
        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");
        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);
        leftFrontDrive.setZeroPowerBehavior(BRAKE);
        rightFrontDrive.setZeroPowerBehavior(BRAKE);
        leftBackDrive.setZeroPowerBehavior(BRAKE);
        rightBackDrive.setZeroPowerBehavior(BRAKE);
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        gate = hardwareMap.get(Servo.class, "gate");
        gate.setPosition(0);
        /*leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");*/
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        intakeMotor.setZeroPowerBehavior(BRAKE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));
        /*leftIntake.setPower(0);
        rightIntake.setPower(0);
        leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);*/
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                switch (state) {
                    case 0:
                        timer.reset();
                        state = 1;
                        break;
                    case 1:
                        if (timer.milliseconds() >= 15000) {
                            leftFrontDrive.setPower(0.5);
                            rightFrontDrive.setPower(0.5);
                            leftBackDrive.setPower(0.5);
                            rightBackDrive.setPower(0.5);
                            timer.reset();
                            state = 2;
                        }
                        break;
                    case 2:
                        if (timer.milliseconds() >= 500) {
                            leftFrontDrive.setPower(0);
                            rightFrontDrive.setPower(0);
                            leftBackDrive.setPower(0);
                            rightBackDrive.setPower(0);
                            shooterMotor.setVelocity(SHOOTER_VELOCITY);
                            timer.reset();
                            state = 3;
                        }
                        break;
                    case 3:
                        if (timer.milliseconds() >= 3000) {
                            intakeMotor.setPower(1);
                            gate.setPosition(0.75);
                            /*leftIntake.setPower(1);
                            rightIntake.setPower(1);*/
                            timer.reset();
                            state = 4;
                        }
                        break;
                    case 4:
                        if (timer.milliseconds() >= 10000) {
                            intakeMotor.setPower(0);
                            shooterMotor.setVelocity(0);
                            gate.setPosition(0);
                            /*leftIntake.setPower(0);
                            rightIntake.setPower(0);*/
                            timer.reset();
                            state = 5;
                        }
                        break;
                }
                telemetry.addData("State", state);
                telemetry.addData("Time", timer.milliseconds());
            }
        }
    }
}
