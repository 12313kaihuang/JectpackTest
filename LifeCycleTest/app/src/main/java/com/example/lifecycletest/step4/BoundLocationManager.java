package com.example.lifecycletest.step4;

import android.annotation.SuppressLint;
import android.content.Context;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.util.Log;

import androidx.lifecycle.Lifecycle;
import androidx.lifecycle.LifecycleObserver;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.OnLifecycleEvent;

/**
 * @author hy
 * @Date 2019/10/31 0031
 **/
public class BoundLocationManager {

    public static void bindLocationListenerIn(LifecycleOwner lifecycleOwner,
                                              LocationListener locationListener, Context context) {

        new BoundLocationListener(lifecycleOwner, locationListener, context);
    }

    @SuppressLint("MissingPermission")
    static class BoundLocationListener implements LifecycleObserver {
        private final Context mContext;
        private LocationListener mListener;
        private LocationManager mLocationManager;

        public BoundLocationListener(LifecycleOwner lifecycleOwner,
                                     LocationListener locationListener, Context context) {
            this.mContext = context;
            this.mListener = locationListener;
            lifecycleOwner.getLifecycle().addObserver(this);
        }

        //Call this on resume
        @OnLifecycleEvent(Lifecycle.Event.ON_RESUME)
        void addLocationListener() {
            // Note: 使用来自谷歌Play Services的融合位置提供程序.
            // https://developers.google.com/android/reference/com/google/android/gms/location/FusedLocationProviderApi
            mLocationManager = (LocationManager) mContext.getSystemService(Context.LOCATION_SERVICE);
            mLocationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 0, 0, mListener);
            Log.d("BoundLocationMgr", "Listener added");

            // 使用最后一个位置强制更新(如果可用).
            Location lastLocation = mLocationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            if (lastLocation != null) {
                mListener.onLocationChanged(lastLocation);
            }
        }

        @OnLifecycleEvent(Lifecycle.Event.ON_PAUSE)
        void removeLocationListener() {
            if (mLocationManager == null) {
                return;
            }
            //需要权限android.permission.ACCESS_COARSE_LOCATION
            mLocationManager.removeUpdates(mListener);
            mLocationManager = null;
            Log.d("BoundLocationMgr", "Listener removed");
        }
    }


}
