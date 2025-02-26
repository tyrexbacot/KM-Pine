package com.example.pinel;

import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.Objects;

import top.canyie.pine.Pine;
import top.canyie.pine.PineConfig;
import top.canyie.pine.callback.MethodHook;
import top.canyie.pine.callback.MethodReplacement;

public class ContextManager extends Application {
    public static boolean isElaina = false;
    static {
        try {
            LoadLibrary(Objects.requireNonNull(getContext()));
        } catch (Throwable ignored) {
        }
    }

    public static Context getContext() {
        try {
            @SuppressLint("PrivateApi") Class<?> cls = Class.forName("android.app.ActivityThread");
            Method declaredMethod = cls.getDeclaredMethod("currentActivityThread", new Class[0]);
            declaredMethod.setAccessible(true);
            Object invoke = declaredMethod.invoke(null, new Object[0]);
            @SuppressLint("DiscouragedPrivateApi") Field declaredField = cls.getDeclaredField("mBoundApplication");
            declaredField.setAccessible(true);
            Object obj = declaredField.get(invoke);
            assert obj != null;
            Field declaredField2 = obj.getClass().getDeclaredField("info");
            declaredField2.setAccessible(true);
            Object obj2 = declaredField2.get(obj);
            assert obj2 != null;
            @SuppressLint("PrivateApi") Method declaredMethod2 = Class.forName("android.app.ContextImpl").getDeclaredMethod("createAppContext", cls, obj2.getClass());
            declaredMethod2.setAccessible(true);
            Object invoke2 = declaredMethod2.invoke(null, invoke, obj2);
            if (invoke2 instanceof Context) {
                return (Context) invoke2;
            }
        } catch (ClassNotFoundException | IllegalAccessException | NoSuchFieldException | NoSuchMethodException | InvocationTargetException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static void setIsHook(boolean isElaina) {
        ContextManager.isElaina = isElaina;
    }

    public static boolean getIsHook() {
        return isElaina;
    }

    @SuppressLint("UnsafeDynamicallyLoadedCode")
    static void LoadLibrary(Context context) throws Exception {
        String appDataPath = context.getDataDir().getPath();
        assets.main(context, "Elaina", new ArrayList<>());
        assets.main(context, "App_dex", new ArrayList<>());
        String property = System.getProperty("os.arch");
        assert property != null;
        String archType;
        if (property.equals("arm64") || property.equals("aarch64")) {
            archType = "arm64";
        } else if (property.equals("arm") || property.contains("armeabi")) {
            archType = "arm";
        } else if (property.contains("x86_64")) {
            archType = "x86_64";
        } else {
            archType = "x86";
        }
        Log.d("Property", property);
        Log.d("archType", archType);

        switch (archType) {
            case "arm":
                System.load(appDataPath + "/app_libs/armeabi-v7a/libelaina.so");
                setIsHook(true);
                break;
            case "arm64":
                System.load(appDataPath + "/app_libs/arm64-v8a/libelaina.so");
                setIsHook(true);
                break;
        }

        Log.i("isElaina: ", String.valueOf(isElaina));

        if (getIsHook()) {
            PineConfig.debug = false;
            PineConfig.debuggable = false;
            try {
                // Define a target method
                Constructor<?> coba = Class.forName("com.kinemaster.module.network.communication.account.dto.SubscribeResponseDto").getDeclaredConstructor(boolean.class, String.class, boolean.class, long.class, long.class, String.class, String.class, String.class);
                Pine.hook(coba, new MethodHook() {
                    @Override
                    public void beforeCall(Pine.CallFrame callFrame) throws Throwable {
                        callFrame.args[0] = true;
                        callFrame.args[1] = "DarkWeb🗿";
                        callFrame.args[7] = "Lifetime";
                    //Context context = (Context) callFrame.thisObject;
                        //if (context != null) {
                    //Toast.makeText(context.getApplicationContext(), "Premium hooked", Toast.LENGTH_LONG).show();
                        //}        
                    }
                });
                
                Class<?> ini = Class.forName("com.kinemaster.app.screen.home.ui.main.HomeActivity");
                Method itu = ini.getDeclaredMethod("onCreate", Bundle.class);

                Pine.hook(itu, new MethodHook() {
                    @Override
                    public void afterCall(Pine.CallFrame callFrame) throws Throwable {
                        Context context = (Context) callFrame.thisObject;
                        Toast.makeText(context.getApplicationContext(), "Shirayuki Mods", Toast.LENGTH_LONG).show();
                }
            });
            } catch (Exception ignored) {

            }
        }
    }

}