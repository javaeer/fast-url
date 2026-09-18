package cn.net.yunlou.fasturl.service;

import cn.net.yunlou.fasturl.entity.FastUrlAccess;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FastUrlAccessService extends IService<FastUrlAccess> {
    void addAccessCount(String id);
}
