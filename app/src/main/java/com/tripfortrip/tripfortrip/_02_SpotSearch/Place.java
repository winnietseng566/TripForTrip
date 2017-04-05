package com.tripfortrip.tripfortrip._02_SpotSearch;

import android.util.Log;

import com.tripfortrip.tripfortrip.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;


/**
 * Created by wei-tzutseng on 2017/3/30.
 */

public class Place {
    private String id;
    private String icon;
    private String name;
    private String vicinity;
    private Double latitude;
    private Double longitude;
    private String photo_reference="";
    private String photoUrlString ;
    //    private SpotPhoto SpotPhoto;

    public String getId() {
        return id;
    }
    public void setId(String id) {
        this.id = id;
    }
    public String getIcon() {
        return icon;
    }
    public void setIcon(String icon) {
        this.icon = icon;
    }
    public Double getLatitude() {
        return latitude;
    }
    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }
    public Double getLongitude() {
        return longitude;
    }
    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }
    public String getName() {
        return name;
    }
    public void setName(String name) {
        this.name = name;
    }
    public String getVicinity() {
        return vicinity;
    }
    public void setVicinity(String vicinity) {
        this.vicinity = vicinity;
    }

    public String getPhoto_reference() {
        return photo_reference;
    }

    public void setPhoto_reference(String photo_reference) {
        this.photo_reference = photo_reference;
    }

    public String getPhotoUrlString() {
        return photoUrlString;
    }

    public void setPhotoUrlString(String photoUrlString) {
        this.photoUrlString = photoUrlString;
    }
    //    public com.tripfortrip.tripfortrip._02_SpotSearch.SpotPhoto getSpotPhoto() {
//        return SpotPhoto;
//    }
//
//    public void setSpotPhoto(SpotPhoto spotPhoto) {
//        SpotPhoto = spotPhoto;
//    }

    static Place jsonToPontoReferencia(JSONObject pontoReferencia) {

            Place result = new Place();
        try {
            JSONObject geometry = (JSONObject) pontoReferencia.get("geometry");
            JSONObject location = (JSONObject) geometry.get("location");
            result.setLatitude((Double) location.get("lat"));
            result.setLongitude((Double) location.get("lng"));
            result.setIcon(pontoReferencia.getString("icon"));
            result.setName(pontoReferencia.getString("name"));
            result.setVicinity(pontoReferencia.getString("vicinity"));
            result.setId(pontoReferencia.getString("id"));

            JSONArray array =pontoReferencia.getJSONArray("photos");
//            Log.d("array", array.toString());
            JSONObject photos = (JSONObject) array.get(0);
            result.setPhoto_reference(photos.getString("photo_reference"));
//            Log.d("photo_reference", photos.getString("photo_reference"));
            result.setPhotoUrlString(makePhotoUrlStr(result.getPhoto_reference()));





        } catch (JSONException ex) {
            Logger.getLogger(Place.class.getName()).log(Level.SEVERE, null, ex);
        }
        return result;
    }
//建立照片的ＵＲＬ
    private static String makePhotoUrlStr(String photo_reference) {
        String httpUrl ="https://maps.googleapis.com/maps/api/place/photo?maxwidth=256&photoreference=";

        return httpUrl+photo_reference+"&key=";
    }


    @Override
    public String toString() {
        return "Place{" + "id=" + id + ", icon=" + icon + ", name=" + name + ", latitude=" + latitude + ", longitude=" + longitude + ",Photo_reference="+photo_reference+"}";
    }
}
