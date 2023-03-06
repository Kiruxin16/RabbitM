package ru.geekbrains.rabbit.mq.producers;


import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class NewsProducer {
    private static final String EXCHANGE_NAME = "newsExchanger";

    private static final List<String> topics = new ArrayList<>(List.of(
            "php","cSharp","c++","java","kotlin"));


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
                if(isTopicCorrect(topic)){
                    message=msg.substring(topic.length()+1);
                    channel.basicPublish(EXCHANGE_NAME,topic,null,message.getBytes(StandardCharsets.UTF_8));
                }else if(topic.equals("end")){
                    break;
                }


                System.out.println(message);
            }

        }
    }

    private static boolean isTopicCorrect(String topic){
        for (String t:topics) {
            if (t.equals(topic))
                return true;
        }
        return false;
    }
}
