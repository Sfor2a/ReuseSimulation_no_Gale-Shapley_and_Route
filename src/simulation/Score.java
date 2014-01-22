package simulation;

import housedata.HAdata;
import housedata.Housedata;

public class Score {
	private int ScoreforBuy; //買う側のスコア
	private int ScoreforSell; //売る側のスコア
	private Housedata BuyHouse; //買う側の家
	private Housedata SellHouse; //売る側の家
	private HAdata SellHA; //売るための家具
	private int Dist; //距離
	
	//ゲッター・セッター
	public int getScoreforBuy() {
		return ScoreforBuy;
	}
	public void setScoreforBuy(int scoreforBuy) {
		ScoreforBuy = scoreforBuy;
	}
	public int getScoreforSell() {
		return ScoreforSell;
	}
	public void setScoreforSell(int scoreforSell) {
		ScoreforSell = scoreforSell;
	}
	public Housedata getBuyHouse() {
		return BuyHouse;
	}
	public void setBuyHouse(Housedata buyHouse) {
		BuyHouse = buyHouse;
	}
	public Housedata getSellHouse() {
		return SellHouse;
	}
	public void setSellHouse(Housedata sellHouse) {
		SellHouse = sellHouse;
	}
	public HAdata getSellHA() {
		return SellHA;
	}
	public void setSellHA(HAdata sellHA) {
		SellHA = sellHA;
	}
	public int getDist() {
		return Dist;
	}
	public void setDist(int dist) {
		Dist = dist;
	}
	public Score ( int scoreBuy, int scoreSell, Housedata buy1, Housedata sell1, HAdata setSellTargetHA, Simulator simulator, int dist ) { //スコアの生成
		setScoreforBuy ( scoreBuy );
		setScoreforSell ( scoreSell );
		setBuyHouse ( buy1 );
		setSellHouse ( sell1 );
		setSellHA ( setSellTargetHA );
		simulator.setScoreList ( this );
		setDist ( dist );
	}
}

@SuppressWarnings("rawtypes")
class BuyScoreComparator implements java.util.Comparator {
	public int compare ( Object s, Object t ) {
		return ( ( Score ) s ).getScoreforBuy() - ( ( Score ) t ).getScoreforBuy();
	}
}

@SuppressWarnings("rawtypes")
class SellScoreComparator implements java.util.Comparator {
	public int compare ( Object s, Object t ) {
		return ( ( Score ) s ).getScoreforSell() - ( ( Score ) t ).getScoreforSell();
	}
}
