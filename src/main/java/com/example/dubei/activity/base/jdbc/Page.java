package com.example.dubei.activity.base.jdbc;

import java.util.List;

public class Page {
	private int start;
	
	private int rows;
	
	private int page;
	
	private int totalCount;
	
	private List result;
	
	public Page(int rows, int page, int totalCount, List result){
		this.rows = rows;
		this.page = page;
		this.totalCount = totalCount;
		this.result = result;
	}
	
	public int getTotalCount() {
		return totalCount;
	}

	public void setTotalCount(int totalCount) {
		this.totalCount = totalCount;
	}

	public List getResult() {
		return result;
	}

	public void setResult(List result) {
		this.result = result;
	}

	public int getStart() {
		return start;
	}
	public void setStart(int start) {
		this.start = start;
	}
	public int getRows() {
		return rows;
	}
	public void setRows(int rows) {
		this.rows = rows;
	}
	public int getPage() {
		return page;
	}
	public void setPage(int page) {
		this.page = page;
	}
	
}
