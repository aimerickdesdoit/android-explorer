package fr.aimerick.explorer.lib;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

import fr.aimerick.explorer.R;

import android.app.Activity;
import android.content.ActivityNotFoundException;
import android.content.Intent;
import android.net.Uri;
import android.webkit.MimeTypeMap;

public class DirectoryManager {
	
	protected Activity _activity = null;
	protected File _current = null;
	protected File _parent = null;
	protected ArrayList<File> _children = null;
	
    public DirectoryManager(Activity activity) {
    	_activity = activity;
    	
        String current_path = "/";
        
        if (activity.getIntent().getExtras() != null) {
	        String extra = String.valueOf(activity.getIntent().getExtras().get("path"));
	        if (extra != null) {
	        	current_path = extra;
	        }
        }
        
        _current = new File(current_path);
        _parent = _current.getParentFile();
        if (_current.getAbsolutePath().equals("/")) {
        	_parent = null;
        }
    }
    
    public Activity getActivity() {
    	return _activity;
    }
    
    public File getCurrentFile() {
    	return _current;
    }

    public boolean isRoot() {
    	return getParentFile() == null;
    }
    
    public File getParentFile() {
    	return _parent;
    }
    
    public File getCurrentFileChild(int position) {
		if (getCurrentFileChildren().size() > position) {
			return getCurrentFileChildren().get(position);
		}
		return null;
    }
	
	public ArrayList<File> getCurrentFileChildren() {
		if (_children == null) {
			_children = new ArrayList<File>();
			if (_parent != null) {
				_children.add(_parent);
			}
			File[] files = (_current.listFiles());
			for (File file : files) {
				_children.add(file);
			}
			Collections.sort(_children, new Comparator<File>() {
			    public int compare(File item1, File item2) {
			    	if (item1 == _parent) {
			    		return -1;
			    	}
			    	if (item2 == _parent) {
			    		return 1;
			    	}
			        return item1.getName().compareTo(item2.getName());
			    }
			});
		}
		return _children;
	}
	
	public void resetCurrentFileChildren() {
		_children = null;
	}
	
	public Intent getOpenIntent(int position) throws DirectoryManagerException {
		Intent intent = null;
		
		File item = getCurrentFileChild(position);
		
		if (item.canRead()) {
			if (item.isDirectory()) {
				intent = new Intent(_activity, _activity.getClass());
				intent.putExtra("path", item.getAbsolutePath());
			}
			else {
				try {
					intent = new Intent(android.content.Intent.ACTION_VIEW);
					String extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(item).toString());
					String mimetype = MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
					intent.setDataAndType(Uri.fromFile(item), mimetype);
				}
				catch (ActivityNotFoundException e) {
					String msg = String.format(_activity.getResources().getString(R.string.directory_open_activity_not_found), item.getName());
					throw new DirectoryManagerException(msg);
				}
			}
		}
		else {
			String msg = null;
			if (item.isDirectory()) {
				msg = String.format(_activity.getResources().getString(R.string.directory_read_error_dir), item.getName());
			}
			else {
				msg = String.format(_activity.getResources().getString(R.string.directory_read_error_file), item.getName());
			}
			throw new DirectoryManagerException(msg);
		}
		
		if (intent != null) {
			intent.setFlags(Intent.FLAG_ACTIVITY_NO_ANIMATION);
		}
		
		return intent;
	}

}