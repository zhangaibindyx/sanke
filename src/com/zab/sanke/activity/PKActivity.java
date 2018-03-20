package com.zab.sanke.activity;

import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;
import java.util.ArrayList;
import java.util.Random;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zab.sanke.R;
import com.zab.sanke.R.id;
import com.zab.sanke.R.layout;
import com.zab.sanke.adapter.RoomAdapter;
import com.zab.sanke.entity.UserEntity;
import com.zab.sanke.view.PkGame;
import com.zab.sanke.view.RockerView;
import com.zab.sanke.view.RockerView.Direction;
import com.zab.sanke.view.RockerView.OnShakeListener;

/**
 * 数据格式： 1 : 2 : 3 : 4 : 5 : 6 : 7 房间id 房间名 行为 动作 己/方 预留 :在线人数 房间id
 * 根据在线人数自动增加2022 房间名 创建时命名 默认为 一起来战 行为 准备 100 开始 101 结束 103 动作 准备： 己 准备好 1
 * 友准备好2 开始： 友：上 201 下202 左203 右204 结束： -1 已/友 己 1 友 2 预留：暂无数据 默认为-100
 * 在线人数：由服务器计算
 * 
 * @author Administrator
 *
 */
@SuppressLint({ "HandlerLeak", "InflateParams" })
public class PKActivity extends Activity {
	public static final String READER_GAME = "100";
	public static final String START_GAME = "101";
	public static final String OVER_GAME = "102";
	public static final String ACTION_GAME_UP = "201";
	public static final String ACTION_GAME_DOWN = "202";
	public static final String ACTION_GAME_LEFT = "203";
	public static final String ACTION_GAME_RIGHT = "204";
	public static String friend = "301";

	private ListView lvRooms;
	private Button btnService, btnCline;
	private EditText etIP;
	private String IP;
	private boolean isConnect = true;
	private TextView tvNumber;
	public Handler mHandler = new Handler() {
		@Override
		public void handleMessage(android.os.Message msg) {
			switch (msg.what) {
			case HANDLER_CONNECT_ERROR:
				connectError();
				break;
			case HANDLER_CONNECT_SUCCESS:
				connectSuccess();
				Log.i("TEDU", "获得服务器数据");
				break;
			case HANDLER_MESSAGE_READER_STATA:
				Bundle data = msg.getData();
				initRoomsData(data);
				break;

			default:
				break;
			}
		}
	};

	private void connectSuccess() {
		Toast.makeText(PKActivity.this, "服务器连接成功", Toast.LENGTH_LONG).show();
		etIP.setEnabled(true);
		etIP.setText("一起来战，蛇我其谁");
		btnService.setEnabled(true);
		btnService.setText("创建房间");
		btnCline.setEnabled(true);
		isConnect = true;
		UserEntity u = new UserEntity();
		u.setRoomId("-1");
		try {
			writeToServer(u.toDataString());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			mHandler.sendEmptyMessage(HANDLER_CONNECT_ERROR);
		}
		dialog.cancel();
		new ReaderThread().start();
	}

	private ArrayList<UserEntity> rooms = new ArrayList<UserEntity>();

	/**
	 * 初始化当前房间列表和在线人数
	 * 
	 * @param data
	 *            服务器 返回的数据
	 */
	protected void initRoomsData(Bundle data) {
		String str = data.getString(MESSAGE_DATA_KEY);
		String[] strs = str.split(":");
		UserEntity ue = new UserEntity();
		ue.setRoomId(strs[0]);
		ue.setRoomName(strs[1]);
		ue.setAction(strs[3]);
		ue.setBehavior(strs[2]);
		ue.setOther(strs[4]);
		ue.setFriend(strs[5]);
		ue.setNumber(strs[6]);
		tvNumber.setText("当前在线玩家：" + ue.getNumber());
		if (ue.getRoomId().equals("-1")) {
			return;
		}
		int count = 0;
		for (int i = 0; i < rooms.size(); i++) {
			if (rooms.get(i).getRoomId().equals(ue.getRoomId())) {
				break;
			}
			count++;
		}
		if (count == rooms.size()) {
			rooms.add(ue);
			initRoomsAdatper();
		}
		if (u!=null&&ue.getRoomId().equals(u.getRoomId()) && game != null
				&& !ue.getFriend().equals(friend)) {
			upFrindSnake(ue);
		}
	}

	private void upFrindSnake(UserEntity ue) {
		setContentView(R.layout.activity_game_pk);
		initPKView();
		String dir = ue.getAction();
		if(dir==null){
			return;
		}
		switch (dir) {
		case ACTION_GAME_DOWN:
			game.setFriendDirection(PkGame.DOWN);
			break;
		case ACTION_GAME_UP:
			game.setFriendDirection(PkGame.UP);
			break;
		case ACTION_GAME_LEFT:
			game.setFriendDirection(PkGame.RIGHT);
			break;
		case ACTION_GAME_RIGHT:
			game.setFriendDirection(PkGame.LEFT);
			break;
		}

	}

	private void initRoomsAdatper() {
		ArrayList<UserEntity> r=new ArrayList<UserEntity>();
		for (int i =  rooms.size()-1; i>=0; i--) {
			r.add(rooms.get(i));
		}
		RoomAdapter adapter = new RoomAdapter(this, r,
				R.layout.lv_rooms_item);
		lvRooms.setAdapter(adapter);
	}

	private void connectError() {
		Toast.makeText(PKActivity.this, "服务器连接失败", Toast.LENGTH_LONG).show();
		etIP.setEnabled(false);
		etIP.setText("请重新连接");
		btnService.setText("重新连接");
		btnCline.setEnabled(false);
		isConnect = false;
		dialog.cancel();
	}

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_pk);
		initView();
		initListener();
		friend = friend + new Random().nextInt(9999);

		initConnect();
	}

	private ProgressDialog dialog;

	private void initConnect() {
		dialog = new ProgressDialog(this, ProgressDialog.STYLE_SPINNER);
		dialog.setMessage("拼命连接中...");
		dialog.setCancelable(false);
		dialog.show();
		new Thread() {
			public void run() {
				try {
					intitConnect();
				} catch (IOException e) {
					e.printStackTrace();
					mHandler.sendEmptyMessage(HANDLER_CONNECT_ERROR);
				}
			};
		}.start();
	}

	Socket socket;
	DataInputStream dis;
	DataOutputStream dos;
	private ImageView iv;
	private static final int HANDLER_CONNECT_SUCCESS = 200;
	private static final int HANDLER_CONNECT_ERROR = 404;
	private static final int HANDLER_MESSAGE_READER_STATA = 201;
	private static final String MESSAGE_DATA_KEY = "com.tedu.data.key.reader";
	private static final String SERVICE_IP = "114.115.138.35";

	private void intitConnect() throws IOException {
		socket = new Socket(SERVICE_IP, 8888);
		dis = new DataInputStream(socket.getInputStream());
		dos = new DataOutputStream(socket.getOutputStream());
		mHandler.sendEmptyMessage(HANDLER_CONNECT_SUCCESS);

	}
	@Override
	public void onBackPressed() {
		super.onBackPressed();
		startActivity(new Intent(PKActivity.this,MainActivity.class));
	}
	class ReaderThread extends Thread {
		@Override
		public void run() {
			try {
				while (true) {
					SystemClock.sleep(10);
					String str = dis.readUTF();
					Message msg = new Message();
					msg.what = HANDLER_MESSAGE_READER_STATA;
					Bundle data = new Bundle();
					data.putString(MESSAGE_DATA_KEY, str);
					msg.setData(data);
					mHandler.sendMessage(msg);
				}
			} catch (Exception e) {
				mHandler.sendEmptyMessage(HANDLER_CONNECT_ERROR);
			}
		}
	}

	// 向服务端写数据
	private void writeToServer(final String text) throws IOException {
		new Thread() {
			@Override
			public void run() {
				try {
					dos.writeUTF(text);
					dos.flush();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}.start();

	}

	private void initListener() {
		btnService.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				if (!isConnect) {
					initConnect();
				} else {
					CreateRoom();

				}
			}

		});
		btnCline.setOnClickListener(new OnClickListener() {
			public void onClick(View v) {
				rooms.clear();
				initRoomsAdatper();
			}
		});
		lvRooms.setOnItemClickListener(new OnItemClickListener() {
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				u = rooms.get(position);
				u.setFriend(friend);
				try {
					writeToServer(u.toDataString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
				setContentView(R.layout.activity_game_pk);
				initPKView();
			}
		});

	}

	protected void ShowWaitDiaLog() {
		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		LayoutInflater inflater = getLayoutInflater();
		final View layout = inflater.inflate(R.layout.dialog_create, null);
		builder.setView(layout);
		iv = (ImageView) layout.findViewById(R.id.iv_friend);
		
		builder.setPositiveButton("开始", new DialogInterface.OnClickListener() {
			public void onClick(DialogInterface dialog, int which) {
				setContentView(R.layout.activity_game_pk);
				initPKView();
			}
		});
		builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {

			@Override
			public void onClick(DialogInterface dialog, int which) {
			
			}
		});
		builder.setCancelable(false);
		builder.create().show();

	}

	private RockerView rv;
	private PkGame game;
	private TextView tv;
	// 当前房间对象
	private UserEntity u;

	protected void initPKView() {

		rv = (RockerView) findViewById(R.id.main_rockerView);
		tv = (TextView) findViewById(R.id.tv_store);
		game = (PkGame) findViewById(R.id.mygame);
		game.setTextView(tv);
		game.setFrindPK(true);
		rv.setOnShakeListener(new OnShakeListener() {
			public void onFinish() {
			}

			public void direction(Direction direction) {
				String dir = "";
				switch (direction) {
				case UP:
					game.up();
					dir = ACTION_GAME_UP;
					break;
				case DOWN:
					game.down();
					dir = ACTION_GAME_DOWN;
					break;
				case LEFT:
					game.left();
					dir = ACTION_GAME_LEFT;
					break;
				case RIGHT:
					game.right();
					dir = ACTION_GAME_RIGHT;
					break;

				default:
					break;
				}
				u.setAction(dir);
				u.setFriend(friend);
				u.setBehavior(START_GAME);
				try {
					writeToServer(u.toDataString());
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		});
	}

	private void CreateRoom() {
		if("".equals(etIP.getText().toString())){
			etIP.setText("一起来战，蛇我其谁");
			Toast.makeText(this, "请输入房间名", Toast.LENGTH_LONG).show();
			return;
		}
		if(etIP.getText().toString().length()<3||etIP.getText().toString().length()>10){
			Toast.makeText(this, "请输入3到10个字", Toast.LENGTH_LONG).show();
			etIP.setText("");
			return;
		}
		u = new UserEntity();
		Random r = new Random();
		int id = r.nextInt(10000) + 1000;
		u.setRoomId(id + "");
		u.setRoomName(etIP.getText().toString());

		u.setAction("1");
		u.setBehavior("1");
		u.setFriend("1");
		u.setOther("-100");
		try {
			writeToServer(u.toDataString());
		} catch (IOException e) {
			e.printStackTrace();
			mHandler.sendEmptyMessage(HANDLER_CONNECT_ERROR);
		}
		ShowWaitDiaLog();
	}

	private void initView() {
		lvRooms = (ListView) findViewById(R.id.pk_lv);
		btnService = (Button) findViewById(R.id.pk_btn_sercvice);
		btnCline = (Button) findViewById(R.id.pk_btn_friend);
		etIP = (EditText) findViewById(R.id.pk_et_ip);
		tvNumber = (TextView) findViewById(R.id.pk_tv_number);
	}

	@Override
	protected void onDestroy() {
		super.onDestroy();
		try {
			if(dis!=null&&dos!=null&&socket!=null){
				dis.close();
				dos.close();
				socket.close();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
