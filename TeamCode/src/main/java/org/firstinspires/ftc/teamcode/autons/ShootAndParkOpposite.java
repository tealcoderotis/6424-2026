package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.BezierLine;
import com.pedropathing.geometry.Pose;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.ivy.pedro.PedroCommands;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedroPathing.Constants;
import org.firstinspires.ftc.teamcode.util.UtilCommands;
import org.firstinspires.ftc.teamcode.util.globals.Alliance;
import org.firstinspires.ftc.teamcode.util.globals.Globals;
import org.firstinspires.ftc.teamcode.util.shooterIntake.AutonShooterIntake;

@Autonomous(name="Shoot and Park (downward hive)")
public class ShootAndParkOpposite extends LinearOpMode {
    protected class Pathing {
        private final Pose startRed = new Pose(60, 135, Math.toRadians(90));
        private final Pose shootRed = new Pose(60, 111, Math.toRadians(270));
        private final Pose parkRed = new Pose(11, 118, Math.toRadians(270));
        private final Pose parkRedControl1 = new Pose(24, 132);
        private final Pose startBlue = startRed.rotate(Math.toRadians(180), true);
        private final Pose shootBlue = shootRed.rotate(Math.toRadians(180), true);
        private final Pose parkBlue = parkRed.rotate(Math.toRadians(180), true);
        private final Pose parkBlueControl1 = parkRedControl1.rotate(Math.toRadians(180), true);

        public Path shootRed() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierLine(startRed, shootRed))
                    .setConstantHeadingInterpolation(shootRed.getHeading())
                    .build().firstPath();
        }

        public Path parkRed() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierCurve(shootRed, parkRedControl1, parkRed))
                    .setConstantHeadingInterpolation(parkRed.getHeading())
                    .build().firstPath();
        }

        public Path shootBlue() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierLine(startBlue, shootBlue))
                    .setConstantHeadingInterpolation(shootBlue.getHeading())
                    .build().firstPath();
        }

        public Path parkBlue() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierCurve(shootBlue, parkBlueControl1, parkBlue))
                    .setConstantHeadingInterpolation(parkBlue.getHeading())
                    .build().firstPath();
        }

    }
    private Alliance alliance = Alliance.UNKNOWN;
    private Pathing paths = new Pathing();
    private AutonShooterIntake shooterIntake;
    @Override
    public void runOpMode() throws InterruptedException {
        Scheduler.reset();
        shooterIntake = new AutonShooterIntake(hardwareMap);
        Globals.follower = Constants.create(hardwareMap);

        while (opModeInInit()) {
            if (gamepad1.bWasPressed()) {
                alliance = Alliance.RED;
                Globals.follower.setPose(paths.startRed);
            }
            if (gamepad1.xWasPressed()) {
                alliance = Alliance.BLUE;
                Globals.follower.setPose(paths.startBlue);
            }
            telemetry.addData("Telemetry:", alliance.name());
            telemetry.update();
        }
        if (opModeIsActive()) {
            if (alliance != Alliance.UNKNOWN) {
                Command routine;
                if (alliance == Alliance.RED) {
                    routine = Groups.sequential(new UtilCommands().waitDuration(15000));
                    routine.then(Groups.parallel(PedroCommands.follow(Globals.follower, paths.shootRed()), shooterIntake.prepareShooting()));
                    routine.then(shooterIntake.shoot(3000));
                    routine.then(PedroCommands.follow(Globals.follower, paths.parkRed()));
                }
                else {
                    routine = Groups.sequential(new UtilCommands().waitDuration(15000));
                    routine.then(Groups.parallel(PedroCommands.follow(Globals.follower, paths.shootBlue()), shooterIntake.prepareShooting()));
                    routine.then(shooterIntake.shoot(3000));
                    routine.then(PedroCommands.follow(Globals.follower, paths.parkBlue()));
                }
                Scheduler.schedule(routine);
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
