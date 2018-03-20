package com.zab.sanke.view;


import android.annotation.SuppressLint;
import android.content.Context;
import android.util.AttributeSet;
import android.util.MonthDisplayHelper;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnTouchListener;
import android.widget.ImageButton;

@SuppressLint({ "NewApi", "ClickableViewAccessibility" })
public class SpeedButton extends ImageButton {

	public SpeedButton(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public SpeedButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	public SpeedButton(Context context, AttributeSet attrs, int defStyleAttr) {
		super(context, attrs, defStyleAttr);
		// TODO Auto-generated constructor stub
	}

	public SpeedButton(Context context, AttributeSet attrs, int defStyleAttr,
			int defStyleRes) {
		super(context, attrs, defStyleAttr, defStyleRes);
		// TODO Auto-generated constructor stub
	}


	private GameClickListener listener;
	public void setOnGameClickListener(GameClickListener listener){
		this.listener=listener;
		setOnTouchListener(new ButtonListener());
	}

	public interface GameClickListener{
		void onClickDown(View v);
		void onClickUp(View v);
	}

	@SuppressLint("ClickableViewAccessibility")
	public class ButtonListener implements OnTouchListener{  
		  
		  
        public boolean onTouch(View v, MotionEvent event) {  
                if(event.getAction() == MotionEvent.ACTION_UP){  
                    listener.onClickUp(v);
                }   
                if(event.getAction() == MotionEvent.ACTION_DOWN){  
                    listener.onClickDown(v);
                }  
            return false;  
        }  
          
    }  





}
