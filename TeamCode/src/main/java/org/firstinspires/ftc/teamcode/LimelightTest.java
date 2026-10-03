package org.firstinspires.ftc.teamcode;

import com.pedropathing.math.Pose;
import com.qualcomm.hardware.limelightvision.LLResult;
import com.qualcomm.hardware.limelightvision.LLResultTypes;
import com.qualcomm.hardware.limelightvision.Limelight3A;
import com.qualcomm.robotcore.eventloop.opmode.LinearOpMode;

import org.firstinspires.ftc.robotcore.external.navigation.Pose3D;
import org.firstinspires.ftc.teamcode.until.vision.TargetPose;

import java.util.List;

public class LimelightTest extends LinearOpMode {
    Limelight3A limelight;

    @Override
    public void runOpMode() throws InterruptedException {
        limelight = hardwareMap.get(Limelight3A.class, "limelight");
        waitForStart();
        limelight.pipelineSwitch(1);
        limelight.start();
        while (opModeIsActive()) {
            LLResult limelightResult = limelight.getLatestResult();
            if (limelightResult.isValid()) {
                telemetry.clearAll();
                List<LLResultTypes.ColorResult> colorResults = limelightResult.getColorResults();
                for (LLResultTypes.ColorResult result : colorResults) {
                    Pose3D targetPose = result.getRobotPoseFieldSpace();
                    telemetry.addLine("Target " + colorResults.indexOf(result));
                    telemetry.addLine("Target\tx: " + targetPose.getPosition().x + "\ty: " + targetPose.getPosition().y);
                    telemetry.addLine("Distance:" + TargetPose.GetGamePieceDistance(result));
                    telemetry.addLine("Pose: " + TargetPose.GetGamePiecePositionFieldSpace(new Pose(0, 0), result));
                    telemetry.addLine();
                }
                telemetry.update();
                sleep(100);
            }
        }
    }
}
