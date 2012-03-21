package fr.aimerick.explorer;

import fr.aimerick.explorer.lib.DirectoryAdapter;
import fr.aimerick.explorer.lib.DirectoryManager;
import fr.aimerick.explorer.lib.DirectorySharedMethods;
import android.app.ListActivity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ListView;

public class DirectoryListActivity extends ListActivity {
	
	protected DirectoryManager _manager = null;
	protected DirectorySharedMethods _sharedMethods = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        _manager = new DirectoryManager(this);
        _sharedMethods = new DirectorySharedMethods(this, _manager);
        
        setContentView(R.layout.directory_list);
        setListAdapter(new DirectoryAdapter(_manager, R.layout.directory_list_item, R.id.directory_list_label, R.id.directory_list_image));
    }

	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		super.onListItemClick(l, v, position, id);
		_sharedMethods.openFileAt(position);
	}
	
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		return _sharedMethods.onKeyDown(keyCode, event);
	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		super.onCreateOptionsMenu(menu);
		_sharedMethods.onCreateOptionsMenu(menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		super.onOptionsItemSelected(item);
		_sharedMethods.onOptionsItemSelected(item);
		return false;
	}

}