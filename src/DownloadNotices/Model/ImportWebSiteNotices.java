package DownloadNotices.Model;

import Dates.Dates;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chromeclass.SeleniumChrome;
import org.openqa.selenium.wait.wait;
import sql.Database;

public class ImportWebSiteNotices {
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");

    private final SeleniumChrome chrome;
    private final String url;

    //Date
    private String dateAttribute;
    private String dateFormat;
    private Calendar lastAddedDate;

    //By Variables
    private By noticesBy;
    private By dateBy;
    private By hrefBy;
    private By nameBy;

    //List Notices
    private final Notices notices = new Notices();

    public ImportWebSiteNotices(SeleniumChrome chrome, String url) {
        this.chrome = chrome;
        this.url = url;        
    }

    public void setDateAttribute(String dateAttribute) {
        this.dateAttribute = dateAttribute;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public void setNoticesBy(By noticesBy) {
        this.noticesBy = noticesBy;
    }

    public void setDateBy(By dateBy) {
        this.dateBy = dateBy;
    }

    public void setHrefBy(By hrefBy) {
        this.hrefBy = hrefBy;
    }

    public void setNameBy(By nameBy) {
        this.nameBy = nameBy;
    }

    public void getNoticesFromUrl() {
        Integer page = 0;

        while (lastAddedDate == null || lastAddedDate.after(notices.getCalendarLimit())) {
            page++;

            addPageNotices(page);
        }
    }

    private void addPageNotices(Integer page) {
        //Acessa página
        chrome.getChrome().get(url + page);

        //Pega notícias
        WebElement waitElement = wait.element(chrome.getChrome(), noticesBy);
        if (waitElement != null) {
            List<WebElement> elements = chrome.getChrome().findElements(noticesBy);

            //Percorre noticias
            for (WebElement element : elements) {
                //Get Date
                WebElement dateElement = wait.element(chrome.getChrome(), dateBy);
                
                if(dateElement != null){
                    String dateElementAttribute = dateElement.getAttribute(dateAttribute);
                    lastAddedDate = Dates.getCalendarFromFormat(dateElementAttribute, dateFormat);

                    if (lastAddedDate != null && lastAddedDate.after(notices.getCalendarLimit())) {
                        String href = element.findElement(hrefBy).getAttribute("href");
                        String name = element.findElement(nameBy).getAttribute("innerHTML");
                        String dateString = sqlDateFormat.format(lastAddedDate.getTime());

                        notices.add(
                                new String[]{
                                    dateString,
                                    href,
                                    name
                                }
                        );
                    } else {
                        break;
                    }
                }else{
                    //Se não encontrar a data na página e não tiver sido encontrado nenhuma data ainda, para tudo
                    if(lastAddedDate == null){
                        breakLastDate();
                        break;
                    }
                }
            }
        }else{
            //Se nao encontrar noticias e já estiver mais da 3 pagina, para por aqui
            if(page > 3){
                breakLastDate();
            }
        }
    }
    
    private void breakLastDate(){
        lastAddedDate = notices.getCalendarLimit();
        lastAddedDate.add(Calendar.DAY_OF_MONTH, 1);
    }

    public void importToDb(Database banco) {
        notices.importToDb(banco);
    }
}
