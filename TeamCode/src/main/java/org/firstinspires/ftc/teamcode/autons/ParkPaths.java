package org.firstinspires.ftc.teamcode.autons;

import com.pedropathing.geometry.BezierCurve;
import com.pedropathing.geometry.Pose;
import com.pedropathing.paths.Path;

import org.firstinspires.ftc.teamcode.util.globals.Globals;

public class ParkPaths {
    public static Path parkRed() {
        return Globals.follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(60, 8), new Pose(24, 24), new Pose(9, 208)))
                .setConstantHeadingInterpolation(Math.toRadians(90))
                .build().firstPath();
    }

    public static Path parkBlue() {
        return Globals.follower.pathBuilder()
                .addPath(new BezierCurve(new Pose(60, 8).rotate(Math.toRadians(180), true), new Pose(24, 24).rotate(Math.toRadians(180), true), new Pose(9, 208).rotate(Math.toRadians(180), true)))
                .setConstantHeadingInterpolation(Math.toRadians(270))
                .build().firstPath();
    }
}
