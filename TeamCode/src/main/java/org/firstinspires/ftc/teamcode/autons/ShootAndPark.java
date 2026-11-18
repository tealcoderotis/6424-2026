package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.api.Paths;
import com.pedropathing.api.PoseFactory;
import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.Scheduler;
import com.pedropathing.ivy.groups.Groups;
import com.pedropathing.ivy.pedro.PedroCommands;
import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;
import com.qualcomm.robotcore.eventloop.opmode.Autonomous;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.teamcode.pedro.Constants;
import org.firstinspires.ftc.teamcode.util.globals.Alliance;
import org.firstinspires.ftc.teamcode.util.globals.Globals;
import org.firstinspires.ftc.teamcode.util.shooterIntake.AutonShooterIntake;

@Autonomous(name="Soot and Park (upward hive)")
public class ShootAndPark extends LinearOpMode {
    protected class Pathing {
        private final PoseFactory poseFactoryRed = PoseFactory.degrees();
        private final PoseFactory poseFactoryBlue = PoseFactory.degrees().rotateAround(72, 72, 180);
        private final Pose startRed = poseFactoryRed.of(60, 9, 90);
        private final Pose point1Red = poseFactoryRed.of(60, 33, 90);
        private final Pose path2Red = poseFactoryRed.of(11, 98, 90);
        private final Pose path2Control1Red = poseFactoryRed.of(24, 18, 0);
        private final Pose startBlue = poseFactoryBlue.of(60, 9, 90);
        private final Pose point1Blue = poseFactoryBlue.of(60, 33, 90);
        private final Pose path2Blue = poseFactoryBlue.of(11, 98, 90);
        private final Pose path2Control1Blue = poseFactoryBlue.of(24, 18, 0);

        public Path path1Red() {
            return Paths.line(startRed, point1Red).constant(point1Red);
        }

        public Path path2Red() {
            return Paths.curve(point1Red, path2Control1Red, path2Red).constant(path2Red);
        }
        public Path path1Blue() {
            return Paths.line(startBlue, point1Blue).constant(point1Blue);
        }

        public Path path2Blue() {
            return Paths.curve(point1Blue, path2Control1Blue, path2Blue).constant(path2Blue);
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
