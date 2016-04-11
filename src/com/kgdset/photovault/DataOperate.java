package com.kgdset.photovault;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.StringWriter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlSerializer;

import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Xml;

public class DataOperate {

	public static String filePath = "";
	
	//public static String folderspath=SdcardPath+"/folders/";
	//public static String SdcardPath;
	public static String folderspath = "";
	public static List<GridPhotoEntity> photolist;
	
	/** 
     * 通过文件名 获取图片的缩略图 
     *  
     * @param context 
     * @param cr     cr = getContentResolver(); 
     * @param testVideopath  全路径 "/mnt/sdcard/sidamingbu.mp4"; 
     * @return 
     */  
    public static String getImageThumbnail(Context context,String testVideopath) {  
        // final String testVideopath = "/mnt/sdcard/sidamingbu.mp4";  
    	String videoPath = "";  
    	try {
    		 ContentResolver testcr = context.getContentResolver();  
    	        String[] projection = { MediaStore.Images.Media._ID, };  
    	        String whereClause = MediaStore.Images.Media.DATA + " = '" + testVideopath + "'";  
    	        Cursor cursor = testcr.query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI, projection, whereClause,  
    	                null, null);  
    	        int _id = 0;  
    	        
    	        if (cursor == null || cursor.getCount() == 0) {  
    	            return null;  
    	        }  
    	        if (cursor.moveToFirst()) {  
    	  
    	            int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);  
    	        
    	  
    	            do {  
    	                _id = cursor.getInt(_idColumn);  
    	                
    	            } while (cursor.moveToNext());  
    	        }
    	        cursor.close();
    	        String[] projection1 = { MediaStore.Images.Thumbnails.DATA, }; 
    	        String whereClause1 = MediaStore.Images.Thumbnails.IMAGE_ID + " = '" + _id + "'";  
    	        Cursor cursor1 = testcr.query(MediaStore.Images.Thumbnails.EXTERNAL_CONTENT_URI, projection1, whereClause1,  
    	                null, null);

    	        if (cursor1 == null || cursor1.getCount() == 0) {  
    	            return null;  
    	        }  
    	        if (cursor1.moveToFirst()) {  
    	  
    	            //int _idColumn = cursor.getColumnIndex(MediaStore.Images.Media._ID);  
    	            int _dataColumn = cursor1.getColumnIndex(MediaStore.Images.Thumbnails.DATA);  
    	  
    	            do {  
    	                 
    	                videoPath = cursor1.getString(_dataColumn);  
    	                
    	            } while (cursor.moveToNext());  
    	        } 
    	       cursor1.close();
    	      
    	       return videoPath;  
		} catch (Exception e) {
			return videoPath;  // TODO: handle exception
		}
       
        
    } 
    public static void LoadthumbnailPaths()
    {
    	 File fileDir = new File(StaticParameter.CacheDir);  
         File[] files = fileDir.listFiles();  
         if(StaticParameter.ThumbnailList!=null)
         {
         StaticParameter.ThumbnailList.clear();
         }
         StaticParameter.ThumbnailList=new HashMap<String, String>();
         if(files!=null){  
             for(File file:files){  
                 String fileName = file.getName(); 
                 String suffix= fileName.substring(fileName.lastIndexOf(".") + 1,fileName.length());
                 if (fileName.lastIndexOf(".") > 0&&(suffix.equals("thumbnail")))
                 { 
                	 StaticParameter.ThumbnailList.put(file.getName(),file.getPath());
                 }
             }
      }
    }
	 /**
     * 初始化xml文件，没有则创建文件，并写入根节点数据
     * @param context   
     * @return
     */
	
	public static void init(Context context){
		 // String sdcardPath = Environment.getExternalStorageDirectory().toString();
		  //filePath = sdcardPath+"/"+ "parameter.xml";
		if(filePath.length()<1)
		{
		filePath = context.getFilesDir().getPath() + File.separator + "parameter.xml";
		//SdcardPath=context.getFilesDir().getPath() + File.separator;
		folderspath=context.getFilesDir().getPath() + File.separator+"folders/";
		}
		File file = new File(filePath);
		 File destDir = new File(folderspath);
		  if (!destDir.exists()) {
		   destDir.mkdirs();
		  }
		if(!file.exists())
		{
			try {
				file.createNewFile();
				initXmlFile(file);
				CreateXML();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		else
		{
			/***
			 * 第二次大版本更新处理
			 * 
			 * ***/
			if(!SelUserExist("version2"))
			{
				Version2 version2 = new Version2();
				version2.setPatternpwd("0");
				version2.setintegral(50);
				version2.setPwdType("0");
				version2.setPatternOpen("0");
				version2.setAdOpen("0");
				version2.setFriendtime("0");
				version2.setWxtime("0");
				addUpdateVersion2(version2);
			}
			if(!SelUserExist("version3"))
			{
				Version3 version3 = new Version3();
				version3.setbgstr("0");
				version3.setusebgstr("|");
				boolean result=addUpdateVersion3(version3);
			}
		}
	
		
	}
	
	public static boolean LoadPhotoCarema(Bitmap bitmap1,String foldername)
	{
		
		

	
		Bitmap bitmap = bitmap1;// 获取相机返回的数据，并转换为Bitmap图片格式
		FileOutputStream b = null;
		String PhotoName=Long.toString(System.currentTimeMillis());
		String fileName = folderspath+foldername+"/"+PhotoName+".kgdset";
		
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			return true;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			return false;
		} 
		
	}
	/** 
     * Get pictures under directory of strPath 
     * @param strPath 
     * @return list 
     */  
    public static List<String> getPictures(final String FolderName) {  
        List<String> list = new ArrayList<String>();  
          
        File file = new File(folderspath+FolderName);  
        File[] files = file.listFiles();  
          
        if (files == null) {  
            return null;  
        }  
          
        for(int i = 0; i < files.length; i++) {  
            final File f = files[i];  
            if(f.isFile()) {  
                try{  
                    int idx = f.getPath().lastIndexOf(".");  
                    if (idx <= 0) {  
                        continue;  
                    }  
                    String suffix = f.getPath().substring(idx);  
                    if (suffix.toLowerCase().equals(".kgdset"))
                    {  
                        list.add(f.getPath());  
                    }  
                } catch(Exception e) {  
                    e.printStackTrace();  
                }  
            }  
        }  
          
        return list;  
    }
  

    public static void CreateThumbnail(String ThumbnailName,Bitmap bitmap1)
    {
    	if(bitmap1!=null)
    	{
    	Bitmap bitmap = bitmap1;
		FileOutputStream b = null;
		//String PhotoName=ThumbnailName;
		String fileName = StaticParameter.CacheDir+ThumbnailName;
		String ThisDirPath=StaticParameter.CacheDir;
		 File destDir = new File(ThisDirPath);
		  if (!destDir.exists()) {
		   destDir.mkdirs();
		  }
		try {
			
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// 把数据写入文件
			//bitmap1.recycle();
			//bitmap.recycle();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} 
    	}
    }

  
   
    /**
     * ***获取SD卡路径
     * **
     * ***/
    public static String getSDPath(){
    	  String sdDir =null;
    	  boolean sdCardExist = Environment.getExternalStorageState()
    	  .equals(android.os.Environment.MEDIA_MOUNTED); //判断sd卡是否存在
    	  if (sdCardExist)
    	  {
    	  sdDir = Environment.getExternalStorageDirectory().toString();//获取跟目录
    	  }
    	  return sdDir;
    	   
    	 }
	/***
	 * *删除目录及目录下的所有文件
	 * **/
	/**
     * 递归删除文件和文件夹
     * @param file    要删除的根目录
     */
    public static void RecursionDeleteFile(File file){
    	
        if(file.isFile()){
            file.delete();
            return;
        }
        if(file.isDirectory()){
            File[] childFile = file.listFiles();
            if(childFile == null || childFile.length == 0){
                file.delete();
                return;
            }
            for(File f : childFile){
                RecursionDeleteFile(f);
            }
            file.delete();
        }
    }

	public static void CreateFolders(String name)
	{
		
		 File destDir = new File(folderspath+name);
		  if (!destDir.exists()) {
		   destDir.mkdirs();
		  }
	}
public static void CreateXML()
{
	if(!SelExist("新建目录"))
	{
		folders folders = new folders();
		folders.setId(0);
		folders.setName("新建目录");
		folders.setType("0");
		addfolder(folders);
	}
	if(!SelExist("相机照片"))
	{
		folders folders = new folders();
		folders.setId(1);
		folders.setName("相机照片");
		folders.setType("0");
		addfolder(folders);
		CreateFolders("相机照片");
	}
	if(!SelUserExist("UserEntity"))
	{
		UserEntity user = new UserEntity();
		user.setinitial("0");
		user.setbackstageswitch("0");
		user.setIsHideLock("0");
		user.setPwd1("null");
		user.setPwd2("null");
	addUserEntity(user);
	}
	
	
	if(!SelUserExist("version2"))
	{
		Version2 version2 = new Version2();
		version2.setPatternpwd("0");
		version2.setintegral(50);
		version2.setPwdType("0");
		version2.setPatternOpen("0");
		version2.setAdOpen("0");
		version2.setFriendtime("0");
		version2.setWxtime("0");
		addUpdateVersion2(version2);
	}
	if(!SelUserExist("version3"))
	{
		Version3 version3 = new Version3();
		version3.setbgstr("0");
		version3.setusebgstr("|");
		addUpdateVersion3(version3);
	}
	}
	 /**
     * 初始化xml文件
     * @param context   
     * @return
     */
	public static void initXmlFile(File file){
		XmlSerializer serialize=Xml.newSerializer();
		StringWriter writer=new StringWriter();
		try {
			serialize.setOutput(writer);
			serialize.startDocument("UTF-8", true);
			serialize.startTag("", "parameters");			
			serialize.endTag("", "parameters");
			serialize.endDocument();		
			OutputStream os=new FileOutputStream(file);
			OutputStreamWriter ow=new OutputStreamWriter(os);
			ow.write(writer.toString());
			ow.close();
			os.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
	
	}
	
	
		
	/**
     * 读取传入的路径，返回一个document对象      
     * @return document
     */
    public static Document loadInit(){
        Document document = null;
        try{
            DocumentBuilderFactory factory = DocumentBuilderFactory.newInstance();
            DocumentBuilder builder = factory.newDocumentBuilder();
          
            document = builder.parse(new File(filePath));
            document.normalize();
            return document;
        }catch(Exception e){
            e.printStackTrace();
            return null;
        }
    }
    
    /**
     * 增加一个Version3所需要的节点
     * @param Version3   
     * @return 是否增加成功，true为增加成功，false为增加失败
     */
	public static boolean addUpdateVersion3(Version3 session){

        try{
            //读取传入的路径，返回一个document对象
            Document document = loadInit();
            //创建叶节点
            Element eltSession = document.createElement("version3");
            Element bgstr = document.createElement("bgstr");//创建叶节点的第一个元素
            Element usebgstr = document.createElement("usebgstr");
           // Element sswitchel = document.createElement("sswitch");
            //Element IsHide = document.createElement("ishide");
            Text bgstrtxt = document.createTextNode(session.getbgstr());//创建叶节点的第一个元素下的文本节点
            Text usebgstrtxt = document.createTextNode(session.getusebgstr());
            bgstr.appendChild(bgstrtxt);//把该文本节点加入到叶节点的第一个元素里面
            usebgstr.appendChild(usebgstrtxt);
         
            eltSession.appendChild(usebgstr);
            eltSession.appendChild(bgstr);
            //获取根节点
            Element eltRoot = document.getDocumentElement();
            //把叶节点加入到根节点下
            eltRoot.appendChild(eltSession);
            //更新修改后的源文件
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
    /**
     * 增加一个Version2所需要的节点
     * @param Version2   
     * @return 是否增加成功，true为增加成功，false为增加失败
     */
	public static boolean addUpdateVersion2(Version2 session){

        try{
            //读取传入的路径，返回一个document对象
            Document document = loadInit();
            //创建叶节点
            Element eltSession = document.createElement("version2");
            Element pwdtype = document.createElement("pwdtype");//创建叶节点的第一个元素
            Element integral = document.createElement("integral");//创建叶节点的第二个元素
            Element patternpwd = document.createElement("patternpwd");//创建叶节点的第三个元素     
            Element patternopen = document.createElement("patternopen");//创建叶节点的第三个元素     
            Element adopen = document.createElement("adopen");//创建叶节点的第三个元素     
            Element wxtime = document.createElement("wxtime");//创建叶节点的第三个元素    
            Element wxfriendtime = document.createElement("wxfriendtime");//创建叶节点的第三个元素     
           // Element sswitchel = document.createElement("sswitch");
            //Element IsHide = document.createElement("ishide");
            Text pwdtypetxt = document.createTextNode(session.getPwdType());//创建叶节点的第一个元素下的文本节点
            pwdtype.appendChild(pwdtypetxt);//把该文本节点加入到叶节点的第一个元素里面
            Text integraltxt = document.createTextNode(Integer.toString(session.getintegral()));//创建叶节点的第二个元素下的文本节点
            integral.appendChild(integraltxt);//把该文本节点加入到叶节点的第二个元素里面
            Text patternpwdtxt = document.createTextNode(session.getpatternpwd());//创建叶节点的第三个元素下的文本节点
            patternpwd.appendChild(patternpwdtxt);//把该文本节点加入到叶节点的第三个元素里面
            Text patternopentxt = document.createTextNode(session.GetPatternOpen());//创建叶节点的第三个元素下的文本节点
            patternopen.appendChild(patternopentxt);//把该文本节点加入到叶节点的第三个元素里面
            Text adopentxt = document.createTextNode(session.GetAdOpen());//创建叶节点的第三个元素下的文本节点
            adopen.appendChild(adopentxt);//把该文本节点加入到叶节点的第三个元素里面
            Text wxtimetxt = document.createTextNode(session.GetWxtime());//创建叶节点的第三个元素下的文本节点
            wxtime.appendChild(wxtimetxt);//把该文本节点加入到叶节点的第三个元素里面
            Text wxfriendtimetxt = document.createTextNode(session.GetFriendtime());//创建叶节点的第三个元素下的文本节点
            wxfriendtime.appendChild(wxfriendtimetxt);//把该文本节点加入到叶节点的第三个元素里面
            //Text sswitch = document.createTextNode(session.getbackstageswitch());
           // sswitchel.appendChild(sswitch);
           // Text ishide = document.createTextNode(session.getIsHideLock());
           // IsHide.appendChild(ishide);
            //把叶节点下的元素加入到叶节点下
            eltSession.appendChild(pwdtype);
            eltSession.appendChild(integral);
            eltSession.appendChild(patternpwd);
            eltSession.appendChild(patternopen);
            eltSession.appendChild(adopen);
            eltSession.appendChild(wxtime);
            eltSession.appendChild(wxfriendtime);
          //eltSession.appendChild(sswitchel);
          //eltSession.appendChild(IsHide);
            //获取根节点
            Element eltRoot = document.getDocumentElement();
            //把叶节点加入到根节点下
            eltRoot.appendChild(eltSession);
            //更新修改后的源文件
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
    
    
    /**
     * 增加一个UserEntity节点
     * @param UserEntity   
     * @return 是否增加成功，true为增加成功，false为增加失败
     */
	public static boolean addUserEntity(UserEntity session){

        try{
            //读取传入的路径，返回一个document对象
            Document document = loadInit();
            //创建叶节点
            Element eltSession = document.createElement("UserEntity");
            Element p1 = document.createElement("p1");//创建叶节点的第一个元素
            Element p2 = document.createElement("p2");//创建叶节点的第二个元素
            Element initial = document.createElement("initial");//创建叶节点的第三个元素     
            Element sswitchel = document.createElement("sswitch");
            Element IsHide = document.createElement("ishide");
            Text pwd1 = document.createTextNode(session.getPwd1());//创建叶节点的第一个元素下的文本节点
            p1.appendChild(pwd1);//把该文本节点加入到叶节点的第一个元素里面
            Text pwd2 = document.createTextNode(session.getPwd2());//创建叶节点的第二个元素下的文本节点
            p2.appendChild(pwd2);//把该文本节点加入到叶节点的第二个元素里面
            Text init = document.createTextNode(session.getinitial());//创建叶节点的第三个元素下的文本节点
            initial.appendChild(init);//把该文本节点加入到叶节点的第三个元素里面
            Text sswitch = document.createTextNode(session.getbackstageswitch());
            sswitchel.appendChild(sswitch);
            Text ishide = document.createTextNode(session.getIsHideLock());
            IsHide.appendChild(ishide);
            //把叶节点下的元素加入到叶节点下
            eltSession.appendChild(p1);
            eltSession.appendChild(p2);
            eltSession.appendChild(initial);
          eltSession.appendChild(sswitchel);
          eltSession.appendChild(IsHide);
            //获取根节点
            Element eltRoot = document.getDocumentElement();
            //把叶节点加入到根节点下
            eltRoot.appendChild(eltSession);
            //更新修改后的源文件
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
    /**
     * 增加一个folder节点
     * @param folder   
     * @return 是否增加成功，true为增加成功，false为增加失败
     */
	public static boolean addfolder(folders session){

        try{
            //读取传入的路径，返回一个document对象
            Document document = loadInit();
            //创建叶节点
            Element eltSession = document.createElement("folder");
            Element eltId = document.createElement("id");//创建叶节点的第一个元素
            Element eltStartTime = document.createElement("name");//创建叶节点的第二个元素
            Element eltEndTime = document.createElement("type");//创建叶节点的第三个元素           
            Text id = document.createTextNode(Long.toString(session.getId()));//创建叶节点的第一个元素下的文本节点
            eltId.appendChild(id);//把该文本节点加入到叶节点的第一个元素里面
            Text startTime = document.createTextNode(session.getName()+"");//创建叶节点的第二个元素下的文本节点
            eltStartTime.appendChild(startTime);//把该文本节点加入到叶节点的第二个元素里面
            Text endTime = document.createTextNode(session.getType()+"");//创建叶节点的第三个元素下的文本节点
            eltEndTime.appendChild(endTime);//把该文本节点加入到叶节点的第三个元素里面           
            //把叶节点下的元素加入到叶节点下
            eltSession.appendChild(eltId);
            eltSession.appendChild(eltStartTime);
            eltSession.appendChild(eltEndTime);
          
            //获取根节点
            Element eltRoot = document.getDocumentElement();
            //把叶节点加入到根节点下
            eltRoot.appendChild(eltSession);
            //更新修改后的源文件
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
	
	 /**
     * 删除一个folder节点
     *  @param sessionId   
     * @return 是否删除成功，true为删除成功，false为删除失败
     */
	public static boolean delSession(String id){

        Document document = loadInit();
        try{
            NodeList sessionList = document.getElementsByTagName("folder");
            for(int i=0; i<sessionList.getLength(); i++){
                String ID = document.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();
                //删除节点时传入的参数
                if(ID.equals(id)){
                    Node node = sessionList.item(i);
                    node.getParentNode().removeChild(node);
                    saveXML(document, filePath);
                    break;
                }
            }
            File file=new File(folderspath+id);
            RecursionDeleteFile(file);
            
            return true;
           
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	
		
	}
	
	
   
	
	 /**
     * 删除所有Session节点     * 
     * @return 是否删除成功，true为删除成功，false为删除失败
     */
	public static boolean delAllSessionList(){
		try {
			File file = new File(filePath);
			initXmlFile(file);
			return true;
		} catch (Exception e) {
			// TODO: handle exception
		}
		return false;
	
	}
	
	 /**
     * 删除一个Session节点
     *  @param session   
     * @return 是否删除成功，true为删除成功，false为删除失败
     */
	public static boolean delSession(Session session){
        Document document = loadInit();
        try{
            NodeList sessionList = document.getElementsByTagName("session");
            for(int i=0; i<sessionList.getLength(); i++){
                String id = document.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();
                //删除节点时传入的参数
                if(id.equals(session.getId())){
                    Node node = sessionList.item(i);
                    node.getParentNode().removeChild(node);
                    saveXML(document, filePath);
                    break;
                }
            }
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}	

	 /**
     * 更新一个folder节点
     *  @param folder   
     * @return 是否更新成功，true为更新成功，false为更新失败
     */
	public static boolean updatefolder(String foldername,String newname){
         //读取传入的路径，返回一个document对象
         Document document = loadInit();
         try{
            //获取叶节点
             NodeList sessionList = document.getElementsByTagName("folder");
            //遍历叶节点
             for(int i=0; i<sessionList.getLength(); i++){
            	 String name = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
                 
                 if(name.equals(foldername)){
                     document.getElementsByTagName("name").item(i).getFirstChild().setNodeValue(newname);
                    
                     break;
                 }
             }
             saveXML(document, filePath);
             return true;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return false;
         }
    
		
	}
	 /**
     * 更新一个Version2节点
     *  @param version   
     * @return 是否更新成功，true为更新成功，false为更新失败
     */
	public static boolean UpdateVersion3(Version3 version){
         //读取传入的路径，返回一个document对象
         Document document = loadInit();
         try{
            //获取叶节点
             NodeList sessionList = document.getElementsByTagName("version3");
            //遍历叶节点
             for(int i=0; i<sessionList.getLength(); i++){
            	
                if(version.getbgstr()!=null)
                {
                     document.getElementsByTagName("bgstr").item(i).getFirstChild().setNodeValue(version.getbgstr());
                     
                }
                if(version.getusebgstr()!=null)
                {
                     document.getElementsByTagName("usebgstr").item(i).getFirstChild().setNodeValue(version.getusebgstr());
                     
                }
             }
             saveXML(document, filePath);
             return true;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return false;
         }
    
		
	}
	 /**
     * 更新一个Version2节点
     *  @param version   
     * @return 是否更新成功，true为更新成功，false为更新失败
     */
	public static boolean UpdateVersion2(Version2 version){
         //读取传入的路径，返回一个document对象
         Document document = loadInit();
         try{
            //获取叶节点
             NodeList sessionList = document.getElementsByTagName("version2");
            //遍历叶节点
             for(int i=0; i<sessionList.getLength(); i++){
            	
                if(version.getPwdType()!=null)
                {
                     document.getElementsByTagName("pwdtype").item(i).getFirstChild().setNodeValue(version.getPwdType());
                }
                if(version.getintegral()!=null)
                {
                     document.getElementsByTagName("integral").item(i).getFirstChild().setNodeValue(Integer.toString(version.getintegral()));
                }
                if(version.getpatternpwd()!=null)
                {
                     document.getElementsByTagName("patternpwd").item(i).getFirstChild().setNodeValue(version.getpatternpwd());
                }
                if(version.GetAdOpen()!=null)
                {
                     document.getElementsByTagName("adopen").item(i).getFirstChild().setNodeValue(version.GetAdOpen());
                }
                if(version.GetPatternOpen()!=null)
                {
                     document.getElementsByTagName("patternopen").item(i).getFirstChild().setNodeValue(version.GetPatternOpen());
                }
                if(version.GetFriendtime()!=null)
                {
                     document.getElementsByTagName("wxfriendtime").item(i).getFirstChild().setNodeValue(version.GetFriendtime());
                }
                if(version.GetWxtime()!=null)
                {
                     document.getElementsByTagName("wxtime").item(i).getFirstChild().setNodeValue(version.GetWxtime());
                }
             }
             saveXML(document, filePath);
             return true;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return false;
         }
    
		
	}
	 /**
     * 更新一个user节点
     *  @param user   
     * @return 是否更新成功，true为更新成功，false为更新失败
     */
	public static boolean updateuser(UserEntity user){
         //读取传入的路径，返回一个document对象
         Document document = loadInit();
         try{
            //获取叶节点
             NodeList sessionList = document.getElementsByTagName("UserEntity");
            //遍历叶节点
             for(int i=0; i<sessionList.getLength(); i++){
            	
                if(user.getPwd1()!=null)
                {
                     document.getElementsByTagName("p1").item(i).getFirstChild().setNodeValue(user.getPwd1());
                }
                if(user.getPwd2()!=null)
                {
                     document.getElementsByTagName("p1").item(i).getFirstChild().setNodeValue(user.getPwd2());
                }
                if(user.getinitial()!=null)
                {
                     document.getElementsByTagName("initial").item(i).getFirstChild().setNodeValue(user.getinitial());
                }
                if(user.getbackstageswitch()!=null)
                {
                     document.getElementsByTagName("sswitch").item(i).getFirstChild().setNodeValue(user.getbackstageswitch());
                }
                if(user.getIsHideLock()!=null)
                {
                     document.getElementsByTagName("ishide").item(i).getFirstChild().setNodeValue(user.getIsHideLock());
                }
     
             }
             saveXML(document, filePath);
             return true;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return false;
         }
    
		
	}
	 /**
     * 查寻一个节点是否存在
     *  @param session   
     * @return ，true为存在，false不存在
     */
	public static boolean SelExist(String name){
         //读取传入的路径，返回一个document对象
		boolean IsExist=false;
         Document document = loadInit();
         try{
            //获取叶节点
             NodeList sessionList = document.getElementsByTagName("folder");
            //遍历叶节点
             for(int i=0; i<sessionList.getLength(); i++){
                 String xmlname = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
               
                 //修改节点时传入的参数
                 if(xmlname.equals(name)){
                	 IsExist=true;
                     break;
                 }
                
             }
            
             return IsExist;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return false;
         }
    
		
	}
	 /**
     * 查寻一个节点是否存在
     *  @param session   
     * @return ，true为存在，false不存在
     */
	public static boolean SelUserExist(String name){
         //读取传入的路径，返回一个document对象
		boolean IsExist=false;
         Document document = loadInit();
         try{
            //获取叶节点
             NodeList sessionList = document.getElementsByTagName(name);
            //遍历叶节点
            
                 if(sessionList.getLength()>0){
                	 IsExist=true;
                   
                 
                
             }
            
             return IsExist;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return false;
         }
    
		
	}
	
	 /**
     * 把修改后的document写进源文件（更新源文件）
     * @param document
     * @param filePath
     * @return
     */
    public static boolean saveXML(Document document, String filePath){
        try{
            TransformerFactory tFactory = TransformerFactory.newInstance();
            Transformer transformer = tFactory.newTransformer();
            
            DOMSource source = new DOMSource(document);
            StreamResult result = new StreamResult(new File(filePath));
            transformer.transform(source, result);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    }
    
    /**
     * 获取folder的所有记录     
     * @return
     */
    public static List<folders> loadSessionList(){
         List<folders> sessionList = new ArrayList<folders>();
         try{
             //读取传入的路径，返回一个document对象
             Document document = loadInit();
             //获取叶节点
             NodeList nodeList = document.getElementsByTagName("folder");
             //遍历叶节点
             for(int i=0; i<nodeList.getLength(); i++){
            	 folders folders = new folders();
                 String id = document.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();             
                 String startTime = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
                 String endTime = document.getElementsByTagName("type").item(i).getFirstChild().getNodeValue();  
                 folders.setId( Long.parseLong(id));
                 folders.setName(startTime);
                 folders.setType(endTime);
                 sessionList.add(folders);
             }
             return sessionList;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return null;
         }
    }
    /**
     * 获取UserEntity的所有记录     
     * @return
     */
    public static UserEntity loadUserEntity(){
         
         try{
        	 UserEntity user = new UserEntity();
             //读取传入的路径，返回一个document对象
             Document document = loadInit();
             //获取叶节点
             NodeList nodeList = document.getElementsByTagName("UserEntity");
             //遍历叶节点
             for(int i=0; i<nodeList.getLength(); i++){
            	
                 String pwd1 = document.getElementsByTagName("p1").item(i).getFirstChild().getNodeValue();             
                 String pwd2 = document.getElementsByTagName("p2").item(i).getFirstChild().getNodeValue();
                 String initial = document.getElementsByTagName("initial").item(i).getFirstChild().getNodeValue(); 
                 String sswitch = document.getElementsByTagName("sswitch").item(i).getFirstChild().getNodeValue();
                 String ishide = document.getElementsByTagName("ishide").item(i).getFirstChild().getNodeValue();
                 user.setinitial(initial);
                 user.setPwd1(pwd1);
                 user.setPwd2(pwd2);
               user.setbackstageswitch(sswitch);
               user.setIsHideLock(ishide);
             }
             return user;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return null;
         }
    }
	
    
    /**
     * 获取Version2的所有记录     
     * @return
     */
    public static Version2 loadVersion2(){
         
         try{
        	 Version2 version = new Version2();
             //读取传入的路径，返回一个document对象
             Document document = loadInit();
             //获取叶节点
             NodeList nodeList = document.getElementsByTagName("version2");
             //遍历叶节点
             for(int i=0; i<nodeList.getLength(); i++){
            	
             
                
                version.setintegral(Integer.parseInt(document.getElementsByTagName("integral").item(i).getFirstChild().getNodeValue()));
                 version.setPatternpwd(document.getElementsByTagName("patternpwd").item(i).getFirstChild().getNodeValue());
                 version.setPwdType(document.getElementsByTagName("pwdtype").item(i).getFirstChild().getNodeValue());
                 version.setPatternOpen(document.getElementsByTagName("patternopen").item(i).getFirstChild().getNodeValue());
                 version.setAdOpen(document.getElementsByTagName("adopen").item(i).getFirstChild().getNodeValue());
                 version.setFriendtime(document.getElementsByTagName("wxfriendtime").item(i).getFirstChild().getNodeValue());
                 version.setWxtime(document.getElementsByTagName("wxtime").item(i).getFirstChild().getNodeValue());
             }
             return version;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return null;
         }
    }
    /**
     * 获取Version3的所有记录     
     * @return
     */
    public static Version3 loadVersion3(){
         
         try{
        	 Version3 version = new Version3();
             //读取传入的路径，返回一个document对象
             Document document = loadInit();
             //获取叶节点
             NodeList nodeList = document.getElementsByTagName("version3");
             //遍历叶节点
             for(int i=0; i<nodeList.getLength(); i++){
            	
             
                
                version.setbgstr(document.getElementsByTagName("bgstr").item(i).getFirstChild().getNodeValue());
                version.setusebgstr(document.getElementsByTagName("usebgstr").item(i).getFirstChild().getNodeValue());
             }
             return version;
         }catch(Exception e){
             e.printStackTrace();
//             System.out.println(e.getMessage());
             return null;
         }
    }
	
}
