package org.firstinspires.ftc.teamcode.util.globals;

import com.pedropathing.math.Pose;

public class Globals {
    private static Alliance alliance = Alliance.UNKNOWN;
    private static Pose pose = null;

    public static Alliance getAlliance() {
        return alliance;
    }

    public static void setAlliance(Alliance newAlliance) {
        alliance = newAlliance;
    }

    public static Pose getPose() {
        return new Pose(pose.x(), pose.y(), pose.heading());
    }

    public static void setPose(Pose newPose) {
        pose = new Pose(newPose.x(), newPose.y(), newPose.heading());
    }

    public static void resetGlobals() {
        alliance = Alliance.UNKNOWN;
        pose = null;
    }
}
