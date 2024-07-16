package org.example.helper;

import com.rabbitmq.client.ConnectionFactory;

public class RabbitMqFactoryHelper {

    private static final String RABBITMQ_HOST = "127.0.0.1";
    private static final int RABBITMQ_PORT = 5672;
    private static final String RABBITMQ_USERNAME = "guest";
    private static final String RABBITMQ_PASSWORD = "guest";

    private static ConnectionFactory factory;

    public static ConnectionFactory getConnectionFactory() {
        if (factory == null) {
            // 創建一個連接工廠
            factory = new ConnectionFactory();

            // 配置 RabbitMQ Host
            factory.setHost(RABBITMQ_HOST);

            // 配置端口號
            factory.setPort(RABBITMQ_PORT);

            // 配置 Act
            factory.setUsername(RABBITMQ_USERNAME);

            // 配置 Pwd
            factory.setPassword(RABBITMQ_PASSWORD);
        }
        return factory;
    }
}
