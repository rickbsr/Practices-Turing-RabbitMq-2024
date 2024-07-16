package org.example.headers;

import com.rabbitmq.client.*;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.TimeoutException;

public class ProducerHeaders {

    public static final String EXCHANGE_NAME_HEADERS = "exchange_name_headers";
    public static final String QUEUE_NAME_HEADERS_ALL = "queue_name_headers_all";
    public static final String QUEUE_NAME_HEADERS_ANY = "queue_name_headers_any";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        try (
                Connection connection = factory.newConnection(); // 創建連接
                Channel channel = connection.createChannel() // 創建通道
        ) {

            // 創建交換機
            channel.exchangeDeclare(
                    EXCHANGE_NAME_HEADERS, // 交換機名稱
                    BuiltinExchangeType.HEADERS, // 交換機類型：direct, topic, fanout & headers（性能較差）
                    true, // 指定交換機是否需要持久化, 若設置為 true, 那麼交換機的元數據要持久化
                    false, // 指定交換機在沒有隊列綁定時, 是否需要刪除, 若設置 false , 那麼表示不需要刪除
                    null // Map<String, Object> 類型, 用來設置指定交換機的某些結構化信息, 此處不需要, 設置為 null
            );

            // 創建隊列
            channel.queueDeclare(QUEUE_NAME_HEADERS_ALL, true, false, false, null);
            channel.queueDeclare(QUEUE_NAME_HEADERS_ANY, true, false, false, null);

            Map<String, Object> headerMap = new HashMap<>();
            headerMap.put("system", "test-systemXX");
            headerMap.put("env", "dev");

            AMQP.BasicProperties.Builder builder = new AMQP.BasicProperties().builder().headers(headerMap);

            // 發送消息
            String message = "hello headers";

            channel.basicPublish(
                    EXCHANGE_NAME_HEADERS,
                    "",
                    builder.build(),
                    message.getBytes(StandardCharsets.UTF_8)
            );
        }
    }
}
