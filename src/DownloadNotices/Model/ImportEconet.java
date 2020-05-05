package DownloadNotices.Model;

import Dates.Dates;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.List;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import sql.Banco;

public class ImportEconet {

    private String url = "http://www.econeteditora.com.br/links_pagina_inicial/";
    private final WebDriver driver;
    
    //Date
    private final String dateFormat = "dd/MM/yyyy";
    private final SimpleDateFormat sqlDateFormat = new SimpleDateFormat("yyyy-MM-dd");
    
    //Bys
    private final By elementsBy = By.cssSelector("center *");
    private final By titleBy = By.cssSelector("b > span");
    private final By hrefBy = By.cssSelector("p > a");
    private final By dateBy = By.cssSelector("font");
    
    private final Notices notices = new Notices();

    public ImportEconet(String path, WebDriver driver) {
        this.url += path + ".php";
        this.driver = driver;
    }

    public void getNoticesFromUrl() {
        Calendar lastDate = Calendar.getInstance();
        String href = "";
        String title = "";
        
        //Acessa Link
        driver.get(url);
        
        List<WebElement> elements =  driver.findElements(elementsBy);
        //Percorre de cima para baixo os elementos
        for (WebElement element : elements) {
            //Enquanto a data atual estiver depois da data limite
            if(lastDate.after(notices.getCalendarLimit())){
                //Se tiver o elemento de data
                if(!element.findElements(dateBy).isEmpty()){
                    String dateInnerHtml = element.findElement(dateBy).getAttribute("innerHTML").replaceAll("\n", "");
                    
                    //Se estiver no formato estipulado
                    if(Dates.isDateInThisFormat(dateInnerHtml, "dd/MM/yyyy")){
                        lastDate = Dates.getCalendarFromFormat(dateInnerHtml, dateFormat);
                    }
                }
                
                //Se tiver o elemento de titulo
                if(!element.findElements(titleBy).isEmpty()){
                    String titleString = element.findElement(titleBy).getAttribute("innerHTML");
                    
                    title += (title.equals("")?"":" ") + titleString;
                }
                
                //Se tiver o elemento de href
                if(!element.findElements(hrefBy).isEmpty()){
                    WebElement hrefElement = element.findElement(hrefBy);
                    
                    title += (title.equals("")?"":" ") + hrefElement.getAttribute("innerHTML");
                    href = hrefElement.getAttribute("href");
                }
                
                //Se tiver o href
                if(!href.equals("") & !title.equals("")){
                    notices.add(
                            new String[]{
                                sqlDateFormat.format(lastDate.getTime()),
                                title,
                                href
                            }
                    );
                    
                    href = "";
                    title = "";
                }
            }else{
                break;
            }
        }
    }

    public void importToDb(Banco banco) {
        notices.importToDb(banco);
    }

}
