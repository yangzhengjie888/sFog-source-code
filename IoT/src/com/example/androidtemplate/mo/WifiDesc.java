package com.example.androidtemplate.mo;

import android.os.Parcel;
import android.os.Parcelable;

/**
 *
 */
public class WifiDesc implements Parcelable {
    private String SSID;
    private String BSSID;
    private int frequency;
    private int level;
    private String date;

    public WifiDesc(String SSID, String BSSID, int frequency, int level, String date) {
        this.SSID = SSID;
        this.BSSID = BSSID;
        this.frequency = frequency;
        this.level = level;
        this.date = date;
    }

    public WifiDesc(String SSID, int level) {
        this.SSID = SSID;
        this.level = level;
    }

    public String getSSID() {
        return SSID;
    }

    public void setSSID(String SSID) {
        this.SSID = SSID;
    }

    public String getBSSID() {
        return BSSID;
    }

    public void setBSSID(String BSSID) {
        this.BSSID = BSSID;
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

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;

        WifiDesc wifiDesc = (WifiDesc) o;

        return SSID != null ? SSID.equals(wifiDesc.SSID) : wifiDesc.SSID == null;

    }

    @Override
    public int hashCode() {
        return SSID != null ? SSID.hashCode() : 0;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(this.SSID);
        dest.writeString(this.BSSID);
        dest.writeInt(this.frequency);
        dest.writeInt(this.level);
        dest.writeString(this.date);
    }

    protected WifiDesc(Parcel in) {
        this.SSID = in.readString();
        this.BSSID = in.readString();
        this.frequency = in.readInt();
        this.level = in.readInt();
        this.date = in.readString();
    }

    public static final Creator<WifiDesc> CREATOR = new Creator<WifiDesc>() {
        @Override
        public WifiDesc createFromParcel(Parcel source) {
            return new WifiDesc(source);
        }

        @Override
        public WifiDesc[] newArray(int size) {
            return new WifiDesc[size];
        }
    };
}
