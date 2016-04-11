package com.kgdset.photovault;


import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.util.Iterator;
import java.util.List;

import net.youmi.android.AdManager;
import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;
import net.youmi.android.dev.AppUpdateInfo;
import net.youmi.android.dev.CheckAppUpdateCallBack;
import net.youmi.android.offers.OffersAdSize;
import net.youmi.android.offers.OffersBanner;
import net.youmi.android.offers.PointsManager;

import android.R.integer;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.Vibrator;
import android.provider.MediaStore;
import android.app.Activity;
import android.app.ActivityManager;
import android.app.AlertDialog;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.format.Time;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.AdapterView.OnItemLongClickListener;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import com.kgdset.photovault.wxapi.WXEntryActivity;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.SendMessageToWX;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.tencent.mm.sdk.openapi.WXMediaMessage;
import com.tencent.mm.sdk.openapi.WXWebpageObject;

import com.baidu.mobstat.StatService;


public class ListViewActivity extends Activity  implements OnItemClickListener, OnItemLongClickListener{

	public TextView titletext;
	private TextView wxfriendtxt;
	private TextView wxtxt;
	public ImageView titleback;
	private NoteBookAdapter noteBookAdapter;
	private int currentPosition = -1;
	private int mSingleChoiceID = -1;
	private ListView noteBookslist;
	private String[] mItems;
	private InputMethodManager imm;
	private List<folders> folders;
private ImageButton imagebtn;
private RelativeLayout linear;
public boolean IsGocamera=false;
private ImageButton imagelistbtn;
private ImageButton settingbtn;
private LinearLayout settinglinear;
private RelativeLayout changepwdbtn;
private RelativeLayout sharebtn;
private RelativeLayout sharetowxbtn;
//private RelativeLayout jfqbtn;
private RelativeLayout jfq_dm_btn;
private RelativeLayout aboutbtn;
private RelativeLayout pingfenbtn;
private RelativeLayout updatebtn;
private RelativeLayout themebtn;
private TextView jifentxt;
public SlipButton slipbutton;
public SlipButton hidelockbtn;
public SlipButton pwdtypebutton;
public SlipButton Adbutton;
private IWXAPI api;


public static ListViewActivity instance = null; 
private void regToWx()
{
	api=WXAPIFactory.createWXAPI(this, StaticParameter.APP_wx_ID,true);
	api.registerApp(StaticParameter.APP_wx_ID);
	}
public void SetAdve()
{
	AdwoView.SetAdwo(this,(ViewGroup) findViewById(R.id.ReLayout),0);
	
}



	@Override
	public void onCreate(Bundle savedInstanceState) {
		//requestWindowFeature(Window.FEATURE_NO_TITLE);	//去掉标题�?
		super.onCreate(savedInstanceState);
 //
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		 setContentView(R.layout.activity_list_view);
		ApplicationEx.getInstance().addActivity(this);
		 
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制�?
		 instance=this;
		 init.getparameter(this);
		 //SetAdve();
		 regToWx();
		 initView();
		 setData();
		linear=(RelativeLayout) findViewById(R.id.ReLayout); 
		linear.setOnClickListener(onclick);
		// 为ListView设置列表项点击监听器
		
		checkjifen();
		 checkAppUpdate(1);	
	}
	
	  /**
     * 单击事件
     */
    private OnClickListener onclick = new OnClickListener()
    {
        @Override
        public void onClick(View v)
        {
            switch (v.getId())
            {
            // 点击空白外让其消失
                case R.id.ReLayout:
                	currentPosition = -1;
            		noteBookAdapter.notifyDataSetChanged();
                    break;
                default:
                    break;
            }
        }
    };
    public boolean onKeyDown(int keyCode, KeyEvent event) {  
        if (keyCode == KeyEvent.KEYCODE_BACK) {  
        	 finish();
         //设置切换动画，从右边进入，左边
        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
     
        }
		return false; 
    }
    @Override
    protected void onStop() {
            // TODO Auto-generated method stub
            super.onStop();

            if (!isAppOnForeground()) {
                    //app 进入后台
            	
            	//super.onDestroy();
            	if(!IsGocamera)
            	{
            		if(StaticParameter.IsSwitch)
                	{
            	finish();
            	System.exit(0);
                	}
            	}
                    //全局变量isActive = false 记录当前已经进入后台
            }
    }
   
   
    /**
     * 程序是否在前台运行
     * 
     * @return
     */
    public boolean isAppOnForeground() {
            // Returns a list of application processes that are running on the
            // device
             
            ActivityManager activityManager = (ActivityManager) getApplicationContext().getSystemService(Context.ACTIVITY_SERVICE);
            String packageName = getApplicationContext().getPackageName();

            List<RunningAppProcessInfo> appProcesses = activityManager
                            .getRunningAppProcesses();
            if (appProcesses == null)
                    return false;

            for (RunningAppProcessInfo appProcess : appProcesses) {
                    // The name of the process that this object is associated with.
                    if (appProcess.processName.equals(packageName)
                                    && appProcess.importance == RunningAppProcessInfo.IMPORTANCE_FOREGROUND) {
                            return true;
                    }
            }

            return false;
    } 
    public void showToast(final int max) {
    	
		
			
		
		
		}
		
	
    private void checkjifen()
    {
    	if(VersionOperate.IsAddIntegral(StaticParameter.wxfriendnumber))
		{
			wxfriendtxt.setText("每天第一次分享  积分+"+StaticParameter.wxfriendnumber+"");
		}
		else
		{
			wxfriendtxt.setText("");
		}
		if(VersionOperate.IsAddIntegral(StaticParameter.wxnumber))
		{
			wxtxt.setText("每天第一次分享  积分+"+StaticParameter.wxnumber+"");
		}
		else
		{
			wxtxt.setText("");
		}
	
		int youmi=PointsManager.getInstance(this).queryPoints();
		
		if(youmi>StaticParameter.integral)
		{
			StaticParameter.integral=youmi;
			StatService.onEvent(ListViewActivity.this, "5", "有米积分+"+youmi+"", 1);
		}
		else
		{
			
			PointsManager.getInstance(this).awardPoints(StaticParameter.integral-youmi);
		}
    }
    @Override
protected void onPause()
     {
     	super.onPause();
     	StatService.onPause(this);
     }
	@Override
	protected void onResume() {
		updateNoteBookList();
		 StatService.onResume(this);
		
		super.onResume();
		
		ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.ReLayout),(ViewGroup)findViewById(R.id.title_bar),getAssets());
		checkjifen();
		jifentxt.setText("当前积分:"+Integer.toString(StaticParameter.integral));
		VersionOperate.SetIntegral(StaticParameter.integral);
		if(StaticParameter.IsPwdType)
		{
			pwdtypebutton.setChecked(false);//设置button的初始化状态
		}
		else
		{
			pwdtypebutton.setChecked(true);
		}
	}
    private void initView() {
    	
		noteBookslist=(ListView) findViewById(R.id.listView1);
		imagebtn=(ImageButton) findViewById(R.id.imageButton2);
		titletext=(TextView)findViewById(R.id.titletext);
		wxfriendtxt=(TextView)findViewById(R.id.wxfrendtxt);
		wxtxt=(TextView)findViewById(R.id.wxtxt);
		titletext.setText("私密相册");
		titleback=(ImageView)findViewById(R.id.backbtn);
		jifentxt=(TextView)findViewById(R.id.jifentxt);
		jifentxt.setText("当前积分:"+Integer.toString(StaticParameter.integral));
		imagelistbtn=(ImageButton) findViewById(R.id.ImageBtn);
		settingbtn=(ImageButton) findViewById(R.id.settingbtn);
		settinglinear=(LinearLayout) findViewById(R.id.settinglinnear);
		slipbutton = (SlipButton) findViewById(R.id.wiperSwitch1);
		hidelockbtn=(SlipButton) findViewById(R.id.hidelockswitch);
		pwdtypebutton=(SlipButton) findViewById(R.id.PwdSwitch);
		Adbutton=(SlipButton) findViewById(R.id.adbtn);
		changepwdbtn=(RelativeLayout)findViewById(R.id.changepwd);
		sharebtn=(RelativeLayout)findViewById(R.id.share);
		sharetowxbtn=(RelativeLayout)findViewById(R.id.sharetowx);
		aboutbtn=(RelativeLayout)findViewById(R.id.aboutbtn);
		pingfenbtn=(RelativeLayout)findViewById(R.id.pingfen);
		updatebtn=(RelativeLayout)findViewById(R.id.updatebtn);
		themebtn=(RelativeLayout)findViewById(R.id.theme);
		//jfqbtn=(RelativeLayout)findViewById(R.id.jfbtn);
		jfq_dm_btn=(RelativeLayout)findViewById(R.id.jf_dm_btn);
		
				
				
				
						
				
		hidelockbtn.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				UserEntity entity=new UserEntity();
				//Log.e("log", "" + isChecked);
	            if(isChecked)
	            {
	            	entity.setIsHideLock("0");
	            	
	            	
	            }
	            else
	            {
	            	
	            	entity.setIsHideLock("1");
	            	
	            }
	            DataOperate.updateuser(entity);
	            init.GetUserEntity();
			}
			
		});
		slipbutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

					@Override
					public void onCheckedChanged(CompoundButton buttonView,
							boolean isChecked) {
						UserEntity entity=new UserEntity();
						//Log.e("log", "" + isChecked);
			            if(isChecked)
			            {
			            	entity.setbackstageswitch("1");
			            }
			            else
			            {
			            	entity.setbackstageswitch("0");
			            }
			            DataOperate.updateuser(entity);
			            init.GetUserEntity();
					}
					
				});
		pwdtypebutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				//Log.e("log", "" + isChecked);
	            if(isChecked)
	            {
	            	if(StaticParameter.IsPwdType)
	            	{
	            	
	  	          Intent intent=new Intent();
	        		intent.putExtra("type", "change");
	    			intent.setClass(ListViewActivity.this, LoginActivity.class);
	    			 startActivity(intent);
	    			 overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
	            	}
	            	
	            }
	            else
	            {
	            	if(!StaticParameter.IsPwdType)
	            	{
	            		if(VersionOperate.IsOpenPattern())
	            		{
	            			StatService.onEvent(ListViewActivity.this, "1", "开启图案解锁", 1);
	  	            Intent intent=new Intent();
	        		intent.putExtra("type", "3");
	    			intent.setClass(ListViewActivity.this, PwdActivity.class);
	    			 startActivity(intent);
	    			 overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
	            		}
	            		else
	            		{
	            			pwdtypebutton.setChecked(true);
	            			new AlertDialog.Builder(ListViewActivity.this).setTitle("温馨提示").setMessage("积分达到"+StaticParameter.PatternIntegral+"才能开启此功能，加油吧！").setPositiveButton("确定", null).show();
	            		}
	            		}
	            	}
	          
			}
			
		});
		Adbutton.setOnCheckedChangeListener(new OnCheckedChangeListener() {

			@Override
			public void onCheckedChanged(CompoundButton buttonView,
					boolean isChecked) {
				
				//Log.e("log", "" + isChecked);
	            if(isChecked)
	            {
	            	VersionOperate.changeAd(false);
	            }
	            else
	            {
	            	if(VersionOperate.IsClosePattern())
            		{
	            		StatService.onEvent(ListViewActivity.this, "2", "关闭广告", 1);
	            		VersionOperate.changeAd(true);
            		}
            		else
            		{
            			Adbutton.setChecked(true);
            			new AlertDialog.Builder(ListViewActivity.this).setTitle("温馨提示").setMessage("积分达到"+StaticParameter.CloseAdIntegral+"才能开启此功能，加油吧！").setPositiveButton("确定", null).show();
            		}
	            	
	          
	            }
	            }
			
		});
        //设置监听
	
		if(StaticParameter.IsSwitch)
		{
		slipbutton.setChecked(false);//设置button的初始化状
		}
		else
		{
			slipbutton.setChecked(true);
		}
		if(StaticParameter.IsHideLock)
		{
			hidelockbtn.setChecked(true);//设置button的初始化状态
		}
		else
		{
			hidelockbtn.setChecked(false);
		}
		if(StaticParameter.IsPwdType)
		{
			pwdtypebutton.setChecked(false);//设置button的初始化状态
		}
		else
		{
			pwdtypebutton.setChecked(true);
		}
		if(StaticParameter.AdOpen)
		{
			Adbutton.setChecked(false);//设置button的初始化状态
		}
		else
		{
			Adbutton.setChecked(true);
		}
		imagelistbtn.setOnClickListener(mClickListener);
		settingbtn.setOnClickListener(mClickListener);
		titleback.setOnClickListener(mClickListener);
		imagebtn.setOnClickListener(mClickListener);
		changepwdbtn.setOnClickListener(mClickListener);
		//jfqbtn.setOnClickListener(mClickListener);
		jfq_dm_btn.setOnClickListener(mClickListener);
		sharebtn.setOnClickListener(mClickListener);
		sharetowxbtn.setOnClickListener(mClickListener);
		aboutbtn.setOnClickListener(mClickListener);
		pingfenbtn.setOnClickListener(mClickListener);
		updatebtn.setOnClickListener(mClickListener);
		themebtn.setOnClickListener(mClickListener);
		noteBookslist.setOnItemClickListener(this);
		noteBookslist.setOnItemLongClickListener(this);
		//noteBookslist.setBackgroundResource(R.drawable.app_list_corner_round);
	}
  
    
   /*
   @Override
    public void OnChanged(SlipButton wiperSwitch, boolean checkState) {
    	UserEntity entity=new UserEntity();
    	
            Log.e("log", "" + checkState);
            if(checkState)
            {
            	entity.setbackstageswitch("0");
            }
            else
            {
            	entity.setbackstageswitch("1");
            }
            SessionFileManager.updateuser(entity);
            init.GetUserEntity();
    }
*/
    @Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		super.onActivityResult(requestCode, resultCode, data);
		 IsGocamera=false;
		if (resultCode == Activity.RESULT_OK) {

			String sdStatus = Environment.getExternalStorageState();
			if (!sdStatus.equals(Environment.MEDIA_MOUNTED)) { // �?��sd是否可用
				Log.v("TestFile",
						"SD card is not avaiable/writeable right now.");
				return;
			}
			//将保存在本地的图片取出并缩小后显示在界面上  
            Bitmap bitmap = BitmapUtilities.readBitmapAutoSize(Environment.getExternalStorageDirectory()+"/image.jpg",init.Pwidth,init.Pheight);  
            DataOperate.LoadPhotoCarema(bitmap,"相机照片");
			 Toast.makeText(getApplicationContext(), "已保存至相机照片目录", Toast.LENGTH_SHORT)
            .show();
            //由于Bitmap内存占用较大，这里需要回收内存，否则会报out of memory异常  
            bitmap.recycle();  
            File file=new File(Environment.getExternalStorageDirectory()+"/image.jpg");
            file.delete();
		
			
		}
	}
	public void setData()
	{
		//keyboard(false);
		
		folders = DataOperate.loadSessionList();
        if (noteBookAdapter == null) {
			noteBookAdapter = new NoteBookAdapter(this, folders);
			noteBookslist.setAdapter(noteBookAdapter);
			//setListViewHeightBasedOnChildren(noteBookslist);
		} else {
			noteBookAdapter.notifyDataSetChanged();
			//setListViewHeightBasedOnChildren(noteBookslist);
		}
    
}
	
	private OnClickListener mClickListener = new OnClickListener() {
		@Override
		public void onClick(View v) {
			switch (v.getId()) {
			case R.id.imageButton2:
				 IsGocamera=true;
				 Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
				 Uri imageUri = Uri.fromFile(new File(Environment.getExternalStorageDirectory(),"image.jpg"));  
				//指定照片保存路径（SD卡），image.jpg为一个临时文件，每次拍照后这个图片都会被替换  
				 intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri); 
				 startActivityForResult(intent, 0);  
				break;
			case R.id.backbtn:
				 finish();
		         //设置切换动画，从右边进入，左边
		        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
				break;
			case R.id.settingbtn:
				settingbtn.setBackgroundResource(R.drawable.tabbar2_selected);
				imagelistbtn.setBackgroundResource(R.drawable.tabbar_01);
				noteBookslist.setVisibility(View.GONE);
				settinglinear.setVisibility(View.VISIBLE);
				titletext.setText("设置");
				titleback.setVisibility(View.GONE);
				 overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				
				break;
			case R.id.ImageBtn:
				settingbtn.setBackgroundResource(R.drawable.tabbar_2);
				imagelistbtn.setBackgroundResource(R.drawable.tabbar_selected);
				noteBookslist.setVisibility(View.VISIBLE);
				settinglinear.setVisibility(View.GONE);
				titletext.setText("私密相册");
				titleback.setVisibility(View.VISIBLE);
				 overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				break;
			case R.id.changepwd:
				 Intent intent1=new Intent();
	        	   intent1.putExtra("type", "1");
	        	   if(StaticParameter.IsPwdType)
	        	   {
	        		   
	        		   intent1.setClass(ListViewActivity.this, SetPasswordActivity.class);
	        	   }
	        	   else
	        	   {
	    			intent1.setClass(ListViewActivity.this, PwdActivity.class);
	        	   }
	    			startActivity(intent1);
	    			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
				break;
			case R.id.aboutbtn:
				 Intent intent11=new Intent();
	        	 
	    			intent11.setClass(ListViewActivity.this, AboutActivity.class);
	    			startActivity(intent11);
	    			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
	    		break;
			case R.id.pingfen:
				Uri uri = Uri.parse("market://details?id="+getPackageName());
				Intent pingfen = new Intent(Intent.ACTION_VIEW,uri);
				pingfen.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
				startActivity(pingfen);

	    		break;
			case R.id.theme:
				 Intent intent12=new Intent();
	        	 
	    			intent12.setClass(ListViewActivity.this, ThemeActivity.class);
	    			startActivity(intent12);
	    			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
	    		break;
			case R.id.updatebtn:
				checkAppUpdate(0);	
	    		break;
			
			case R.id.jf_dm_btn:
				//打开积分墙
				net.youmi.android.offers.OffersManager.getInstance(ListViewActivity.this).showOffersWall();
				break;
			case R.id.share:
				StaticParameter.addnumber=StaticParameter.wxfriendnumber;
			
				Share("wx1");		
				break;
			case R.id.sharetowx:
				StaticParameter.addnumber=StaticParameter.wxnumber;
				 
				Share("wx");		
				break;
			default:
				break;
			}
		}
	};
	
	private void Share(String type)
	{
		 IsGocamera=true;
		WXWebpageObject webpage = new WXWebpageObject();
		webpage.webpageUrl = "http://a.app.qq.com/o/simple.jsp?pkgname=com.kgdset.photovault&g_f=991653";
		WXMediaMessage msg = new WXMediaMessage(webpage);
		msg.title = "私密相册\n安全隐蔽，很不错哦，快来试试吧！";
		msg.description = "我正在使用私密相册，安全隐蔽，很不错哦，你也来试试吧！";
		Bitmap thumb = BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher);
		msg.thumbData = Util.bmpToByteArray(thumb, true);
		
		SendMessageToWX.Req req = new SendMessageToWX.Req();
		req.transaction = buildTransaction("webpage");
		req.message = msg;
		if(type.equals("wx"))
		{
		req.scene = SendMessageToWX.Req.WXSceneSession;
		}
		else
		{
			req.scene = SendMessageToWX.Req.WXSceneTimeline;
		}
		api.sendReq(req);
	}
	
	private String buildTransaction(final String type) {
		return (type == null) ? String.valueOf(System.currentTimeMillis()) : type + System.currentTimeMillis();
	}

	private void updateNoteBookList() {
		if (noteBookAdapter != null) {
			noteBookAdapter=null;
			setData();
			noteBookAdapter.notifyDataSetChanged();
			
		}
	}


	public void Add(String name) throws Exception {
		int StrLength=name.length();
		if(StrLength>0)
		{
			if(!(StrLength>10))
			{
			boolean IsExist=false;
			Iterator<folders> itr = folders.iterator();
			while (itr.hasNext()) {
				folders nextObj = itr.next();
				String getname=nextObj.getName();
				if(getname.equals(name))
				{
					IsExist=true;
					break;
				}
			}
			if(!IsExist)
			{
		folders folders = new folders();
		folders.setId(System.currentTimeMillis());
		folders.setName(name);
		folders.setType("1");
		if(DataOperate.addfolder(folders))
		{
		DataOperate.CreateFolders(Long.toString(folders.getId()));
		onResume();
		keyboard(false);
		}
		else
		{
			 Toast.makeText(getApplicationContext(), "目录添加失败", Toast.LENGTH_SHORT)
             .show();
		}
			}
			else
			{
				 Toast.makeText(getApplicationContext(), "目录名重复！", Toast.LENGTH_SHORT)
	             .show();
			}
			}
			else
			{

				 Toast.makeText(getApplicationContext(), "目录名不能超10个字符！", Toast.LENGTH_SHORT)
	             .show();
			}
		}
		else
		{
			 Toast.makeText(getApplicationContext(), "目录名不能为空！", Toast.LENGTH_SHORT)
             .show();
			// TODO Aut
		}
		}
	public boolean EditFolder(String name,String newname){
		boolean IsSuccess=false;
		int StrLength=newname.length();
		if(StrLength>0)
		{
			if(!(StrLength>10))
			{
			boolean IsExist=false;
			Iterator<folders> itr = folders.iterator();
			while (itr.hasNext()) {
				folders nextObj = itr.next();
				String getname=nextObj.getName();
				if(getname.equals(newname))
				{
					IsExist=true;
					break;
				}
			}
			if(!IsExist)
			{
		folders folders = new folders();
		folders.setId(1);
		folders.setName(name);
		folders.setType("1");
		IsSuccess=DataOperate.updatefolder(name, newname);
		onResume();
		keyboard(false);
			}
			else
			{
				 Toast.makeText(getApplicationContext(), "目录名重复！", Toast.LENGTH_SHORT)
	             .show();
			}
			}
			else
			{

				 Toast.makeText(getApplicationContext(), "目录名不能超过10个字符！", Toast.LENGTH_SHORT)
	             .show();
			}
		}
		else
		{
			 Toast.makeText(getApplicationContext(), "目录名不能为空！", Toast.LENGTH_SHORT)
             .show();
			// TODO Aut
		}
		return IsSuccess;
		}
	

	@Override
	public void onItemClick(AdapterView<?> parent, View view,
			int position, long id) {
		
			 final TextView tv = (TextView)view.findViewById(R.id.itemtxt1);
			 final TextView folderid = (TextView)view.findViewById(R.id.txtid);
			 final EditText et=(EditText)view.findViewById(R.id.editText1);
			 final Button btn1=(Button)view.findViewById(R.id.button1);
			 final Button btn2=(Button)view.findViewById(R.id.button2);
				final LinearLayout linear2=(LinearLayout)view.findViewById(R.id.Linear2); 
			 if(tv.getText().toString().equals("新建目录"))
			 {
				 tv.setVisibility(8);
				 linear2.setVisibility(0);
				 et.setVisibility(0);
				 et.requestFocus();
				 keyboard(true);
			  
			 btn1.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)
			 {
				 try {
					Add(et.getText().toString());
					
					
				} catch (Exception e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			 }
			 });
			 btn2.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)//点击
			 {
				 tv.setVisibility(0);
				 linear2.setVisibility(8);
					 et.setVisibility(8);
					 keyboard(false);
			 }
			 });
			 }
			 else
			 {
				 Intent intent = new Intent(this, ListViewActivity.class); 
				 intent.putExtra("id", folderid.getText().toString()); 
				 intent.putExtra("name", tv.getText().toString()); 
				  
				 intent.setClass(ListViewActivity.this,PhotoGridViewActivity.class);
		         startActivity(intent);
		        
		         overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				 //startActivityForResult(intent, REQUEST_TEXT); 
			 }
			// Toast.makeText(getApplicationContext(), "您点击的是：" + tv.getText()+"dfs:"+id, Toast.LENGTH_SHORT)
             //.show();
			// TODO Auto-generated method stub
			
		}
	private void checkAppUpdate(final int type) {
		AdManager.getInstance(ListViewActivity.this).asyncCheckAppUpdate(
				new CheckAppUpdateCallBack() {

					@Override
					public void onCheckAppUpdateFinish(
							AppUpdateInfo appUpdateInfo) {
						if (appUpdateInfo == null) {
							if(type==0)
							{
							Toast.makeText(ListViewActivity.this, "当前版本已经是最新版",Toast.LENGTH_SHORT).show();
							}
						} else {
							// 获取版本号
							int versionCode = appUpdateInfo.getVersionCode();
							// 获取版本
							String versionName = appUpdateInfo.getVersionName();
							// 获取新版本的信息
							String updateTips = appUpdateInfo.getUpdateTips();
							// 获取apk下载地址
							final String downloadUrl = appUpdateInfo.getUrl();

							AlertDialog updateDialog = new AlertDialog.Builder(
									ListViewActivity.this)
									.setIcon(android.R.drawable.ic_dialog_info)
									.setTitle("发现新版本 " + versionName)
									.setMessage(updateTips)
									.setPositiveButton(
											"更新",
											new DialogInterface.OnClickListener() {
												@Override
												public void onClick(
														DialogInterface dialog,
														int which) {
													try {
														Intent intent = Intent
																.parseUri(
																		downloadUrl,
																		Intent.FLAG_ACTIVITY_NEW_TASK);
														startActivity(intent);
													} catch (Exception e) {
														// TODO: handle
														// exception
													}
												}
											}).setNegativeButton("下次再说", null)
									.create();
							updateDialog.show();
						}
					}
				});
	}

	
	/**
	 长按操作
	 */

	@Override
	public boolean onItemLongClick(AdapterView<?> listview, View arg1, int position, long arg3) {
		 final TextView tv = (TextView)arg1.findViewById(R.id.itemtxt1); 
		 if(tv!=null)
		 {
		 if(!tv.getText().toString().equals("新建目录")&&!tv.getText().toString().equals("相机照片"))
		 {
		Vibrator vibrator = (Vibrator) getSystemService(VIBRATOR_SERVICE);
		vibrator.vibrate(40);
		currentPosition = position;
		noteBookAdapter.notifyDataSetChanged();
		 }
		
		 }
		 return true;
	}
	
	public void keyboard(Boolean Open)
	{
		
		imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);  
		  
		if(Open)
		{
			
          
            //显示软键�?
			imm.toggleSoftInput(0, InputMethodManager.HIDE_NOT_ALWAYS); 
		}
		else
		{
			boolean isOpen=imm.isActive(getCurrentFocus());//isOpen若返回true，则表示输入法打�?
			  //隐藏软键�?
		if(isOpen)
		{
	          imm.hideSoftInputFromWindow(linear.getWindowToken(), 0);
		}
		}
		
	}
	

	
	private Handler handler = new Handler() {
		public void handleMessage(android.os.Message msg) {
			if (msg.what == 1) {
				noteBookAdapter.notifyDataSetChanged();
			}
		}
	};
	
	class NoteBookAdapter extends BaseAdapter {
		private Context context;
		private LayoutInflater inflater;
		private List<folders> fileTypeList;

		
		
		public NoteBookAdapter(Activity activity, List<folders> fileTypeList) {
			this.context = activity;
			this.fileTypeList = fileTypeList;
			inflater = LayoutInflater.from(context);
		}

		public int getCount() {

			return fileTypeList.size();
		}

		public int getItemViewType(int position) {
			return position;
		}

		public Object getItem(int arg0) {
			return fileTypeList.get(arg0);
		}

		public long getItemId(int position) {
			return position;
		}

		public View getView(final int position, View convertView, ViewGroup parent) {
		
			ViewHolder holder = null;
			if (convertView == null) {
				convertView = inflater.inflate(R.layout.list_items, parent, false);
				holder = new ViewHolder();
				
				holder.Name = (TextView) convertView.findViewById(R.id.itemtxt1);
				holder.Image1 = (ImageView) (ImageView)convertView.findViewById(R.id.imageView1);
				holder.Image2 = (ImageView) (ImageView)convertView.findViewById(R.id.imageView2);
				holder.imageLayout=(LinearLayout)convertView.findViewById(R.id.imageLayout);
				holder.linearLayout = (LinearLayout) convertView.findViewById(R.id.layout_other);
				holder.openLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_open);
				holder.editLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_edit);
				holder.moveLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_move);
				holder.deleteLinearLayout = (LinearLayout) convertView.findViewById(R.id.item_delete);
				holder.folderid=(TextView) convertView.findViewById(R.id.txtid);
				 final TextView tv = (TextView)convertView.findViewById(R.id.itemtxt1); 
				 final EditText et=(EditText)convertView.findViewById(R.id.editText1);
				 final Button btn1=(Button)convertView.findViewById(R.id.button1);
				 final Button btn2=(Button)convertView.findViewById(R.id.button2);
					final LinearLayout linear2=(LinearLayout)convertView.findViewById(R.id.Linear2); 
				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			String name = fileTypeList.get(position).getName();
		    String Type=fileTypeList.get(position).getType();
		    String ID= Long.toString(fileTypeList.get(position).getId());
			holder.Name.setText(name);
			holder.folderid.setText(ID);
			if(Type.equals("0")&&name.equals("新建目录"))
			{
				holder.Image1.setImageResource(R.drawable.index_add);
				holder.Image2.setVisibility(8);
				holder.imageLayout.setVisibility(8);
			
			}
			else
			{
				holder.Image1.setImageResource(R.drawable.index_file);
				holder.Image2.setImageResource(R.drawable.arrow);
				holder.Image2.setVisibility(0);
				holder.imageLayout.setVisibility(0);
			}
			if (position == currentPosition) {
				
				holder.linearLayout.setVisibility(View.VISIBLE);
				holder.openLinearLayout.setClickable(true);
				holder.editLinearLayout.setClickable(true);
				holder.moveLinearLayout.setClickable(true);
				holder.deleteLinearLayout.setClickable(true);
				holder.openLinearLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						currentPosition = -1;
						 Intent intent = new Intent(); 
							
							String name=fileTypeList.get(position).getName();
							Long id=fileTypeList.get(position).getId();
						 intent.putExtra("id", id.toString()); 
						 intent.putExtra("name", name); 
						  
						 intent.setClass(ListViewActivity.this,PhotoGridViewActivity.class);
				         startActivity(intent);
				        
				         overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
					}
				});
				holder.editLinearLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						String name=fileTypeList.get(position).getName();
						AlertDialog.Builder builder = new AlertDialog.Builder(ListViewActivity.this);
						builder.setTitle("请输入新目录");
					 	builder.setIcon(android.R.drawable.ic_dialog_info);
					 	final  EditText edittext=new EditText(ListViewActivity.this);
					 	edittext.setText(name);
					 	edittext.selectAll();
                        //edittextlog.setView(edittext);
					 	builder.setView(edittext);
					 	builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
								String name=fileTypeList.get(position).getName();
								String newname=edittext.getText().toString();
								
								if(!name.equals(newname))
								{
								
								Boolean result=EditFolder(name,newname);
								if(result)
								{
									fileTypeList.get(position).setName(newname);
									currentPosition = -1;
									handler.sendEmptyMessage(1);
									Toast.makeText(getApplicationContext(), "修改成功", Toast.LENGTH_SHORT)
						            .show();
								}
								else
								{
									Toast.makeText(getApplicationContext(), "修改失败", Toast.LENGTH_SHORT)
						            .show();
								}
								}	

							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

							}
						});
						builder.create().show();
						//currentPosition = -1;

					}
				});
				holder.deleteLinearLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						AlertDialog.Builder builder = new AlertDialog.Builder(ListViewActivity.this);
						mSingleChoiceID = -1;
						builder.setIcon(android.R.drawable.ic_dialog_info);
						builder.setTitle("确认要删除此目录吗？");
						builder.setSingleChoiceItems(mItems, 0, new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								mSingleChoiceID = whichButton;
							}
						});
						builder.setPositiveButton("确定", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {
								
								String ID=Long.toString(fileTypeList.get(position).getId());
								fileTypeList.remove(position);
								
								Boolean result=DataOperate.delSession(ID);
								if(result)
								{
									currentPosition = -1;
									handler.sendEmptyMessage(1);
									Toast.makeText(getApplicationContext(), "删除成功", Toast.LENGTH_SHORT)
						            .show();
								}
								else
									{
									Toast.makeText(getApplicationContext(), "删除失败", Toast.LENGTH_SHORT)
						            .show();
									}

							}
						});
						builder.setNegativeButton("取消", new DialogInterface.OnClickListener() {
							public void onClick(DialogInterface dialog, int whichButton) {

							}
						});
						builder.create().show();
					}
				});
				holder.moveLinearLayout.setOnClickListener(new OnClickListener() {

					@Override
					public void onClick(View v) {
						// TODO Auto-generated method stub
						
						currentPosition = -1;
						handler.sendEmptyMessage(1);
					}
				});
			} else {
				holder.linearLayout.setVisibility(View.GONE);
				holder.openLinearLayout.setClickable(false);
				holder.editLinearLayout.setClickable(false);
				holder.moveLinearLayout.setClickable(false);
				holder.deleteLinearLayout.setClickable(false);
			}

			return convertView;
		}

		class ViewHolder {
			public TextView Name;
			public TextView folderid;
			public ImageView Image1;
			public ImageView Image2;
			public  LinearLayout imageLayout;
			public LinearLayout linearLayout;
			public LinearLayout openLinearLayout;
			public LinearLayout editLinearLayout;
			public LinearLayout moveLinearLayout;
			public LinearLayout deleteLinearLayout;
		
		}
	}
	
}
