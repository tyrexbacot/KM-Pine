package com.example.pinel;

import android.app.AppComponentFactory;


public class Loader extends AppComponentFactory {
    static {
        try {
            new ContextManager();
        } catch (Throwable ignored) {
        }
    }
}
