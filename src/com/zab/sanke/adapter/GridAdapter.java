package com.zab.sanke.adapter;

import com.zab.sanke.MyApplication;
import com.zab.sanke.R;
import com.zab.sanke.R.id;

import android.app.ActionBar.LayoutParams;
import android.app.Activity;
import android.content.Context;
import android.util.DisplayMetrics;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

public class GridAdapter extends BaseAdapter {
	private Context context;
	private int layoutId;
	private int [] data;
	private int [] dataTag;
	public GridAdapter(Context  context,int layoutId,int [] data) {
		this.data=data;
		this.layoutId=layoutId;
		this.context=context;
		dataTag=MyApplication.instace.getShopingTag();
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return data==null?0:data.length;
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return data[position];
	}

	@Override
	public long getItemId(int position) {
		return position;
	}
	private boolean isShopping=true;
	public   void setIsShopping(boolean isShoping){
		this.isShopping=isShoping;
	}
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ImageView iv=null;
		Button btn=null;
		if(convertView==null){
			convertView=View.inflate(context, layoutId, null);
			DisplayMetrics dm = new DisplayMetrics();
            ((Activity) context).getWindowManager().getDefaultDisplay().getMetrics(dm);
            int height = dm.heightPixels ;//高度
            convertView.setLayoutParams(new LayoutParams(LayoutParams.MATCH_PARENT,height/3));
		}
		iv=	(ImageView) convertView.findViewById(R.id.gv_iv);
		btn=(Button) convertView.findViewById(R.id.gv_btn);
		iv.setImageResource(data[position]);
        for (int i = 0; i < dataTag.length; i++) {
			if(dataTag[i]==position||isShopping){
				btn.setText("已购买");
				if(isShopping){
					btn.setText("使用");
				}
				return convertView;
			}
			
        }
		btn.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				boolean b=MyApplication.instace.getGold()>=1888;
				if(b){
					listener.shoppingListener(position);
					Toast.makeText(context, "购买成功", 1).show();
				}else{
					Toast.makeText(context, "购买失败,金币不足", 1).show();
				}
			}
		});
		
		return convertView;
	}
	private OnShoppingListener listener;
	public void setOnShoppingListener(OnShoppingListener listener){
		this.listener=listener;
	}
	public interface OnShoppingListener{
		void shoppingListener(int position);
	}
}
