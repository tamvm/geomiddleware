package geomobility.core.net;

import java.util.Hashtable;
public class HttpData {
      public String content;
      public Hashtable cookies = new Hashtable();
      public Hashtable headers = new Hashtable();
      public boolean isChange = true;
      public int responseCode;
}
