package com.scwl.hzzxgd.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class SandAppMessageVo {
    private String touser;
//    private String toparty;
//    private String totag;
    private String msgtype;
    private int agentid;
    private TextContentVo textcard;
//    private int safe;
//    private int enable_id_trans;
//    private int enable_duplicate_check;
//    private int duplicate_check_interval;
}
