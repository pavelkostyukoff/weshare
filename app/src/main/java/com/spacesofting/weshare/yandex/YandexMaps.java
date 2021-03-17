package com.spacesofting.weshare.yandex;

import android.location.Location;
import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.spacesofting.weshare.api.model.place.Place;
import com.spacesofting.weshare.mvp.model.dto.Component;
import com.spacesofting.weshare.mvp.model.dto.Point;
import com.spacesofting.weshare.mvp.model.dto.ResponseMaps;
import com.spacesofting.weshare.utils.Consts;
import com.spacesofting.weshare.utils.LogUtil;

import java.util.ArrayList;
import java.util.Locale;
import java.util.Objects;

import cz.msebera.android.httpclient.Header;


public class YandexMaps {

    private static final String FORMAT = "format";
    private static final String JSON = "json";

    private static final String APIKEY = "apikey";
    private static final String API_KEY = "bb3c117d-ae78-44c6-a677-5a74f6b85bb3";

    private static final String KIND = "kind";
    private static final String HOUSE = "house";

    private static final String GEOCODE = "geocode";
    private static final String LL = "ll";

    // адрес должен формироваться где-то здесь
    public void getAddressBy(final String name, final Location location, final YandexMapsListener listener) {
        RequestParams params = new RequestParams();
        params.put(KIND, HOUSE);
        params.put(FORMAT, JSON);

        if (!TextUtils.isEmpty(name)) {
            params.put(GEOCODE, name);
            params.put(APIKEY, API_KEY);

            if (location != null && location.getLongitude() != 0.0) {
                params.put(LL, String.format(Locale.ENGLISH, "%f,%f", location.getLongitude(), location.getLatitude()));
            }
        } else {
            params.put(GEOCODE, String.format(Locale.ENGLISH, "%f,%f", location.getLongitude(), location.getLatitude()));
        }

        YandexMapsClient.get("/", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure(statusCode);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                new Thread(() -> {
                    //XXX: http://www.jsonschema2pojo.org/ делает из json классы
                    //XXX: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
                    //  LogUtil.Log(responseString);
                    Place place = null;
                    try {
                        place = new Place();
                        Gson gson = new Gson();
                        ResponseMaps maps = new ResponseMaps();
                        ResponseMaps data = gson.fromJson(responseString, ResponseMaps.class);
                        ArrayList<Component> components = (ArrayList<Component>) data.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getMetaDataProperty().getGeocoderMetaData().getAddress().getComponents();
                        Point point = data.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getPoint();
                        String address = data.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getMetaDataProperty().getGeocoderMetaData().getAddressDetails().getCountry().getAddressLine();
                        String address2 = data.getResponse().getGeoObjectCollection().getFeatureMember().get(0).getGeoObject().getMetaDataProperty().getGeocoderMetaData().getAddress().getFormatted();
                        if(address != null) {
                            place.setAddress(address2);
                        }
                        if(components != null) {
                            for(int i = 0; i < components.size(); i++) {
                                if(components.get(i).getKind().equals("house")){
                                    place.setHouse(components.get(i).getName());
                                }
                                else if (components.get(i).getKind().equals("street")){
                                    place.setStreet(components.get(i).getName());
                                }
                                else if (components.get(i).getKind().equals("locality")){
                                    place.setCity(components.get(i).getName());
                                }
                                else if (components.get(i).getKind().equals("country")){
                                    place.setCountry(components.get(i).getName());
                                }
                                System.out.println(i);
                            }
                        }
                        assert point != null;
                        if (Objects.requireNonNull(point.getPos()).length() > 1) {
                            String strArr[] = point.getPos().split(" ");
                            double numArr[] = new double[strArr.length];
                            for (int i = 0; i < strArr.length; i++) {
                                numArr[i] = Double.parseDouble(strArr[i]);
                                // System.out.println(numArr[i]);
                            }


                            Location loc = new Location("loc");
                            loc.setLongitude(Double.parseDouble(strArr[0]));
                            loc.setLatitude(Double.parseDouble(strArr[1]));
                            place.setLocation(loc);


                            //37.407079 55.653853
                        }
                       /* System.err.println("YMSearch1 " + responseString);
                        JsonElement jsonElement = fromString(responseString, "response.GeoObjectCollection.featureMember");
                       // Делаем нормальный парсинг
                        if (jsonElement instanceof JsonArray) {
                            JsonArray featureMembers = (JsonArray) jsonElement;
                            if (featureMembers.size() > 0) {
                                JsonElement featureMember = featureMembers.get(0);
                        *//* todo       {"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"exact","text":"Россия, Москва, улица Главмосстроя, 5","kind":"house","Address":{"country_code":"RU","formatted":"Россия, Москва, улица Главмосстроя, 5",
                                        "postal_code":"119618","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Центральный федеральный округ"},{"kind":"province","name":"Москва"},{"kind":"locality","name":"Москва"},
                                    {"kind":"street","name":"улица Главмосстроя"},{"kind":"house","name":"5"}]},"AddressDetails":{"Country":{"AddressLine":"Россия, Москва, улица Главмосстроя, 5","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":
                                    {"AdministrativeAreaName":"Москва","Locality":{"LocalityName":"Москва","Thoroughfare":{"ThoroughfareName":"улица Главмосстроя","Premise":{"PremiseNumber":"5","PostalCode":{"PostalCodeNumber":"119618"}}}}}}}}},
                                    "name":"улица Главмосстроя, 5","description":"Москва, Россия","boundedBy":{"Envelope":{"lowerCorner":"37.402884 55.65051","upperCorner":"37.411095 55.655153"}},"Point":{"pos":"37.406989 55.652832"}}}*//*

                        *//* todo       {"GeoObject":{"metaDataProperty":{"GeocoderMetaData":{"precision":"exact","text":"Россия, Ростовская область, Новочеркасск, Будённовская улица, 141","kind":"house","Address":{"country_code":"RU","formatted":"Россия, Ростовская область," +
                                        " Новочеркасск, Будённовская улица, 141","postal_code":"346411","Components":[{"kind":"country","name":"Россия"},{"kind":"province","name":"Южный федеральный округ"},{"kind":"province","name":"Ростовская область"},
                                    {"kind":"area","name":"городской округ Новочеркасск"},{"kind":"locality","name":"Новочеркасск"},{"kind":"street","name":"Будённовская улица"},{"kind":"house","name":"141"}]},"AddressDetails":{"Country":
                                    {"AddressLine":"Россия, Ростовская область, Новочеркасск, Будённовская улица, 141","CountryNameCode":"RU","CountryName":"Россия","AdministrativeArea":{"AdministrativeAreaName":"Ростовская область","SubAdministrativeArea":
                                        {"SubAdministrativeAreaName":"городской округ Новочеркасск","Locality":{"LocalityName":"Новочеркасск","Thoroughfare":{"ThoroughfareName":"Будённовская улица","Premise":{"PremiseNumber":"141","PostalCode":{"PostalCodeNumber":"346411"
                                        }}}}}}}}}}
                                ,"name":"Будённовская улица, 141","description":"Новочеркасск, Ростовская область, Россия","boundedBy":{"Envelope":{"lowerCorner":"40.071895 47.422375","upperCorner":"40.080106 47.427947"}},"Point":{"pos":"40.076001 47.425161"}}}  *//*
                                JsonElement name1 = fromString(featureMember.toString(), "GeoObject.name");
                                JsonElement point = fromString(featureMember.toString(), "GeoObject.Point.pos");
                                // судя по поведению, этот метод используется только при поиске, надо добавить эти строки и в другие методы

                                String kind = fromString(featureMember.toString(),
                                        "GeoObject.metaDataProperty.GeocoderMetaData.kind").getAsString();

                                if (kind.equals("house")) {
                                    String city = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                    ".AdministrativeArea.Locality.LocalityName").getAsString();
                                    String street = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                    ".AdministrativeArea.Locality.Thoroughfare.ThoroughfareName").getAsString();
                                    String house = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                    ".AdministrativeArea.Locality.Thoroughfare.Premise.PremiseNumber").getAsString();
                                if (!TextUtils.isEmpty(name1.getAsString()) && !TextUtils.isEmpty(point.getAsString())) {
                                    String[] coords = point.getAsString().split(" ");

                                    if (coords.length > 1) {
                                        place = new Place();
                                        place.setAddress(name1.getAsString());
                                        // и запишем
                                        place.setCity(city);
                                        place.setStreet(street);
                                        place.setHouse(house);

                                        Location loc = new Location("loc");
                                        loc.setLongitude(Double.parseDouble(coords[0]));
                                        loc.setLatitude(Double.parseDouble(coords[1]));
                                        place.setLocation(loc);
                                        //37.407079 55.653853
                                    }
                                }
                            }
                            }
                        }*/
                    } catch (JsonSyntaxException e) {
                        LogUtil.Loge(e);
                        e.printStackTrace();
                        place = null;
                    } catch (NumberFormatException e) {
                        LogUtil.Loge(e);
                        e.printStackTrace();
                        place = null;
                    } finally {
                        if (listener != null) {
                            if (place != null) {
                                listener.onSuccessResponse(place);
                            } else {
                                listener.onFailure(Consts.PARSE_ERROR);
                            }
                        }
                    }
                }).start();

            }
        });
    }

  /*  public void getAddressesBy(final Location location, final YandexMapsListener listener) {
        RequestParams params = new RequestParams();
        params.put(KIND, HOUSE);
        params.put(FORMAT, JSON);

        params.put(GEOCODE, String.format(Locale.ENGLISH, "%f,%f", location.getLongitude(), location.getLatitude()));

        YandexMapsClient.get("?", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure(statusCode);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        //XXX: http://www.jsonschema2pojo.org/ делает из json классы
                        //XXX: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
                        LogUtil.Log(responseString);

                        ArrayList<Place> places = new ArrayList<Place>();

                        try {
                            JsonElement jsonElement = fromString(responseString, "response.GeoObjectCollection.featureMember");
                            System.err.println("YMSearch2 " + responseString);
                            // плохо, значит мы нашли не все места, где получается адрес

                            if (jsonElement instanceof JsonArray) {
                                JsonArray featureMembers = (JsonArray) jsonElement;
                                for (JsonElement featureMember : featureMembers) {
                                    JsonElement name = fromString(featureMember.toString(), "GeoObject.name");
                                    JsonElement point = fromString(featureMember.toString(), "GeoObject.Point.pos");
                                    JsonElement desc = fromString(featureMember.toString(), "GeoObject.description");

                                    String kind = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.kind").getAsString();
                                    String city = "";
                                    String street = "";
                                    String house = "";

                                    if (kind.equals("house")) {
                                        city = fromString(featureMember.toString(),
                                                "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                        ".AdministrativeArea.Locality.LocalityName").getAsString();
                                        street = fromString(featureMember.toString(),
                                                "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                        ".AdministrativeArea.Locality.Thoroughfare.ThoroughfareName").getAsString();
                                        house = fromString(featureMember.toString(),
                                                "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                        ".AdministrativeArea.Locality.Thoroughfare.Premise.PremiseNumber").getAsString();
                                    }
                                    if (!TextUtils.isEmpty(name.getAsString()) && !TextUtils.isEmpty(point.getAsString())) {
                                        String[] coords = point.getAsString().split(" ");
                                        if (coords.length > 1) {
                                            Place place = new Place();
                                            place.setAddress(name.getAsString());
                                            place.setFullAddress(desc.getAsString());

                                            place.setCity(city);
                                            place.setStreet(street);
                                            place.setHouse(house);

                                            Location loc = new Location("loc");
                                            loc.setLongitude(Double.parseDouble(coords[0]));
                                            loc.setLatitude(Double.parseDouble(coords[1]));
                                            place.setLocation(loc);

                                            places.add(place);
                                        }
                                    }
                                }
                            }
                        } catch (JsonSyntaxException e) {
                            LogUtil.Loge(e);
                            e.printStackTrace();
                            places = null;
                        } catch (NumberFormatException e) {
                            LogUtil.Loge(e);
                            e.printStackTrace();
                            places = null;
                        } finally {
                            if (listener != null) {
                                if (places != null) {
                                    listener.onSuccessResponse(places);
                                } else {
                                    listener.onFailure(Consts.PARSE_ERROR);
                                }
                            }
                        }
                    }
                }).start();

            }
        });
    }*/
/*
    public void getAddressesBy(final String name, final YandexMapsListener listener) {
        RequestParams params = new RequestParams();
        params.put(KIND, HOUSE);
        params.put(FORMAT, JSON);
        params.put(APIKEY, API_KEY);
        params.put(GEOCODE, name);

        YandexMapsClient.get("?", params, new TextHttpResponseHandler() {
            @Override
            public void onFailure(int statusCode, Header[] headers, String responseString, Throwable throwable) {
                if (listener != null) {
                    listener.onFailure(statusCode);
                }
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, final String responseString) {
                new Thread(() -> {
                    //XXX: http://www.jsonschema2pojo.org/ делает из json классы
                    //XXX: https://github.com/codepath/android_guides/wiki/Leveraging-the-Gson-Library
                    LogUtil.Log(responseString);

                    ArrayList<Place> places = new ArrayList<Place>();

                    try {
                        JsonElement jsonElement = fromString(responseString, "response.GeoObjectCollection.featureMember");
                        System.err.println("YMSearch3 " + responseString);

                        if (jsonElement instanceof JsonArray) {
                            JsonArray featureMembers = (JsonArray) jsonElement;
                            for (JsonElement featureMember : featureMembers) {
                                JsonElement name1 = fromString(featureMember.toString(), "GeoObject.name");
                                JsonElement point = fromString(featureMember.toString(), "GeoObject.Point.pos");
                                JsonElement desc = fromString(featureMember.toString(), "GeoObject.description");

                                String kind = fromString(featureMember.toString(),
                                        "GeoObject.metaDataProperty.GeocoderMetaData.kind").getAsString();
                                String city = "";
                                String street = "";
                                String house = "";

                                if (kind.equals("house")) {
                                    city = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                    ".AdministrativeArea.Locality.LocalityName").getAsString();
                                    street = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                    ".AdministrativeArea.Locality.Thoroughfare.ThoroughfareName").getAsString();
                                    house = fromString(featureMember.toString(),
                                            "GeoObject.metaDataProperty.GeocoderMetaData.AddressDetails.Country" +
                                                    ".AdministrativeArea.Locality.Thoroughfare.Premise.PremiseNumber").getAsString();

                                }
                                if (!TextUtils.isEmpty(name1.getAsString()) && !TextUtils.isEmpty(point.getAsString())) {
                                    String[] coords = point.getAsString().split(" ");

                                    if (coords.length > 1) {
                                        Place place = new Place();
                                        place.setAddress(name1.getAsString());
                                        place.setFullAddress(desc.getAsString());

                                        place.setCity(city);
                                        place.setStreet(street);
                                        place.setHouse(house);

                                        Location loc = new Location("loc");
                                        loc.setLongitude(Double.parseDouble(coords[0]));
                                        loc.setLatitude(Double.parseDouble(coords[1]));
                                        place.setLocation(loc);
                                        places.add(place);
                                    }
                                }
                            }
                        }
                    } catch (JsonSyntaxException e) {
                        LogUtil.Loge(e);
                        e.printStackTrace();
                        places = null;
                    } catch (NullPointerException e) {
                        LogUtil.Loge(e);
                        e.printStackTrace();
                        places = null;
                    } finally {
                        if (listener != null) {
                            if (places != null) {
                                listener.onSuccessResponse(places);
                            } else {
                                listener.onFailure(Consts.PARSE_ERROR);
                            }
                        }
                    }
                }).start();

            }
        });
    }*/

    public static JsonElement fromString(String json, String path)
            throws JsonSyntaxException, NullPointerException {
        JsonObject obj = new GsonBuilder().create().fromJson(json, JsonObject.class);
        String[] seg = path.split("\\.");
        for (String element : seg) {
            if (obj != null) {
                JsonElement ele = obj.get(element);
                if (!ele.isJsonObject())
                    return ele;
                else
                    obj = ele.getAsJsonObject();
            } else {
                return null;
            }
        }
        return obj;
    }
}
