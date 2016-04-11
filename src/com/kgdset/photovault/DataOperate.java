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
     * ͨ���ļ��� ��ȡͼƬ������ͼ 
     *  
     * @param context 
     * @param cr     cr = getContentResolver(); 
     * @param testVideopath  ȫ·�� "/mnt/sdcard/sidamingbu.mp4"; 
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
     * ��ʼ��xml�ļ���û���򴴽��ļ�����д����ڵ�����
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
			 * �ڶ��δ�汾���´���
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
		
		

	
		Bitmap bitmap = bitmap1;// ��ȡ������ص����ݣ���ת��ΪBitmapͼƬ��ʽ
		FileOutputStream b = null;
		String PhotoName=Long.toString(System.currentTimeMillis());
		String fileName = folderspath+foldername+"/"+PhotoName+".kgdset";
		
		try {
			b = new FileOutputStream(fileName);
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
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
			bitmap.compress(Bitmap.CompressFormat.JPEG, 100, b);// ������д���ļ�
			//bitmap1.recycle();
			//bitmap.recycle();
			
		} catch (FileNotFoundException e) {
			e.printStackTrace();
			
		} 
    	}
    }

  
   
    /**
     * ***��ȡSD��·��
     * **
     * ***/
    public static String getSDPath(){
    	  String sdDir =null;
    	  boolean sdCardExist = Environment.getExternalStorageState()
    	  .equals(android.os.Environment.MEDIA_MOUNTED); //�ж�sd���Ƿ����
    	  if (sdCardExist)
    	  {
    	  sdDir = Environment.getExternalStorageDirectory().toString();//��ȡ��Ŀ¼
    	  }
    	  return sdDir;
    	   
    	 }
	/***
	 * *ɾ��Ŀ¼��Ŀ¼�µ������ļ�
	 * **/
	/**
     * �ݹ�ɾ���ļ����ļ���
     * @param file    Ҫɾ���ĸ�Ŀ¼
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
	if(!SelExist("�½�Ŀ¼"))
	{
		folders folders = new folders();
		folders.setId(0);
		folders.setName("�½�Ŀ¼");
		folders.setType("0");
		addfolder(folders);
	}
	if(!SelExist("�����Ƭ"))
	{
		folders folders = new folders();
		folders.setId(1);
		folders.setName("�����Ƭ");
		folders.setType("0");
		addfolder(folders);
		CreateFolders("�����Ƭ");
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
     * ��ʼ��xml�ļ�
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
     * ��ȡ�����·��������һ��document����      
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
     * ����һ��Version3����Ҫ�Ľڵ�
     * @param Version3   
     * @return �Ƿ����ӳɹ���trueΪ���ӳɹ���falseΪ����ʧ��
     */
	public static boolean addUpdateVersion3(Version3 session){

        try{
            //��ȡ�����·��������һ��document����
            Document document = loadInit();
            //����Ҷ�ڵ�
            Element eltSession = document.createElement("version3");
            Element bgstr = document.createElement("bgstr");//����Ҷ�ڵ�ĵ�һ��Ԫ��
            Element usebgstr = document.createElement("usebgstr");
           // Element sswitchel = document.createElement("sswitch");
            //Element IsHide = document.createElement("ishide");
            Text bgstrtxt = document.createTextNode(session.getbgstr());//����Ҷ�ڵ�ĵ�һ��Ԫ���µ��ı��ڵ�
            Text usebgstrtxt = document.createTextNode(session.getusebgstr());
            bgstr.appendChild(bgstrtxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�һ��Ԫ������
            usebgstr.appendChild(usebgstrtxt);
         
            eltSession.appendChild(usebgstr);
            eltSession.appendChild(bgstr);
            //��ȡ���ڵ�
            Element eltRoot = document.getDocumentElement();
            //��Ҷ�ڵ���뵽���ڵ���
            eltRoot.appendChild(eltSession);
            //�����޸ĺ��Դ�ļ�
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
    /**
     * ����һ��Version2����Ҫ�Ľڵ�
     * @param Version2   
     * @return �Ƿ����ӳɹ���trueΪ���ӳɹ���falseΪ����ʧ��
     */
	public static boolean addUpdateVersion2(Version2 session){

        try{
            //��ȡ�����·��������һ��document����
            Document document = loadInit();
            //����Ҷ�ڵ�
            Element eltSession = document.createElement("version2");
            Element pwdtype = document.createElement("pwdtype");//����Ҷ�ڵ�ĵ�һ��Ԫ��
            Element integral = document.createElement("integral");//����Ҷ�ڵ�ĵڶ���Ԫ��
            Element patternpwd = document.createElement("patternpwd");//����Ҷ�ڵ�ĵ�����Ԫ��     
            Element patternopen = document.createElement("patternopen");//����Ҷ�ڵ�ĵ�����Ԫ��     
            Element adopen = document.createElement("adopen");//����Ҷ�ڵ�ĵ�����Ԫ��     
            Element wxtime = document.createElement("wxtime");//����Ҷ�ڵ�ĵ�����Ԫ��    
            Element wxfriendtime = document.createElement("wxfriendtime");//����Ҷ�ڵ�ĵ�����Ԫ��     
           // Element sswitchel = document.createElement("sswitch");
            //Element IsHide = document.createElement("ishide");
            Text pwdtypetxt = document.createTextNode(session.getPwdType());//����Ҷ�ڵ�ĵ�һ��Ԫ���µ��ı��ڵ�
            pwdtype.appendChild(pwdtypetxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�һ��Ԫ������
            Text integraltxt = document.createTextNode(Integer.toString(session.getintegral()));//����Ҷ�ڵ�ĵڶ���Ԫ���µ��ı��ڵ�
            integral.appendChild(integraltxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵڶ���Ԫ������
            Text patternpwdtxt = document.createTextNode(session.getpatternpwd());//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            patternpwd.appendChild(patternpwdtxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������
            Text patternopentxt = document.createTextNode(session.GetPatternOpen());//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            patternopen.appendChild(patternopentxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������
            Text adopentxt = document.createTextNode(session.GetAdOpen());//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            adopen.appendChild(adopentxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������
            Text wxtimetxt = document.createTextNode(session.GetWxtime());//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            wxtime.appendChild(wxtimetxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������
            Text wxfriendtimetxt = document.createTextNode(session.GetFriendtime());//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            wxfriendtime.appendChild(wxfriendtimetxt);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������
            //Text sswitch = document.createTextNode(session.getbackstageswitch());
           // sswitchel.appendChild(sswitch);
           // Text ishide = document.createTextNode(session.getIsHideLock());
           // IsHide.appendChild(ishide);
            //��Ҷ�ڵ��µ�Ԫ�ؼ��뵽Ҷ�ڵ���
            eltSession.appendChild(pwdtype);
            eltSession.appendChild(integral);
            eltSession.appendChild(patternpwd);
            eltSession.appendChild(patternopen);
            eltSession.appendChild(adopen);
            eltSession.appendChild(wxtime);
            eltSession.appendChild(wxfriendtime);
          //eltSession.appendChild(sswitchel);
          //eltSession.appendChild(IsHide);
            //��ȡ���ڵ�
            Element eltRoot = document.getDocumentElement();
            //��Ҷ�ڵ���뵽���ڵ���
            eltRoot.appendChild(eltSession);
            //�����޸ĺ��Դ�ļ�
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
    
    
    /**
     * ����һ��UserEntity�ڵ�
     * @param UserEntity   
     * @return �Ƿ����ӳɹ���trueΪ���ӳɹ���falseΪ����ʧ��
     */
	public static boolean addUserEntity(UserEntity session){

        try{
            //��ȡ�����·��������һ��document����
            Document document = loadInit();
            //����Ҷ�ڵ�
            Element eltSession = document.createElement("UserEntity");
            Element p1 = document.createElement("p1");//����Ҷ�ڵ�ĵ�һ��Ԫ��
            Element p2 = document.createElement("p2");//����Ҷ�ڵ�ĵڶ���Ԫ��
            Element initial = document.createElement("initial");//����Ҷ�ڵ�ĵ�����Ԫ��     
            Element sswitchel = document.createElement("sswitch");
            Element IsHide = document.createElement("ishide");
            Text pwd1 = document.createTextNode(session.getPwd1());//����Ҷ�ڵ�ĵ�һ��Ԫ���µ��ı��ڵ�
            p1.appendChild(pwd1);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�һ��Ԫ������
            Text pwd2 = document.createTextNode(session.getPwd2());//����Ҷ�ڵ�ĵڶ���Ԫ���µ��ı��ڵ�
            p2.appendChild(pwd2);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵڶ���Ԫ������
            Text init = document.createTextNode(session.getinitial());//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            initial.appendChild(init);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������
            Text sswitch = document.createTextNode(session.getbackstageswitch());
            sswitchel.appendChild(sswitch);
            Text ishide = document.createTextNode(session.getIsHideLock());
            IsHide.appendChild(ishide);
            //��Ҷ�ڵ��µ�Ԫ�ؼ��뵽Ҷ�ڵ���
            eltSession.appendChild(p1);
            eltSession.appendChild(p2);
            eltSession.appendChild(initial);
          eltSession.appendChild(sswitchel);
          eltSession.appendChild(IsHide);
            //��ȡ���ڵ�
            Element eltRoot = document.getDocumentElement();
            //��Ҷ�ڵ���뵽���ڵ���
            eltRoot.appendChild(eltSession);
            //�����޸ĺ��Դ�ļ�
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
    /**
     * ����һ��folder�ڵ�
     * @param folder   
     * @return �Ƿ����ӳɹ���trueΪ���ӳɹ���falseΪ����ʧ��
     */
	public static boolean addfolder(folders session){

        try{
            //��ȡ�����·��������һ��document����
            Document document = loadInit();
            //����Ҷ�ڵ�
            Element eltSession = document.createElement("folder");
            Element eltId = document.createElement("id");//����Ҷ�ڵ�ĵ�һ��Ԫ��
            Element eltStartTime = document.createElement("name");//����Ҷ�ڵ�ĵڶ���Ԫ��
            Element eltEndTime = document.createElement("type");//����Ҷ�ڵ�ĵ�����Ԫ��           
            Text id = document.createTextNode(Long.toString(session.getId()));//����Ҷ�ڵ�ĵ�һ��Ԫ���µ��ı��ڵ�
            eltId.appendChild(id);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�һ��Ԫ������
            Text startTime = document.createTextNode(session.getName()+"");//����Ҷ�ڵ�ĵڶ���Ԫ���µ��ı��ڵ�
            eltStartTime.appendChild(startTime);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵڶ���Ԫ������
            Text endTime = document.createTextNode(session.getType()+"");//����Ҷ�ڵ�ĵ�����Ԫ���µ��ı��ڵ�
            eltEndTime.appendChild(endTime);//�Ѹ��ı��ڵ���뵽Ҷ�ڵ�ĵ�����Ԫ������           
            //��Ҷ�ڵ��µ�Ԫ�ؼ��뵽Ҷ�ڵ���
            eltSession.appendChild(eltId);
            eltSession.appendChild(eltStartTime);
            eltSession.appendChild(eltEndTime);
          
            //��ȡ���ڵ�
            Element eltRoot = document.getDocumentElement();
            //��Ҷ�ڵ���뵽���ڵ���
            eltRoot.appendChild(eltSession);
            //�����޸ĺ��Դ�ļ�
            saveXML(document, filePath);
            return true;
        }catch(Exception e){
            e.printStackTrace();
//            System.out.println(e.getMessage());
            return false;
        }
    
		
	}
	
	 /**
     * ɾ��һ��folder�ڵ�
     *  @param sessionId   
     * @return �Ƿ�ɾ���ɹ���trueΪɾ���ɹ���falseΪɾ��ʧ��
     */
	public static boolean delSession(String id){

        Document document = loadInit();
        try{
            NodeList sessionList = document.getElementsByTagName("folder");
            for(int i=0; i<sessionList.getLength(); i++){
                String ID = document.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();
                //ɾ���ڵ�ʱ����Ĳ���
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
     * ɾ������Session�ڵ�     * 
     * @return �Ƿ�ɾ���ɹ���trueΪɾ���ɹ���falseΪɾ��ʧ��
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
     * ɾ��һ��Session�ڵ�
     *  @param session   
     * @return �Ƿ�ɾ���ɹ���trueΪɾ���ɹ���falseΪɾ��ʧ��
     */
	public static boolean delSession(Session session){
        Document document = loadInit();
        try{
            NodeList sessionList = document.getElementsByTagName("session");
            for(int i=0; i<sessionList.getLength(); i++){
                String id = document.getElementsByTagName("id").item(i).getFirstChild().getNodeValue();
                //ɾ���ڵ�ʱ����Ĳ���
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
     * ����һ��folder�ڵ�
     *  @param folder   
     * @return �Ƿ���³ɹ���trueΪ���³ɹ���falseΪ����ʧ��
     */
	public static boolean updatefolder(String foldername,String newname){
         //��ȡ�����·��������һ��document����
         Document document = loadInit();
         try{
            //��ȡҶ�ڵ�
             NodeList sessionList = document.getElementsByTagName("folder");
            //����Ҷ�ڵ�
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
     * ����һ��Version2�ڵ�
     *  @param version   
     * @return �Ƿ���³ɹ���trueΪ���³ɹ���falseΪ����ʧ��
     */
	public static boolean UpdateVersion3(Version3 version){
         //��ȡ�����·��������һ��document����
         Document document = loadInit();
         try{
            //��ȡҶ�ڵ�
             NodeList sessionList = document.getElementsByTagName("version3");
            //����Ҷ�ڵ�
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
     * ����һ��Version2�ڵ�
     *  @param version   
     * @return �Ƿ���³ɹ���trueΪ���³ɹ���falseΪ����ʧ��
     */
	public static boolean UpdateVersion2(Version2 version){
         //��ȡ�����·��������һ��document����
         Document document = loadInit();
         try{
            //��ȡҶ�ڵ�
             NodeList sessionList = document.getElementsByTagName("version2");
            //����Ҷ�ڵ�
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
     * ����һ��user�ڵ�
     *  @param user   
     * @return �Ƿ���³ɹ���trueΪ���³ɹ���falseΪ����ʧ��
     */
	public static boolean updateuser(UserEntity user){
         //��ȡ�����·��������һ��document����
         Document document = loadInit();
         try{
            //��ȡҶ�ڵ�
             NodeList sessionList = document.getElementsByTagName("UserEntity");
            //����Ҷ�ڵ�
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
     * ��Ѱһ���ڵ��Ƿ����
     *  @param session   
     * @return ��trueΪ���ڣ�false������
     */
	public static boolean SelExist(String name){
         //��ȡ�����·��������һ��document����
		boolean IsExist=false;
         Document document = loadInit();
         try{
            //��ȡҶ�ڵ�
             NodeList sessionList = document.getElementsByTagName("folder");
            //����Ҷ�ڵ�
             for(int i=0; i<sessionList.getLength(); i++){
                 String xmlname = document.getElementsByTagName("name").item(i).getFirstChild().getNodeValue();
               
                 //�޸Ľڵ�ʱ����Ĳ���
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
     * ��Ѱһ���ڵ��Ƿ����
     *  @param session   
     * @return ��trueΪ���ڣ�false������
     */
	public static boolean SelUserExist(String name){
         //��ȡ�����·��������һ��document����
		boolean IsExist=false;
         Document document = loadInit();
         try{
            //��ȡҶ�ڵ�
             NodeList sessionList = document.getElementsByTagName(name);
            //����Ҷ�ڵ�
            
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
     * ���޸ĺ��documentд��Դ�ļ�������Դ�ļ���
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
     * ��ȡfolder�����м�¼     
     * @return
     */
    public static List<folders> loadSessionList(){
         List<folders> sessionList = new ArrayList<folders>();
         try{
             //��ȡ�����·��������һ��document����
             Document document = loadInit();
             //��ȡҶ�ڵ�
             NodeList nodeList = document.getElementsByTagName("folder");
             //����Ҷ�ڵ�
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
     * ��ȡUserEntity�����м�¼     
     * @return
     */
    public static UserEntity loadUserEntity(){
         
         try{
        	 UserEntity user = new UserEntity();
             //��ȡ�����·��������һ��document����
             Document document = loadInit();
             //��ȡҶ�ڵ�
             NodeList nodeList = document.getElementsByTagName("UserEntity");
             //����Ҷ�ڵ�
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
     * ��ȡVersion2�����м�¼     
     * @return
     */
    public static Version2 loadVersion2(){
         
         try{
        	 Version2 version = new Version2();
             //��ȡ�����·��������һ��document����
             Document document = loadInit();
             //��ȡҶ�ڵ�
             NodeList nodeList = document.getElementsByTagName("version2");
             //����Ҷ�ڵ�
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
     * ��ȡVersion3�����м�¼     
     * @return
     */
    public static Version3 loadVersion3(){
         
         try{
        	 Version3 version = new Version3();
             //��ȡ�����·��������һ��document����
             Document document = loadInit();
             //��ȡҶ�ڵ�
             NodeList nodeList = document.getElementsByTagName("version3");
             //����Ҷ�ڵ�
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
