package org.firstinspires.ftc.teamcode.until.vision;

import com.pedropathing.math.Pose;
import com.pedropathing.math.Vector2D;
import com.qualcomm.hardware.limelightvision.LLResultTypes;

import org.firstinspires.ftc.robotcore.external.navigation.Pose2D;

import java.io.NotActiveException;

import kotlin.NotImplementedError;

public class TargetPose {
    private static final double LIMELIGHT_HEIGHT = 0;
    private static final double LIMELIGHT_MOUNT_ANGLE = 0;
    private static final double PIECE_HEIGHT = 0;

    public static double GetGamePieceDistance(LLResultTypes.ColorResult target) {
        double angle = Math.toRadians(LIMELIGHT_MOUNT_ANGLE + target.getTargetYDegrees());
        double distance = (PIECE_HEIGHT - LIMELIGHT_HEIGHT) / Math.tan(angle);
        return distance;
    }

    public static Pose GetGamePiecePositionFieldSpace(Pose robotPose, LLResultTypes.ColorResult target) {
        double distance = GetGamePieceDistance(target);

        double horizontalAngle = Math.toRadians(target.getTargetXDegrees());
        double forwardDistance = Math.cos(horizontalAngle) * distance;
        double horizontalDistance = Math.sin(horizontalAngle) * distance;

        double xPose = robotPose.x() + (horizontalDistance * Math.cos(-robotPose.heading()) - forwardDistance * Math.sin(-robotPose.heading()));
        double yPose = robotPose.y() + (horizontalDistance * Math.sin(-robotPose.heading()) + forwardDistance * Math.cos(-robotPose.heading()));

        Pose piecePose = new Pose(xPose, yPose);

        return piecePose;
    }
}
