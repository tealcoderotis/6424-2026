package org.firstinspires.ftc.teamcode.util.shooterIntake;

import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.HardwareMap;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

public class TeleopShooterIntake
{
    private final String INTAKE_HARDWARE_MAP = "intake";
    private final String SHOOTER_HARDWARE_MAP = "shooter";
    private final String ROLLER_LEFT_HARDWARE_MAP = "leftIntake";
    private final String ROLLER_RIGHT_HARDWARE_MAP = "rightIntake";
    private final double SHOOTER_SHOOT_VELOCITY = 1250;
    private final double INDEXER_SHOOT_POWER = 0.5;
    private final double INDEXER_INTAKE_POWER = 1;
    private final double ROLLER_LEFT_POWER = 1;
    private final double ROLLER_RIGHT_POWER = 1;
    private DcMotor intakeMotor;
    private DcMotorEx shooterMotor;
    private CRServo leftRoller;
    private CRServo rightRoller;

    public TeleopShooterIntake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotor.class, INTAKE_HARDWARE_MAP);
        shooterMotor = hardwareMap.get(DcMotorEx.class, SHOOTER_HARDWARE_MAP);
        leftRoller = hardwareMap.get(CRServo.class, ROLLER_LEFT_HARDWARE_MAP);
        rightRoller = hardwareMap.get(CRServo.class, ROLLER_RIGHT_HARDWARE_MAP);

        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(40, 0, 0, 12.5));

        intakeMotor.setZeroPowerBehavior(DcMotor.ZeroPowerBehavior.BRAKE);

        leftRoller.setPower(0);
        rightRoller.setPower(0);
        leftRoller.setDirection(DcMotorSimple.Direction.REVERSE);
    }

    public void beginIntaking() {
        intakeMotor.setPower(INDEXER_INTAKE_POWER);
        leftRoller.setPower(ROLLER_LEFT_POWER);
        rightRoller.setPower(ROLLER_RIGHT_POWER);
    }

    public void stopIntaking() {
        intakeMotor.setPower(0);
        leftRoller.setPower(0);
        rightRoller.setPower(0);
    }

    public void shoot() {
        shooterMotor.setVelocity(SHOOTER_SHOOT_VELOCITY);
        intakeMotor.setPower(INDEXER_SHOOT_POWER);
    }

    public void stopShooting() {
        shooterMotor.setVelocity(0);
        intakeMotor.setPower(0);
    }
}
