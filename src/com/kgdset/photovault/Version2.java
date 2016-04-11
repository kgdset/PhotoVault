package com.kgdset.photovault;

public class Version2 {
	 private String PwdType;
     private Integer integral;
     private String Patternpwd;
     private String PatternOpen;
     private String AdOpen;
     private String wxFriendtime;
     private String Wxtime;
     public String getPwdType() {
         return PwdType;
     }
     
     public void setPwdType(String pwdtype) {
         this.PwdType = pwdtype;
     }
     public Integer getintegral() {
             return integral;
     }
     public void setintegral(Integer integral) {
             this.integral = integral;
     }
     public String getpatternpwd()
     {
    	 return Patternpwd; 
     }
     public void  setPatternpwd(String patternpwd)
     {
    	 this.Patternpwd=patternpwd;
     }
     public String GetPatternOpen()
     {
    	 return PatternOpen;
     }
     public void  setPatternOpen(String patternopen)
     {
    	 this.PatternOpen=patternopen;
     }
     public String GetAdOpen()
     {
    	 return AdOpen;
     }
     public void  setAdOpen(String adopen)
     {
    	 this.AdOpen=adopen;
     }
     public String GetFriendtime()
     {
    	 return wxFriendtime;
     }
     public void  setFriendtime(String time)
     {
    	 this.wxFriendtime=time;
     }
     public String GetWxtime()
     {
    	 return Wxtime;
     }
     public void  setWxtime(String time)
     {
    	 this.Wxtime=time;
     }
}
