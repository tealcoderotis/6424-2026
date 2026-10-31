package org.firstinspires.ftc.teamcode;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.DriveFunctions;
import org.firstinspires.ftc.teamcode.util.ManipulateFunctions;

@TeleOp(name = "FunctionsUsageExample")
//@Disabled
public class FunctionsUsageExample extends OpMode {
    DriveFunctions drive = new DriveFunctions();
    ManipulateFunctions manip = new ManipulateFunctions();
    boolean fieldCentric = false;

    @Override
    public void init() {
        manip.LAUNCHER_MIN_VELOCITY = 900;
        manip.LAUNCHER_TARGET_VELOCITY = 1000;
        drive.driveIMUInit(hardwareMap);
        manip.manipulateInit(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        telemetry.addLine("Press B to toggle fieldCentric");
        telemetry.addLine("Press up and down to change launcher velocity");
        if (gamepad1.bWasPressed()) {
            fieldCentric = !fieldCentric;
        }
        telemetry.addData("fieldCentric: ", fieldCentric);
        if (gamepad1.dpadDownWasPressed()) {
            manip.LAUNCHER_TARGET_VELOCITY -= 20;
            manip.LAUNCHER_MIN_VELOCITY = manip.LAUNCHER_TARGET_VELOCITY - 100;
        }
        if (gamepad1.dpadUpWasPressed()) {
            manip.LAUNCHER_TARGET_VELOCITY += 20;
            manip.LAUNCHER_MIN_VELOCITY = manip.LAUNCHER_TARGET_VELOCITY - 100;
        }
        telemetry.addData("Velocity: ", manip.LAUNCHER_TARGET_VELOCITY);
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        drive.mecanumFieldDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, fieldCentric);

        manip.manipulate(gamepad1.right_bumper, gamepad1.a, telemetry);

        if (gamepad1.bWasPressed()) {
            fieldCentric = !fieldCentric;
        }
        telemetry.addData("fieldCentric: ", fieldCentric);
        if (gamepad1.dpadDownWasPressed()) {
            manip.LAUNCHER_TARGET_VELOCITY -= 20;
            manip.LAUNCHER_MIN_VELOCITY = manip.LAUNCHER_TARGET_VELOCITY - 100;
        }
        if (gamepad1.dpadUpWasPressed()) {
            manip.LAUNCHER_TARGET_VELOCITY += 20;
            manip.LAUNCHER_MIN_VELOCITY = manip.LAUNCHER_TARGET_VELOCITY - 100;
        }
        telemetry.addData("Goal velocity: ", manip.LAUNCHER_TARGET_VELOCITY);
    }

    @Override
    public void stop() {
    }
}
