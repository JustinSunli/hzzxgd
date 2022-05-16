package com.scwl.hzzxgd.vo;

import lombok.Data;
import lombok.experimental.Accessors;

@Data
@Accessors(chain = true)
public class TextContentVo {
    private String title;
    private String description;
    private String url;
    private String btntxt;
}
