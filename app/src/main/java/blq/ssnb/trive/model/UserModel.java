package blq.ssnb.trive.model;

import java.util.HashMap;
import java.util.Map;

public class UserModel {

	private String email;
	private int gender=0;//0是男的，1是女的
	private int	resident=0;//0是本土 1是游客
	private int age;
	private String birth_country;
	private int	driver_licence;
	private int employment;
	private int studying;
	private int workspace=-1;
	private String occupation;
	private int	study_level=-1;
	private int other_activity=-1;
	private int industry=-1;
	
	public int getIndustry() {
		return industry;
	}
	public void setIndustry(int industry) {
		this.industry = industry;
	}
	public String getEmail() {
		return email;
	}
	public void setEmail(String email) {
		this.email = email;
	}
	public int getGender() {
		return gender;
	}
	public void setGender(int gender) {
		this.gender = gender;
	}
	public int getResident() {
		return resident;
	}
	public void setResident(int resident) {
		this.resident = resident;
	}
	public int getAge() {
		return age;
	}
	public void setAge(int age) {
		this.age = age;
	}
	public String getBirth_country() {
		return birth_country;
	}
	public void setBirth_country(String birth_country) {
		this.birth_country = birth_country;
	}
	public int getDriver_licence() {
		return driver_licence;
	}
	public void setDriver_licence(int driver_licence) {
		this.driver_licence = driver_licence;
	}
	public int getEmployment() {
		return employment;
	}
	public void setEmployment(int employment) {
		this.employment = employment;
	}
	public int getStudying() {
		return studying;
	}
	public void setStudying(int studying) {
		this.studying = studying;
	}
	public int getWorkspace() {
		return workspace;
	}
	public void setWorkspace(int workspace) {
		this.workspace = workspace;
	}
	public String getOccupation() {
		return occupation;
	}
	public void setOccupation(String occupation) {
		this.occupation = occupation;
	}
	public int getStudy_level() {
		return study_level;
	}
	public void setStudy_level(int study_level) {
		this.study_level = study_level;
	}
	public int getOther_activity() {
		return other_activity;
	}
	public void setOther_activity(int other_activity) {
		this.other_activity = other_activity;
	}
	
	public Map<String, String> getMap(){
		Map<String,String> map = new HashMap<String, String>();
		map.put("email", email);
		map.put("gender", gender+"");
		map.put("resident", resident+"");
		map.put("age", age+"");
		map.put("birth_country", birth_country);
		map.put("driver_licence", driver_licence+"");
		map.put("employment", employment+"");
		map.put("studying", studying+"");
		map.put("workspace", workspace+"");
		map.put("occupation", occupation);
		map.put("study_level", study_level+"");
		map.put("other_activity", other_activity+"");
		map.put("industry",industry+"");
		return map;
	}

}
