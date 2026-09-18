package cn.net.yunlou.fasturl.entity;

import cn.net.yunlou.common.beans.ObjectEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@TableName("t_fast_url")
@AllArgsConstructor
public class FastUrl extends ObjectEntity {

    private static final long serialVersionUID = 1L;

    public static final String CACHE_KEY_PREFIX = "FAST_URL:";

    @TableId(
            type = IdType.INPUT
    )
    private Long id;

    /**
     * 长链接
     */
    private String longUrl;

    /**
     * 短链接
     */
    private String shortUrl;

    /**
     * 域名
     */
    private String domain;

    /**
     * 创建时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date createTime;

    /**
     * 更新时间
     */
    @JsonFormat(
            pattern = "yyyy-MM-dd HH:mm:ss",
            timezone = "GMT+8"
    )
    private Date updateTime;

    /**
     * 删除标记
     */
    private Integer delFlag;


    public FastUrl(String longUrl) {
        this.longUrl = longUrl;
    }
}
