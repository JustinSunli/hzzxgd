package com.scwl.hzzxgd.exception;

import lombok.Data;

/**
 * 自定义异常
 * 
 * @author chenshun
 * @email sunlightcs@gmail.com
 * @date 2016年10月27日 下午10:11:27
 */

@Data
public class SandException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
    private String msg;
    private int code = 500;
    
    public SandException(String msg) {
		super(msg);
		this.msg = msg;
	}
	
	public SandException(String msg, Throwable e) {
		super(msg, e);
		this.msg = msg;
	}
	
	public SandException(String msg, int code) {
		super(msg);
		this.msg = msg;
		this.code = code;
	}
	
	public SandException(String msg, int code, Throwable e) {
		super(msg, e);
		this.msg = msg;
		this.code = code;
	}
}
