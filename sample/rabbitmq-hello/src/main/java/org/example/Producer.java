package org.example;

import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import org.example.helper.RabbitMqFactoryHelper;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.concurrent.TimeoutException;

public class Producer {

    public static final String EXCHANGE_NAME = "exchange_name";
    public static final String QUEUE_NAME = "queue_name";
    public static final String QUEUE_KEY = "key";

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = RabbitMqFactoryHelper.getConnectionFactory();


        try (
                Connection connection = factory.newConnection(); // 創建連接
                Channel channel = connection.createChannel() // 創建通道
        ) {
            // 創建交換機
            channel.exchangeDeclare(
                    EXCHANGE_NAME, // 交換機名稱
                    BuiltinExchangeType.DIRECT, // 交換機類型：direct, topic, fanout & headers（性能較差）
                    true, // 指定交換機是否需要持久化, 若設置為 true, 那麼交換機的元數據要持久化
                    false, // 指定交換機在沒有隊列綁定時, 是否需要刪除, 若設置 false , 那麼表示不需要刪除
                    null // Map<String, Object> 類型, 用來設置指定交換機的某些結構化信息, 此處不需要, 設置為 null
            );

            // 創建隊列
            channel.queueDeclare(
                    QUEUE_NAME, // 隊列名稱
                    true, // 指定隊列是否需要持久化, 但是要注意, 此處持久化對象是指隊列名稱等這些元數據的持久化, 而不是隊列中的消息
                    false, // 指定隊列是否為私有, 若為私有, 只有創建它的應用程序才能消費其消息
                    false, // 指定隊列在沒有消費者訂閱的情況下是否要自動刪除
                    null // 用來設置指定隊列的某些結構化信息, 譬如聲明死信隊列, 或是磁盤隊列時
            );

            // 將交換機與隊列綁定
            channel.queueBind(
                    QUEUE_NAME,
                    EXCHANGE_NAME,
                    QUEUE_KEY // 路由鍵, 在直連模式下, 也可以直接以我們隊列名稱為路由鍵即可
            );

            // 消息
            String message = "hello rabbitmq";
            String msgKey = "key";

            // 發送消息
            channel.basicPublish(
                    EXCHANGE_NAME, // 目標交換機
                    msgKey, // 路由鍵
                    null, // 其它參數信息
                    message.getBytes(StandardCharsets.UTF_8) // 欲發送消息體本身, 需為字節碼
            );
        }
    }
}

