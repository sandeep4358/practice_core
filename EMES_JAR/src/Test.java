import java.text.SimpleDateFormat;
import java.util.Date;

public class Test {
  public static void main() {
	  //MM/DD/YYYY HH24:MI:SS
	  
	  SimpleDateFormat formatter= new SimpleDateFormat("MM/DD/YYYY HH24:MI:SS");  //EMES-11484
		Date currentDate = new Date();							
		System.out.println(formatter.format(currentDate));
  }
}
