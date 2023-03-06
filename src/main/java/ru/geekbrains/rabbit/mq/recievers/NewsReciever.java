package ru.geekbrains.rabbit.mq.recievers;

import com.rabbitmq.client.*;

public class NewsReciever {

    private static final String EXCHANGE_NAME = "newsExchanger";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory =new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName =channel.queueDeclare().getQueue();

        channel.queueBind(queueName,EXCHANGE_NAME,"php");

        System.out.println("Fine");
        DeliverCallback deliverCallback =(consumerTag,delivery)->{
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received "+message);
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag ->{
        });

        /*while (true){

        }*/


    }
}
