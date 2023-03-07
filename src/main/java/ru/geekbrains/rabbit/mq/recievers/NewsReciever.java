package ru.geekbrains.rabbit.mq.recievers;

import com.rabbitmq.client.*;

import java.util.Scanner;

public class NewsReciever {

    private static final String EXCHANGE_NAME = "newsExchanger";

    public static void main(String[] args) throws Exception {

        ConnectionFactory factory =new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.DIRECT);
        String queueName =channel.queueDeclare().getQueue();


        System.out.println("Введите команду 'set_topic *topic*' для добавления топика подписок.\n" +
                "Используйте команду 'start' для запуска");
        Scanner scanner = new Scanner(System.in);
        String topic = "";
        while (true){
            String msg = scanner.nextLine();
            String[] arr = msg.split(" ",2);
            if(msg.startsWith("set_topic")){
                channel.queueBind(queueName,EXCHANGE_NAME,arr[1]);
            }else if(msg.equals("start")){
                System.out.println("go");
                break;
            }
        }


        DeliverCallback deliverCallback =(consumerTag,delivery)->{
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println("Received "+message);
        };

        channel.basicConsume(queueName,true,deliverCallback,consumerTag ->{
        });

        System.out.println("Введите команду 'add_topic *topic*' для добавления топика подписок.\n"+
                "Введите команду 'delete_topic *topic*' для удаления топика из подписок."
        );
        while (true) {
            String msg = scanner.nextLine();
            String[] arr = msg.split(" ", 2);
            if (msg.startsWith("add_topic")) {
                channel.queueBind(queueName, EXCHANGE_NAME, arr[1]);
            }else if(msg.startsWith("delete_topic")) {
                channel.queueUnbind(queueName, EXCHANGE_NAME, arr[1]);
            }
        }
    }
}
