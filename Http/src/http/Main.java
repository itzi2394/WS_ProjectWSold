/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package http;

import java.io.IOException;
import java.net.URLEncoder;

public class Main {
    public static void main(String[] args) throws IOException {
        Arquivo arquivo = new Arquivo();
        arquivo.dado = URLEncoder.encode("Testando mensagem deaplicativo local", "UTF-8");
        arquivo.nome = URLEncoder.encode("Nome teste 1", "UTF-8");
        Manip upDown = new Manip();
        upDown.enviarArquivo(arquivo);
        upDown.receberArquivo();
}
}