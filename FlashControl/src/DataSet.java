import java.io.Serializable;
import java.util.Hashtable;


public class DataSet implements Serializable{

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Hashtable<Integer, Integer> values = new Hashtable<Integer, Integer>();
	private String name;
	
	public DataSet(String filename, Hashtable<Integer,Integer> data){
		name = filename;
		values = data;
	}
	
	public Hashtable getData(){
		return values;
	}
	
	public void setData(Hashtable data){
		values = data;
	}

}
