package com.kgdset.photovault;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import com.kgdset.photovault.SdCardPhotoSelect.LoadImage;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.media.ThumbnailUtils;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;

public class SdcardPhotoSelectImageAdapter extends BaseAdapter {
	 int width=(int) (init.GridViewImageW);
	//  public static  List<LoadImage> picList = new ArrayList<LoadImage>();    //图片集合  
	    private List<String> picNumber = new ArrayList<String>();       //选中图片的位置集合  
	    private int ownposition;
	    private LayoutInflater inflater;  
	    private List<String> mList = null;
	    private Context mcontext;
	    public SdcardPhotoSelectImageAdapter(Context mContext,List<String> list){  
	        inflater = LayoutInflater.from(mContext);  
	        mcontext=mContext;
	        mList=list;
	    }  
	    public int getOwnposition() {
			return ownposition;
		}
	    public void setOwnposition(int ownposition) {
			this.ownposition = ownposition;
		}
	    @Override  
	    public int getCount() {  
	        return mList.size();  
	    }  
	  
	    @Override  
	    public Object getItem(int position) {  
	    	ownposition=position;
	        return mList.get(position); 
	        
	    }  
	    /** 
	     * 添加选中状态的图片位置 
	     * @param position 
	     */  
	    public void addNumber(String position){  
	        picNumber.add(position);  
	    }  
	    /** 
	     * 去除已选中状态的图片位置 
	     * @param position 
	     */  
	    public void delNumber(String position){  
	        picNumber.remove(position);  
	    }  
	    /** 
	     * 清空已选中的图片状态 
	     */  
	    public void clear(){  
	        picNumber.clear();  
	        notifyDataSetChanged();   
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
	            convertView = inflater.inflate(R.layout.griditem, null);  
	            holder.image1 = (ImageView)convertView.findViewById(R.id.scan_img);  
	            holder.image2 = (ImageView)convertView.findViewById(R.id.scan_select); 
	            android.view.ViewGroup.LayoutParams para;
              para = holder.image1.getLayoutParams();
  	        para.height = init.GridViewImageW;
  	        para.width = init.GridViewImageW;
  	       // holder.image2.setLayoutParams(para);
  	        holder.image1.setLayoutParams(para);
	            convertView.setTag(holder);  
	        }else{  
	            holder = (ViewHolder)convertView.getTag();  
	        }  
	        String url = mList.get(position);
			//首先我们先通过cancelPotentialLoad方法去判断imageview是否有线程正在为它加载图片资源，
			//如果有现在正在加载，那么判断加载的这个图片资源（url）是否和现在的图片资源一样，不一样则取消之前的线程（之前的下载线程作废）。
			//见下面cancelPotentialLoad方法代码
			if (cancelPotentialLoad(url, holder.image1)) {
		         AsyncLoadImageTask task = new AsyncLoadImageTask(holder.image1);
		         LoadedDrawable loadedDrawable = new LoadedDrawable(task);
		         holder.image1.setImageDrawable(loadedDrawable);
		         task.execute(position);
		     }		 

	        if(picNumber.contains(""+position)){    //如果该图片在选中状态，使其右上角的小对号图片显示，并且添加边框。  
	            holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed);
	           // holder.image1.setImageResource(R.drawable.arrow);  
	        }else{  
	        	  holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_normal);
	        }  
	        
			//viewHolder.textview_test.setText((position+1)+"");
			return convertView;
	        
	   
	        
	    }  
	      
	    public static class ViewHolder{  
	        public ImageView image1;     //要显示的图片  
	        public ImageView image2;     //图片右上角的小对号图片(标示选中状态的玩意)  
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
				this.url = mList.get(params[0]);			
				bitmap = getBitmapFromUrl(url);
				SdCardPhotoSelect.gridviewBitmapCaches.put(mList.get(params[0]), bitmap);			
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
		private Bitmap getBitmapFromUrl(String url){
			Bitmap bitmap = null;
			bitmap = SdCardPhotoSelect.gridviewBitmapCaches.get(url);
			if(bitmap != null){
				//System.out.println(url);
				return bitmap;
			}
			//url = url.substring(0, url.lastIndexOf("/"));//处理之前的路径区分，否则路径不对，获取不到图片
			
			//bitmap = BitmapFactory.decodeFile(url);
			//这里我们不用BitmapFactory.decodeFile(url)这个方法
			//用decodeFileDescriptor()方法来生成bitmap会节省内存
			//查看源码对比一下我们会发现decodeFile()方法最终是以流的方式生成bitmap
			//而decodeFileDescriptor()方法是通过Native方法decodeFileDescriptor生成bitmap的
			
			 String ThumName= url.substring(url.lastIndexOf("/")+1,url.length());
	 			ThumName= ThumName.substring(0,ThumName.lastIndexOf("."));
	 			 ThumName=ThumName+".thumbnail";
try {
 				
				
					
 					bitmap = BitmapUtilities.GetBitmap(StaticParameter.CacheDir+ThumName);
 			        if(bitmap==null)
 			        {
 			        	//bitmap = BitmapUtilities.GetBitmap(DataOperate.getImageThumbnail(mcontext,url));
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
