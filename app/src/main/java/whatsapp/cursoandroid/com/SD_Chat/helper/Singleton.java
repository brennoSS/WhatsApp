package whatsapp.cursoandroid.com.SD_Chat.helper;

import android.util.Log;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.SD_Chat.model.Contato;
import whatsapp.cursoandroid.com.SD_Chat.model.Conversa;

/**
 * Created by Brenno on 03/10/2017.
 */

public class Singleton {
    private String DADOSLOGIN = "dadosLogin";
    private String USUARIO = "username";
    private String SENHA = "password";
   private String usuario ="";
    private String linha ="";
    private  String grupo= "nulo";
    private String deletar="nulo";
    private  ArrayList<Conversa> listaConversa= new ArrayList<Conversa>();
    private  ArrayList<Contato> contato= new ArrayList<Contato>();

    private static Singleton instance;

    public String getusuario() {
        return usuario;
    }

    public void setUsuario(String usuario) {
        this.usuario = usuario;
    }

    private Singleton() {
    }

    public static Singleton getInstance() {
        if (instance == null){

            instance = new Singleton();
            Log.e("instance","nulaaaa");
        }

        return instance;
    }

    public static void setInstance(Singleton instance) {
        Singleton.instance = instance;
    }

    public ArrayList<Conversa> getListaConversa() {
        return listaConversa;
    }

    public void setListaConversa(ArrayList<Conversa> listaConversa) {
        this.listaConversa = listaConversa;
    }

    public String getLinha() {
        return linha;
    }

    public void setLinha(String linha) {
        this.linha = linha;
    }


    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getDeletar() {
        return deletar;
    }

    public void setDeletar(String deletar) {
        this.deletar = deletar;
    }

    public ArrayList<Contato> getContato() {
        return contato;
    }

    public void setContato(ArrayList<Contato> contato) {
        this.contato = contato;
    }
    public void setContatoUM(Contato contato) {
       this.contato.add(contato);
    }
}
