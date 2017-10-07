package whatsapp.cursoandroid.com.SD_Chat.helper;

import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

import whatsapp.cursoandroid.com.SD_Chat.model.Conversa;

public class Preferencias {

    private Context contexto;
    private SharedPreferences preferences;
    private final String NOME_ARQUIVO = "whatsapp.preferencias";
    private final int MODE = 0;
    private SharedPreferences.Editor editor;

    private final String CHAVE_IDENTIFICADOR = "identificadorUsuarioLogado";
    private final String CHAVE_NOME = "nomeUsuarioLogado";
    private  final String chave_contato ="chaveContato";

    public Preferencias( Context contextoParametro){

        contexto = contextoParametro;
        preferences = contexto.getSharedPreferences(NOME_ARQUIVO, MODE );
        editor = preferences.edit();

    }

    public void salvarDados( String identificadorUsuario, String nomeUsuario ){

        editor.putString(CHAVE_IDENTIFICADOR, identificadorUsuario);
        editor.putString(CHAVE_NOME, nomeUsuario);
        editor.commit();

    }
    public void salvarcontatos( String nome, String contato ){

        editor.putString("nome", nome);
        editor.putString(chave_contato, contato);
        editor.commit();

    }
    public String getChave_contato(){
        return preferences.getString(chave_contato, null);
    }

    public String getIdentificador(){
        return preferences.getString(CHAVE_IDENTIFICADOR, null);
    }

    public String getNome(){
        return preferences.getString(CHAVE_NOME, null);
    }



    //Salvando
    public void saveContactList(ArrayList<String> listaDeContatos){

        Set<String> contactSet = new HashSet<>();
        contactSet.addAll(listaDeContatos);
        editor.putStringSet("lista_de_contatos_key", contactSet);
        editor.commit();
        Log.e("CONTACTE  ", String.valueOf(contactSet));
    }
    public void saveContactListconversa(ArrayList<Conversa> lista){
int valor=0;
        for (int i =0;i<lista.size();i++) {
            Set<String> contactSet = new HashSet<>();
            contactSet.addAll( lista.get(i).getMensagens());
            editor.putStringSet(""+i, contactSet);
            editor.putString("send"+i,lista.get(i).getNome());
           valor=i;

        }
        editor.putString("i", String.valueOf(valor));
        Log.e("aquiii  ", String.valueOf(preferences.getStringSet(""+0,null)));
        editor.commit();
    }
    public ArrayList<Conversa> retrievetListconversa(){
        ArrayList<Conversa> conversas = new ArrayList<Conversa>();
                if (preferences.contains("i") ){
            int a = Integer.parseInt(preferences.getString("i", null)) + 1;

            for (int i = 0; i < a; i++) {
                Set<String> concatcSet = preferences.getStringSet("" + i, null);
                String nome = preferences.getString("send" + i, null);
                ArrayList<String> mensagem = new ArrayList<String>(concatcSet);
                Conversa conversa = new Conversa();
                conversa.setNome(nome);
                conversa.setMensagens(mensagem);
                conversas.add(conversa);
            }
        }
        Log.e("eii  ", String.valueOf(conversas));
        return conversas;
    }
    public void saveContactList2(String conversa){
ArrayList<String> conversas  =  new ArrayList<String>();
        conversas = retrieveContactList();
        conversas.add(conversa);
        Set<String> contactSet = new HashSet<>();
        contactSet.addAll(conversas);
        editor.putStringSet("lista_de_contatos_key", contactSet);
        editor.commit();
    }


    //Recuperando
    public ArrayList<String> retrieveContactList(){

        Set<String> concatcSet = preferences.getStringSet("lista_de_contatos_key", null);
        Log.e("CONTACTE  ", String.valueOf(concatcSet));
        return new ArrayList<String>(concatcSet);
    }

}
