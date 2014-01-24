package simulation;

import housedata.*;
import mapdata.*;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Random;

public class Simulator extends HouseElements {
	private List < Housedata > HouseList = new ArrayList <> (); //家のリスト 
	private List < Point > PointList = new ArrayList <> (); //ポイントそのもののリスト
	private List < Routedata > RouteList = new ArrayList <> (); //道のリスト
	private int[][] RouteArray; //地図行列
	private int[][] proposeMatrix; //プロポーズ表買う側　２は契約　１はプロポーズ失敗か契約破棄
	private List < Housedata > BuyHouse = new ArrayList <> (); //買いたい家のリスト
	private List < Housedata > SellHouse = new ArrayList <> (); //売りたい家のリスト
	private List < Score > ScoreList = new ArrayList <> (); //選考表作成のタメのリスト
	private int[] BuyS; //買いたい側の婚約表
	private int[] SellS; //売りたい側の婚約表
	private int[][] BuyQ; //買いたい側の選好表
	private int[][] SellQ; //売りたい側の選好表
	private int[][] SellQRev; //売りたい側の逆選好表
	private List < Integer > ScoreCheckerBuy = new ArrayList <> (); //ダブりスコアのチェック用リスト
	private List < Integer > ScoreCheckerSell = new ArrayList <> ();
    private Random rnd = new Random(); //Randomクラスのインスタンス化
	//セッター
	public void setHouseList ( Housedata Hus ) { //ハウスリストセッター
		HouseList.add ( Hus ); 
	}
	public void setPoint ( Point Pit ) { //ポイントリストのセッター
		PointList.add ( Pit );
	}
	public void setRoute ( Routedata Rte ) { //道リストのセッター
		RouteList.add ( Rte );
	}
	public void setRouteArraysize ( int size ) {
		RouteArray = new int [size][size];
	}
	public void setScoreList ( Score sco ) { //スコアリストセッター
		ScoreList.add ( sco ); 
	}
	//ゲッター	
	public List < Housedata > getHouseList () { //Houseリストそのもののゲッター
		return HouseList;
	}
	public Housedata getHouseData ( int num ) { //指定番号の家のゲッター
		Housedata Hse = HouseList.get ( num );
		return Hse;
	}
	public List < Point > getPitList () { //PointListそのものをもってくるゲッター
		List < Point > PitList = PointList; 
		return PitList;	
	}
	public List < Routedata > getRteList () { //RouteListそのものをもってくるゲッター
		List < Routedata > RteList = RouteList; 
		return RteList;	
	}
	public Point getPoint ( int num ) { //指定番号のポイントのゲッター
		Point Pit = PointList.get ( num );
		return Pit;
	}
	public Routedata getRoute ( int num ) { //指定番号の道のゲッター
		Routedata Rte = RouteList.get ( num );
		return Rte;
	}
	public List < Score > getScoreList () { //スコアﾘｽﾄのゲッター
		return ScoreList;
	}
	
	public Simulator() { //データの読み込み
		//地図と家の作成
		String HouseListData = ".\\recycle\\Houselist.txt";
		String MapListData = ".\\recycle\\SqMap.txt";
		createHousedatafromFile ( HouseListData );
		createMapfromFile ( MapListData );
		CreateMapMatrix(); //地図行列作成　いこうRouteArray[][]が地図行列になる
	}
	private void Flug ( int num, int lineNum, String str ) { //フラグによるエラー処理
		switch ( num ) {
			case 1:
				System.out.print ( "Error: 複数のライフサイクル名が記述されています、どれか一つに統一してください" );
				break;
			case 2:
				System.out.print ( "Error: 同じ名前のステージがすでに存在しています" );
				break;
			case 3:
				System.out.print ( "Error: 同じ経路のパスがすでに存在しています" );
				break;
			case 4:
				System.out.print ( "Error: 存在しないステージを参照しようとするパスです。　パスの名前を修正するか、綴りを確認してください" );
				break;
			case 5:
				System.out.print ( "Error: 読み込みに関係のない行が挿入されています。　この行は読み飛ばします。　スペルミス、書式をチェックしてください" );
				break;
			case 6:
				System.out.println ( "Error: パスの記述書式に問題があります つなげられるステージはパス一本につき、二つです" );
				break;
			default:
				System.out.print ( "Error: その他のエラーが発生しています。　該当する行を確認するか、管理者に問い合わせてください" );
				break;
		}
		System.out.println ( "( 行番号" + lineNum + ": " + str + " )" ); //エラーメッセージを出した後は、必ずそのエラーのある行をしめす
	}
	//地図定義部
	private Point findNamedPoint ( List < Point > Pit, String Name, String str, int CountLine ) { //同じ名前のポイントを見つけるメソッド
		Point C = null; //用意してあげないとEclipseがエラー吐く
		for ( Iterator < Point > i = Pit.iterator (); i.hasNext (); ) { //PointListの中身全部に対して検索をかける
			Point B = i.next ();
			if ( Name.equals ( B.getName () ) ) return C = B; //同じポイントを見つけたらCへ代入しこれを返す
		}
		if ( C == null ) Flug ( 4, CountLine, str ); //nullの場合のエラー処理、フラグをエラー処理メソッドへ投げる
		return C;
	}	
	private int PointDuplicationSearch ( String PitDupName ) { //ポイント名の重複検索
		List < Point > SearchList = getPitList (); //既存のポイントリストを読み込む
		int findExistingName = -1; //既存名前判定フラグ
		if ( SearchList.size () != 0 ) { //一番最初に検出したポイント名はデフォルトで追加
			for ( int i = 0; i < SearchList.size (); i++ ) { //すべてのポイントの名前に関して総当たりで検索をかける
				if ( SearchList.get ( i ).getName ().equals ( PitDupName ) ) {
					findExistingName = 1; //既存の名前があれば
					break;
				}
				else findExistingName = -1; //なければ-1を返す
			}
		}
		return findExistingName;
	}
	private int RouteDuplicationSearch( String RteDupNameIn, String RteDupNameOut ) { //道名の重複検索
		List < Routedata > SearchList = getRteList (); //既存のポイントリストを読み込む
		int findExistingName = -1; //既存名前判定フラグ
		if ( SearchList.size () != 0 ) { //一番最初に検出した道名はデフォルトで追加
			for ( int i = 0; i < SearchList.size (); i++ ) { //すべてのポイントの名前に関して総当たりで検索をかける
				if ( SearchList.get ( i ).getName ().equals ( RteDupNameIn + ", " + RteDupNameOut ) ) {
					findExistingName = 1; //既存の名前があれば
					break;
				}
				else findExistingName = -1; //なければ-1を返す
			}
		}
		return findExistingName;
	}
	private void reedingMapName ( String str, int CountLine) { //地図の名前読み込み
		String[] strAry = str.split ( " ", 2 );
		if ( getName () != null ) Flug ( 1, CountLine, str ); //すでに名前がはいっている段階でLifeCycleName行をよんだらエラーを出す
		else setName ( strAry[1] ); //行頭の"MapName "を除外し、ヌルの場合名前へ代入
	}
	private void reedingPointName ( String str, int CountLine ) { //ポイント名の読み込み
		String[] strAry = str.split( ", ", 5 );
		if (  PointDuplicationSearch ( strAry[1] ) == -1 )
			new Point ( this, strAry[1], Integer.parseInt( strAry[2] ), Integer.parseInt( strAry[3] ), Integer.parseInt( strAry[4] ) ); //行頭の"Point "を除外しポイント名前を取得し、ポイントをその名前と座標で作成
		else Flug ( 2, CountLine, str ); //エラー処理
	}
	private void reedingRouteName ( String str, int CountLine ) { //道名の読み込み
		String[] strAry = str.split( " ", 3 );
		String strRoute = strAry[1]; //行頭の"Route "を除外
		String[] RouteName = strRoute.split ( ",", 0 ); //配列RouteNameへ","で分割したものを代入
		if ( RouteName.length < 3 ) { //道は二つのポイントしかむすばないよ
			Point fromPoint = findNamedPoint ( PointList, RouteName[0], str, CountLine ); //検索をかけて代入
			Point toPoint = findNamedPoint ( PointList, RouteName[1], str, CountLine ); //検索をかけて代入				
			if ( fromPoint != null & toPoint != null ){ //nullでない場合に道を作る nullよけ
				if ( RouteDuplicationSearch( RouteName[0], RouteName[1] ) == -1 ) { //重複判定
					Routedata A = new Routedata (this, fromPoint, toPoint, Integer.parseInt( strAry[2] ) ); //道を作成
					fromPoint.setRoutedataOut ( A ); //RouteOutへ代入
					toPoint.setRoutedataIn ( A ); //RouteInへ代入
				}
				else Flug (3, CountLine, str); //重複道を発見の場合のエラー
			}
		}
		else Flug ( 6, CountLine, str ); //道の書式エラー排除
	}
	public void createMapfromFile ( String DataFolder ) { //引数にファイルをなげて地図を作成
		try {
			//ファイルリーダー
			 File file = new File ( DataFolder );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//ここまでファイルリーダー
			 
			 String str = "0" ; //読み取り用String関数 ここをヌルにすると止まる（当たり前だ）
			 int CountLine = 0; //行番号加算変数（エラー処理用）
			
			//読み込みメソッド
			while ( str != null ) { //EOFをnullとして検出、それまでは読み込み続ける
				str = br.readLine (); CountLine++; //一行を読み込む 読み込むたびに次のif文シーケンスで判定、同時に行番号加算
				if (str != null) { //読み込み行がnullでなければこのシーケンスを実行、行頭で判別する
					if ( str.startsWith ( "MapName" ) ) reedingMapName ( str, CountLine ); //ライフサイクル名の読み込み
					else if ( str.startsWith ( "Point" ) ) reedingPointName ( str, CountLine ); //ポイントの読み込み
					else if ( str.startsWith ( "Route" ) ) reedingRouteName ( str, CountLine ); //道の読み込み
					else if ( str.startsWith ( "//" ) ); //コメント文を検出すると何もしない
					else Flug ( 5, CountLine, str ); //謎の文章を読み込むとエラーを表示して無視
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
	//家のデータを読み込み家電のわりあてる部分
	public void createHousedatafromFile ( String DataFolder ) { //引数にファイルの位置を投げるとファイルからライフサイクルを生成 こいつに渡されるのは家のリスト
		try {
			//ファイルリーダー
			 File file = new File ( DataFolder );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//ここまでファイルリーダー
			 
			 String str = "0" ; //読み取り用String関数 ここをヌルにすると止まる（当たり前だ）
			 int CountLine = 0; //行番号加算変数（エラー処理用）
			
			//読み込みメソッド
			while ( str != null ) { //EOFをnullとして検出、それまでは読み込み続ける
				str = br.readLine (); CountLine++; //一行を読み込む 読み込むたびに次のif文シーケンスで判定、同時に行番号加算
				if (str != null) { //読み込み行がnullでなければこのシーケンスを実行、行頭で判別する
					if ( str.startsWith ( "HouseName " ) ) reedingHouseName ( str, CountLine ); //家の名前の読み込み
					else if ( str.startsWith ( "//" ) ); //コメント文を検出すると何もしない
					else Flug ( 5, CountLine, str ); //謎の文章を読み込むとエラーを表示して無視
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
	private void reedingHouseName ( String str, int countLine ) { //家の名前を読み込んだよ　家をつくるよ
		try {
			String StAry[] = str.split ( " ", 2 ); //"Housename "を除去
			String Housename= ".\\recycle\\housedata\\" + StAry[1] + "_data.txt";
			//ファイルリーダー
			 File file = new File ( Housename );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//ここまでファイルリーダー
			 
			 String str1 = "0" ; //読み取り用String関数 ここをヌルにすると止まる（当たり前だ）
			 int CountLine_1 = 0; //行番号加算変数（エラー処理用）
			 String[] HouseAry = new String[2];
			
			//読み込みメソッド
			while ( str1 != null ) { //EOFをnullとして検出、それまでは読み込み続ける
				str1 = br.readLine (); CountLine_1++; //一行を読み込む 読み込むたびに次のif文シーケンスで判定、同時に行番号加算
				if (str1 != null) { //読み込み行がnullでなければこのシーケンスを実行、行頭で判別する
					if ( str1.startsWith ( "Name " ) ) {
						String[] strAry = str1.split ( " ", 2 );
						HouseAry[0] = strAry[1];
					}
					else if ( str1.startsWith ( "Coin " ) ) {
						String[] strAry = str1.split ( " ", 2 );
						HouseAry[1] = strAry[1];		
					}
					else if ( str1.startsWith ( "//" ) ); //コメント文を検出すると何もしない
					else Flug ( 5, CountLine_1, str1 ); //謎の文章を読み込むとエラーを表示して無視
				}			
			}
			//ここまで読み込みメソッド
			br.close (); //ファイルを閉じる
			new Housedata ( HouseAry[0], Integer.parseInt ( HouseAry[1] ), this ); //家の作成
		} catch ( FileNotFoundException e ) { //例外処理
			System.err.println ( e.getMessage () ); //ファイルなしの例外	
			System.exit ( 0 );
		} catch ( IOException e ) {
		    System.err.println ( e.getMessage () ); //IOエラー例外
		    System.exit ( 0 );
		}
	}
	//地図行列作成部
	private void CreateMapMatrix () { //地図行列作成
		int PointNum = getPitList ().size (); //地点の数
		setRouteArraysize ( PointNum );
		for ( int i=0; i< RouteArray.length; i++ ) { //地図行列の初期化
			for ( int j =0; j< RouteArray[0].length; j++) {
				RouteArray[i][j] = 0;
			}
		}
		for ( int i = 0; i < PointNum; i++ ) { //地点1からくりかえす
			int RouteOutNum = getPitList ().get ( i ).getRteOutList ().size ();
			for ( int j = 0; j < RouteOutNum; j++ ) {
				double x1 = getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getX ();
				double y1 = getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getY ();
				double x2 = getPitList ().get ( i ).getX ();
				double y2 = getPitList ().get ( i ).getY ();
				double Length = Math.sqrt ( ( x2 - x1 ) * ( x2 - x1 ) + ( y2 - y1 ) * ( y2 - y1 ) ); //結ばれている2点間の距離を計算
				
				String AiteName = getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getName ();
				int l = 0;
				
				for ( int k = 0; k < PointNum; k++ ) { //つながる先のポイントを検索して、リストの番号を手に入れる
					if ( AiteName.equals( getPitList ().get ( k ).getName () ) ){
						l = k; 
						break;
					}
				}
				
				int Costs = getRouteCosts ( getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getName (),
						getPitList ().get ( i ).getName() ); //道路のコスト取得
				
				RouteArray[i][l] = (int) Length * Costs; //距離を代入（おもて
				RouteArray[l][i] = (int) Length * Costs ; //距離を代入（うら
			}
			
		}
	}
	private int getRouteCosts ( String nam1, String nam2 ) { //道のコストを発見
		int ret = 0;
		for ( int i=0; i < getRteList().size(); i++ ) {
			if ( getRteList().get(i).getTo().getName().equals ( nam1 ) && getRteList().get(i).getFrom().getName().equals ( nam2 ) ) {
				ret = getRteList().get(i).getMoveCosts();
			}
		}
		return ret;
	}
	//最短経路問題解決部
	private int MARange ( Housedata B1, Housedata B2 ) { //家のデータから最短距離を返すよ あるいみただのゲッター
		int PointNum = getPitList().size(); //地図モデルの行列
		
		String Name1 = B1.getName(); //家の名前を取得
		String Name2 = B2.getName();
		
		for ( int i = 0; i < getPitList().size(); i++ ) { //家の名前からルートを調べる
			if ( Name1.equals ( getPitList().get(i).getName() ) ) {
				for ( int j = 0; j < getPitList().size(); j++ ) {
					if ( Name2.equals ( getPitList().get(j).getName() ) ) {
						return MinimumAccessProblem ( RouteArray, PointNum, i, j ); //行列とつなぎ先をいれて、距離とルートを返す
					}
				}
			}
		}
		return 0;
	}
 	private int MinimumAccessProblem ( int[][] RouteArray, int N, int Start, int Goal ) { //最短経路問題実行部
 		boolean Visited[] = new boolean[N]; //最短距離が確定した地点
		int Dist[] = new int[N]; //最初からそこまでの距離
		int prev[] = new int[N]; //まえにいた地点
		int nodes[] = new int[N]; //街番号
		for ( int i = 0; i < N; i++ ) {
			Visited[i] = false; //全ての街をいってない状態にする
			Dist[i] = Integer.MAX_VALUE; //全ての街までの距離を無限にする(2147483647
			nodes[i] = i; //街番号を代入			
		}
		Dist[Start] = 0; //最初の地点なので当然距離は0になる
		prev[Start] = Start; // 最初に選択した町の前の町は無いので、とりあえず自分の町を入れておく
		int pos = Start; //現在地を設定
		MinimumRouteSearch ( Visited, RouteArray, Dist, prev, pos, N, nodes ); //最小経路探索
		
		//イカコンストラクタ終了まで表示のコマンド、ぶっちゃけ一部のぞいてもおｋ　つまり経路の表示　距離自体はDistに格納されてるので問題なし
		@SuppressWarnings("unused")
		String str = Start + "から" + Goal + "までの最短ルートは";
		int node = prev[Goal];
		String track = Integer.toString ( Goal );
		for ( int i = 0; i < N; i++ ) {
			track += node;
			if ( node == Start ) break;
			node = prev[node];
		}
		for ( int i = track.length () - 1; i >= 0; i--) str += " => " + track.charAt ( i );
		//if ( node == Start ) System.out.println ( str );
		//else System.out.println ( "ルートなし" );
		//System.out.println ( "距離"+ Dist[Goal] );
		return Dist[Goal]; //距離参照用
	}
	private void MinimumRouteSearch ( boolean[] Visited, int[][] RouteArray, int[] Dist, int[] prev, int pos, int N, int[] nodes ) { //最小経路探索部
		while (true) {
			Visited[pos] = true; //この処理をするとVisitedフラグを立てる
			for ( int x = 0; x < N; x++ ) {
				if ( Visited[x] ) continue; //すでにいった街ならこの処理はスルー
				if ( RouteArray[pos][x] > 0) { //経路がある場合この処理をやれ
					int d = Dist[pos] + RouteArray[pos][x]; //現在地から次の地点までの距離
					if ( d < Dist[x] ) { //今の地点 + 次の地点が元々入っていた距離より小さければ更新
						Dist[x] = d;
						prev[x] = pos;
					}	
				}
			}
			//まだ訪れていない町の中で、スタート地点からの距離が最小になる町を選ぶ
			int next = -1; //次の街探索フラグ
			int nextDist = Integer.MAX_VALUE;
			for ( int node: nodes ) { 
				if ( Visited[node] ) continue;
				if ( Dist[node] < nextDist ) {
					next = node;
					nextDist = Dist[node];
				}
			}
			if ( next == -1 ) break;
			else pos = next;			
		}
	}
	//お見合いエントリー部
	private void SellHouseSelection ( int HouseNum ) { //売りたいと思う家を選び出す
		for ( int i = 0; i < HouseNum; i++ ) { //すべての家について
			Housedata Target = getHouseList ().get ( i );
			if ( Target.getHAList().size() >= 2 ) SellHouse.add ( Target ); //売却条件１：ターゲットが冷蔵庫を２個持っているとき
			else {
				if ( Target.getHAList().size() != 0 ) { //家電を所持している場合にのみ実行
					for ( int j = 0; j < Target.getHAList().size(); j++ ) {
						int Count = Target.getHAList().get(j).getUseTernCount();
						if ( Count >= 5 ) { //条件2：nターム以上つかったら飽きる　今は5
							int ran = rnd.nextInt(100); //0～99までのうち１つを生成
							if ( ran > 0 && ran < 49 ) { //条件2続き:n%の確率でターゲットを売りたいと思う家に指定 現在50%
								if ( SellHouse.size() == 0 ) SellHouse.add(Target); //BuyHouseが空なら追加
								else { //そうでなければ
									if ( SellHouse.indexOf ( Target ) == -1 ) SellHouse.add(Target); //すでにターゲットが追加されていない場合に限り追加
								}
							}
						}
						else break;
					}
				}
			}
		}
	}
	private void BuyHouseSelection ( int HouseNum ) { //買いたい家を選びだす
		for ( int i = 0; i < HouseNum; i++ ) { //その家電に飽きたかどうかを判断
			Housedata Target = getHouseList ().get ( i );
			if ( SellHouse.indexOf ( Target ) == -1 ) { //Targetがすでに売りたい家になっていないことが条件
				if ( Target.getHAList().size() == 0 ) BuyHouse.add ( Target ); //買取条件１：ターゲットが冷蔵庫を0個持っているとき
				else {
					for ( int j = 0; j < Target.getHAList().size(); j++ ) {
						int Count = Target.getHAList().get(j).getUseTernCount();
						if ( Count >= 5 ) { //条件2：nターム以上つかったら飽きる　今は5
							int ran = rnd.nextInt(100); //0～99までのうち１つを生成
							if ( ran > 0 && ran < 49 ) { //条件2続き:n%の確率でターゲットをかいたいと思う家に指定 現在50%
								if ( BuyHouse.size() == 0 ) {
									BuyHouse.add(Target); //BuyHouseが空なら追加
								}
								else { //そうでなければ
									if ( BuyHouse.indexOf ( Target ) == -1 ) BuyHouse.add(Target); //すでにターゲットが追加されていない場合に限り追加
								}
							}
						}
						else break;
					}
				}
			}
		}
	}
	@SuppressWarnings("unused")
	private void sizeAdjust () { //売買する家の数を同数に調整 
		if ( BuyHouse.size() != SellHouse.size() /*&& BuyHouse.size() != 0 && SellHouse.size() != 0*/) { //売りたい家と買いたい家を同数にする
			if ( BuyHouse.size() > SellHouse.size() ) { //売るほうが多いとき
				do BuyHouse.remove(0); while ( BuyHouse.size() != SellHouse.size() ); //あたまから同数になるまで削除する
			}
			else { //買うほうが多いとき
				do SellHouse.remove(0);  while ( BuyHouse.size() != SellHouse.size() ); //あたまから同数になるまで削除する
			}
		}
	}
	//選考表作成部
	private void ResetSheet (int EntryNumBuy, int EntryNumSell ) { //表のリセット
		if ( EntryNumBuy == EntryNumSell ) { //念のため売買の家が同数かを確認
			int EntryNum = EntryNumBuy;
			proposeMatrix = new int [EntryNum][EntryNum]; //表の再定義
			BuyS = new int [EntryNum];
			SellS = new int [EntryNum];
			BuyQ = new int [EntryNum][EntryNum];
			SellQ = new int [EntryNum][EntryNum];
			SellQRev = new int [EntryNum][EntryNum];
			for ( int i = 0; i < proposeMatrix.length; i++ ) { //プロポーズ表のリセット
				for ( int j = 0; j < proposeMatrix[0].length; j++ ) {
					proposeMatrix[i][j] = 0;
				}
			}
			for ( int i = 0; i < BuyQ.length; i++ ) { //買う側選好表のリセット
				for ( int j = 0; j < BuyQ[0].length; j++ ) {
					BuyQ[i][j] = 0;
				}
			}
			for ( int i = 0; i < SellQ.length; i++ ) { //売る側選好表のリセット
				for ( int j = 0; j < SellQ[0].length; j++ ) {
					SellQ[i][j] = 0;
				}
			}
			for ( int i = 0; i < SellQRev.length; i++ ) { //逆選好表のリセット
				for ( int j = 0; j < SellQRev[0].length; j++ ) {
					SellQRev[i][j] = 0;
				}
			}
			for ( int i = 0; i < BuyS.length; i++ ) { //買いたい側婚約表のリセット
					BuyS[i] = Integer.MAX_VALUE;
			}
			for ( int i = 0; i < SellS.length; i++ ) { //売りたい側婚約表のリセット
				SellS[i] = Integer.MAX_VALUE;
			}
		}
		else System.out.println ( "エラー：売買する家が同数ではありません" );
	}
	private void ScoreCreator( Housedata Buy1, Housedata Sell1 ) { //スコアを作成する
		int Mem_i = 0; //iの記憶用
		if ( Sell1.getHAList ().size () >= 2 ) { //冷蔵庫を2こもっていれば耐久度のひくい方を手放すようにする
			int LowDur = Integer.MAX_VALUE;
			for( int i = 0; i < Sell1.getHAList().size(); i++ ) 
				if ( LowDur >= Sell1.getHAList ().get ( i ).getDurability () ) Mem_i = i; //より耐久度の低いほうを取得する	
		}
		//比較用データ（売る側）
		HAdata setSellTargetHA = Sell1.getHAList ().get ( Mem_i ); //2個なければ１個目、２こあれば上できりかえたどちらかを使う。
		int setSellDur = setSellTargetHA.getDurability();
		int setSellSpec = Integer.parseInt( setSellTargetHA.getSpec() );
		int setSellValue = setSellTargetHA.getTermValue();
		//比較用データ（買う側）
		int setBuySpec = 1;
		int setBuyCost = 1;
		HAdata setBuyTargetHA = null;
		if ( Buy1.getHAList ().size () > 0 ) setBuyTargetHA = Buy1.getHAList ().get ( 0 ); //買いたい家の比較用家電を用意（家電もっていれば　もっていないと１，１，が適用
		for ( int i = 0; i < Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList ().size (); i++ ) {
			Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList ().get ( i );
			if ( setSellDur > Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList().get ( i ).getDurability () ) 
				setBuyCost = Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList ().get ( i ).getCost (); //相手の耐久度にふさわしい取引価格をもちだす
		}
		if ( setBuyTargetHA != null ) setBuySpec = Integer.parseInt ( setBuyTargetHA.getSpec () ); //家電が存在していればスペックを呼び出す
		
		//距離の呼び出し
		int Dist = MARange ( Buy1, Sell1 );
		//スコア作成 （スペック差の割合＊耐久度＊取引価格の割合＊距離）
		double ValuePercent = ( ( double ) Math.abs( ( ( double ) setSellValue - (double) setBuyCost ) ) / ( double ) setBuyCost );
		double SpecPercent = ( ( double ) Math.abs ( ( double ) setSellSpec -  ( double )setBuySpec )  / ( double ) setBuySpec );
		int ScoreBuy = ( int ) ( ( double ) ( 1 / ValuePercent ) + ( double ) ( 1 / SpecPercent ) + /* ( double ) ( 1 / ( double ) Dist )  +*/ ( double ) ( 100 - setSellDur ) ); //買う側の相手へのスコア スコアに距離を含めない
		if ( ScoreCheckerBuy.size() == 0 ) {
			ScoreCheckerBuy.add ( ScoreBuy ); //ダブってるスコアがないように加算する部分　スコアチェッカーが空ならチェッカーに追加
		}
		else { //なにかはいっているとき
			if ( ScoreCheckerBuy.indexOf( ScoreBuy ) == -1 ) ScoreCheckerBuy.add ( ScoreBuy ); //チェックリストを確認し、該当がなければ追加
			else { //該当があれば1加算して、追加する
				ScoreBuy = ScoreBuy + 1;
				ScoreCheckerBuy.add ( ScoreBuy );
			}	
		}
		int ScoreSell = setBuyCost; //売る側の相手へのスコア
		if ( ScoreCheckerSell.size() == 0 ) ScoreCheckerSell.add ( ScoreSell ); //ダブってるスコアがないように加算する部分　スコアチェッカーが空ならチェッカーに追加
		else { //なにかはいっているとき
			if ( ScoreCheckerSell.indexOf( ScoreSell ) == -1 ) ScoreCheckerSell.add ( ScoreSell ); //チェックリストを確認し、該当がなければ追加
			else { //該当があれば1加算して、追加する
				ScoreSell = ScoreSell + 1;
				ScoreCheckerBuy.add ( ScoreSell );
			}	
		}
		new Score ( ScoreBuy, ScoreSell, Buy1, Sell1, setSellTargetHA, this, Dist ); //選考表を作成するためのスコアデータの作成
	}
	@SuppressWarnings({ "unchecked", "unused" })
	private void preferanceSheetCreator () { //選考表作成
		List < Score > BuyScoreSortor = getScoreList(); //買う側の並び替えようリスト
		Collections.sort ( BuyScoreSortor, new BuyScoreComparator () ); //買う側のスコアで並び替えする
		Collections.reverse ( BuyScoreSortor ); //このままだと照準なので子運順にする
		int n = 0; //加算用n
		for ( int i = 0; i < BuyHouse.size(); i++ ) { //マズは最初から順に買う側の選好表を作成する
			n = 0; //毎回リセットしなさい
			for ( int j = 0; j < BuyScoreSortor.size(); j++ ) {
				if ( BuyScoreSortor.get ( j ).getBuyHouse () == BuyHouse.get ( i ) ) {
					int m = SellHouse.indexOf ( BuyScoreSortor.get ( j ).getSellHouse () ); //相手の家が売る家リストのm番目に該当するかをしらべる
					BuyQ[i][n] = m; //好きな人が分かるので書き込み
					n += 1; //次の好きな人を探せるようにする
				}
			}
		}
		n = 0; //nのリセット
		List < Score > SellScoreSortor = getScoreList(); //売る側の並び替えようリスト
		Collections.sort ( SellScoreSortor, new SellScoreComparator () ); //売る側のスコアで並び替えする
		Collections.reverse ( SellScoreSortor ); //こっちも降順にする
		for ( int i = 0; i < SellHouse.size(); i++ ) { //最初から順に売る側の選好表を作成する
			n = 0; //毎回リセットしなさい
			for ( int j = 0; j < SellScoreSortor.size(); j++ ) {
				if ( SellScoreSortor.get ( j ).getSellHouse () == SellHouse.get ( i ) ) {
					int m = BuyHouse.indexOf ( SellScoreSortor.get ( j ).getBuyHouse () ); //相手の家が買う家リストのm番目に該当するかをしらべる
					SellQ[i][n] = m; //好きな人が分かるので書き込み
					n += 1; //次の好きな人を探せるようにする
				}
			}
		}
		//BuyScoreSortor.clear(); //並び替えようリストのクリア
		//SellScoreSortor.clear();
		SellQRev = RevMaker ( SellQ, SellQRev ); //逆選考表の作成
	}
	private int[][] RevMaker ( int[][] peopleQ, int[][] QRev ) { //逆選考表をつくるよ
		for ( int i = 0; i < peopleQ.length; i++ ) {
			for ( int j = 0; j < peopleQ[0].length; j++ ) {
				QRev[i][ peopleQ[i][j] ] = j;
			}
		}
		return QRev;
	}
	//安定結婚システム
	@SuppressWarnings("unused")
	private void MatchSystem () { //マッチシステム
		int PartnerJ = 0; //第1希望のパートナーを選ぶ変数
		int flug = 1; //終了フラグ
		do {
			flug = 0; //フラグは折っておく
			for ( int i = 0; i < BuyHouse.size(); i++ ) {
				if ( BuyS[i] == Integer.MAX_VALUE ) { //結婚していないとき、結婚相手を探す
					if ( proposeMatrix[i][ BuyQ[i][PartnerJ] ] == 0 ) { //対象にプロポーズしているかを調べる、プロポーズしていない場合
						if ( SellS[BuyQ[i][PartnerJ]] == Integer.MAX_VALUE ) { //対象が未婚
							SellS[BuyQ[i][PartnerJ]] = i; //iの人が一番結婚したい女性が独身ならばiの人と結婚する
							BuyS[i] = BuyQ[i][PartnerJ]; //希望女性と結婚と書き込む
							proposeMatrix[i][ BuyQ[i][PartnerJ] ] = 2;//プロポーズ表を更新する 2だと結婚
						}
						else if ( SellS[ BuyQ[i][PartnerJ] ] < Integer.MAX_VALUE ) { //対象の女性が既に結婚していたら
								if ( SellQRev[ BuyQ[i][PartnerJ] ][i] > SellQRev[ BuyQ[i][PartnerJ] ][ SellS[ BuyQ[i][PartnerJ] ] ] ) { //逆選考表の対象女性にとってiの人が上か、それとも今の結婚相手のが上かどうか
									proposeMatrix[i][ BuyQ[i][PartnerJ] ] = 1; //すでに結婚してる人のほうが好きなとき　は嫌われたのでプロポーズだけ
								}
								else if ( SellQRev[ BuyQ[i][PartnerJ] ][i] < SellQRev[ BuyQ[i][PartnerJ] ][ SellS[ BuyQ[i][PartnerJ] ] ] ) { //新しい人のが好き
									proposeMatrix[ SellS[ BuyQ[i][PartnerJ] ] ][ BuyQ[i][PartnerJ] ] = 1; //婚約破棄
									BuyS [ SellS[ BuyQ[i][PartnerJ] ] ] = Integer.MAX_VALUE; //二人ともいったん未婚に
									SellS[ BuyQ[i][0] ] = Integer.MAX_VALUE;
									proposeMatrix[i][ BuyQ[i][PartnerJ] ] = 2; //新しい結婚
									BuyS[i] = BuyQ[i][PartnerJ]; //お互いの結婚相手を変更
									SellS[BuyQ[i][PartnerJ]] = i;
								}					
						}
					}
					else if ( proposeMatrix[i][ BuyQ[i][PartnerJ] ] > 0 ) { //対象がプロポーズしていたり結婚していた場合
						PartnerJ += 1;
						if ( SellS[BuyQ[i][PartnerJ]] == Integer.MAX_VALUE ) { //対象が未婚
							SellS[BuyQ[i][PartnerJ]] = i; //iの人が一番結婚したい女性が独身ならばiの人と結婚する
							BuyS[i] = BuyQ[i][PartnerJ]; //希望女性と結婚と書き込む
							proposeMatrix[i][ BuyQ[i][PartnerJ] ] = 2;//プロポーズ表を更新する 2だと結婚
						}
						else if ( SellS[ BuyQ[i][PartnerJ] ] < Integer.MAX_VALUE ) { //対象の女性が既に結婚していたら
								if ( SellQRev[ BuyQ[i][PartnerJ] ][i] > SellQRev[ BuyQ[i][PartnerJ] ][ SellS[ BuyQ[i][PartnerJ] ] ] ) { //逆選考表の対象女性にとってiの人が上か、それとも今の結婚相手のが上かどうか
									proposeMatrix[i][ BuyQ[i][PartnerJ] ] = 1; //すでに結婚してる人のほうが好きなとき　は嫌われたのでプロポーズだけ
								}
								else if ( SellQRev[ BuyQ[i][PartnerJ] ][i] < SellQRev[ BuyQ[i][PartnerJ] ][ SellS[ BuyQ[i][PartnerJ] ] ] ) { //新しい人のが好き
									proposeMatrix[ SellS[ BuyQ[i][PartnerJ] ] ][ BuyQ[i][PartnerJ] ] = 1; //婚約破棄
									BuyS [ SellS[ BuyQ[i][PartnerJ] ] ] = Integer.MAX_VALUE; //二人ともいったん未婚に
									SellS[ BuyQ[i][0] ] = Integer.MAX_VALUE;
									proposeMatrix[i][ BuyQ[i][PartnerJ] ] = 2; //新しい結婚
									BuyS[i] = BuyQ[i][PartnerJ]; //お互いの結婚相手を変更
									SellS[BuyQ[i][PartnerJ]] = i;
								}					
						}
					}
				}
			}
			for ( int i = 0; i < BuyS.length; i++ ) { //フラグ処理
				if ( BuyS[i] == Integer.MAX_VALUE ) flug = 1;
			}
		} while ( flug == 1 ); //未婚約の人を処理
	}
	//リユース実行部
	private void DoReuse ( WriteOutData WOD ) { //リユースを行う
		List <Housedata> SellHouseCheckList = new ArrayList <> ();
		for ( int i = 0; i < BuyHouse.size(); i++ ) { //すべての買いたい家について評価を行う
			int j = 0; //SellHouse探し用
			int Flug = 1; //フラグを立てるく
			do {
				if ( j < ScoreList.size() ) {
					Score ReuseTarget = ScoreList.get( j ); //最初はjは0
					if ( ReuseTarget.getBuyHouse() == BuyHouse.get(i) ) { //とってきたターゲットに買いたい家があれば次を実行
						if ( SellHouseCheckList.indexOf ( ReuseTarget.getSellHouse() ) == -1 || SellHouseCheckList.size() == 0 ) { //対象の家がまだ家電を売っていない場合、リユース
							int BuyHouseNum = getHouseList ().indexOf ( ReuseTarget.getBuyHouse () ); //買う家を探す
							int SellHouseNum = getHouseList ().indexOf ( ReuseTarget.getSellHouse () ); //売る家を探す
							int SellHANum = getHouseList ().get ( SellHouseNum ).getHAList ().indexOf ( ReuseTarget.getSellHA() ); //売却家電を探す
							if ( SellHANum != -1 ) {
								getHouseList ().get ( SellHouseNum ).getHAList ().get ( SellHANum ).setUseTernCount ( 0 ); //使用対象が移動するので使用回数をリセット
								getHouseList ().get ( SellHouseNum ).getHAList ().get( SellHANum ).setExchangecount ( getHouseList ().get ( SellHouseNum ).getHAList ().get( SellHANum ).getExchangecount () + 1 ); //リユース回数を増やす
								getHouseList ().get( BuyHouseNum ).setHAdata( getHouseList ().get ( SellHouseNum ).getHAList ().get( SellHANum ) ); //リユース対象家電を移動
								getHouseList ().get( SellHouseNum ).getHAList ().remove ( SellHANum ); //移動終了
								getHouseList ().get( BuyHouseNum ).setCoin ( getHouseList ().get( BuyHouseNum ).getCoin() - ReuseTarget.getScoreforSell() ); //お金の支払い
								getHouseList ().get( SellHouseNum ).setCoin ( getHouseList ().get( SellHouseNum ).getCoin() + ReuseTarget.getScoreforSell() ); //お金の受け取り
								//データ書き出し部
								int KK = 0;
								int JJ = 0;
								for ( int k = 0; k < BuyQ[i].length; k++ ) {
									if ( BuyQ[i][k] == SellHouse.indexOf( ReuseTarget.getSellHouse() ) ) KK = k; //買い手はk番目に好きな人とリユースした
								}
								for ( int k = 0; k < SellQ[i].length; k++ ) {
									if ( SellQ[i][k] == BuyHouse.indexOf( ReuseTarget.getBuyHouse() ) ) JJ = k; //売り手はk番目に好きな人とリユースした
								}
								WOD.WriteOut( ReuseTarget, KK+1, JJ+1 );
								SellHouseCheckList.add ( ReuseTarget.getSellHouse() ); //リユースしたらその家はすでに販売したリストに加える
								Flug = 0; //リユースをしたら次の買いたい家について調べるためループを抜ける
							}
						}
					}
				}
			if ( j < ScoreList.size() ) j += 1; //次の家について探す
			if (j == ScoreList.size() - 1 ) Flug = 0;
			} while ( Flug == 1 );
			if ( i >= SellHouse.size() ) break; //家の数がこえないように気をつける
		}
		SellHouseCheckList.clear();
	}
	//ターン終了処理部
	private void MinusDurAllHA () { //全ての家電の耐久度を減じ、新たな現在価格を設定し、使用回数を増やす
		for ( int i = 0 ; i < getHouseList ().size (); i++ ) { //全ての家の
			if ( getHouseList ().get ( i ).getHAList ().size () != 0 ) { //所持家電が0でない限り
				for ( int j = 0; j <getHouseList ().get ( i ).getHAList ().size (); j++ ) { //所持する全家電にたいし
					getHouseList ().get ( i ).getHAList ().get( j ).setDurability ( getHouseList ().get ( i ).getHAList ().get( j ).getDurability ()
							- getHouseList ().get ( i ).getHAList ().get( j ).getMinusDur () ) ; //耐久度を減産
					double TermValue = ( double ) getHouseList ().get ( i ).getHAList ().get( j ).getMaxValue() *
							( ( double ) getHouseList ().get ( i ).getHAList ().get( j ).getDurability () / ( double ) 100  ); //現在価格計算
					getHouseList ().get ( i ).getHAList ().get( j ).setTermValue( ( int ) TermValue ); //新しい現在価格を設定					
					getHouseList ().get ( i ).getHAList ().get( j ).setUseTernCount( getHouseList ().get ( i ).getHAList ().get( j ).getUseTernCount() + 1 ); //使用回数を増やす。
				}
			}
		}
	}
	private void ClearList () { //リストや配列を空にする
		BuyHouse.clear(); //売買する家リストをクリア
		SellHouse.clear();
		ScoreList.clear(); //スコアリストをクリア
	}
	
	@SuppressWarnings("unused")
	private void QView ( int[][] View ) { //選考表の表示
		for ( int i = 0; i < View.length; i++ ) {
			System.out.print ( i + " {\t" );
			for ( int j = 0; j < View[0].length; j++ ) {
				System.out.print( View[i][j] + "\t" );
			}
			System.out.println("}");
		}
		System.out.println();
	}
	
	//シミュレータ本体
	public void SimulationStart () { //冷蔵庫に限定したシミュレーション実行部
		//お見合いエントリー
		int HouseNumber = getHouseList ().size (); //すべての家の数
		SellHouseSelection ( HouseNumber ); //売りたい家選び
		BuyHouseSelection ( HouseNumber ); //買いたい家選び
		WriteOutData WOD = new WriteOutData( SellHouse.size(), BuyHouse.size() );
		sizeAdjust();
		WOD.Adjust( SellHouse.size(), BuyHouse.size() );
		ResetSheet ( BuyHouse.size (), SellHouse.size () );
		if ( BuyHouse.size() != 0 && SellHouse.size() != 0 ) {
			//選考表作成・安定結婚システム
			for ( int i = 0; i < BuyHouse.size(); i++ ) { //お見合い相手に対するスコアを作成
				for ( int j = 0; j < SellHouse.size(); j++ ) {
					ScoreCreator( BuyHouse.get ( i ), SellHouse.get ( j ) ); //iにたいしてすべてのjのスコアを確認
				}
			}
			ScoreCheckerBuy.clear();
			ScoreCheckerSell.clear();
			preferanceSheetCreator();
			//MatchSystem(); //安定結婚システムを動かす BuyS[]に取引相手が収まっている マッチングしないからやらない
			DoReuse( WOD ); //リユース
			System.out.print("リユースあり ");
		}		
		else {
			System.out.print("リユースなし ");
			WOD.None();
		}
		
		//ターン終了処理
		MinusDurAllHA(); //耐久度をへらし使用回数を増やすなど
		ClearList(); //リストを空にする
		WOD.WriteDist();
		System.out.println("ターンエンド");
	}
}
