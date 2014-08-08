import java.util.ArrayList;
import java.util.Arrays;
import java.util.Enumeration;

import gnu.io.CommPortIdentifier;

/**
 * List the ports.
 * 
 * @author Ian F. Darwin, http://www.darwinsys.com/
 * @version $Id: CommPortLister.java,v 1.4 2004/02/09 03:33:51 ian Exp $
 */
public class CommPortLister {
	ArrayList<String> listed = new ArrayList<String>();

  /** Simple test program. */
  public static void main(String[] ap) {
    new CommPortLister().list();
  }

  /** Ask the Java Communications API * what ports it thinks it has. */
  protected ArrayList<String> list() {
    // get list of ports available on this particular computer,
    // by calling static method in CommPortIdentifier.
    Enumeration pList = CommPortIdentifier.getPortIdentifiers();
    int count = 0;

    // Process the list.
    while (pList.hasMoreElements()) {
      CommPortIdentifier cpi = (CommPortIdentifier) pList.nextElement();
      System.out.print("Port " + cpi.getName() + " ");
      
      listed.add (cpi.getName());
      count++;
      if (cpi.getPortType() == CommPortIdentifier.PORT_SERIAL) {
        System.out.println("is a Serial Port: " + cpi);
      } else if (cpi.getPortType() == CommPortIdentifier.PORT_PARALLEL) {
        System.out.println("is a Parallel Port: " + cpi);
      } else {
        System.out.println("is an Unknown Port: " + cpi);
      }
    }
	return listed;
  }
}