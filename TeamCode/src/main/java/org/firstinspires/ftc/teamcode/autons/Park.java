package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.pedro.PedroCommands;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.globals.Alliance;
import org.firstinspires.ftc.teamcode.util.globals.Globals;

@Autonomous
public class Park extends LinearOpMode {
    private Alliance alliance = Alliance.UNKNOWN;
    @Override
    public void runOpMode() throws InterruptedException {
        Scheduler.reset();
        Globals.follower = Constants.create(hardwareMap);

        while (opModeInInit()) {
            if (gamepad1.bWasPressed()) {
                alliance = Alliance.RED;
                Globals.follower.setPose(ParkPaths.parkRed().getPoint(0));
            }
            if (gamepad1.xWasPressed()) {
                alliance = Alliance.BLUE;
                Globals.follower.setPose(ParkPaths.parkBlue().getPoint(0));
            }
            telemetry.addData("Telemetry:", alliance.name());
            telemetry.update();
        }
        if (opModeIsActive()) {
            if (alliance != Alliance.UNKNOWN) {
                if (alliance == Alliance.RED) {
                    Scheduler.schedule(PedroCommands.follow(Globals.follower, ParkPaths.parkRed()));
                }
                else {
                    Scheduler.schedule(PedroCommands.follow(Globals.follower, ParkPaths.parkBlue()));
                }
                while (opModeIsActive()) {
                    Globals.follower.update();
                    Scheduler.execute();
                    Globals.writePose(telemetry);
                }
                Globals.alliance = this.alliance;
            }
            else {
                throw new InterruptedException("Please specify an alliance");
            }
        }
    }
}
