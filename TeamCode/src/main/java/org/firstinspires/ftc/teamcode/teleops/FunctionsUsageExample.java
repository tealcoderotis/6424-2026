package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.DriveFunctions;
import org.firstinspires.ftc.teamcode.util.ManipulateFunctions;

@TeleOp(name = "FunctionsUsageExample")
//@Disabled
public class FunctionsUsageExample extends OpMode {
    DriveFunctions drive = new DriveFunctions();
    ManipulateFunctions manip = new ManipulateFunctions();
    double targetRPM = 1200;

    @Override
    public void init() {
        drive.driveIMUInit(hardwareMap);
        manip.manipulateInit(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        telemetry.addLine("These adjustments also work when running");
        telemetry.addLine("Press up and down to change launcher RPM");
        if (gamepad1.dpadDownWasPressed()) {
            targetRPM -= 20;
        }
        if (gamepad1.dpadUpWasPressed()) {
            targetRPM += 20;
        }
        telemetry.addData("goal RPM: ", targetRPM);
        if (gamepad1.bWasPressed()) {
            drive.fieldCentric = !drive.fieldCentric;
        }
        telemetry.addLine("Press B to toggle fieldCentric");
        telemetry.addData("fieldCentric: ", drive.fieldCentric);
    }

    @Override
    public void loop() {
        telemetry.addLine("left bumper to intake, right bumper to rev up shooter, both to shoot");

        drive.Drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);

        manip.intake(gamepad1.left_bumper, (gamepad1.left_trigger > 0));
        manip.shoot(gamepad1.right_bumper, (gamepad1.right_trigger > 0), targetRPM, telemetry);

        if (gamepad1.dpadDownWasPressed()) {
            targetRPM -= 20;
        }
        if (gamepad1.dpadUpWasPressed()) {
            targetRPM += 20;
        }

        if (gamepad1.bWasPressed()) {
            drive.fieldCentric = !drive.fieldCentric;
        }
        telemetry.addData("fieldCentric: ", drive.fieldCentric);
    }
}
