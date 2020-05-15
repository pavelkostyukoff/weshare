
package com.spacesofting.weshare.yandex;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class YandexMapsAddress {

    @SerializedName("type")
    @Expose
    private String type;
    @SerializedName("displayName")
    @Expose
    private String displayName;
    @SerializedName("value")
    @Expose
    private String value;
    private double latitude;
    private double longitude;

    //не всегда возвращается integer, бывает mp - уберем, потому что не используем
    @SerializedName("hl")
    @Expose
    private List<List<Integer>> hl = new ArrayList<List<Integer>>();

    /**
     * 
     * @return
     *     The type
     */
    public String getType() {
        return type;
    }

    /**
     * 
     * @param type
     *     The type
     */
    public void setType(String type) {
        this.type = type;
    }

    /**
     * 
     * @return
     *     The displayName
     */
    public String getDisplayName() {
        if (displayName != null)
            return displayName.trim();
        return displayName;
    }

    /**
     * 
     * @param displayName
     *     The displayName
     */
    public void setDisplayName(String displayName) {
        this.displayName = displayName;
    }

    /**
     * 
     * @return
     *     The value
     */
    public String getValue() {
        if (value != null)
            return value.trim();
        return value;
    }

    /**
     * 
     * @param value
     *     The value
     */
    public void setValue(String value) {
        this.value = value;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLongitude() {
        return longitude;
    }
}
