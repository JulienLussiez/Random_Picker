package com.my.randompicker;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;



public class ListnameActivity extends AppCompatActivity {

	private ListView listview1;

	private double i = 0;
	private String emptyKey = "";

	private ArrayList<String> list = new ArrayList<String>();

	private Intent intent = new Intent();
	private SharedPreferences loadedListname;
	private SharedPreferences backPressed;
	private SharedPreferences deletedListname;
	private SharedPreferences mode;

	private SharedPreferences themeColor;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		themeColor = getSharedPreferences("themeColor", Activity.MODE_PRIVATE);
		switch (themeColor.getString("themeColor", "")) {
			case "":
				break;
			case "red":
				setTheme(R.style.RedTheme);
				break;
			case "pink":
				setTheme(R.style.PinkTheme);
				break;
			case "deepPurple":
				setTheme(R.style.DeepPurpleTheme);
				break;
			case "indigo":
				setTheme(R.style.IndigoTheme);
				break;
			case "blue":
				setTheme(R.style.BlueTheme);
				break;
			case "cyan":
				setTheme(R.style.CyanTheme);
				break;
			case "teal":
				setTheme(R.style.TealTheme);
				break;
			case "green":
				setTheme(R.style.GreenTheme);
				break;
			case "amber":
				setTheme(R.style.AmberTheme);
				break;
			case "orange":
				setTheme(R.style.OrangeTheme);
				break;
			case "deepOrange":
				setTheme(R.style.DeepOrangeTheme);
				break;
			case "brown":
				setTheme(R.style.BrownTheme);
				break;
			case "grey":
				setTheme(R.style.GreyTheme);
				break;
			case "blueGrey":
				setTheme(R.style.BlueGreyTheme);
				break;
			case "deepBlueGrey":
				setTheme(R.style.DeepBlueGreyTheme);
				break;
			case "black":
				setTheme(R.style.BlackTheme);
				break;
		}
		setContentView(R.layout.listname);
		initialize();
		initializeLogic();
	}

	private void  initialize() {
		listview1 = (ListView) findViewById(R.id.listview1);


		loadedListname = getSharedPreferences("loadedListname", Activity.MODE_PRIVATE);
		backPressed = getSharedPreferences("backPressed", Activity.MODE_PRIVATE);
		deletedListname = getSharedPreferences("deletedListname", Activity.MODE_PRIVATE);
		mode = getSharedPreferences("mode", Activity.MODE_PRIVATE);

		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView _parent, View _view, final int _position, long _id) { 
				if (mode.getString("mode", "").equals("load")) {
					loadedListname.edit().putString("loadListname", list.get((int)(_position))).commit();
					intent.setClass(getApplicationContext(), MainActivity.class);
					backPressed.edit().putString("bp", "false").commit();
					mode.edit().putString("mode", "load").commit();
					startActivity(intent);
					finish();
				}
				else {
					if (mode.getString("mode", "").equals("delete")) {
						deletedListname.edit().putString("deletedListname", list.get((int)(_position))).commit();
						deletedListname.edit().putString("position", String.valueOf((long)(_position))).commit();
						intent.setClass(getApplicationContext(), MainActivity.class);
						backPressed.edit().putString("bp", "false").commit();
						mode.edit().putString("mode", "delete").commit();
						startActivity(intent);
						finish();
					}
				}
			}
		});

	}

	private void  initializeLogic() {
		if (mode.getString("mode", "").equals("load")) {
			setTitle(getResources().getString(R.string.load_list));
			listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list));
			i = 1;
			for(int _repeat58 = 0; _repeat58 < (int)(Double.parseDouble(getIntent().getStringExtra("length"))); _repeat58++) {
				list.add(getIntent().getStringExtra(String.valueOf((long)(i))));
				i++;
			}
			((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
		}
		else {
			if (mode.getString("mode", "").equals("delete")) {
				setTitle(getResources().getString(R.string.delete_list));
				listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list));
				i = 1;
				for(int _repeat44 = 0; _repeat44 < (int)(Double.parseDouble(getIntent().getStringExtra("length"))); _repeat44++) {
					list.add(getIntent().getStringExtra(String.valueOf((long)(i))));
					i++;
				}
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
			}
		}
	}

	@Override
	public void onBackPressed() {
				intent.setClass(getApplicationContext(), MainActivity.class);
				backPressed.edit().putString("bp", "true").commit();
				intent.putExtra("backPressed", "true");
				startActivity(intent);
				finish();
	}





	// created automatically
	private void showMessage(String _s) {
		Toast.makeText(getApplicationContext(), _s, Toast.LENGTH_SHORT).show();
	}

	private int getRandom(int _minValue ,int _maxValue){
		Random random = new Random();
		return random.nextInt(_maxValue - _minValue + 1) + _minValue;
	}

	public ArrayList<Double> getCheckedItemPositionsToArray(ListView _list) {
		ArrayList<Double> _result = new ArrayList<Double>();
		SparseBooleanArray _arr = _list.getCheckedItemPositions();
		for (int _iIdx = 0; _iIdx < _arr.size(); _iIdx++) {
			if (_arr.valueAt(_iIdx))
				_result.add((double)_arr.keyAt(_iIdx));
		}
		return _result;
	}

	private float getDip(int _input){
		return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, _input, getResources().getDisplayMetrics());
	}

	private int getDisplayWidthPixels(){
		return getResources().getDisplayMetrics().widthPixels;
	}

	private int getDisplayHeightPixels(){
		return getResources().getDisplayMetrics().heightPixels;
	}


}
