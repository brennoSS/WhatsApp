package whatsapp.cursoandroid.com.SD_Chat.activity;

import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import whatsapp.cursoandroid.com.SD_Chat.R;
import whatsapp.cursoandroid.com.SD_Chat.adapter.MensagemAdapter;
import whatsapp.cursoandroid.com.SD_Chat.helper.Preferencias;
import whatsapp.cursoandroid.com.SD_Chat.helper.Singleton;
import whatsapp.cursoandroid.com.SD_Chat.model.Conection;
import whatsapp.cursoandroid.com.SD_Chat.model.Conversa;
import whatsapp.cursoandroid.com.SD_Chat.model.Mensagem;
import whatsapp.cursoandroid.com.SD_Chat.model.Send;


public class ConversaActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private EditText editMensagem;
    private ImageButton btMensagem;
    private int grupo =0;

    private ListView listView;
    private ArrayList<Mensagem> mensagens;
    private ArrayAdapter<Mensagem> adapter;
    // dados do destinatário
    private String nomeUsuarioDestinatario;
    private String idUsuarioDestinatario;
    private int posicao;
    Conection conection = null;
    ArrayList<Conversa> conversas = new ArrayList<Conversa>();
    ArrayList<String> msg = new ArrayList<String>();

     Preferencias preferencias;
    // dados do rementente
    private String idUsuarioRemetente;
    private String nomeUsuarioRemetente;
    private Handler handler = new Handler();
    //Utilizar o Runnable sempre que quiser rodar um processo em uma thread
    private Runnable runnableCodigo;
    @Override
    public void onBackPressed() {

        super.onBackPressed();
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_conversa);

        toolbar = (Toolbar) findViewById(R.id.tb_conversa);
        editMensagem = (EditText) findViewById(R.id.edit_mensagem);
        btMensagem = (ImageButton) findViewById(R.id.bt_enviar);
        listView = (ListView) findViewById(R.id.lv_conversas);
        preferencias = new Preferencias(ConversaActivity.this);

        // dados do usuário logado

        idUsuarioRemetente = preferencias.getIdentificador();

        Bundle extra = getIntent().getExtras();

        if( extra != null ){
            nomeUsuarioDestinatario = extra.getString("nome");
            posicao= extra.getInt("posicao");

        }

        try {
            conection = new Conection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        // Configura toolbar
        toolbar.setTitle( nomeUsuarioDestinatario );
        toolbar.setNavigationIcon(R.drawable.ic_action_arrow_left);
        setSupportActionBar(toolbar);




        // Monta listview e adapter
        mensagens = new ArrayList<>();
        mensagens.clear();
        //conversas = preferencias.retrievetListconversa();
        conversas = Singleton.getInstance().getListaConversa();
        msg= conversas.get(posicao).getMensagens();

        Log.e("con", String.valueOf(msg));

        for (int i = 0; i <msg.size();i++){
            Mensagem  mensagem = new Mensagem();
            String temp = msg.get(i);
            String array[] =temp.split("-");
            String temp2 = array[1] ;
            mensagem.setIdUsuario(array[0]);
            mensagem.setMensagem(temp2);
            mensagens.add(mensagem);
        }


        adapter = new MensagemAdapter(ConversaActivity.this, mensagens,nomeUsuarioDestinatario);
        listView.setAdapter( adapter );
        runnableCodigo = new Runnable() {
            @Override
            public void run() {
                //conversas = preferencias.retrievetListconversa();
                conversas = Singleton.getInstance().getListaConversa();
                mensagens.clear();
                msg= conversas.get(posicao).getMensagens();
                // con = preferencias.retrieveContactList();
                Log.e("con", String.valueOf(msg));

                for (int i = 0; i <msg.size();i++){
                    Mensagem  mensagem = new Mensagem();
                    String temp = msg.get(i);
                    String array[] =temp.split("-");
                    String temp2 = array[1] ;
                    mensagem.setIdUsuario(array[0]);
                    mensagem.setMensagem(temp2);
                    mensagens.add(mensagem);
                }
                adapter.notifyDataSetChanged();
                Log.e("taaaa","fazendo");

                //executa o objeto runnableCodigo a cada 10 segundo, configure aqui o tempo
                handler.postDelayed(runnableCodigo, 2500);
            }
        };
        handler.post(runnableCodigo);
        // Enviar mensagem
        btMensagem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String idUsuarioRementente = Singleton.getInstance().getusuario();
                String textoMensagem = idUsuarioRementente+"-"+editMensagem.getText().toString();
                Singleton.getInstance().setLinha(editMensagem.getText().toString());
                Mensagem  mensagem = new Mensagem();
                mensagem.setIdUsuario(idUsuarioRementente);
                mensagem.setMensagem(editMensagem.getText().toString());
                Log.e("id",idUsuarioRementente);
                msg.add(textoMensagem);
                String temp = nomeUsuarioDestinatario;
                String array[] =temp.split("");
                String temp2 = array[array.length-1] ;
                Log.e("aaaaaaaaaaaaaaaaa ",temp2);
                try {
                    if (temp2.equals("/") || temp2 == "/"){
                        Log.e("entrou nessa ","oxe");
                        String temp3 = nomeUsuarioDestinatario;
                        String array3[] =temp3.split("/");
                        String temp4 = array3[0] ;
                        Log.e("entrou nessa ",temp4);
                        Send.sendGrupo(conection.getChannel(),Singleton.getInstance().getusuario(),
                                temp4,editMensagem.getText().toString());
                    }else{
                        Send.sendUsuario(conection.getChannel(), Singleton.getInstance().getusuario(), nomeUsuarioDestinatario, editMensagem.getText().toString());
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }
                editMensagem.setText("");
                mensagens.add(mensagem);
                adapter.notifyDataSetChanged();
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_group, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.item_adduser:
                String t = nomeUsuarioDestinatario;
                String a[] =t.split("");
                String t2 = a[a.length-1] ;
                Log.e("aaaaaaaaaaaaaaaaa ",t2);

                if (!t2.equals("/")){
                    Log.e("aloo","brasil");
                }else{
                    addUser();
                }

                return true;
            case R.id.item_delUser :
                String t3 = nomeUsuarioDestinatario;
                String a1[] =t3.split("");
                String t4 = a1[a1.length-1] ;
                Log.e("aaaaaaaaaaaaaaaaa ",t4);

                if (!t4.equals("/")){
                    Log.e("aloo","brasil");
                }else{
                    delUser();
                }

                return true;

            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    protected void onStop() {
        super.onStop();

    }
    private void addUser(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConversaActivity.this);
        //Configurações do Dialog
        alertDialog.setTitle("Add User");
        alertDialog.setMessage("Nome do usuário");
        alertDialog.setCancelable(false);
        final EditText editText = new EditText(ConversaActivity.this);

        alertDialog.setView( editText );
        //Configura botões
        alertDialog.setPositiveButton("Add", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = editText.getText().toString();
                //Valida se o e-mail foi digitado
                if( user.isEmpty() ){
                    Toast.makeText(ConversaActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    String ta1 = nomeUsuarioDestinatario;
                    String ar1[] =ta1.split("/");
                    String grupo = ar1[0] ;
                    try {
                        conection.adicionarUsuarioGrupo(user,grupo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }


    private void delUser(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(ConversaActivity.this);
        //Configurações do Dialog
        alertDialog.setTitle("Del User");
        alertDialog.setMessage("Nome do usuário");
        alertDialog.setCancelable(false);
        final EditText editText = new EditText(ConversaActivity.this);

        alertDialog.setView( editText );
        //Configura botões
        alertDialog.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String user = editText.getText().toString();
                //Valida se o e-mail foi digitado
                if( user.isEmpty() ){
                    Toast.makeText(ConversaActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    String ta1 = nomeUsuarioDestinatario;
                    String ar1[] =ta1.split("/");
                    String grupo = ar1[0] ;
                    try {
                        conection.removerUsuarioGrupo(user,grupo);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
            }
        });
        alertDialog.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

            }
        });

        alertDialog.create();
        alertDialog.show();

    }
}
