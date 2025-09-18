package org.example;

import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;

public class LimparFilas {
    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        try (Connection conexao = factory.newConnection();
             Channel canal = conexao.createChannel()) {

            canal.queuePurge("kitchen_queue");
            canal.queuePurge("delivery_queue");
            canal.queuePurge("finance_queue");

            System.out.println("Todas as filas foram limpas!");
        }
    }
}
