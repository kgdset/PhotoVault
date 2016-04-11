/*******************************************************************************
 * Copyright 2011-2013 Sergey Tarasevich
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *******************************************************************************/
package com.kgdset.photovault;

import java.lang.ref.SoftReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import uk.co.senab.photoview.PhotoView;

import android.R.integer;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.os.Parcelable;
import android.renderscript.Int3;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.View.OnClickListener;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.RelativeLayout.LayoutParams;

import com.viewpagerindicator.HackyViewPager;
import com.viewpagerindicator.PageIndicator;

/**
 * @author Sergey Tarasevich (nostra13[at]gmail[dot]com)
 */
public class ImagePagerActivity extends BaseActivity {
	
	//protected ImageLoader imageLoader = ImageLoader.getInstance();
	
	
	
	
	
	private static final String STATE_POSITION = "STATE_POSITION";


	private static final String IMAGE_POSITION = "image_index";
	public ImageView titleback;
	static public TextView titletext;
	
	 private List<String> picList;
    
	 private Map<Integer,Bitmap> BitmapList;
    
	 private int _position = 0;// 当前为第一张  
	    private String dirname;
	    private String dirid;
	HackyViewPager pager;
	//PageIndicator mIndicator; 
	
	
	public void SetAdve()
	{
		//layout1 = (FrameLayout) ;

		AdwoView.SetAdwo(this,(ViewGroup) findViewById(R.id.ReLayout),5);
	}
	
	
	
	
	@Override
    protected void onDestroy() { 
		 super.onDestroy();
		 pager.removeAllViews();
	Iterator keys = BitmapList.keySet().iterator();
			
			while(keys.hasNext()){
				Object key = keys.next();//key
				Bitmap delBitmap = null;
				delBitmap = BitmapList.get(key);
				if(delBitmap!=null)
				{
					delBitmap.recycle();
				}
			}
			BitmapList.clear();
	//System.gc();
  
    }
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_CUSTOM_TITLE); //
		setContentView(R.layout.ac_image_pager);
		getWindow().setFeatureInt(Window.FEATURE_CUSTOM_TITLE, R.layout.title);//在title layout中，可以定制之
		SetAdve();
		ThemeOperate.setbackground(this,(ViewGroup)findViewById(R.id.ReLayout),(ViewGroup)findViewById(R.id.title_bar),getAssets());
		BitmapList=new HashMap<Integer,Bitmap>();
		Bundle extras = getIntent().getExtras(); 
		titleback=(ImageView)findViewById(R.id.backbtn);
		titletext=(TextView)findViewById(R.id.titletext);
		titleback.setOnClickListener(new OnClickListener()
		 {
		 public void onClick(View v)
		 {
			
			 finish();
	  	        overridePendingTransition(R.anim.in_from_left, R.anim.out_to_right);
	  	        // overridePendingTransition(R.anim.in_from_up,R.anim.out_to_bottom);
	  	     
		 }  
		 });
		
		_position= extras.getInt("position"); 
		dirname= extras.getString("dirname"); 
		dirid= extras.getString("dirid"); 
		 String foldername;
	        if(dirname.equals("相机照片"))
	        {
	        	foldername=dirname;
	        }
	        else
	        {
	        	foldername=dirid;
	        }
		
		//Bundle bundle = getIntent().getExtras();
		//String[] imageUrls = bundle.getStringArray(IMAGES);
		int pagerPosition = extras.getInt(IMAGE_POSITION, _position);

		if (savedInstanceState != null) {
			pagerPosition = savedInstanceState.getInt(STATE_POSITION);
		}

		
		

		pager = (HackyViewPager) findViewById(R.id.pager);
		pager.setAdapter(new ImagePagerAdapter(foldername,this));
		pager.setCurrentItem(pagerPosition);
		// ViewPager滑动监听器  
		pager.setOnPageChangeListener(new MyListener()); 
              
         
		//CirclePageIndicator	mIndicator = (CirclePageIndicator)findViewById(R.id.indicator);
	    // mIndicator.setViewPager(pager, pagerPosition);
		
		
	}

	
	
	
	@Override
	public void onSaveInstanceState(Bundle outState) {
		outState.putInt(STATE_POSITION, pager.getCurrentItem());
	}

	private class ImagePagerAdapter extends PagerAdapter {

		
		private LayoutInflater inflater;
		private Context mContext;
		
		ImagePagerAdapter(String dirname,Context context) {
			picList=DataOperate.getPictures(dirname);
			titletext.setText(_position+1+"/"+picList.size());
			this.mContext=context;
			inflater = getLayoutInflater();
		}

		@Override
		public void destroyItem(ViewGroup container, int position, Object object) {
			container.removeView((View)object);
			BitmapList.remove(position);
		Log.v("个数", Integer.toString(BitmapList.size()));
		}

		@Override
		public void finishUpdate(View container) {
		}

		@Override
		public int getCount() {
			return picList.size();
		}
		
		@Override
		public Object instantiateItem(ViewGroup view, int position) {
			
			View imageLayout = inflater.inflate(R.layout.item_pager_image, view, false);
			
			PhotoView imageView = (PhotoView) imageLayout.findViewById(R.id.image);
			//final ProgressBar spinner = (ProgressBar) imageLayout.findViewById(R.id.loading);
			Bitmap bmp = BitmapUtilities.GetBitmap(picList.get(position)); 
			BitmapList.put(position, bmp);
			imageView.setImageBitmap(BitmapList.get(position));
			
			((ViewPager) view).addView(imageLayout, 0);
			
			return imageLayout;
			
		}

		@Override
		public boolean isViewFromObject(View view, Object object) {
			return view.equals(object);
		}

		@Override
		public void restoreState(Parcelable state, ClassLoader loader) {
		}

		@Override
		public Parcelable saveState() {
			return null;
		}

		@Override
		public void startUpdate(View container) {
		}
	}
	
	
	
	class MyListener implements OnPageChangeListener { 
		//当滑动状态改变时调用 
		@Override 
		public void onPageScrollStateChanged(int arg0) { 
		// TODO Auto-generated method stub 
		//arg0=arg0%list.size(); 

		} 
		//当当前页面被滑动时调用 
		@Override 
		public void onPageScrolled(int arg0, float arg1, int arg2) { 
		// TODO Auto-generated method stub 

		} 
		//当新的页面被选中时调用 
		@Override 
		public void onPageSelected(int arg0) { 
		if(arg0>2){ 
		arg0=arg0%picList.size(); 
		
		} 
		 

		titletext.setText(arg0+1+"/"+picList.size());
		} 
		} 
		

}