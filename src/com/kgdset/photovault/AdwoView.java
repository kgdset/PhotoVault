package com.kgdset.photovault;


import net.youmi.android.banner.AdSize;
import net.youmi.android.offers.OffersAdSize;
import net.youmi.android.offers.OffersBanner;

import org.json.JSONObject;

import android.app.Activity;
import android.content.Context;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;

import com.baidu.mobads.AdSettings;
import com.baidu.mobads.AdView;
import com.baidu.mobads.AdViewListener;



public class AdwoView extends Activity{
	//static ViewGroup viewLayout;
	/**
	 * ���� Banner
	 */
	static OffersBanner mBanner;
	/**
	 * ���� Mini Banner
	 */
	static OffersBanner mMiniBanner;

	/**
	 * ��ʾ�������Ŀؼ�
	 */
	static LayoutParams params = null;
	//private String LOG_TAG = "Adwo";
	static boolean testMode=false;
	static int refreshInterval=40;
	//static int AdwoType=0;//0 ����  1�ٶ�
	
	static public void SetAdwo(Context mcontext,ViewGroup view,int AdwoType)
	{
		view.clearDisappearingChildren();
		if(!StaticParameter.AdOpen)
		{
			
		if(AdwoType==0)
		{
			
			mMiniBanner = new OffersBanner(mcontext, OffersAdSize.SIZE_MATCH_SCREENx32);//
			
			view.addView(mMiniBanner);
		}
		else if(AdwoType==3)
		{
			
			mBanner = new OffersBanner(mcontext, OffersAdSize.SIZE_MATCH_SCREENx60);
			mMiniBanner = new OffersBanner(mcontext, OffersAdSize.SIZE_MATCH_SCREENx32);
			RelativeLayout.LayoutParams layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mBanner.setLayoutParams(layout);
			view.addView(mMiniBanner);
			view.addView(mBanner);
		}
		else if(AdwoType==2)
		{
			mMiniBanner = new OffersBanner(mcontext, OffersAdSize.SIZE_MATCH_SCREENx32);
			view.addView(mMiniBanner);
			RelativeLayout.LayoutParams layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
		
			net.youmi.android.banner.AdView adve= new net.youmi.android.banner.AdView(mcontext, AdSize.FIT_SCREEN);
			adve.setLayoutParams(layout);
			view.addView(adve);
		}
		else if(AdwoType==5)
		{
			mMiniBanner = new OffersBanner(mcontext, OffersAdSize.SIZE_MATCH_SCREENx32);
			
			RelativeLayout.LayoutParams layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mMiniBanner.setLayoutParams(layout);
			view.addView(mMiniBanner);
			AdSettings.setKey(new String[] { "baidu", "�� �� " });
			
			// ʵ����,Ĭ��Ϊ���banner���
			AdView adView = new AdView(mcontext);
		
			// ����adViewΪ��ǰActivity��View
			view.addView(adView);
		}
		else if(AdwoType==1)
		{
			mMiniBanner = new OffersBanner(mcontext, OffersAdSize.SIZE_MATCH_SCREENx32);
			RelativeLayout.LayoutParams layout=new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT);
			layout.addRule(RelativeLayout.CENTER_HORIZONTAL);
			layout.addRule(RelativeLayout.ALIGN_PARENT_BOTTOM);
			mMiniBanner.setLayoutParams(layout);
			view.addView(mMiniBanner);
				AdSettings.setKey(new String[] { "baidu", "�� �� " });
				
				// ʵ����,Ĭ��Ϊ���banner���
				AdView adView = new AdView(mcontext);
			
				// ����adViewΪ��ǰActivity��View
				view.addView(adView);

				// ���ü�����
				adView.setListener(new AdViewListener() {

					public void onAdSwitch() {
						Log.w("", "onAdSwitch");
					}

					public void onAdShow(JSONObject info) {
						Log.w("", "onAdShow " + info.toString());
					}

					public void onAdReady(AdView adView) {
						Log.w("", "onAdReady " + adView);
					}

					public void onAdFailed(String reason) {
						Log.w("", "onAdFailed " + reason);
					}

					public void onAdClick(JSONObject info) {
						Log.w("", "onAdClick " + info.toString());
					}

					public void onVideoStart() {
						Log.w("", "onVideoStart");
					}

					public void onVideoFinish() {
						Log.w("", "onVideoFinish");
					}
					
					@Override
					public void onVideoClickAd() {
						Log.w("", "onVideoFinish");
						
					}

					@Override
					public void onVideoClickClose() {
						Log.w("", "onVideoFinish");
						
					}

					@Override
					public void onVideoClickReplay() {
						Log.w("", "onVideoFinish");
						
					}

					@Override
					public void onVideoError() {
						Log.w("", "onVideoFinish");
						
					}
				});
		}
		}
	}
	
	
}
