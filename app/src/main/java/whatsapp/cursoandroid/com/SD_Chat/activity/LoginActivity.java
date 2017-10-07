package whatsapp.cursoandroid.com.SD_Chat.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

import whatsapp.cursoandroid.com.SD_Chat.R;
import whatsapp.cursoandroid.com.SD_Chat.helper.Base64Custom;
import whatsapp.cursoandroid.com.SD_Chat.helper.Preferencias;
import whatsapp.cursoandroid.com.SD_Chat.helper.Singleton;
import whatsapp.cursoandroid.com.SD_Chat.model.Usuario;

public class LoginActivity extends AppCompatActivity {

    private EditText email;
    private Button botaoLogar;
    private Usuario usuario;
    private String identificadorUsuarioLogado;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        email = (EditText) findViewById(R.id.edit_login_email);
        botaoLogar = (Button) findViewById(R.id.bt_logar);

        botaoLogar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                usuario = new Usuario();
                usuario.setNome( email.getText().toString() );
                try {
                    validarLogin();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (TimeoutException e) {
                    e.printStackTrace();
                }
            }
        });

    }
    private void validarLogin() throws IOException, TimeoutException {


                    identificadorUsuarioLogado = Base64Custom.codificarBase64(usuario.getNome());
        Singleton.getInstance().setUsuario(usuario.getNome());
                            Preferencias preferencias = new Preferencias(LoginActivity.this);
                            preferencias.salvarDados( identificadorUsuarioLogado, usuario.getNome() );
                    abrirTelaPrincipal();
                    Toast.makeText(LoginActivity.this, "Sucesso ao fazer login!", Toast.LENGTH_LONG ).show();

    }

    private void abrirTelaPrincipal(){
        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
        intent.putExtra("nome", usuario.getNome() );
        startActivity( intent );
        finish();
    }
}
