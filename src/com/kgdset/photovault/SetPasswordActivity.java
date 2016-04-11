package com.kgdset.photovault;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.kgdset.photovault.R;
import com.kgdset.photovault.LocusPassWordView.OnCompleteListener;
import com.way.util.StringUtil;

public class SetPasswordActivity extends BaseActivity {
	private LocusPassWordView lpwv;
	private String password;
	private boolean needverify = true;
	private Toast toast;
	private TextView titletext;
	private TextView PwdTxt;
	private ImageView titleback;
	private Button tvreset;
	private Button tvsave;
	private String SetType;
	private void showToast(CharSequence message) {
		if (null == toast) {
			toast = Toast.makeText(this, message, Toast.LENGTH_SHORT);
//			toast.setGravity(Gravity.CENTER, 0, 0);
		} else {
			toast.setText(message);
		}

		toast.show();
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE);
		setContentView(R.layout.setpassword_activity);
		 getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//��title layout�У����Զ���֮
		 titleback=(ImageView)findViewById(R.id.backbtn);
			ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.ReLayout),(ViewGroup)findViewById(R.id.title_bar),getAssets());
		 titletext=(TextView)findViewById(R.id.titletext);
		 tvreset=(Button)findViewById(R.id.tvReset);
		 tvsave=(Button)findViewById(R.id.tvSave);
		 PwdTxt=(TextView)findViewById(R.id.Pwdtxt);
			Bundle extras = getIntent().getExtras(); 
			//SetType=extras.getString("type");
		 titletext.setText("��������");
			titleback.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)
			 {
				 SetPasswordActivity.this.finish();
			        //�����л����������ұ߽��룬����˳�
			        //overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
			        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
			        
			 }
			 });
			if(needverify)
			{
				PwdTxt.setText("�����������");
				titletext.setText("�����������");
				tvreset.setVisibility(View.GONE);
				tvsave.setVisibility(View.GONE);
			}
			
		lpwv = (LocusPassWordView) this.findViewById(R.id.mLocusPassWordView);
		lpwv.setOnCompleteListener(new OnCompleteListener() {
			@Override
			public void onComplete(String mPassword) {
				password = mPassword;
				if (needverify) {
					
					if (lpwv.verifyPassword(mPassword)) {
						showToast("����������ȷ,������������!");
						password="";
						lpwv.clearPassword();
						PwdTxt.setText("������������");
						titletext.setText("������������");
						tvreset.setVisibility(View.VISIBLE);
						tvsave.setVisibility(View.VISIBLE);
						needverify = false;
					} else {
						showToast("���������,����������!");
						lpwv.clearPassword();
						password = "";
					}
				}
			}
		});

		OnClickListener mOnClickListener = new OnClickListener() {
			@Override
			public void onClick(View v) {
				switch (v.getId()) {
				case R.id.tvSave:
					if (StringUtil.isNotEmpty(password)) {
						lpwv.resetPassWord(password);
						lpwv.clearPassword();
						  Version2 entity=new Version2();
						  // entity.setPatternpwd("0");
			           	entity.setPwdType("1");
			           	DataOperate.UpdateVersion2(entity);
			           	UserEntity user=new UserEntity();
						   user.setinitial("0");
						   user.setPwd1("0");
						   DataOperate.updateuser(user);
			           	init.GetVersion2();
						showToast("�������óɹ�,���ס����.");
						finish();
						 overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
						
					} else {
						lpwv.clearPassword();
						showToast("���벻��Ϊ��,����������.");
					}
					break;
				case R.id.tvReset:
					lpwv.clearPassword();
					break;
				}
			}
		};
		Button buttonSave = (Button) this.findViewById(R.id.tvSave);
		buttonSave.setOnClickListener(mOnClickListener);
		Button tvReset = (Button) this.findViewById(R.id.tvReset);
		tvReset.setOnClickListener(mOnClickListener);
		// �������Ϊ��,ֱ����������
		if (lpwv.isPasswordEmpty()) {
			this.needverify = false;
			//showToast("����������");
			PwdTxt.setText("��������������");
			titletext.setText("��������������");
			tvreset.setVisibility(View.VISIBLE);
			tvsave.setVisibility(View.VISIBLE);
		}
		
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
	}

	@Override
	protected void onStop() {
		super.onStop();
	}

}
