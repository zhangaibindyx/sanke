package com.zab.sanke.view;

import java.net.InterfaceAddress;
import java.util.ArrayList;
import java.util.Random;






import com.zab.sanke.R;
import com.zab.sanke.R.drawable;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.res.Resources;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.os.SystemClock;
import android.util.AttributeSet;
import android.view.KeyEvent;
import android.view.View;
import android.widget.TextView;


/**
 * 把屏幕划分成一个二维的矩阵,用int[][]来记录每一个方格的状态
 * 计算横,纵向下图形(方格)的个数
 * 计算横纵向的偏移量,用来绘制每个方格时定位
 * 初始化二维数组
 * */
@SuppressLint({ "ClickableViewAccessibility", "DrawAllocation", "Recycle" })
public class MyGame extends View {
	private int size = 30; //图片的大小
	private int xcount;   //定义图片的x上需要放的数量
	private int ycount;   //定义图片的y上需要放的数量
	private int xoffset;  //x方向的偏移量
	private int yoffset;  //y方向的偏移量
	private int[][] map;  //地图二维数组
	private Bitmap[] pics;//用来存放图片,3张图片
	/**墙体*/
	public static final int GREEN_STAR = 1;  //3种图片类型,定义成常量
	/**身体*/
	public static final int RED_STAR = 2;
	/**尾部*/
	public static final int YELLOW_STAR = 3;//苹果
	/**头*/
	public static final int TOU_STAT = 4;
	/**苹果*/
	public static final int PG_STAT = 5;
	/**苹果*/
	public static final int PG_GOLD_STAT = 6;
	/**苹果*/
	public static final int MACHINE = 7;
	private int mm=5;

	private boolean [][] wallMap;
	//-----------方向
	public static final int UP = 1;
	public static final int RIGHT = 2;
	public static final int DOWN = 3;
	public static final int LEFT = 4;
	public int direction;
	public int nextdirection;

	private int mMode = READY;  //游戏运行状态
	public static final int PAUSE = 1;   //暂停
	public static final int READY = 2;   //准备
	public static final int RUNING = 3;  //正在运行
	public static final int LOSE = 4;    //结束,停止运行

	//血量 每次初始化游戏时都要重置为3  每次撞墙时减少1
	private static int boold=3;
	//恢复血量方法
	public void setBoold(){
		if(boold<3){
			++boold;
		}
	}
	
	private TextView textview = null;    //屏幕上显示的文字
	private int score = 0;    //当前分数
	private int speed = 200;

	private int redId;
	private int greenId;
	private int yellowId;
	private int tou=R.drawable.chongzitou;
	//蛇身
	private ArrayList<Coordinate> snakeList = new ArrayList<Coordinate>();

	//苹果
	private ArrayList<Coordinate> appleList = new ArrayList<Coordinate>();
	int setSpeed=0;
	boolean isSpeed=false;
	public void setSpeedUp(boolean isSetSpeed){
		if(isSetSpeed&&isSpeed!=isSetSpeed){
			isSpeed=isSetSpeed;
			setSpeed=speed;
			speed*=0.4;
		}else if(!isSetSpeed&&isSpeed!=isSetSpeed){
			isSpeed=isSetSpeed;
			speed=setSpeed;
		}
	}
	/**
	 * 向上移动
	 */
	public void up(){
		if( direction !=  UP&& direction!=MyGame.DOWN)
			nextdirection= UP;
	}
	/**
	 * 向下移动
	 */
	public void down(){
		if( direction !=   DOWN& direction!=MyGame.UP)
			nextdirection= DOWN;
	}
	/**
	 * 向左移动
	 */
	public void left(){
		if( direction !=  LEFT&& direction!=MyGame.RIGHT)
			nextdirection= LEFT;
	}
	/**
	 * 向右移动
	 */
	public void right(){
		if( direction !=  RIGHT&& direction!=MyGame.LEFT)
			nextdirection= RIGHT;
	}


	//复写了2个参数的构造方法
	public MyGame(Context context, AttributeSet attrs) {
		super(context, attrs);
		isMachine=false;

	}

	//复写onDraw()方法
	//这里作用:完成反复绘图
	@Override
	protected void onDraw(Canvas canvas) {
		// TODO Auto-generated method stub
		super.onDraw(canvas);

		Paint paint = new Paint();

		//绘图
		for(int i=0; i<xcount; i++)
		{
			for(int j=0; j<ycount; j++)
			{
				if(map[i][j]>0)  // 大于0时就绘图
				{
					canvas.drawBitmap(pics[map[i][j]],
							xoffset+i*size,yoffset+j*size, paint);
				}
			}
		}
	}
	//onSizeChanged()是在布局发生变化时的回调函数，间接回去调用onMeasure, onLayout函数重新布局
	//如果是刚加入的视图,变更前的值为0    
	//这里作用:完成初始化工作
	@Override
	protected void onSizeChanged(int w, int h, int oldw, int oldh) {
		// TODO Auto-generated method stub
		super.onSizeChanged(w, h, oldw, oldh);
		
		xcount = (int)Math.floor(w/size);   //x方向需要的图片数量,向下取整
		ycount = (int)Math.floor(h/size);

		xoffset = (w - size*xcount) / 2;  //宽度减去总图片的宽度/2
		yoffset = (h - size*ycount) / 2;

		map = new int[xcount][ycount];
		wallMap=new boolean [xcount][ycount];
		initGame();     // 初始化游戏,再刚进游戏时
	}



	//地图数组赋值   图片下标,该点坐标x,y
	public void setTile(int picIndex,int x,int y)
	{
		map[x][y] = picIndex;
	}

	//将3副图片加载进来,加载到pics图片数组里
	public void loadPic(int key,Drawable drawable)
	{
		/*
		 * 1、Drawable就是一个可画的对象，其可能是一张位图（BitmapDrawable），也可能是一个图形（ShapeDrawable），还有可能是一个图层（LayerDrawable），我们根据画图的需求，创建相应的可画对象
			2、Canvas画布，绘图的目的区域，用于绘图
			3、Bitmap位图，用于图的处理
			4、Matrix矩阵
		 * */
		Bitmap bitmap = Bitmap.createBitmap(size,size,Bitmap.Config.ARGB_8888);
		Canvas canvas = new Canvas(bitmap);   //建立对应 bitmap 的画布
		drawable.setBounds(0,0,size,size);   //它是指定一个矩形区域，然后通过drawable(Canvas)画的时候，就只在这个矩形区域内画图。
		drawable.draw(canvas);   // 把 drawable 内容画到画布中 
		pics[key] = bitmap;      //设置图片数组的该位置是哪个位图
	}

	//初始化游戏 
	@SuppressWarnings("deprecation")
	public void initGame()
	{	
		mMode=READY;
		gameOver=true;
		appleList.clear();    //移除此列表中的所有元素。
		snakeList.clear();    //移除此列表中的所有元素。
		snakeMachineList.clear();
		speed = 800;          //初始速度
		score = 0;            //初始分数
		boold=3;
		//将图片加载到图片数组里
		Resources r = getResources();  //从资源中获得
		pics = new Bitmap[8];
		loadPic(GREEN_STAR,r.getDrawable(R.drawable.green));
		loadPic(RED_STAR,r.getDrawable(R.drawable.pingguo));
		loadPic(YELLOW_STAR,r.getDrawable(R.drawable.redstar));
		loadPic(TOU_STAT,r.getDrawable(R.drawable.chongzitou));
		loadPic(PG_STAT,r.getDrawable(R.drawable.pingguo));
		loadPic(PG_GOLD_STAT,r.getDrawable(R.drawable.jinpingguo));
		loadPic(MACHINE,r.getDrawable(R.drawable.yellowstar));

		//设置蛇的初始位置
		snakeList.add(new Coordinate(5, 7));
		snakeList.add(new Coordinate(4, 7));
		snakeList.add(new Coordinate(3, 7));
		snakeList.add(new Coordinate(2, 7));
		snakeMachineList.add(new Coordinate(5, 9));
		snakeMachineList.add(new Coordinate(4, 9));
		snakeMachineList.add(new Coordinate(3, 9));
		snakeMachineList.add(new Coordinate(2, 9));
		direction=RIGHT;   //设置蛇的初始方向
		nextdirection=RIGHT;
		addRandomApple();   //产生随机苹果
		update();
	}

	//组建墙
	/*
	 * 完成墙绘制之前需要加载游戏中用的图片,红色(蛇头),黄色(蛇身),绿色(墙,苹果)
	 * 为地图位置赋值
	 * */
	public void buildWall(){
		for (int i = 0; i < ycount; i++) {
			if(i==ycount/2||i==ycount/2+1||i==ycount/2-1){
				continue;
			}
			wallMap[xcount/2][i]=true;
			setTile(GREEN_STAR, xcount/2, i);
		}
		for(int i=0; i<xcount; i++)
		{
			setTile(YELLOW_STAR,i,0);         //最上面那堵墙,把这些坐标设置成墙
			setTile(YELLOW_STAR,i,ycount-1);  //最下面
			wallMap[i][ycount-1]=true;
			wallMap[i][0]=true;
		}
		for(int j=0; j<ycount; j++)
		{
			setTile(YELLOW_STAR,0,j);        //左
			setTile(YELLOW_STAR,xcount-1,j);
			wallMap[0][j]=true;
			wallMap[xcount-1][j]=true;
		}

	}


	//需要重复绘制图形,所以需要用到线程,Android里需要用到Handler
	//常用的方法是利用Handler来实现UI线程的更新的
	private MyHandler handler = new MyHandler();
	class MyHandler extends Handler{

		@Override
		public void handleMessage(Message msg) {
			// TODO Auto-generated method stub
			super.handleMessage(msg);
				update();


			/*
			 * invalidate()是用来刷新View的，必须是在UI线程中进行工作。比如在修改某个view的显示时，
			 * 调用invalidate()才能看到重新绘制的界面。
			 * invalidate()的调用是把之前的旧的view从主UI线程队列中pop掉。
			 * */
			MyGame.this.invalidate();   //可以实现重绘
		}
		public void sleep(int delay)
		{
			this.removeMessages(0);

			//sendMessageDelayed 是将某个需要处理的消息事件发送给handler来处理，
			//并且在此之前按你传入的参数延迟一定的时间。
			//你需要在handler中去处理你要发送的消息。
			if(mMode!=LOSE){
				sendMessageDelayed(obtainMessage(0), speed);
			}
		}
	}

	/*---
	 * 蛇的移动考虑:
	 * 实现不断的重绘
	 * 蛇的位置变化,形成走动
	 * 蛇的长度不断变化,用ArrayList
	 * 要记录蛇的位置需要用到一个坐标类Coordinate
	 *    private ArrayList<Coordinate>snakeTrail = new ArrayList<Coordinate>();
	 * */

	public void update(){
		if(!isPause){
		clearTile();   //清空map地图数组
		buildWall();   //组建墙
		updateSnake();
		if(isMachine){
			upMachineSnke();
		}
		updateApple();
		}
		handler.sleep(speed);   //延迟
	}
	/**
	 * 设置无敌效果 为3秒钟
	 */
	private boolean  invincible=false;	
	public void setInvincible(){
		invincible=true;
		new Thread(){
			public void run() {
				SystemClock.sleep(3000);
				invincible=false;
			};
		}.start();
	}
	private BooldStatListener listener;
	public void setBooldStatListener(BooldStatListener listener){
		this.listener=listener;
	}
	private int nextMachineDirection=RIGHT;
	private int macthineDirection=RIGHT;
	private ArrayList<Coordinate> snakeMachineList = new ArrayList<Coordinate>();
	public void upMachineSnke(){
		//获得蛇头
				Coordinate header = snakeMachineList.get(0);
				Coordinate newHeader = new Coordinate(1,1);   //新的蛇头
				//处理蛇的走向
				//首先判断苹果是否在同一侧
				
				Coordinate app=appleList.get(0);
				
				
				
				if(app.x<xcount/2&&header.x<xcount/2){
					//都在左侧
					if(app.x>header.x){
						nextMachineDirection=RIGHT;
					}else if(app.x<header.x){
						nextMachineDirection=LEFT;
					}else{
						if(app.y>header.y){
							nextMachineDirection=DOWN;
						}else{
							nextMachineDirection=UP;
						}
					}
					
				}else if(app.x>xcount/2&&header.x>xcount/2){
					//都在右侧
					if(app.x>header.x){
						nextMachineDirection=RIGHT;
					}else if(app.x<header.x){
						nextMachineDirection=LEFT;
					}else{
						if(app.y>header.y){
							nextMachineDirection=DOWN;
						}else{
							nextMachineDirection=UP;
						}
					}
					
				}else{
					//不在一起
                   if(header.y>ycount/2){
                	   nextMachineDirection=UP;
                   }else if(header.y<ycount/2){
                	   nextMachineDirection=DOWN;
                   }else{
                	   if(header.x<xcount/2){
                		   nextMachineDirection=RIGHT;
                	   }else if(header.x>xcount/2){
                		   nextMachineDirection=LEFT;
                	   }else{
                		  
                	   }
                   }
					
				}
				//检测蛇是否吃到自己
				for(Coordinate c: snakeList)
				{
					if(c.x==newHeader.x+1|| c.y==newHeader.y+1
					 ||c.x==newHeader.x||c.y==newHeader.y+1
					 ||c.x==newHeader.x-1||c.y==newHeader.y
					 ||c.x==newHeader.x||c.y==newHeader.y-1)
					{
						nextMachineDirection=macthineDirection;
					}
				}
			 switch (macthineDirection) {
			case RIGHT:
				if(nextMachineDirection==LEFT){
					if(header.y<ycount/2){
						nextMachineDirection=DOWN;
					}else{
						nextMachineDirection=UP;
					}
				}
				break;
			case LEFT:
				if(nextMachineDirection==RIGHT){
					if(header.y<ycount/2){
						nextMachineDirection=DOWN;
					}else{
						nextMachineDirection=UP;
					}
				}
				
				break;
			case UP:
				if(nextMachineDirection==DOWN){
					if(header.x<xcount/2){
						nextMachineDirection=RIGHT;
					}else{
						nextMachineDirection=LEFT;
					}
				}
				break;
			case DOWN:
				if(nextMachineDirection==UP){
					if(header.y<ycount/2){
						nextMachineDirection=RIGHT;
					}else{
						nextMachineDirection=LEFT;
					}
				}
				
				break;

			default:
				break;
			}
		
			
				
				macthineDirection = nextMachineDirection;
				
				switch(macthineDirection)
				{
				case RIGHT:
					newHeader = new Coordinate(header.x+1,header.y);   //这是往右移
					break;
				case LEFT:
					newHeader = new Coordinate(header.x-1,header.y);   //这是往左移
					break;
				case UP:
					newHeader = new Coordinate(header.x,header.y-1);   //这是往上移
					break;
				case DOWN:
					newHeader = new Coordinate(header.x,header.y+1);   //这是往下移
					break;
				}
				
				boolean growSnake=false;
				//蛇头吃到苹果
				if(newHeader.x==appleList.get(0).x && newHeader.y==appleList.get(0).y)  
				{
					appleList.remove(0);
					addRandomApple();
					speed *= 0.9;   //速度越来越快
					growSnake=true;
					
				}

				//将新的蛇头添加到蛇的ArrayList<Coordinate>中
				snakeMachineList.add(0,newHeader);
				if(!growSnake)   //如果没吃到果子则去掉最后一节蛇,吃到了则不清理最后一节
				{
					//将原来的蛇的尾部除掉
					snakeMachineList.remove(snakeMachineList.size()-1);
				}
				
				//index是蛇的具体哪一节
				int index = 0;
				for (Coordinate c : snakeMachineList) //迭代取出ArrayList<Coordinate>中的数据
				{
					if (index == 0)   //蛇头,绘制黄色
					{
						setTile(TOU_STAT, c.x, c.y);
					} 
					else if(index!=0&&index!=snakeMachineList.size()-1)            //蛇身,绘制红色
					{
						setTile(RED_STAR, c.x, c.y);
					}else{
						setTile(MACHINE, c.x, c.y);
					}		
					index++;          //位置往蛇的后面移动
				}
		
	}
	//更新蛇的身体
	public void updateSnake()
	{
		if(textview!=null)
			textview.setText("分数:"+score);
		//获得蛇头
		Coordinate header = snakeList.get(0);
		Coordinate newHeader = new Coordinate(1,1);   //新的蛇头
		direction = nextdirection;
		switch(direction)
		{
		case RIGHT:
			newHeader = new Coordinate(header.x+1,header.y);   //这是往右移
			break;
		case LEFT:
			newHeader = new Coordinate(header.x-1,header.y);   //这是往左移
			break;
		case UP:
			newHeader = new Coordinate(header.x,header.y-1);   //这是往上移
			break;
		case DOWN:
			newHeader = new Coordinate(header.x,header.y+1);   //这是往下移
			break;
		}
		if(!invincible){
			//是否撞墙
			for (int i = 0; i < wallMap.length; i++) {
				for (int j = 0; j <wallMap[i].length; j++) {
					if(wallMap[i][j]==true)
					{ 
						if (newHeader.x==i&&newHeader.y==j) {
							if(boold<1){
								setMode(LOSE);
								return ;
							}else{
								--boold;
							}
						}
					}
				}
			}


			//检测蛇是否吃到自己
			for(Coordinate c: snakeList)
			{
				if(c.x==newHeader.x && c.y==newHeader.y)
				{
					if(boold<1){
						setMode(LOSE);
						return ;
					}else{
						--boold;
					}
				}
			}
			//此时血量减少1 回调血量减少接口BooldStatListener
			if(listener!=null){
				listener.onBooldStatListener(boold);
			}



		}
		if(newHeader.x<0||newHeader.x>xcount-1||newHeader.y<0||newHeader.y>ycount-1){
			setMode(LOSE);
			return;
		}

		boolean growSnake=false;
		//蛇头吃到苹果
		if(newHeader.x==appleList.get(0).x && newHeader.y==appleList.get(0).y)  
		{
			//如果是金苹果 则回调GlodAppleListener
			if(isGoldPg){
				appleListener.goldAppleListener();
				
			}
			score++;   //分数+1
			if(sulistener!=null){
				sulistener.scoreUpListener(score);
			}
			appleList.remove(0);
			addRandomApple();
			speed *= 0.9;   //速度越来越快
			growSnake=true;
			//通知主界面金币增加
			if(goldInterface!=null)
			goldInterface.goldAddListener();
			
		}

		

		//将新的蛇头添加到蛇的ArrayList<Coordinate>中
		snakeList.add(0,newHeader);
		if(!growSnake)   //如果没吃到果子则去掉最后一节蛇,吃到了则不清理最后一节
		{
			//将原来的蛇的尾部除掉
			snakeList.remove(snakeList.size()-1);
		}

		//index是蛇的具体哪一节
		int index = 0;
		for (Coordinate c : snakeList) //迭代取出ArrayList<Coordinate>中的数据
		{
			if (index == 0)   //蛇头,绘制黄色
			{
				setTile(TOU_STAT, c.x, c.y);
			} 
			else if(index!=0&&index!=snakeList.size()-1)            //蛇身,绘制红色
			{
				setTile(RED_STAR, c.x, c.y);
			}else{
				setTile(YELLOW_STAR, c.x, c.y);
			}
			index++;          //位置往蛇的后面移动
		}


	}
	private ScoreUpListener sulistener;
    public void setScoreUpListener(ScoreUpListener sulistener){
    	 this.sulistener=sulistener;
    }
		
	public interface ScoreUpListener{
		void scoreUpListener(int score);
	}

	
	
	
	
	 private boolean isPause=false;
	/**
	 * 游戏是否继续或者暂停
	 * @param isPause true  暂停   false 继续
	 */
	public void setGamePauseOrCountine(boolean isPause){
		this.isPause=isPause;
	}
	
	
	/***
	 * 金币增加回调接口
	 * @author Administrator
	 *
	 */
	public interface GoldInterface{
			/**
			 * 贪吃蛇已经吃到苹果，可以增加金币
			 */
		    void goldAddListener();
	}
	private GoldInterface goldInterface;
	public void setGoldInterface(GoldInterface goldInterface){
		this.goldInterface=goldInterface;
	}
	
	
	/**
	 * 当前血量回调接口，此接口在updateSnake函数中检测撞墙和吃到自己时回调
	 * @author Administrator
	 *
	 */
	public interface BooldStatListener{
		void onBooldStatListener(int boold);
	}


	public void setTextView(TextView newView) {  
		textview = newView;
	}
	private boolean gameOver=true;
	//设置游戏运行状态״̬
	public void setMode(int newMode)
	{ 
		int oldMode = mMode;
		mMode = newMode;
		if(newMode==LOSE)
		{
			textview.setText("游戏结束");
			clearTile();   //清除地图
			handler.removeCallbacks(null);    //ֹͣ停止hander
			//			initGame();   //初始化游戏
			
			if(!isExit){//默认false
				isExit=false;
				if (GOlistener!=null&&gameOver) {
					GOlistener.GameOver();
					gameOver=false;
				}
			}
		}

	}
	/**
	 * 主要用于游戏结束后继续游戏采用的初始化动作。
	 * 注意此时的分数是接着上一次结束时的分数
	 * 注意此时的速度是接着上一次结束时的速度
	 * 注意蛇的身体为初始化位置，因为撞墙后无法继续前进和无血量后将无法继续
	 */
	public void setInitGame(){
		gameOver=true;
		int oldscore=score;
		int oldSpeed=speed;
		initGame();
		score=oldscore;
		speed=oldSpeed;
	}
	/**
	 * 游戏结束回调函数 作用于游戏结束时回调，主要提示用户是否继续或者退出游戏，一般使用对话框提示。
	 * @author Administrator
	 *
	 */
	public interface GameOverListener{
		/**
		 * 当此方法被执行时，代表游戏已经结束，血量为0。
		 * 此方法处理游戏退出和复活时使用。
		 */
		void GameOver();
	}
	GameOverListener GOlistener;
	public void setGameOverListener(GameOverListener listener){
		this.GOlistener=listener;
	}


	//将map数组(地图)清零
	public void clearTile(){
		for(int i=0; i<xcount; i++)
		{
			for(int j=0; j<ycount; j++)
			{
				setTile(0,i,j);
			}
		}
	}

	//表示蛇身的坐标类
	private class Coordinate{
		public int x;
		public int y;
		public Coordinate(int x,int y)
		{
			super();
			this.x = x;
			this.y = y;
		}

		//冲突检测,蛇是否吃到苹果
		public boolean equals(Coordinate other)
		{
			if( this.x == other.x && this.y == other.y )
			{
				return true;
			}
			return false;
		}
	}

	//获取用户输入的按键
	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub

		//用户选择的方向,改变direction方向
		switch(keyCode)
		{
		case KeyEvent.KEYCODE_DPAD_UP:   //用户按下   上   键
			if(direction != DOWN)
				nextdirection = UP;
			break;
		case KeyEvent.KEYCODE_DPAD_RIGHT:
			if(direction != LEFT)
				nextdirection = RIGHT;
			break;
		case KeyEvent.KEYCODE_DPAD_DOWN:
			if(direction != UP)
				nextdirection = DOWN;
			break;
		case KeyEvent.KEYCODE_DPAD_LEFT:
			if(direction != RIGHT)
				nextdirection = LEFT;
			break;
		case KeyEvent.KEYCODE_BACK:    //按返回键直接退出
			System.exit(0);
			break;
		}
		return super.onKeyDown(keyCode, event);
	}
	private boolean isExit=false;
	public void setExitGame(){
		this.isExit=true;
		setMode(LOSE);
	}
	
	
	//随机产生苹果
	private void addRandomApple()
	{
		Coordinate newcoor = new Coordinate(0,0);
		Random random = new Random();
		boolean found = false;
		while(!found)
		{
			newcoor.x = 1+random.nextInt(xcount-2);
			newcoor.y = 1+random.nextInt(ycount-2);
			for(Coordinate c: snakeList)
			{
				if(newcoor.x == c.x && newcoor.y == c.y)  //产生的苹果坐标与蛇身冲突
				{
					found = false;
					break;
				}
			}
			if(wallMap[newcoor.x][newcoor.y]){
				found=false;
				break;
			}
			found = true;
		}
		int i=new Random().nextInt(2);
		if(i%2==0){
			isGoldPg=true;
		}else{
			isGoldPg=false;
		}
		appleList.add(newcoor);
		updateApple();
	}
   private boolean isGoldPg=false;
	//给苹果绘图
	private void updateApple()
	{   //金苹果时，开启抽奖模式
		if(isGoldPg){
			setTile(PG_GOLD_STAT, appleList.get(0).x,appleList.get(0).y);
		}else{
			setTile(PG_STAT,appleList.get(0).x,appleList.get(0).y);
			
		}
		
	}
	
	private GoldAppleListener appleListener;
	public void setGoldAppleListener(GoldAppleListener appleListener){
		this.appleListener=appleListener;
	}
	
	/**
	 * 遇到金苹果时开启抽奖模式，同时游戏暂停
	 * @author Administrator
	 *
	 */
	public interface GoldAppleListener{
		void goldAppleListener();
	}
	private boolean isMachine=false;
	public void setMachine() {
		isMachine=true;
		
	}
	
	
	
}
