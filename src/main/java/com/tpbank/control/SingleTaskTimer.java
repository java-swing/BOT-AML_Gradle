package tpbank.control;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

//simple Timer wrapper that run one task only.
public class SingleTaskTimer {
    private Timer timer = new Timer();
    private TimerTask task = null;

    public void setTask(TimerTask task) {
        this.task = task;
    }

    public void schedule(Date start, Date end, long period) {
        SingleTaskTimer self = this;
        timer = new Timer();
        if (task == null) {
            throw new IllegalStateException("Task not specified");
        } else {
            timer.schedule(task, start, period);
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    self.cancel();
                }
            }, end);
        }
    }

    public void cancel() {
        try {
            timer.cancel();
            timer.purge();
        } catch (IllegalStateException e) {
            //Timer already cancelled
        } finally {
            timer = new Timer();
        }
    }
}
