package com.zab.sanke.adapter;

import java.util.ArrayList;







import com.zab.sanke.R;
import com.zab.sanke.R.id;
import com.zab.sanke.entity.TaskEntity;
import com.zab.sanke.util.AnimUitl;

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.TextView;

public class TaskAdapter extends BaseAdapter {
	private  ArrayList<TaskEntity> data;
	private  Context context;
	private  int resId;
	public TaskAdapter(Context context,ArrayList<TaskEntity> data,int resId) {
		this.data=data;
		this.context=context;
		this.resId=resId;
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
	public void upData(ArrayList<TaskEntity> data){
		this.data=data;
		notifyDataSetChanged();
	}
     private boolean isSelect=false;
	@Override
	public View getView(final int position, View convertView, ViewGroup parent) {
		ViewHolder vh=null;
		if(convertView==null){
			convertView=View.inflate(context, resId, null);
			vh=new ViewHolder();
			vh.btnReceiver=(Button) convertView.findViewById(R.id.btn_task_receive);
			vh.tvContent=(TextView) convertView.findViewById(R.id.tv_task_content);
			vh.tvGold=(TextView) convertView.findViewById(R.id.tv_task_gold);
			convertView.setTag(vh);
		}else{
			vh=(ViewHolder) convertView.getTag();
		}
		TaskEntity task=data.get(position);
		
		
		vh.tvContent.setText(task.getTaskContent());
		vh.tvGold.setText(task.getGold()+"");
		if(task.isGet()){
			vh.btnReceiver.setText("已领取");
			vh.btnReceiver.setTextSize(15);
			vh.btnReceiver.setEnabled(false);
		}else{
			vh.btnReceiver.setText("领取");
			vh.btnReceiver.setTextSize(15);
			if(task.isComplete()){
				vh.btnReceiver.setEnabled(true);
				Animation animation=AnimUitl.getSharAnimation();
				vh.btnReceiver.startAnimation(animation);
			}else{
				vh.btnReceiver.setEnabled(false);
			}
		}
		vh.btnReceiver.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				v.clearAnimation();
				if(listener!=null){
					listener.Receicer(data.get(position),position);
				}
			}
		});
		return convertView;
	}
	private ReceiverListener listener;
	public  void  setOnReceiverListener(ReceiverListener listener){
		this.listener=listener;
	}

	public interface ReceiverListener{
		void Receicer(TaskEntity entity,int position);
	}

	class ViewHolder{
		TextView tvGold;
		TextView tvContent;
		Button btnReceiver;
	}
}
