package newbuy;

public class freezer { //新品冷蔵庫のデータを管理するリスト
	private int Spec;
	private int Cost;
		
	public int getSpec() {
		return Spec;
	}
	public void setSpec(int spec) {
		Spec = spec;
	}
	public int getCost() {
		return Cost;
	}
	public void setCost(int cost) {
		Cost = cost;
	}

	
	public freezer ( ReadFileFreezer RF, int cost, int spec ) {
		setCost ( cost );
		setSpec ( spec );
		RF.setfreezerlist ( this );
	}
}
