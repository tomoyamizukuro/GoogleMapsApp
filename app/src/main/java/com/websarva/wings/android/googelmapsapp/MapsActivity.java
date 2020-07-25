package com.websarva.wings.android.googelmapsapp;

import android.location.Location;
import android.os.Bundle;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

	private GoogleMap mMap;
	private FusedLocationProviderClient mFusedLocationClient;
	private Location mLastLocation;
	private SettingsClient mSettingsClient;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_maps);
		// Obtain the SupportMapFragment and get notified when the map is ready to be used.
		SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
			.findFragmentById(R.id.map);
		mapFragment.getMapAsync(this);
	}


	/**
	 * Manipulates the map once available.
	 * This callback is triggered when the map is ready to be used.
	 * This is where we can add markers or lines, add listeners or move the camera. In this case,
	 * we just add a marker near Sydney, Australia.
	 * If Google Play services is not installed on the device, the user will be prompted to install
	 * it inside the SupportMapFragment. This method will only be triggered once the user has
	 * installed Google Play services and returned to the app.
	 */
	public void onMapReady(GoogleMap googleMap) {
		mMap = googleMap;
		double lat = 0;
		double lng = 0;
		mMap.setMyLocationEnabled(true);
		getLastLocation();
		if (mLastLocation != null) {
			lat = mLastLocation.getLatitude();
			lng = mLastLocation.getLongitude();
		} else {
			getLastLocation();
			setLocationMarker(lat, lng);
		}
		// Add a marker in Sydney and move the camera
		setLocationMarker(lat, lng);
	}

	private void setLocationMarker(double lat, double lng) {
		LatLng sydney = new LatLng(lat, lng);
		mMap.addMarker(new MarkerOptions().position(sydney).title("Marker in Sydney"));
		mMap.moveCamera(CameraUpdateFactory.newLatLng(sydney));
	}

	private void getLastLocation() {
		mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
		mSettingsClient = LocationServices.getSettingsClient(this);
		LocationRequest locationRequest = new LocationRequest();
		locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
		mFusedLocationClient.getLastLocation()
			.addOnCompleteListener(this, new OnCompleteListener<Location>() {
				@Override
				public void onComplete(@NonNull Task<Location> task) {
					if (task.isSuccessful() && task.getResult() != null) {
						mLastLocation = task.getResult();
						Log.d("GET LOCATION", "lat = " + mLastLocation.getLatitude() + ", lon = " + mLastLocation.getLongitude());
					} else {
						Log.w("failed location", "getLastLocation:exception", task.getException());
					}
				}
			});
	}
}
