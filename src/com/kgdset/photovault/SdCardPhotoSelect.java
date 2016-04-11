package com.kgdset.photovault;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import com.kgdset.photovault.SdcardPhotoSelectImageAdapter.ViewHolder;


import android.os.AsyncTask;
import android.os.Bundle;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class SdCardPhotoSelect extends BaseActivity implements OnScrollListener{

	private String DirPath;
	private GridView gridView;  
	private List<String> mList = null;
    private SdcardPhotoSelectImageAdapter imgAdapter;  
    private List<LoadImage> fileNameList = new ArrayList<LoadImage>();     //保存Adapter中显示的图片详情(要跟adapter里面的List要对应)  
    private List<LoadImage> selectFileLs = new ArrayList<LoadImage>();      //保存选中的图片信息  
    //public static List<LoadImage> GalleryImageList; 
    private boolean isDbClick = false;   //是否正在长按状态  
    private RelativeLayout frame1;
private String titlename;
private String folderid;
public TextView titletext;
public TextView editbtn;
public ImageView titleback;
public ImageView image2;
public String dirname;
List<Bitmap> bms;
//用来保存GridView中每个Item的图片，以便释放
public static Map<String,Bitmap> gridviewBitmapCaches = new HashMap<String,Bitmap>();
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
	
		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //
		
		setContentView(R.layout.activity_sd_card_photo_select);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制之
		
		Bundle extras = getIntent().getExtras(); 
		image2 = (ImageView)findViewById(R.id.scan_select); 
		
		dirname=extras.getString("dirname");
		titlename = extras.getString("name"); 
		folderid = extras.getString("id");
		//folderid = extras.getString("id");
		DirPath = extras.getString("path");
		titletext=(TextView)findViewById(R.id.titletext);
		titletext.setText("请选择图片"); 
		titleback=(ImageView)findViewById(R.id.backbtn);
		editbtn=(TextView)findViewById(R.id.editbtn);
		//editbtn.setText("添加");
		editbtn.setVisibility(View.GONE);
		SetAdve();
		 new AsyncTask<Integer, Integer, String[]>()  
         {  

             private ProgressDialog dialog;  
          
             protected void onPreExecute()  
             {  
                 dialog = ProgressDialog.show(SdCardPhotoSelect.this, "",  
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
	public void SetAdve()
	{
		AdwoView.SetAdwo(this,(ViewGroup) findViewById(R.id.frame1),1);
		ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.frame1),(ViewGroup)findViewById(R.id.title_bar),getAssets());
	}
	 //卸载图片的函数
  	private void recycleBitmapCaches(int fromPosition,int toPosition){		
  		Bitmap delBitmap = null;
  		for(int del=fromPosition;del<toPosition;del++){
  			delBitmap = gridviewBitmapCaches.get(mList.get(del));	
  			if(delBitmap != null){	
  				//如果非空则表示有缓存的bitmap，需要清理	
  				//Log.d(TAG, "release position:"+ del);		
  				//从缓存中移除该del->bitmap的映射		
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
		 try {
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
				 imgAdapter.clear();  
			     fileNameList.clear();     //保存Adapter中显示的图片详情(要跟adapter里面的List要对应)  
			   selectFileLs.clear();    //保存选中的图片信息  
		} catch (Exception e) {
			// TODO: handle exception
		}
		
  //System.gc();	
		

    }
	public void SetData()
	{
		mList = new ArrayList<String>();
		String name;
    	
    	if(!dirname.equals("相机照片"))
    	{
    		name=folderid;
    	}
    	else
    	{
    		name=dirname;
    	}
    	
        File fileDir = new File(DirPath);  
        File[] files = fileDir.listFiles();  
        boolean result = false;  
        if(files!=null){  
            for(File file:files){  
            	  if(file!=null&&file.length()>0)
                  {
                String fileName = file.getName(); 
                String suffix= fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
                suffix=suffix.toLowerCase();
                if(suffix.equals("jpg")||suffix.equals("bmp")||suffix.equals("png")||suffix.equals("jpeg")||suffix.equals("gif"))
                {
                	try { 
                		mList.add(file.getPath());
                		//mList.add(DataOperate.getImageThumbnail(SdCardPhotoSelect.this, SdCardPhotoSelect.this.getContentResolver(), file.getPath()));
                		LoadImage loadimage=new LoadImage();
                		loadimage.setFileName(fileName);
                		loadimage.setFilePath(file.getPath());
                		//loadimage.setImageThumPath();
                		fileNameList.add(loadimage);
                		loadimage=null;
                       } catch (Exception e) {  
                           e.printStackTrace();  
                       }    
                }  
            } 
            }
        }  
       
	}
public void filecopy()
{
	String name;
	if(!dirname.equals("相机照片"))
	{
		name=folderid;
	}
	else
	{
		name=dirname;
	}
	for(LoadImage loadimg : selectFileLs){  
        //File file = new File(loadimg.getFileName());  
		String imagename=loadimg.getFileName();
		imagename=imagename.substring(0, imagename.lastIndexOf("."));
       FileCopy.copyFile(loadimg.getBitpath(), DataOperate.folderspath+name+"/"+imagename+".kgdset"); 
        //Log.i("----------------------删除图片------", str+"---------------");  
    }  
   
   
	}
private void SetView()
{
	titleback.setOnClickListener(new OnClickListener()
	 {
	 public void onClick(View v)
	 {
		 if(isDbClick){    //点击返回按键  
	            isDbClick = false;  
	            //gridView.setPadding(0, 0, 0, 0);            //退出编辑转台时候，使gridview全屏显示  
	            titletext.setText(titlename); 
	           // editbtn.setText("添加"); 
	            selectFileLs.clear();  
	            imgAdapter.clear();  
	           
	        }  
	        else
	        {
	        	 finish();
 	         //设置切换动画，从右边进入，左边退出
 	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
 	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
 	       
	        }
	 }
	 });
	
	editbtn.setOnClickListener(new OnClickListener()
	 {
	 public void onClick(View v)//添加操作
	 {
		  
			  
	            	AlertDialog dialog = new AlertDialog.Builder(SdCardPhotoSelect.this).setTitle("温馨提示").setMessage("确认要添加选择的图片么？")  
	                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){  
	                            @Override  
	                            public void onClick(DialogInterface dialog, int which) {  
	                            	 new AsyncTask<Integer, Integer, String[]>()  
	                                 {  

	                                     private ProgressDialog dialog;  
	                                  
	                                     protected void onPreExecute()  
	                                     {  
	                                         dialog = ProgressDialog.show(SdCardPhotoSelect.this, "",  
	                                                 "正在导入，请稍后....");  
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
	                                        	 filecopy();
	                                         }  
	                                         return null;  
	                                     }  

	                                     protected void onPostExecute(String[] result)  
	                                     {  
	                                         dialog.dismiss(); 
	                                         selectFileLs.clear();  
	                                         imgAdapter.clear(); 
	                                         Intent intent = new Intent(); 
	                        				 intent.putExtra("id", folderid); 
	                        				 intent.putExtra("name",dirname); 
	                        				 intent.setClass(SdCardPhotoSelect.this,PhotoGridViewActivity.class);
	                        		         startActivity(intent);		                        		        
	                        		         overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	                        		         finish();
	                        		         if(SdCardPhotoListActivity.instance!=null)
	                        		         {
	                        		         SdCardPhotoListActivity.instance.finish();
	                        		         }
	                        		         if(PhotoGridViewActivity.instance!=null)
	                        		         {
	                        		         PhotoGridViewActivity.instance.finish();
	                        		         }
	                                         super.onPostExecute(result);  
	                                     }  
	                                 }.execute(0);   
	                            }  
	                        }).setNegativeButton("取消", new DialogInterface.OnClickListener() {  
	                            @Override  
	                            public void onClick(DialogInterface dialog, int which) {  
	                                
	                            }  
	                        }).show();  
	 }
	 });
	
	//GalleryImageList= new ArrayList<LoadImage>(); 
	//bms=new ArrayList<Bitmap>();
	//List<String> photolist=SessionFileManager.getPictures(titlename);
	 gridView = (GridView)findViewById(R.id.picture_grid);  
	 frame1= (RelativeLayout)findViewById(R.id.frame1);
	 gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
	 gridView.setOnScrollListener(this);
       //deleteButton.setOnClickListener(delClickListener);    
       imgAdapter = new SdcardPhotoSelectImageAdapter(this,mList); 
       
       gridView.setAdapter(imgAdapter);  
       gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
           @Override  
           public void onItemClick(AdapterView<?> parent, View view,  
                   int position, long id) {  
               LoadImage loadimg = fileNameList.get(position);  
               ViewHolder holder = (ViewHolder)view.getTag();  
               
                   if(selectFileLs.contains(loadimg)){  
                       //holder.image1.setImageDrawable(null);   
                   	 holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_normal); 
                       imgAdapter.delNumber(position+"");  
                       selectFileLs.remove(loadimg);  
                     
                   }else{  
                       //holder.image1.setImageResource(R.drawable.arrow);    //添加图片(带边框的透明图片)[主要目的就是让该图片带边框]  
                   	 holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed);   //设置图片右上角的对号显示 
                       
                       imgAdapter.addNumber(position+"");    //把该图片添加到adapter的选中状态，防止滚动后就没有在选中状态了。  
                       selectFileLs.add(loadimg);  
                   }  
                   
                   if(selectFileLs.size()!=0)
	                 {
                   	titletext.setText("选中"+selectFileLs.size()+"张图片");  
	                	 editbtn.setVisibility(view.VISIBLE);
	                 }
	                 else
	                 {
	                	 titletext.setText("请选择图片");  
	                	 editbtn.setVisibility(view.GONE);
	                 }
           }  
       });  
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
        	
        
        //return super.onKeyDown(keyCode, event);  
    }  
	/* public boolean onKeyDown(int keyCode, KeyEvent event) {  
	        if (keyCode == KeyEvent.KEYCODE_BACK) {  
	         Intent intent =new Intent();
	         intent.setClass(PhotoGridViewActivity.this,ListViewActivity.class);
	         startActivity(intent);
	         //设置切换动画，从右边进入，左边退出
	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
	        PhotoGridViewActivity.this.finish();
	        }
			return false; 
	    }
	 */
    
       
     /** 
      * 图片详细信息bean 
      * @author： aokunsang 
      * @date： 2012-8-31 
      */  
    public class LoadImage {  
           
         private String fileName;  
       
         private String filepath;  
         private String imageThumbnailpath;
        public LoadImage() {  
            super();  
            // TODO Auto-generated constructor stub  
        }  
        public LoadImage(String fileName, String path,String imagethumPath) {  
            super();  
            this.fileName = fileName;  
            this.filepath=path;
            this.imageThumbnailpath=imagethumPath;
        }  
        /** 
         * @return the fileName 
         */  
        public String getFileName() {  
            return fileName;  
        }  
        /** 
         * @param fileName the fileName to set 
         */  
        public void setFileName(String fileName) {  
            this.fileName = fileName;  
        } 
        public void setImageThumPath(String thumpath) {  
            this.imageThumbnailpath = thumpath;  
        } 
        public void setFilePath(String Path) {  
            this.filepath = Path;  
        }
        /** 
         * @return the bitmap 
         */  
      
        public String getBitpath() {  
            return filepath;  
        } 
        public String getThumPath() {  
            return imageThumbnailpath;  
        } 
        /** 
         * @param bitmap the bitmap to set 
         */  
      
        @Override  
        public int hashCode() {  
            return this.getBitpath().hashCode();  
        }  
          
        @Override  
        public boolean equals(Object o) {  
            LoadImage loadImg = (LoadImage)o;  
            return this.getBitpath().equals(loadImg.getBitpath());  
        }  
    }
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	}
}
