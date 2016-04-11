package com.kgdset.photovault;


import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.List;

import android.content.ContentResolver;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;



	public class SdCardPhotoImageAdapter extends BaseAdapter  {
		// List<AsyncLoadImageTask> mTaskList; 
		
		   int width=(int) (init.GridViewImageW);
		public  List<GridPhotoEntity> picList;    //图片集合  
	   // private List<String> picNumber = new ArrayList<String>();       //选中图片的位置集合  
	    private int ownposition;
	    private LayoutInflater inflater;  
	    private Context mcontext;
	      
	  //  private Map<ImageView,String> ImageViewList=new HashMap();
	    ContentResolver cr;
	    public SdCardPhotoImageAdapter(Context mContext,List<GridPhotoEntity> list){  
	        inflater = LayoutInflater.from(mContext);  
	        picList=list;
	        mcontext=mContext;
	     
	        cr=mcontext.getContentResolver();
	       // mTaskList = new ArrayList<AsyncLoadImageTask>(picList.size());
	    }  
	    public int getOwnposition() {
			return ownposition;
		}
	    public void setOwnposition(int ownposition) {
			this.ownposition = ownposition;
		}
	    @Override  
	    public int getCount() {  
	        return picList.size();  
	       
	    }  
	  public void clear()
	  {
		  picList.clear();
		  
	  }
	    @Override  
	    public Object getItem(int position) {  
	    	ownposition=position;
	        return picList.get(position); 
	        
	    }  
	  
	  
	    @Override  
	    public long getItemId(int position) {  
	    	ownposition=position;
	        return position;  
	    }  
	  
	  
		@Override  
	    public View getView(int position, View convertView, ViewGroup parent) {  
			  ViewHolder holder = null;  
	        if(holder==null){  
	            holder = new ViewHolder();  
	            convertView = inflater.inflate(R.layout.sdcardphotoitem, null);  
	            holder.image1 = (ImageView)convertView.findViewById(R.id.scan_img);  
	            holder.image2 = (ImageView)convertView.findViewById(R.id.scan_select); 
	            holder.counttxt = (TextView)convertView.findViewById(R.id.counttxt); 
	            holder.nametxt = (TextView)convertView.findViewById(R.id.nametxt);
	            holder.dirpathtxt = (TextView)convertView.findViewById(R.id.dirpath);
	            android.view.ViewGroup.LayoutParams para;
	            android.view.ViewGroup.LayoutParams para1;
                para = holder.image1.getLayoutParams();
                para1 = holder.image2.getLayoutParams();
    	        para.height = init.GridViewImageW;
    	        para.width = init.GridViewImageW;
    	        //
    	        holder.image1.setLayoutParams(para);
    	        para1.height = (int) (init.GridViewImageW*0.4);
    	        para1.width = (int) (init.GridViewImageW*0.4);
    	       // holder.image2.setLayoutParams(para1);
	            convertView.setTag(holder);  
	        }else{  
	            holder = (ViewHolder)convertView.getTag();  
	        }  
	        String url = picList.get(position).getImageThumbnailPath();
			//首先我们先通过cancelPotentialLoad方法去判断imageview是否有线程正在为它加载图片资源，
			//如果有现在正在加载，那么判断加载的这个图片资源（url）是否和现在的图片资源一样，不一样则取消之前的线程（之前的下载线程作废）。
			//见下面cancelPotentialLoad方法代码
			if (cancelPotentialLoad(url, holder.image1)) {

				  AsyncLoadImageTask task = new AsyncLoadImageTask(holder.image1);
			         LoadedDrawable loadedDrawable = new LoadedDrawable(task);
			         holder.image1.setImageDrawable(loadedDrawable);
			         task.execute(position);
		      
		     
		     }		 

	       
	       // holder.image1.setImageBitmap(picList.get(position).getBitmap());  
	        holder.counttxt.setText("("+Integer.toString(picList.get(position).getcount())+")");
	         holder.nametxt.setText(picList.get(position).getdirname());
	         holder.dirpathtxt.setText(picList.get(position).getdirpath());
	            //holder.image2.setVisibility(View.GONE);  
	       
	        return convertView;  
	    }  
	      
	    public static class ViewHolder{  
	        public ImageView image1;     //要显示的图片  
	        public ImageView image2;     //图片右上角的小对号图片(标示选中状态的玩意) 
	        public TextView nametxt;
	        public TextView counttxt;
	        public TextView dirpathtxt;
	    } 
	    
	   
	    
	    
	  
	    
	    
	    
	    
	    
	    
	    
	    
	    
	    private boolean cancelPotentialLoad(String url,ImageView imageview){
	 			AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);

	 		    if (loadImageTask != null) {
	 		        String bitmapUrl = loadImageTask.url;
	 		        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
	 		        	loadImageTask.cancel(true);	        	
	 		        } else {
	 		            // 相同的url已经在加载中.
	 		            return false;
	 		        }
	 		    }
	 		    return true;

	 		}
	 	    
	 	  //加载图片的异步任务	
	 		private class AsyncLoadImageTask extends AsyncTask<Integer, Void, Bitmap>{
	 			 
	 			private String url = null;
	 			private final WeakReference<ImageView> imageViewReference;
	 			
	 			public AsyncLoadImageTask(ImageView imageview) {
	 				super();
	 				// TODO Auto-generated constructor stub
	 				imageViewReference = new WeakReference<ImageView>(imageview);
	 			}

	 			@Override
	 			protected Bitmap doInBackground(Integer... params) {
	 				// TODO Auto-generated method stub
	 				Bitmap bitmap = null;
	 				this.url = picList.get(params[0]).getImageThumbnailPath();
	 				String name=picList.get(params[0]).getImageThumName();
	 				bitmap = getBitmapFromUrl(url,name);
	 				SdCardPhotoListActivity.gridviewBitmapCaches.put(this.url, bitmap);	
	 			
	 				return bitmap;
	 			}

	 			@Override
	 			protected void onPostExecute(Bitmap resultBitmap) {
	 				// TODO Auto-generated method stub
	 				if(isCancelled()){
	 					resultBitmap = null;
	 				}
	 				if(imageViewReference != null){
	 					ImageView imageview = imageViewReference.get();
	 					AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);
	 				    // Change bitmap only if this process is still associated with it
	 				    if (this == loadImageTask) {
	 				    	imageview.setImageBitmap(resultBitmap);
	 				    	//imageview.setScaleType(ImageView.ScaleType.CENTER_INSIDE);
	 				    }
	 				}
	 				super.onPostExecute(resultBitmap);
	 			}							
	 		}
	 		
	 		//当 loadImageTask.cancel(true)被执行的时候，则AsyncLoadImageTask 就会被取消，
	 		//当AsyncLoadImageTask 任务执行到onPostExecute的时候，如果这个任务加载到了图片，
	 		//它也会把这个bitmap设为null了。 
	 		//getAsyncLoadImageTask代码如下：
	 		private AsyncLoadImageTask getAsyncLoadImageTask(ImageView imageview){
	 			if (imageview != null) {
	 		        Drawable drawable = imageview.getDrawable();
	 		        if (drawable instanceof LoadedDrawable) {
	 		        	LoadedDrawable loadedDrawable = (LoadedDrawable)drawable;
	 		            return loadedDrawable.getLoadImageTask();
	 		        }
	 		    }
	 		    return null;
	 		}
	 		private Bitmap getBitmapFromUrl(String url,String ThumName){
	 			Bitmap bitmap = null;
	 			bitmap = SdCardPhotoListActivity.gridviewBitmapCaches.get(url);
	 			if(bitmap != null){
	 				
	 				return bitmap;
	 			}
	 		
	 			//bitmap = BitmapFactory.decodeFile(url);
	 			//这里我们不用BitmapFactory.decodeFile(url)这个方法
	 			//用decodeFileDescriptor()方法来生成bitmap会节省内存
	 			//查看源码对比一下我们会发现decodeFile()方法最终是以流的方式生成bitmap
	 			//而decodeFileDescriptor()方法是通过Native方法decodeFileDescriptor生成bitmap的
	 		 			
try {
 				
				
					
 					bitmap = BitmapUtilities.GetBitmap(StaticParameter.CacheDir+ThumName);
 			        if(bitmap==null)
 			        {
 			        	bitmap = BitmapUtilities.readBitmapAutoSize(url,init.GridViewImageW,init.GridViewImageW);
 	 				   DataOperate.CreateThumbnail(ThumName,bitmap);
 			        }
 				
				//bitmap = DataOperate.getImageThumbnail(mcontext, mcontext.getContentResolver(), url);
			} catch (Exception e) {
				// TODO Auto-generated catch block

				
			
			} 
				//int wdith=init.GridViewImageW;
				//bitmap = BitmapUtilities.getBitmapThumbnail(bitmap,wdith,wdith);
				return bitmap;
	 		}
	 		
	 		//该类功能为：记录imageview加载任务并且为imageview设置默认的drawable
	 		public static class LoadedDrawable extends ColorDrawable{
	 			private final WeakReference<AsyncLoadImageTask> loadImageTaskReference;

	 		    public LoadedDrawable(AsyncLoadImageTask loadImageTask) {
	 		        super(Color.TRANSPARENT);
	 		        loadImageTaskReference =
	 		            new WeakReference<AsyncLoadImageTask>(loadImageTask);
	 		    }

	 		    public AsyncLoadImageTask getLoadImageTask() {
	 		        return loadImageTaskReference.get();
	 		    }

	 		} 
	    
	    
	    
	    
		}  