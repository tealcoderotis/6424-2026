package org.firstinspires.ftc.teamcode.util.shooterIntake;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;
import com.pedropathing.ivy.groups.Groups;
import com.qualcomm.robotcore.hardware.DcMotorEx;
import com.qualcomm.robotcore.hardware.HardwareMap;

import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

public class ShooterIntake {
    private final String INTAKE_HARDWARE_MAP = "intake";
    private final String SHOOTER_HARDWARE_MAP = "shooter";
    private final double SHOOTER_SHOOT_VELOCITY = 5000;
    private final double VELOCITY_TOLERANCE = 250;
    private boolean isPrepared = false;
    private DcMotorEx intakeMotor;
    private DcMotorEx shooterMotor;

    public ShooterIntake(HardwareMap hardwareMap) {
        intakeMotor = hardwareMap.get(DcMotorEx.class, INTAKE_HARDWARE_MAP);
        shooterMotor = hardwareMap.get(DcMotorEx.class, SHOOTER_HARDWARE_MAP);
    }

    public Command prepareShooting() {
        Command command = Command.build()
                .setStart(new PrepareShooting())
                .setExecute(() -> {})
                .setDone(new DonePrepareShooting())
                .setEnd(new StopPrepareShooting())
                .requiring(this)
                .setInterruptedBehavior(InterruptedBehavior.END)
                .setBlockedBehavior(BlockedBehavior.QUEUE)
                .setConflictBehavior(ConflictBehavior.QUEUE)
                .setPriority(0);
        return command;
    }

    private class PrepareShooting implements Runnable {
        public void run() {
            shooterMotor.setVelocity(SHOOTER_SHOOT_VELOCITY);
        }
    }

    private class DonePrepareShooting implements BooleanSupplier {
        public boolean getAsBoolean() {
            return Math.abs(shooterMotor.getVelocity() - SHOOTER_SHOOT_VELOCITY) <= VELOCITY_TOLERANCE;
        }
    }

    private class StopPrepareShooting implements Consumer<EndCondition> {
        public void accept(EndCondition endCondition) {
            if (endCondition == EndCondition.INTERRUPTED) {
                shooterMotor.setVelocity(0);
            }
            else if (endCondition == EndCondition.NATURALLY) {
                isPrepared = true;
            }
        }
    }

    public Command shoot() {
        Command command = Command.build();
        if (!isPrepared) {
            return Groups.sequential(command, prepareShooting());
        }
        else {
            return command;
        }
    }
}
