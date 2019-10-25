package Bank;

import java.io.IOException;
import java.util.Properties;

@SuppressWarnings("serial")
public class MyProperties extends Properties {

	private static MyProperties instance=null;
	private MyProperties() 
	{
		try {
			this.load(MyProperties.class.getClassLoader().getResourceAsStream("db.properties"));
		} catch (IOException e) {
			// TODO 自动生成�? catch �?
			e.printStackTrace();
		}
	}
	
	public static MyProperties getInstance() {
			instance = new MyProperties();
			return instance;
	}

	
}
