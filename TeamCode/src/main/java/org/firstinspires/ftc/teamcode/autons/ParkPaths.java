package org.firstinspires.ftc.teamcode.autons;

import static com.pedropathing.api.Paths.curve;

import com.pedropathing.math.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.util.globals.Globals;

public class ParkPaths {
    public static Path parkRed() {
        return curve(new Pose(60, 8), new Pose(24, 24), new Pose(9, 208)).constant(90);
    }

    public static Path parkBlue() {
        return curve(Globals.invertPoseNoHeading(new Pose(60, 8)), Globals.invertPoseNoHeading(new Pose(24, 24)), Globals.invertPoseNoHeading(new Pose(9, 208))).constant(Globals.invertHeading(90));
    }
}
