package org.example.topic;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class ProducerTopic {

    public static final String EXCHANGE_NAME_TOPIC = "exchange_name_topic";
    public static final String QUEUE_NAME_TOPIC_1 = "queue_name_topic_1";
    public static final String QUEUE_NAME_TOPIC_2 = "queue_name_topic_2";
    public static final String QUEUE_NAME_TOPIC_3 = "queue_name_topic_3";
    public static final String QUEUE_NAME_TOPIC_4 = "queue_name_topic_4";
    public static final String QUEUE_KEY_1 = "p1.p2.p3.*";
    public static final String QUEUE_KEY_2 = "p1.#";
    public static final String QUEUE_KEY_3 = "*.p2.*.p4";
    public static final String QUEUE_KEY_4 = "#.p3.p4";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        try (
                Connection connection = factory.newConnection(); // 創建連接
                Channel channel = connection.createChannel() // 創建通道
        ) {
            // 創建交換機
            channel.exchangeDeclare(
                    EXCHANGE_NAME_TOPIC, // 交換機名稱
                    BuiltinExchangeType.TOPIC, // 交換機類型：direct, topic, fanout & headers（性能較差）
                    true, // 指定交換機是否需要持久化, 若設置為 true, 那麼交換機的元數據要持久化
                    false, // 指定交換機在沒有隊列綁定時, 是否需要刪除, 若設置 false , 那麼表示不需要刪除
                    null // Map<String, Object> 類型, 用來設置指定交換機的某些結構化信息, 此處不需要, 設置為 null
            );

            // 創建隊列
            channel.queueDeclare(QUEUE_NAME_TOPIC_1, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_TOPIC_2, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_TOPIC_3, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_TOPIC_4, true, false, false, null);

            // 將交換機與隊列綁定
            channel.queueBind(QUEUE_NAME_TOPIC_1, EXCHANGE_NAME_TOPIC, QUEUE_KEY_1);
            channel.queueBind(QUEUE_NAME_TOPIC_2, EXCHANGE_NAME_TOPIC, QUEUE_KEY_2);
            channel.queueBind(QUEUE_NAME_TOPIC_3, EXCHANGE_NAME_TOPIC, QUEUE_KEY_3);
            channel.queueBind(QUEUE_NAME_TOPIC_4, EXCHANGE_NAME_TOPIC, QUEUE_KEY_4);

            // 發送消息
            String message = "hello topic: ";
            String msgKey = "p1.p2.p3.p4";

            channel.basicPublish(EXCHANGE_NAME_TOPIC, msgKey,
                    null, (message + "key is " + msgKey).getBytes(StandardCharsets.UTF_8));
        }
    }
}
