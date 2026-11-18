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
import org.firstinspires.ftc.teamcode.util.WaitCommand;
import org.firstinspires.ftc.teamcode.util.globals.Alliance;
import org.firstinspires.ftc.teamcode.util.globals.Globals;
import org.firstinspires.ftc.teamcode.util.shooterIntake.AutonShooterIntake;

@Autonomous(name="Soot and Park (downward hive)")
public class ShootAndParkOpposite extends LinearOpMode {
    protected class Pathing {
        private final PoseFactory poseFactoryRed = PoseFactory.degrees();
        private final PoseFactory poseFactoryBlue = PoseFactory.degrees().rotateAround(72, 72, 180);
        private final Pose startRed = poseFactoryRed.of(60, 135, 90);
        private final Pose shootRed = poseFactoryRed.of(60, 111, 270);
        private final Pose parkRed = poseFactoryRed.of(11, 118, 270);
        private final Pose parkRedControl1 = poseFactoryRed.of(24, 132, 0);
        private final Pose startBlue = poseFactoryBlue.of(60, 135, 90);
        private final Pose shootBlue = poseFactoryBlue.of(60, 111, 270);
        private final Pose parkBlue = poseFactoryBlue.of(11, 118, 270);
        private final Pose parkBlueControl1 = poseFactoryBlue.of(24, 132, 0);

        public Path shootRed() {
            return Paths.line(startRed, shootRed).constant(shootRed);
        }

        public Path parkRed() {
            return Paths.curve(shootRed, parkRedControl1, parkRed).constant(parkRed);
        }

        public Path shootBlue() {
            return Paths.line(startBlue, shootBlue).constant(shootBlue);
        }

        public Path parkBlue() {
            return Paths.curve(shootBlue, parkBlueControl1, parkBlue).constant(parkBlue);
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
                    routine = Groups.sequential(new WaitCommand().begin(15000));
                    routine.then(Groups.parallel(PedroCommands.follow(Globals.follower, paths.shootRed()), shooterIntake.prepareShooting()));
                    routine.then(shooterIntake.shoot(3000));
                    routine.then(PedroCommands.follow(Globals.follower, paths.parkRed()));
                }
                else {
                    routine = Groups.sequential(new WaitCommand().begin(15000));
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
