package com.kgdset.photovault;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

public class FileCopy {
	/**  
     * ���Ƶ����ļ�  
     * @param oldPath String ԭ�ļ�·�� �磺c:/fqf.txt  
     * @param newPath String ���ƺ�·�� �磺f:/fqf.txt  
     * @return boolean  
     */   
   public static void copyFile(String oldPath, String newPath) {   
       try {   
    	   Bitmap  newbitmap;
    	   BitmapFactory.Options opt = new BitmapFactory.Options();  
           opt.inJustDecodeBounds = true;            
                   //����ֻ�ǽ���ͼƬ�ı߾࣬�˲���Ŀ���Ƕ���ͼƬ��ʵ�ʿ�Ⱥ͸߶�  
           BitmapFactory.decodeFile(oldPath, opt);  
     
           int outWidth = opt.outWidth; //���ͼƬ��ʵ�ʸߺͿ�  
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
    		   }//ѹ��ͼƬ
    		   //newbitmap=BitmapUtilities.readBitmapAutoSize(oldPath,width,width);
    		   newbitmap=BitmapUtilities.readBitmapAutoSize(oldPath, outWidth, outHeight);
    	   }
    	   else
    	   {
    		  newbitmap=BitmapUtilities.GetBitmap(oldPath); //��ѹ��
    	   }
    	   LoadPhotoCarema(newbitmap,newPath);
    	  // bitmap.recycle();
    	   newbitmap.recycle();
            
       }   
       catch (Exception e) {   
           System.out.println("���Ƶ����ļ���������");   
           e.printStackTrace();   
  
       }   
  
   }   
   public static boolean LoadPhotoCarema(Bitmap bitmap1,String foldername)
	{
		
		

	
		Bitmap bitmap = bitmap1;// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
		FileOutputStream b = null;
		//String PhotoName=Long.toString(System.currentTimeMillis());
		String fileName = foldername;
		
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
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
     * �����ļ�  
     * @param oldPath String ԭ�ļ�·�� �磺c:/fqf  
     * @param newPath String ���ƺ�·�� �磺f:/fqf/ff  
     * @return boolean  
     */   
   public static void copyfiletosd(String oldPath, String newPath) {   
  
       try {   
           (new File(newPath)).mkdirs(); //����ļ��в����� �������ļ���   
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
           System.out.println("�����ļ�����");   
           e.printStackTrace();   
  
       }   
  
   }  
}
