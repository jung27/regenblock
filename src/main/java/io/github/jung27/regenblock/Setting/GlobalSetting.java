package io.github.jung27.regenblock.Setting;

public class GlobalSetting {
    private static GlobalSetting instance;
    private boolean autoFill = false;
    private GlobalSetting() {
    }
    public boolean isAutoFill() {
        return autoFill;
    }
    public void setAutoFill(boolean autoFill) {
        this.autoFill = autoFill;
    }
    public static GlobalSetting getInstance() {
        if (instance == null) {
            instance = new GlobalSetting();
        }
        return instance;
    }
}
