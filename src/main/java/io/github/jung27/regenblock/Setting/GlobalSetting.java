package io.github.jung27.regenblock.Setting;

public class GlobalSetting {
    private static GlobalSetting instance;
    private boolean autoFill = false;
    private boolean autoPickup = false;
    private GlobalSetting() {
    }
    public boolean isAutoFill() {
        return autoFill;
    }
    public boolean isAutoPickup() {
        return autoPickup;
    }
    public void setAutoFill(boolean autoFill) {
        this.autoFill = autoFill;
    }
    public void setAutoPickup(boolean autoPickup) {
        this.autoPickup = autoPickup;
    }
    public static GlobalSetting getInstance() {
        if (instance == null) {
            instance = new GlobalSetting();
        }
        return instance;
    }
}
