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
	private int downX;//�մ������������X����
	private boolean IsOpen;//�Ƿ����
	private int width;//����������
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
		getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN, WindowManager.LayoutParams.FLAG_FULLSCREEN);//����ȫ��
		requestWindowFeature(Window.FEATURE_NO_TITLE);	//ȥ��������
     
		super.onCreate(savedInstanceState);
		Resources resource = this.getResources();
		String pkgName = this.getPackageName();
		setContentView(R.layout.activity_main);
	
		
		
		//titletext=(TextView)findViewById(R.id.titletext);
		setview();
		//SetAdve();
		// ��ʼ���ӿڣ�Ӧ��������ʱ�����
				// ������appId, appSecret, ����ģʽ
				AdManager.getInstance(this).init("94cfc89251d9bf3c",
						"8414c02cc56ea413", false);
				// ���ʹ�û��ֹ�棬����ص��û��ֹ��ĳ�ʼ���ӿ�:
				net.youmi.android.offers.OffersManager.getInstance(this).onAppLaunch();
		//tiaoguo();
				
				//push
				
				// ��apikey�ķ�ʽ��¼��һ�������Activity��onCreate��
				PushManager.startWork(getApplicationContext(),
						PushConstants.LOGIN_TYPE_API_KEY, 
						Utils.getMetaValue(MainActivity.this, "api_key"));
				//PushManager.
				
				//PushManager.setTags(getApplicationContext(),);
				//�����Զ����֪ͨ��ʽ�������ʹ��ϵͳĬ�ϵĿ��Բ�����δ���
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
				//Log.e("log", "������");
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
                 //app �Ӻ�̨���ѣ�����ǰ̨
         	
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
					
					AlertDialog dialog = new AlertDialog.Builder(MainActivity.this).setTitle("��ܰ��ʾ").setMessage("��ȷ�����Ѽ�ס����ڣ����غ�λ�ò��䡣���Կ����Ͻ���...")  
	                        .setPositiveButton("�Ҽ�ס��", new DialogInterface.OnClickListener(){  
	                            @Override  
	                            public void onClick(DialogInterface dialog, int which) {  
	                            	UserEntity user=new UserEntity();
	            					user.setIsHideLock("1");
	            					DataOperate.updateuser(user);
	            					hidelayout.setVisibility(View.GONE);
	            			    	TouchView.setVisibility(View.VISIBLE);
	            			    	init.GetUserEntity();
	            			    	//Log.e("log", "������"+user.getIsHideLock());
	                            }  
	                        }).setNegativeButton("ȡ������", new DialogInterface.OnClickListener() {  
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
                    //�����µ�ʱ��
                    case (MotionEvent.ACTION_DOWN):
                    		IsOpen=true;
                            Display("ACTION_DOWN",event);
                    break;
                 
                    
                     //��������ʱ��
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
                    //�����µ�ʱ��
                    case (MotionEvent.ACTION_DOWN):
                    	if (!isChecked) {
                    		try {
                    			sp.play(hit, 1, 1, 0, 0, 1); 
                        		rLayout.setBackgroundResource(R.drawable.on);
                        		
                        		
                                    	
                                        	camera = Camera.open();
                        					camera.startPreview();
                        					parameters = camera.getParameters();
                        					parameters.setFlashMode(Parameters.FLASH_MODE_TORCH);// ����
                        					camera.setParameters(parameters);
                        					 camera.startPreview(); // ��ʼ����
                        					isChecked=true;
                                    

							} catch (Exception e) {
								//isChecked=false;// TODO: handle exception
							}
                    		

        				} else {
        					try {
        						
            					sp.play(hit, 1, 1, 0, 0, 1); 
            					parameters.setFlashMode(Parameters.FLASH_MODE_OFF);// �ر�
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
                    //�����ϵ�ʱ��
                    
                    
                    }
                    return true;
            }
    });
	}
	 public void Display(String eventType,MotionEvent event){
	    	if(eventType=="ACTION_DOWN")//��ָ�մ�����������ʱ
	    	{
	    		width=TouchView.getWidth();//��ȡ����������
	    		downX=(int) event.getX();//��ȡ�մ��������X����
	    		
	    	}
	    	if(eventType=="ACTION_MOVE")//���л���ʱ
	    	{
	    		int x =(int) event.getX();//��ȡX����
	            
	           int num= x-downX;//���㵱ǰX����͸մ���ʱX����Ĳ�ֵ
	           if(num>width*0.7)//�����ֵ���ڽ��������ȵ�70%����ô����
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
	// ʹ��onkeydown�������ؼ�

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
				Toast.makeText(getApplicationContext(), "�ٰ�һ���˳�����",
						Toast.LENGTH_SHORT).show(); // ����handler�ӳٷ��͸���״̬��Ϣ
				mHandler.sendEmptyMessageDelayed(0, 2000);
			} else {
				
				ApplicationEx.getInstance().exit();
		          // System.exit(0)�ǽ�������������������ݶ�ͣ����
								// ����dispose()ֻ�ǹر�������ڣ����ǲ�û��ֹͣ����application
								// exit()
								// ��������Σ��ڴ涼�ͷ��ˣ�Ҳ����˵��JVM���ر��ˣ��ڴ�����������ܻ���ʲô����
				// System.exit(0)�������˳����򣬶�System.exit(1)����˵��0��ʾ�������˳�����
				// System.exit(status)����statusΪ��ֵ�����˳����򡣺�return
				// ��������²�ͬ�㣺return�ǻص���һ�㣬��System.exit(status)�ǻص����ϲ�
			}
		}
		return false;
	}

	protected void dialog() {
		AlertDialog.Builder builder = new Builder(MainActivity.this);
		builder.setMessage("���ߣ�");

		builder.setTitle("����");

	

		
	}

	
	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// TODO Auto-generated method stub
		if (item.getItemId() == 2) {
			Toast.makeText(MainActivity.this, "�͹ټǵ��´�����Ŷ", 1).show();
			finish();
			System.exit(0);
			// dialog();
		} else if (item.getItemId() == 1) {
			dialog();
			// Toast.makeText(MainActivity.this, "���ߣ��Ϻ�", 1).show();

		}
		return super.onOptionsItemSelected(item);
	}

	
	
	
	
	
}
