package com.example.exampleproject.pojo;

import java.io.Serializable;

/**
 * Created by chang on 2016/7/20.
 */
public class SmsInfo implements Serializable {
	private static final long serialVersionUID = 8706642702531822704L;
	private int id;
	private String sender;
	private String phone;
	private String content;
	private String time;
	private int type;

	public SmsInfo(int id, String sender, String phone, String content,
			String time, int type) {
		this.id = id;
		this.sender = sender;
		this.phone = phone;
		this.content = content;
		this.time = time;
		this.type = type;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getSender() {
		return sender;
	}

	public void setSender(String sender) {
		this.sender = sender;
	}

	public String getPhone() {
		return phone;
	}

	public void setPhone(String phone) {
		this.phone = phone;
	}

	public String getContent() {
		return content;
	}

	public void setContent(String content) {
		this.content = content;
	}

	public String getTime() {
		return time;
	}

	public void setTime(String time) {
		this.time = time;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}
}
