package org.firstinspires.ftc.teamcode.util;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class ManipulateFunctions {
    private DcMotorEx shooterMotor = null;
    private DcMotor intakeMotor = null;
    private Servo gate = null;
    private final double PulsesPerRotation = 28.0; // From 6000 RPM yellow jacket motor datasheet
    public final double RPMtoVelocity = PulsesPerRotation/60.0;
    public void manipulateInit(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        gate = hardwareMap.get(Servo.class, "gate");
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);

        intakeMotor.setZeroPowerBehavior(BRAKE);
        intakeMotor.setDirection(DcMotorSimple.Direction.REVERSE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        gate.setPosition(0.5);
    }
    public void intake(boolean doIntake, boolean reverse) {
        double intakePower = 0;
        if (doIntake) {
            intakePower = 1;
            shooterMotor.setPower(-1);
        }
        if (reverse) {
            if (doIntake) {
                intakePower = 0;
            } else {
                intakePower = -0.5;
                shooterMotor.setVelocity(-1000);
            }
        }
        intakeMotor.setPower(intakePower);
    }
    public void shoot(boolean doRev, boolean doShoot, double targetRPM, org.firstinspires.ftc.robotcore.external.Telemetry telemetry) {
        if (doRev) {
            shooterMotor.setVelocity(targetRPM * RPMtoVelocity);
            double trueRPM = shooterMotor.getVelocity()/RPMtoVelocity;
            telemetry.addData("True RPM: ", trueRPM);
            telemetry.addData("Goal RPM: ", targetRPM);
            if (doShoot) {
                intakeMotor.setPower(0.65);
                gate.setPosition(0.75);
            } else {
                intakeMotor.setPower(0);
                gate.setPosition(0.5);
            }
        } else {
            shooterMotor.setVelocity(0);
        }
    }
    public void setGate(boolean yPressed) {
        if (yPressed) {
            gate.setPosition(0);
        }
    }
}
