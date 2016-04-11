package com.kgdset.photovault;



import com.tencent.mm.sdk.platformtools.Log;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.TextView;

public class AboutActivity extends BaseActivity {
	public ImageView titleback;
	public TextView titletext;
	private TextView versiontxt;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.activity_about);
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//��title layout�У����Զ���֮
		 titletext=(TextView)findViewById(R.id.titletext);
		 titleback=(ImageView)findViewById(R.id.backbtn);
		 titletext.setText("����");
		 versiontxt=(TextView)findViewById(R.id.versiontext);
		 versiontxt.setText("�汾��"+getAppVersionName(this));
		 ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.frame1),(ViewGroup)findViewById(R.id.title_bar),getAssets());
		 titleback.setOnClickListener(new OnClickListener()
		 {
		 public void onClick(View v)
		 {
			   finish();
			     
		        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
		 }
		 });
	}
	/**  
	* ���ص�ǰ����汾��  
	*/   
	public static String getAppVersionName(Context context) {   
	    String versionName = "";   
	    try {   
	        // ---get the package info---   
	        PackageManager pm = context.getPackageManager();   
	        PackageInfo pi = pm.getPackageInfo(context.getPackageName(), 0);   
	        versionName = pi.versionName;   
	        if (versionName == null || versionName.length() <= 0) {   
	            return "";   
	        }   
	    } catch (Exception e) {   
	        Log.e("VersionInfo", "Exception", e);   
	    }   
	    return versionName;   
	}  
	 @Override  
	    public boolean onKeyDown(int keyCode, KeyEvent event) {
	    	
	        if(keyCode == KeyEvent.KEYCODE_BACK){    //������ذ���  
	     	   finish();
	     	     
	           overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
	        }
	       return false;
	    }  

}
