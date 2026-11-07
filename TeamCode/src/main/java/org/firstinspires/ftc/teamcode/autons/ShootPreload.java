package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.ivy.Scheduler;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.util.shooterIntake.AutonShooterIntake;

public class ShootPreload extends LinearOpMode {
    AutonShooterIntake shooterIntake;
    @Override
    public void runOpMode() throws InterruptedException {
        shooterIntake = new AutonShooterIntake(hardwareMap);
        Scheduler.reset();
        waitForStart();
        if (opModeIsActive()) {
            Scheduler.schedule(shooterIntake.shoot(3000));
            while (opModeIsActive()) {
                Scheduler.execute();
            }
        }
    }
}
