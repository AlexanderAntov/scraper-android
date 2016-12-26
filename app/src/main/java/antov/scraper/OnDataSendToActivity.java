package antov.scraper;

import java.util.ArrayList;
import antov.scraper.Models.NewsDataObject;

public interface OnDataSendToActivity {
    public void sendData(ArrayList<NewsDataObject> list);
}
