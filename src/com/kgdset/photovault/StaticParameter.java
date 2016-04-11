package com.kgdset.photovault;

import java.util.List;
import java.util.Map;

import android.graphics.Bitmap;

public class StaticParameter {
public static boolean IsSwitch=false;//是否开启后台重输密码
public static boolean IsHideLock=false;//是否隐藏滑动锁
public static String SdcardPath="";//SD卡路径
public static String CacheDir="";//缩略图缓存路径
public static Map<String,String> ThumbnailList;
///Version2
public static boolean IsPwdType=false;//false为数字密码,true为图案解锁
public static Integer integral =0;//总积分
public static Integer PatternIntegral=1000;//开启图案解锁达到的积分条件
public static String PatternOpen="0";//图案解锁是否打开，0关 1开
public static final String APP_wx_ID="wx44291116afbe5710";
public static Integer addnumber=0;//每次增加的积分值，默认0
public static Integer wxnumber=20;//分享到微信的积分
public static Integer wxfriendnumber=100;//分享到朋友圈的积分
public static boolean AdOpen=false;//是否关闭广告
public static Integer CloseAdIntegral=11000;
public static String wxtime="";
public static String wxfriendtime=""; 
public static Integer shareintegral=0;
public static String  bgstr="";
public static String  usebgstr="";
}
