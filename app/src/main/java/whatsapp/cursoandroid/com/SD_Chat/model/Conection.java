package whatsapp.cursoandroid.com.SD_Chat.model;

/**
 * Created by Brenno on 02/10/2017.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
        import com.rabbitmq.client.Channel;
        import com.rabbitmq.client.Connection;
        import com.rabbitmq.client.ConnectionFactory;
        import java.io.IOException;
        import java.util.concurrent.TimeoutException;

/**
 *
 * @author Brenno
 */
public class Conection {

    static ConnectionFactory factory;
    static Connection connection;
    static Channel channel;

    public Conection() throws IOException, TimeoutException {
        factory = new ConnectionFactory();
        factory.setHost("54.186.212.11");
        factory.setUsername("davi");
        factory.setPassword("davi");
        //factory.setHost("rhino.rmq.cloudamqp.com");
        //factory.setUsername("ooztnauk");
        //factory.setPassword("TG1wCS2aVAQxRdwsu4GitLR-LhYffW8s");
        //factory.setVirtualHost("ooztnauk");
        connection= factory.newConnection();
        channel = connection.createChannel();
    }
    public Channel getChannel() {
        return channel;
    }
    public void criarUsuario(String userName) throws IOException {
        channel.queueDeclare(userName, false, false, false, null);
    }
    public void queueDeclare(String queueName) throws IOException {
        channel.queueDeclare(queueName, false, false, false, null);
    }
    public void criarGrupo(String userName, String groupName) throws IOException {
        channel.exchangeDeclare(groupName, "fanout");
        channel.queueBind(userName, groupName, "");
        System.out.println(userName + " criou o grupo " + groupName);
    }
    public void adicionarUsuarioGrupo(String userName, String groupName) throws IOException {
        channel.queueBind(userName, groupName, "");
        System.out.println(userName + " foi adicionado ao grupo " + groupName);
    }
    public void removerUsuarioGrupo(String userName, String groupName) throws IOException {
        channel.queueUnbind(userName, groupName, "");
        System.out.println(userName+ " foi removido do grupo " + groupName);
    }
    public void deletarGrupo(String groupName) throws IOException {
        channel.exchangeDelete(groupName);
        System.out.println("Grupo " + groupName + " foi detelado");
    }

}
