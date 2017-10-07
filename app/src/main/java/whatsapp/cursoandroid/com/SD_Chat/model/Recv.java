package whatsapp.cursoandroid.com.SD_Chat.model;

/**
 * Created by Brenno on 02/10/2017.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

import android.content.Context;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.rabbitmq.client.AMQP;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Consumer;
import com.rabbitmq.client.DefaultConsumer;
import com.rabbitmq.client.Envelope;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;

import whatsapp.cursoandroid.com.SD_Chat.helper.Singleton;
import whatsapp.cursoandroid.com.SD_Chat.model.Msg.Mensagem;

/**
 *
 * @author Brenno
 */
public class Recv implements Runnable {
    private static Channel channel;
    private static  Boolean flag=false;
    private static String  nome;
    private static  ArrayList<Conversa> listaConversa;
    private ListView listView;
    private ArrayAdapter<Conversa> adapter;
    private    Context context;

    public Recv(Channel channel, String nome, ListView listView, Context context) {
        this.channel = channel;
        this.nome = nome;
        this.listView = listView;
        this.context = context;

    }
    @Override
    public void run() {
       // final Preferencias preferencias = new Preferencias(context);

         listaConversa = new ArrayList<Conversa>();
        //listaConversa = preferencias.retrievetListconversa();
        listaConversa = Singleton.getInstance().getListaConversa();

        final Consumer consumer;
        consumer = new DefaultConsumer(channel) {

            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                Mensagem msg = Mensagem.parseFrom(body);
                String msg2 = null;
                String grupo = null;
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    msg2 = new String(msg.getContent(0).getBody().toByteArray(), StandardCharsets.UTF_8);
                }
                String send = "";
                if (msg.getGroup().equals("/")){
                    send = msg.getSender();
                }else{
                    send = msg.getGroup();
                }
                Log.e("msn",msg.getDate() +
                        " ás "+msg.getTime()+") " + msg.getSender() +
                        " diz: "+ msg2);
                listaConversa = Singleton.getInstance().getListaConversa();
                if(!Singleton.getInstance().getLinha().equals(msg2)) {
                    for (int i = 0; i < listaConversa.size(); i++) {
                        if (send.equals(listaConversa.get(i).getNome())) {
                            listaConversa.get(i).setMensagens(msg.getSender() + "-" + "(" + msg.getDate() + " ás " + msg.getTime() + ") " + " diz: " + msg2);
                            flag = true;

                            Log.e("conercsasss", String.valueOf(listaConversa.get(i).getMensagens()));
                            break;
                        }
                    }
                    if (flag == true) {

                    } else {
                        Conversa conversa = new Conversa();
                        conversa.setNome(send);

                        conversa.setMensagens(msg.getSender() + "-" + "(" + msg.getDate() +
                                " ás " + msg.getTime() + ") " +
                                " diz: " + msg2);
                        listaConversa.add(conversa);
                        Log.e("conercsasss", String.valueOf(conversa.getMensagens()));
                    }
                    flag = false;
                    Singleton.getInstance().setListaConversa(listaConversa);

                }



            }
        };
        try {
            channel.basicConsume(nome, true, consumer);
        } catch (IOException ex) {
            Logger.getLogger(Recv.class.getName()).log(Level.SEVERE, null, ex);
        }


    }
    public  ArrayList<Conversa> getListaConversa(){

        return listaConversa;
    }

}

