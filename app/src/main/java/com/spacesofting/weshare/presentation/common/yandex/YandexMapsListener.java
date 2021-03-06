package com.spacesofting.weshare.presentation.common.yandex;

import com.spacesofting.weshare.data.api.model.place.Place;

import java.util.ArrayList;

public interface YandexMapsListener {
    void onSuccessResponse(Place place);

    void onFailure(int statusCode);

    void onSuccessResponse(ArrayList<Place> places);
}
