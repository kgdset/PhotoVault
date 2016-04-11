package com.kgdset.photovault;




import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.text.Editable;
import android.text.InputFilter;
import android.text.InputType;
import android.text.TextWatcher;
import android.text.method.DigitsKeyListener;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.View.OnClickListener;
import android.view.View.OnKeyListener;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

public class PwdActivity extends BaseActivity {
	UserEntity user;
	EditText PwdEtext1;
	EditText PwdEtext2;
	EditText PwdEtext3;
	EditText PwdEtext4;
	EditText hidetext;
	TextView Textview1;
	String PwdStr="";
	public int num;
	public int newnum;
	boolean IsStart=true;
    int reg=0;//输入密码状态：0 初次设置密码  1 二次确认密码  3 登录时输入密码
    String type;
	int Pwidth;
	int Pheight;
	RelativeLayout num0;
	RelativeLayout num1;
	RelativeLayout num2;
	RelativeLayout num3;
	RelativeLayout num4;
	RelativeLayout num5;
	RelativeLayout num6;
	RelativeLayout num7;
	RelativeLayout num8;
	RelativeLayout num9;

	RelativeLayout del;
	 String Pwd1;
	 String Pwd2;
	 public TextView titletext;
		public ImageView titleback;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_pwd);
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制之
		 titleback=(ImageView)findViewById(R.id.backbtn);
		 titletext=(TextView)findViewById(R.id.titletext);
		 ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.frame1),(ViewGroup)findViewById(R.id.title_bar),getAssets());
			titleback.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)
			 {
				 finish();
			        //设置切换动画，从右边进入，左边退出
			        //overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
			        
			 }
			 });
		hidetext=(EditText)findViewById(R.id.HideText);
		 PwdEtext1=(EditText)findViewById(R.id.pwd1);
		 PwdEtext2=(EditText)findViewById(R.id.pwd2);
		 PwdEtext3=(EditText)findViewById(R.id.pwd3);
		 PwdEtext4=(EditText)findViewById(R.id.pwd4);
		 Textview1=(TextView)findViewById(R.id.Textview1);
		 Textview1.setText("1111");
		  num0=(RelativeLayout)findViewById(R.id.num0);
		  num1=(RelativeLayout)findViewById(R.id.num1);
		  num2=(RelativeLayout)findViewById(R.id.num2);
		  num3=(RelativeLayout)findViewById(R.id.num3);
		  num4=(RelativeLayout)findViewById(R.id.num4);
		  num5=(RelativeLayout)findViewById(R.id.num5);
		  num6=(RelativeLayout)findViewById(R.id.num6);
		  num7=(RelativeLayout)findViewById(R.id.num7);
		  num8=(RelativeLayout)findViewById(R.id.num8);
		  num9=(RelativeLayout)findViewById(R.id.num9);
		
		  del=(RelativeLayout)findViewById(R.id.del);
		 num0.setOnClickListener(listener);
		 num1.setOnClickListener(listener);
		 num2.setOnClickListener(listener);
		 num3.setOnClickListener(listener);
		 num4.setOnClickListener(listener);
		 num5.setOnClickListener(listener);
		 num6.setOnClickListener(listener);
		 num7.setOnClickListener(listener);
		 num8.setOnClickListener(listener);
		 num9.setOnClickListener(listener);
	
		 del.setOnClickListener(listener);
			WindowManager wm = (WindowManager) this.getSystemService(Context.WINDOW_SERVICE);
		 final int Pwidth = wm.getDefaultDisplay().getWidth();//屏幕宽度
			final int Pheight = wm.getDefaultDisplay().getHeight();//屏幕高度
			//Toast.makeText(PwdActivity.this, "宽、高"+Pwidth+"、"+Pheight, 500).show();
			int numbtnwidth=(int) (Pwidth*0.25);//设定数字键盘宽度值
			int numbtnheight=(int) (Pheight*0.1);//设定数字键盘高度值
			int Editwidth=(int) (Pwidth*0.2);//设定输入框宽度值
		
			SetEditWidth(Editwidth);//设置输入框宽度
		PwdEtext2.setKeyListener(new DigitsKeyListener(false, true));
		PwdEtext3.setKeyListener(new DigitsKeyListener(false, true));
		PwdEtext4.setKeyListener(new DigitsKeyListener(false, true));
		
	
		hidetext.addTextChangedListener(new TextWatcher(){ 
			 
            @Override 
            public void afterTextChanged(Editable s) { 
            	
            } 
 
            @Override 
            public void beforeTextChanged(CharSequence s, int start, int count, 
                    int after) { 
            	
            
            	
            } 
 
            @Override 
            public void onTextChanged(CharSequence s, int start, int before, 
                    int count) { 
            	
            	if(num==0)
            	{
            		num=s.length();
            		PwdEtext1.setText("1");
            		//Toast.makeText(PwdActivity.this, "我现在是0，获取S我是："+num, 500).show();
            	}
            	else
            	{
            		if(num<s.length())
            		{
            			switch (s.length()) {
						case 1:
							PwdEtext1.setText("1");
							break;
						case 2:
							PwdEtext2.setText("1");
							break;
						case 3:
							PwdEtext3.setText("1");
							break;
						case 4:
							PwdEtext4.setText("1");
							break;
						default:
							break;
						}
            			num=s.length();
            		//Toast.makeText(PwdActivity.this, "我现在比S小，获取S我是："+num, 500).show();
            		}
            		else
            		{
            			switch (s.length()) {
						case 0:
							PwdEtext1.setText("");
							break;
						case 1:
							PwdEtext2.setText("");
							break;
						case 2:
							PwdEtext3.setText("");
							break;
						case 3:
							PwdEtext4.setText("");
							break;
						default:
							break;
						}
            			num=s.length();
            			//Toast.makeText(PwdActivity.this, "我现在比S大，获取S我是："+num, 500).show();
            		}
            	}
            		//Toast.makeText(PwdActivity.this, "手电已开启"+s.length(), 500).show();
            		
            		
            		
            	

            } 
             
        });
		init();
	}
	OnClickListener listener = new OnClickListener()
    {
    public void onClick(View v)
    {
    	RelativeLayout btn = (RelativeLayout)v;
    	switch(btn.getId())
    	{
    	
    	case R.id.num0:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="0";
    		hidetext.setText(PwdStr);
    	}
           break;
    	case R.id.num1:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="1";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num2:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="2";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num3:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="3";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num4:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="4";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num5:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="5";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num6:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="6";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num7:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="7";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num8:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="8";
    		hidetext.setText(PwdStr);}
           break;
    	case R.id.num9:
    		if(PwdStr.length()<4)
    		{
    		PwdStr+="9";
    		hidetext.setText(PwdStr);}
           break;
    	
    
case R.id.del:
    		
    		if(PwdStr.length()>0)
    		{
    			PwdStr=PwdStr.substring(0, PwdStr.length()-1);
    			hidetext.setText(PwdStr);
    		}
           break;
       default:
           return;
   }
    	if(PwdStr.length()==4)
    	{
    		//Toast.makeText(PwdActivity.this, "提交！"+PwdStr, 500).show();
    		Register(PwdStr);
    	}
    	}
    };
    private void cleartxt()
    {
    	num=0;
    	PwdStr="";
    	PwdEtext1.setText("");
		PwdEtext2.setText("");
		PwdEtext3.setText("");
		PwdEtext4.setText("");
    }
   private void Register(String Pwd)
   {
	   if(type.equals("0"))
	   {
	   if(reg==0)
	   {
		   Pwd1=Pwd;
		   reg=1;
		   init();
		   cleartxt();
	   }
	   else if(reg==2)
	   {
		   Pwd2=Pwd;
		   if(Pwd1.equals(Pwd2))
		{
			   UserEntity user=new UserEntity();
			   user.setinitial("1");
			   user.setPwd1(Pwd1);
			   DataOperate.updateuser(user);
			   cleartxt();
			 
			   AlertDialog.Builder builder = new AlertDialog.Builder(PwdActivity.this); 
			   builder.setTitle("密码设置成功，请牢记！"); 
			   builder.setMessage("您的密码为："+Pwd);
			   	builder.setPositiveButton("确\t定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								 Intent intent=new Intent();
									intent.setClass(PwdActivity.this, ListViewActivity.class);
									startActivity(intent);
									  overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
									PwdActivity.this.finish();
							}
							});
								
			   	builder.show();
			   //Toast.makeText(PwdActivity.this, "OK！", 500).show();
		}
		else
		{
			 cleartxt();
			Toast.makeText(PwdActivity.this, "两次输入的密码不一致！", 500).show();
		}
   }
	   else if(reg==3)
	   {
		   if(Pwd.equals(user.getPwd1()))
		   {
			   cleartxt();
			 
	   Intent intent=new Intent();
		intent.setClass(PwdActivity.this, ListViewActivity.class);
		startActivity(intent);
		 //overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		  overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
		PwdActivity.this.finish();
		   }
		   else
		   {
			   cleartxt();
				Toast.makeText(PwdActivity.this, "密码有误，请重新输入！", 500).show();
		   }
	   }
	   }
	   else if(type.equals("3"))
	   {
		   if(Pwd.equals(user.getPwd1()))
		   {
			   cleartxt();
			 
 	            init.GetVersion2();
	   Intent intent=new Intent();
		intent.setClass(PwdActivity.this, LoginActivity.class);
		intent.putExtra("type", "change");
		startActivity(intent);
		 //overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
		  overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
		finish();
		   }
		   else
		   {
			   cleartxt();
				Toast.makeText(PwdActivity.this, "密码有误，请重新输入！", 500).show();
		   }
	   }
	   else if(type.equals("5"))
	   {
		   if(reg==0)
		   {
			   Pwd1=Pwd;
			   reg=1;
			   init();
			   cleartxt();
		   }
		   else if(reg==2)
		   {
			   Pwd2=Pwd;
			   if(Pwd1.equals(Pwd2))
			{
				   UserEntity user=new UserEntity();
				   user.setinitial("1");
				   user.setPwd1(Pwd1);
				   DataOperate.updateuser(user);
				   cleartxt();
				   Version2 entity=new Version2();
				   entity.setPwdType("0");
	           	entity.setPatternpwd("0");
	           	  DataOperate.UpdateVersion2(entity);
	 	            init.GetVersion2();
				   AlertDialog.Builder builder = new AlertDialog.Builder(PwdActivity.this); 
				   builder.setTitle("密码设置成功，请牢记！"); 
				   builder.setMessage("您的密码为："+Pwd);
				   	builder.setPositiveButton("确\t定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									
										finish();
										  overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
								}
								});
									
				   	builder.show();
				   //Toast.makeText(PwdActivity.this, "OK！", 500).show();
			}
			else
			{
				 cleartxt();
				Toast.makeText(PwdActivity.this, "两次输入的密码不一致！", 500).show();
			}
	   }
	   }
	   
	   else
	   {
		   if(reg==1)
		   {
			   Pwd1=Pwd;
			   reg=2;
			   init();
			   cleartxt();
		   }
		   else if(reg==2)
		   {
			   Pwd2=Pwd;
			   if(Pwd1.equals(Pwd2))
			{
				   UserEntity user=new UserEntity();
				   user.setinitial("1");
				   user.setPwd1(Pwd1);
				   DataOperate.updateuser(user);
				   cleartxt();
				   AlertDialog.Builder builder = new AlertDialog.Builder(PwdActivity.this); 
				   builder.setTitle("密码修改成功，请牢记！"); 
				   builder.setMessage("您的密码为："+Pwd);
				   	builder.setPositiveButton("确\t定", new DialogInterface.OnClickListener() {
								public void onClick(DialogInterface dialog, int whichButton) {
									
										finish();
										  overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
								}
								});
									
				   	builder.show();
				   //Toast.makeText(PwdActivity.this, "OK！", 500).show();
			}
			else
			{
				 cleartxt();
				Toast.makeText(PwdActivity.this, "两次输入的密码不一致！", 500).show();
			}
	   }
		   else if(reg==0)
		   {
			   if(Pwd.equals(user.getPwd1()))
			   {
				   Pwd1=Pwd;
				   reg=1;
				   init();
				   cleartxt();
			   }
			   else
			   {
				   cleartxt();
					Toast.makeText(PwdActivity.this, "密码有误，请重新输入！", 500).show();
			   }
		   }
	   }
   }
   public boolean onKeyDown(int keyCode, KeyEvent event) {  
       if (keyCode == KeyEvent.KEYCODE_BACK) {  
    	   finish();
     
        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
       
       }
		return false; 
   }

//设置输入框宽度
private void SetEditWidth(int width)
{
	 PwdEtext1.setWidth(width);
	 PwdEtext2.setWidth(width);
	 PwdEtext3.setWidth(width);
	 PwdEtext4.setWidth(width);
	}
private void init()
{
	Bundle extras = getIntent().getExtras(); 
	type=extras.getString("type");
	if(type.equals("0"))
	{
	if(reg==0)
	{
		user=new UserEntity();
		user= DataOperate.loadUserEntity();
	if(user!=null)
	{
		
	if(user.getinitial().equals("0"))
	{
		
		titletext.setText("请设置密码");
		Textview1.setText("请设置密码");
		Textview1.setTextSize(40);
	}
	else
	{
		reg=3;
		titletext.setText("请输入密码");
		Textview1.setText("请输入密码");
		Textview1.setTextSize(40);
	}
	}
	}
	else if(reg==1)
	{
		reg=2;
		titletext.setText("请再次输入密码");
		Textview1.setText("请再次输入密码");
		Textview1.setTextSize(40);
		
	}
	}
	else if(type.equals("3"))
	{
		user=new UserEntity();
		user= DataOperate.loadUserEntity();
	if(user!=null)
	{
		
	
		reg=3;
		titletext.setText("请先输入数字密码");
		Textview1.setText("请先输入数字密码");
		Textview1.setTextSize(40);
	
	}
	}
	else if(type.equals("5"))
	{
		if(reg==0)
		{
			user=new UserEntity();
			user= DataOperate.loadUserEntity();
		if(user!=null)
		{
			
		if(user.getinitial().equals("0"))
		{
			
			titletext.setText("请设置密码");
			Textview1.setText("请设置密码");
			Textview1.setTextSize(40);
		}
		else
		{
			reg=3;
			titletext.setText("请输入密码");
			Textview1.setText("请输入密码");
			Textview1.setTextSize(40);
		}
		}
		}
		else if(reg==1)
		{
			reg=2;
			titletext.setText("请再次输入密码");
			Textview1.setText("请再次输入密码");
			Textview1.setTextSize(40);
			
		}
	}
	else
	{
		if(reg==0)
		{
		
			user=new UserEntity();
			user= DataOperate.loadUserEntity();
			titletext.setText("请输入旧密码");
			Textview1.setText("请输入旧密码");
			Textview1.setTextSize(40);
		
		
		}
		else if(reg==1)
		{
			
			titletext.setText("请输入新密码");
			Textview1.setText("请输入新密码");
			Textview1.setTextSize(40);
			
		}
		else if(reg==2)
		{
			
			titletext.setText("请再次输入密码");
			Textview1.setText("请再次输入密码");
			Textview1.setTextSize(40);
			
		}
	}
	}
}
