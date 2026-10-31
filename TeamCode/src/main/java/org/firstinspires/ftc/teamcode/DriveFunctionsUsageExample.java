package org.firstinspires.ftc.teamcode;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

import org.firstinspires.ftc.teamcode.util.DriveFunctions;

@TeleOp(name = "teleOpTest")
//@Disabled
public class DriveFunctionsUsageExample extends OpMode {
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

    DriveFunctions drive = new DriveFunctions();

    @Override
    public void init() {

        drive.driveIMUInit(hardwareMap);
        intake = hardwareMap.get(DcMotor.class, "intake");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");
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

        drive.mecanumFieldDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, false);

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
