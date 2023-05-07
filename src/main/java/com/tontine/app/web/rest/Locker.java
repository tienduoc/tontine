package com.tontine.app.web.rest;

public class Locker {

    private static boolean lock;

    public static boolean isLock() {
        return lock;
    }

    public static void setLock(boolean lock) {
        Locker.lock = lock;
    }
}
