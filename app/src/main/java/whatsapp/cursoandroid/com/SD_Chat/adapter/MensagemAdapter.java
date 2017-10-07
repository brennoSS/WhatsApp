package whatsapp.cursoandroid.com.SD_Chat.adapter;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.SD_Chat.R;
import whatsapp.cursoandroid.com.SD_Chat.helper.Preferencias;
import whatsapp.cursoandroid.com.SD_Chat.model.Mensagem;

public class MensagemAdapter extends ArrayAdapter<Mensagem> {

    private Context context;
    private ArrayList<Mensagem> mensagens;
    private String nome ;

    public MensagemAdapter(Context c, ArrayList<Mensagem> objects, String nome) {
        super(c, 0, objects);
        this.context = c;
        this.mensagens = objects;
        this.nome=nome;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        View view = null;


        // Verifica se a lista está preenchida
        if( mensagens != null ){

            // Recupera dados do usuario remetente
            Preferencias preferencias = new Preferencias(context);
            String idUsuarioRementente = preferencias.getNome();

            // Inicializa objeto para montagem do layout
            LayoutInflater inflater = (LayoutInflater) context.getSystemService(context.LAYOUT_INFLATER_SERVICE);

            // Recupera mensagem
            Mensagem mensagem = mensagens.get( position );

            // Monta view a partir do xml
            if(idUsuarioRementente.equals( mensagem.getIdUsuario() )  ){
                view = inflater.inflate(R.layout.item_mensagem_direita, parent, false);
                TextView textoMensagem = (TextView) view.findViewById(R.id.tv_mensagem);
                textoMensagem.setText( mensagem.getMensagem() );
            }else {
                view = inflater.inflate(R.layout.item_mensagem_esquerda, parent, false);
                TextView textoMensagem = (TextView) view.findViewById(R.id.tv_mensagem);
                if(nome.equals(mensagem.getIdUsuario())){
                    textoMensagem.setText( mensagem.getMensagem() );
                }else{
                    textoMensagem.setText(mensagem.getIdUsuario()+" "+ mensagem.getMensagem() );
                }

            }


            // Recupera elemento para exibição


            /*
            String array[] =nomeUsuarioDestinatario.split("/");
            if (array.length ==1){
                String temp2 = array[0] ;
                nomeUsuarioDestinatario = temp2;

            }*/



        }

        return view;

    }
}
