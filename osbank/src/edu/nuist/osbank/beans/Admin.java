package edu.nuist.osbank.beans;



import edu.nuist.osbank.abenablebean.util.DBTableInitor;
import edu.nuist.osbank.dbenablebean.DBEnableBean;
import edu.nuist.osbank.dbenablebean.dbablebeanannotation.SP;



//��һ�������User���ӣ������ݿ����ɾ�Ĳ�
@SP(
		//tableָ��user��
		table="admin", 
		
		//ʹ�õ�sql���
		sps="queryByName : select * from {tableName} where name={name};"
			+ "loginSQL : select * from {tableName} where name={name} and password={password};",
		
		//�Ƚ���
		create = "create table admin (" +
				"id int(11) NOT NULL AUTO_INCREMENT PRIMARY KEY, " +
				"name varchar(255), password varchar(255));"		 
	)

//@SP����һ����ע�����Ǹ��߿��һ���BEANҪ����Щ���ݿ����



public class Admin extends DBEnableBean{

	private String name;
	private String password;
	
	
	
	public Admin() {
		super();
		// TODO Auto-generated constructor stub
	}

	public Admin(String name, String password) {
		super();
		this.name = name;
		this.password = password;
	}

	public String getName() {
		return name;
	}

	public Admin setName(String name) {
		this.name = name;
		return this;
	}

	public String getPassword() {
		return password;
	}

	public Admin setPassword(String password) {
		this.password = password;
		return this;
	}
	
	
	

	public static void main(String[] args) {
		// TODO Auto-generated method stub
		
		
//		User u = new User();
//	 
//		u.setName("test_zhang");
//		u.setPassword("123456");//��ʼ��
//    	new DBTableInitor(User.class, u);//����һ�ű�  
//		
		
//		
    	/* String name = "zzz";
    	 String password="123456";
    	 User u = new User();
    	 u.setName(name).setPassword(password).setEmail("757165407@qq.com");
    	 
    		//new DBTableInitor(User.class, u);//����һ�ű�  
    		
    		
        u.insert();	*/	
//		//�����û�
    	
//    	User u = new User();
//    	u.setName(name);
//    	u.setPassword(password);
//    LinkedList<DBEnableBean> users = u.query("Search");//�õ�sps�е�sql�����	
//     if(users.size() > 0){
//     u = (User)users.get(0);
//     u.delete();
//     }
    //  ɾ���û�
     
     
    //   User u = new User().setName(name).setPassword(password);
    //   LinkedList<DBEnableBean> users = u.query("Search");
    //		if(users.size() > 0){
    //			u = (User)users.get(0);
    //			u.setPassword("1111");
    //			u.setName("cjx1");
    //		u.update();
    //		}
    	//�޸��û�
//	
//    	User u = new User();
//    			u.setName(name);
//    			u.setPassword(password);
//		LinkedList<DBEnableBean> users = u.query("loginSQL");
//		if(users.size() > 0){
//			u = (User)users.get(0);
//			//u.delete();
//			System.out.println( "Login Success!!" );
//		}else{
//			System.out.println( "Login Error!!" );
//		}
////		
		//�����û�
		  
		 Admin u = new Admin();
    	 u.setName("admin").setPassword("admin");
    	 
    		new DBTableInitor(Admin.class, u);
		
	}
	
	
}
