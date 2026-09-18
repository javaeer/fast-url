package cn.net.yunlou.fasturl.listener;

import cn.net.yunlou.fasturl.service.FastUrlAccessService;
import com.rabbitmq.client.Channel;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.annotation.Exchange;
import org.springframework.amqp.rabbit.annotation.Queue;
import org.springframework.amqp.rabbit.annotation.QueueBinding;
import org.springframework.amqp.rabbit.annotation.RabbitListener;
import org.springframework.stereotype.Component;
import static cn.net.yunlou.fasturl.entity.FastUrlAccess.FAST_URL_ACCESS_ADD_EXCHANGE;
import static cn.net.yunlou.fasturl.entity.FastUrlAccess.FAST_URL_ACCESS_ADD_QUEUE;
import static cn.net.yunlou.fasturl.entity.FastUrlAccess.FAST_URL_ACCESS_ADD_ROUTING_KEY;

@Slf4j
@Component
@RequiredArgsConstructor
public class FastUrlListener {

    private final FastUrlAccessService fastUrlAccessService;

    /**
     * 监听 短地址 访问 访问次数 +1
     *
     * @param shortUrl
     * @param message
     * @param channel
     */
    @RabbitListener(
            bindings = {
                    @QueueBinding(
                            value = @Queue(value = FAST_URL_ACCESS_ADD_QUEUE, durable = "true"),
                            exchange = @Exchange(value = FAST_URL_ACCESS_ADD_EXCHANGE),
                            key = {FAST_URL_ACCESS_ADD_ROUTING_KEY}
                    )
            }, ackMode = "MANUAL" // 手动ACK
    )
    public void fastUrlAccessAddAccount(String shortUrl, Message message, Channel channel) {

        long deliveryTag = message.getMessageProperties().getDeliveryTag();
        log.info("短地址访问消息抵达，消息序号:{}", deliveryTag);
        try {
            log.info("叠加短地址访问总数，短地址 :{}", shortUrl);
            fastUrlAccessService.addAccessCount(shortUrl);
            log.info("短地址访问叠加成功，短地址 :{}", shortUrl);
            log.info("消息应答");
            channel.basicAck(deliveryTag, false);

        } catch (Exception e) {
            log.error("消息消费失败，消息序号:{}", deliveryTag, e);
            try {
                // 拒绝消息
                channel.basicReject(deliveryTag, true);
            } catch (IOException e1) {
                log.error("消息消费失败，消息序号:{}", deliveryTag, e1);
            }
        }
    }

}
