package edu.skku.cs.pa3;

import static java.lang.Thread.sleep;

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import com.naver.maps.geometry.LatLng;
import com.naver.maps.map.CameraUpdate;
import com.naver.maps.map.LocationTrackingMode;
import com.naver.maps.map.MapView;
import com.naver.maps.map.NaverMap;
import com.naver.maps.map.OnMapReadyCallback;
import com.naver.maps.map.overlay.Marker;
import com.naver.maps.map.overlay.OverlayImage;
import com.naver.maps.map.util.FusedLocationSource;

import java.security.Permission;
import java.security.Permissions;
import java.util.ArrayList;

public class MapActivity extends AppCompatActivity implements MySimpleContract.ContractForView, OnMapReadyCallback{
    private View decorView;
    private int	uiOption;
    private MySimplePresenter presenter;
    private MapView mapView;
    private NaverMap naverMap;
    private static final int LOCATION_PERMISSION_REQUEST_CODE = 1000;
    private FusedLocationSource locationSource;
    private Button btn_current, btn_view, btn_near, btn_dark;
    private int dark;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        decorView = getWindow().getDecorView();
        uiOption = getWindow().getDecorView().getSystemUiVisibility();
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.ICE_CREAM_SANDWICH )
            uiOption |= View.SYSTEM_UI_FLAG_HIDE_NAVIGATION;
        if( Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN )
            uiOption |= View.SYSTEM_UI_FLAG_FULLSCREEN;
        decorView.setSystemUiVisibility(uiOption);

        mapView = findViewById(R.id.map_view);
        presenter = new MySimplePresenter(this, new MySimpleModel(presenter, MapActivity.this));

        locationSource = new FusedLocationSource(this, LOCATION_PERMISSION_REQUEST_CODE);
        mapView.onCreate(savedInstanceState);

        btn_current = findViewById(R.id.btn_current);
        btn_near = findViewById(R.id.btn_near);
        btn_view = findViewById(R.id.btn_view);
        btn_dark = findViewById(R.id.btn_dark);
        presenter.setMarkers(naverMap);
        btn_current.setOnClickListener(view -> {
            presenter.onCurrentClicked(naverMap);
        });
        btn_near.setOnClickListener(view -> {
            presenter.onNearClicked(naverMap);
        });
        btn_view.setOnClickListener(view -> {
            presenter.onViewClicked(naverMap);
        });
        btn_dark.setOnClickListener(view -> {
            dark = presenter.onDarkClicked(naverMap);
            if(dark == 1){
                btn_dark.setBackgroundColor(Color.WHITE);
                btn_dark.setTextColor(Color.BLACK);
            }
            else if(dark == 0){
                btn_dark.setBackgroundColor(Color.BLACK);
                btn_dark.setTextColor(Color.WHITE);
            }
        });

        mapView.getMapAsync(this);
    }

    public void onRequestPermissionResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        if(locationSource.onRequestPermissionsResult(
                requestCode, permissions, grantResults)) {
            return;
        }
        super.onRequestPermissionsResult(
                requestCode, permissions, grantResults
        );
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mapView.onLowMemory();
    }
    @Override
    public void onLoginResult(Boolean result) {
        //btn.setText("Value: " + value); // update UI\
    }

    @Override
    public void displayValue(int value) {
        //btn.setText("Value: " + value); // update UI
    }

    @Override
    public void setMarker(Marker marker, double lat, double lon, int resourceID){
        marker.setIconPerspectiveEnabled(true);
        marker.setIcon(OverlayImage.fromResource(resourceID));
        marker.setAlpha(0.8f);
        marker.setPosition(new LatLng(lat, lon));
        marker.setMap(naverMap);
    }

    @Override
    public void onMapReady(@NonNull NaverMap naverMap) {
        this.naverMap = naverMap;
        naverMap.setLocationSource(locationSource);
        //ActivityCompat.requestPermissions(this, PERMISSIONS, LOCATION_PERMISSION_REQUEST_CODE);
        naverMap.setLocationTrackingMode(LocationTrackingMode.Follow);
    }
}
