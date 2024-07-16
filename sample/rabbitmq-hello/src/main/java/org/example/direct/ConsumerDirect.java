package org.example.direct;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.example.direct.ProducerDirect.*;

public class ConsumerDirect {

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        Connection connection = factory.newConnection(); // 創建連接
        Channel channel = connection.createChannel(); // 創建通道

        // 消費消息
        channel.basicConsume(QUEUE_NAME_DIRECT_1, true,
                (consumerTage, message) ->
                        System.out.println("Q1 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );

        channel.basicConsume(QUEUE_NAME_DIRECT_2, true,
                (consumerTage, message) ->
                        System.out.println("Q2 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q2 中斷消費消息。")
        );

        channel.basicConsume(QUEUE_NAME_DIRECT_3, true,
                (consumerTage, message) ->
                        System.out.println("Q3 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q3 中斷消費消息。")
        );

        channel.basicConsume(QUEUE_NAME_DIRECT_4, true,
                (consumerTage, message) ->
                        System.out.println("Q4 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q4 中斷消費消息。")
        );
    }
}