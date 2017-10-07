package whatsapp.cursoandroid.com.SD_Chat.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;
import whatsapp.cursoandroid.com.SD_Chat.R;
import whatsapp.cursoandroid.com.SD_Chat.activity.ConversaActivity;
import whatsapp.cursoandroid.com.SD_Chat.adapter.ConversaAdapter;
import whatsapp.cursoandroid.com.SD_Chat.helper.Preferencias;
import whatsapp.cursoandroid.com.SD_Chat.helper.Singleton;
import whatsapp.cursoandroid.com.SD_Chat.model.Conection;
import whatsapp.cursoandroid.com.SD_Chat.model.Conversa;
import whatsapp.cursoandroid.com.SD_Chat.model.Recv;
import whatsapp.cursoandroid.com.SD_Chat.model.Usuario;

/**
 * A simple {@link Fragment} subclass.
 */
public class ConversasFragment extends Fragment {
    //cria objeto handler que irá lidar com o processo de execução na thread
    private Handler handler = new Handler();
    //Utilizar o Runnable sempre que quiser rodar um processo em uma thread
    private Runnable runnableCodigo;
    private ListView listView;
    private ArrayAdapter<Conversa> adapter;
    private ArrayList<Conversa> conversas;
    public ConversasFragment() {
        // Required empty public constructor
    }

    @Override
    public View onCreateView(final LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_conversas, container, false);
        final Conversa conversa = new Conversa();
        Usuario usuario = new Usuario();
        Intent i = getActivity().getIntent();
        Bundle extra = i.getExtras();
        if( extra != null ) {
            usuario.setNome(extra.getString("nome"));
        }
        Conection conection = null;
        try {
            conection = new Conection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        try {
            conection.criarUsuario(usuario.getNome());
        } catch (IOException e) {
            e.printStackTrace();
        }
        listView = (ListView) view.findViewById(R.id.lv_conversas);
        final Recv receiver = new Recv(conection.getChannel(), usuario.getNome(),listView,getActivity() );
        Thread receiving = new Thread(receiver);
        receiving.start();
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // Monta listview e adapter
        conversas = new ArrayList<Conversa>();
        Singleton.getInstance().setListaConversa(receiver.getListaConversa());
        conversas = Singleton.getInstance().getListaConversa();

        adapter = new ConversaAdapter(getActivity(), conversas );
        listView.setAdapter( adapter );
        // recuperar dados do usuário
        final Preferencias preferencias = new Preferencias(getActivity());
        runnableCodigo = new Runnable() {
            @Override
            public void run() {
                if(Singleton.getInstance().getGrupo()!="nulo" || !Singleton.getInstance().getGrupo().equals("nulo") ){
                    Conversa conversa = new Conversa();
                    conversa.setNome(Singleton.getInstance().getGrupo());
                    conversas.add(conversa);
                    Singleton.getInstance().setGrupo("nulo");
                }
                if(Singleton.getInstance().getDeletar()!="nulo" || !Singleton.getInstance().getDeletar().equals("nulo") ) {
                    Log.e("nao nulo","aki1");
                    for (int i = 0; i < conversas.size(); i++) {
                        if (Singleton.getInstance().getDeletar().equals(conversas.get(i).getNome())) {
                         conversas.get(i).setMensagens("-Grupo DELETADO...");
                        }
                    }
                    Singleton.getInstance().setDeletar("nulo");
                }

                adapter.notifyDataSetChanged();

                Singleton.getInstance().setListaConversa(conversas);
                handler.postDelayed(runnableCodigo, 2500);
            }
        };
        /*
            faz a primeira execução do código, uma vez iniciado, dentro do código será
            mantido sua execução, criando um processo pós execução.
        */
        handler.post(runnableCodigo);


        //Adicionar evento de clique na lista
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Preferencias preferencias = new Preferencias(getActivity());
                Conversa conversa = conversas.get(position);
                Singleton.getInstance().setListaConversa(conversas);
                Intent intent = new Intent(getActivity(), ConversaActivity.class );
                intent.putExtra("nome", conversa.getNome() );
                intent.putExtra("posicao",position);
                //Salvando

                startActivity(intent);

            }
        });

        return view;

    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }


}
