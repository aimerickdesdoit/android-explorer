package fr.aimerick.explorer.lib;

import fr.aimerick.explorer.DirectoryGridActivity;
import fr.aimerick.explorer.DirectoryListActivity;
import fr.aimerick.explorer.R;
import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.GridView;
import android.widget.Toast;

public class DirectorySharedMethods {
	
	protected static final int ACTION_REFRESH = 1;
	protected static final int ACTION_LISTVIEW = 2;
	protected static final int ACTION_GRIDVIEW = 3;
	
	protected Activity _activity = null;
	protected DirectoryManager _manager = null;
	
	protected boolean _back = false;

	public DirectorySharedMethods(Activity activity, DirectoryManager manager) {
		_activity = activity;
		_manager = manager;
	}
	
	public void onCreateOptionsMenu(Menu menu) {
		menu.add(0, ACTION_REFRESH, 0, R.string.action_refresh);
		if (_activity instanceof ListActivity) {
			menu.add(0, ACTION_GRIDVIEW, 0, R.string.action_gridview);
		}
		else {
			menu.add(0, ACTION_LISTVIEW, 0, R.string.action_listview);
		}
	}

	public void onOptionsItemSelected(MenuItem item) {
		Intent intent = null;
		switch (item.getItemId()) {
			case ACTION_REFRESH:
				_manager.resetCurrentFileChildren();
				DirectoryAdapter adapter = null;
				if (_activity instanceof ListActivity) {
					ListActivity list = (ListActivity) _activity;
					adapter = (DirectoryAdapter) list.getListAdapter();
				}
				else {
					GridView grid = (GridView) _activity.findViewById(R.id.directory_grid);
					adapter = (DirectoryAdapter) grid.getAdapter();
				}
				adapter.notifyDataSetChanged();
				break;
			case ACTION_LISTVIEW:
				intent = new Intent(_activity, DirectoryListActivity.class);
				break;
			case ACTION_GRIDVIEW:
				intent = new Intent(_activity, DirectoryGridActivity.class);
				break;
		}
		if (intent != null) {
			intent.putExtra("path", _manager.getCurrentFile().getAbsolutePath());
			_activity.startActivity(intent);
			_activity.overridePendingTransition(0, 0);
		}
	}
	
	public boolean onKeyDown(int keyCode, KeyEvent event) {
	    if (keyCode == KeyEvent.KEYCODE_BACK) {
	    	if (_manager.isRoot()) {
		    	if (!_back) {
		    		_back = true;
					Toast toast = Toast.makeText(_activity, R.string.exit_info, Toast.LENGTH_LONG);
					toast.show();
					return false;
		    	}
		    	else {
		    		_activity.moveTaskToBack(true);
		    		_activity.finish();
		    	}
	    	}
	    	else {
	    		openFileAt(0);
	    	}
	    }
	    return false;
	}

    
	public void openFileAt(int position) {
		_back = false;
		try {
			Intent intent = _manager.getOpenIntent(position);
			if (intent != null) {
				_activity.startActivity(intent);
			}
		}
		catch (DirectoryManagerException e) {
			Toast toast = Toast.makeText(_activity, e.getMessage(), Toast.LENGTH_LONG);
			toast.show();
		}
	}
	
}