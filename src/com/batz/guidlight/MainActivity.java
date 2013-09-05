package com.batz.guidlight;

import android.hardware.Camera;
import android.hardware.Camera.Parameters;
import android.os.Bundle;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.ImageButton;

public class MainActivity extends Activity {

	private boolean isLighOn = false;
	private Camera camera;
	private ImageButton button;

	@Override
	protected void onStop() {
		super.onStop();

		if (camera != null) {
			camera.release();
		}
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.main);

		button = (ImageButton) findViewById(R.id.buttonFlashlight);
		Context context = this;
		PackageManager pm = context.getPackageManager();

		// if device support camera?
		if (!pm.hasSystemFeature(PackageManager.FEATURE_CAMERA)) {
			Log.e("err", "Device has no camera!");
			return;
		}

//		camera = Camera.open();
//		final Parameters p = camera.getParameters();

		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				
				if(camera == null){
					camera = Camera.open();
				}
				
				final Parameters p = camera.getParameters();

				if (isLighOn) {

					Log.i("info", "torch is turn off!");
					button.setBackgroundResource(R.drawable.on);
					isLighOn = false;
					p.setFlashMode(Parameters.FLASH_MODE_OFF);
					camera.setParameters(p);
					camera.stopPreview();
					//camera.release();
					
					

				} else {

					Log.i("info", "torch is turn on!");
					button.setBackgroundResource(R.drawable.off);
					p.setFlashMode(Parameters.FLASH_MODE_TORCH);
					camera.setParameters(p);
					camera.startPreview();
					isLighOn = true;
					

				}

			}
		});

	}

	@Override
	protected void onPause() {
		super.onPause();
		try {
			camera.stopPreview();
			camera.setPreviewCallback(null);
			camera.release();
			camera = null;

		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	@Override
	protected void onResume() {
		super.onResume();
		try {
			camera = Camera.open();
			camera.setPreviewCallback(null);
		} catch (Exception e) {

		}
	}

}
