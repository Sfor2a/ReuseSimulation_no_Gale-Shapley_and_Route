package housedata;

public class HAsWishValue extends HouseElements {
	private int Durability; //耐久度
	private int Cost; //それに見合う価値
	private String spec;
	private static int WVLNum; //ID加算
	
	//ゲッター・セッター
	public int getDurability() {
		return Durability;
	}
	public void setDurability(int durability) {
		Durability = durability;
	}
	public int getCost() {
		return Cost;
	}
	public void setCost(int cost) {
		Cost = cost;
	}
	public String getSpec() {
		return spec;
	}
	public void setSpec(String spec) {
		this.spec = spec;
	}
	
	public HAsWishValue ( HousesWish WL, int Dur, int cost, String spec ) { //その家具のウィッシュリストだよ
		ID = WVLNum++;
		setDurability ( Dur );
		setCost ( cost );
		setSpec ( spec );
		WL.setHAsWishVal ( this );
	}

}
