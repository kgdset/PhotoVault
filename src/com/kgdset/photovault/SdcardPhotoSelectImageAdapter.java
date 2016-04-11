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
	//  public static  List<LoadImage> picList = new ArrayList<LoadImage>();    //ͼƬ����  
	    private List<String> picNumber = new ArrayList<String>();       //ѡ��ͼƬ��λ�ü���  
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
	     * ���ѡ��״̬��ͼƬλ�� 
	     * @param position 
	     */  
	    public void addNumber(String position){  
	        picNumber.add(position);  
	    }  
	    /** 
	     * ȥ����ѡ��״̬��ͼƬλ�� 
	     * @param position 
	     */  
	    public void delNumber(String position){  
	        picNumber.remove(position);  
	    }  
	    /** 
	     * �����ѡ�е�ͼƬ״̬ 
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
			//����������ͨ��cancelPotentialLoad����ȥ�ж�imageview�Ƿ����߳�����Ϊ������ͼƬ��Դ��
			//������������ڼ��أ���ô�жϼ��ص����ͼƬ��Դ��url���Ƿ�����ڵ�ͼƬ��Դһ������һ����ȡ��֮ǰ���̣߳�֮ǰ�������߳����ϣ���
			//������cancelPotentialLoad��������
			if (cancelPotentialLoad(url, holder.image1)) {
		         AsyncLoadImageTask task = new AsyncLoadImageTask(holder.image1);
		         LoadedDrawable loadedDrawable = new LoadedDrawable(task);
		         holder.image1.setImageDrawable(loadedDrawable);
		         task.execute(position);
		     }		 

	        if(picNumber.contains(""+position)){    //�����ͼƬ��ѡ��״̬��ʹ�����Ͻǵ�С�Ժ�ͼƬ��ʾ��������ӱ߿�  
	            holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_pressed);
	           // holder.image1.setImageResource(R.drawable.arrow);  
	        }else{  
	        	  holder.image2.setBackgroundResource(R.drawable.gridview_checkbox_normal);
	        }  
	        
			//viewHolder.textview_test.setText((position+1)+"");
			return convertView;
	        
	   
	        
	    }  
	      
	    public static class ViewHolder{  
	        public ImageView image1;     //Ҫ��ʾ��ͼƬ  
	        public ImageView image2;     //ͼƬ���Ͻǵ�С�Ժ�ͼƬ(��ʾѡ��״̬������)  
	    }  
	    private boolean cancelPotentialLoad(String url,ImageView imageview){
			AsyncLoadImageTask loadImageTask = getAsyncLoadImageTask(imageview);

		    if (loadImageTask != null) {
		        String bitmapUrl = loadImageTask.url;
		        if ((bitmapUrl == null) || (!bitmapUrl.equals(url))) {
		        	loadImageTask.cancel(true);	        	
		        } else {
		            // ��ͬ��url�Ѿ��ڼ�����.
		            return false;
		        }
		    }
		    return true;

		}
	    
	  //����ͼƬ���첽����	
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
		
		//�� loadImageTask.cancel(true)��ִ�е�ʱ����AsyncLoadImageTask �ͻᱻȡ����
		//��AsyncLoadImageTask ����ִ�е�onPostExecute��ʱ��������������ص���ͼƬ��
		//��Ҳ������bitmap��Ϊnull�ˡ� 
		//getAsyncLoadImageTask�������£�
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
			//url = url.substring(0, url.lastIndexOf("/"));//����֮ǰ��·�����֣�����·�����ԣ���ȡ����ͼƬ
			
			//bitmap = BitmapFactory.decodeFile(url);
			//�������ǲ���BitmapFactory.decodeFile(url)�������
			//��decodeFileDescriptor()����������bitmap���ʡ�ڴ�
			//�鿴Դ��Ա�һ�����ǻᷢ��decodeFile()���������������ķ�ʽ����bitmap
			//��decodeFileDescriptor()������ͨ��Native����decodeFileDescriptor����bitmap��
			
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
		
		//���๦��Ϊ����¼imageview����������Ϊimageview����Ĭ�ϵ�drawable
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
