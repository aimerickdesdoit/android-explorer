package fr.aimerick.explorer.lib;

import java.io.File;

import fr.aimerick.explorer.R;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

public class DirectoryAdapter extends BaseAdapter {
		
	protected LayoutInflater _inflater = null;
	protected DirectoryManager _mananger = null;
	protected int _layoutResourceId = 0;
	protected int _textResourceId = 0;
	protected int _imageResourceId = 0;
	
	public DirectoryAdapter(DirectoryManager mananger, int layoutResourceId, int textResourceId, int imageResourceId) {
		_mananger = mananger;
		_layoutResourceId = layoutResourceId;
		_textResourceId = textResourceId;
		_imageResourceId = imageResourceId;
	}
	
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewGroup layout = (ViewGroup) _getLayoutInflater().inflate(_layoutResourceId, parent, false);
		
		File item = (File) getItem(position);
		
		TextView text = (TextView) layout.findViewById(_textResourceId);			
		String name = item == _mananger.getParentFile() ? ".." : item.getName();
		text.setText(name);

		ImageView image = (ImageView) layout.findViewById(_imageResourceId);
		if (item.isDirectory()) {
			image.setImageResource(R.drawable.directory);
		}
		else {
			image.setImageResource(R.drawable.file);
		}
		
		return layout;
	}

	public int getCount() {
		return _mananger.getCurrentFileChildren().size();
	}

	public Object getItem(int position) {
		return _mananger.getCurrentFileChildren().get(position);
	}

	public long getItemId(int position) {
		return position;
	}
	
	protected LayoutInflater _getLayoutInflater() {
		if (_inflater == null){
			_inflater = _mananger.getActivity().getLayoutInflater();
		}
		return _inflater;
	}
	
}