/*
 *
 *     Copyright (c) 2019 - forever, javaeer All rights reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 * Redistributions of source code must retain the above copyright notice,
 * this list of conditions and the following disclaimer.
 * Redistributions in binary form must reproduce the above copyright
 * notice, this list of conditions and the following disclaimer in the
 * documentation and/or other materials provided with the distribution.
 * Neither the name of the haoheio.com developer nor the names of its
 * contributors may be used to endorse or promote products derived from
 * this software without specific prior written permission.
 *
 * Author: javaeer (javaeer@aliyun.com)
 *
 */

package cn.net.yunlou.fasturl;

import com.fasterxml.jackson.annotation.JsonProperty;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import java.io.Serializable;
import lombok.Data;

/**
 * @author laughtiger
 */
@Data
@ApiModel(value = "接口统一返回实体模型")
public class ResponzeResult<T extends Object> implements Serializable {

    public static final String DATA_TYPE_XML = "xml";
    public static final String DATA_TYPE_JSON = "json";
    public static final String DATA_TYPE_HTML = "html";


    @ApiModelProperty(value = "返回状态码", required = true)
    private Integer code;

    @ApiModelProperty(value = "返回消息体", required = true)
    private String message;

    @ApiModelProperty(value = "返回数据类型", required = true)
    @JsonProperty(value = "data_type")
    private String dataType;

    @ApiModelProperty(value = "返回数据", required = true)
    private T data;

    @ApiModelProperty(value = "返回扩展信息", required = true)
    private String extra;//全局扩展字段

    public ResponzeResult() {
        this.dataType = DATA_TYPE_JSON;
    }


    public ResponzeResult(T data) {
        ResponzeResult.success(data);
    }

    public ResponzeResult(Integer code, String message) {
        this.code = code;
        this.message = message;
        this.dataType = DATA_TYPE_JSON;
    }


    public static ResponzeResult success() {
        return ResponzeResult.success(null);
    }

    public static ResponzeResult success(Object data) {
        return ResponzeResult.success(data, null);
    }

    public static ResponzeResult success(Object data, String extra) {
        return success(data, extra, true);
    }

    public static ResponzeResult success(Object data, String extra, boolean dataFormat) {
        return success(data, extra, dataFormat, DATA_TYPE_JSON);
    }

    public static ResponzeResult success(Object data, String extra, boolean dataFormat, String dataType) {
        ResponzeResult restResult = new ResponzeResult();
        restResult.setData(data);
        restResult.setDataType(dataType);
        restResult.setRestResult(ResponzeStatus.SUCCESS);
        restResult.setExtra(extra);
        return restResult;
    }


    public static ResponzeResult undefined() {
        ResponzeResult restResult = new ResponzeResult();
        restResult.setRestResult(ResponzeStatus.INTERNAL_SERVER_UNDEFINED_ERROR);
        restResult.setDataType(DATA_TYPE_JSON);
        return restResult;
    }

    public static ResponzeResult failed(Integer code, String message) {
        return new ResponzeResult(code, message);
    }

    public static ResponzeResult failed(ResponzeStatus status,String message) {
        return failed(status.getValue(), message);
    }

    public static ResponzeResult failed(ResponzeStatus status) {
        return failed(status, status.getLabel());
    }


    private void setRestResult(ResponzeStatus restStatus) {
        this.code = restStatus.getValue();
        this.message = restStatus.getLabel();
    }
}
