package com.thao.alarmclock;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.Window;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.TimePicker;

import com.thao.alarmclock.helper.AlarmDBHelper;
import com.thao.alarmclock.model.AlarmModel;
import com.trigg.alarmclock.R;

public class AlarmDetailsActivity extends Activity {

	private AlarmDBHelper dbHelper = new AlarmDBHelper(this);
	private AlarmModel alarmDetails;

	private TimePicker timePicker;
	private EditText edtName;
	private TextView txtToneSelection, txtrepeat, txtsnooze, txttypetone,
			txtvolume;
	private CheckBox chkvibrate;
	private int snoozetime;
	private static final int REQUESTCODE = 1;
	private static final int REQUESTCODE2 = 2;
	private static final int REQUESTCODE3 = 3;
	private static final int REQUESTCODE4 = 4;
	private static final int REQUESTCODETYPE = 5;
	private LinearLayout ringToneContainer;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);

		requestWindowFeature(Window.FEATURE_ACTION_BAR);

		setContentView(R.layout.activity_details);

		getActionBar().setDisplayHomeAsUpEnabled(true);
		chkvibrate = (CheckBox) findViewById(R.id.check_vibrate);
		timePicker = (TimePicker) findViewById(R.id.alarm_details_time_picker);
		edtName = (EditText) findViewById(R.id.alarm_details_name);
		txtToneSelection = (TextView) findViewById(R.id.alarm_label_tone_selection);
		txtrepeat = (TextView) findViewById(R.id.alarm_label_repeat_selection);
		txtsnooze = (TextView) findViewById(R.id.alarm_label_snooze_selection);
		txttypetone = (TextView) findViewById(R.id.alarm_label_typetone_selection);
		txtvolume = (TextView) findViewById(R.id.alarm_label_volume_selection);
		long id = getIntent().getExtras().getLong("id");
		if (id == -1) {
			alarmDetails = new AlarmModel();
			alarmDetails.setRepeatingDay(alarmDetails.MONDAY, true);
			alarmDetails.setRepeatingDay(alarmDetails.TUESDAY, true);
			alarmDetails.setRepeatingDay(alarmDetails.WEDNESDAY, true);
			alarmDetails.setRepeatingDay(alarmDetails.THURSDAY, true);
			alarmDetails.setRepeatingDay(alarmDetails.FRDIAY, true);
			alarmDetails.setRepeatingDay(alarmDetails.SATURDAY, true);
			alarmDetails.setRepeatingDay(alarmDetails.SUNDAY, true);
			txtrepeat.setText("EVERY DAYS");
			alarmDetails.typetone = "Ringtone";
			txttypetone.setText(alarmDetails.typetone);
		} else {
			alarmDetails = dbHelper.getAlarm(id);

			timePicker.setCurrentMinute(alarmDetails.timeMinute);
			timePicker.setCurrentHour(alarmDetails.timeHour);

			edtName.setText(alarmDetails.name);

			txtToneSelection.setText(RingtoneManager.getRingtone(this,
					alarmDetails.alarmTone).getTitle(this));
			snoozetime = alarmDetails.snooze;
			txtsnooze.setText(snoozetime + " minutes");
			chkvibrate.setChecked(alarmDetails.vibrate);
			txtvolume.setText(alarmDetails.volume + "%");
			txttypetone.setText(alarmDetails.typetone);
			updatetext();
		}
		final LinearLayout typeToneContainer = (LinearLayout) findViewById(R.id.alarm_typetone_container);
		typeToneContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getBaseContext(),
						AlarmListTypeTone.class);
				startActivityForResult(intent, REQUESTCODETYPE);
			}
		});
		ringToneContainer = (LinearLayout) findViewById(R.id.alarm_ringtone_container);
		ringToneContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent i = null;
				if (alarmDetails.typetone.equalsIgnoreCase("Ringtone")) {
					i = new Intent(RingtoneManager.ACTION_RINGTONE_PICKER);
					i.putExtra(RingtoneManager.EXTRA_RINGTONE_TITLE,
							"Select Tone");
					i.putExtra(RingtoneManager.EXTRA_RINGTONE_EXISTING_URI,
							(Uri) alarmDetails.alarmTone);
				} else if (alarmDetails.typetone.equalsIgnoreCase("Music")) {
					i = new Intent(Intent.ACTION_GET_CONTENT);
					i.setType("audio/*");
				}
				startActivityForResult(i, REQUESTCODE);
			}
		});
		final LinearLayout repeatContainer = (LinearLayout) findViewById(R.id.alarm_repeat_container);
		repeatContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						RepeatListActivity.class);
				Bundle bundle = new Bundle();
				bundle.putBoolean("WEEKLY", alarmDetails.repeatWeekly);
				bundle.putBoolean("SUNDAY",
						alarmDetails.getRepeatingDay(AlarmModel.SUNDAY));
				bundle.putBoolean("MONDAY",
						alarmDetails.getRepeatingDay(AlarmModel.MONDAY));
				bundle.putBoolean("TUESDAY",
						alarmDetails.getRepeatingDay(AlarmModel.TUESDAY));
				bundle.putBoolean("WEDNESDAY",
						alarmDetails.getRepeatingDay(AlarmModel.WEDNESDAY));
				bundle.putBoolean("THURSDAY",
						alarmDetails.getRepeatingDay(AlarmModel.THURSDAY));
				bundle.putBoolean("FRIDAY",
						alarmDetails.getRepeatingDay(AlarmModel.FRDIAY));
				bundle.putBoolean("SATURDAY",
						alarmDetails.getRepeatingDay(AlarmModel.SATURDAY));
				intent.putExtra("DATAFROMLIST", bundle);
				startActivityForResult(intent, REQUESTCODE2);
			}
		});
		final LinearLayout snoozeContainer = (LinearLayout) findViewById(R.id.alarm_snooze_container);
		snoozeContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						SnoozeAtivity.class);
				startActivityForResult(intent, REQUESTCODE3);
			}
		});
		final LinearLayout vibrate = (LinearLayout) findViewById(R.id.alarm_vibrate_container);
		vibrate.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				if (chkvibrate.isChecked()) {
					chkvibrate.setChecked(false);
				} else {
					chkvibrate.setChecked(true);
				}

			}
		});
		final LinearLayout volumeContainer = (LinearLayout) findViewById(R.id.alarm_volume_container);
		volumeContainer.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent(getApplicationContext(),
						VolumeActivity.class);
				Bundle bundle = new Bundle();
				bundle.putLong("ID", alarmDetails.id);
				bundle.putInt("VOLUME", alarmDetails.volume);
				intent.putExtra("DATA", bundle);
				startActivityForResult(intent, REQUESTCODE4);
			}

		});
	}

	private void updatetext() {
		boolean chkWeekly = alarmDetails.repeatWeekly;
		boolean chkMonday = alarmDetails.getRepeatingDay(AlarmModel.MONDAY);
		boolean chkTuesday = alarmDetails.getRepeatingDay(AlarmModel.TUESDAY);
		boolean chkWednesday = alarmDetails
				.getRepeatingDay(AlarmModel.WEDNESDAY);

		boolean chkThursday = alarmDetails.getRepeatingDay(AlarmModel.THURSDAY);
		boolean chkFriday = alarmDetails.getRepeatingDay(AlarmModel.FRDIAY);
		boolean chkSaturday = alarmDetails.getRepeatingDay(AlarmModel.SATURDAY);
		boolean chkSunday = alarmDetails.getRepeatingDay(AlarmModel.SUNDAY);
		String repeat = "";
		if (chkMonday && chkTuesday && chkWednesday && chkThursday && chkFriday
				&& chkSaturday && chkSunday && chkWeekly) {
			repeat = "EVERY DAYS (WEEKLY)";
		} else if (!chkMonday && !chkTuesday && !chkWednesday && !chkThursday
				&& !chkFriday && !chkSaturday && !chkSunday && !chkWeekly) {
			repeat = "Choose repeat type";
		} else {
			if (chkMonday) {
				repeat = "MON ";
			}
			if (chkTuesday) {
				repeat = repeat + "TUE ";
			}
			if (chkWednesday) {
				repeat = repeat + "WED ";
			}
			if (chkThursday)
				repeat = repeat + "THU ";
			if (chkFriday)
				repeat = repeat + "FRI ";
			if (chkSaturday)
				repeat = repeat + "SAT ";
			if (chkSunday)
				repeat = repeat + "SUN ";
			if (chkWeekly)
				repeat = repeat + "(WEEKLY)";
		}
		txtrepeat.setText(repeat);

	}

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);

		if (resultCode == RESULT_OK) {
			switch (requestCode) {
			case REQUESTCODE: {
				if (alarmDetails.typetone.equalsIgnoreCase("Ringtone")) {
					alarmDetails.alarmTone = data
							.getParcelableExtra(RingtoneManager.EXTRA_RINGTONE_PICKED_URI);
					txtToneSelection.setText(RingtoneManager.getRingtone(this,
							alarmDetails.alarmTone).getTitle(this));
				} else if (alarmDetails.typetone.equalsIgnoreCase("Music")) {
					alarmDetails.alarmTone = data.getData();
					txtToneSelection.setText(getRealPathFromURI(
							getBaseContext(), alarmDetails.alarmTone));
				}
				break;
			}
			default: {
				break;
			}
			}
		}
		if (resultCode == SnoozeAtivity.RESULT_CODE) {
			snoozetime = data.getIntExtra("TIMESOONE", 10);
			txtsnooze.setText(snoozetime + " minutes");
		}
		if (resultCode == RepeatListActivity.RESULT_CODE) {
			Bundle b = data.getBundleExtra("DATA");
			alarmDetails.repeatWeekly = b.getBoolean("WEEKLY");
			alarmDetails.setRepeatingDay(alarmDetails.SUNDAY,
					b.getBoolean("SUNDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.MONDAY,
					b.getBoolean("MONDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.TUESDAY,
					b.getBoolean("TUESDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.WEDNESDAY,
					b.getBoolean("WEDNESDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.THURSDAY,
					b.getBoolean("THURSDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.FRDIAY,
					b.getBoolean("FRIDAY"));
			alarmDetails.setRepeatingDay(alarmDetails.SATURDAY,
					b.getBoolean("SATURDAY"));
			boolean chkWeekly = alarmDetails.repeatWeekly;
			boolean chkMonday = alarmDetails.getRepeatingDay(AlarmModel.MONDAY);
			boolean chkTuesday = alarmDetails
					.getRepeatingDay(AlarmModel.TUESDAY);
			boolean chkWednesday = alarmDetails
					.getRepeatingDay(AlarmModel.WEDNESDAY);

			boolean chkThursday = alarmDetails
					.getRepeatingDay(AlarmModel.THURSDAY);
			boolean chkFriday = alarmDetails.getRepeatingDay(AlarmModel.FRDIAY);
			boolean chkSaturday = alarmDetails
					.getRepeatingDay(AlarmModel.SATURDAY);
			boolean chkSunday = alarmDetails.getRepeatingDay(AlarmModel.SUNDAY);
			String repeat = "";
			if (chkMonday && chkTuesday && chkWednesday && chkThursday
					&& chkFriday && chkSaturday && chkSunday && chkWeekly) {
				repeat = "EVERY DAYS (WEEKLY)";
			} else if (!chkMonday && !chkTuesday && !chkWednesday
					&& !chkThursday && !chkFriday && !chkSaturday && !chkSunday
					&& !chkWeekly) {
				repeat = "Choose repeat type";
			} else {
				if (chkMonday) {
					repeat = "MON ";
				}
				if (chkTuesday) {
					repeat = repeat + "TUE ";
				}
				if (chkWednesday) {
					repeat = repeat + "WED ";
				}
				if (chkThursday)
					repeat = repeat + "THU ";
				if (chkFriday)
					repeat = repeat + "FRI ";
				if (chkSaturday)
					repeat = repeat + "SAT ";
				if (chkSunday)
					repeat = repeat + "SUN ";
				if (chkWeekly)
					repeat = repeat + "(WEEKLY)";
			}
			txtrepeat.setText(repeat);
		}
		if (resultCode == VolumeActivity.RESULT_CODE) {
			int volume = data.getIntExtra("VOLUME", 0);
			txtvolume.setText(volume + "%");
			alarmDetails.volume = volume;
		}
		if (resultCode == AlarmListTypeTone.RESULTCODE) {
			String typetone = data.getStringExtra("TONETYPE");
			txttypetone.setText(typetone);
			alarmDetails.typetone = typetone;
			if (alarmDetails.typetone.equalsIgnoreCase("Silent")) {
				ringToneContainer.setVisibility(View.GONE);
				alarmDetails.alarmTone = null;
			} else {
				ringToneContainer.setVisibility(View.VISIBLE);
			}
		}
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.alarm_details, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {

		switch (item.getItemId()) {
		case android.R.id.home: {
			finish();
			break;
		}
		case R.id.action_save_alarm_details: {
			updateModelFromLayout();

			AlarmManagerHelper.cancelAlarms(this);

			if (alarmDetails.id < 0) {
				dbHelper.createAlarm(alarmDetails);
			} else {
				dbHelper.updateAlarm(alarmDetails);
			}

			AlarmManagerHelper.setAlarms(this);

			setResult(RESULT_OK);
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

	private void updateModelFromLayout() {
		alarmDetails.timeMinute = timePicker.getCurrentMinute().intValue();
		alarmDetails.timeHour = timePicker.getCurrentHour().intValue();
		alarmDetails.name = edtName.getText().toString();
		alarmDetails.snooze = snoozetime;
		alarmDetails.vibrate = chkvibrate.isChecked();
		alarmDetails.isEnabled = true;
	}

	private String getRealPathFromURI(Context context, Uri uri) {
		String fileName = "unknown";// default fileName
		Uri filePathUri = uri;
		if (uri.getScheme().toString().compareTo("content") == 0) {
			Cursor cursor = context.getContentResolver().query(uri, null, null,
					null, null);
			if (cursor.moveToFirst()) {
				int column_index = cursor
						.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
				filePathUri = Uri.parse(cursor.getString(column_index));
				fileName = filePathUri.getLastPathSegment().toString();
			}
		} else if (uri.getScheme().compareTo("file") == 0) {
			fileName = filePathUri.getLastPathSegment().toString();
		} else {
			fileName = fileName + "_" + filePathUri.getLastPathSegment();
		}
		return fileName;
	}
}
