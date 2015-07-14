package com.test.dao;

public class NameValue{
	private String name;
	private Object value;
	private Object typeFlag;
	public NameValue(String name,Object value){
		this.name=name;
		this.value=value;
	}
	
	public NameValue(String name, Object value, Object typeFlag) {
		super();
		this.name = name;
		this.value = value;
		this.typeFlag = typeFlag;
	}

	public String getName() {
		return name;
	}
	public void setName(String name) {
		this.name = name;
	}
	public Object getValue() {
		return value;
	}
	public void setValue(Object value) {
		this.value = value;
	}

	public Object getTypeFlag() {
		return typeFlag;
	}

	public void setTypeFlag(Object typeFlag) {
		this.typeFlag = typeFlag;
	}

	@Override
	public String toString() {
		return "NameValue [name=" + name + ", value=" + value + ", typeFlag="
				+ typeFlag + "]";
	}
	

}
