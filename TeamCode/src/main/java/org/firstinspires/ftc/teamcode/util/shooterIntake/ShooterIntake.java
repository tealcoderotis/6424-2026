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
    private final double INDEXER_SHOOT_VELOCITY = 500;
    private final double INDEXER_INTAKE_VELOCITY = 500;
    private final double VELOCITY_TOLERANCE = 250;
    private boolean isPrepared = false;
    private DcMotorEx intakeMotor;
    private DcMotorEx shooterMotor;
    private int shootingTime = 0;
    private long startShootingTime;

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

    public Command shoot(int time) {
        Command command = Command.build()
                .setStart(new BeginShooting(time))
                .setExecute(() -> {})
                .setDone(new DoneShooting())
                .setEnd(new StopShooting())
                .requiring(this)
                .setInterruptedBehavior(InterruptedBehavior.END)
                .setBlockedBehavior(BlockedBehavior.QUEUE)
                .setConflictBehavior(ConflictBehavior.QUEUE)
                .setPriority(0);
        if (!isPrepared) {
            return Groups.sequential(prepareShooting(), command);
        }
        else {
            return command;
        }
    }

    private class BeginShooting implements Runnable {
        int tempShootingTime;
        public BeginShooting(int time) {
            tempShootingTime = time;
        }

        public void run() {
            shootingTime = tempShootingTime;
            startShootingTime = System.currentTimeMillis();
            intakeMotor.setVelocity(INDEXER_SHOOT_VELOCITY);
        }
    }

    private class DoneShooting implements BooleanSupplier {
        public boolean getAsBoolean() {
            return System.currentTimeMillis() >= startShootingTime + shootingTime;
        }
    }

    private class StopShooting implements Consumer<EndCondition> {
        public void accept(EndCondition endCondition) {
            isPrepared = false;
            shooterMotor.setVelocity(0);
            intakeMotor.setVelocity(0);
        }
    }

    public Command intake() {
        Command command = Command.build()
                .setExecute(new BeginIntaking())
                .setExecute(() -> {})
                .setDone(() -> false)
                .setEnd(new StopIntaking())
                .setInterruptedBehavior(InterruptedBehavior.END)
                .setBlockedBehavior(BlockedBehavior.QUEUE)
                .setConflictBehavior(ConflictBehavior.QUEUE)
                .setPriority(0);
        return command;
    }

    private class BeginIntaking implements Runnable {
        public void run() {
            intakeMotor.setVelocity(INDEXER_INTAKE_VELOCITY);
        }
    }

    private class StopIntaking implements Consumer<EndCondition> {
        public void accept(EndCondition endCondition) {
            intakeMotor.setVelocity(0);
        }
    }
}
