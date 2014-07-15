package com.thao.alarmclock;

import com.trigg.alarmclock.R;

import android.app.Activity;
import android.content.Intent;
import android.net.VpnService;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.SeekBar.OnSeekBarChangeListener;

public class VolumeActivity extends Activity {
	private int width;
	private int height;
	private int time;
	private TextView txtvolume;
	private SeekBar seekTime;
	public static final int RESULT_CODE = 5;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		width = dimension.widthPixels;
		height = dimension.heightPixels;
		showAsPopup(VolumeActivity.this);
		setContentView(R.layout.volume_settings);
		txtvolume = (TextView) findViewById(R.id.txtvolume);
		seekTime = (SeekBar) findViewById(R.id.seek2);
		Bundle b = getIntent().getBundleExtra("DATA");
		long id = b.getLong("ID");
		int volume = b.getInt("VOLUME");
		if (id >= 0) {
			seekTime.setProgress(volume);
		} else {
			seekTime.setProgress(100);
			volume=100;
		}

		txtvolume.setText(volume + "%");
		seekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				txtvolume.setText(seekTime.getProgress() + "%");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				txtvolume.setText(seekTime.getProgress() + "%");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				txtvolume.setText(seekTime.getProgress() + " %");
			}
		});
	}

	private void showAsPopup(Activity activity) {
		activity.requestWindowFeature(Window.FEATURE_ACTION_BAR);
		activity.getWindow().setFlags(
				WindowManager.LayoutParams.FLAG_DIM_BEHIND,
				WindowManager.LayoutParams.FLAG_DIM_BEHIND);
		android.view.WindowManager.LayoutParams params = activity.getWindow()
				.getAttributes();
		params.alpha = 1.0f;
		params.dimAmount = 0f;
		activity.getWindow().setAttributes(
				(android.view.WindowManager.LayoutParams) params);
		activity.getWindow().setLayout(width * 9 / 10, height * 5 / 10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_save_alarm_details: {
			Intent intent = new Intent();
			intent.putExtra("VOLUME", seekTime.getProgress());
			setResult(RESULT_CODE, intent);
			finish();
			break;
		}
		case R.id.action_cancel_alarm_details: {
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}
}
