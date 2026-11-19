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

    private final double RPM = 3000;
    double maxRPM = 0;
    private final ElapsedTime runtime = new ElapsedTime();
    private boolean runtimeStopped = true;

    private double seconds = 0;


    double[] coeffs = {40, 0, 0, 12.5};

    int coeffIndex = 0;

    double[] stepSizes = {1.0, 0.1, 0.01, 0.001};

    int stepIndex = 0;

    public void init() {
        shooterMotor = hardwareMap.get(DcMotorEx.class, "shooter");
        shooterMotor.setDirection(DcMotor.Direction.REVERSE);
        shooterMotor.setMode(DcMotor.RunMode.RUN_USING_ENCODER);
        shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(coeffs[0], coeffs[1], coeffs[2], coeffs[3]));
    }

    public void loop() {
        telemetry.addLine("b rotates step size");
        telemetry.addLine("a to change which coefficient you are changing");
        telemetry.addLine("dpad up/down to step up/down the coefficient");
        telemetry.addLine("y to spin up flywheel");
        if (gamepad1.bWasPressed()) {
            stepIndex = (stepIndex + 1) % stepSizes.length;
        }

        if (gamepad1.aWasPressed()) {
            coeffIndex = (coeffIndex + 1) % coeffs.length;
        }

        if (gamepad1.dpadUpWasPressed()) {
            coeffs[coeffIndex] += stepSizes[stepIndex];
            shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(coeffs[0], coeffs[1], coeffs[2], coeffs[3]));
        }

        if (gamepad1.dpadDownWasPressed()) {
            coeffs[coeffIndex] -= stepSizes[stepIndex];
            shooterMotor.setPIDFCoefficients(DcMotor.RunMode.RUN_USING_ENCODER, new PIDFCoefficients(coeffs[0], coeffs[1], coeffs[2], coeffs[3]));
        }

        telemetry.addData("P: ", coeffs[0]);
        telemetry.addData("I: ", coeffs[1]);
        telemetry.addData("D: ", coeffs[2]);
        telemetry.addData("F: ", coeffs[3]);

        if (gamepad1.y) {
            shooterMotor.setVelocity(RPM * RPMtoVelocity);
            if (shooterMotor.getVelocity()/RPMtoVelocity > maxRPM) {
                maxRPM = shooterMotor.getVelocity()/RPMtoVelocity;
            }
            if (shooterMotor.getVelocity()/RPMtoVelocity > RPM * 0.95) {
                runtimeStopped = true;
            }
        }

        if (gamepad1.yWasReleased()) {
            runtimeStopped = true;
        }

        if (gamepad1.yWasPressed()) {
            maxRPM = shooterMotor.getVelocity()/RPMtoVelocity;
            runtime.reset();
            runtimeStopped = false;
        }

        if (!runtimeStopped) {
            seconds = runtime.seconds();
        }

        telemetry.addData("currentRPM: ", shooterMotor.getVelocity()/RPMtoVelocity);
        telemetry.addData("maxRPM: ", maxRPM);
        telemetry.addData("spinupDuration: ", seconds);
    }
}
