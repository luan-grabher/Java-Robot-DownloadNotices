package DownloadNotices;

import DownloadNotices.Control.Controller;
import Entity.Executavel;
import Executor.Execution;
import Robo.AppRobo;
import java.util.ArrayList;
import java.util.List;

public class DownloadNotices {
  public static void main(String [] args){
      
        String nome = "Baixar Noticias";
        
        AppRobo robo = new AppRobo(nome);               
        
        robo.definirParametros();
                
        robo.executar(
                executar(nome)
        );
        
        System.exit(0);
  }
  
  public static String executar(String nome){
      String r = "";
      try {
          Controller controller = new Controller();
          
          List<Executavel> execs = new ArrayList<>();
          execs.add(controller.new connectDataBase());
          execs.add(controller.new openChrome());
          execs.add(controller.new importNoticesFromFenacon());
          execs.add(controller.new importNoticesFromSesconRS());
          execs.add(controller.new importNoticesFromCRCRS());
          execs.add(controller.new importNoticesFromContabeis());
          execs.add(controller.new importNoticesFromContabilidadeNaTV());
          execs.add(controller.new importNoticesFromEconet("trabalhista"));
          execs.add(controller.new importNoticesFromEconet("federal"));
          execs.add(controller.new importNoticesFromEconet("rio_grande_do_sul"));
          execs.add(controller.new importNoticesFromEconet("portoalegre"));
          
          Execution exec = new Execution(nome);
          exec.setExecutables(execs);
          exec.runExecutables();
          
          r = exec.getRetorno();
            
          controller.closeChrome();
      } catch (Exception e) {
      }
      
      return r;
  }

}
