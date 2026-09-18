package cn.net.yunlou.fasturl.service;

import cn.net.yunlou.fasturl.entity.FastUrlAccess;
import cn.net.yunlou.fasturl.mapper.FastUrlAccessMapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FastUrlAccessServiceImpl extends ServiceImpl<FastUrlAccessMapper, FastUrlAccess> implements FastUrlAccessService {

    @Transactional(rollbackFor = RuntimeException.class)
    @Override
    public void addAccessCount(String id) {
        FastUrlAccess access = getById(id);
        if (access == null) {
            access = new FastUrlAccess();
            access.setId(id);
            access.setAccessCount(1);
            save(access);
        } else {
            access.setAccessCount(access.getAccessCount() + 1);
            updateById(access);
        }
    }
}
