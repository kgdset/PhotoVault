package com.kgdset.photovault;

import java.io.File;


import android.R.integer;
import android.app.Activity;
import android.content.Context;
import android.view.WindowManager;
import android.widget.TextView;

public class init extends Activity{
	public static WindowManager wm;
public static int  Pwidth;
public static int  Pheight;
public static int GridViewImageW;
public static int GridViewImageH;
public static void getparameter(Context context) {
	if(StaticParameter.SdcardPath.length()<2)
	{
	 wm = (WindowManager) context.getSystemService(Context.WINDOW_SERVICE);
	 Pwidth = wm.getDefaultDisplay().getWidth();//ÆÁÄ»¿í¶È
	Pheight = wm.getDefaultDisplay().getHeight();//ÆÁÄ»¸ß¶È
	GridViewImageW=(int) (Pwidth*0.325);
	GridViewImageH=(int) (Pwidth*0.3);
	DataOperate.init(context);
	StaticParameter.SdcardPath=DataOperate.getSDPath();
	GetUserEntity();
	 CreateCacheDir();
	 GetVersion2();
	 GetVersion3();
	}
	// DataOperate.init(context);
}
public static void GetUserEntity()
{
	UserEntity entity=DataOperate.loadUserEntity();
	if(entity.getbackstageswitch().equals("0"))
	{
		StaticParameter.IsSwitch=true;
	}
	else
	{
		StaticParameter.IsSwitch=false;
	}
	if(entity.getIsHideLock().equals("0"))
	{
		StaticParameter.IsHideLock=true;
	}
	else
	{
		StaticParameter.IsHideLock=false;
	}
}

public static void GetVersion3()
{
	Version3 entity=DataOperate.loadVersion3();
	StaticParameter.bgstr=entity.getbgstr();
	StaticParameter.usebgstr=entity.getusebgstr().replace("|", "");
	}
public static void GetVersion2()
{
	Version2 entity=DataOperate.loadVersion2();
	
	StaticParameter.PatternOpen=entity.GetPatternOpen();
	
	if(entity.GetAdOpen().equals("0"))
	{
		StaticParameter.AdOpen=false;
	}
	else
	{
		StaticParameter.AdOpen=true;
	}
	if(entity.getPwdType().equals("0"))
	{
		StaticParameter.IsPwdType=false;
	}
	else
	{
		StaticParameter.IsPwdType=true;
	}
	StaticParameter.integral=entity.getintegral();
}

public static void CreateCacheDir()
{

	
	if(!StaticParameter.SdcardPath.equals(""))
	{
		StaticParameter.CacheDir=StaticParameter.SdcardPath+"/kgdset/cache/";
	 File destDir = new File(StaticParameter.CacheDir);
	  if (!destDir.exists()) {
	   destDir.mkdirs();
	  }
	}
	
}
}
