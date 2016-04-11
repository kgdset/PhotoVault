package com.kgdset.photovault;

import java.io.File;
import java.io.FileInputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

import java.util.List;

import com.kgdset.photovault.ImageAdapter.ViewHolder;
import com.kgdset.photovault.SdCardPhotoSelect.LoadImage;

import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.FeatureInfo;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.AbsListView.OnScrollListener;
import android.widget.RelativeLayout.LayoutParams;

public class PhotoGridViewActivity extends BaseActivity implements OnScrollListener{
	private GridView gridView;  
    private ImageAdapter imgAdapter;  
    private List<LoadImage> fileNameList = new ArrayList<LoadImage>();     //保存Adapter中显示的图片详情(要跟adapter里面的List要对应)  
    private List<LoadImage> selectFileLs=new ArrayList<LoadImage>();    //保存选中的图片信息  
   // public static List<LoadImage> GalleryImageList; 
    static boolean isDbClick = false;   //是否正在长按状态  
    private RelativeLayout frame1;
private String titlename;
private String folderid;
public TextView titletext;
public TextView editbtn;
public TextView copybtn;
public ImageView titleback;
public ImageView image2;
boolean IsExist;




//用来保存GridView中每个Item的图片，以便释放
public static Map<String,Bitmap> gridviewBitmapCaches = new HashMap<String,Bitmap>();
public static PhotoGridViewActivity instance = null; 



public void SetAdve()
{
	AdwoView.SetAdwo(this,(ViewGroup) findViewById(R.id.frame1),1);
	ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.frame1),(ViewGroup)findViewById(R.id.title_bar),getAssets());
}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //
		
		setContentView(R.layout.activity_photo_grid_view);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制之
		instance=this;
image2 = (ImageView)findViewById(R.id.scan_select); 
SetAdve();
		Bundle extras = getIntent().getExtras();   
		titlename = extras.getString("name"); 
		folderid = extras.getString("id");
		titletext=(TextView)findViewById(R.id.titletext);
		titletext.setText(titlename);
		titleback=(ImageView)findViewById(R.id.backbtn);
		editbtn=(TextView)findViewById(R.id.editbtn);
		copybtn=(TextView)findViewById(R.id.copybtn);

		editbtn.setVisibility(View.VISIBLE);
		new AsyncTask<Integer, Integer, String[]>()  
        {  

            private ProgressDialog dialog;  
         
            protected void onPreExecute()  
            {  
                dialog = ProgressDialog.show(PhotoGridViewActivity.this, "",  
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
	
		
	}  
	private void SetView()
	{
		
		
		titleback.setOnClickListener(new OnClickListener()
		 {
		 public void onClick(View v)
		 {
			 if(isDbClick){    //点击返回按键  
				 EixtEdit();
		           
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
		copybtn.setOnClickListener(new OnClickListener()
		 {
		 public void onClick(View v)
		 {
			  if(isDbClick)
			  {
				   if(selectFileLs.isEmpty()) {  
		                Toast.makeText(PhotoGridViewActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();  
		                return ;  
		            }
		            else
		            {
		            	AlertDialog dialog = new AlertDialog.Builder(PhotoGridViewActivity.this).setTitle("温馨提示").setMessage("图片将导出到SD卡/photovault目录中")  
		                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){  
		                            @Override  
		                            public void onClick(DialogInterface dialog, int which) { 
		                            	 new AsyncTask<Integer, Integer, String[]>()  
		                                 {  

		                                     private ProgressDialog dialog;  
		                                  
		                                     protected void onPreExecute()  
		                                     {  
		                                         dialog = ProgressDialog.show(PhotoGridViewActivity.this, "",  
		                                                 "正在导出,请稍候....");  
		                                         super.onPreExecute();  
		                                     }  

		                                     protected String[] doInBackground(Integer... params)  
		                                     {  
		                                         if (android.os.Environment.getExternalStorageState()  
		                                                 .equals(android.os.Environment.MEDIA_MOUNTED))  
		                                         {  
		                                        	 String pathstr=StaticParameter.SdcardPath+"/photovault/";
		                                        	 for(LoadImage loadimg : selectFileLs){  
		     		                                    FileCopy.copyfiletosd(loadimg.getBitpath(), pathstr);
		     		                                  
		     		                                    //Log.i("----------------------删除图片------", isTrue+"---------------");  
		     		                                }
		                                         }  
		                                         
		                                         return null;  
		                                     }  

		                                     protected void onPostExecute(String[] result)  
		                                     {  
		                                         dialog.dismiss();  
		                                        
		                                        // selectFileLs = new ArrayList<LoadImage>();
		 		                                isDbClick = false;  
		 		                               // gridView.setPadding(0, 0, 0, 0);            //退出编辑转台时候，使gridview全屏显示  
		 		                                titletext.setText(titlename);
		 		                               Toast.makeText(getApplicationContext(), "导出成功！", Toast.LENGTH_SHORT)
		 		     	                     .show();
		 		                                copybtn.setVisibility(7);
		 		                               editbtn.setBackgroundResource(R.drawable.title_add_bg);
		 		                                selectFileLs.clear();  
		 		                               imgAdapter.clear();
		 		                               imgAdapter.DataChanged();
		 		                                
		 		                               if(imgAdapter.getCount()==0)
		 		                               {
		 		                            	  frame1.setBackgroundResource(R.drawable.list_blank);
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
			  }
			
	  	     
		 }
		 });
		editbtn.setOnClickListener(new OnClickListener()
		 {
		 public void onClick(View v)
		 {
			  if(isDbClick)
			  {
				   if(selectFileLs.isEmpty()) {  
		                Toast.makeText(PhotoGridViewActivity.this, "请选择图片", Toast.LENGTH_SHORT).show();  
		                return ;  
		            }
		            else
		            {
		            	AlertDialog dialog = new AlertDialog.Builder(PhotoGridViewActivity.this).setTitle("温馨提示").setMessage("确定要删除选中的图片么？")  
		                        .setPositiveButton("确定", new DialogInterface.OnClickListener(){  
		                            @Override  
		                            public void onClick(DialogInterface dialog, int which) { 
		                            	 new AsyncTask<Integer, Integer, String[]>()  
		                                 {  

		                                     private ProgressDialog dialog;  
		                                  
		                                     protected void onPreExecute()  
		                                     {  
		                                         dialog = ProgressDialog.show(PhotoGridViewActivity.this, "",  
		                                                 "正在删除,请稍候....");  
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
		                                        	 for(LoadImage loadimg : selectFileLs){  
		     		                                    File file = new File(loadimg.getBitpath());  
		     		                                    boolean isTrue = file.delete();
		     		                                  
		     		                                    //Log.i("----------------------删除图片------", isTrue+"---------------");  
		     		                                }

		                                         }  
		                                         return null;  
		                                     }  

		                                     protected void onPostExecute(String[] result)  
		                                     {  
		                                         dialog.dismiss();  
		                                         imgAdapter.deletePhoto(selectFileLs);
		                                         deletePhoto(selectFileLs);
		                                        // selectFileLs = new ArrayList<LoadImage>();
		 		                                isDbClick = false;  
		 		                               // gridView.setPadding(0, 0, 0, 0);            //退出编辑转台时候，使gridview全屏显示  
		 		                                titletext.setText(titlename);
		 		                                
		 		                               editbtn.setBackgroundResource(R.drawable.title_add_bg);
		 		                                copybtn.setVisibility(7);
		 		                                selectFileLs.clear();  
		 		                                imgAdapter.clear();
		 		                                
		 		                               if(imgAdapter.getCount()==0)
		 		                               {
		 		                            	  frame1.setBackgroundResource(R.drawable.list_blank);
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
			  }
			  else
			  {
				  Intent intent=new Intent();
 				 intent.putExtra("dirname", titlename);
 				intent.putExtra("id", folderid);
	    			intent.setClass(PhotoGridViewActivity.this, SdCardPhotoListActivity.class);
	    			startActivity(intent);
	    			//MainActivity.this.finish();
	    			 //设置切换动画，从右边进入，左边退出
	    			//overridePendingTransition(R.anim.new_dync_in_from_right, R.anim.new_dync_out_to_left);
	    			overridePendingTransition(R.anim.in_from_down, R.anim.out_to_up);
			  }
	  	     
		 }
		 });
	
		//bms=new ArrayList<Bitmap>();
		//List<String> photolist=SessionFileManager.getPictures(titlename);
		 gridView = (GridView)findViewById(R.id.picture_grid);  
		 frame1= (RelativeLayout)findViewById(R.id.frame1);
if(!IsExist)
{
	frame1.setBackgroundResource(R.drawable.list_blank); 
	}
		 gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		 gridView.setOnScrollListener(this);
	        //deleteButton.setOnClickListener(delClickListener);    
	        imgAdapter = new ImageAdapter(this,fileNameList); 
	        
	        gridView.setAdapter(imgAdapter);  
	        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
	            @Override  
	            public void onItemClick(AdapterView<?> parent, View view,  
	                    int position, long id) {  
	                LoadImage loadimg = fileNameList.get(position);  
	                ViewHolder holder = (ViewHolder)view.getTag();  
	                if(isDbClick){  
	                	
	                    if(selectFileLs.contains(loadimg)){  
	                       // holder.image1.setImageDrawable(null);   
	                    	 holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_normal); 
	                        imgAdapter.delNumber(position+"");  
	                        selectFileLs.remove(loadimg);  
	                    }else{  
	                        //holder.image1.setImageResource(R.drawable.arrow);    //添加图片(带边框的透明图片)[主要目的就是让该图片带边框]  
	                       // holder.image2.setVisibility(View.VISIBLE);  //设置图片右上角的对号显示 
	                        holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed); 
	                        imgAdapter.addNumber(position+"");    //把该图片添加到adapter的选中状态，防止滚动后就没有在选中状态了。  
	                        selectFileLs.add(loadimg);  
	                    }  
if(selectFileLs.size()==0)
{EixtEdit();
	   }
else{
	                    titletext.setText("选中"+selectFileLs.size()+"张图片"); 
}
	                }else{  
	                	
	                	 Intent intent = new Intent(); 
	    				 intent.putExtra("position", position); 
	    				 intent.putExtra("dirname", titlename);
	    				 intent.putExtra("dirid", folderid);
	    				//intent.putExtra("imagelist", GalleryImageList);
	    				 intent.setClass(PhotoGridViewActivity.this,ImagePagerActivity.class);
	    		         startActivity(intent);
	    		        // PhotoGridViewActivity.this.finish();
	    		         overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
	        			//PhotoGridViewActivity.this.startActivity(intent);
	                }  
	            }  
	        });  
	        gridView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {  
	            @Override  
	            public boolean onItemLongClick(AdapterView<?> parent, View view,  
	                    int position, long id) {  
	                LoadImage loadimg = fileNameList.get(position);
	                
	                ViewHolder holder = (ViewHolder)view.getTag();  
	                if(!isDbClick){
	                	dataChanged();
	                	//selectFileLs = new ArrayList<LoadImage>();
	                	 Toast.makeText(getApplicationContext(), "进入编辑状态，可多选删除或导出图片！", Toast.LENGTH_SHORT)
	                     .show();
	                    isDbClick = true;  
	                   // gridView.setPadding(0, 50, 0, 50);            //长按后，让gridview上下都分出点空间，显示删除按钮之类的。看效果图就知道了。  
	                    
	                    editbtn.setBackgroundResource(R.drawable.btn_bg_selector);
	                    copybtn.setVisibility(view.VISIBLE);
	                    //holder.image1.setImageResource(R.drawable.arrow);  
	                   holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed); 
	                    imgAdapter.addNumber(position+"");  
	                    selectFileLs.add(loadimg);  
	                    titletext.setText("选中1张图片");  
	                    return true;  
	                }  
	                return false;  
	            }  
	        });  
	}
	private void dataChanged() {  
        // 通知listView刷新  
		imgAdapter.notifyDataSetChanged();  
        // TextView显示最新的选中数目  
       // tv_show.setText("已选中" + checkNum + "项");  
    };  
	public void EixtEdit()
	{
		Toast.makeText(getApplicationContext(), "编辑状态已退出！", Toast.LENGTH_SHORT)
	     .show();
		 isDbClick = false;  
	     //gridView.setPadding(0, 0, 0, 0);            //退出编辑转台时候，使gridview全屏显示  
	     titletext.setText(titlename); 
	     editbtn.setBackgroundResource(R.drawable.title_add_bg);
	     copybtn.setVisibility(7);
	     selectFileLs.clear();  
	     imgAdapter.clear();
	}
	 public void deletePhoto(List<LoadImage> loadimgLs){  
	        for(LoadImage img:loadimgLs){  
	            if(fileNameList.contains(img)){  
	            	fileNameList.remove(img);  
	            }  
	        }
	       
	    } 
	 public void SetData()
	 {
		 String name;
     	if(!titlename.equals("相机照片"))
     	{
     		name=folderid;
     	}
     	else
     	{
     		name=titlename;
     	}
         File fileDir = new File(DataOperate.folderspath+name); 
         
         File[] files = fileDir.listFiles();  
       
         if(files!=null&&files.length>0){  
             for(File file:files){  
                 String fileName = file.getName(); 
                
                 String suffix= fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
                 if (fileName.lastIndexOf(".") > 0    
                         &&(suffix.equals("kgdset")))
                 { 
                 	 
                	 IsExist=true;
                         LoadImage loadimage=new LoadImage();
                        		loadimage.setFileName(fileName);
                        		loadimage.setFilePath(file.getPath());
                        		loadimage.setImageName(fileName.substring(0,fileName.lastIndexOf("."))+".thumbnail");
                             fileNameList.add(loadimage);
                 	
                 }
                 else
                 {
                	 IsExist=false;
                 }
             }
         }
         else
         {
        	
        	 IsExist=false;
         }
                        
	 }
	 
	 //卸载图片的函数
	  	private void recycleBitmapCaches(int fromPosition,int toPosition){		
	  		Bitmap delBitmap = null;
	  		for(int del=fromPosition;del<toPosition;del++){
	  			delBitmap = gridviewBitmapCaches.get(fileNameList.get(del).getBitpath());	
	  			if(delBitmap != null){	
	  				//如果非空则表示有缓存的bitmap，需要清理	
	  				//Log.d(TAG, "release position:"+ del);		
	  				//从缓存中移除该del->bitmap的映射		
	  				gridviewBitmapCaches.remove(fileNameList.get(del).getBitpath());		
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
	  		 // Log.v("log", key.toString()); 
			}
		 imgAdapter.clear();  
	     fileNameList.clear();     //保存Adapter中显示的图片详情(要跟adapter里面的List要对应)  
	   selectFileLs.clear();    //保存选中的图片信息  
	   gridviewBitmapCaches.clear();
	  imgAdapter.ClearAll();
	//System.gc();
   
     }
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if(isDbClick && keyCode == KeyEvent.KEYCODE_BACK){    //点击返回按键  
        	EixtEdit(); 
            return false;  
        }  
        else
        {
        	if (keyCode == KeyEvent.KEYCODE_BACK) { 
        		 finish();
        		
  	         //设置切换动画，从右边进入，左边退出
  	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
  	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
  	      
        	}
  	      return false;  
        	
        }
       
    }  
	
  
       
         
     @Override  
    protected Dialog onCreateDialog(int id) {  
         AlertDialog dialog = new AlertDialog.Builder(PhotoGridViewActivity.this).setTitle("温馨提示").setMessage("暂时还没有照片,请先采集照片！")  
            .setPositiveButton("确定", new DialogInterface.OnClickListener(){  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                
                }  
            }).setNegativeButton("取消", new DialogInterface.OnClickListener() {  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    finish();  
                }  
            }).show();  
         return dialog;  
    }  
       
     
     /** 
      * 图片详细信息bean 
      * @author： aokunsang 
      * @date： 2012-8-31 
      */  
    public class LoadImage {  
           
         private String fileName;  
       
         private String filepath;  
         private String imageThumName;
        public LoadImage() {  
            super();  
            // TODO Auto-generated constructor stub  
        }  
        public LoadImage(String fileName, String path,String imagename) {  
            super();  
            this.fileName = fileName;  
            this.filepath=path;
            this.imageThumName=imagename;
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
        public void setImageName(String name) {  
            this.imageThumName = name;  
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
        public String getImageName() {  
            return imageThumName;  
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
	public void onScrollStateChanged(AbsListView arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}
}
