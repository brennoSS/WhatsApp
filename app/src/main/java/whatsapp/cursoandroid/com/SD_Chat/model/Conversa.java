package whatsapp.cursoandroid.com.SD_Chat.model;

import java.util.ArrayList;

public class Conversa {

    private String nome;
    private String mensagem;
    private ArrayList<String> mensagens = new ArrayList<>();

    public Conversa() {
    }
    public ArrayList<String> getMensagens() {
        return mensagens;
    }

    public void setMensagens(String nome) {
        this.mensagens.add(nome);
    }
    public void setMensagens(ArrayList<String> nome) {
        this.mensagens.clear();
        this.mensagens= nome;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getMensagem() {
        return mensagem;
    }

    public void setMensagem(String mensagem) {
        this.mensagem = mensagem;
    }


}
