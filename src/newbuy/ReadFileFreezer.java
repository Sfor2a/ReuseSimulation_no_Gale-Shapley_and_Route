package newbuy;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class ReadFileFreezer {
	private List < freezer > NewfreezerList = new ArrayList <> (); //冷蔵庫のリスト
	public ReadFileFreezer	() {
		
	}
	
	public void setfreezerlist ( freezer freezer ) {
		NewfreezerList.add ( freezer );		
	}
	public List < freezer > getfreezerlist() {
		return NewfreezerList;
	}
	
	public void createNewFreezerList ( String Name ) { //フォルダ名を投げて新品冷蔵庫を作る
		try {
			//ファイルリーダー
			 File file = new File ( Name );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//ここまでファイルリーダー
			 
			 String str = "0" ; //読み取り用String関数 ここをヌルにすると止まる（当たり前だ）

			//読み込みメソッド
			while ( str != null ) { //EOFをnullとして検出、それまでは読み込み続ける
				str = br.readLine ();
				if (str != null) { //読み込み行がnullでなければこのシーケンスを実行、行頭で判別する
					if ( str.startsWith ( "#" ) ); //コメント文を検出すると何もしない
					else {
						String StAry[] = str.split ( ", ", 2 ); //コストとスペックで二分
						new freezer ( this, Integer.parseInt( StAry[1] ), Integer.parseInt( StAry[0] ) );
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
