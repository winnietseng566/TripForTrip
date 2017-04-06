package com.tripfortrip.tripfortrip._02_SpotSearch;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.location.Location;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.Html;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tripfortrip.tripfortrip.R;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class Search2Activity extends AppCompatActivity {

    private static final int REQUEST_PLACE_PICKER = 100;
    private EditText edCountry;
    private EditText edSpot;

    private final String TAG = getClass().getSimpleName();
    //    private GoogleMap mMap;
    private String placesType;
    private LocationManager locationManager;
    private Location loc;
//    private ArrayList<Place> findPlaces;
    private RecyclerView recyclerView;
    private LatLngBounds latLngBounds;
    private LatLng latLng;

    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search2);
//        findViews();
        recyclerView = (RecyclerView) findViewById(R.id.rv_searchSpot);
        //設定城市與景點型態下拉選單
        Spinner spinnerCountry = (Spinner) findViewById(R.id.sp_searchCountry);
        Spinner spinnerSpotType = (Spinner) findViewById(R.id.sp_searchSpotType);
        spinnerCountry.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.places_city)));
        spinnerSpotType.setAdapter(new ArrayAdapter<String>(this,android.R.layout.simple_list_item_1,getResources().getStringArray(R.array.places_type)));
        final ArrayList<LatLngBounds> latLngBoundsArrayList = getLatLngBoundsOfCitys();
        final ArrayList<LatLng> latLngs = getLatLngs();
//        final String [] placesTypes = getResources().getStringArray(R.array.places_type);
        final ArrayList<String> placesTypes = getSpotTypes();

        latLngBounds = new LatLngBounds(new LatLng(22.044841, 120.101598),new LatLng(25.343849, 122.102462));
        latLng = new LatLng(23.866057, 120.856318);

        spinnerCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                Log.d("SelectedItemPosition",parent.getSelectedItemPosition()+"");//位置
                Log.d("SelectedItem", parent.getSelectedItem()+"");  //縣市
                latLngBounds = latLngBoundsArrayList.get(parent.getSelectedItemPosition());
                latLng = latLngs.get(parent.getSelectedItemPosition());

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinnerSpotType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                int selectedPosition=parent.getSelectedItemPosition();
                switch (selectedPosition){
                    case 0:
                        placesType = "";
                        break;
                    case 1:
                        placesType = "飯店";
                        break;
                    default:
                        placesType= placesTypes.get(selectedPosition);
                        break;
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

//        TextView textPlace = (TextView) findViewById(R.id.textPlace);

//        currentLocation();


//        View view = getLayoutInflater().inflate(R.layout.activity_search2);

//        RecyclerView recyclerView=(RecyclerView) inflater.inflate(R.layout.journey_detail_everyday_fragment,container,false);
//        setUpRecyclerView(recyclerView);
    }

    @NonNull
    private ArrayList<String> getSpotTypes() {
        ArrayList<String> placesTypes = new ArrayList<>();
        placesTypes.add("");
        placesTypes.add("ROOM");
        placesTypes.add("AMUSEMENT_PARK");
        placesTypes.add("RESTAURANT");
        placesTypes.add("ZOO");
        placesTypes.add("TRAIN_STATION");
        placesTypes.add("SHOPPING_MALL");
        return placesTypes;
    }

    private ArrayList<LatLng> getLatLngs() {
        ArrayList<LatLng> latLngs = new ArrayList<>();
        latLngs.add(new LatLng(23.866057, 120.856318));
        latLngs.add(new LatLng(25.125580, 121.748294));
        latLngs.add(new LatLng(25.045533, 121.534565));
        latLngs.add(new LatLng(24.994473, 121.501971));
        latLngs.add(new LatLng(24.972864, 121.195201));
        latLngs.add(new LatLng(24.795991, 121.008498));
        latLngs.add(new LatLng(24.574141, 120.814325));
        latLngs.add(new LatLng(24.191484, 120.644327));
        latLngs.add(new LatLng(23.866057, 120.856318));
        latLngs.add(new LatLng(24.060021, 120.564172));
        latLngs.add(new LatLng(23.708055, 120.352149));
        latLngs.add(new LatLng(23.480899, 120.429042));
        latLngs.add(new LatLng(23.039819, 120.221042));
        latLngs.add(new LatLng(22.663054, 120.313986));
        latLngs.add(new LatLng(22.563538, 120.532790));
        latLngs.add(new LatLng(24.719144, 121.740546));
        latLngs.add(new LatLng(23.963497, 121.574313));
        latLngs.add(new LatLng(22.761864, 121.109626));
        return latLngs;
    }

    private ArrayList<LatLngBounds> getLatLngBoundsOfCitys() {
        ArrayList<LatLngBounds> latLngBoundses = new ArrayList<>();
        latLngBoundses.add(new LatLngBounds(new LatLng(22.044841, 120.101598),new LatLng(25.343849, 122.102462)));
        latLngBoundses.add(new LatLngBounds(new LatLng(25.064521, 121.637283),new LatLng(25.169511, 121.819797)));
        latLngBoundses.add(new LatLngBounds(new LatLng(25.202970, 121.625256),new LatLng(25.202970, 121.625256)));
        latLngBoundses.add(new LatLngBounds(new LatLng(24.695520, 121.366476),new LatLng(25.292518, 122.055010)));
        latLngBoundses.add(new LatLngBounds(new LatLng(24.905841, 121.019409),new LatLng(25.133969, 121.454828)));
        latLngBoundses.add(new LatLngBounds(new LatLng(24.510559, 120.937290),new LatLng(24.796600, 121.440430)));
        latLngBoundses.add(new LatLngBounds(new LatLng(24.357212, 120.633890),new LatLng(24.595899, 121.266140)));
        latLngBoundses.add(new LatLngBounds(new LatLng(24.078203, 120.478128),new LatLng(24.410136, 121.416054)));
        latLngBoundses.add(new LatLngBounds(new LatLng(23.505381, 120.649167),new LatLng(24.224902, 121.375134)));
        latLngBoundses.add(new LatLngBounds(new LatLng(23.831802, 120.253295),new LatLng(24.108693, 120.699786)));
        latLngBoundses.add(new LatLngBounds(new LatLng(23.519184, 120.144591),new LatLng(23.791411, 120.648449)));
        latLngBoundses.add(new LatLngBounds(new LatLng(23.308462, 120.125364),new LatLng(23.585177, 120.868508)));
        latLngBoundses.add(new LatLngBounds(new LatLng(22.938326, 120.069917),new LatLng(23.349800, 120.632838)));
        latLngBoundses.add(new LatLngBounds(new LatLng(22.481629, 120.290577),new LatLng(23.512031, 121.018346)));
        latLngBoundses.add(new LatLngBounds(new LatLng(21.946865, 120.666548),new LatLng(22.848226, 120.889997)));
        latLngBoundses.add(new LatLngBounds(new LatLng(24.350329, 121.320038),new LatLng(24.998716, 121.978671)));
        latLngBoundses.add(new LatLngBounds(new LatLng(23.129979, 121.030565),new LatLng(24.348258, 121.756929)));
        latLngBoundses.add(new LatLngBounds(new LatLng(22.234574, 120.769501),new LatLng(23.434917, 121.493376)));
        return latLngBoundses;
    }

    private void currentLocation() {
//        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
//
//        String provider = locationManager
//                .getBestProvider(new Criteria(), false);
//
//        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
//            // TODO: Consider calling
//            //    ActivityCompat#requestPermissions
//            // here to request the missing permissions, and then overriding
//            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
//            //                                          int[] grantResults)
//            // to handle the case where the user grants the permission. See the documentation
//            // for ActivityCompat#requestPermissions for more details.
//            return;
//        }
//        Location location = locationManager.getLastKnownLocation(provider);
//
//        if (location == null) {
//            locationManager.requestLocationUpdates(provider, 0, 0, listener);
//        } else {
//            loc = location;
//            new GetPlacesTask(Search2Activity.this,
//                    places[0].toLowerCase().replace(
//                            "-", "_")).execute();
//            Log.e(TAG, "location : " + location);
//        }
    }
    //搜尋
    public void onSearchClick(View view) {
        new GetPlacesTask(Search2Activity.this,
                placesType.toLowerCase().replace(
                        "-", "_")).execute();
    }
    //景點選擇器
    public void onPickClick(View view) {
        try {
//            LatLngBounds latLngBounds = LatLngBounds.createFromAttributes(this,)
//            LatLng northEast =new LatLng(25.1345654302915,121.7838000302915);
//            LatLng southWest = new LatLng(25.1318674697085,121.7811020697085);
//            latLngBounds = new LatLngBounds(southWest,northEast);
//            builder.include(northEast);
//            builder.include(southWest);

            PlacePicker.IntentBuilder intentBuilder =
                    new PlacePicker.IntentBuilder().setLatLngBounds(latLngBounds);
            Intent intent = intentBuilder.build(this);
            // Start the intent by requesting a result,
            // identified by a request code.
            startActivityForResult(intent, REQUEST_PLACE_PICKER);

        } catch (GooglePlayServicesRepairableException e) {
            // ...
        } catch (GooglePlayServicesNotAvailableException e) {
            // ...
        }
    }
    @Override
    protected void onActivityResult(int requestCode,
                                    int resultCode, Intent data) {

        if (requestCode == REQUEST_PLACE_PICKER
                && resultCode == Activity.RESULT_OK) {

            // The user has selected a place. Extract the name and address.
            final com.google.android.gms.location.places.Place place = PlacePicker.getPlace( this,data);

            final CharSequence name = place.getName();
            final CharSequence address = place.getAddress();

            String attributions = PlacePicker.getAttributions(data);
            if (attributions == null) {
                attributions = "";
            }
//            textPlace.setText(name+"/"+address+"/"+attributions+"/"+place.getId()+"/"+place.getLatLng()+"/"+place.getLocale()+"/"+place.getPhoneNumber()+"/"+place.getPlaceTypes()+"/"+place.getPriceLevel()+"/"+place.getRating()+"/"+place.getViewport()+"/"+place.getWebsiteUri());
            Toast.makeText(this,name+"/"+address+"/"+ Html.fromHtml(attributions),Toast.LENGTH_LONG).show();

//            mViewName.setText(name);
//            mViewAddress.setText(address);
//            mViewAttributions.setText(Html.fromHtml(attributions));

        } else {
            super.onActivityResult(requestCode, resultCode, data);
        }
    }




    private class GetPlacesTask extends AsyncTask<Void,Void,ArrayList<Place>>{
//        private ProgressDialog dialog;
        private Context context;
        private String places;

        public GetPlacesTask(Context context, String places) {
            this.context = context;
            this.places = places;
        }

        @Override
        protected ArrayList<Place> doInBackground(Void... params) {
            PlacesService service = new PlacesService(
                    getString(R.string.key_placesAPI));
            // 28.632808
            // 77.218276
            ArrayList<Place> findPlaces = service.findPlaces(latLng.latitude, // 28.632808
                    latLng.longitude, places);


            for (int i = 0; i < findPlaces.size(); i++) {

                Place placeDetail = findPlaces.get(i);
                Log.e(TAG, "places : " + placeDetail.getName());
            }
            return findPlaces;

        }
        @Override
        protected void onPostExecute(ArrayList<Place> findPlaces) {
            super.onPostExecute(findPlaces);
//            Toast.makeText(Search2Activity.this,places.toString(),Toast.LENGTH_SHORT);
            setUpRecyclerView(recyclerView,findPlaces);


        }

    }
    //照片會亂跳
    private class GetPhotoTask extends AsyncTask<Void,Void,Bitmap>{
        String photoUrl;
        private ImageView img;

        public GetPhotoTask(String photoUrl, ImageView img) {
            this.photoUrl = photoUrl;
            this.img=img;
        }

        @Override
        protected Bitmap doInBackground(Void... params) {
            PlacesService placesService = new PlacesService(getString(R.string.key_placesAPI));
            return placesService.getPhotos(photoUrl);
        }

        @Override
        protected void onPostExecute(Bitmap bitmap) {
            super.onPostExecute(bitmap);
            if(bitmap!=null) {
                img.setImageBitmap(bitmap);
            }
        }
    }

    private void findViews() {
//        edCountry = (EditText) findViewById(R.id.edt_searchCountry);
//        edSpot = (EditText) findViewById(R.id.edt_searchSpot);
    }

    //    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
//        View view = inflater.inflate(R.layout.activity_search2,container,false);
////        RecyclerView recyclerView=(RecyclerView) inflater.inflate(R.layout.journey_detail_everyday_fragment,container,false);
//        RecyclerView recyclerView= (RecyclerView) view.findViewById(R.id.rv_searchSpot);
//        setUpRecyclerView(recyclerView);
//        return  view;
//    }
    private void setUpRecyclerView(RecyclerView rv,ArrayList<Place> findPlaces)
    {
        rv.setLayoutManager(new GridLayoutManager(this,2));
        rv.setAdapter(new ShowSpotAdapter(this, getListForItems(findPlaces)));
//        rv.setLayoutManager(new LinearLayoutManager(rv.getContext()));
//        rv.setAdapter(new ShowSpotAdapter(rv.getContext(), getListForItems()));

    }
    public ArrayList<Map<String,String>> getListForItems(ArrayList<Place> findPlaces)
    {
        ArrayList<Map<String,String>> spotList =new ArrayList<>();
        for(int i=0 ;i<findPlaces.size();i++) {
            Map<String, String> map = new HashMap<>();
            map.put("picUrl", findPlaces.get(i).getPhotoUrlString());
            map.put("title", i+findPlaces.get(i).getName());
            map.put("address", i+findPlaces.get(i).getVicinity());
            spotList.add(map);
        }


        return spotList;
    }
    public   class ShowSpotAdapter extends RecyclerView.Adapter<ShowSpotAdapter.ViewHolder> {
        ArrayList<Map<String,String>> spotList =new ArrayList<>();
        Context aboutuscontext;
        public  class ViewHolder extends RecyclerView.ViewHolder {
            TextView spotTitle;
            TextView spotAddress;
            ImageView spotImg;


            public ViewHolder(View view) {
                super(view);
                spotTitle=(TextView)view.findViewById(R.id.txv_journey_title);
                spotAddress =(TextView)view.findViewById(R.id.txv_journey_info);
                spotImg =  (ImageView) view.findViewById(R.id.iv_journey_pic);
            }
        }
        public ShowSpotAdapter(Context context, ArrayList<Map<String,String>> list) {
            aboutuscontext=context;
            spotList =list;
        }
        @Override
        public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view=LayoutInflater.from(parent.getContext()).inflate(R.layout.journey_card, parent, false);
            return  new ViewHolder(view);
        }
        @Override
        public void onBindViewHolder(ViewHolder holder, int position) {
            new GetPhotoTask(spotList.get(position).get("picUrl"),holder.spotImg).execute();

            holder.spotTitle.setText(spotList.get(position).get("title"));
            holder.spotAddress.setText(spotList.get(position).get("address"));

        }
        @Override
        public int getItemCount() {
            return spotList.size();
        }
    }
}
