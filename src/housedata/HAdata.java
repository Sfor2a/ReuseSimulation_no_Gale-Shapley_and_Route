package housedata;

public class HAdata extends HouseElements {
	private int Durability; //その家具の耐久度
	private int MinusDur; //1タームにおける減少耐久度（まずは使わない方法で？？？
	private int MaxValue; //新品での価格
	private String Spec; //スペック　冷蔵庫ならリットル、電子レンジならわっと数など
	private int TermValue; //耐久度減少後の価格（減少度つかわんならこっちもな
	private int Exchangecount; //交換回数（ｒｙ
	private static int HPAIDadd; //ID加算
	private int UseTernCount;
	
	//ゲッターセッター
	public int getDurability() {
		return Durability;
	}
	public void setDurability(int durability) {
		Durability = durability;
	}
	public int getMinusDur() {
		return MinusDur;
	}
	public void setMinusDur(int minusDur) {
		MinusDur = minusDur;
	}
	public int getMaxValue() {
		return MaxValue;
	}
	public void setMaxValue(int maxValue) {
		MaxValue = maxValue;
	}
	public int getTermValue() {
		return TermValue;
	}
	public void setTermValue(int termValue) {
		TermValue = termValue;
	}
	public int getExchangecount() {
		return Exchangecount;
	}
	public void setExchangecount(int exchangecount) {
		Exchangecount = exchangecount;
	}
	public String getSpec() {
		return Spec;
	}
	public void setSpec(String spec) {
		Spec = spec;
	}
	public int getUseTernCount() {
		return UseTernCount; 
	}
	public void setUseTernCount ( int Utc ) {
		UseTernCount = Utc; 
	}
	//ここまでゲッターセッター
	

	public HAdata(String Nam, String spec, int Val, int MD, Housedata HH) { //家具をつくるよ
		ID = HPAIDadd++;
		setExchangecount ( 0 );
		setName ( Nam );
		setDurability ( 100 );
		setTermValue ( Val );
		setMaxValue ( Val );
		setMinusDur ( MD );
		setSpec(spec);
		setUseTernCount ( 0 );
		HH.setHAdata ( this );
	}
	
	public boolean equals(Object obj){
	    HAdata t = ( HAdata )obj;
	    String ts = t.Name;

		if( Name.equals ( ts ) ) return true;
		else return false;
	}
}
