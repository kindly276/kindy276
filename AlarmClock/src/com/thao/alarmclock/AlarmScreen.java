package com.thao.alarmclock;

import java.util.Calendar;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.AudioManager;
import android.media.MediaPlayer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.PowerManager.WakeLock;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class AlarmScreen extends Activity {

	public final String TAG = this.getClass().getSimpleName();

	private WakeLock mWakeLock;
	private MediaPlayer mPlayer;
	private static final int WAKELOCK_TIMEOUT = 60 * 1000;
	public AlarmModel alarmmodel;
	private final static int MAX_VOLUME = 100;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		// Setup layout
		this.setContentView(R.layout.activity_alarm_screen);
		final long id = getIntent().getLongExtra(AlarmManagerHelper.ID, 0);
		final String name = getIntent().getStringExtra(AlarmManagerHelper.NAME);
		final int timeHour = getIntent().getIntExtra(AlarmManagerHelper.TIME_HOUR, 0);
		final int timeMinute = getIntent().getIntExtra(
				AlarmManagerHelper.TIME_MINUTE, 0);
		String tone = getIntent().getStringExtra(AlarmManagerHelper.TONE);
		final int snooze = getIntent()
				.getIntExtra(AlarmManagerHelper.SNOOZE, 0);
		final Uri uritone = Uri.parse(tone);
		int volume = getIntent().getIntExtra(AlarmManagerHelper.VOLUME, 100);
		TextView tvName = (TextView) findViewById(R.id.alarm_screen_name);
		tvName.setText(name);
		TextView tvTime = (TextView) findViewById(R.id.alarm_screen_time);
		tvTime.setText(String.format("%02d:%02d", timeHour, timeMinute));
		Button dismissButton = (Button) findViewById(R.id.alarm_screen_button);
		dismissButton.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View view) {
				mPlayer.stop();
				Intent intent = new Intent(AlarmScreen.this,
						AlarmListActivity.class);
				startActivity(intent);
			}
		});
		Button btnsnooze = (Button) findViewById(R.id.alarm_screen_snooze);
		btnsnooze.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				mPlayer.stop();
				Calendar now = Calendar.getInstance();
				int hour = now.get(Calendar.HOUR_OF_DAY);
				int minute = now.get(Calendar.MINUTE);
				alarmmodel = new AlarmModel(id, hour, minute + snooze, uritone,
						name, snooze);
				Intent i = new Intent(AlarmScreen.this, WaitingActivity.class);
				Bundle b=new Bundle();
				b.putInt("SNOOZE",snooze);
				b.putInt("HOUR", timeHour);
				b.putInt("MINUTE",timeMinute );
				i.putExtra("TIMESNOOZE", b);
				startActivity(i);
				AlarmManagerHelper.setSnooze(AlarmScreen.this, alarmmodel);
				finish();
			}
		});

		// Play alarm tone
		mPlayer = new MediaPlayer();
		try {
			if (tone != null && !tone.equals("")) {
				Uri toneUri = Uri.parse(tone);
				if (toneUri != null) {
//					final float setvolume = (float) (1 - (Math.log(MAX_VOLUME
//							- volume) / Math.log(MAX_VOLUME)));
//					mPlayer.setVolume(setvolume, setvolume);
					Toast.makeText(getBaseContext(), "VOLUME"+tone, Toast.LENGTH_LONG).show();
					mPlayer.setDataSource(this, toneUri);
					mPlayer.setAudioStreamType(AudioManager.STREAM_ALARM);
					mPlayer.setLooping(true);
					mPlayer.prepare();
					mPlayer.start();
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		// Ensure wakelock release
		Runnable releaseWakelock = new Runnable() {

			@Override
			public void run() {
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
				getWindow().clearFlags(
						WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

				if (mWakeLock != null && mWakeLock.isHeld()) {
					mWakeLock.release();
				}
			}
		};

		new Handler().postDelayed(releaseWakelock, WAKELOCK_TIMEOUT);
	}

	@SuppressWarnings("deprecation")
	@Override
	protected void onResume() {
		super.onResume();

		// Set the window to keep screen on
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_TURN_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_SHOW_WHEN_LOCKED);
		getWindow().addFlags(WindowManager.LayoutParams.FLAG_DISMISS_KEYGUARD);

		// Acquire wakelock
		PowerManager pm = (PowerManager) getApplicationContext()
				.getSystemService(Context.POWER_SERVICE);
		if (mWakeLock == null) {
			mWakeLock = pm
					.newWakeLock(
							(PowerManager.FULL_WAKE_LOCK
									| PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP),
							TAG);
		}

		if (!mWakeLock.isHeld()) {
			mWakeLock.acquire();
			Log.i(TAG, "Wakelock aquired!!");
		}

	}

	@Override
	protected void onPause() {
		super.onPause();

		if (mWakeLock != null && mWakeLock.isHeld()) {
			mWakeLock.release();
		}
	}
}
