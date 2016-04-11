package com.kgdset.photovault;

public class UserEntity {
	 private String pwd1;
     private String pwd2;
     private String initial;
     private String backstageswitch;
     private String IsHideLock;
     public String getIsHideLock() {
         return IsHideLock;
 }
 public void setIsHideLock(String ishide) {
         this.IsHideLock = ishide;
 }
     public String getPwd1() {
             return pwd1;
     }
     public void setPwd1(String pwd) {
             this.pwd1 = pwd;
     }
     public String getPwd2() {
             return pwd2;
     }
     public void setbackstageswitch(String sswitch) {
         this.backstageswitch = sswitch;
 }
     public String getbackstageswitch()
     {
    	 return backstageswitch;
     }
     public void setPwd2(String pwd) {
             this.pwd2 = pwd;
     }
     public String getinitial() {
    	 return initial;
 }
     public void setinitial(String initial) {
         this.initial = initial;
 }
}
