package org.example.topic;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

import static org.example.topic.ProducerTopic.*;

public class ConsumerTopic {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        Connection connection = factory.newConnection(); // 創建連接
        Channel channel = connection.createChannel(); // 創建通道

        // 消費消息
        channel.basicConsume(QUEUE_NAME_TOPIC_1, true,
                (consumerTage, message) ->
                        System.out.println("Q1 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );

        // 消費消息
        channel.basicConsume(QUEUE_NAME_TOPIC_2, true,
                (consumerTage, message) ->
                        System.out.println("Q1 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );

        // 消費消息
        channel.basicConsume(QUEUE_NAME_TOPIC_3, true,
                (consumerTage, message) ->
                        System.out.println("Q1 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );

        // 消費消息
        channel.basicConsume(QUEUE_NAME_TOPIC_4, true,
                (consumerTage, message) ->
                        System.out.println("Q1 接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("Q1 中斷消費消息。")
        );
    }
}