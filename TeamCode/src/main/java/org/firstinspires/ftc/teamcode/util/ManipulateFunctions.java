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
    public void manipulate(float right_trigger, float left_trigger, boolean right_bumper) {
        double intakePower = right_trigger - left_trigger;
        if (right_bumper) {
            shooter.setVelocity(LAUNCHER_TARGET_VELOCITY);
            if (shooter.getVelocity() > LAUNCHER_MIN_VELOCITY) {
                intakePower += 0.5;
            }
        } else {
            shooter.setVelocity(0);
        }
        intake.setPower(intakePower);
        leftIntake.setPower(intakePower);
        rightIntake.setPower(intakePower);
    }
}
