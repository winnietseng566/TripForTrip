package com.tripfortrip.tripfortrip._02_SpotSearch;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.text.Spanned;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.Result;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.location.places.*;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.tripfortrip.tripfortrip.R;

import static android.R.attr.filter;


public class AutocompleteFilterActivity extends AppCompatActivity implements GoogleApiClient.OnConnectionFailedListener{
        /**
         * GoogleApiClient wraps our service connection to Google Play Services and provides access
         * to the user's sign in state as well as the Google's APIs.
         */
        private static final int GOOGLE_API_CLIENT_ID = 0;
        private static final int REQUESE_FINE_LOCATION = 100;

        protected GoogleApiClient mGoogleApiClient;

        private PlaceAutocompleteAdapter mAdapter;

        private AutoCompleteTextView mAutocompleteView;

        private TextView mPlaceDetailsText;

        private TextView mPlaceDetailsAttribution;

        private Button mCurrentLocation;

        private static final String TAG = "AutocompleteFilterActivity";

        private static final LatLngBounds BOUNDS_GREATER_SYDNEY = new LatLngBounds(
                new LatLng(-34.041458, 150.790100), new LatLng(-33.682247, 151.383362));
        private static final LatLngBounds BOUNDS_GREATER_KEELUNG = new LatLngBounds(
                 new LatLng(25.131867,121.781102),new LatLng(25.134565,121.783800));
        @Override
        protected void onCreate(Bundle savedInstanceState) {
            super.onCreate(savedInstanceState);

            // Construct a GoogleApiClient for the {@link Places#GEO_DATA_API} using AutoManage
            // functionality, which automatically sets up the API client to handle Activity lifecycle
            // events. If your activity does not extend FragmentActivity, make sure to call connect()
            // and disconnect() explicitly.
            mGoogleApiClient = new GoogleApiClient.Builder(this)
                    .enableAutoManage(this, GOOGLE_API_CLIENT_ID /* clientId */, AutocompleteFilterActivity.this)
                    .addApi(Places.GEO_DATA_API)
                    .addApi(Places.PLACE_DETECTION_API)
                    .build();

            setContentView(R.layout.activity_autocomplete_filter);

            // Retrieve the AutoCompleteTextView that will display Place suggestions.
            mAutocompleteView = (AutoCompleteTextView)
                    findViewById(R.id.autocomplete_places);

            // Register a listener that receives callbacks when a suggestion has been selected
//            mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);

            // Retrieve the TextViews that will display details and attributions of the selected place.
            mPlaceDetailsText = (TextView) findViewById(R.id.place_details);
            mPlaceDetailsAttribution = (TextView) findViewById(R.id.place_attribution);

            // CurrentLocation
            mCurrentLocation = (Button) findViewById(R.id.ll_current_location);
            mCurrentLocation.setOnClickListener(mOnClickListener);

            // Set up the adapter that will retrieve suggestions from the Places Geo Data API that cover
            // the entire world.
            mAdapter = new PlaceAutocompleteAdapter(this, android.R.layout.simple_list_item_1,
                    mGoogleApiClient, BOUNDS_GREATER_KEELUNG , null);
            mAutocompleteView.setAdapter(mAdapter);
        }

    @Override
    protected void onStart() {
        super.onStart();
        mAutocompleteView.setOnItemClickListener(mAutocompleteClickListener);
    }

    @Override
    protected void onStop() {
        super.onStop();
    }

    @Override
        public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
            super.onRequestPermissionsResult(requestCode, permissions, grantResults);
            if(requestCode == REQUESE_FINE_LOCATION && grantResults[0] == RESULT_OK){
                getCurrentPlaceResult();

            }
        }

        private View.OnClickListener mOnClickListener = new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int permission = ActivityCompat.checkSelfPermission(AutocompleteFilterActivity.this, Manifest.permission.ACCESS_FINE_LOCATION);
                if ( permission!= PackageManager.PERMISSION_GRANTED) {
                    // TODO: Consider calling
                    //    ActivityCompat#requestPermissions
                    // here to request the missing permissions, and then overriding
                    //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                    //                                          int[] grantResults)
                    // to handle the case where the user grants the permission. See the documentation
                    // for ActivityCompat#requestPermissions for more details.
                    ActivityCompat.requestPermissions(AutocompleteFilterActivity.this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION},REQUESE_FINE_LOCATION);
                }else{
                    getCurrentPlaceResult();
                }

            }
        };

        @SuppressWarnings("MissingPermission")
        private void getCurrentPlaceResult() {
            PendingResult<PlaceLikelihoodBuffer> result = Places.PlaceDetectionApi
                    .getCurrentPlace(mGoogleApiClient, null);
            result.setResultCallback(new ResultCallback<PlaceLikelihoodBuffer>() {
                @Override
                public void onResult(PlaceLikelihoodBuffer likelyPlaces) {
                    if (!likelyPlaces.getStatus().isSuccess()) {
                        // Request did not complete successfully
                        Log.e(TAG, "Place query did not complete. Error: " + likelyPlaces.getStatus().toString());
                        likelyPlaces.release();
                        return;
                    }
                    String placeName = String.format("%s", likelyPlaces.get(0).getPlace().getName());
                    String placeAttributuion = String.format("%s", likelyPlaces.get(0).getPlace().getAddress());
                    mPlaceDetailsText.setText(placeName);
                    mPlaceDetailsAttribution.setText(placeAttributuion);
                    likelyPlaces.release();
                }
            });
        }

        /**
         * Listener that handles selections from suggestions from the AutoCompleteTextView that
         * displays Place suggestions.
         * Gets the place id of the selected item and issues a request to the Places Geo Data API
         * to retrieve more details about the place.
         *
         * @see com.google.android.gms.location.places.GeoDataApi#getPlaceById(com.google.android.gms.common.api.GoogleApiClient,
         * String...)
         */
        private AdapterView.OnItemClickListener mAutocompleteClickListener
                = new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            /*
             Retrieve the place ID of the selected item from the Adapter.
             The adapter stores each Place suggestion in a PlaceAutocomplete object from which we
             read the place ID.
              */
                final PlaceAutocompleteAdapter.PlaceAutocomplete item = mAdapter.getItem(position);
                final String placeId = String.valueOf(item.placeId);
                Log.i(TAG, "Autocomplete item selected: " + item.description);

            /*
             Issue a request to the Places Geo Data API to retrieve a Place object with additional
              details about the place.
              */
                PendingResult<PlaceBuffer> placeResult = Places.GeoDataApi
                        .getPlaceById(mGoogleApiClient, placeId);
                placeResult.setResultCallback(mUpdatePlaceDetailsCallback);
                Log.i(TAG, "Called getPlaceById to get Place details for " + item.placeId);
            }
        };

        /**
         * Callback for results from a Places Geo Data API query that shows the first place result in
         * the details view on screen.
         */
        private ResultCallback<PlaceBuffer> mUpdatePlaceDetailsCallback
                = new ResultCallback<PlaceBuffer>() {
            @Override
            public void onResult(PlaceBuffer places) {
                if (!places.getStatus().isSuccess()) {
                    // Request did not complete successfully
                    Log.e(TAG, "Place query did not complete. Error: " + places.getStatus().toString());
                    places.release();
                    return;
                }
                // Get the Place object from the buffer.
                final Place place = places.get(0);

                // Format details of the place for display and show it in a TextView.
//                mPlaceDetailsText.setText(formatPlaceDetails(getResources(), place.getName(),
//                        place.getId(), place.getAddress(), place.getPhoneNumber(),
//                        place.getWebsiteUri()));
                mPlaceDetailsText.setText(place.getName()+"/"+
                        place.getId()+"/"+place.getAddress()+"/"+place.getPhoneNumber()+"/"+
                        place.getWebsiteUri());
                Log.e(TAG,place.getName()+"/"+
                        place.getId()+"/"+place.getAddress()+"/"+place.getPhoneNumber()+"/"+
                        place.getWebsiteUri());

                // Display the third party attributions if set.
                final CharSequence thirdPartyAttribution = places.getAttributions();
                if (thirdPartyAttribution == null) {
                    mPlaceDetailsAttribution.setVisibility(View.GONE);
                } else {
                    mPlaceDetailsAttribution.setVisibility(View.VISIBLE);
                    mPlaceDetailsAttribution.setText(Html.fromHtml(thirdPartyAttribution.toString()));
                }

                Log.i(TAG, "Place details received: " + place.getName());

                places.release();
            }
        };

        private static Spanned formatPlaceDetails(Resources res, CharSequence name, String id,
                                                  CharSequence address, CharSequence phoneNumber, Uri websiteUri) {
            Log.e(TAG, res.getString(R.string.place_details, name, id, address, phoneNumber,
                    websiteUri));
            return Html.fromHtml(res.getString(R.string.place_details, name, id, address, phoneNumber,
                    websiteUri));

        }

        /**
         * Called when the Activity could not connect to Google Play services and the auto manager
         * could resolve the error automatically.
         * In this case the API is not available and notify the user.
         *
         * @param connectionResult can be inspected to determine the cause of the failure
         */
        @Override
        public void onConnectionFailed(ConnectionResult connectionResult) {

            Log.e(TAG, "onConnectionFailed: ConnectionResult.getErrorCode() = "
                    + connectionResult.getErrorCode());

            // TODO(Developer): Check error code and notify the user of error state and resolution.
            Toast.makeText(this,
                    "Could not connect to Google API Client: Error " + connectionResult.getErrorCode(),
                    Toast.LENGTH_SHORT).show();
            AutocompleteFilterActivity.this.finish();
        }

}