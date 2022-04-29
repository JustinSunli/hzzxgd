package com.scwl.hzzxgd.exception;

import com.scwl.hzzxgd.utils.SandResponse;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.NoHandlerFoundException;
import org.yaml.snakeyaml.constructor.DuplicateKeyException;

import javax.servlet.http.HttpServletResponse;

/**
 * 全局异常处理器
 *
 */
@RestControllerAdvice
public class SandExceptionHandler {
	private Logger logger = LoggerFactory.getLogger(getClass());

	/**
	 * 处理自定义异常
	 */
	@ExceptionHandler(SandException.class)
	public SandResponse handleSandException(SandException e){
		SandResponse r = new SandResponse();
		r.put("code", e.getCode());
		r.put("msg", e.getMessage());

		return r;
	}

	@ExceptionHandler(NoHandlerFoundException.class)
	public SandResponse handlerNoFoundException(Exception e) {
		logger.error(e.getMessage(), e);
		return SandResponse.error(404, "路径不存在，请检查路径是否正确");
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public SandResponse validExceptionHandler(HttpServletResponse response, MethodArgumentNotValidException ex) {
		BindingResult bindingResult = ex.getBindingResult();
		StringBuffer stringBuffer = new StringBuffer();
		if(bindingResult.hasErrors()){
			for (FieldError fieldError : bindingResult.getFieldErrors()) {
				//该格式仅仅作为response展示和log作用，前端应自己做校验
				stringBuffer.append(fieldError.getObjectName() + "--" + fieldError.getDefaultMessage() + " ");
			}
		}

		logger.error(stringBuffer.toString());
		return SandResponse.error(HttpStatus.BAD_REQUEST.value(), stringBuffer.toString());
	}

	/**
	 * 上传文件大小超出范围
	 * @param response
	 * @param ex
	 * @return
	 */
	@ExceptionHandler(MultipartException.class)
	public  SandResponse outOfLimits(HttpServletResponse response, MultipartException ex){
		logger.error(ex.getMessage(), ex);
		return SandResponse.error("上传文件大小超出限制范围.");
	}

	@ExceptionHandler(DuplicateKeyException.class)
	public SandResponse handleDuplicateKeyException(DuplicateKeyException e){
		logger.error(e.getMessage(), e);
		return SandResponse.error("数据库中已存在该记录");
	}

//	@ExceptionHandler(AuthorizationException.class)
//	public SandResponse handleAuthorizationException(AuthorizationException e){
//		logger.error(e.getMessage(), e);
//		return SandResponse.error("没有权限，请联系管理员授权");
//	}

	/**
	 * 未知异常，请联系管理员
	 * @param e
	 * @return
	 */
	@ExceptionHandler(Exception.class)
	public SandResponse handleException(Exception e){
		logger.error(e.getMessage(), e);
		return SandResponse.error();
	}
}
