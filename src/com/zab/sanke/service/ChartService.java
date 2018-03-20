package com.zab.sanke.service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

public class ChartService extends Service {

	public ChartService() {
	         
	}

	@Override
	public IBinder onBind(Intent intent) {
		return null;
	}
	
	
}
