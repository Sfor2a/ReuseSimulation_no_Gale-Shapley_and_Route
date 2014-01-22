package mapdata;

import simulation.Simulator;


public class Routedata extends Map_Elements {
	private Point From; //どこからきたの
	private Point To; //どこへいくの
	private int MoveCosts; //移動コスト
	private static int RouteNum = 0; //加算用変数
	
	//セッター部
	public void setFrom ( Point from ) { //Fromのセッター
		From = from;
	}
	public void setTo ( Point to ) { //Toのセッター
		To = to;
	}
	public void setMoveCosts ( int moveCosts ) { //コストのセッター
		MoveCosts = moveCosts;
	}
	//ここまでセッター部
	
	//ゲッター部
	public Point getTo () { //Toのゲッター
		return To;
	}
	public Point getFrom () { //Fromのゲッター
		return From;
	}
	public int getMoveCosts () { //コストのゲッター
		return MoveCosts;
	}
	//ここまでゲッター部
	public Routedata ( Simulator simulator, Point FromPoint, Point ToPoint, int Costs ) { //道をつくるよ
		ID = RouteNum++; //Route66　みたいな
		setName ( FromPoint.getName () + ", " + ToPoint.getName () ); //横浜横須賀道路的な名前
		setFrom ( FromPoint ); //どこからか
		setTo ( ToPoint ); //どこへいくか
		setMoveCosts ( Costs ); //コストをセット
		simulator.setRoute ( this ); //道路を定義
	}
}
