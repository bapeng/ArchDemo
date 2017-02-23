package com.peng.htmlview;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

import java.util.Timer;
import java.util.TimerTask;

//UI线程任务队列
public class UITasker {

    public static UITasker newInstance() {
        return new UITasker();
    }

    private Timer timer = new Timer();

    private TimerTask timerTask;
    private TimerTask timerRunable;

    private Runnable runnable;
    private Task task;
    private int count;
    private int maxCount = Integer.MAX_VALUE;

    private UITasker() {
    }

    public interface Task {
        public void run(int count);
    }

    private Handler post = new Handler(Looper.getMainLooper());

    private Handler schedule = new Handler(Looper.getMainLooper()) {
        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 1000) {
                if (task != null) {
                    task.run(msg.arg1);
                }
            } else if (msg.what == 2000) {
                if (runnable != null) {
                    runnable.run();
                }
            }
        }
    };

    public void post(Runnable runnable) {
        post.post(runnable);
    }

    public void post(Runnable runnable, long delay) {
        post.postDelayed(runnable, delay);
    }

    public void scheduleTask(Task task, long delay, long period) {
        this.task = task;
        this.count = 0;
        this.maxCount = Integer.MAX_VALUE;
        if (timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count >= 0 && count <= maxCount) {
                    Message msg = Message.obtain(schedule);
                    msg.what = 1000;
                    msg.arg1 = count++;
                    schedule.sendMessage(msg);
                } else {
                    cancel();
                    timer.purge();
                }
            }
        };
        timer.schedule(timerTask, delay, period);
    }

    public void scheduleTask(Task task, long delay, long period, int max) {
        this.task = task;
        this.count = 0;
        this.maxCount = max;
        if (timerTask != null) {
            timerTask.cancel();
            timer.purge();
        }
        timerTask = new TimerTask() {
            @Override
            public void run() {
                if (count >= 0 && count <= maxCount) {
                    Message msg = Message.obtain(schedule);
                    msg.what = 1000;
                    msg.arg1 = count++;
                    schedule.sendMessage(msg);
                } else {
                    cancel();
                    timer.purge();
                }
            }
        };
        timer.schedule(timerTask, delay, period);
    }

    public void cancleTask() {
        if (timerTask != null) {
            timerTask.cancel();
        }
        timer.purge();
        schedule.removeMessages(1000);
    }

    public void scheduleRunnable(Runnable runnable, long delay, long period) {
        this.runnable = runnable;
        this.count = 0;
        if (timerRunable != null) {
            timerRunable.cancel();
            timer.purge();
        }
        timerRunable = new TimerTask() {
            @Override
            public void run() {
                schedule.sendEmptyMessage(2000);
            }
        };
        timer.schedule(timerRunable, delay, period);
    }

    public void cancleRunnable() {
        if (timerRunable != null) {
            timerRunable.cancel();
        }
        timer.purge();
        schedule.removeMessages(2000);
    }

    public void cancle() {
        if (timer != null) {
            timer.cancel();
        }
    }

}
