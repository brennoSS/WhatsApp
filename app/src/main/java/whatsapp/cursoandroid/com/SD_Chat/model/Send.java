package whatsapp.cursoandroid.com.SD_Chat.model;

import android.os.Build;
import com.google.protobuf.ByteString;
import com.rabbitmq.client.Channel;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.GregorianCalendar;

import whatsapp.cursoandroid.com.SD_Chat.model.Msg.Mensagem;

/**
 * Created by Brenno on 02/10/2017.
 */

public class Send {

    public static void sendUsuario(Channel channel, String usuario, String destinatario, String mesagem) throws UnsupportedEncodingException, IOException {
        Mensagem msg = mensagemCompleta(mesagem, usuario, null);
        channel.basicPublish("", destinatario, null, msg.toByteArray());
    }
    public static void sendGrupo(Channel channel, String sender, String group, String message) throws UnsupportedEncodingException, IOException {
        Mensagem msg = null;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
            msg = mensagemCompleta(message, sender, group + "/");
        }
        channel.basicPublish(group, "", null, msg.toByteArray());
    }

    private static Mensagem mensagemCompleta(String mesagem, String usuario, String grupo) {
        GregorianCalendar calendar = new GregorianCalendar();
        String data = Integer.toString(calendar.get(GregorianCalendar.DAY_OF_MONTH))
                + "/" + Integer.toString(calendar.get(GregorianCalendar.MONTH)) + "/"
                + Integer.toString(calendar.get(GregorianCalendar.YEAR));
        String horario = Integer.toString(calendar.get(GregorianCalendar.HOUR_OF_DAY))
                + ":" + Integer.toString(calendar.get(GregorianCalendar.MINUTE));

        if (grupo == null){
            grupo = "/";
        }
        Mensagem.Builder mensagem = Mensagem.newBuilder();
        mensagem.setSender(usuario);
        mensagem.setDate(data);
        mensagem.setGroup(grupo);
        mensagem.setTime(horario);
        Mensagem.Conteudo.Builder mss =
                Mensagem.Conteudo.newBuilder().setName("");
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
            mss.setBody(ByteString.copyFrom(mesagem, StandardCharsets.UTF_8));
        }
        mss.setType("text/plain");
        mensagem.addContent(mss);
        return mensagem.build();
    }





}