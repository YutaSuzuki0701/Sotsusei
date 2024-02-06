package com.example.demo.bean;

public class PyObject {

	private String ai_determined_price;

	//getter setter
	public String getAi_determined_price() {
		return ai_determined_price;
	}

	public void setAi_determined_price(String ai_determined_price) {
		this.ai_determined_price = ai_determined_price;
	}

	//toString
	@Override
	public String toString() {
		return "PyObject [ai_determined_price=" + ai_determined_price + "]";
	}
}
