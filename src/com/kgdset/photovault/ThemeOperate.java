package com.kgdset.photovault;

import java.io.IOException;
import java.io.InputStream;

import android.content.Context;
import android.content.res.AssetManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Shader.TileMode;
import android.graphics.drawable.BitmapDrawable;
import android.view.ViewGroup;

public class ThemeOperate {
	static String[] images = null;  
	AssetManager assets = null;  
	static public BitmapDrawable drawable=null;
	static public BitmapDrawable drawable1=null;
	static public Bitmap bmp=null;
	static public  void setbackground(Context context,ViewGroup view1,ViewGroup view2,AssetManager assets)
	{
		 try{  
	         
	         //获取/assests/目录下的所有的文件   
	         images = assets.list("");  
	         InputStream assetFile = null; 
	         assetFile = assets.open(images[Integer.parseInt(StaticParameter.bgstr)]);
	         if(bmp==null)
	         {
	          bmp=BitmapFactory.decodeStream(assetFile);
	         }
	         if(drawable==null)
	         {
	        drawable = new BitmapDrawable(bmp); 
	       drawable1 = new BitmapDrawable(bmp);
	      
	         drawable1.setTileModeXY(TileMode.MIRROR , TileMode.REPEAT );
	         drawable1.setDither(true);
	         }
	         view1.setBackgroundDrawable(drawable);
	         view2.setBackgroundDrawable(drawable1);
	         view2.getBackground().setAlpha(220);
	     	//bmp.recycle();
	     }catch(IOException e){  
	         e.printStackTrace();  
	     }  
		
		//findViewById(R.id.item_layout).setBackgroundResource(R.drawable.bg_2);
	}
	static public void cleardrawable()
	{
		 drawable=null;
		 drawable1=null;
		 bmp=null;
	}
}
