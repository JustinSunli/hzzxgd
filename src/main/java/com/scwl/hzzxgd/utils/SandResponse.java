package com.scwl.hzzxgd.utils;
import java.util.HashMap;
import java.util.Map;

/**
 * 返回数据
 *
 */
//@ApiModel(value = "返回数据")  FIXME WHAT?
public class SandResponse extends HashMap<String, Object> {
    public static final int  SAND_HTTP_STATUS_OSS_HAVE_TOKEN = 4012 ;  // 有获得OSS TOKEN
    public static final int  SAND_HTTP_STATUS_OSS_UNAUTHORIZED = 4011 ;  // 没获得TOKEN
    private static final long serialVersionUID = 1L;

    public SandResponse() {
        put("code", 0);
        put("msg", "success");
    }

    public static SandResponse error() {
//		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, "未知异常，请联系管理员");
        return error(500, "未知异常，请联系管理员");
    }

    public static SandResponse error(String msg) {
//		return error(HttpStatus.SC_INTERNAL_SERVER_ERROR, msg);
        return error(500, msg);
    }

    public static SandResponse error(int code, String msg) {
        SandResponse r = new SandResponse();
        r.put("code", code);
        r.put("msg", msg);
        return r;
    }

    public static SandResponse ok(String msg) {
        SandResponse r = new SandResponse();
        r.put("msg", msg);
        return r;
    }

    public static SandResponse ok(Map<String, Object> map) {
        SandResponse r = new SandResponse();
        r.putAll(map);
        return r;
    }

    public static SandResponse ok() {
        return new SandResponse();
    }

    public SandResponse put(String key, Object value) {
        super.put(key, value);
        return this;
    }

    public String getValString(SandResponse sandResponse,String key){
        String value =  String.valueOf(sandResponse.get(key));
        return value;
    }

}
