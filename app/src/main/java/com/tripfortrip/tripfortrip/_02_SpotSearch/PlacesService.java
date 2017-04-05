package com.tripfortrip.tripfortrip._02_SpotSearch;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.util.Log;
import android.widget.ImageView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Created by wei-tzutseng on 2017/3/30.
 */

public class PlacesService {
    private String API_KEY;

    public PlacesService(String apikey) {
        this.API_KEY = apikey;
    }

    public void setApiKey(String apikey) {
        this.API_KEY = apikey;
    }

    public ArrayList<Place> findPlaces(double latitude, double longitude,
                                       String placeSpacification) {

        String urlString = makeUrl(latitude, longitude, placeSpacification);

        try {
            String json = getJSON(urlString);
            Log.d("urlString",urlString);
//            Log.d("json",json);
//            System.out.pr/intln(json);
            JSONObject object = new JSONObject(json);

            JSONArray array = object.getJSONArray("results");

            ArrayList<Place> arrayList = new ArrayList<Place>();
            for (int i = 0; i < array.length(); i++) {
                try {
                    Place place = Place
                            .jsonToPontoReferencia((JSONObject) array.get(i));
//                    Log.v("Places Services ", "" + place);
                    arrayList.add(place);
                } catch (Exception e) {
                }
            }
            return arrayList;
        } catch (JSONException ex) {
            Logger.getLogger(PlacesService.class.getName()).log(Level.SEVERE,
                    null, ex);
        }
        return null;
    }

    public Bitmap getPhotos(String photoUrl){
        try {
            Bitmap bm = null;
            InputStream is = null;
            BufferedInputStream bis = null;

            try {
                    URLConnection conn = new URL(photoUrl +API_KEY).openConnection();
                Log.d("photoUrl +API_KEY",photoUrl +API_KEY);
                    conn.connect();
                    is = conn.getInputStream();
                    bis = new BufferedInputStream(is, 81920);
                    bm = BitmapFactory.decodeStream(bis);
                Log.d("bm",bm+"");

            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                if (bis != null) {
                    try {
                        bis.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                if (is != null) {
                    try {
                        is.close();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            return bm;
        }catch (Exception e){
            e.printStackTrace();

        }
        return null;
    }


    // https://maps.googleapis.com/maps/api/place/search/json?location=28.632808,77.218276&radius=500&types=atm&sensor=false&key=apikey
    private String makeUrl(double latitude, double longitude, String place) {
        StringBuilder urlString = new StringBuilder(
                "https://maps.googleapis.com/maps/api/place/search/json?");

        if (place.equals("")) {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            // urlString.append("&types="+place);
            urlString.append("&sensor=false&key=" + API_KEY);
        } else {
            urlString.append("&location=");
            urlString.append(Double.toString(latitude));
            urlString.append(",");
            urlString.append(Double.toString(longitude));
            urlString.append("&radius=1000");
            urlString.append("&types=" + place);
            urlString.append("&sensor=false&key=" + API_KEY);
        }
        return urlString.toString();
    }

    protected String getJSON(String url) {
//        Log.d("getJSON","getJSON");

        return getUrlContents(url);
    }

    private String getUrlContents(String theUrl) {
        StringBuilder content = new StringBuilder();
        try {
            URL url = new URL(theUrl);
            URLConnection urlConnection = url.openConnection();
            BufferedReader bufferedReader = new BufferedReader(
                    new InputStreamReader(urlConnection.getInputStream()));
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                content.append(line + "\n");
            }
            bufferedReader.close();
        }catch (Exception e) {
            e.printStackTrace();
        }
//        Log.d("getUrlContents",content.toString());

        return content.toString();
    }
}
