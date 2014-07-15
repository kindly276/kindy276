package com.thao.alarmclock;

import android.app.Activity;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import com.trigg.alarmclock.R;

public class WaitingActivity extends Activity {
	private long soMiliGiay;
	CountDownTimer demThoiGian;
	private TextView txttime, txtmaintime;
	private int snozee;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.waiting_activity);
		txttime = (TextView) findViewById(R.id.txttime);
		txtmaintime = (TextView) findViewById(R.id.txtsnoooze);
		Bundle b = getIntent().getBundleExtra("TIMESNOOZE");
		snozee = b.getInt("SNOOZE");
		int hour = b.getInt("HOUR");
		int minute = b.getInt("MINUTE");
		txtmaintime.setText("Snooze for: " + hour + ":" + minute);
		soMiliGiay = (snozee * 60000) - 3000;
		thoigian();
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_waiting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		if (item.getItemId() == R.id.action_cancel_snooze_details) {

		}
		return super.onOptionsItemSelected(item);
	}

	public void thoigian() {
		demThoiGian = new CountDownTimer(soMiliGiay, 1000) {
			@Override
			public void onTick(long millisUntilFinished) {
				long phut = (millisUntilFinished / 1000) / 60;
				long giay = (millisUntilFinished / 1000) % 60;
				if ((phut >= 10) && (giay >= 10)) {
					txttime.setText("00:" + phut + ":" + giay + " ");
				} else if ((phut < 10) && (giay >= 10)) {
					txttime.setText("00:0" + phut + ":" + giay + " ");
				} else if ((phut < 10) && (giay < 10)) {
					txttime.setText("00:0" + phut + ":0" + giay + " ");
				}
			}

			@Override
			public void onFinish() {
				demThoiGian.cancel();
			}
		};
		demThoiGian.start();
	}
}
