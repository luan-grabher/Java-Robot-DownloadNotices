package Testes;

import static DownloadNotices.DownloadNotices.executar;

public class Testes {
    
    public static void main(String [] args){
        application();
        
        System.exit(0);
    }
    
    public static void application(){
        String nome = "Teste Baixar Noticias";
        System.out.println(executar(nome).replaceAll("<br>", "\n"));
    }
}
