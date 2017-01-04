package com.example.exampleproject.pojo;

import java.io.Serializable;

/**
 * Created by chang on 2016/7/7.
 */
public class RecordEntity implements Serializable {
	private static final long serialVersionUID = 1738633342031768755L;
	private String name;
	private String number;
	private int type;
	private long lDate;
	private long duration;
	private int _new;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getNumber() {
		return number;
	}

	public void setNumber(String number) {
		this.number = number;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public long getlDate() {
		return lDate;
	}

	public void setlDate(long lDate) {
		this.lDate = lDate;
	}

	public long getDuration() {
		return duration;
	}

	public void setDuration(long duration) {
		this.duration = duration;
	}

	public int get_new() {
		return _new;
	}

	public void set_new(int _new) {
		this._new = _new;
	}

	@Override
	public String toString() {
		return "RecordEntity [toString()=" + name + "," + number + "," + type
				+ "," + lDate + "," + duration + "," + name + "," + "]";
	}

}
