package Bank;

public class BankDAO {
public void update(String cardno,float money){
	DbHelper db=new DbHelper();
	String sql="update account set balance=balance + ? where accountid=?";
  db.Update(sql,cardno,money);
	
}
}
