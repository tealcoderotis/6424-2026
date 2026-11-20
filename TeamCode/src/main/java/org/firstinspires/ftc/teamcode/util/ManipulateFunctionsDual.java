package org.firstinspires.ftc.teamcode.util;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.hardware.Servo;

public class ManipulateFunctionsDual {
    private DcMotorEx shooterMotor = null;
    private DcMotorEx shooterMotorSecondary = null;
    private DcMotor intakeMotor = null;
    private Servo gate = null;
    private final double PulsesPerRotation = 28.0; // From 6000 RPM yellow jacket motor datasheet
    public final double RPMtoVelocity = PulsesPerRotation/60.0;
    public void manipulateInit(com.qualcomm.robotcore.hardware.HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, "intake");
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotorSecondary = hardwareMap.get(DcMotorEx.class, "");
        gate = hardwareMap.get(Servo.class, "gate");
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        shooterMotorSecondary.setDirection(DcMotor.Direction.FORWARD);

        intakeMotor.setZeroPowerBehavior(BRAKE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotorSecondary.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));
        shooterMotorSecondary.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        gate.setPosition(0);
    }
    public void intake(boolean doIntake, boolean reverse) {
        double intakePower = 0;
        if (doIntake) {
            intakePower = 1;
        }
        if (reverse) {
            if (doIntake) {
                intakePower = 0;
            } else {
                intakePower = -1;
            }
        }
        intakeMotor.setPower(intakePower);
    }
    public void shoot(boolean doRev, boolean doShoot, double targetRPM, org.firstinspires.ftc.robotcore.external.Telemetry telemetry) {
        if (doRev) {
            shooterMotor.setVelocity(targetRPM * RPMtoVelocity);
            shooterMotorSecondary.setVelocity(targetRPM * RPMtoVelocity);
            double trueRPM = shooterMotor.getVelocity()/RPMtoVelocity;
            double trueRPMSecondary = shooterMotorSecondary.getVelocity()/RPMtoVelocity;
            telemetry.addData("True RPM Primary Motor: ", trueRPM);
            telemetry.addData("True RPM Secondary Motor: ", trueRPMSecondary);
            telemetry.addData("Goal RPM: ", targetRPM);
            if (doShoot) {
                intakeMotor.setPower(1);
                gate.setPosition(1);
            } else {
                intakeMotor.setPower(0);
                gate.setPosition(0);
            }
        } else {
            shooterMotor.setVelocity(0);
            shooterMotorSecondary.setVelocity(0);
            gate.setPosition(0);
        }
    }
}
