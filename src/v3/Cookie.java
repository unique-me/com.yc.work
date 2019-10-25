package v3;


public class Cookie {
private String name;//name与value必填 形式 name=value
private String value;
private String path;//path domain不一定要写有默认值，domain默认当前域名，path当前网页所在路径
private String domain;
private  Integer maxAge;//不能用0（表示立即删除它）用Integer（可以为空）不用int（不能为空），不设置就随浏览器其关闭失效

public Cookie(String name,String value){
	super();
	this.name=name;
	this.value=value;
	
}
public String getName() {
	return name;
}
public void setName(String name) {
	this.name = name;
}
public String getValue() {
	return value;
}
public void setValue(String value) {
	this.value = value;
}
public String getPath() {
	return path;
}
public void setPath(String path) {
	this.path = path;
}
public String getDomain() {
	return domain;
}
public void setDomain(String domain) {
	this.domain = domain;
}
public int getMaxAge() {
	return maxAge;
}
public void setMaxAge(int maxAge) {
	this.maxAge = maxAge;
}
@Override
public String toString() {
	String ret="Set-Cookie:"+name+"="+value+"; ";
	ret+=path==null ? "" :("path="+path+"; ");
	ret+=domain==null ? "" :("domain="+domain+"; ");
	ret+=maxAge==null ? "" :("maxAge="+maxAge+"; ");
	return ret;
}

}
