package org.firstinspires.ftc.teamcode.util;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class ManipulateFunctions {
    private DcMotorEx shooter = null;
    private DcMotor intake = null;
    private CRServo leftIntake = null;
    private CRServo rightIntake = null;
    private final double RPMtoVelocity = 0.46676;
    public double LAUNCHER_TARGET_VELOCITY = 1250; //2678 RPM
    public double LAUNCHER_MIN_VELOCITY = 1200; //2571 RPM
    public void manipulateInit(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap) {
        intake = hardwareMap.get(DcMotor.class, "intake");
        shooter = hardwareMap.get(DcMotorEx.class, "shooter");
        leftIntake = hardwareMap.get(CRServo.class, "leftIntake");
        rightIntake = hardwareMap.get(CRServo.class, "rightIntake");
        shooter.setDirection(DcMotor.Direction.REVERSE);

        intake.setZeroPowerBehavior(BRAKE);
        shooter.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooter.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        leftIntake.setPower(0);
        rightIntake.setPower(0);
        leftIntake.setDirection(DcMotorSimple.Direction.REVERSE);
    }
    public void manipulate(boolean right_bumper, boolean btnA, org.firstinspires.ftc.robotcore.external.Telemetry telemetry) {
        if (right_bumper) {
            shooter.setVelocity(LAUNCHER_TARGET_VELOCITY * RPMtoVelocity);
            double trueVelocity = shooter.getVelocity();
            telemetry.addData("True velocity: ", trueVelocity);
        } else {
            shooter.setVelocity(0);
        }
        double intakePower = 0;
        if (btnA) {
            intakePower = 1;
        }
        intake.setPower(intakePower);
        leftIntake.setPower(intakePower);
        rightIntake.setPower(intakePower);
    }
}
