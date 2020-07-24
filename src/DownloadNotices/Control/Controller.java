package DownloadNotices.Control;

import DownloadNotices.Model.ImportEconet;
import DownloadNotices.Model.ImportWebSiteNotices;
import Entity.Executavel;
import org.openqa.selenium.By;
import org.openqa.selenium.chromeclass.SeleniumChrome;
import sql.Database;

public class Controller {

    private SeleniumChrome chrome;
    private Database banco;
    private String mysqlPath = "\\\\zac\\robos\\Tarefas\\Arquivos\\mysql.cfg";

    /**
     * Cria conexão com o banco de dados ZAC
     */
    public class connectDataBase extends Executavel {

        public connectDataBase() {
            name =  "Conectar ao banco de dados";
        }

        @Override
        public void run() {
            banco = new Database(mysqlPath);
            if (!banco.testConnection()) {
                throw new Error("Erro ao conectar no banco de dados no arquivo: " + mysqlPath);
            }
        }
    }

    /**
     * Abre o navegador
     */
    public class openChrome extends Executavel {

        public openChrome() {
            name =  "Abrir navegador Chrome";
        }

        @Override
        public void run() {
            chrome = new SeleniumChrome();
            chrome.abrirChrome("http://google.com.br/");
        }
    }

    /**
     * Importa notícias do Fenacon
     */
    public class importNoticesFromFenacon extends Executavel {

        public importNoticesFromFenacon() {
            name =  "Importar noticias Fenacon";
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
    
    /**
     * Importa notícias do SesconRs
     */
    public class importNoticesFromSesconRS extends Executavel {

        public importNoticesFromSesconRS() {
            name =  "Importar noticias Sescon RS";
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
    
    /**
     * Importa notícias do CRCRS
     */
    public class importNoticesFromCRCRS extends Executavel {

        public importNoticesFromCRCRS() {
            name =  "Importar noticias CRCRS";
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
    
    /**
     * Importa notícias do Contabeis
     */
    public class importNoticesFromContabeis extends Executavel {

        public importNoticesFromContabeis() {
            name =  "Importar noticias site Contabeis";
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
    
    /**
     * Importa notícias do Contabeis
     */
    public class importNoticesFromContabilidadeNaTV extends Executavel {

        public importNoticesFromContabilidadeNaTV() {
            name =  "Importar noticias site Contabilidade na TV";
        }

        @Override
        public void run() {
            //URL Variables
            String url = "https://www.contabilidadenatv.com.br/categoria/noticias-contabeis/page/";
            
            ImportWebSiteNotices importation =  new ImportWebSiteNotices(chrome, url);
            
            importation.setDateAttribute("datetime");
            importation.setDateFormat("yyyy-MM-d");
            
            importation.setNoticesBy(By.cssSelector(".td_module_10"));
            
            
            importation.setDateBy(By.cssSelector("div.item-details > div.td-module-meta-info > span.td-post-date > time"));            
            importation.setHrefBy(By.cssSelector("div.item-details > h3 > a"));
            importation.setNameBy(By.cssSelector("div.item-details > h3 > a"));
            
            importation.getNoticesFromUrl();
            importation.importToDb(banco);
        }
    }
    
    /**
     * Importa notícias do Econet
     */
    public class importNoticesFromEconet extends Executavel {
        private String path;
        public importNoticesFromEconet(String path) {
            this.path = path;
            name =  "Importar sintese Econet " + path;
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

    /**
     * Fecha o Chrome
     */
    public void closeChrome() {
        chrome.fecharChrome();
    }
}
