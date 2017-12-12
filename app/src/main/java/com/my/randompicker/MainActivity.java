package com.my.randompicker;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.SparseBooleanArray;
import android.util.TypedValue;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Random;

public class MainActivity extends AppCompatActivity {

    final Context context = this;

	private ListView listview1;
	private EditText edit_item;
	private EditText edit_listdata;

    private Button red;

	private double i = 0;
	private double length = 0;
	private String listname = "";
	private String empty = "";
	private double loadOn = 0;
    private double deleteDisabled = 0;

	private ArrayList<String> list = new ArrayList<String>();
	private ArrayList<String> selection = new ArrayList<String>();

	private SharedPreferences f;
	private Intent intent = new Intent();
	private SharedPreferences loadedListname;
	private SharedPreferences backPressed;
	private SharedPreferences deletedListname;
	private SharedPreferences mode;
	private SharedPreferences listnameList;
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
        setContentView(R.layout.main);
        initialize();
        initializeLogic();
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

    public boolean onOptionsItemSelected(MenuItem item) {
        //On regarde quel item a été cliqué grâce à son id et on déclenche une action
        switch (item.getItemId()) {
            case R.id.new_list:
                if (list.size() == 0) {
                    edit_listdata.setText("");
                    edit_listdata.setVisibility(View.GONE);
                    edit_item.setVisibility(View.VISIBLE);
					FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
					fabButton.setImageResource(R.drawable.ic_shuffle_black_24dp);
                }
                else {
                    edit_listdata.setVisibility(View.GONE);
                    edit_item.setVisibility(View.VISIBLE);
                    edit_listdata.setText("");
                    list.clear();
                    ((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
                    setTitle(getResources().getString(R.string.new_list));
					FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
					fabButton.setImageResource(R.drawable.ic_shuffle_black_24dp);
                }
                return true;
            case R.id.save_list:
                if (list.size() == 0) {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), getResources().getString(R.string.at_least_one), Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }
                    else {
                        showMessage(getResources().getString(R.string.at_least_one));
                    }
                }
                else {
                    if (edit_listdata.getText().toString().equals("")) {
                        setTitle(getResources().getString(R.string.save_list));
                        edit_listdata.setVisibility(View.VISIBLE);
                        edit_item.setVisibility(View.GONE);
                        i = 1;
                        deleteDisabled = 1;
                        FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
                        fabButton.setImageResource(R.drawable.ic_clear_black_24dp);
                    }
                    else {
                        _removeData();
                        _saveData();
                        _compareListName();
                        if (i == 1) {
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), getResources().getString(R.string.list_updated), Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            }
                            else {
                                showMessage(getResources().getString(R.string.list_updated));
                            }
                        }
                        else {
                            _saveSelection();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), getResources().getString(R.string.list_saved), Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                            }
                            else {
                                showMessage(getResources().getString(R.string.list_saved));
                            }

                        }
                        deleteDisabled = 0;
                        i = 0;
                        edit_listdata.setVisibility(View.GONE);
                        edit_item.setVisibility(View.VISIBLE);
                    }
                }
                return true;
            case R.id.load_list:
                i = 1;
                intent.setClass(getApplicationContext(), ListnameActivity.class);
                intent.putExtra("length", String.valueOf((long)(selection.size())));
                mode.edit().putString("mode", "load").commit();
                for(int _repeat14 = 0; _repeat14 < (int)(selection.size()); _repeat14++) {
                    intent.putExtra(String.valueOf((long)(i)), selection.get((int)(i - 1)));
                    i++;
                }
                startActivity(intent);
                finish();
                return true;
            case R.id.about:
                AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                alertDialog.setTitle(getResources().getString(R.string.about));
                alertDialog.setMessage(getResources().getString(R.string.about_text));
                alertDialog.show();
                return true;
            case R.id.delete_list:
                i = 1;
                intent.setClass(getApplicationContext(), ListnameActivity.class);
                intent.putExtra("length", String.valueOf((long)(selection.size())));
                mode.edit().putString("mode", "delete").commit();
                for(int _repeat28 = 0; _repeat28 < (int)(selection.size()); _repeat28++) {
                    intent.putExtra(String.valueOf((long)(i)), selection.get((int)(i - 1)));
                    i++;
                }
                startActivity(intent);
                finish();
                return true;
			case R.id.theme:
                final Dialog dialog = new Dialog(context);
                dialog.setContentView(R.layout.custom);
                Button red = (Button) dialog.findViewById(R.id.red);
                Button pink = (Button) dialog.findViewById(R.id.pink);
                Button deepPurple = (Button) dialog.findViewById(R.id.deepPurple);
                Button indigo = (Button) dialog.findViewById(R.id.indigo);
                Button blue = (Button) dialog.findViewById(R.id.blue);
                Button cyan = (Button) dialog.findViewById(R.id.cyan);
                Button teal = (Button) dialog.findViewById(R.id.teal);
                Button green = (Button) dialog.findViewById(R.id.green);
                Button amber = (Button) dialog.findViewById(R.id.amber);
                Button orange = (Button) dialog.findViewById(R.id.orange);
                Button deepOrange = (Button) dialog.findViewById(R.id.deepOrange);
                Button brown = (Button) dialog.findViewById(R.id.brown);
                Button grey = (Button) dialog.findViewById(R.id.grey);
                Button blueGrey = (Button) dialog.findViewById(R.id.blueGrey);
                Button deepBlueGrey = (Button) dialog.findViewById(R.id.deepBlueGrey);
                Button black = (Button) dialog.findViewById(R.id.black);
                red.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "red").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                pink.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "pink").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                deepPurple.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "deepPurple").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                indigo.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "indigo").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                blue.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "blue").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                cyan.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "cyan").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                teal.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "teal").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                green.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "green").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                amber.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "amber").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                orange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "orange").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                deepOrange.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "deepOrange").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                brown.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "brown").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                grey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "grey").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                blueGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "blueGrey").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                deepBlueGrey.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "deepBlueGrey").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                black.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        themeColor.edit().putString("themeColor", "black").apply();
                        finish();
                        overridePendingTransition(0, 0);
                        startActivity(getIntent());
                        overridePendingTransition(0, 0);
                    }
                });
                dialog.show();
				return true;
        }
        return false;}

	private void  initialize() {
		listview1 = (ListView) findViewById(R.id.listview1);
		edit_item = (EditText) findViewById(R.id.edit_item);
		edit_listdata = (EditText) findViewById(R.id.edit_listdata);

		f = getSharedPreferences("lists", Activity.MODE_PRIVATE);

		loadedListname = getSharedPreferences("loadedListname", Activity.MODE_PRIVATE);
		backPressed = getSharedPreferences("backPressed", Activity.MODE_PRIVATE);
		deletedListname = getSharedPreferences("deletedListname", Activity.MODE_PRIVATE);
		mode = getSharedPreferences("mode", Activity.MODE_PRIVATE);
		listnameList = getSharedPreferences("listnameList", Activity.MODE_PRIVATE);

        final FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Click action
                if (deleteDisabled == 1) {
                    edit_listdata.setVisibility(View.GONE);
                    edit_item.setVisibility(View.VISIBLE);
                    fab.setImageResource(R.drawable.ic_shuffle_black_24dp);
                    deleteDisabled = 0;
                    setTitle(getResources().getString(R.string.new_list));
                }
                else {
                    if (list.size() == 0) {

                    }
                    else {
//                        showMessage(list.get((int)(getRandom((int)(0), (int)(list.size() - 1)))));
//                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), list.get((int)(getRandom((int)(0), (int)(list.size() - 1)))), Snackbar.LENGTH_SHORT);
                        AlertDialog alertDialog = new AlertDialog.Builder(MainActivity.this).create();
                        alertDialog.setTitle(getResources().getString(R.string.random_pick));
                        alertDialog.setMessage(list.get((int)(getRandom((int)(0), (int)(list.size() - 1)))));
//                        alertDialog.setButton(AlertDialog.BUTTON_NEUTRAL, "OK",
//                                new DialogInterface.OnClickListener() {
//                                    public void onClick(DialogInterface dialog, int which) {
//                                        dialog.dismiss();
//                                    }
//                                });
                        alertDialog.show();
//                        mySnackbar.show();
                    }
                }
            }
        });
		listview1.setOnItemClickListener(new AdapterView.OnItemClickListener() {
			@Override
			public void onItemClick(AdapterView _parent, View _view, final int _position, long _id) {
				if (deleteDisabled == 0) {
					list.remove((int) (_position));
					((BaseAdapter) listview1.getAdapter()).notifyDataSetChanged();
				}
				else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                        Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "Save or cancel to delete this item", Snackbar.LENGTH_SHORT);
                        mySnackbar.show();
                    }
                    else {
                        showMessage("Save or cancel to delete this item");
                    }
                }
			}
		});
		edit_listdata.addTextChangedListener(new TextWatcher() {
			@Override
			public void beforeTextChanged(CharSequence _text, int _start, int _count, int _after) {
			}
			@Override
			public void onTextChanged(final CharSequence _charSeq, int _start, int _before, int _count) {
				listname = _charSeq.toString();
			}
			@Override
			public void afterTextChanged(Editable _text) {
			}
		});
		edit_item.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				if (edit_item.getText().toString().equals("")) {

				}
				else {
					list.add(edit_item.getText().toString());
					f.edit().putString(listname.concat(String.valueOf((long)(list.size()))), edit_item.getText().toString()).commit();
					listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list));
					((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
					edit_item.setText("");
				}
			}
		});
		edit_listdata.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View _v) {
				if (edit_listdata.getText().toString().equals("")) {

				}
				else {
					_removeData();
					_saveData();
					_compareListName();
					if (i == 1) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), "List Updated", Snackbar.LENGTH_SHORT);
                            mySnackbar.show();
                        }
                        else {
                            showMessage("List Updated");
                        }
						setTitle(edit_listdata.getText().toString());
					}
					else {
						_saveSelection();
						setTitle(edit_listdata.getText().toString());
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                            Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), getResources().getString(R.string.list_saved), Snackbar.LENGTH_SHORT);
                            mySnackbar.show();
                        }
                        else {
                            showMessage("List Saved");
                        }
					}
					i = 0;
					edit_listdata.setVisibility(View.GONE);
					edit_item.setVisibility(View.VISIBLE);
                    FloatingActionButton fabButton = (FloatingActionButton) findViewById(R.id.fab);
                    fabButton.setImageResource(R.drawable.ic_shuffle_black_24dp);
				}
			}
		});

	}

	private void  initializeLogic() {
		i = 1;
		edit_listdata.setVisibility(View.GONE);
		_refreshData();
		if (backPressed.getString("bp", "").equals("")) {
            setTitle(getResources().getString(R.string.new_list));
		}
		else {
			if (backPressed.getString("bp", "").equals("true")) {
                setTitle(getResources().getString(R.string.new_list));
			}
			else {
				if (mode.getString("mode", "").equals("")) {
					setTitle(getResources().getString(R.string.new_list));
			}
				else {
					if (mode.getString("mode", "").equals("load")) {
						if (!loadedListname.getString("loadListname", "").equals("")) {
							listname = loadedListname.getString("loadListname", "");
							edit_listdata.setText(loadedListname.getString("loadListname", ""));
							_loadData();
                            mode.edit().putString("mode", "").commit();
                            setTitle(loadedListname.getString("loadListname", ""));
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), getResources().getString(R.string.list_loaded), Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                                mySnackbar.addCallback(new Snackbar.Callback() {
                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        if (event == DISMISS_EVENT_TIMEOUT) {
                                        }
                                    }
                                });
                            }
                            else {
                                showMessage("List loaded");
                            }
						}
					}
					else {
						if (!deletedListname.getString("deletedListname", "").equals("")) {
							listname = deletedListname.getString("deletedListname", "");
							edit_listdata.setText(deletedListname.getString("deletedListname", ""));
							_deleteList();
                            setTitle(getResources().getString(R.string.new_list));
                            edit_listdata.setText("");
                            mode.edit().putString("mode", "").commit();
                            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                Snackbar mySnackbar = Snackbar.make(findViewById(R.id.myCoordinatorLayout), getResources().getString(R.string.list_deleted), Snackbar.LENGTH_SHORT);
                                mySnackbar.show();
                                mySnackbar.addCallback(new Snackbar.Callback() {

                                    @Override
                                    public void onDismissed(Snackbar snackbar, int event) {
                                        if (event == DISMISS_EVENT_TIMEOUT) {
                                        }
                                    }
                                });
                            }
                            else {
                                showMessage("List deleted");
                            }

						}
					}
				}
			}
		}
	}


	private void _removeData () {
		i = 1;
		while(true) {
			if (!f.getString(listname.concat(String.valueOf((long)(i))), "").equals("")) {
				f.edit().remove(listname.concat(String.valueOf((long)(i)))).commit();
				i++;
			}
			else {
				f.edit().remove(listname.concat(String.valueOf((long)(i)))).commit();
				break;
			}
		}
	}
	private void _saveData () {
		i = 1;
		while(true) {
			if (!(i == list.size())) {
				f.edit().putString(listname.concat(String.valueOf((long)(i))), list.get((int)(i - 1))).commit();
				i++;
			}
			else {
				f.edit().putString(listname.concat(String.valueOf((long)(i))), list.get((int)(i - 1))).commit();
				break;
			}
		}
	}
	private void _saveSelection () {
		i = 1;
		selection.add(edit_listdata.getText().toString());
		while(true) {
			if (!(i == selection.size())) {
				f.edit().putString("listkey".concat(String.valueOf((long)(i))), selection.get((int)(i - 1))).commit();
				i++;
			}
			else {
				f.edit().putString("listkey".concat(String.valueOf((long)(i))), selection.get((int)(i - 1))).commit();
				break;
			}
		}
	}
	private void _compareListName () {
		i = 0;
		for(int _repeat11 = 0; _repeat11 < (int)(selection.size()); _repeat11++) {
			if (!edit_listdata.getText().toString().equals(selection.get((int)(i)))) {
				i++;
			}
		}
		if (i == selection.size()) {
			i = 0;
		}
		else {
			i = 1;
		}
	}
	private void _loadData () {
		i = 1;
		list.clear();
		while(true) {
			if (!f.getString(listname.concat(String.valueOf((long)(i))), "").equals("")) {
				list.add(f.getString(listname.concat(String.valueOf((long)(i))), ""));
				i++;
			}
			else {
				listview1.setAdapter(new ArrayAdapter<String>(getBaseContext(), android.R.layout.simple_list_item_1, list));
				((BaseAdapter)listview1.getAdapter()).notifyDataSetChanged();
				break;
			}
		}
		i = 1;
	}
	private void _deleteList () {
		i = 1;
		_removeData();
		selection.remove((int)(Double.parseDouble(deletedListname.getString("position", ""))));
		list.clear();
		i = 1;
		while(true) {
			if (!f.getString("listkey".concat(String.valueOf((long)(i))), "").equals("")) {
				f.edit().remove("listkey".concat(String.valueOf((long)(i)))).commit();
				i++;
			}
			else {
				f.edit().remove("listkey".concat(String.valueOf((long)(i)))).commit();
				f.edit().remove("listkey".concat("1")).commit();
				break;
			}
		}
		i = 1;
		if (!(selection.size() == 0)) {
			while(true) {
				if (!(i == selection.size())) {
					f.edit().putString("listkey".concat(String.valueOf((long)(i))), selection.get((int)(i - 1))).commit();
					i++;
				}
				else {
					f.edit().putString("listkey".concat(String.valueOf((long)(i))), selection.get((int)(i - 1))).commit();
					break;
				}
			}
		}
	}
	private void _refreshData () {
		i = 1;
		selection.clear();
		while(true) {
			if (f.getString("listkey".concat(String.valueOf((long)(i))), "").equals("")) {
				break;
			}
			else {
				selection.add(f.getString("listkey".concat(String.valueOf((long)(i))), ""));
				i++;
			}
		}
		setTitle(f.getString("listkey".concat(String.valueOf((long)(i))), ""));
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
