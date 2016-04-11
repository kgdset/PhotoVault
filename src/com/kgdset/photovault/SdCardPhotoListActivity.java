package com.kgdset.photovault;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import net.youmi.android.banner.AdSize;
import net.youmi.android.banner.AdView;
import net.youmi.android.banner.AdViewListener;


import android.os.AsyncTask;
import android.os.Bundle;

import android.provider.MediaStore;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.KeyEvent;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.FrameLayout;
import android.widget.GridView;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.AbsListView.OnScrollListener;

public class SdCardPhotoListActivity extends BaseActivity implements OnScrollListener{
	   private SdCardPhotoImageAdapter imgAdapter;
	   public TextView titletext;
	   public TextView editbtn;
	   public ImageView titleback;
	   public String dirname;
	   public String folderid;
	   List<GridPhotoEntity> photolist;
	   public static SdCardPhotoListActivity instance = null; 
	 //��������GridView��ÿ��Item��ͼƬ���Ա��ͷ�
	   public static Map<String,Bitmap> gridviewBitmapCaches = new HashMap<String,Bitmap>();
	   private GridView gridView;
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		 requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //
		setContentView(R.layout.activity_sd_card_photo_list);
		instance=this;
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//��title layout�У����Զ���֮
		//bms=new ArrayList<Bitmap>();
		//List<String> photolist=SessionFileManager.getPictures(titlename);
		Bundle extras = getIntent().getExtras(); 
	
		dirname = extras.getString("dirname"); 
		folderid = extras.getString("id");
		 gridView = (GridView)findViewById(R.id.picture_grid);  
		 titletext=(TextView)findViewById(R.id.titletext);
			titletext.setText("ͼƬĿ¼");
			titleback=(ImageView)findViewById(R.id.backbtn);
			
			
			titleback.setOnClickListener(new OnClickListener()
			 {
			 public void onClick(View v)
			 {
				 
			        	 finish();
		  	         //�����л����������ұ߽��룬����˳�
			        	 overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
		  	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
		  	       
			      
			 }
			 });
			 gridView.setOnScrollListener(this);
		 gridView.setSelector(new ColorDrawable(Color.TRANSPARENT));
		 //Thread mThread = new Thread(setview());  
         //mThread.start();//�߳�����  
		 SetAdve();
		 new AsyncTask<Integer, Integer, String[]>()  
         {  

             private ProgressDialog dialog;  
          
             protected void onPreExecute()  
             {  
                 dialog = ProgressDialog.show(SdCardPhotoListActivity.this, "",  
                         "����ɨ��SD��,���Ժ�....");  
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
                	 setdata();
                 }  
                 return null;  
             }  

             protected void onPostExecute(String[] result)  
             {  
                 dialog.dismiss();  
                 setview();
                 super.onPostExecute(result);  
             }  
         }.execute(0);  
         //imgAdapter = new SdCardPhotoImageAdapter(this); 
         
         //gridView.setAdapter(imgAdapter);
     }  
	public void SetAdve()
	{
		
		AdwoView.SetAdwo(this,(ViewGroup) findViewById(R.id.frame1),2);
		
	
		ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.frame1),(ViewGroup)findViewById(R.id.title_bar),getAssets());
	}
	 public boolean onKeyDown(int keyCode, KeyEvent event) {  
	       if (keyCode == KeyEvent.KEYCODE_BACK) {  
	    	   finish();
	        //�����л����������ұ߽��룬����˳�
	        //overridePendingTransition(R.anim.push_left_in, R.anim.push_right_out);
	        overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
	       
	       }
			return false; 
	   }
	public void setdata()
	{
		getFileDir();
    //deleteButton.setOnClickListener(delClickListener);    
  
	
		}
	public void setview()
	{
		 imgAdapter = new SdCardPhotoImageAdapter(this,photolist); 
        
        gridView.setAdapter(imgAdapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {  
            @Override  
            public void onItemClick(AdapterView<?> parent, View view,  
                    int position, long id) {  
                  
            	TextView nametxt = (TextView)view.findViewById(R.id.nametxt);
            	TextView dirpathtxt = (TextView)view.findViewById(R.id.dirpath);
                	 Intent intent = new Intent(); 
    				 intent.putExtra("name", nametxt.getText()); 
    				 intent.putExtra("path", dirpathtxt.getText()); 
    				 intent.putExtra("dirname", dirname); 
    				 intent.putExtra("id", folderid);
    				//intent.putExtra("imagelist", GalleryImageList);
    				 intent.setClass(SdCardPhotoListActivity.this,SdCardPhotoSelect.class);
    		         startActivity(intent);
    		        // PhotoGridViewActivity.this.finish();
    		         overridePendingTransition(R.anim.in_from_right, R.anim.out_to_left);
        			//PhotoGridViewActivity.this.startActivity(intent);
                  
            }  
        }); 
	}
	 //ж��ͼƬ�ĺ���
  	private void recycleBitmapCaches(int fromPosition,int toPosition){		
  		Bitmap delBitmap = null;
  		for(int del=fromPosition;del<toPosition;del++){
  			delBitmap = gridviewBitmapCaches.get(photolist.get(del).getImageThumbnailPath());	
  			if(delBitmap != null){	
  				//����ǿ����ʾ�л����bitmap����Ҫ����	
  				//Log.d(TAG, "release position:"+ del);		
  				//�ӻ������Ƴ���del->bitmap��ӳ��		
  				gridviewBitmapCaches.remove(photolist.get(del).getImageThumbnailPath());		
  				delBitmap.recycle();	
  				delBitmap = null;
  				
  			}
  		}				
  	}
  	
  	
  	
  	
  	@Override
	public void onScroll(AbsListView view, int firstVisibleItem,
			int visibleItemCount, int totalItemCount) {
		// TODO Auto-generated method stub
		//ע�ͣ�firstVisibleItemΪ��һ���ɼ���Item��position����0��ʼ�������϶���ı�
		//visibleItemCountΪ��ǰҳ���ܹ��ɼ���Item������
		//totalItemCountΪ��ǰ�ܹ��Ѿ����ֵ�Item������
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
	  				//����ǿ����ʾ�л����bitmap����Ҫ����	
	  				//Log.d(TAG, "release position:"+ del);		
	  				//�ӻ������Ƴ���del->bitmap��ӳ��		
	  						
	  				delBitmap.recycle();	
	  			}
	  		  //Log.v("log", key.toString()); 
			}
			 gridviewBitmapCaches.clear();
			 imgAdapter.clear();  
			    photolist.clear();
//System.gc();	
   
    
   

    }
	protected  Map<String, String> GetImage() {
		Map<String,String> uriArray = new HashMap<String, String>();//��ͼƬ·������������
		try {
			
	
			String uri = null;
			//��Ҫ�ķ���ֵ���ڵ���
			String[] projection = { MediaStore.Images.Media.DATA};
			//ͼƬ��Ϣ�洢�� android.provider.MediaStore.Images.Thumbnails���ݿ� 
			//���ٲ�ѯ���ݿ��е�ͼƬ��Ӧ���·��         
			 ContentResolver testcr = this.getContentResolver();  
			Cursor cursor = testcr.query(
					MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
					projection, //List of columns to return ����Ҫ�����ص���
					null, // Return all rows
					null,
					null);
			int columnIndex = cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
			
			int i=0;
			while(cursor.moveToNext()&&i<cursor.getCount())
			{   //�Ƶ�ָ����λ�ã��������ݿ�
				cursor.moveToPosition(i);
				uri = cursor.getString(columnIndex);
				String imagedir=uri.substring(0, uri.lastIndexOf("/"));
				if(!uriArray.containsKey(uri))
				{
				
					uriArray.put(imagedir, uri);
				}
				i++;
			}
			cursor.close();//�ر����ݿ�
			return uriArray;
		} catch (Exception e) {
			return uriArray;// TODO: handle exception
		}
		
		
	}
	
	
	
	public  void getFileDir() {
		 Map<String, String> GetImage=GetImage();
		 photolist=new ArrayList<GridPhotoEntity>(); 
    	
	    	String Dirpath;
	    	String ImagePath;
	    	boolean IsOnMedia=false;
	        try{  
	        	 Iterator keys = GetImage.keySet().iterator();
	 			
	 			while(keys.hasNext()){
	 				int j = 0;
	 				Object key = keys.next();
	 				Dirpath=(String) key;
	 				ImagePath=GetImage.get(key);
	 				File f = new File(Dirpath);  
		            File[] files = f.listFiles();
		            if(files != null){  
		                int count = files.length;
		                for (int i = 0; i < count; i++) {  
		                    File file = files[i]; 
		                    if(file!=null&&file.length()>0)
		                    {
		                    String filepath  = file.getAbsolutePath();
		                    
		                    String path=file.getPath();
		                    //&&!suffix.equals("nomedia")
		                    //���������ͼ��picshow�ļ��У���ϵͳ�Զ����ɵ�dcim�ļ�����
		                 	String fileName = file.getName(); 
		                    String suffix= fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
		                    suffix=suffix.toLowerCase();
		                    if(suffix.equals("jpg")||suffix.equals("bmp")||suffix.equals("png")||suffix.equals("jpeg")||suffix.equals("gif"))
		                    {
		                    	j++;
		                    }
		                    }
		                }
		              }
		            String Dirname=Dirpath.substring(Dirpath.lastIndexOf("/")+1,Dirpath.length());
		            if(Dirname.length()>10)
		            {
		            	Dirname=Dirname.substring(0, 8)+"...";
		            }
		           // ContentResolver cr = this.getContentResolver();
		            GridPhotoEntity entity=new GridPhotoEntity();
		            entity.setImageThumbnailPath(ImagePath);
		            String ThumName= ImagePath.substring(ImagePath.lastIndexOf("/")+1,ImagePath.length());
		 			ThumName= ThumName.substring(0,ThumName.lastIndexOf("."));
		 			 ThumName=ThumName+".thumbnail";
		 			 entity.setImageThumName(ThumName);
		            entity.setcount(j);
		            entity.setdirname(Dirname);
		            entity.setdirpath(Dirpath);
		            photolist.add(entity);
		            entity=null;
		            }
	 			
	          }catch(Exception ex){  
	            ex.printStackTrace();  
	        }
	    }
	
	
	
	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		// TODO Auto-generated method stub
		
	} 
}
