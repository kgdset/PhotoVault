package com.kgdset.photovault;

import java.io.BufferedInputStream;
import java.io.FileInputStream;

import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.graphics.PorterDuff.Mode;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
public class BitmapUtilities {

	public BitmapUtilities() {
		// TODO Auto-generated constructor stub
	}

	public static Bitmap readBitmapAutoSize(String filePath, int outWidth, int outHeight) {    
        //outWidth��outHeight��Ŀ��ͼƬ������Ⱥ͸߶ȣ���������  
		FileInputStream fs = null;  
		BufferedInputStream bs = null;  
		try {  
    fs = new FileInputStream(filePath);  
    bs = new BufferedInputStream(fs);  
    BitmapFactory.Options options = setBitmapOption(filePath, outWidth, outHeight);  
    return BitmapFactory.decodeStream(bs, null, options);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
    try {  
        bs.close();  
        fs.close();  
    	} catch (Exception e) {  
        e.printStackTrace();  
    }  
}  
return null;  
}
	public static Bitmap GetBitmap(String filePath) {    
        //outWidth��outHeight��Ŀ��ͼƬ������Ⱥ͸߶ȣ���������  
		FileInputStream fs = null;  
		
		try { 
			 BitmapFactory.Options opt = new BitmapFactory.Options();  
		     opt.inPreferredConfig = Bitmap.Config.RGB_565;   
		      opt.inPurgeable = true;  
		      opt.inInputShareable = true; 
		 
		 
    fs = new FileInputStream(filePath);  
 
    return BitmapFactory.decodeFileDescriptor(fs.getFD(), null, opt);  
		} catch (Exception e) {  
			e.printStackTrace();  
		} finally {  
    try {  
      
        fs.close();  
    	} catch (Exception e) {  
        e.printStackTrace();  
    }  
}  
return null;  
}
	private static BitmapFactory.Options setBitmapOption(String file, int width, int height) {  
        BitmapFactory.Options opt = new BitmapFactory.Options();  
        opt.inJustDecodeBounds = true;            
                //����ֻ�ǽ���ͼƬ�ı߾࣬�˲���Ŀ���Ƕ���ͼƬ��ʵ�ʿ�Ⱥ͸߶�  
        BitmapFactory.decodeFile(file, opt);  
  
        int outWidth = opt.outWidth; //���ͼƬ��ʵ�ʸߺͿ�  
        int outHeight = opt.outHeight;  
        opt.inDither = false;  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;      
                //���ü���ͼƬ����ɫ��Ϊ16bit��Ĭ����RGB_8888����ʾ24bit��ɫ��͸��ͨ������һ���ò���  
        opt.inSampleSize = 1;                            
                //�������ű�,1��ʾԭ������2��ʾԭ�����ķ�֮һ....  
                //�������ű�  
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {  
            int sampleSize = (outWidth / width + outHeight / height) / 2;  
            opt.inSampleSize = sampleSize;  
        }  
  
        opt.inJustDecodeBounds = false;//���ѱ�־��ԭ  
        return opt;  
    } 
	/**  
	 *  ����ͼƬ   
	 * @param bm ��Ҫת����bitmap  
	 * @param newWidth�µĿ�  
	 * @param newHeight�µĸ�    
	 * @return ָ����ߵ�bitmap  
	 */  
	 public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){    
	    // ���ͼƬ�Ŀ��    
	    int width = bm.getWidth();    
	    int height = bm.getHeight();    
	    // �������ű���    
	    float scaleWidth = ((float) newWidth) / width;    
	    float scaleHeight = ((float) newHeight) / height;    
	    // ȡ����Ҫ���ŵ�matrix����    
	    Matrix matrix = new Matrix();    
	    matrix.postScale(scaleWidth, scaleHeight);    
	    // �õ��µ�ͼƬ    
	    Bitmap newbm = Bitmap.createBitmap(bm, 0, 0, width, height, matrix, true);    
	    return newbm;    
	}
	 
	 
	 public static Bitmap toRoundCorner(Bitmap bitmap, int pixels) { 

		 Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Config.ARGB_8888); 
		 Canvas canvas = new Canvas(output); 
		 final int color = 0xff424242; 
		 final Paint paint = new Paint(); 
		 final Rect rect = new Rect(0, 0, bitmap.getWidth(), bitmap.getHeight()); 
		 final RectF rectF = new RectF(rect); 
		 final float roundPx = pixels; 
		 paint.setAntiAlias(true); 
		 canvas.drawARGB(0, 0, 0, 0); 
		 paint.setColor(color); 
		 canvas.drawRoundRect(rectF, roundPx, roundPx, paint); 
		 paint.setXfermode(new PorterDuffXfermode(Mode.SRC_IN)); 
		 canvas.drawBitmap(bitmap, rect, rect, paint); 
		 return output; 
		 }
}
