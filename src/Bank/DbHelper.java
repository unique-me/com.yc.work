package Bank;

import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

//import com.yc.commons.MyProperties;

import oracle.sql.BLOB;

/**
 * @author JY TANG
 *
 */
public class DbHelper {

	private Connection conn;
	private PreparedStatement pstmt;
	private ResultSet rs;

	static {
		// 加载驱动
		try {
			Class.forName(MyProperties.getInstance().getProperty("driverName"));
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	public Connection getConn() throws SQLException {
		conn = DriverManager.getConnection(MyProperties.getInstance().getProperty("url"),
				MyProperties.getInstance());
		
		return conn;
	}
	

	//关闭资源
	public void closeAll(Connection conn, PreparedStatement pstmt, ResultSet rs) {
		// 关闭资源
		if (rs != null) {
			try {
				rs.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if (null != pstmt) {
			try {
				pstmt.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
		if (null != conn) {
			try {
				conn.close();
			} catch (SQLException e) {
				// TODO 自动生成的 catch 块
				e.printStackTrace();
			}
		}
	}
	
	//批处理操作
	public int update(List<String >sql,List<List<Object>> params) throws SQLException {
		int result=0;
		try {
			conn=getConn();
			//设置事务手动提交
			conn.setAutoCommit(false);
			//循环sql语句
			if(null ==sql ||sql.isEmpty()) {	
				return result;
			}
			for (int i=0;i<sql.size();i++) {
				//获取sql语句并创建预编译对象
				pstmt=conn.prepareStatement(sql.get(i));
				//获取对应的sql语句参数集合
				List<Object> param=params.get(i);
				//设置参数
				setParamsList(pstmt,param);
				//执行更新
				result =pstmt.executeUpdate();
				if (result<=0) {
					return result;
				}
			}
			conn.commit();
			
		} catch (SQLException e) {
			// TODO 自动生成的 catch 块
			e.printStackTrace();
			conn.rollback();
			result=0;
		}finally {
			//还原事务的状态
			conn.setAutoCommit(true);
			closeAll(conn, pstmt, null);
		}
		
		
		return result;
	}

	/*
	 * 返回多条记录操作查询 select * from table
	 * 
	 */

	//传入的第一个参数为sql语句，第二个参数是为了给？赋值
	public List<Map<String, Object>> selectMutil(String sql, List<Object> params) throws SQLException, IOException {
		List<Map<String, Object>> list = new ArrayList<Map<String, Object>>();
		Map<String, Object> map = null;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);

			// 设置参数
			setParamsList(pstmt, params);
			// 获取结果集
			rs = pstmt.executeQuery();
			// 根据结果集对象获取到所有结果集中所有列名
			List<String> columnNames = getAllColumnNames(rs);
			while (rs.next()) {
				map = new HashMap<String, Object>();
				String typeName = null; // 值的类型
				Object obj = null; // 获取的值
				// 循环所有的列名
				for (String name : columnNames) {
					obj = rs.getObject(name);
					if (null != obj) {
						typeName = obj.getClass().getName();
					}
					if ("oracle.sql.BLOB".equals(typeName)) {
						// 对图片进行处理
						BLOB blob = (BLOB) obj;
						InputStream in = blob.getBinaryStream();
						byte[] bt = new byte[(int) blob.length()];
						in.read(bt);
						map.put(name, bt); // 将blob类型值以字节数组的形式存储
					} else {
						map.put(name, obj);
					}

				}
				list.add(map); 
			}
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return list;
	}

	/*
	 * 单记录的查询 select * from table_name where id=? sql 查询的sql语句 params 传入的参数 集合中
	 * 集合的参数的顺序必须和？一致
	 * 
	 */
	public Map<String, Object> selectSingle(String sql, List<Object> params) throws SQLException, IOException {
		Map<String, Object> map = null;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);

			// 设置参数
			setParamsList(pstmt, params);
			// 获取结果集
			rs = pstmt.executeQuery();
			// 根据结果集对象获取到所有结果集中所有列名
			List<String> columnNames = getAllColumnNames(rs);
			while (rs.next()) {
				map = new HashMap<String, Object>();
				String typeName = null; // 值的类型
				Object obj = null; // 获取的值
				// 循环所有的列名
				for (String name : columnNames) {
					obj = rs.getObject(name);
					if (null != obj) {
						typeName = obj.getClass().getName();
					}
					if ("oracle.sql.BLOB".equals(typeName)) {
						// 对图片进行处理
						BLOB blob = (BLOB) obj;
						InputStream in = blob.getBinaryStream();
						byte[] bt = new byte[(int) blob.length()];
						in.read(bt);
						map.put(name, bt); // 将blob类型值以字节数组的形式存储
					} else {
						map.put(name, obj);
					}

				}

			}
		} finally {
			closeAll(conn, pstmt, rs);
		}
		return map;
	}
	
	
	// 获取查询后的字段名
	private List<String> getAllColumnNames(ResultSet rs) throws SQLException {
		List<String> list = new ArrayList<String>();
		ResultSetMetaData data = rs.getMetaData();
		int count = data.getColumnCount();
		for (int i = 1; i <= count; i++) {
			String str=data.getColumnName(i);
			// 添加列名到list集合中
			list.add(str);

		}
		return list;
	}

//将集合设置到预编译对象中
	private void setParamsList(PreparedStatement pstmt, List<Object> params) throws SQLException {
		if (null == params || params.size() <= 0) {
			return;
		}

		for (int i = 0; i < params.size(); i++) {
			pstmt.setObject(i + 1, params.get(i));
		}
	}

	/*
	 * 更新操作 sql 更新语句 params 传入的参数 不定长的对象数组 传入的参数的顺序宇？一致
	 */
	public int Update(String sql, Object... params) {
		int result = 0;
		try {
			conn = getConn();
			pstmt = conn.prepareStatement(sql);
			// 设置参数
			setParamsObject(pstmt, params);
			// 执行
			result = pstmt.executeUpdate();
		} catch (SQLException e) {

			e.printStackTrace();
		} finally {
			closeAll(conn, pstmt, null);
		}

		return result;
	}

	private void setParamsObject(PreparedStatement pstmt, Object... params) throws SQLException {
		if (null == params || params.length <= 0) {
			return;
		}
		for (int i = 0; i < params.length; i++) {
			pstmt.setObject(i + 1, params[i]); // 将数组中的第个元素的值设置为第i+1个问号
		}

	}
	
	
	/**
	 * 聚合函数操作select count(*)from table_name
	 * @param sql
	 * @param params
	 * @return
	 * @throws Exception
	 */
	
	public double getPolymer(String sql,List<Object> params) throws Exception{
		double result=0;
		try{
			conn=getConn();
			pstmt=conn.prepareStatement(sql);
			setParamsList(pstmt,params);
			rs=pstmt.executeQuery();
			if(rs.next()){
				result=rs.getDouble(1);
			}
		}finally{
			closeAll(conn,pstmt,rs);
		}
		
		return result;
	}
/**
 * 返回一行记录
 * @param sql
 * @param params
 * @param cls
 * @return
 * @throws SQLException
 * @throws InstantiationException
 * @throws IllegalAccessException
 * @throws IllegalArgumentException
 * @throws InvocationTargetException
 */

	
public <T> T findSingle(String  sql,List<Object>params,Class<T>cls) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	T t=null;
	try {
		conn=getConn();
		pstmt=conn.prepareStatement(sql);
	//设置参数
		setParamsList(pstmt,params);
		rs=pstmt.executeQuery();
	//通过反射获取实体类中所给所有方法
	Method [] methods=cls.getDeclaredMethods();
	List<String> columnNames=getAllColumnNames(rs);
	Object obj=null;
	if(rs.next()){
		//创建对象  通过反射
		t=cls.newInstance();//默认调用对象的无参数的构造函数
		//循环列
		for(String name :columnNames){
			obj=rs.getObject(name);//获取值ֵ
			//循环方法 set+name setUname
			for(Method m: methods){
				if(("set"+name).equalsIgnoreCase(m.getName())){
					//set方法的形参类型进行判断， set 方法的形参的数据类型
					
					String typeName=m.getParameterTypes()[0].getName();
					if("java.lang.Integer".equals(typeName)){
						m.invoke(t, rs.getInt(name));//激活此方法 传入的参数必须和底层方法的数据类型一致
					}else if("java.lang.Double".equals(typeName)){
						m.invoke(t, rs.getDouble(name));
					}else if("java.lang.Float".equals(typeName)){
						m.invoke(t, rs.getDouble(name));
					}else if("java.lang.Long".equals(typeName)){
						m.invoke(t, rs.getLong(name));
					}else{
						m.invoke(t, rs.getString(name));
					}
					
				}
			}
			
			
		}
	}
}	finally{
	closeAll(conn,pstmt,rs);
}
	return t;
}

/**
 *查询语句 返回多条记录
 * @throws SQLException 
 * @throws IllegalAccessException 
 * @throws InstantiationException 
 * @throws InvocationTargetException 
 * @throws IllegalArgumentException 
 */

public <T>List<T> findMutil(String sql,List<Object> params,Class<T> cls) throws SQLException, InstantiationException, IllegalAccessException, IllegalArgumentException, InvocationTargetException{
	List<T> list=new ArrayList<T>();
	T t=null;
	try{
		conn=getConn();
		pstmt=conn.prepareStatement(sql);
	//设置参数
		setParamsList(pstmt,params);
		rs=pstmt.executeQuery();
	//通过反射获取实体类中给的所有方法
	Method [] methods=cls.getDeclaredMethods();
	List<String> columnNames=getAllColumnNames(rs);
	Object obj=null;
	while(rs.next()){
		//创建对象 通过反射
		t=cls.newInstance();//默认调用对象的无参数的构造函数
		//循环列
		for(String name: columnNames){
			obj=rs.getObject(name);
			//循环方法 set+name setUname
			for(Method m:methods){
			if(("set"+name).equalsIgnoreCase(m.getName())){
				//循环方法 set+name setUname
				String typeName=m.getParameterTypes()[0].getName();
				if("java.lang.Integer".equals(typeName)){
					m.invoke(t, rs.getInt(name));//激活此方法 传入的参数必须和底层方法的数据类型一致
				}else if("java.lang.Double".equals(typeName)){
					m.invoke(t, rs.getDouble(name));
				}else if("java.lang.Float".equals(typeName)){
					m.invoke(t, rs.getDouble(name));
				}else if("java.lang.Long".equals(typeName)){
					m.invoke(t, rs.getLong(name));
				}else{
					m.invoke(t, rs.getString(name));
				}
			
		}
			}
	}
		//将对象添加到List集合中
		
	list.add(t);	
	}
	}finally{
		closeAll(conn,pstmt,rs);
	}
	return list;
	}

	}



