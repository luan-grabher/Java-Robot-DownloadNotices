package DownloadNotices.Model;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import sql.Banco;

public class Notices {
    private final Integer limitDays = 10;
    private final Calendar calendarLimit = Calendar.getInstance();

    private List<String[]> notices = new ArrayList<>();

    public Notices() {
        calendarLimit.add(Calendar.DAY_OF_MONTH, - limitDays);
    }

    public Calendar getCalendarLimit() {
        return calendarLimit;
    }   

    public String[] add(String[] notice) {
        notices.add(notice);
        return notice;
    }

    public void importToDb(Banco banco) {
        //For each notices
        for (String[] notice : notices) {
            if (notice.length == 3) {
                //Get attributes of notice
                String date = notice[0];
                String href = notice[1];
                String name = notice[2];
                String defaultState = "espera";

                //Verify exists notice
                String sqlVerifyNoticeExists = "select * from noticias where link_noticia = '" + href + "'";
                if (banco.select(sqlVerifyNoticeExists).isEmpty()) {

                    //import
                    String sql_insert = "insert into noticias(data_noticia,nome_noticia,link_noticia,status)"
                            + " values('" + date + "','" + name + "','" + href + "','" + defaultState + "');";
                    banco.query(sql_insert);
                }

                System.out.println(date + " - " + name + " - " + href);
            }
        }
    }   
    
}
