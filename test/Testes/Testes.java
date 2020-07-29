package Testes;

import DownloadNotices.Control.Controller;
import static DownloadNotices.DownloadNotices.executar;

public class Testes {
    
    public static void main(String [] args){
        testSite();
        
        System.exit(0);
    }
    
    public static void application(){
        String nome = "Teste Baixar Noticias";
        System.out.println(executar(nome).replaceAll("<br>", "\n"));
    }
    
    public static void testSite(){
            Controller controller = new Controller();
            
            controller.new connectDataBase().run();
            controller.new openChrome().run();
            controller.new importNoticesFromSesconRS().run();
    }
}
