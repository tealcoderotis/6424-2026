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
import org.firstinspires.ftc.teamcode.util.globals.Alliance;
import org.firstinspires.ftc.teamcode.util.globals.Globals;
import org.firstinspires.ftc.teamcode.util.shooterIntake.AutonShooterIntake;

@Autonomous(name="Shoot and Park (upward hive)")
public class ShootAndPark extends LinearOpMode {
    protected class Pathing {
        private final Pose startRed = new Pose(60, 9, Math.toRadians(90));
        private final Pose point1Red = new Pose(60, 33, Math.toRadians(90));
        private final Pose path2Red = new Pose(11, 98, Math.toRadians(90));
        private final Pose path2Control1Red = new Pose(24, 18);
        private final Pose startBlue = startRed.rotate(Math.toRadians(180), true);
        private final Pose point1Blue = point1Red.rotate(Math.toRadians(180), true);
        private final Pose path2Blue = path2Red.rotate(Math.toRadians(180), true);
        private final Pose path2Control1Blue = path2Control1Red.rotate(Math.toRadians(180), true);

        public Path path1Red() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierLine(startRed, point1Red))
                    .setConstantHeadingInterpolation(point1Red.getHeading())
                    .build().firstPath();
        }

        public Path path2Red() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierCurve(point1Red,  path2Control1Red, path2Red))
                    .setConstantHeadingInterpolation(path2Red.getHeading())
                    .build().firstPath();
        }
        public Path path1Blue() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierLine(startBlue, point1Blue))
                    .setConstantHeadingInterpolation(point1Blue.getHeading())
                    .build().firstPath();
        }

        public Path path2Blue() {
            return Globals.follower.pathBuilder()
                    .addPath(new BezierCurve(point1Blue,  path2Control1Blue, path2Blue))
                    .setConstantHeadingInterpolation(path2Blue.getHeading())
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
                    routine = Groups.sequential(Groups.parallel(PedroCommands.follow(Globals.follower, paths.path1Red()), shooterIntake.prepareShooting()));
                    routine.then(shooterIntake.shoot(3000));
                    routine.then(PedroCommands.follow(Globals.follower, paths.path2Red()));
                }
                else {
                    routine = Groups.sequential(Groups.parallel(PedroCommands.follow(Globals.follower, paths.path1Blue()), shooterIntake.prepareShooting()));
                    routine.then(shooterIntake.shoot(3000));
                    routine.then(PedroCommands.follow(Globals.follower, paths.path2Blue()));
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
