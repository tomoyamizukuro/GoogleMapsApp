package com.websarva.wings.android.googelmapsapp;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;


public class MainActivity extends AppCompatActivity {

	private static final int REQUEST_PERMISSION = 1000;
	final Handler mHandler = new Handler();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);
		mHandler.postDelayed(mSplashTask, 2000);
		if (Build.VERSION.SDK_INT >= 23) {
			checkPermission();
		} else {
			startActivity();
		}
	}

	// 位置情報許可の確認
	public void checkPermission() {
		// 既に許可している
		if (ContextCompat.checkSelfPermission(this,
			Manifest.permission.ACCESS_FINE_LOCATION)
			== PackageManager.PERMISSION_GRANTED) {
			startActivity();
		}
		// 拒否していた場合
		else {
			requestLocationPermission();
		}
	}

	// 許可を求める
	private void requestLocationPermission() {
		if (ActivityCompat.shouldShowRequestPermissionRationale(this,
			Manifest.permission.ACCESS_FINE_LOCATION)) {
			ActivityCompat.requestPermissions(MainActivity.this,
				new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
				REQUEST_PERMISSION);

		} else {
//			toastMake("許可されないとアプリが実行できません", 10, -100);
		}
	}


	// 結果の受け取り
	@Override
	public void onRequestPermissionsResult(int requestCode,
										   @NonNull String[] permissions,
										   @NonNull int[] grantResults) {
		if (requestCode == REQUEST_PERMISSION) {
			// 使用が許可された
			if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
				startActivity();
			} else {
				// それでも拒否された時の対応
//				toastMake("これ以上なにもできません", 0, -200);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
		mHandler.removeCallbacks(mSplashTask);
	}

	private Runnable mSplashTask = new Runnable() {
		@Override
		public void run() {
			startActivity();
			finish();//現在のActivityを削除
		}
	};

	private void startActivity() {
		Intent intent = new Intent(MainActivity.this, MapsActivity.class);//画面遷移のためのIntentを準備
		startActivity(intent);//実際の画面遷移を開始
	}

}
