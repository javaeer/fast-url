package cn.net.yunlou.fasturl.service;

import cn.net.yunlou.fasturl.entity.FastUrl;
import cn.net.yunlou.fasturl.mapper.FastUrlMapper;
import cn.net.yunlou.fasturl.utils.EncoderUtils;
import cn.net.yunlou.fasturl.utils.Sequence;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import java.util.Optional;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class FastUrlServiceImpl extends ServiceImpl<FastUrlMapper, FastUrl> implements FastUrlService {
    @Override
    @Transactional(rollbackFor = RuntimeException.class)
    public FastUrl saveAndGet(FastUrl fastUrl) {
        FastUrl url = getByLongUrl(fastUrl.getLongUrl());

        if (url != null){
            return url;
        }
        fastUrl.setDomain("https://www.yunlou.net.cn");
        Sequence sequence = new Sequence();
        long id = sequence.nextId();
        fastUrl.setId(id);
        fastUrl.setShortUrl(EncoderUtils.encodeBase62(id));
        if (this.save(fastUrl)) {
            return fastUrl;
        }
        return null;
    }

    private FastUrl getByLongUrl(String longUrl) {
        Optional<FastUrl> fastUrl = this.lambdaQuery().eq(FastUrl::getLongUrl, longUrl).oneOpt();
        if (fastUrl.isPresent()) {
            return fastUrl.get();
        }
        return null;
    }
}
