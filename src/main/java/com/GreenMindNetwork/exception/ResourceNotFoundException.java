package com.GreenMindNetwork.exception;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ResourceNotFoundException extends RuntimeException {

	private String resourceName;
	private String feildName;
	private long resourceNumber;
	private String resourceFeild;
	public ResourceNotFoundException(String resourceName, String feildName, long resourceNumber) {
		super(String.format("%s is not found at %s : %s", resourceName,feildName,resourceNumber));
		this.resourceName = resourceName;
		this.feildName = feildName;
		this.resourceNumber = resourceNumber;
	}
	public ResourceNotFoundException(String resourceName, String feildName, String resourceFeild) {
		super(String.format("%s is not found at %s : %s", resourceName,feildName,resourceFeild));
		this.resourceName = resourceName;
		this.feildName = feildName;
		this.resourceFeild = resourceFeild;
	}
	

	
}
