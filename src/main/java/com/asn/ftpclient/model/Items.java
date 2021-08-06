package com.asn.ftpclient.model;

public class Items {
	private String file;
	private String size;
	private int type;
	private String directory;

	public Items(String file, String size, int type, String directory) {
		super();
		this.file = file;
		this.size = size;
		this.type = type;
		this.directory = directory;
	}

	public String getDirectory() {
		return directory;
	}

	public void setDirectory(String directory) {
		this.directory = directory;
	}

	public String getFile() {
		return file;
	}

	public void setFile(String file) {
		this.file = file;
	}

	public String getSize() {
		return size;
	}

	public void setSize(String size) {
		this.size = size;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	@Override
	public String toString() {
		return "Items [file=" + file + ", size=" + size + ", type=" + type + ", directory=" + directory + "]";
	}

}
