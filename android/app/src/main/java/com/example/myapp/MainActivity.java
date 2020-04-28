package com.example.myapp;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Build;

import java.util.Objects;

import io.flutter.embedding.android.FlutterActivity;
import io.flutter.embedding.engine.FlutterEngine;
import io.flutter.plugin.common.MethodCall;
import io.flutter.plugin.common.MethodChannel;
import io.flutter.plugins.GeneratedPluginRegistrant;

public class MainActivity extends FlutterActivity {
  @Override
  public void configureFlutterEngine(FlutterEngine flutterEngine) {
    GeneratedPluginRegistrant.registerWith(flutterEngine);

    new MethodChannel(Objects.requireNonNull(getFlutterEngine()).getDartExecutor().
            getBinaryMessenger(), "course.flutter.dev/battery").setMethodCallHandler(
            new MethodChannel.MethodCallHandler() {
              @Override
              public void onMethodCall(MethodCall methodCall, MethodChannel.Result result) {
                // we need to call .method after methodCall
                if(methodCall.method.equals("getBatteryLevel")){
                  int batteryLevel = getBatteryLevel();
                  if (batteryLevel != -1){
                      result.success(batteryLevel);
                  }else {
                    result.error("UNAVAILABLE","Could not fetch battery level.", null);
                  }
                }else {
                  result.notImplemented();
                }
              }
            }
    );
  }
  // here -1 = value not get
  private int getBatteryLevel(){
    int batteryLevel = -1;
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP){
      BatteryManager batteryManager = (BatteryManager) getSystemService(BATTERY_SERVICE);
      assert batteryManager != null;
      batteryLevel = batteryManager.getIntProperty(BatteryManager.BATTERY_PROPERTY_CAPACITY);
    }else {
      Intent intent = new ContextWrapper(getApplicationContext()).registerReceiver(null, new IntentFilter(Intent.ACTION_BATTERY_CHANGED));
      assert intent != null;
      batteryLevel = (intent.getIntExtra(BatteryManager.EXTRA_LEVEL, -1) * 100) / intent.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
    }
    return batteryLevel;
  }
}
