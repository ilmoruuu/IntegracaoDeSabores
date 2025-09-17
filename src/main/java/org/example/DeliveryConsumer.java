package org.example;

import com.rabbitmq.client.*;
import java.nio.charset.StandardCharsets;

public class DeliveryConsumer {
    private static final String EXCHANGE = "orders"; //Aqui definimos de QUAL exchange estamos recebendo
    private static final String QUEUE = "delivery_queue";
    private static final String ROUTING_KEY = "order.delivery";

    public static void main(String[] args) throws Exception {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection conn = factory.newConnection();
        Channel channel = conn.createChannel();

        channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true);

        //Respectivamente: Nome da fila, a "durabilidade" da fila, se a fila some automaticamente se não tem mais consumidores, se tem algum parâmetro personalidado:
        channel.queueDeclare(QUEUE, true, false, false, null);
        channel.queueBind(QUEUE, EXCHANGE, ROUTING_KEY);

        System.out.println("[Motoboy] - Aguardando pedidos de entregas...");

        DeliverCallback callback = (consumerTag, delivery) -> {
            String msg = new String(delivery.getBody(), StandardCharsets.UTF_8); //Aqui vai extrair o corpo da requisição
            System.out.println("Endereço recebido: " + msg);
            channel.basicAck(delivery.getEnvelope().getDeliveryTag(), false);
        };

        channel.basicConsume(QUEUE, false, callback, consumerTag -> {});
    }
}
