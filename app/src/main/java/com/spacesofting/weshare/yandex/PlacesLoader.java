package com.spacesofting.weshare.yandex;

import android.content.Context;
import android.util.Xml;

import androidx.loader.content.AsyncTaskLoader;

import com.spacesofting.weshare.api.model.place.Place;
import com.spacesofting.weshare.utils.LogUtil;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;


/**
 * Created by vkozlov on 22.01.16.
 */
public class PlacesLoader extends AsyncTaskLoader<List<Place>> {

    private final Place.PlaceParam placeParam;

    private static final String ns = null;

    public PlacesLoader(Context context, int placeParam) {
        super(context);
        this.placeParam = Place.PlaceParam.valueOf(placeParam);
    }

    @Override
    protected void onStartLoading() {
        if (takeContentChanged())
            forceLoad();
    }

    @Override
    protected void onStopLoading() {
        cancelLoad();
    }


    @Override
    public List<Place> loadInBackground() {
        ArrayList<Place> items = new ArrayList<Place>();

        InputStream in = null;
        try {
            in = getContext().getAssets().open("Place.plist");
            items = parse(in);
            Collections.sort(items, new Comparator<Place>() {
                @Override
                public int compare(Place s1, Place s2) {
                    return s1.getAddress().compareToIgnoreCase(s2.getAddress());
                }
            });

        } catch (IOException e) {
            LogUtil.Loge(e);
            e.printStackTrace();
        } catch (XmlPullParserException e) {
            LogUtil.Loge(e);
            e.printStackTrace();
        }

        switch (placeParam) {
            case Metro:
                break;
            case Airport:
                break;
            case Train:
                break;
        }
        return items;
    }

    private ArrayList<Place> parse(InputStream in) throws XmlPullParserException, IOException {

        try {
            XmlPullParser parser = Xml.newPullParser();
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in, null);
            parser.nextTag();

            parser.require(XmlPullParser.START_TAG, ns, "plist");
            parser.nextTag();

            return readXml(parser);
        } finally {
            in.close();
        }
    }

    private ArrayList<Place> readXml(XmlPullParser parser) throws XmlPullParserException, IOException {
        ArrayList<Place> entries = new ArrayList<Place>();

        parser.require(XmlPullParser.START_TAG, ns, "dict");
        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }
            String name = parser.getName();
            // Starts by looking for the entry tag
            if (name.equals("key")) {
//                entries.add(readItem(parser));
                String key = readKey(parser);
                if (key.equalsIgnoreCase(placeParam.toString())) {
                    entries.addAll(readItems(parser));
                }
            } else {
                skip(parser);
            }
        }
        return entries;
    }

    private ArrayList<Place> readItems(XmlPullParser parser) throws XmlPullParserException, IOException {
        parser.nextTag();

        parser.require(XmlPullParser.START_TAG, ns, "array");


        ArrayList<Place> items = new ArrayList<>();

        while (parser.next() != XmlPullParser.END_TAG) {
            if (parser.getEventType() != XmlPullParser.START_TAG) {
                continue;
            }

            HashMap<String, String> params = new HashMap<String, String>();

            parser.require(XmlPullParser.START_TAG, ns, "dict");

            while (parser.next() != XmlPullParser.END_TAG) {
                if (parser.getEventType() != XmlPullParser.START_TAG) {
                    continue;
                }

                String name = parser.getName();
                if (name.equals("key")) {
                    String key = readKey(parser);
                    parser.nextTag();
                    String value = readValue(parser);
                    params.put(key, value);
                } else {
                    skip(parser);
                }
            }
            Place place = new Place(params);
            switch (placeParam) {
                case Metro:
                    break;
                case Airport:
                    place.setIsAirport(true);
                    break;
                case Train:
                    break;
            }
            items.add(place);
        }

        return items;
    }

    private String readValue(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, parser.getName());
        String value = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, parser.getName());
        return value;
    }

    private String readKey(XmlPullParser parser) throws IOException, XmlPullParserException {
        parser.require(XmlPullParser.START_TAG, ns, "key");
        String key = readText(parser);
        parser.require(XmlPullParser.END_TAG, ns, "key");
        return key;
    }

    private String readText(XmlPullParser parser) throws IOException, XmlPullParserException {
        String result = "";
        if (parser.next() == XmlPullParser.TEXT) {
            result = parser.getText();
            parser.nextTag();
        }
        return result;
    }

    private void skip(XmlPullParser parser) throws XmlPullParserException, IOException {
        if (parser.getEventType() != XmlPullParser.START_TAG) {
            throw new IllegalStateException();
        }
        int depth = 1;
        while (depth != 0) {
            switch (parser.next()) {
                case XmlPullParser.END_TAG:
                    depth--;
                    break;
                case XmlPullParser.START_TAG:
                    depth++;
                    break;
            }
        }
    }
}
