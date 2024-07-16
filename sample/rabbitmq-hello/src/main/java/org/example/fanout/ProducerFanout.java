package org.example.fanout;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ProducerFanout {

    public static final String EXCHANGE_NAME_FANOUT = "exchange_name_fanout";
    public static final String QUEUE_NAME_FANOUT_1 = "queue_name_fanout_1";
    public static final String QUEUE_NAME_FANOUT_2 = "queue_name_fanout_2";
    public static final String QUEUE_NAME_FANOUT_3 = "queue_name_fanout_3";
    public static final String QUEUE_NAME_FANOUT_4 = "queue_name_fanout_4";
    public static final String QUEUE_KEY_1 = "queue_name_1";
    public static final String QUEUE_KEY_3 = "queue_name_3";
    public static final String QUEUE_KEY_4 = "queue_name_4";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        try (
                Connection connection = factory.newConnection(); // 創建連接
                Channel channel = connection.createChannel() // 創建通道
        ) {
            // 創建交換機
            channel.exchangeDeclare(
                    EXCHANGE_NAME_FANOUT, // 交換機名稱
                    BuiltinExchangeType.FANOUT, // 交換機類型：direct, topic, fanout & headers（性能較差）
                    true, // 指定交換機是否需要持久化, 若設置為 true, 那麼交換機的元數據要持久化
                    false, // 指定交換機在沒有隊列綁定時, 是否需要刪除, 若設置 false , 那麼表示不需要刪除
                    null // Map<String, Object> 類型, 用來設置指定交換機的某些結構化信息, 此處不需要, 設置為 null
            );

            // 創建隊列
            channel.queueDeclare(QUEUE_NAME_FANOUT_1, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_FANOUT_2, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_FANOUT_3, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_FANOUT_4, true, false, false, null);

            // 將交換機與隊列綁定
            channel.queueBind(QUEUE_NAME_FANOUT_1, EXCHANGE_NAME_FANOUT, QUEUE_KEY_1);
            channel.queueBind(QUEUE_NAME_FANOUT_2, EXCHANGE_NAME_FANOUT, QUEUE_KEY_1);
            channel.queueBind(QUEUE_NAME_FANOUT_3, EXCHANGE_NAME_FANOUT, QUEUE_KEY_3);
            channel.queueBind(QUEUE_NAME_FANOUT_4, EXCHANGE_NAME_FANOUT, QUEUE_KEY_4);

            // 發送消息
            String message = "hello fanout: ";
            String msgKey = "queue_name_1";

            channel.basicPublish(EXCHANGE_NAME_FANOUT, msgKey,
                    null, (message + "key is " + msgKey).getBytes(StandardCharsets.UTF_8));
        }
    }
}
