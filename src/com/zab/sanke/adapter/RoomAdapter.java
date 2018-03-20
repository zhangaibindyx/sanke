package com.zab.sanke.adapter;

import java.util.ArrayList;




import com.zab.sanke.R;
import com.zab.sanke.R.id;
import com.zab.sanke.entity.UserEntity;

import android.content.Context;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class RoomAdapter extends BaseAdapter {
  private Context context;
  private ArrayList<UserEntity> data;
  private int layoutId;
	public RoomAdapter(Context context,ArrayList<UserEntity> data,int layoutId) {
		this.context=context;
		this.data=data;
		this.layoutId=layoutId;
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			vh=new ViewHolder();
			convertView=View.inflate(context, layoutId, null);
			vh.roomName=(TextView) convertView.findViewById(R.id.tv_room_name);
			vh.roomId=(TextView) convertView.findViewById(R.id.tv_room_id);
		   convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		UserEntity u=data.get(position);
		vh.roomName.setText(u.getRoomName());
		vh.roomId.setText("房间号："+u.getRoomId());
		return convertView;
	}

	class ViewHolder{
		TextView roomName;
		TextView roomId;
		Button btnJoin;
	}
	
	
	
	
}
