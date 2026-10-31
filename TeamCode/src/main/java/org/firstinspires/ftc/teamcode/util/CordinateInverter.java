package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.math.Pose;

public class CordinateInverter {
    public static Pose invert(Pose pose) {
        double heading = pose.heading() + Math.PI;
        heading %= 2*Math.PI;
        return new Pose(144 - pose.x(), 144 - pose.y(), heading);
    }
}
