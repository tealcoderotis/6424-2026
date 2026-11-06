package org.firstinspires.ftc.teamcode.util;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

@TeleOp
public class StickDriftTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        telemetry.addLine("To continue start OpMode");
        telemetry.update();
        waitForStart();
        if (opModeIsActive()) {
            while (opModeIsActive()) {
                telemetry.addData("Gamepad 1 left x", gamepad1.left_stick_x);
                telemetry.addData("Gamepad 1 left y", gamepad1.left_stick_y);
                telemetry.addData("Gamepad 1 right x", gamepad1.right_stick_x);
                telemetry.addData("Gamepad 1 right y", gamepad1.right_stick_y);
                telemetry.addData("Gamepad 2 left x", gamepad2.left_stick_x);
                telemetry.addData("Gamepad 2 left y", gamepad2.left_stick_y);
                telemetry.addData("Gamepad 2 right x", gamepad2.right_stick_x);
                telemetry.addData("Gamepad 2 right y", gamepad2.right_stick_y);
                telemetry.update();
            }
        }
    }
}
