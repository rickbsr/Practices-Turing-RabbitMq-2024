package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Consumer {
    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();

        Connection connection = factory.newConnection(); // 創建連接
        Channel channel = connection.createChannel(); // 創建通道

        // 消費消息
        channel.basicConsume(
                Producer.QUEUE_NAME, // 目標消費隊列
                true, // 消費成功後是否需要自動回應, true 代表將自動回應
                (consumerTage, message) -> // 接受消息的回饋函數
                        System.out.println("接受到消息： " + new String(message.getBody(), StandardCharsets.UTF_8)),
                consumerTage -> System.out.println("中斷消費消息。") // 取消消息的回饋函數
        );
    }
}