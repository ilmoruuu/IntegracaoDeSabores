package producers;

import com.rabbitmq.client.*;

import java.nio.charset.StandardCharsets;

public class Producer {
    //Aqui definimos nosso Exchange, ele que vai "dividir" nossa requisição:
    private static final String EXCHANGE = "orders";

    //Aqui vamos derfinir nossas "chaves", são elas que irão direcionar pra nossas queues (filas):
    private static final String KITCHEN_KEY = "order.kitchen";
    private static final String DELIVERY_KEY = "order.delivery";
    private static final String FINANCE_KEY = "order.finance";

    public static void main(String[] args) throws Exception {
        System.out.println("Bem vindo à Integração de Sabores!\n A melhor e maior pizzaria do nossa rua!");
        ConnectionFactory factory = new ConnectionFactory(); //Instanciamos a nossa conexão
        factory.setHost("localhost");
        try (Connection conn = factory.newConnection();
             Channel channel = conn.createChannel()) {

            channel.exchangeDeclare(EXCHANGE, BuiltinExchangeType.DIRECT, true); //Aqui definimos o "comportamento" do exchange:

            String orderId = "order-001";

            //Estrutura JSON que vai ser divida:
            String kitchenJson = "{\"id\":\""+orderId+"\",\"items\":[{\"name\":\"Moda da Casa\",\"size\":\"G\"}],\"notes\":\"Sem cebola\"}";
            String deliveryJson = "{\"id\":\""+orderId+"\",\"customer\":\"Élisson\",\"address\":\"Rua Cap. Pedro Rodrigues\",\"phone\":\"999999999\"}";
            String financeJson = "{\"id\":\""+orderId+"\",\"total\":55.50,\"paymentMethod\":\"PIX\"}";

            //Aqui temos a distribuição:
            channel.basicPublish(EXCHANGE, KITCHEN_KEY,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    kitchenJson.getBytes(StandardCharsets.UTF_8));

            channel.basicPublish(EXCHANGE, DELIVERY_KEY,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    deliveryJson.getBytes(StandardCharsets.UTF_8));

            channel.basicPublish(EXCHANGE, FINANCE_KEY,
                    MessageProperties.PERSISTENT_TEXT_PLAIN,
                    financeJson.getBytes(StandardCharsets.UTF_8));

            System.out.println("Pedido feito! Os setores da pizzaria já foram Informados!\n Já já chega aí! ;)");
        }
    }
}
