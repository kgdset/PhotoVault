package com.kgdset.photovault;



public class Session {
	
	private String id;
	private long startTime;
	private long endTime;
	
	public String getId() {
		return id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public long getStartTime() {
		return startTime;
	}
	public void setStartTime(long startTime) {
		this.startTime = startTime;
	}
	public long getEndTime() {
		return endTime;
	}
	public void setEndTime(long endTime) {
		this.endTime = endTime;
	}
	//@Override
	//public String toString() {
		//return "Session [id=" + id + ", startTime=" + DateUtil.getTimeSting(startTime) + ", endTime="
			//	+ DateUtil.getTimeSting(endTime) + "]";
	//}
	
	

}
