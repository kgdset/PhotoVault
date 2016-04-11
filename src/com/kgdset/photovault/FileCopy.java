package com.kgdset.photovault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileCopy {
	/**  
     * 复制单个文件  
     * @param oldPath String 原文件路径 如：c:/fqf.txt  
     * @param newPath String 复制后路径 如：f:/fqf.txt  
     * @return boolean  
     */   
   public static void copyFile(String oldPath, String newPath) {   
       try {   
    	   Bitmap  newbitmap;
    	   BitmapFactory.Options opt = new BitmapFactory.Options();  
           opt.inJustDecodeBounds = true;            
                   //设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度  
           BitmapFactory.decodeFile(oldPath, opt);  
     
           int outWidth = opt.outWidth; //获得图片的实际高和宽  
           int outHeight = opt.outHeight; 
           opt.inJustDecodeBounds = false;   
    	   if((outWidth*outHeight)>(init.Pwidth*init.Pheight))
    	   {
    		   float k;   
    		   k=((float)outWidth/(float)outHeight);   
    		   if(outWidth>outHeight)   
    		   {   
    			   outWidth=init.Pwidth;
    			   outHeight=(int) (outWidth/k);   
    		   }   
    		   else   
    		   {   
    			   outHeight=init.Pheight;
    			   outWidth=(int) (outHeight*k);   
    		   }//压缩图片
    		   //newbitmap=BitmapUtilities.readBitmapAutoSize(oldPath,width,width);
    		   newbitmap=BitmapUtilities.readBitmapAutoSize(oldPath, outWidth, outHeight);
    	   }
    	   else
    	   {
    		  newbitmap=BitmapUtilities.GetBitmap(oldPath); //不压缩
    	   }
    	   LoadPhotoCarema(newbitmap,newPath);
    	  // bitmap.recycle();
    	   newbitmap.recycle();
            
       }   
       catch (Exception e) {   
           System.out.println("复制单个文件操作出错");   
           e.printStackTrace();   
  
       }   
  
   }   
   public static boolean LoadPhotoCarema(Bitmap bitmap1,String foldername)
	{
		
		

	
		Bitmap bitmap = bitmap1;// 获取相机返回的数据，并转换为Bitmap图片格式
		FileOutputStream b = null;
		//String PhotoName=Long.toString(System.currentTimeMillis());
		String fileName = foldername;
		
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			bitmap1.recycle();
			bitmap.recycle();
			System.gc();
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} 
		
	}
   /**  
     * 复制文件  
     * @param oldPath String 原文件路径 如：c:/fqf  
     * @param newPath String 复制后路径 如：f:/fqf/ff  
     * @return boolean  
     */   
   public static void copyfiletosd(String oldPath, String newPath) {   
  
       try {   
           (new File(newPath)).mkdirs(); //如果文件夹不存在 则建立新文件夹   
           File temp=new File(oldPath);   
         
  
               if(temp.isFile()){   
                   FileInputStream input = new FileInputStream(temp);  
                   String imagename=temp.getName();
           		imagename=imagename.substring(0, imagename.lastIndexOf("."));
           		imagename=imagename+".jpg";
                   FileOutputStream output = new FileOutputStream(newPath + "/" +   
                		   imagename);   
                   byte[] b = new byte[1024 * 5];   
                   int len;   
                   while ( (len = input.read(b)) != -1) {   
                       output.write(b, 0, len);   
                   }   
                   output.flush();   
                   output.close();   
                   input.close();   
               }   
              
       }   
       catch (Exception e) {   
           System.out.println("复制文件出错");   
           e.printStackTrace();   
  
       }   
  
   }  
}
