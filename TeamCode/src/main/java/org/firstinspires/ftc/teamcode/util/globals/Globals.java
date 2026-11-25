package org.firstinspires.ftc.teamcode.util.globals;

import com.pedropathing.follower.Follower;
import com.pedropathing.geometry.Pose;

public class Globals {
    public static Follower follower;
    public static Alliance alliance = Alliance.UNKNOWN;
    public static final Pose initPoseRedShooting = new Pose(56, 7.2, 90);

    public static Pose getPose() {
        return new Pose(follower.getPose().getX(), follower.getPose().getY(), follower.getPose().getHeading());
    }

    public static void setPose(Pose newPose) {
        follower.setPose(new Pose(newPose.getX(), newPose.getY(), newPose.getHeading()));
    }

    public static void writePose(org.firstinspires.ftc.robotcore.external.Telemetry telemetry) {
        telemetry.addData("x", Globals.follower.getPose().getX());
        telemetry.addData("y", Globals.follower.getPose().getY());
        telemetry.addData("heading", Globals.follower.getPose().getHeading());
        telemetry.update();
    }
    public static void invertPose() {
        double heading = follower.getPose().getHeading() + Math.PI;
        heading %= 2 * Math.PI;
        follower.setPose(new Pose(144 - follower.getPose().getX(), 144 - follower.getPose().getY(), heading));
    }

    public static Pose invertPoseNoHeading(Pose oldPose) {
        return new Pose(144 - oldPose.getX(), 144 - oldPose.getY());
    }

    public static double invertHeading(double oldHeading) {
        double heading = oldHeading + Math.PI;
        heading %= 2 * Math.PI;
        return heading;
    }
}
