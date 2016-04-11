package com.kgdset.photovault.wxapi;

import net.youmi.android.offers.PointsManager;

import com.baidu.mobstat.StatService;
import com.kgdset.photovault.ListViewActivity;
import com.kgdset.photovault.R;

import com.kgdset.photovault.VersionOperate;

import com.kgdset.photovault.StaticParameter;
import com.tencent.mm.sdk.openapi.BaseReq;
import com.tencent.mm.sdk.openapi.BaseResp;

import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.IWXAPIEventHandler;
import com.tencent.mm.sdk.openapi.WXAPIFactory;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;

import android.widget.Toast;

public class WXEntryActivity extends Activity implements IWXAPIEventHandler{
	
	
	
	
	
	// IWXAPI �ǵ�����app��΢��ͨ�ŵ�openapi�ӿ�
    private IWXAPI api;
	
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        api=WXAPIFactory.createWXAPI(this, StaticParameter.APP_wx_ID,true);
        api.handleIntent(getIntent(), this);
        finish();
    }

	@Override
	protected void onNewIntent(Intent intent) {
		super.onNewIntent(intent);
		
		setIntent(intent);
        api.handleIntent(intent, this);
	}

	// ΢�ŷ������󵽵�����Ӧ��ʱ����ص����÷���
	@Override
	public void onReq(BaseReq req) {
		switch (req.getType()) {
		
		}
	}

	// ������Ӧ�÷��͵�΢�ŵ�����������Ӧ�������ص����÷���
	@Override
	public void onResp(BaseResp resp) {
		int result = 0;
		switch (resp.errCode) {
		case BaseResp.ErrCode.ERR_OK:
		    if(VersionOperate.IsAddIntegral(StaticParameter.addnumber))
		    {
			VersionOperate.ChangeIntegral("add", StaticParameter.addnumber);
			
			PointsManager.getInstance(this).awardPoints(StaticParameter.addnumber);
			StatService.onEvent(WXEntryActivity.this, "3", "����:+"+StaticParameter.addnumber+"", 1);
		    }
			result = R.string.errcode_success;
			break;
		case BaseResp.ErrCode.ERR_USER_CANCEL:
			result =0;
			break;
		case BaseResp.ErrCode.ERR_AUTH_DENIED:
			result = R.string.errcode_deny;
			break;
		default:
			result = R.string.errcode_unknown;
			break;
		}
		ListViewActivity.instance.IsGocamera=false;
		if(result!=0)
		{
		Toast.makeText(this, result, Toast.LENGTH_LONG).show();
		}
	}
	

	
}