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
    private List<LoadImage> fileNameList = new ArrayList<LoadImage>();     //����Adapter����ʾ��ͼƬ����(Ҫ��adapter�����ListҪ��Ӧ)  
    private List<LoadImage> selectFileLs=new ArrayList<LoadImage>();    //����ѡ�е�ͼƬ��Ϣ  
   // public static List<LoadImage> GalleryImageList; 
    static boolean isDbClick = false;   //�Ƿ����ڳ���״̬  
    private RelativeLayout frame1;
private String titlename;
private String folderid;
public TextView titletext;
public TextView editbtn;
public TextView copybtn;
public ImageView titleback;
public ImageView image2;
boolean IsExist;




//��������GridView��ÿ��Item��ͼƬ���Ա��ͷ�
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
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//��title layout�У����Զ���֮
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
                        "������,���Ժ�....");  
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
			 if(isDbClick){    //������ذ���  
				 EixtEdit();
		           
		        }  
		        else
		        {
		        	 finish();
		        	
	  	         //�����л����������ұ߽��룬����˳�
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
		                Toast.makeText(PhotoGridViewActivity.this, "��ѡ��ͼƬ", Toast.LENGTH_SHORT).show();  
		                return ;  
		            }
		            else
		            {
		            	AlertDialog dialog = new AlertDialog.Builder(PhotoGridViewActivity.this).setTitle("��ܰ��ʾ").setMessage("ͼƬ��������SD��/photovaultĿ¼��")  
		                        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){  
		                            @Override  
		                            public void onClick(DialogInterface dialog, int which) { 
		                            	 new AsyncTask<Integer, Integer, String[]>()  
		                                 {  

		                                     private ProgressDialog dialog;  
		                                  
		                                     protected void onPreExecute()  
		                                     {  
		                                         dialog = ProgressDialog.show(PhotoGridViewActivity.this, "",  
		                                                 "���ڵ���,���Ժ�....");  
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
		     		                                  
		     		                                    //Log.i("----------------------ɾ��ͼƬ------", isTrue+"---------------");  
		     		                                }
		                                         }  
		                                         
		                                         return null;  
		                                     }  

		                                     protected void onPostExecute(String[] result)  
		                                     {  
		                                         dialog.dismiss();  
		                                        
		                                        // selectFileLs = new ArrayList<LoadImage>();
		 		                                isDbClick = false;  
		 		                               // gridView.setPadding(0, 0, 0, 0);            //�˳��༭ת̨ʱ��ʹgridviewȫ����ʾ  
		 		                                titletext.setText(titlename);
		 		                               Toast.makeText(getApplicationContext(), "�����ɹ���", Toast.LENGTH_SHORT)
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
		                        }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
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
		                Toast.makeText(PhotoGridViewActivity.this, "��ѡ��ͼƬ", Toast.LENGTH_SHORT).show();  
		                return ;  
		            }
		            else
		            {
		            	AlertDialog dialog = new AlertDialog.Builder(PhotoGridViewActivity.this).setTitle("��ܰ��ʾ").setMessage("ȷ��Ҫɾ��ѡ�е�ͼƬô��")  
		                        .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){  
		                            @Override  
		                            public void onClick(DialogInterface dialog, int which) { 
		                            	 new AsyncTask<Integer, Integer, String[]>()  
		                                 {  

		                                     private ProgressDialog dialog;  
		                                  
		                                     protected void onPreExecute()  
		                                     {  
		                                         dialog = ProgressDialog.show(PhotoGridViewActivity.this, "",  
		                                                 "����ɾ��,���Ժ�....");  
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
		     		                                  
		     		                                    //Log.i("----------------------ɾ��ͼƬ------", isTrue+"---------------");  
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
		 		                               // gridView.setPadding(0, 0, 0, 0);            //�˳��༭ת̨ʱ��ʹgridviewȫ����ʾ  
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
		                        }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
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
	    			 //�����л����������ұ߽��룬����˳�
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
	                        //holder.image1.setImageResource(R.drawable.arrow);    //���ͼƬ(���߿��͸��ͼƬ)[��ҪĿ�ľ����ø�ͼƬ���߿�]  
	                       // holder.image2.setVisibility(View.VISIBLE);  //����ͼƬ���ϽǵĶԺ���ʾ 
	                        holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed); 
	                        imgAdapter.addNumber(position+"");    //�Ѹ�ͼƬ��ӵ�adapter��ѡ��״̬����ֹ�������û����ѡ��״̬�ˡ�  
	                        selectFileLs.add(loadimg);  
	                    }  
if(selectFileLs.size()==0)
{EixtEdit();
	   }
else{
	                    titletext.setText("ѡ��"+selectFileLs.size()+"��ͼƬ"); 
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
	                	 Toast.makeText(getApplicationContext(), "����༭״̬���ɶ�ѡɾ���򵼳�ͼƬ��", Toast.LENGTH_SHORT)
	                     .show();
	                    isDbClick = true;  
	                   // gridView.setPadding(0, 50, 0, 50);            //��������gridview���¶��ֳ���ռ䣬��ʾɾ����ť֮��ġ���Ч��ͼ��֪���ˡ�  
	                    
	                    editbtn.setBackgroundResource(R.drawable.btn_bg_selector);
	                    copybtn.setVisibility(view.VISIBLE);
	                    //holder.image1.setImageResource(R.drawable.arrow);  
	                   holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed); 
	                    imgAdapter.addNumber(position+"");  
	                    selectFileLs.add(loadimg);  
	                    titletext.setText("ѡ��1��ͼƬ");  
	                    return true;  
	                }  
	                return false;  
	            }  
	        });  
	}
	private void dataChanged() {  
        // ֪ͨlistViewˢ��  
		imgAdapter.notifyDataSetChanged();  
        // TextView��ʾ���µ�ѡ����Ŀ  
       // tv_show.setText("��ѡ��" + checkNum + "��");  
    };  
	public void EixtEdit()
	{
		Toast.makeText(getApplicationContext(), "�༭״̬���˳���", Toast.LENGTH_SHORT)
	     .show();
		 isDbClick = false;  
	     //gridView.setPadding(0, 0, 0, 0);            //�˳��༭ת̨ʱ��ʹgridviewȫ����ʾ  
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
     	if(!titlename.equals("�����Ƭ"))
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
	 
	 //ж��ͼƬ�ĺ���
	  	private void recycleBitmapCaches(int fromPosition,int toPosition){		
	  		Bitmap delBitmap = null;
	  		for(int del=fromPosition;del<toPosition;del++){
	  			delBitmap = gridviewBitmapCaches.get(fileNameList.get(del).getBitpath());	
	  			if(delBitmap != null){	
	  				//����ǿ����ʾ�л����bitmap����Ҫ����	
	  				//Log.d(TAG, "release position:"+ del);		
	  				//�ӻ������Ƴ���del->bitmap��ӳ��		
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
	  		 // Log.v("log", key.toString()); 
			}
		 imgAdapter.clear();  
	     fileNameList.clear();     //����Adapter����ʾ��ͼƬ����(Ҫ��adapter�����ListҪ��Ӧ)  
	   selectFileLs.clear();    //����ѡ�е�ͼƬ��Ϣ  
	   gridviewBitmapCaches.clear();
	  imgAdapter.ClearAll();
	//System.gc();
   
     }
    @Override  
    public boolean onKeyDown(int keyCode, KeyEvent event) {
    	
        if(isDbClick && keyCode == KeyEvent.KEYCODE_BACK){    //������ذ���  
        	EixtEdit(); 
            return false;  
        }  
        else
        {
        	if (keyCode == KeyEvent.KEYCODE_BACK) { 
        		 finish();
        		
  	         //�����л����������ұ߽��룬����˳�
  	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
  	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
  	      
        	}
  	      return false;  
        	
        }
       
    }  
	
  
       
         
     @Override  
    protected Dialog onCreateDialog(int id) {  
         AlertDialog dialog = new AlertDialog.Builder(PhotoGridViewActivity.this).setTitle("��ܰ��ʾ").setMessage("��ʱ��û����Ƭ,���Ȳɼ���Ƭ��")  
            .setPositiveButton("ȷ��", new DialogInterface.OnClickListener(){  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                
                }  
            }).setNegativeButton("ȡ��", new DialogInterface.OnClickListener() {  
                @Override  
                public void onClick(DialogInterface dialog, int which) {  
                    finish();  
                }  
            }).show();  
         return dialog;  
    }  
       
     
     /** 
      * ͼƬ��ϸ��Ϣbean 
      * @author�� aokunsang 
      * @date�� 2012-8-31 
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
