package com.thao.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;
import android.widget.SeekBar;
import android.widget.SeekBar.OnSeekBarChangeListener;
import android.widget.TextView;

import com.trigg.alarmclock.R;

public class SnoozeAtivity extends Activity {
	private int width;
	private int height;
	private int time;
	private TextView txttime;
	private SeekBar seekTime;
	public static final int RESULT_CODE = 4;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		width = dimension.widthPixels;
		height = dimension.heightPixels;
		showAsPopup(SnoozeAtivity.this);
		setContentView(R.layout.snooze_settings);
		txttime = (TextView) findViewById(R.id.txtsnooze);
		seekTime = (SeekBar) findViewById(R.id.seek1);
		seekTime.setProgress(10);
		txttime.setText(seekTime.getProgress() + " Minutes");
		seekTime.setOnSeekBarChangeListener(new OnSeekBarChangeListener() {

			@Override
			public void onStopTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				txttime.setText(seekTime.getProgress() + " Minutes");
			}

			@Override
			public void onStartTrackingTouch(SeekBar seekBar) {
				// TODO Auto-generated method stub
				txttime.setText(seekTime.getProgress() + " Minutes");
			}

			@Override
			public void onProgressChanged(SeekBar seekBar, int progress,
					boolean fromUser) {
				// TODO Auto-generated method stub
				txttime.setText(seekTime.getProgress() + " Minutes");
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
			intent.putExtra("TIMESOONE", seekTime.getProgress());
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
