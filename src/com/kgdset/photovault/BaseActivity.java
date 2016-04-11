package com.kgdset.photovault;

import java.util.List;

import com.baidu.mobstat.StatService;

import android.app.Activity;
import android.app.ActivityManager;
import android.app.ActivityManager.RunningAppProcessInfo;
import android.content.Context;
import android.os.Bundle;
 
/**
 * ���ƣ�BaseActivity 
 * ������ 
 * �����ˣ� 
 * ���ڣ�2012-6-20 ����5:53:35 
 * �����
 */
 
public class BaseActivity extends Activity {
	 public void onCreate(Bundle savedInstanceState) { 
	        super.onCreate(savedInstanceState); 
	       // ApplicationEx application = (ApplicationEx) this.getApplication(); 
	        ApplicationEx.getInstance().addActivity(this);
	        //application.getActivityManager().pushActivity(this); 
	        
	        init.getparameter(this);
	    } 
	
	
        @Override
        protected void onStop() {
                // TODO Auto-generated method stub
                super.onStop();
 
                if (!isAppOnForeground()) {
                        //app �����̨
                	
                	//super.onDestroy();
                	if(StaticParameter.IsSwitch)
                	{
                	ApplicationEx.getInstance().exit();
                	}
                        //ȫ�ֱ���isActive = false ��¼��ǰ�Ѿ������̨
                }
        }
       
        @Override
        protected void onResume() {
                // TODO Auto-generated method stub
               super.onResume();
               StatService.onResume(this);
              
               // if (!isActive) {
                        //app �Ӻ�̨���ѣ�����ǰ̨
                	
                        //isActive = true;
               // }
        }
        @Override
 protected void onPause()
        {
        	super.onPause();
        	StatService.onPause(this);
        }
        /**
         * �����Ƿ���ǰ̨����
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

		
}