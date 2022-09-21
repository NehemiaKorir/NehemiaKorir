package com.example.criminalalertapp;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.loader.app.LoaderManager;
import androidx.loader.content.CursorLoader;
import androidx.loader.content.Loader;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Dialog;
import android.content.ContentValues;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.view.Menu;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

public abstract class map extends AppCompatActivity implements OnMapReadyCallback, LoaderManager.LoaderCallbacks {
    GoogleMap googleMap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        // Getting Google Play availability status
        int status = GooglePlayServicesUtil.isGooglePlayServicesAvailable(getBaseContext());

// Showing status
        if (status != ConnectionResult.SUCCESS) { // Google Play Services are not available

            int requestCode = 10;
            Dialog dialog = GooglePlayServicesUtil.getErrorDialog(status, this, requestCode);
            dialog.show();

        } else { // Google Play Services are available

// Getting reference to the SupportMapFragment of activity_main.xml
            //SupportMapFragment fm = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
            SupportMapFragment mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager()
                    .beginTransaction()
                    .add(R.id.map, mapFragment)
                    .commit();
// Getting GoogleMap object from the fragment
                       //googleMap = mapFragment.getMap();


// Enabling MyLocation Layer of Google Map
            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                // TODO: Consider calling
                //    ActivityCompat#requestPermissions
                // here to request the missing permissions, and then overriding
                //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
                //                                          int[] grantResults)
                // to handle the case where the user grants the permission. See the documentation
                // for ActivityCompat#requestPermissions for more details.
                return;
            }
            googleMap.setMyLocationEnabled(true);

// Invoke LoaderCallbacks to retrieve and draw already saved locations in map
            getSupportLoaderManager().initLoader(0, null, this);
        }

        googleMap.setOnMapClickListener(new GoogleMap.OnMapClickListener() {

            @Override
            public void onMapClick(LatLng point) {

// Drawing marker on the map
                drawMarker(point);

// Creating an instance of ContentValues
                ContentValues contentValues = new ContentValues();

// Setting latitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LAT, point.latitude );

// Setting longitude in ContentValues
                contentValues.put(LocationsDB.FIELD_LNG, point.longitude);

// Setting zoom in ContentValues
                contentValues.put(LocationsDB.FIELD_ZOOM, googleMap.getCameraPosition().zoom);

// Creating an instance of LocationInsertTask
                LocationInsertTask insertTask = new LocationInsertTask();

// Storing the latitude, longitude and zoom level to SQLite database
                insertTask.execute(contentValues);

                Toast.makeText(getBaseContext(), "Marker is added to the Map", Toast.LENGTH_SHORT).show();

            }
        });

        googleMap.setOnMapLongClickListener(new GoogleMap.OnMapLongClickListener() {
            @Override
            public void onMapLongClick(LatLng point) {

// Removing all markers from the Google Map
                googleMap.clear();

// Creating an instance of LocationDeleteTask
                LocationDeleteTask deleteTask = new LocationDeleteTask();

// Deleting all the rows from SQLite database table
                deleteTask.execute();

                Toast.makeText(getBaseContext(), "All markers are removed", Toast.LENGTH_LONG).show();

            }
        });
    }

    private void drawMarker(LatLng point){
// Creating an instance of MarkerOptions
        MarkerOptions markerOptions = new MarkerOptions();

// Setting latitude and longitude for the marker
        markerOptions.position(point);

// Adding marker on the Google Map
        googleMap.addMarker(markerOptions);
    }

    @Override
    public void onMapReady(@NonNull GoogleMap googleMap) {
        googleMap.addMarker(new MarkerOptions()
                .position(new LatLng(0, 0))
                .title("Marker"));
    }

    private class LocationInsertTask extends AsyncTask<ContentValues, Void, Void> {
        @Override
        protected Void doInBackground(ContentValues... contentValues) {

/** Setting up values to insert the clicked location into SQLite database */
            getContentResolver().insert(LocationsContentProvider.CONTENT_URI, contentValues[0]);
            return null;
        }
    }

    private class LocationDeleteTask extends AsyncTask<Void, Void, Void>{
        @Override
        protected Void doInBackground(Void... params) {

/** Deleting all the locations stored in SQLite database */
            getContentResolver().delete(LocationsContentProvider.CONTENT_URI, null, null);
            return null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
// Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public Loader onCreateLoader(int arg0,
                                 Bundle arg1) {

// Uri to the content provider LocationsContentProvider
        Uri uri = LocationsContentProvider.CONTENT_URI;

// Fetches all the rows from locations table
        return new CursorLoader(this, uri, null, null, null, null);

    }



    @SuppressLint("Range")
    public void onLoadFinished(Loader arg0,
                               Cursor arg1) {
        int locationCount = 0;
        double lat=0;
        double lng=0;
        float zoom=0;

// Number of locations available in the SQLite database table
        locationCount = arg1.getCount();

// Move the current record pointer to the first row of the table
        arg1.moveToFirst();

        for(int i=0;i<locationCount;i++){

// Get the latitude
            lat = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LAT));

// Get the longitude
            lng = arg1.getDouble(arg1.getColumnIndex(LocationsDB.FIELD_LNG));

// Get the zoom level
            zoom = arg1.getFloat(arg1.getColumnIndex(LocationsDB.FIELD_ZOOM));

// Creating an instance of LatLng to plot the location in Google Maps
            LatLng location = new LatLng(lat, lng);

// Drawing the marker in the Google Maps
            drawMarker(location);

// Traverse the pointer to the next row
            arg1.moveToNext();
        }

        if(locationCount>0){
// Moving CameraPosition to last clicked position
            googleMap.moveCamera(CameraUpdateFactory.newLatLng(new LatLng(lat,lng)));

// Setting the zoom level in the map on last position is clicked
            googleMap.animateCamera(CameraUpdateFactory.zoomTo(zoom));

        }
    }

    @Override
    public void onLoaderReset(Loader arg0) {
// TODO Auto-generated method stub

    }
}
