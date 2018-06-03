package com.egzefer.diffr.model;

public class DiffDetails {

	private final String id;

	boolean equal;

	boolean sameSize;

	String message;

	public DiffDetails(String id, boolean equal, boolean sameSize) {
		this.id = id;
		this.equal = equal;
		this.sameSize = sameSize;
	}

	public String getId() {
		return id;
	}

	public boolean isEqual() {
		return equal;
	}

	public void setEqual(boolean equal) {
		this.equal = equal;
	}

	public boolean isSameSize() {
		return sameSize;
	}

	public void setSameSize(boolean sameSize) {
		this.sameSize = sameSize;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

}
