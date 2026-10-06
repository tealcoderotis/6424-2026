package org.firstinspires.ftc.teamcode.Test;

import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;
import com.qualcomm.robotcore.hardware.DcMotor;

@TeleOp
public class ShooterTest extends LinearOpMode {
    @Override
    public void runOpMode() throws InterruptedException {
        DcMotor shooter = (DcMotor) hardwareMap.get(DcMotor.class, "shooter");
        waitForStart();
        while (opModeIsActive()) {
            shooter.setPower(-gamepad1.left_stick_y);
        }
    }
}
