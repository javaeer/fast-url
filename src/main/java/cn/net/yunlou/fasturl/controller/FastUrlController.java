package cn.net.yunlou.fasturl.controller;

import cn.net.yunlou.common.redis.utils.RedisCacheUtils;
import cn.net.yunlou.common.utils.ObjectUtils;
import cn.net.yunlou.fasturl.ResponzeResult;
import cn.net.yunlou.fasturl.entity.FastUrl;
import cn.net.yunlou.fasturl.service.FastUrlService;
import java.io.IOException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.amqp.rabbit.core.RabbitTemplate;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import static cn.net.yunlou.fasturl.entity.FastUrlAccess.FAST_URL_ACCESS_ADD_EXCHANGE;
import static cn.net.yunlou.fasturl.entity.FastUrlAccess.FAST_URL_ACCESS_ADD_ROUTING_KEY;

/**
 * springboot 请求转发与重定向
 * 类的注解不能使用@RestController，要用@Controller；
 * 因为@RestController内含@ResponseBody，解析返回的是json串。不是跳转页面
 */
@RestController
@RequiredArgsConstructor
public class FastUrlController {

    private final RabbitTemplate rabbitTemplate;

    private final RedisCacheUtils redisCacheUtils;

    private final FastUrlService fastUrlService;


    /**
     * 生成短链
     *
     * @param longUrl
     * @return
     */
    @RequestMapping(value = "generate", method = {RequestMethod.GET, RequestMethod.POST})
    public ResponzeResult<FastUrl> generate(@RequestParam(value = "longUrl") String longUrl) {
        FastUrl fastUrl = new FastUrl(longUrl);
        FastUrl data = fastUrlService.saveAndGet(fastUrl);
        if (ObjectUtils.isNotEmpty(data)) {
            if (ObjectUtils.isEmpty(redisCacheUtils.getObject(FastUrl.CACHE_KEY_PREFIX + data.getShortUrl()))) {
                redisCacheUtils.putObject(FastUrl.CACHE_KEY_PREFIX + data.getShortUrl(), data.getLongUrl());
            }
        }
        return ResponzeResult.success(data);
    }


    /**
     * 通过短链直接重定向到原始链
     * springboot 请求转发与重定向
     * 方式一：使用 "forward:"与"redirect:" 关键字
     * 此方式下 类的注解不能使用@RestController，要用@Controller
     * 因为@RestController内含@ResponseBody，解析返回的是json串。不是跳转页面
     * 方式二：使用重定向方式 使用Servlet提供的API
     * 此方式下类的注解可以使用@Controller，也可以用@RestController
     *
     * @param shortUrl
     * @return
     */
    @GetMapping("{shortUrl}")
    public void redirect(@PathVariable(value = "shortUrl", required = false) String shortUrl, HttpServletRequest request, HttpServletResponse response) {
        String longUrl = redisCacheUtils.getObject(FastUrl.CACHE_KEY_PREFIX + shortUrl);
        try {
            if (ObjectUtils.isEmpty(longUrl)) {
                response.sendRedirect("https://smartcloudx.com/404.html");
            }
            //访问次数叠加
            rabbitTemplate.convertAndSend(FAST_URL_ACCESS_ADD_EXCHANGE, FAST_URL_ACCESS_ADD_ROUTING_KEY, shortUrl);
            response.sendRedirect(longUrl);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

}
