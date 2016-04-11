package com.kgdset.photovault;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.kgdset.photovault.R;
import com.kgdset.photovault.LocusPassWordView.OnCompleteListener;

public class LoginActivity extends BaseActivity {
	private LocusPassWordView lpwv;
	private Toast toast;
	private TextView titletext;  
	private ImageView titleback;
	private String  LoginType;
	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
			// toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}

		toast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.login_activity);
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制之
		 titleback=(ImageView)findViewById(R.id.backbtn);
		 titletext=(TextView)findViewById(R.id.titletext);
			Bundle extras = getIntent().getExtras(); 
			LoginType=extras.getString("type");
			ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.frame1),(ViewGroup)findViewById(R.id.title_bar),getAssets());
		 titletext.setText("请输入手势密码");
			titleback.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)
			 {
				 LoginActivity.this.finish();
			        //设置切换动画，从右边进入，左边退出
			        //overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
			        
			 }
			 });
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				// 如果密码正确,则进入主页面。
				if (lpwv.verifyPassword(mPassword)) {
					if(LoginType.equals("login"))
					{
					showToast("登陆成功！");
					Intent intent = new Intent(LoginActivity.this,
							ListViewActivity.class);
					// 打开新的Activity
					startActivity(intent);
					finish();
					}
					else
					{
						Intent intent = new Intent(LoginActivity.this,
								PwdActivity.class);
						 intent.putExtra("type", "5");
			    		
						// 打开新的Activity
						startActivity(intent);
						finish();
					}
					 overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
				} else {
					showToast("密码输入错误,请重新输入");
					lpwv.clearPassword();
				}
			}
		});

	}
	   public boolean onKeyDown(int keyCode, KeyEvent event) {  
	       if (keyCode == KeyEvent.KEYCODE_BACK) {  
	    	   finish();
	     
	        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
	       
	       }
			return false; 
	   }
	@Override
	protected void onStart() {
		super.onStart();
	
		// 如果密码为空,则进入设置密码的界面
		View noSetPassword = (View) this.findViewById(R.id.tvNoSetPassword);
		TextView toastTv = (TextView) findViewById(R.id.login_toast);
		if (lpwv.isPasswordEmpty()) {
			lpwv.setVisibility(View.GONE);
			noSetPassword.setVisibility(View.VISIBLE);
			
			toastTv.setText("请绘制手势密码");
			noSetPassword.setOnClickListener(new OnClickListener() {
				@Override
				public void onClick(View v) {
					
					Intent intent = new Intent(LoginActivity.this,
							SetPasswordActivity.class);
					// 打开新的Activity
					
					startActivity(intent);
					finish();
					  overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
				}

			});
				
			
		} else {
			if(LoginType.equals("login"))
			{
			toastTv.setText("请输入手势密码");
			lpwv.setVisibility(View.VISIBLE);
			noSetPassword.setVisibility(View.GONE);
			}
			else if(LoginType.equals("change"))
			{
				toastTv.setText("请先解锁手势密码");
				lpwv.setVisibility(View.VISIBLE);
				noSetPassword.setVisibility(View.GONE);
			}
		}
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
