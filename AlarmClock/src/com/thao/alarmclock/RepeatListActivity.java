package com.thao.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.Window;
import android.view.WindowManager;

import com.trigg.alarmclock.R;

public class RepeatListActivity extends Activity {
	private CustomSwitch chkWeekly;
	private CustomSwitch chkSunday;
	private CustomSwitch chkMonday;
	private CustomSwitch chkTuesday;
	private CustomSwitch chkWednesday;
	private CustomSwitch chkThursday;
	private CustomSwitch chkFriday;
	private CustomSwitch chkSaturday;
	private int width;
	private int height;
	public static final int RESULT_CODE = 2;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		width = dimension.widthPixels;
		height = dimension.heightPixels;
		showAsPopup(RepeatListActivity.this);
		setContentView(R.layout.repeat_list);
		chkWeekly = (CustomSwitch) findViewById(R.id.alarm_details_repeat_weekly);
		chkSunday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_sunday);
		chkMonday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_monday);
		chkTuesday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_tuesday);
		chkWednesday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_wednesday);
		chkThursday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_thursday);
		chkFriday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_friday);
		chkSaturday = (CustomSwitch) findViewById(R.id.alarm_details_repeat_saturday);
		Bundle b = getIntent().getBundleExtra("DATAFROMLIST");
		boolean Weekly = b.getBoolean("WEEKLY");
		boolean Sunday = b.getBoolean("SUNDAY");
		boolean Monday = b.getBoolean("MONDAY");
		boolean Tuesday = b.getBoolean("TUESDAY");
		boolean Wednesday = b.getBoolean("WEDNESDAY");
		boolean Thursday = b.getBoolean("THURSDAY");
		boolean Friday = b.getBoolean("FRIDAY");
		boolean Saturday = b.getBoolean("SATURDAY");
		chkWeekly.setChecked(Weekly);
		chkSunday.setChecked(Sunday);
		chkMonday.setChecked(Monday);
		chkTuesday.setChecked(Tuesday);
		chkWednesday.setChecked(Wednesday);
		chkThursday.setChecked(Thursday);
		chkFriday.setChecked(Friday);
		chkSaturday.setChecked(Saturday);

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
		activity.getWindow().setLayout(width * 9 / 10, height * 9 / 10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_details, menu);
		return true;
	}

	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case R.id.action_save_alarm_details: {
			Intent returnIntent = new Intent();
			Bundle bundle = new Bundle();
			bundle.putBoolean("WEEKLY", chkWeekly.isChecked());
			bundle.putBoolean("SUNDAY", chkSunday.isChecked());
			bundle.putBoolean("MONDAY", chkMonday.isChecked());
			bundle.putBoolean("TUESDAY", chkTuesday.isChecked());
			bundle.putBoolean("WEDNESDAY", chkWednesday.isChecked());
			bundle.putBoolean("THURSDAY", chkThursday.isChecked());
			bundle.putBoolean("FRIDAY", chkFriday.isChecked());
			bundle.putBoolean("SATURDAY", chkSaturday.isChecked());
			returnIntent.putExtra("DATA", bundle);
			setResult(RESULT_CODE, returnIntent);
			finish();
		}
		case R.id.action_cancel_alarm_details: {
			finish();
		}
		}

		return super.onOptionsItemSelected(item);
	}
}
