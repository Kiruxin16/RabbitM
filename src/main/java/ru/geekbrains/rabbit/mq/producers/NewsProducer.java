package ru.geekbrains.rabbit.mq.producers;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.Scanner;

public class NewsProducer {
    private static final String EXCHANGE_NAME = "newsExchanger";


    public static void main(String[] args) throws Exception {
        ConnectionFactory factory =new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection connection = factory.newConnection();
            Channel channel = connection.createChannel()){
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
            Scanner scanner = new Scanner(System.in);
            while (true){
                String msg = scanner.nextLine();
                String topic= msg.split(" ")[0];
                String message="";
                if(topic.equals("php")){
                    message=msg.substring(4);
                    channel.basicPublish(EXCHANGE_NAME,"php",null,message.getBytes(StandardCharsets.UTF_8));
                }



                System.out.println(message);
            }

        }
    }
}
