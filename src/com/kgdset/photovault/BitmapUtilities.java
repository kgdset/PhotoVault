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
        //outWidth和outHeight是目标图片的最大宽度和高度，用作限制  
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
        //outWidth和outHeight是目标图片的最大宽度和高度，用作限制  
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
                //设置只是解码图片的边距，此操作目的是度量图片的实际宽度和高度  
        BitmapFactory.decodeFile(file, opt);  
  
        int outWidth = opt.outWidth; //获得图片的实际高和宽  
        int outHeight = opt.outHeight;  
        opt.inDither = false;  
        opt.inPreferredConfig = Bitmap.Config.RGB_565;      
                //设置加载图片的颜色数为16bit，默认是RGB_8888，表示24bit颜色和透明通道，但一般用不上  
        opt.inSampleSize = 1;                            
                //设置缩放比,1表示原比例，2表示原来的四分之一....  
                //计算缩放比  
        if (outWidth != 0 && outHeight != 0 && width != 0 && height != 0) {  
            int sampleSize = (outWidth / width + outHeight / height) / 2;  
            opt.inSampleSize = sampleSize;  
        }  
  
        opt.inJustDecodeBounds = false;//最后把标志复原  
        return opt;  
    } 
	/**  
	 *  处理图片   
	 * @param bm 所要转换的bitmap  
	 * @param newWidth新的宽  
	 * @param newHeight新的高    
	 * @return 指定宽高的bitmap  
	 */  
	 public static Bitmap zoomImg(Bitmap bm, int newWidth ,int newHeight){    
	    // 获得图片的宽高    
	    int width = bm.getWidth();    
	    int height = bm.getHeight();    
	    // 计算缩放比例    
	    float scaleWidth = ((float) newWidth) / width;    
	    float scaleHeight = ((float) newHeight) / height;    
	    // 取得想要缩放的matrix参数    
	    Matrix matrix = new Matrix();    
	    matrix.postScale(scaleWidth, scaleHeight);    
	    // 得到新的图片    
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
