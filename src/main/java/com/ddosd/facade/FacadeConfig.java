package com.ddosd.facade;

public class FacadeConfig {

	private Integer initialTrustScore;
	private Integer threshod;
	private Integer bufferSize;	

	public Integer getBufferSize() {
		return bufferSize;
	}

	public void setBufferSize(Integer bufferSize) {
		this.bufferSize = bufferSize;
	}

	public Integer getThreshod() {
		return threshod;
	}

	public void setThreshod(Integer threshod) {
		this.threshod = threshod;
	}

	public Integer getInitialTrustScore() {
		return initialTrustScore;
	}

	public void setInitialTrustScore(Integer initialTrustScore) {
		this.initialTrustScore = initialTrustScore;
	}
	
	
	
}
