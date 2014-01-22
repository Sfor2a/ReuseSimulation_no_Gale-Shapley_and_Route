package housedata;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import simulation.Simulator;

public class Housedata extends HouseElements { //家のデータを管理する
	public List < HAdata > FurnitureList = new ArrayList <> (); //家具リスト
	public List < HousesWish > HousesWishList = new ArrayList <> (); //ほしいものリスト
	private int Coin; //コイン
	private static int HouseIDAdd; //ID加算
	
	//ゲッター・セッター
	public List<HAdata> getHAList() {
		return FurnitureList;
	}
	public List<HousesWish> getHousesWishList() {
		return HousesWishList;
	}
	public void setHousesWish ( HousesWish WL ) {
		HousesWishList.add ( WL );
	}
	public int getCoin() {
		return Coin;
	}
	public void setCoin(int coin) {
		Coin = coin;
	}
	public void setHAdata ( HAdata HPA ) {
		FurnitureList.add ( HPA );
	}

	//ゲッター・セッター終了
	
	public Housedata ( String Nam, int Val, Simulator simulator ) {
		ID = HouseIDAdd++; //ID加算するだけ
		setName ( Nam ); //家の名前セット
		setCoin ( Val ); //いくらもってるかなー
		reedingHPAName ( Nam ); //家具作りに行きます
		reedingWishlistName ( Nam ); //希望価格リスト読み込む
		simulator.setHouseList ( this ); //おうちのリストにいれるよ
	}
	private void reedingHPAName ( String str1 ) { //家具作るよ
		try {
			String Housename= ".\\recycle\\furnituredata\\" + str1 + "_furniture.txt"; 
			//ファイルリーダー
			 File file = new File ( Housename );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//ここまでファイルリーダー
			 
			 String str = "0" ; //読み取り用String関数 ここをヌルにすると止まる（当たり前だ）
			 String[] FurnitureAry = new String[4];
			
			//読み込みメソッド
			do { //EOFをnullとして検出、それまでは読み込み続ける
				str = br.readLine (); //一行を読み込む 読み込むたびに次のif文シーケンスで判定
				if (str != null) { //読み込み行がnullでなければこのシーケンスを実行、行頭で判別する
					if ( str.startsWith ( "Name " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[0] = strAry[1];
					}
					else if ( str.startsWith ( "Value " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[1] = strAry[1];
						//System.out.print(strAry[1]+",");
					}
					else if ( str.startsWith ( "Minus " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[2] = strAry[1];
					}
					else if ( str.startsWith ( "Spec " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[3] = strAry[1];
						//System.out.println(strAry[1]);
					}
					else if ( str1.startsWith ( "//" ) ); //コメント文を検出すると何もしない
					else System.out.println( "err." );
					
					if ( FurnitureAry[0] != null && FurnitureAry[1] != null && FurnitureAry[2] != null ) { //どちらも代入されたときに実行
						new HAdata ( FurnitureAry[0], FurnitureAry[3], Integer.parseInt ( FurnitureAry[1] ), Integer.parseInt ( FurnitureAry[2] ), this ); //家具作ってくれ
						FurnitureAry [0] = null;
						FurnitureAry [1] = null; //そしてともどもリセット
						FurnitureAry [2] = null;
						FurnitureAry [3] = null; //そしてともどもリセット
						}
				}
			} while ( str != null );
			//ここまで読み込みメソッド
			br.close(); //ファイルを閉じる	
		} catch ( FileNotFoundException e ) { //例外処理
			System.err.println ( e.getMessage () ); //ファイルなしの例外	
			System.exit ( 0 );
		} catch ( IOException e ) {
		    System.err.println ( e.getMessage () ); //IOエラー例外
		    System.exit ( 0 );
		}
	}
	
	private void reedingWishlistName ( String str1 ) {
		try {
			String Housename= ".\\recycle\\wishlist\\" + str1 + "_wishlist.txt"; 
			//ファイルリーダー
			 File file = new File ( Housename );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//ここまでファイルリーダー
			 
			 String str = "0" ; //読み取り用String関数 ここをヌルにすると止まる（当たり前だ）
			 String[] FurnitureAry = new String[4];
			 int JJ = 0; //スイッチ用変数
			
			//読み込みメソッド
			while ( str != null ) { //EOFをnullとして検出、それまでは読み込み続ける
				str = br.readLine (); //一行を読み込む 読み込むたびに次のif文シーケンスで判定
				if (str != null) { //読み込み行がnullでなければこのシーケンスを実行、行頭で判別する
					if ( str.startsWith ( "Name " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[0] = strAry[1];						
					}
					else if ( str.startsWith ( "Durability " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[1] = strAry[1];			
					}
					else if ( str.startsWith ( "Value " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[2] = strAry[1];			
					}
					else if ( str.startsWith ( "Spec " ) ) {
						String[] strAry = str.split( " ", 2 );
						FurnitureAry[3] = strAry[1];			
					}
					else if ( str1.startsWith ( "//" ) ); //コメント文を検出すると何もしない
					
					if ( FurnitureAry[0] != null && FurnitureAry[1] != null && FurnitureAry[2] != null && FurnitureAry[3] != null ) { //どちらも代入されたときに実行
						if ( JJ == 0 ) { //強制的に1回はWishListにぶちこむ
							new HousesWish ( FurnitureAry[0], Integer.parseInt ( FurnitureAry[1] ), Integer.parseInt ( FurnitureAry[2] ), this, FurnitureAry[3] ); //ウィッシュリスト作ってくれ
							JJ = 1;
							FurnitureAry [0] = null;
							FurnitureAry [1] = null;
							FurnitureAry [2] = null;
							FurnitureAry [3] = null;//そしてともどもリセット
						}
						
						if ( FurnitureAry[0] != null && FurnitureAry[1] != null && FurnitureAry[2] != null && FurnitureAry[3] != null ) { //一度通されたものは上のif文でリセットされるから通らない
							for ( int i = 0; i < HousesWishList.size (); i++ ) { //WishList総当たり
								if ( !HousesWishList.get ( i ).getName ().equals ( FurnitureAry[0] ) ) { //WishListの中に名前のついた家具がないときに限り、新しいクラスとしてロード
									new HousesWish ( FurnitureAry[0], Integer.parseInt ( FurnitureAry[1] ), Integer.parseInt ( FurnitureAry[2] ), this, FurnitureAry[3] ); //ウィッシュリスト作ってくれ
								}
								else if ( HousesWishList.get ( i ).getName ().equals ( FurnitureAry[0] ) ) { //同じ名前ついたのがあったとき
									HousesWishList.get ( i ).getHAsWishValList ().add ( 
											new HAsWishValue ( HousesWishList.get ( i ), Integer.parseInt ( FurnitureAry[1] ), Integer.parseInt ( FurnitureAry[2] ), FurnitureAry[3] ) );
									HousesWishList.get( i ).getHAsWishValList ().remove ( HousesWishList.get( 0 ).getHAsWishValList ().size() - 1 ); //二度セットされるから1個消す（
								}
							}
						}						
						FurnitureAry [0] = null;
						FurnitureAry [1] = null;
						FurnitureAry [2] = null;
						FurnitureAry [3] = null;//そしてともどもリセット
						}
				}			
			}
			//ここまで読み込みメソッド
			br.close(); //ファイルを閉じる	
		} catch ( FileNotFoundException e ) { //例外処理
			System.err.println ( e.getMessage () ); //ファイルなしの例外	
			System.exit ( 0 );
		} catch ( IOException e ) {
		    System.err.println ( e.getMessage () ); //IOエラー例外
		    System.exit ( 0 );
		}
	}	
}
