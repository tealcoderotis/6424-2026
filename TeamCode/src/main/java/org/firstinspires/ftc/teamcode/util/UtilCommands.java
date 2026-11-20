package org.firstinspires.ftc.teamcode.util;

import com.pedropathing.ivy.Command;
import com.pedropathing.ivy.behaviors.BlockedBehavior;
import com.pedropathing.ivy.behaviors.ConflictBehavior;
import com.pedropathing.ivy.behaviors.EndCondition;
import com.pedropathing.ivy.behaviors.InterruptedBehavior;

import org.firstinspires.ftc.teamcode.util.globals.Globals;

public class UtilCommands {
    private long waitTime;
    private long startTime;
    public UtilCommands() {

    }
    public Command waitDuration(long time) {
        Command command = Command.build()
                .setStart(() -> {
                    startTime = System.currentTimeMillis();
                    waitTime = time;
                })
                .setExecute(() -> {})
                .setDone(() -> System.currentTimeMillis() >= startTime + waitTime)
                .setEnd((EndCondition condition) -> {})
                .requiring(this)
                .setInterruptedBehavior(InterruptedBehavior.END)
                .setBlockedBehavior(BlockedBehavior.CANCEL)
                .setConflictBehavior(ConflictBehavior.CANCEL)
                .setPriority(0);
        return command;
    }
}
