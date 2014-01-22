package mapdata;

public class Map_Elements {
	int ID; //管理用ID
	String Name; //各々の名前
	
	//そのゲッター
	public int getID () {
		return ID;
	}
	public String getName () {
		return Name;
	}
	//ここまでゲッター
	
	//そのセッター
	public void setID ( int iD ) {
		ID = iD;
	}
	public void setName ( String name ) {
		Name = name;
	}
	//ここまでセッター
}
