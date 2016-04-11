package com.kgdset.photovault;

import java.util.LinkedList;
import java.util.List;
import android.app.Activity;
import android.app.Application;
public class ApplicationEx extends Application { 
	 private List<Activity> activityList = new LinkedList<Activity>(); 
	 private static ApplicationEx instance;
	 private ApplicationEx() 
	 { 
	 }  
	 //单例模式中获取唯�?��MyApplication实例
	 public static ApplicationEx getInstance() 
	 {  
	    if(null == instance) 
	    {  
	      instance = new ApplicationEx(); 
	    }  
	    return instance; 
	 } 
	    //添加Activity到容器中
	 public void addActivity(Activity activity){
	  activityList.add(activity);
	 }
	 //遍历Activity并finish
	 public void exit()
	 {
	  for(Activity activity:activityList){
	   activity.finish();
	  }
	  System.exit(0);
	 }
	 public void CloseActivity(Activity activity)
	 {
		if(activityList.contains(activity))
		{
			activity.finish();
		}
	 }
	 /*在其他的 activity中调用ApplicationEx.getInstance().addActivity(***activity)
	 使其加入列表。然后在要出的按钮等事件监听中调用ApplicationEx.getInstance().exit() 就行*/

} 