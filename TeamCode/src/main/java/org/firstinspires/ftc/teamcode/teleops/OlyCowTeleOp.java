package org.firstinspires.ftc.teamcode.teleops;

import com.qualcomm.robotcore.eventloop.opmode.OpMode;
import com.qualcomm.robotcore.eventloop.opmode.TeleOp;

import org.firstinspires.ftc.teamcode.util.DriveFunctions;
//import org.firstinspires.ftc.teamcode.util.LimelightFunctions;
import org.firstinspires.ftc.teamcode.util.ManipulateFunctions;
import org.firstinspires.ftc.teamcode.util.globals.Alliance;
import org.firstinspires.ftc.teamcode.util.globals.Globals;

@TeleOp(name = "OlyCowTeleOp")
public class OlyCowTeleOp extends OpMode {
    DriveFunctions drive = new DriveFunctions();
    ManipulateFunctions manip = new ManipulateFunctions();
    //LimelightFunctions LL = new LimelightFunctions();
    double targetRPM = 820;

    @Override
    public void init() {
        drive.driveIMUInit(hardwareMap);
        manip.manipulateInit(hardwareMap);
        //LL.limelightInit(hardwareMap);

        telemetry.addData("Status", "Initialized");
    }

    @Override
    public void init_loop() {
        telemetry.addLine("These adjustments also work when running");
        telemetry.addLine("Press up and down to change launcher RPM");
        if (gamepad1.dpadDownWasPressed()) {
            targetRPM -= 20;
        }
        if (gamepad1.dpadUpWasPressed()) {
            targetRPM += 20;
        }
        telemetry.addData("goal RPM: ", targetRPM);
        telemetry.addLine("Press B to toggle fieldCentric");
        if (gamepad1.bWasPressed()) {
            drive.fieldCentric = !drive.fieldCentric;
        }
        telemetry.addData("fieldCentric: ", drive.fieldCentric);
        telemetry.addLine("Press A to choose alliance, favors red nectar if unknown alliance");
        if (gamepad1.aWasPressed()) {
            if (Globals.alliance == Alliance.RED) {
                Globals.alliance = Alliance.BLUE;
                Globals.invertPose();
            } else if (Globals.alliance == Alliance.BLUE){
                Globals.alliance = Alliance.RED;
                Globals.invertPose();
            } else {
                Globals.alliance = Alliance.RED;
                Globals.setPose(Globals.initPoseRedShooting);
            }
        }
        telemetry.addData("alliance", Globals.alliance.name());
    }

    @Override
    public void start() {
        //LL.limelightStart();
    }

    @Override
    public void loop() {
        telemetry.addLine("left bumper to intake, right bumper to rev up shooter, both to shoot");
        drive.Drive(-gamepad1.left_stick_y, gamepad1.left_stick_x, gamepad1.right_stick_x);
        /*LL.updatePieceMap(Globals.getPose());
        LL.alternateTracking();*/
        manip.intake(gamepad1.left_bumper, (gamepad1.left_trigger > 0));
        manip.shoot(gamepad1.right_bumper, (gamepad1.right_trigger > 0), targetRPM, telemetry);

        if (gamepad1.dpadDownWasPressed()) {
            targetRPM -= 20;
        }
        if (gamepad1.dpadUpWasPressed()) {
            targetRPM += 20;
        }
        /*Globals.writePose(telemetry);
        //Globals.follower.update();*/

        if (gamepad1.bWasPressed()) {
            drive.fieldCentric = !drive.fieldCentric;
        }
        telemetry.addData("fieldCentric: ", drive.fieldCentric);
    }
}
