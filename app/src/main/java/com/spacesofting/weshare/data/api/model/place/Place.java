package com.spacesofting.weshare.data.api.model.place;

import android.location.Location;
import android.os.Parcel;
import android.os.Parcelable;
import android.text.TextUtils;

import java.util.HashMap;
import java.util.Iterator;


public class Place implements Parcelable {


    public boolean isAirport() {
        return isAirport;
    }

    public void setIsAirport(boolean isAirport) {
        this.isAirport = isAirport;
    }

    public enum PlaceParam {
        Near,
        Love,
        Metro,
        Airport,
        Train,
        PlaceParamEnumSize;

        public static PlaceParam valueOf(int value) {
            switch (value) {
                case 0:
                    return Near;
                case 1:
                    return Love;
                case 2:
                    return Metro;
                case 3:
                    return Airport;
                case 4:
                    return Train;
            }
            return PlaceParamEnumSize;
        }
    }

    ;

    private String address;
    private String country;
    private Location location;
    private String city;
    private String house;
    private String street;

    private String comment;

    private boolean isAirport;


    public Place(HashMap<String, String> params) throws NumberFormatException {
        Iterator<String> it = params.keySet().iterator();
        String k, v;
        while (it.hasNext()) {

            k = it.next();
            v = params.get(k);

            if ("latitude".equals(k)) {
                if (location == null) {
                    location = new Location("loc");
                }
                location.setLatitude(Double.valueOf(v));
            } else if ("longitude".equals(k)) {
                if (location == null) {
                    location = new Location("loc");
                }
                location.setLongitude(Double.valueOf(v));
            } else if ("title".equals(k)) {
                setAddress(v);
            }
        }
        isAirport = false;
    }

    public Place() {
        Location loc = new Location("loc");
        setLocation(loc);
        isAirport = false;
    }

    public Place(String address, double lat, double lon) {
        setAddress(address);
        Location loc = new Location("loc");
        loc.setLatitude(lat);
        loc.setLongitude(lon);
        setLocation(loc);
        isAirport = false;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(address) && location != null && location.getLatitude() != 0 && location.getLongitude() != 0;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getComment() {
        return comment;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getHouse() {
        return house;
    }

    public void setHouse(String house) {
        this.house = house;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }


    private String fullAddress;

    public String getFullAddress() {
        return fullAddress;
    }

    public void setFullAddress(String fullAddress) {
        this.fullAddress = fullAddress;
    }


    protected Place(Parcel in) {
        address = in.readString();
        country = in.readString();
        location = in.readParcelable(Location.class.getClassLoader());
        comment = in.readString();
        fullAddress = in.readString();
        isAirport = in.readInt() == 0 ? false : true;
        city = in.readString();
        street = in.readString();
        house = in.readString();
    }

    public static final Creator<Place> CREATOR = new Creator<Place>() {
        @Override
        public Place createFromParcel(Parcel in) {
            return new Place(in);
        }

        @Override
        public Place[] newArray(int size) {
            return new Place[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(address);
        dest.writeString(country);
        dest.writeParcelable(location, flags);
        dest.writeString(comment);
        dest.writeString(fullAddress);
        dest.writeInt(isAirport ? 1 : 0);
        dest.writeString(city);
        dest.writeString(street);
        dest.writeString(house);
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null)
            return false;
        if (getClass() != obj.getClass())//if (!(obj instanceof Place))
            return false;
        Place other = (Place) obj;
        if (address != null && other.address != null && !address.equalsIgnoreCase(other.address))
            return false;
        if (country != null && other.country != null && !country.equalsIgnoreCase(other.country))
            return false;
        if (location != null && other.location != null && location.getLatitude() != other.location.getLatitude()
                && location.getLongitude() != other.location.getLongitude())
            return false;
        if (comment != null && other.comment != null && !comment.equalsIgnoreCase(other.comment))
            return false;
        if (fullAddress != null && other.fullAddress != null && !fullAddress.equalsIgnoreCase(other.fullAddress))
            return false;
        if (isAirport != other.isAirport)
            return false;
        return true;
    }
}
