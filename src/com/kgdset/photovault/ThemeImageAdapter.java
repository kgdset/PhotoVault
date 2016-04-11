package com.kgdset.photovault;

import java.io.InputStream;
import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.List;

import net.youmi.android.offers.PointsManager;

import com.baidu.mobstat.StatService;
import com.kgdset.photovault.wxapi.WXEntryActivity;

import android.R.integer;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.res.AssetManager;
import android.content.res.ColorStateList;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;



public class ThemeImageAdapter extends BaseAdapter{
	  int height = init.Pheight/3;
       int width = init.Pwidth/2-40;
       boolean isuse=false;
       String [] usestrs=null;
		//  public static  List<LoadImage> picList = new ArrayList<LoadImage>();    //图片集合  
		 
		    
		    private LayoutInflater inflater;  
		    private List<String> mList = null;
		    private Context mcontext;
		    AssetManager assets = null;  
		    public ThemeImageAdapter(Context mContext,List<String> list,AssetManager asset){  
		        inflater = LayoutInflater.from(mContext);  
		        mcontext=mContext;
		        mList=list;
		        assets=asset;
		        String usestr=StaticParameter.usebgstr;
	            if(usestr.length()>0)
	            {
	            usestrs=StaticParameter.usebgstr.split(",");
	            }
		       
		    }  
		
		    @Override  
		    public int getCount() {  
		        return mList.size();  
		    }  
		  
		    @Override  
		    public Object getItem(int position) {  
		    	
		        return mList.get(position); 
		        
		    }  
		 
		   
		    @Override  
		    public long getItemId(int position) {  
		    	
		        return position;  
		    }  
		  
		  
			@Override  
		    public View getView(int position, View convertView, ViewGroup parent) {  
		        ViewHolder holder = null; 
		        final int id=position;
		        final String url = mList.get(position);
		        if(holder==null){  
		        	 holder = new ViewHolder();  
		            convertView = inflater.inflate(R.layout.theme_item, null);  
		            holder.image1 = (ImageView)convertView.findViewById(R.id.scan_img);  
		            holder.image2 = (ImageView)convertView.findViewById(R.id.scan_select);
		            //holder.textView2=(TextView)convertView.findViewById(R.id.jifentxt);
		            holder.applybtn=(Button)convertView.findViewById(R.id.textView1);
		            if(usestrs!=null)
		            {
		            	for(int i=0;i<usestrs.length;i++)
		            	{
		            		if(url.equals(usestrs[i]))
		            		{
		            		   isuse=true;
		            		   break;
		            		}
		            		else
		            		{
		            			isuse=false;
		            		}
		            	}
		            }
		            else
		            {
		            	isuse=false;
		            }
		           
			        String ThumName= url.substring(0,url.lastIndexOf("."));
			        final int type=Integer.parseInt(ThumName.substring(ThumName.length()-1,ThumName.length()));
			        
			       if(type==0)
			    	   
			        {
			       
			        	holder.applybtn.setText("应用   (免费)");
			        }else
			        {
			       
			        	if(isuse)
			        	{
			        		holder.applybtn.setText("应用 (已购买)");
			        	}
			        	else
			        	{
			        	holder.applybtn.setText("应用 (积分-"+type*100+")");
			        	}
			        	holder.applybtn.setTextColor(Color.parseColor("#bb4400"));  
			        
			        
			        }
		            holder.applybtn.setOnClickListener(new View.OnClickListener() {  
		                
		                @Override  
		                public void onClick(View v) {  
		                    // TODO Auto-generated method stub 
		                	if(type==0)
		                	{
		                   VersionOperate.changebgstr(String.valueOf(id));
		                	
		                   ThemeOperate.cleardrawable();
		                   if(ThemeActivity.instance!=null)
		                   {
		                  ThemeActivity.instance.finish();
		                   }
		                   StatService.onEvent(ThemeActivity.instance, "6", "主题:"+id+"", 1);  
		                   Toast.makeText(mcontext, "设置主题成功！", Toast.LENGTH_SHORT)
		                     .show();
		                	}
		                	  else
				                {
		                		  if(usestrs!=null)
		      		            {
		      		            	for(int i=0;i<usestrs.length;i++)
		      		            	{
		      		            		if(url.equals(usestrs[i]))
		      		            		{
		      		            		   isuse=true;
		      		            		   break;
		      		            		}
		      		            		else
		      		            		{
		      		            			isuse=false;
		      		            		}
		      		            	}
		      		            }
		      		            else
		      		            {
		      		            	isuse=false;
		      		            }
		                		  if(isuse)
		  			        	{
		                			  VersionOperate.changebgstr(String.valueOf(id));
		  		                	
		   		                   ThemeOperate.cleardrawable();
		   		                   if(ThemeActivity.instance!=null)
		   		                   {
		   		                  ThemeActivity.instance.finish();
		   		                   }
		   		                   StatService.onEvent(ThemeActivity.instance, "6", "主题:"+id+"", 1);  
		   		                   Toast.makeText(mcontext, "设置主题成功！", Toast.LENGTH_SHORT)
		   		                     .show();
		  			        	}
		  			        	else
		  			        	{
		                		  int num=type*100;
				                	if(StaticParameter.integral>=num)
				                	{
				                		VersionOperate.ChangeIntegral1("subtract", num);
				                		PointsManager.getInstance(ThemeActivity.instance).spendPoints(num);
				                		
				                		  VersionOperate.changebgstr(String.valueOf(id));
						                	
						                   ThemeOperate.cleardrawable();
						                   if(ThemeActivity.instance!=null)
						                   {
						                  ThemeActivity.instance.finish();
						                   }
						                   
						                   StatService.onEvent(ThemeActivity.instance, "6", "主题:"+id+"", 1); 
						                   Toast.makeText(mcontext, "设置主题成功！", Toast.LENGTH_SHORT)
						                     .show();
						                   StaticParameter.usebgstr=StaticParameter.usebgstr+url+",";
						                   VersionOperate.Changeusestr(StaticParameter.usebgstr);
				                	}
				                	else
				                	{
				                		AlertDialog.Builder builder = new AlertDialog.Builder(ThemeActivity.instance);
										
										builder.setIcon(android.R.drawable.ic_dialog_info);
										builder.setTitle("亲，您的积分不够哦！");
										builder.setPositiveButton("不去了", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												
											}
										});
										builder.setNegativeButton("马上去赚积分", new DialogInterface.OnClickListener() {
											public void onClick(DialogInterface dialog, int whichButton) {
												
												net.youmi.android.offers.OffersManager.getInstance(ThemeActivity.instance).showOffersWall();
												ThemeActivity.instance.finish();

											}
										});
										
										builder.create().show();
				                	}
		  			        	}
				                }
		                	
		                }
		              
		            });  
		            android.view.ViewGroup.LayoutParams para;
	              para = holder.image1.getLayoutParams();
	  	        para.height = init.Pheight/3;
	  	        para.width = init.Pwidth/2-40;
	  	       // holder.image2.setLayoutParams(para);
	  	        holder.image1.setLayoutParams(para);
		            convertView.setTag(holder);  
		        }else{  
		        	  holder = (ViewHolder)convertView.getTag();
		        	  }  
		       
				//首先我们先通过cancelPotentialLoad方法去判断imageview是否有线程正在为它加载图片资源，
				//如果有现在正在加载，那么判断加载的这个图片资源（url）是否和现在的图片资源一样，不一样则取消之前的线程（之前的下载线程作废）。
				//见下面cancelPotentialLoad方法代码
				if (cancelPotentialLoad(url, holder.image1)) {
			         AsyncLoadImageTask task = new AsyncLoadImageTask(holder.image1);
			         LoadedDrawable loadedDrawable = new LoadedDrawable(task);
			         holder.image1.setImageDrawable(loadedDrawable);
			         task.execute(position);
			     }		 

		       if(String.valueOf(id).equals(StaticParameter.bgstr))
		       {
		        	  holder.image2.setBackgroundResource(R.drawable.ic_theme_used);
		        	  holder.applybtn.setEnabled(false);
		       }
				//viewHolder.textview_test.setText((position+1)+"");
				return convertView;
		        
		   
		        
		    }  
		      
		    public static class ViewHolder{  
		        public ImageView image1;     //要显示的图片  
		        public ImageView image2;     //图片右上角的小对号图片(标示选中状态的玩意)  
		        public Button applybtn;
		      //  public TextView textView2;
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
					SdCardPhotoSelect.gridviewBitmapCaches.put( mList.get(params[0]), bitmap);			
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
				bitmap = ThemeActivity.gridviewBitmapCaches.get(url);
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
				
				 
	try {
	 				
		String ThumName= url.substring(0,url.lastIndexOf("."));
		 ThumName=ThumName+".thumbnail";
		 bitmap = BitmapUtilities.GetBitmap(StaticParameter.CacheDir+ThumName);
		// bitmap=BitmapUtilities.toRoundCorner(bitmap, 6);
		
		
			
			
	        if(bitmap==null)
	        {
	        	//bitmap = BitmapUtilities.GetBitmap(DataOperate.getImageThumbnail(mcontext,url));
	        	InputStream assetFile = null; 
	   		 
	            assetFile = assets.open(url);  
	          bitmap=BitmapFactory.decodeStream(assetFile);
	    	 				
	    			bitmap=	BitmapUtilities.zoomImg(bitmap,width,height);	//bitmap = DataOperate.getImageThumbnail(mcontext, mcontext.getContentResolver(), url);
				   DataOperate.CreateThumbnail(ThumName,bitmap);
				   bitmap=BitmapUtilities.toRoundCorner(bitmap, 20);
				  
	        }	
	        else
	        {
	        	 bitmap=BitmapUtilities.toRoundCorner(bitmap, 20);
	        }
		
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
