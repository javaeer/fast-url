package cn.net.yunlou.fasturl.service;

import cn.net.yunlou.fasturl.entity.FastUrl;
import com.baomidou.mybatisplus.extension.service.IService;

public interface FastUrlService extends IService<FastUrl> {
    FastUrl saveAndGet(FastUrl fastUrl);
}
