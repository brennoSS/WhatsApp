package whatsapp.cursoandroid.com.SD_Chat.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.StrictMode;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

import whatsapp.cursoandroid.com.SD_Chat.R;
import whatsapp.cursoandroid.com.SD_Chat.adapter.TabAdapter;
import whatsapp.cursoandroid.com.SD_Chat.helper.Singleton;
import whatsapp.cursoandroid.com.SD_Chat.helper.SlidingTabLayout;
import whatsapp.cursoandroid.com.SD_Chat.model.Conection;
import whatsapp.cursoandroid.com.SD_Chat.model.Contato;
import whatsapp.cursoandroid.com.SD_Chat.model.Conversa;
import whatsapp.cursoandroid.com.SD_Chat.model.Usuario;

public class MainActivity extends AppCompatActivity {

    private Toolbar toolbar;
    private SlidingTabLayout slidingTabLayout;
    private ViewPager viewPager;
    Conection conection = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Usuario usuario = new Usuario();
        if (android.os.Build.VERSION.SDK_INT > 9) {
            StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder().permitAll().build();
            StrictMode.setThreadPolicy(policy);
        }
        try {
            conection = new Conection();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();

        }
        Bundle extra = getIntent().getExtras();

        if( extra != null ) {
            usuario.setNome(extra.getString("nome"));
            try {
                conection.criarUsuario(usuario.getNome());
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("SD-CHAT");
        setSupportActionBar(toolbar);

        slidingTabLayout = (SlidingTabLayout) findViewById(R.id.stl_tabs);
        viewPager = (ViewPager) findViewById(R.id.vp_pagina);

        //Configurar sliding tabs
        slidingTabLayout.setDistributeEvenly(true);
        slidingTabLayout.setSelectedIndicatorColors(ContextCompat.getColor(this,R.color.colorAccent));

        //Configurar adapter
        TabAdapter tabAdapter = new TabAdapter( getSupportFragmentManager() );
        viewPager.setAdapter(tabAdapter);

        slidingTabLayout.setViewPager(viewPager);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        switch ( item.getItemId() ){
            case R.id.item_sair :
                deslogarUsuario();
                return true;
            case R.id.item_configuracoes :
                abrirCadastroGrupo();
                return true;
            case R.id.item_deletar:
                deletarGrupo();
                return true;
            case R.id.item_adicionar :
                abrirCadastroContato();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    private void abrirCadastroGrupo(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //Configurações do Dialog
        alertDialog.setTitle("Novo Grupo");
        alertDialog.setMessage("Nome do Grupo");
        alertDialog.setCancelable(false);
        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView( editText );
        //Configura botões
        alertDialog.setPositiveButton("Criar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String grupo = editText.getText().toString();
                //Valida se o e-mail foi digitado
                if( grupo.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        conection.criarGrupo(Singleton.getInstance().getusuario(),grupo);
                        conection.adicionarUsuarioGrupo("brenno",grupo);
                        ArrayList<Conversa>  conversas = new ArrayList<Conversa>();
                        Conversa conversa = new Conversa();
                        conversas = Singleton.getInstance().getListaConversa();
                        conversa.setNome(grupo+"/");
                        Singleton.getInstance().setGrupo(grupo+"/");

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

    private void deletarGrupo(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //Configurações do Dialog
        alertDialog.setTitle("Deletar Grupo");
        alertDialog.setMessage("Nome do Grupo");
        alertDialog.setCancelable(false);
        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView( editText );
        //Configura botões
        alertDialog.setPositiveButton("Deletar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                final String grupo = editText.getText().toString();
                //Valida se o e-mail foi digitado
                if( grupo.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    try {
                        conection.deletarGrupo(grupo);

                        Singleton.getInstance().setDeletar(grupo+"/");

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

    private void addUser(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //Configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("Nome do usuário");
        alertDialog.setCancelable(false);
        final EditText editText = new EditText(MainActivity.this);
        final EditText editText2 = new EditText(MainActivity.this);
        alertDialog.setView( editText );
        alertDialog.setView( editText2 );

        //Configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                String Contato1 = editText.getText().toString();
                //Valida se o e-mail foi digitado
                if( Contato1.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    Contato contato = new Contato();
                    contato.setNome(Contato1);
                    Singleton.getInstance().setContatoUM(contato);
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
    private void abrirCadastroContato(){

        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        //Configurações do Dialog
        alertDialog.setTitle("Novo contato");
        alertDialog.setMessage("Nome do usuário");
        alertDialog.setCancelable(false);

        final EditText editText = new EditText(MainActivity.this);
        alertDialog.setView( editText );
        //Configura botões
        alertDialog.setPositiveButton("Cadastrar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                 String Contato1 = editText.getText().toString();
                //Valida se o e-mail foi digitado
                if( Contato1.isEmpty() ){
                    Toast.makeText(MainActivity.this, "Preencha o nome", Toast.LENGTH_LONG).show();
                }else{
                    Contato contato = new Contato();
                    contato.setNome(Contato1);
                              Singleton.getInstance().setContatoUM(contato);
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

    private void deslogarUsuario(){

        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        startActivity(intent);
        finish();
    }
}
