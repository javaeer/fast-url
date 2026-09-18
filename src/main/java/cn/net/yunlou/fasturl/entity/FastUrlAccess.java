package cn.net.yunlou.fasturl.entity;

import cn.net.yunlou.common.beans.ObjectEntity;
import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import java.util.Date;
import lombok.Data;

@Data
@TableName("t_fast_url_access")
public class FastUrlAccess extends ObjectEntity {

    private static final long serialVersionUID = 1L;

    public static final String CACHE_KEY_PREFIX = "FAST_URL:";

    public static final String FAST_URL_ACCESS_ADD_QUEUE = "queue.add.access.url.fast";

    public static final String FAST_URL_ACCESS_ADD_EXCHANGE = "exchange.add.access.url.fast";
    public static final String FAST_URL_ACCESS_ADD_ROUTING_KEY = "key.routing.add.access.url.fast";

    /**
     * 短链接
     */
    @TableId(type = IdType.INPUT)
    private String id;

    /**
     * 访问次数
     */
    private Integer accessCount;


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

}
