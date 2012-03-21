package fr.aimerick.explorer;

import fr.aimerick.explorer.lib.DirectoryAdapter;
import fr.aimerick.explorer.lib.DirectoryManager;
import fr.aimerick.explorer.lib.DirectorySharedMethods;
import android.app.Activity;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.GridView;

public class DirectoryGridActivity extends Activity {
	
	protected DirectoryManager _manager = null;
	protected DirectorySharedMethods _sharedMethods = null;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        
        _manager = new DirectoryManager(this);
        _sharedMethods = new DirectorySharedMethods(this, _manager);
        
        setContentView(R.layout.directory_grid);
        
        GridView grid = (GridView) findViewById(R.id.directory_grid);
        grid.setAdapter(new DirectoryAdapter(_manager, R.layout.directory_grid_item, R.id.directory_grid_label, R.id.directory_grid_image));
        
        grid.setOnItemClickListener(new OnItemClickListener() {
			
        	public void onItemClick(AdapterView<?> parent, View v, int position, long id) {
        		_sharedMethods.openFileAt(position);
			}
			
		});
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