package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model;

import com.google.gson.annotations.SerializedName;

public class NetworkInfoPost {
    private String mac;
    private String ssid;
    private int frequency;
    private int level;
    private String capabilities;

    public NetworkInfoPost(NetworkInfo info) {
        mac = info.getMac();
        ssid = info.getSsid();
        frequency = info.getFrequency();
        level = info.getLevel();
        capabilities = info.getCapabilities();
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        this.mac = mac;
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        this.ssid = ssid;
    }

    public int getFrequency() {
        return frequency;
    }

    public void setFrequency(int frequency) {
        this.frequency = frequency;
    }

    public int getLevel() {
        return level;
    }

    public void setLevel(int level) {
        this.level = level;
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        this.capabilities = capabilities;
    }

    @Override
    public String toString() {
        return "NetworkInfoPost{" +
                "mac='" + mac + '\'' +
                ", ssid='" + ssid + '\'' +
                ", frequency=" + frequency +
                ", level=" + level +
                ", capabilities='" + capabilities + '\'' +
                '}';
    }
}
