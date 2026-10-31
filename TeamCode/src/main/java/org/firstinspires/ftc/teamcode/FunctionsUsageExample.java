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
    }

    @Override
    public void start() {
    }

    @Override
    public void loop() {

        drive.mecanumFieldDrive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x, false);

        manip.manipulate(gamepad1.right_trigger, gamepad1.left_trigger, gamepad1.right_bumper);

        //telemetry.addData("Triggers", "left (%.2f, right (%.2f)",gamepad1.left_trigger, gamepad1.right_trigger);
    }

    @Override
    public void stop() {
    }
}
