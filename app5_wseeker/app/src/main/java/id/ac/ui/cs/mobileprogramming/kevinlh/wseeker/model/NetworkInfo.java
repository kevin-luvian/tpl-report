package id.ac.ui.cs.mobileprogramming.kevinlh.wseeker.model;

import com.google.gson.annotations.SerializedName;

public class NetworkInfo {
    public static final String STRING_UNDEFINED = "undefined";
    public static final int LEVEL_UNDEFINED = 0;
    public static final int LEVEL_LOW = 1;
    public static final int LEVEL_MEDIUM = 2;
    public static final int LEVEL_HIGH = 3;
    public static final int FROM_LOCAL = 1;
    public static final int FROM_API = 2;
    static final int[][] frequencies = {
            {2401, 2412, 2423}, {2404, 2417, 2428}, {2411, 2422, 2433}, {2416, 2427, 2438},
            {2421, 2432, 2443}, {2426, 2437, 2448}, {2431, 2442, 2453}, {2436, 2447, 2458},
            {2441, 2452, 2463}, {2451, 2457, 2468}, {2451, 2462, 2473}, {2456, 2467, 2478},
            {2461, 2472, 2483}, {2473, 2484, 2495}
    };
    @SerializedName("_id")
    String id;
    int scanLocation;
    String mac;
    String ssid;
    int frequency;
    int level;
    String capabilities;

    public NetworkInfo() {
        id = "";
        scanLocation = FROM_LOCAL;
        mac = "";
        ssid = "";
        frequency = 0;
        level = LEVEL_UNDEFINED;
        capabilities = "";
    }

    public static boolean isEmptyString(String string) {
        return string == null || string.isEmpty();
    }

    public static int convertSignalLevel(int level) {
        if (level >= -60) return LEVEL_HIGH;
        else if (level <= -60 && level >= -80) return LEVEL_MEDIUM;
        else if (level >= -95) return LEVEL_LOW;
        else return LEVEL_UNDEFINED;
    }

    public int getChannel() {
        for (int i = 0; i < frequencies.length - 1; i++) {
            for (int j = 0; j < frequencies[i].length - 1; j++) {
                if (frequency == frequencies[i][j]) return i;
            }
        }
        return 0;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public int getScanLocation() {
        return scanLocation;
    }

    public void setScanLocation(int scanLocation) {
        this.scanLocation = scanLocation;
    }

    public String getMac() {
        return mac;
    }

    public void setMac(String mac) {
        if (isEmptyString(mac)) {
            this.mac = STRING_UNDEFINED;
        } else {
            this.mac = mac;
        }
    }

    public String getSsid() {
        return ssid;
    }

    public void setSsid(String ssid) {
        if (isEmptyString(ssid)) ssid = STRING_UNDEFINED;
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
        this.level = convertSignalLevel(level);
    }

    public String getCapabilities() {
        return capabilities;
    }

    public void setCapabilities(String capabilities) {
        if (isEmptyString(capabilities)) capabilities = STRING_UNDEFINED;
        this.capabilities = capabilities;
    }

    public static int compareNetworkInfo(NetworkInfo o1, NetworkInfo o2) {
        int levelDifference = o2.getLevel() - o1.getLevel();
        if (levelDifference != 0) return levelDifference;
        return o1.getSsid().compareTo(o2.getSsid());
    }

    @Override
    public String toString() {
        return "NetworkInfo{" +
                "id='" + id + '\'' +
                ", mac='" + mac + '\'' +
                ", ssid='" + ssid + '\'' +
                ", frequency=" + frequency +
                ", level=" + level +
                ", capabilities='" + capabilities + '\'' +
                '}';
    }
}
