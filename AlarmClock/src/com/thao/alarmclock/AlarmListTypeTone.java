package com.thao.alarmclock;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.DisplayMetrics;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.trigg.alarmclock.R;

public class AlarmListTypeTone extends Activity {
	private int width;
	private int height;
	private String[] type = new String[] { "Silent", "Ringtone", "Music" };
	public static final int RESULTCODE = 3;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		DisplayMetrics dimension = new DisplayMetrics();
		getWindowManager().getDefaultDisplay().getMetrics(dimension);
		width = dimension.widthPixels;
		height = dimension.heightPixels;
		showAsPopup(AlarmListTypeTone.this);
		setContentView(R.layout.acitivy_tonetype);
		ListView lv = (ListView) findViewById(R.id.lvtypetone);
		ArrayAdapter<String> adapter = new ArrayAdapter<String>(
				getBaseContext(),
				android.R.layout.simple_list_item_single_choice, type);
		lv.setAdapter(adapter);
		lv.setChoiceMode(ListView.CHOICE_MODE_SINGLE);
		lv.setItemChecked(1, true);
		lv.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Intent i = new Intent();
				i.putExtra("TONETYPE", type[position].toString());
				setResult(RESULTCODE, i);
				finish();
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
		activity.getWindow().setLayout(width * 9 / 10, height * 6 / 10);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu_waiting, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		switch (item.getItemId()) {
		case R.id.action_cancel_snooze_details: {
			finish();
			break;
		}
		}
		return super.onOptionsItemSelected(item);
	}
}
