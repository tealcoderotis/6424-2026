package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.PIDFCoefficients;
import com.qualcomm.robotcore.util.ElapsedTime;

@TeleOp(name = "ShooterPIDFTuner")
public class ShooterPIDFTuner extends OpMode {

    private DcMotorEx shooterMotor = null;

    private final double PulsesPerRotation = 28.0; // From 6000 RPM yellow jacket motor datasheet
    public final double RPMtoVelocity = PulsesPerRotation/60.0;

    double maxRPM = 0;
    private final ElapsedTime runtime = new ElapsedTime();
    private boolean runtimeStopped = true;

    private double seconds = 0;


    double[] coefficients = {40, 0, 0, 12.5};

    int coefficientIndex = 0;

    double[] stepSizes = {1.0, 0.1, 0.01, 0.001};

    int stepIndex = 0;

    public void init() {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(coefficients[0], coefficients[1], coefficients[2], coefficients[3]));
    }

    public void loop() {
        final double RPM = 3200;
        telemetry.addLine("b rotates step size");
        telemetry.addLine("a to change which coefficient you are changing");
        telemetry.addLine("dpad up/down to step up/down the coefficient");
        telemetry.addLine("y to spin up flywheel");
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.aWasPressed()) {
            coefficientIndex = (coefficientIndex + 1) % coefficients.length;
        }

        if (gamepad1.dpadUpWasPressed()) {
            coefficients[coefficientIndex] += stepSizes[stepIndex];
            shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(coefficients[0], coefficients[1], coefficients[2], coefficients[3]));
        }

        if (gamepad1.dpadDownWasPressed()) {
            coefficients[coefficientIndex] -= stepSizes[stepIndex];
            shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(coefficients[0], coefficients[1], coefficients[2], coefficients[3]));
        }

        telemetry.addData("P: ", coefficients[0]);
        telemetry.addData("I: ", coefficients[1]);
        telemetry.addData("D: ", coefficients[2]);
        telemetry.addData("F: ", coefficients[3]);

        if (gamepad1.y) {
            shooterMotor.setVelocity(RPM * RPMtoVelocity);
            if (shooterMotor.getVelocity()/RPMtoVelocity > maxRPM) {
                maxRPM = shooterMotor.getVelocity()/RPMtoVelocity;
            }
            if (shooterMotor.getVelocity()/RPMtoVelocity > RPM * 0.95) {
                runtimeStopped = true;
            }
        } else {
            shooterMotor.setVelocity(0);
        }

        if (gamepad1.yWasReleased()) {
            runtimeStopped = true;
        }

        if (gamepad1.yWasPressed()) {
            runtime.reset();
            runtimeStopped = false;
            maxRPM = 0;
        }

        if (!runtimeStopped) {
            seconds = runtime.seconds();
        }

        telemetry.addData("currentRPM: ", shooterMotor.getVelocity()/RPMtoVelocity);
        telemetry.addData("maxRPM: ", maxRPM);
        telemetry.addData("spinupDuration: ", seconds);
    }
}
