package com.kgdset.photovault;


public class GridPhotoEntity {
	
private int count;
private String DirPath;
private String ImageThumbnailPath;
private String DirName;
private String ThumName;
public GridPhotoEntity() {  
    super();  
    // TODO Auto-generated constructor stub  
}  
public GridPhotoEntity(int count, String thumbnailpath,String DirPath,String DirName,String name) {  
    super();  
    this.count = count;  
    this.DirPath = DirPath; 
    this.ImageThumbnailPath = thumbnailpath; 
    this.DirName = DirName; 
    this.ThumName=name;
}  
public int getcount() {
    return count;
}
public void setcount(int count) {
    this.count = count;
}
public String getdirpath() {
    return DirPath;
}
public void setdirpath(String path) {
    this.DirPath = path;
}
public String getdirname() {
    return DirName;
}
public void setdirname(String DirName) {
    this.DirName = DirName;
}
public String getImageThumbnailPath() {
    return ImageThumbnailPath;
}
public String getImageThumName() {
    return ThumName;
}
public void setImageThumbnailPath(String thumbnailpath) {
    this.ImageThumbnailPath = thumbnailpath;
}
public void setImageThumName(String Name) {
    this.ThumName = Name;
}
}
