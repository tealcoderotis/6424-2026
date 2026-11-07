package org.firstinspires.ftc.teamcode.autons;

import static com.qualcomm.robotcore.hardware.DcMotor.ZeroPowerBehavior.BRAKE;

import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.hardware.CRServo;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.DcMotorSimple;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;

@Autonomous(name="Leave only")
public class PrimitiveLeave extends LinearOpMode {
    private DcMotor leftFrontDrive;
    private DcMotor rightFrontDrive;
    private DcMotor leftBackDrive;
    private DcMotor rightBackDrive;
    private int state = 1;
    private long startTime;
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
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                switch (state) {
                    case 1:
                        leftFrontDrive.setPower(0.5);
                        rightFrontDrive.setPower(0.5);
                        leftBackDrive.setPower(0.5);
                        rightBackDrive.setPower(0.5);
                        startTime = System.currentTimeMillis();
                        state = 2;
                        break;
                    case 2:
                        if (System.currentTimeMillis() >= startTime + 3000) {
                            leftFrontDrive.setPower(0);
                            rightFrontDrive.setPower(0);
                            leftBackDrive.setPower(0);
                            rightBackDrive.setPower(0);
                            startTime = System.currentTimeMillis();
                            state = 3;
                        }
                        break;
                }
            }
        }
    }
}
