package com.kgdset.photovault;
import java.io.IOException;

import java.util.HashMap;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import android.os.AsyncTask;
import android.os.Bundle;

import android.app.ProgressDialog;

import android.content.res.AssetManager;
import android.graphics.Bitmap;

import android.graphics.Color;

import android.graphics.drawable.ColorDrawable;

import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;

import android.view.View.OnClickListener;

import android.view.Window;

import android.widget.AbsListView;

import android.widget.GridView;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;


public class ThemeActivity extends BaseActivity implements OnScrollListener{

	private List<String> mList = null; 
    AssetManager assets = null;  
    
	private GridView gridView;  
	//private List<String> mList = null;
    private ThemeImageAdapter imgAdapter;  
    
public TextView titletext;
public TextView editbtn;
public ImageView titleback;
public ImageView image2;
public static ThemeActivity instance = null; 
List<Bitmap> bms;
//用来保存GridView中每个Item的图片，以便释放
public static Map<String,Bitmap> gridviewBitmapCaches = new HashMap<String,Bitmap>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //
		
		setContentView(R.layout.activity_theme);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制之
		
		instance=this;
		image2 = (ImageView)findViewById(R.id.scan_select); 
		
		
		ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.ReLayout),(ViewGroup)findViewById(R.id.title_bar),getAssets());
	
		//folderid = extras.getString("id");
	
		titletext=(TextView)findViewById(R.id.titletext);
		titletext.setText("请选择背景图片"); 
		titleback=(ImageView)findViewById(R.id.backbtn);
		editbtn=(TextView)findViewById(R.id.editbtn);
		//editbtn.setText("添加");
		editbtn.setVisibility(View.GONE);
		//SetAdve();
		 new AsyncTask<Integer, Integer, String[]>()  
         {  

             private ProgressDialog dialog;  
          
             protected void onPreExecute()  
             {  
                 dialog = ProgressDialog.show(ThemeActivity.this, "",  
                         "加载中,请稍候....");  
                 super.onPreExecute();  
             }  

             protected String[] doInBackground(Integer... params)  
             {  
                 if (!android.os.Environment.getExternalStorageState()  
                         .equals(android.os.Environment.MEDIA_MOUNTED))  
                 {  

                 }  
                 else  
                 {  
                	 SetData();
                 }  
                 return null;  
             }  

             protected void onPostExecute(String[] result)  
             {  
                 dialog.dismiss();  
                 SetView();
                 super.onPostExecute(result);  
             }  
         }.execute(0);  
		 
		 
		 
		
	       
	        //Toast.makeText(this, "加载图片中....", Toast.LENGTH_SHORT).show();  
	       
		
	
		
	}

	 //卸载图片的函数
  	private void recycleBitmapCaches(int fromPosition,int toPosition){		
  		Bitmap delBitmap = null;
  		for(int del=fromPosition;del<toPosition;del++){
  			delBitmap = gridviewBitmapCaches.get(mList.get(del));	
  			if(delBitmap != null){	
  				
  				gridviewBitmapCaches.remove(mList.get(del));		
  				delBitmap.recycle();	
  				delBitmap = null;
  			}
  		}				
  	}
  	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		//注释：firstVisibleItem为第一个可见的Item的position，从0开始，随着拖动会改变
		//visibleItemCount为当前页面总共可见的Item的项数
		//totalItemCount为当前总共已经出现的Item的项数
		recycleBitmapCaches(0,firstVisibleItem);
		recycleBitmapCaches(firstVisibleItem+visibleItemCount, totalItemCount);
		
	}
	@Override
    protected void onDestroy() { 
		 super.onDestroy(); 
		 Iterator keys = gridviewBitmapCaches.keySet().iterator();
			
			while(keys.hasNext()){
				
				Object key = keys.next();
				Bitmap delBitmap = null;
				delBitmap = gridviewBitmapCaches.get(key);	
	  			if(delBitmap != null){	
	  				//如果非空则表示有缓存的bitmap，需要清理	
	  				//Log.d(TAG, "release position:"+ del);		
	  				//从缓存中移除该del->bitmap的映射		
	  						
	  				delBitmap.recycle();	
	  			}
	  		//  Log.v("log", key.toString()); 
			}
			 gridviewBitmapCaches.clear();
			
		   
		
  //System.gc();	
		

    }
	public void SetData()
	{
		
		
	
		 try{  
			 
	         assets = getAssets();  
	         //获取/assests/目录下的所有的文件   
	       String[] mLists = assets.list("");  
	       mList = new ArrayList<String>();
	        for(int i=0;i<mLists.length;i++)
	        {
	        	if(mLists[i].endsWith(".png")||mLists[i].endsWith(".jpg"))
	        	{
	        		mList.add(mLists[i]);
	        	}
	        }
	        
	     	//bmp.recycle();
	     }catch(IOException e){  
	         e.printStackTrace();  
	     }  
    	
    	
   
       
                     
       
	}

private void SetView()
{
	titleback.setOnClickListener(new OnClickListener()
	 {
	 public void onClick(View v)
	 {
		 
	        	 finish();
 	         //设置切换动画，从右边进入，左边退出
 	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
 	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
 	       
	       
	 }
	 });
	
	
	//GalleryImageList= new ArrayList<LoadImage>(); 
	//bms=new ArrayList<Bitmap>();
	//List<String> photolist=SessionFileManager.getPictures(titlename);
	 gridView = (GridView)findViewById(R.id.picture_grid1);  
	 //frame1= (RelativeLayout)findViewById(R.id.frame1);
	 gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	 gridView.setOnScrollListener(this);
       //deleteButton.setOnClickListener(delClickListener);    
       imgAdapter = new ThemeImageAdapter(this,mList,assets); 
       
       gridView.setAdapter(imgAdapter);  
      
	}
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
       
        	if (keyCode == KeyEvent.KEYCODE_BACK) { 
        		 finish();
  	         //设置切换动画，从右边进入，左边退出
  	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
  	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
  	      
        	}
  	      return false;  
        	
    
    }  

    
       
   
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
