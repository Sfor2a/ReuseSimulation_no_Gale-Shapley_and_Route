package mapdata;

import java.util.ArrayList;
import java.util.List;

import simulation.Simulator;

public class Point extends Map_Elements {
	private List < Routedata > RoutedataIn = new ArrayList <> (); //はいってくる道のリスト
	private List < Routedata > RoutedataOut = new ArrayList <> (); //でていく道のリスト
	private static int PointNum = 0; //加算用変数
	private int X; //地点のx座標
	private int Y; //地点のY座標
	private int Classification; //種別
	
	//セッター部
	public void setRoutedataIn ( Routedata  PthIn ) { //RoutedataInのセッター
		RoutedataIn.add ( PthIn );
	}
	public void setRoutedataOut ( Routedata  PthOut ) { //RoutedataOutのセッター
		RoutedataOut.add ( PthOut );
	}
	public void setX ( int seX ) {
		X = seX;
	}
	public void setY ( int seY ) {
		Y = seY;
	}
	public void setClassification ( int seC ) {
		Classification = seC;
	}
	//ここまでセッター部
	
	//ゲッター部
	public Routedata getRoutedataIn ( int Num ) { //指定した番号のRoutedataInをリストから取得
		Routedata Pthi = RoutedataIn.get ( Num ); 
		return Pthi;
	}
	public Routedata getRoutedataOut ( int Num ) { //指定した番号のRoutedataOutをリストから取得
		Routedata Ptho = RoutedataOut.get ( Num ); 
		return Ptho;
	}
	public List < Routedata > getRteInList () { //RoutedataInListそのものをもってくるゲッター
		List < Routedata > PthInList = RoutedataIn; 
		return PthInList;	
	}
	public List < Routedata > getRteOutList () { //RoutedataOutListそのものをもってくるゲッター
		List < Routedata > PthOutList = RoutedataOut; 
		return PthOutList;	
	}
	public int getX () { //X座標を取得
		int geX = X;
		return geX;
	}	
	public int getY () { //Y座標を取得
		int geY = Y;
		return geY;
	}
	public int getClassification () {
		int geC = Classification;
		return geC;
	}
	//ここまでゲッター部分
		
	public Point( Simulator simulator, String PointName, int sX, int sY, int sC) {
			ID = PointNum++; //ポイントができるたびに加算
			setName ( PointName ); //引数の名前を適用
			setX ( sX ); //X座標を設定
			setY ( sY ); //Y座標を設定
			setClassification ( sC ); //種別を定義　1.家 2.交差点
			simulator.setPoint ( this ); //座標を定義
	}
}

