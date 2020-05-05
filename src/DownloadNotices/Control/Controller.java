package DownloadNotices.Control;

import DownloadNotices.Model.ImportEconet;
import DownloadNotices.Model.ImportWebSiteNotices;
import Entity.Executavel;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import java.util.Locale;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromeclass.SeleniumChrome;
import org.openqa.selenium.wait.wait;
import sql.Banco;

public class Controller {

    private SeleniumChrome chrome;
    private Banco banco;
    private String mysqlPath = "\\\\zac\\robos\\Tarefas\\Arquivos\\mysql.cfg";

    public class connectDataBase extends Executavel {

        public connectDataBase() {
            nome = "Conectar ao banco de dados";
        }

        @Override
        public void run() {
            banco = new Banco(mysqlPath);
            if (!banco.testConnection()) {
                throw new Error("Erro ao conectar no banco de dados no arquivo: " + mysqlPath);
            }
        }
    }

    public class openChrome extends Executavel {

        public openChrome() {
            nome = "Abrir navegador Chrome";
        }

        @Override
        public void run() {
            chrome = new SeleniumChrome();
            chrome.abrirChrome("http://google.com.br/");
        }
    }

    public class importNoticesFromFenacon extends Executavel {

        public importNoticesFromFenacon() {
            nome = "Importar noticias Fenacon";
        }

        @Override
        public void run() {
            //URL Variables
            String url = "http://www.fenacon.org.br/noticias/?page=";
            
            ImportWebSiteNotices importation =  new ImportWebSiteNotices(chrome, url);
            
            importation.setDateAttribute("innerHTML");
            importation.setDateFormat("d 'de' MMM 'de' yyyy");
            
            importation.setNoticesBy(By.cssSelector(".info-list-news"));
            
            importation.setDateBy(By.cssSelector("b"));            
            importation.setHrefBy(By.cssSelector("a"));
            importation.setNameBy(By.cssSelector("a h4"));
            
            importation.getNoticesFromUrl();
            importation.importToDb(banco);
        }
    }
    
    public class importNoticesFromSesconRS extends Executavel {

        public importNoticesFromSesconRS() {
            nome = "Importar noticias Sescon RS";
        }

        @Override
        public void run() {
            //URL Variables
            String url = "http://www.sesconrs.com.br/categoria/noticias/page/";
            
            ImportWebSiteNotices importation =  new ImportWebSiteNotices(chrome, url);
            
            importation.setDateAttribute("datetime");
            importation.setDateFormat("yyyy-MM-d");
            
            importation.setNoticesBy(By.cssSelector(".wrap > .box-posts"));
            
            importation.setDateBy(By.cssSelector("a > div span .updated"));            
            importation.setHrefBy(By.cssSelector("a"));
            importation.setNameBy(By.cssSelector("a > p"));
            
            importation.getNoticesFromUrl();
            importation.importToDb(banco);
        }
    }
    
    public class importNoticesFromCRCRS extends Executavel {

        public importNoticesFromCRCRS() {
            nome = "Importar noticias CRCRS";
        }

        @Override
        public void run() {
            //URL Variables
            String url = "http://www.crcrs.org.br/noticias/?paged=";
            
            ImportWebSiteNotices importation =  new ImportWebSiteNotices(chrome, url);
            
            importation.setDateAttribute("datetime");
            importation.setDateFormat("yyyy-MM-d");
            
            importation.setNoticesBy(By.cssSelector(".entry-header"));
            
            importation.setDateBy(By.cssSelector(".entry-date"));            
            importation.setHrefBy(By.cssSelector("a"));
            importation.setNameBy(By.cssSelector("a"));
            
            importation.getNoticesFromUrl();
            importation.importToDb(banco);
        }
    }
    
    public class importNoticesFromContabeis extends Executavel {

        public importNoticesFromContabeis() {
            nome = "Importar noticias site Contabeis";
        }

        @Override
        public void run() {
            //URL Variables
            String url = "https://www.contabeis.com.br/artigos/";
            
            ImportWebSiteNotices importation =  new ImportWebSiteNotices(chrome, url);
            
            importation.setDateAttribute("innerHTML");
            importation.setDateFormat("d/MM/yyyy");
            
            importation.setNoticesBy(By.cssSelector(".texto"));
            
            importation.setDateBy(By.cssSelector("em"));            
            importation.setHrefBy(By.cssSelector("ul"));
            importation.setNameBy(By.cssSelector("h2"));
            
            importation.getNoticesFromUrl();
            importation.importToDb(banco);
        }
    }
    
    public class importNoticesFromEconet extends Executavel {
        private String path;
        public importNoticesFromEconet(String path) {
            this.path = path;
            nome = "Importar sintese Econet " + path;
        }

        @Override
        public void run() {
            //URL Variables
            path = path.toLowerCase();
            
            ImportEconet importation = new ImportEconet(path, chrome.getChrome());
                      
            importation.getNoticesFromUrl();
            importation.importToDb(banco);
        }
    }

    public void closeChrome() {
        chrome.fecharChrome();
    }
}
