package com.example.databindingtest.util;

import java.util.TimerTask;

/**
 * @author hy
 * @Date 2019/10/23 0023
 **/
public class TimerWrapper {

    @SuppressWarnings("unused")
    interface Timer {
        void reset();

        void start(TimerTask task);

        long getElapsedTime();

        void updatePausedTime();

        long getPausedTime();

        void resetStartTime();

        void resetPauseTime();
    }

    public static class DefaultTimer implements Timer {

        private static final long TIMER_PERIOD_MS = 100L;  //周期时间  单位ms

        private long startTime = System.currentTimeMillis();
        private long pauseTime = 0L;

        private java.util.Timer timer = new java.util.Timer();

        @Override
        public void reset() {
            timer.cancel();
        }

        @Override
        public void start(TimerTask task) {
            timer = new java.util.Timer();
            timer.scheduleAtFixedRate(task, 0, TIMER_PERIOD_MS);
        }

        @Override
        public long getElapsedTime() {
            return System.currentTimeMillis() - startTime;
        }

        @Override
        public void updatePausedTime() {
            startTime += System.currentTimeMillis() - pauseTime;
        }

        @Override
        public long getPausedTime() {
            return pauseTime - startTime;
        }

        @Override
        public void resetStartTime() {
            startTime = System.currentTimeMillis();
        }

        @Override
        public void resetPauseTime() {
            pauseTime = System.currentTimeMillis();
        }
    }

}
