package whatsapp.cursoandroid.com.SD_Chat.model;

/**
 * Created by Brenno on 02/10/2017.
 */

/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
        import android.os.Build;

        import java.io.IOException;
        import java.util.Scanner;
        import java.util.concurrent.TimeoutException;
/**
 *
 * @author Brenno
 */
public class ChatSD {
    public static String prompt = ">> ";
    public static String destino = "";
    public static String codigo =">>";
    public static String linha = "";

    static public void main(String args[]) throws IOException, TimeoutException, InterruptedException {
        Scanner scan = new Scanner(System.in);
        Conection conection = new Conection();
        System.out.print("User: ");
        String usuario = scan.nextLine();
        conection.criarUsuario(usuario);
        System.out.print(">> ");
       // Recv receiver = new Recv(conection.getChannel(), usuario);
        //Thread receiving = new Thread(receiver);
        //receiving.start();
        //Thread.sleep(300);
        linha = scan.nextLine();
        while (true){
            switch (linha.split(" ")[0]) {

                case "!addGroup":
                    conection.criarGrupo(usuario, linha.split(" ")[1]);
                    break;
                case "!delGroup":
                    conection.deletarGrupo(linha.split(" ")[1]);
                    break;
                case "!addUser":
                    conection.adicionarUsuarioGrupo(linha.split(" ")[1], linha.split(" ")[2]);
                    break;
                case "!delUser":
                    conection.removerUsuarioGrupo(linha.split(" ")[1], linha.split(" ")[2]);
                    break;
                default:
                    if ("@".equals(linha.substring(0, 1))) {
                        codigo = "@";
                        destino = linha.substring(1, linha.length());
                        conection.queueDeclare(destino);
                        prompt = destino;

                    }else{
                        if ("#".equals(linha.substring(0, 1))) {
                            codigo = "*";
                            destino = linha.substring(1, linha.length());
                            conection.queueDeclare(destino);
                            prompt = destino + codigo;
                        }else{
                            if (codigo != ">>") {
                                if (codigo == "@") {
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                                        Send.sendUsuario(conection.getChannel(), usuario, destino, linha);
                                    }
                                } else {
                                    Send.sendGrupo(conection.getChannel(), usuario, destino, linha);
                                }

                            } else {
                                destino = "";
                                System.out.println("Informe destinatario");
                            }
                        }
                    }
                    break;
            }
            if(codigo.equals("*")){
                System.out.print(destino + "*>> ");
            }else{
                if(codigo.equals("@")){
                    System.out.print(destino + ">> ");
                }else{
                    System.out.print(">> ");
                }
            }
            linha = scan.nextLine();
        }
    }
}
