package whatsapp.cursoandroid.com.SD_Chat.fragment;


import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

import whatsapp.cursoandroid.com.SD_Chat.R;
import whatsapp.cursoandroid.com.SD_Chat.activity.ConversaActivity;
import whatsapp.cursoandroid.com.SD_Chat.adapter.ContatoAdapter;
import whatsapp.cursoandroid.com.SD_Chat.helper.Preferencias;
import whatsapp.cursoandroid.com.SD_Chat.helper.Singleton;
import whatsapp.cursoandroid.com.SD_Chat.model.Contato;
import whatsapp.cursoandroid.com.SD_Chat.model.Conversa;

/**
 * A simple {@link Fragment} subclass.
 */
public class ContatosFragment extends Fragment {

    private ListView listView;
    private ArrayAdapter adapter;
    private ArrayList<Contato> contatos;
    //cria objeto handler que irá lidar com o processo de execução na thread
    private Handler handler = new Handler();

    //Utilizar o Runnable sempre que quiser rodar um processo em uma thread
    private Runnable runnableCodigo;

    public ContatosFragment() {
        // Required empty public constructor
    }

    @Override
    public void onStart() {
        super.onStart();
    }

    @Override
    public void onStop() {
        super.onStop();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, final ViewGroup container,
                             Bundle savedInstanceState) {

        //Instânciar objetos
        contatos = new ArrayList<>();

        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_contatos, container, false);

        //Monta listview e adapter
        listView = (ListView) view.findViewById(R.id.lv_contatos);
        Preferencias preferencias = new Preferencias(getActivity());
        String novo = preferencias.getChave_contato();
        Contato contato = new Contato();

        contato.setNome(novo);
        contatos.add(contato);
        contatos = Singleton.getInstance().getContato();
        adapter = new ContatoAdapter(getActivity(), contatos );
        listView.setAdapter( adapter );


        runnableCodigo = new Runnable() {
            @Override
            public void run() {
                //aqui irá o bloco de códigos que fará a execução de tempos em tempos
                // Toast.makeText(getActivity(), "Executado o código", Toast.LENGTH_LONG).show();
                contatos = Singleton.getInstance().getContato();
                adapter.notifyDataSetChanged();
                //executa o objeto runnableCodigo a cada 10 segundo, configure aqui o tempo
                handler.postDelayed(runnableCodigo, 3000);
            }
        };

       // contatos.add( contato );

       //adapter.notifyDataSetChanged();



        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                int posicao =0;
                Preferencias preferencias = new Preferencias(getActivity());
                Conversa conversa = new Conversa();
                ArrayList<Conversa>  conversas = new ArrayList<Conversa>();
                conversas = Singleton.getInstance().getListaConversa();
                String nome = contatos.get(position).getNome();
                for (int i = 0; i < conversas.size(); i++) {
                    if (nome.equals(conversas.get(i).getNome())) {
                        posicao = i;
                        break;
                    }
                }
                if(posicao!=0){
                     conversa = conversas.get(posicao);
                }else {
                    posicao = conversas.size();
                    conversa.setNome(nome);
                    conversas.add(conversa);
                    Singleton.getInstance().setListaConversa(conversas);

                }

                Intent intent = new Intent(getActivity(), ConversaActivity.class );
                intent.putExtra("nome", conversa.getNome() );
                intent.putExtra("posicao",posicao);
                //Salvando
                startActivity(intent);
            }
        });

        return view;

    }

}
