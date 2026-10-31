package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@TeleOp(name = "teleOpTest")
//@Disabled
public class teleOpTest extends OpMode {

    private DcMotor leftFrontDrive = null;
    private DcMotor leftBackDrive = null;
    private DcMotor rightFrontDrive = null;
    private DcMotor rightBackDrive = null;
    private DcMotorEx shooter = null;
    private DcMotor intake = null;
    private CRServo leftIntake = null;
    private CRServo rightIntake = null;

    public final int LAUNCHER_TARGET_VELOCITY = 1250; //2678 RPM
    public final int LAUNCHER_MIN_VELOCITY = 1200; //2571 RPM

    double leftFrontPower;
    double rightFrontPower;
    double leftBackPower;
    double rightBackPower;

    double intakePower;

    @Override
    public void init() {

        leftFrontDrive = hardwareMap.get(DcMotor.class, "leftFrontDrive");
        rightFrontDrive = hardwareMap.get(DcMotor.class, "rightFrontDrive");
        leftBackDrive = hardwareMap.get(DcMotor.class, "leftBackDrive");
        rightBackDrive = hardwareMap.get(DcMotor.class, "rightBackDrive");
        intake = hardwareMap.get(DcMotor.class, "intake");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");

        leftFrontDrive.setDirection(DcMotor.Direction.REVERSE);
        rightFrontDrive.setDirection(DcMotor.Direction.FORWARD);
        leftBackDrive.setDirection(DcMotor.Direction.REVERSE);
        rightBackDrive.setDirection(DcMotor.Direction.FORWARD);

        leftFrontDrive.setZeroPowerBehavior(BRAKE);
        rightFrontDrive.setZeroPowerBehavior(BRAKE);
        leftBackDrive.setZeroPowerBehavior(BRAKE);
        rightBackDrive.setZeroPowerBehavior(BRAKE);
        intake.setZeroPowerBehavior(BRAKE);

        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);

        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftIntake.setPower(0);
        rightIntake.setPower(0);

        leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        mecanumDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        if (gamepad1.a) {
            leftIntake.setPower(1);
            rightIntake.setPower(1);
            intake.setPower(1);
        } else {
            leftIntake.setPower(0);
            rightIntake.setPower(0);
            intake.setPower(0);
        }

        intakePower = gamepad1.right_trigger - gamepad1.left_trigger;

        launch();


        intake.setPower(intakePower);
        leftIntake.setPower(intakePower);
        rightIntake.setPower(intakePower);

        telemetry.addData("Motors", "left (%.2f), right (%.2f)", leftFrontPower, rightFrontPower);
        //telemetry.addData("Triggers", "left (%.2f, right (%.2f)",gamepad1.left_trigger, gamepad1.right_trigger);
    }

    @Override
    public void stop() {
    }

    void mecanumDrive(double forward, double strafe, double rotate) {
        leftFrontPower = forward + strafe + rotate;
        leftBackPower = forward - strafe + rotate;
        rightFrontPower = forward - strafe - rotate;
        rightBackPower = forward + strafe - rotate;

        /*double max = Math.max(Math.abs(leftFrontPower), Math.abs(rightFrontPower));
        max = Math.max(max, Math.abs(leftBackPower));
        max = Math.max(max, Math.abs(rightBackPower));*/
        double max = Math.abs(forward) + Math.abs(strafe) + Math.abs(rotate);

        if (max > 1.0) {
            leftFrontPower /= max;
            rightFrontPower /= max;
            leftBackPower /= max;
            rightBackPower /= max;
        }

        leftFrontDrive.setPower(leftFrontPower);
        rightFrontDrive.setPower(rightFrontPower);
        leftBackDrive.setPower(leftBackPower);
        rightBackDrive.setPower(rightBackPower);
    }

    void launch() {

        if (gamepad1.right_bumper) {
            shooter.setVelocity(LAUNCHER_TARGET_VELOCITY);
            intake.setPower(1);
        } else {
            shooter.setVelocity(0);
            intake.setPower(0);
        }

        if (gamepad1.right_bumper && shooter.getVelocity() > LAUNCHER_MIN_VELOCITY) {
            intakePower += 0.5;
        }
    }
}
