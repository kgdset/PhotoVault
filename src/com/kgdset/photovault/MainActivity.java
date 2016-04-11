package com.kgdset.photovault;

import java.util.List;

import net.youmi.android.AdManager;
import android.app.AlertDialog;
import android.app.Notification;

import android.app.AlertDialog.Builder;

import android.content.DialogInterface;
import android.content.Intent;

import android.content.res.Resources;
import android.hardware.Camera;
import android.hardware.Camera.Parameters;

import android.media.AudioManager;
import android.media.SoundPool;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.KeyEvent;

import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.android.pushservice.CustomPushNotificationBuilder;
import com.baidu.android.pushservice.PushConstants;
import com.baidu.android.pushservice.PushManager;

import com.kgdset.photovault.SlideLock.OnCheckedChangeListener;


public class MainActivity extends BaseActivity {

	//private SlidingButton mSlidingButton;

	private Camera camera = null;
	private Parameters parameters = null;
	public static boolean kaiguan = true;
	boolean isExit = false;
	long oldTime;
	public TextView TouchView;
	public TextView titletext;
	public TextView openbtn;
	private int downX;//刚触摸滑动区域的X坐标
	private boolean IsOpen;//是否解锁
	private int width;//触摸区域宽度
	private  RelativeLayout rLayout;
	private boolean isChecked;
	int Pwidth;
	int Pheight;
	private LinearLayout layout;
	
	private SlideLock sv;
	private ImageView hidebtn;
	private RelativeLayout hidelayout;
	private SoundPool	sp;
	private  int hit;






	@Override
	public void onCreate(Bundle savedInstanceState) {
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//设置全屏
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//去掉标题栏
     
		super.onCreate(savedInstanceState);
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		setContentView(R.layout.activity_main);
	
		
		
		//titletext=(TextView)findViewById(R.id.titletext);
		setview();
		//SetAdve();
		// 初始化接口，应用启动的时候调用
				// 参数：appId, appSecret, 调试模式
				AdManager.getInstance(this).init("94cfc89251d9bf3c",
						"8414c02cc56ea413", false);
				// 如果使用积分广告，请务必调用积分广告的初始化接口:
				net.youmi.android.offers.OffersManager.getInstance(this).onAppLaunch();
		//tiaoguo();
				
				//push
				
				// 以apikey的方式登录，一般放在主Activity的onCreate中
				PushManager.startWork(getApplicationContext(),
						PushConstants.LOGIN_TYPE_API_KEY, 
						Utils.getMetaValue(MainActivity.this, "api_key"));
				//PushManager.
				
				//PushManager.setTags(getApplicationContext(),);
				//设置自定义的通知样式，如果想使用系统默认的可以不加这段代码
		        CustomPushNotificationBuilder cBuilder = new CustomPushNotificationBuilder(
		        		resource.getIdentifier("notification_custom_builder", "layout", pkgName), 
		        		resource.getIdentifier("notification_icon", "id", pkgName), 
		        		resource.getIdentifier("notification_title", "id", pkgName), 
		        		resource.getIdentifier("notification_text", "id", pkgName));
		        		
		        
		        cBuilder.setNotificationFlags(Notification.FLAG_AUTO_CANCEL);
		        cBuilder.setNotificationDefaults(Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE);
		        cBuilder.setStatusbarIcon(R.drawable.simple_notification_icon);
		        cBuilder.setLayoutDrawable(resource.getIdentifier("push_1", "drawable", pkgName));
				PushManager.setNotificationBuilder(this, 1, cBuilder);
				 cBuilder.setStatusbarIcon(R.drawable.simple_notification_icon2);
			     cBuilder.setLayoutDrawable(resource.getIdentifier("push_2", "drawable", pkgName));
			     PushManager.setNotificationBuilder(this, 2, cBuilder);	
			     cBuilder.setStatusbarIcon(R.drawable.simple_notification_icon3);
			     cBuilder.setLayoutDrawable(resource.getIdentifier("push_3", "drawable", pkgName));
			     PushManager.setNotificationBuilder(this, 3, cBuilder);	
	}
	@Override
	public void onStop() {
		super.onStop();
		PushManager.activityStoped(this);
		layout.removeView(sv);
		sv=null;
		//sv.clearDisappearingChildren();
	}
	@Override
	public void onStart() {
		super.onStart();
		
		PushManager.activityStarted(this);
	}
	public void tiaoguo()
	{
		
		finish();
		Intent intent=new Intent();
  	  
 	   if(StaticParameter.IsPwdType)
 	   {
 		   intent.putExtra("type", "login");
 		   intent.setClass(MainActivity.this, LoginActivity.class);
 			
 	   }
 	   else
 	   {
 		   intent.putExtra("type", "0");
			intent.setClass(MainActivity.this, PwdActivity.class);
			
 	   }
 	   startActivity(intent);
			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
	}
	public void SetAdve()
	{
		AdwoView.SetAdwo(this,(ViewGroup) findViewById(R.id.view),2);
	}
	 protected void onResume() {
         // TODO Auto-generated method stub
		 //onCreate(MainActivity.this);
		 super.onResume();
	
        
        
        sv = new SlideLock(this);
	      
        sv.setOnCheckedChangeListener(new OnCheckedChangeListener() {
			public void onCheckedChanged(boolean isChecked) {
				//Log.e("log", "开开开");
				Intent intent=new Intent();
	        	  
	        	   if(StaticParameter.IsPwdType)
	        	   {
	        		   intent.putExtra("type", "login");
	        		   intent.setClass(MainActivity.this, LoginActivity.class);
	        			
	        	   }
	        	   else
	        	   {
	        		   intent.putExtra("type", "0");
	    			intent.setClass(MainActivity.this, PwdActivity.class);
	    			
	        	   }
	        	   startActivity(intent);
	    			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
			}
		});
        layout.addView(sv);
        
        if(StaticParameter.IsHideLock)
	       {
	    	   hidelayout.setVisibility(View.VISIBLE);
	    	   TouchView.setVisibility(View.GONE);
	       }
	       else
	       {
	    	   hidelayout.setVisibility(View.GONE);
	    	   TouchView.setVisibility(View.VISIBLE);
	       }
        // if (!isActive) {
                 //app 从后台唤醒，进入前台
         	
                 //isActive = true;
        // }
 }

	public void setview()
	{
		sp = new SoundPool(10, AudioManager.STREAM_MUSIC,100);
		hit=sp.load(MainActivity.this,R.raw.key, 1);
		TouchView =(TextView)findViewById(R.id.touch_area);
		openbtn = (TextView)findViewById(R.id.opentn);
		rLayout=(RelativeLayout)findViewById(R.id.view);
		hidelayout=(RelativeLayout)findViewById(R.id.hidelayout);
		 layout = (LinearLayout) findViewById(R.id.layout);
	       hidebtn=(ImageView) findViewById(R.id.hidebtn);
	       if(StaticParameter.IsHideLock)
	       {
	    	   hidelayout.setVisibility(View.VISIBLE);
	    	   TouchView.setVisibility(View.GONE);
	       }
	       else
	       {
	    	   hidelayout.setVisibility(View.GONE);
	    	   TouchView.setVisibility(View.VISIBLE);
	       }
	       hidebtn.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)
			 {
					
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("温馨提示").setMessage("请确认你已记住此入口，隐藏后位置不变。试试看，赶紧的...")  
	                        .setPositiveButton("我记住了", new DialogInterface.OnClickListener(){  
	                            @Override  
	                            public void onClick(DialogInterface dialog, int which) {  
	                            	UserEntity user=new UserEntity();
	            					user.setIsHideLock("1");
	            					DataOperate.updateuser(user);
	            					hidelayout.setVisibility(View.GONE);
	            			    	TouchView.setVisibility(View.VISIBLE);
	            			    	init.GetUserEntity();
	            			    	//Log.e("log", "开开开"+user.getIsHideLock());
	                            }  
	                        }).setNegativeButton("取消隐藏", new DialogInterface.OnClickListener() {  
	                            @Override  
	                            public void onClick(DialogInterface dialog, int which) {  
	                                
	                            }  
	                        }).show();  
			 }
			 
			 });
	       
	        
		//titletext.setText("fdasfsadfadfasfdsa")
		
		//WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
        
		final int Pwidth =init.Pwidth;
		final int Pheight = init.Pheight;
		int btnheight=(int) (Pheight*0.2);
		int btnwidth=(int) (Pwidth*0.2);
	
		openbtn.setHeight(btnheight);
		openbtn.setWidth(btnwidth);
		//openbtn.setTop(topheight);
		TouchView.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    int action =event.getAction();
                    switch(action){
                    //当按下的时候
                    case (MotionEvent.ACTION_DOWN):
                    		IsOpen=true;
                            Display("ACTION_DOWN",event);
                    break;
                 
                    
                     //当触摸的时候
                    case(MotionEvent.ACTION_MOVE):
                    	if(IsOpen)
                            Display("ACTION_MOVE",event);
                    }
                    return true;
            }
    });
		openbtn.setOnTouchListener(new View.OnTouchListener() {
            
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                    int action =event.getAction();
                    
                    switch(action){
                    //当按下的时候
                    case (MotionEvent.ACTION_DOWN):
                    	if (!isChecked) {
                    		try {
                    			sp.play(hit, 1, 1, 0, 0, 1); 
                        		rLayout.setBackgroundResource(R.drawable.on);
                        		
                        		
                                    	
                                        	camera = Camera.open();
                        					camera.startPreview();
                        					parameters = camera.getParameters();
                        					parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// 开启
                        					camera.setParameters(parameters);
                        					 camera.startPreview(); // 开始亮灯
                        					isChecked=true;
                                    

							} catch (Exception e) {
								//isChecked=false;// TODO: handle exception
							}
                    		

        				} else {
        					try {
        						
            					sp.play(hit, 1, 1, 0, 0, 1); 
            					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// 关闭
            					camera.setParameters(parameters);
            					camera.release();
            				
            					isChecked=false;
            					rLayout.setBackgroundResource(R.drawable.off);
							} catch (Exception e) {
								//isChecked=true;// TODO: handle exception
							}
        					
        				
        				}
                    		IsOpen=true;
                            Display("ACTION_DOWN",event);
                    break;
                    //当按上的时候
                    
                    
                    }
                    return true;
            }
    });
	}
	 public void Display(String eventType,MotionEvent event){
	    	if(eventType=="ACTION_DOWN")//手指刚触摸解锁区域时
	    	{
	    		width=TouchView.getWidth();//获取触摸区域宽度
	    		downX=(int) event.getX();//获取刚触摸区域的X坐标
	    		
	    	}
	    	if(eventType=="ACTION_MOVE")//进行滑动时
	    	{
	    		int x =(int) event.getX();//获取X坐标
	            
	           int num= x-downX;//计算当前X坐标和刚触摸时X坐标的差值
	           if(num>width*0.7)//如果差值大于解锁区域宽度的70%，那么解锁
	           {
	        	  
	        	   IsOpen=false;
	        	   Intent intent=new Intent();
	        	   if(StaticParameter.IsPwdType)
	        	   {
	        		   intent.putExtra("type", "login");
	        		   intent.setClass(MainActivity.this, LoginActivity.class);
	        			
	        	   }
	        	   else
	        	   {
	        		   intent.putExtra("type", "0");
	    			intent.setClass(MainActivity.this, PwdActivity.class);
	    			
	        	   }
	    			startActivity(intent);
	    			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
	           }
	    	}
	       
	    }
	// 使用onkeydown监听返回键

	Handler mHandler = new Handler() {
		@Override
		public void handleMessage(Message msg) {
			super.handleMessage(msg);
			isExit = false;
		}
	};

	@Override
	public boolean onKeyDown(int keyCode, KeyEvent event) {
		// TODO Auto-generated method stub
		if (keyCode == KeyEvent.KEYCODE_BACK) {

			if (!isExit) {
				isExit = true;
				Toast.makeText(getApplicationContext(), "再按一次退出程序",
						Toast.LENGTH_SHORT).show(); // 利用handler延迟发送更改状态信息
				mHandler.sendEmptyMessageDelayed(0, 2000);
			} else {
				
				ApplicationEx.getInstance().exit();
		          // System.exit(0)是将你的整个虚拟机里的内容都停掉了
								// ，而dispose()只是关闭这个窗口，但是并没有停止整个application
								// exit()
								// 。无论如何，内存都释放了！也就是说连JVM都关闭了，内存里根本不可能还有什么东西
				// System.exit(0)是正常退出程序，而System.exit(1)或者说非0表示非正常退出程序
				// System.exit(status)不管status为何值都会退出程序。和return
				// 相比有以下不同点：return是回到上一层，而System.exit(status)是回到最上层
			}
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("作者：");

		builder.setTitle("关于");

	

		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 2) {
			Toast.makeText(MainActivity.this, "客官记得下次再来哦", 1).show();
			finish();
			System.exit(0);
			// dialog();
		} else if (item.getItemId() == 1) {
			dialog();
			// Toast.makeText(MainActivity.this, "作者：老韩", 1).show();

		}
		return super.onOptionsItemSelected(item);
	}

	
	
	
	
	
}
