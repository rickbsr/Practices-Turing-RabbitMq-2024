package org.example.headers;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeoutException;

import static org.example.direct.ProducerDirect.*;
import static org.example.headers.ProducerHeaders.*;

public class ConsumerHeaders {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        Connection connection = factory.newConnection(); // 創建連接
        Channel channel = connection.createChannel(); // 創建通道

        Map<String, Object> headerMapAll = new HashMap<>();
        headerMapAll.put("x-match", "any");
        headerMapAll.put("system", "test-system");
        headerMapAll.put("env", "dev");

        // 將交換機與隊列綁定
        channel.queueBind(
                QUEUE_NAME_HEADERS_ALL, // 隊列名稱
                EXCHANGE_NAME_HEADERS,
                "", // 路由鍵, 無使用, 故設置為空字串, 請勿設置為 null
                headerMapAll
        );

        Map<String, Object> headerMapAny = new HashMap<>();
        headerMapAny.put("x-match", "any");
        headerMapAny.put("system", "test-system");
        headerMapAny.put("env", "dev");

        // 將交換機與隊列綁定
        channel.queueBind(
                QUEUE_NAME_HEADERS_ANY, // 隊列名稱
                EXCHANGE_NAME_HEADERS,
                "", // 路由鍵, 無使用, 故設置為空字串, 請勿設置為 null
                headerMapAny
        );

        // 消費消息
        channel.basicConsume(QUEUE_NAME_HEADERS_ALL, true,
                (consumerTage, message) ->
                        System.out.println("Q_ALL 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );

        // 消費消息
        channel.basicConsume(QUEUE_NAME_HEADERS_ANY, true,
                (consumerTage, message) ->
                        System.out.println("Q_ANY 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );
    }
}