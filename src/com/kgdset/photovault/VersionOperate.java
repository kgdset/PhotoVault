package com.kgdset.photovault;

import java.sql.Date;
import java.text.SimpleDateFormat;


public class VersionOperate {
	public static void SetIntegral(Integer number)
	{
		Version2 entity=new Version2();
		entity.setintegral(number);
		DataOperate.UpdateVersion2(entity);
		
	}
	public static void ChangeIntegral(String type,Integer number)
	{
		if (type.equals("add"))
		{
			StaticParameter.integral=StaticParameter.integral+number;
		}
		if(type.equals("subtract"))
		{
			StaticParameter.integral=StaticParameter.integral-number;
		}
		Version2 entity=new Version2();
		
		SimpleDateFormat sdate=new SimpleDateFormat("yyy-MM-dd");
		String time=sdate.format(new java.util.Date());
		if(number==StaticParameter.wxnumber)
		{
			entity.setWxtime(time);
		}
		else
		{
			entity.setFriendtime(time);
		}
		entity.setintegral(StaticParameter.integral);
		DataOperate.UpdateVersion2(entity);
		
		}
	public static void ChangeIntegral1(String type,Integer number)
	{
		if (type.equals("add"))
		{
			StaticParameter.integral=StaticParameter.integral+number;
		}
		if(type.equals("subtract"))
		{
			StaticParameter.integral=StaticParameter.integral-number;
		}
		Version2 entity=new Version2();
		entity.setintegral(StaticParameter.integral);
		DataOperate.UpdateVersion2(entity);
		
		}
		public static void Changeusestr(String str)
		{
			
			Version3 entity=new Version3();
			entity.setusebgstr(str);
			DataOperate.UpdateVersion3(entity);
			
			}
	public static boolean IsAddIntegral(Integer number)
	{
		SimpleDateFormat sdate=new SimpleDateFormat("yyy-MM-dd");
		String time=sdate.format(new java.util.Date());
		Version2 entity=new Version2();
		entity=DataOperate.loadVersion2();
		if(number==StaticParameter.wxnumber)
		{
			if(time.equals(entity.GetWxtime()))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		else
		{
			if(time.equals(entity.GetFriendtime()))
			{
				return false;
			}
			else
			{
				return true;
			}
		}
		
	}
	public static boolean IsOpenPattern()
	{
		if(StaticParameter.integral>=StaticParameter.PatternIntegral)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static boolean IsClosePattern()
	{
		if(StaticParameter.integral>=StaticParameter.CloseAdIntegral)
		{
			return true;
		}
		else
		{
			return false;
		}
	}
	public static void changeAd(boolean Open)
	{
		Version2 entity=new Version2();
		if(Open)
		{
			StaticParameter.AdOpen=true;
		entity.setAdOpen("1");
		}
		else
		{
			StaticParameter.AdOpen=false;
			entity.setAdOpen("0");
		}
		DataOperate.UpdateVersion2(entity);
	}
	public static void changebgstr(String number)
	{
		Version3 entity=new Version3();
		
			
			entity.setbgstr(number);
		
		DataOperate.UpdateVersion3(entity);
		StaticParameter.bgstr=number;
	}
}
