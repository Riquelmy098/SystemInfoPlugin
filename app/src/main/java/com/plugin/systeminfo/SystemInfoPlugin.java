package com.plugin.systeminfo;

import android.app.ActivityManager;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.BatteryManager;
import android.os.Environment;
import android.os.StatFs;
import org.godotengine.godot.Godot;
import org.godotengine.godot.plugin.GodotPlugin;
import org.godotengine.godot.plugin.UsedByGodot;
import java.io.File;

public class SystemInfoPlugin extends GodotPlugin {

    public SystemInfoPlugin(Godot godot) {
        super(godot);
    }

    @Override
    public String getPluginName() {
        return "SystemInfoPlugin";
    }

    // Este não precisa mudar, pois a bateria vai de 0 a 100 (cabe no int)
    @UsedByGodot
    public int getBatteryPercentage() {
        IntentFilter filter = new IntentFilter(Intent.ACTION_BATTERY_CHANGED);
        Intent battery = getActivity().registerReceiver(null, filter);
        if (battery != null) {
            int level = battery.getIntExtra(BatteryManager.EXTRA_LEVEL, -1);
            int scale = battery.getIntExtra(BatteryManager.EXTRA_SCALE, -1);
            return (int) ((level / (float) scale) * 100);
        }
        return -1;
    }

    // --- MÉTODOS ABAIXO ALTERADOS DE long PARA String ---

    @UsedByGodot
    public String getTotalRam() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return String.valueOf(info.totalMem);
    }

    @UsedByGodot
    public String getUsedRam() {
        ActivityManager manager = (ActivityManager) getActivity().getSystemService(Context.ACTIVITY_SERVICE);
        ActivityManager.MemoryInfo info = new ActivityManager.MemoryInfo();
        manager.getMemoryInfo(info);
        return String.valueOf(info.totalMem - info.availMem);
    }

    @UsedByGodot
    public String getTotalStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        return String.valueOf(stat.getTotalBytes());
    }

    @UsedByGodot
    public String getUsedStorage() {
        File path = Environment.getDataDirectory();
        StatFs stat = new StatFs(path.getPath());
        long free = stat.getAvailableBytes();
        long total = stat.getTotalBytes();
        return String.valueOf(total - free);
    }
}
