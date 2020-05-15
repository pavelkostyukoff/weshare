package com.spacesofting.weshare.yandex;

import android.location.Location;
import android.text.TextUtils;

import com.google.gson.GsonBuilder;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonSyntaxException;
import com.loopj.android.http.RequestParams;
import com.loopj.android.http.TextHttpResponseHandler;
import com.spacesofting.weshare.api.model.place.Place;
import com.spacesofting.weshare.utils.Consts;
import com.spacesofting.weshare.utils.LogUtil;

import java.util.ArrayList;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

/**
 * Created by vkozlov on 28.12.15.
 */
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

            if (location != null) {
                params.put(LL, String.format(Locale.ENGLISH, "%f,%f", location.getLongitude(), location.getLatitude()));
            }
        }
        else {
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
                        System.err.println("YMSearch1 " + responseString);
                        JsonElement jsonElement = fromString(responseString, "response.GeoObjectCollection.featureMember");

                        if (jsonElement instanceof JsonArray) {
                            JsonArray featureMembers = (JsonArray) jsonElement;
                            if (featureMembers.size() > 0) {
                                JsonElement featureMember = featureMembers.get(0);

                                JsonElement name1 = fromString(featureMember.toString(), "GeoObject.name");
                                JsonElement point = fromString(featureMember.toString(), "GeoObject.Point.pos");
                                // судя по поведению, этот метод используется только при поиске, надо добавить эти строки и в другие методы

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
                                // как-то так получаются данные от яндекс-карт
                                //todo не плохо разобрался за 1н вечер ссылки не осталось ? где описание ?раньше не
                                // я не читал ничего, это всё из того Json-ответа от сервера
                                // не факт, что всё сразу заработает, там есть ещё моменты. но их долго искать и проверять
                                // давай лучше просто попробуем
                                //todo можно буквально в 2х словах , что это за класс и за что он отвечает ? раньше не открывал его..
                                // как и следует из названия, класс для работы с яндекс-картами, если точнее из функционала - для обработки ответов
                                // api карт
                                //todo ook 1 мин гляну что тут есть.. , ок продолжим?
                                // да
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
                                    }
                                }
                            }
                        }
                    }
                    catch (JsonSyntaxException e) {
                        LogUtil.Loge(e);
                        e.printStackTrace();
                        place = null;
                    }
                    catch (NumberFormatException e) {
                        LogUtil.Loge(e);
                        e.printStackTrace();
                        place = null;
                    }
                    finally {
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

    public void getAddressesBy(final Location location, final YandexMapsListener listener) {
        RequestParams params = new RequestParams();
        params.put(KIND, HOUSE);
        params.put(FORMAT, JSON);

        params.put(GEOCODE, String.format(Locale.ENGLISH, "%f,%f", location.getLongitude(), location.getLatitude()));

        YandexMapsClient.get("/", params, new TextHttpResponseHandler() {
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
    }

    public void getAddressesBy(final String name, final YandexMapsListener listener) {
        RequestParams params = new RequestParams();
        params.put(KIND, HOUSE);
        params.put(FORMAT, JSON);

        params.put(GEOCODE, name);

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
    }

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
