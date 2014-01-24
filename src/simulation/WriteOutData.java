package simulation;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.List;

public class WriteOutData {
	
	File file = new File ( ".\\recycle\\WriteOut\\WritedOut_data5.csv" ); //書き込むファイル名
	List < Integer > StoreScore = new ArrayList <> (); 
	List < Integer > RankBuyStore = new ArrayList <> (); 
	List < Integer > RankSellStore = new ArrayList <> (); 
	
	public WriteOutData ( int SellNum, int BuyNum ) { //初期売買希望人数
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( ",売りたい家の数," + SellNum+ ",買いたい家の数," + BuyNum + "," );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用意
	}
	public void WriteOut ( Score ReuseTarget, int KK, int JJ ) {
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( "," + ReuseTarget.getBuyHouse().getName() + "," +ReuseTarget.getSellHouse().getName() + "," + ReuseTarget.getDist() + "," +
					ReuseTarget.getScoreforSell() + "," + KK + "," + JJ );
			Storage ( ReuseTarget.getDist(), KK, JJ );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用意
	}
	
	private void Storage(int dist, int kK, int jJ) {
		StoreScore.add ( dist );
		RankBuyStore.add (kK);
		RankSellStore.add (jJ);
	}
	public void Adjust( int SellNum, int BuyNum ) { //お見合い参加人数
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( ",売れた家の数," + SellNum+ ",買えた家の数," + BuyNum + "," );
			pw.println ( ",買い取りする家,売る家,そこまでの距離,売買価格,何番目に好きな人から買えた,何番目に好きな人に売れた" );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用意
	}
	public void None() { //取引なしの書き出し
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( ",取引なし" );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用
	}
	
	public void WriteDist () {
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.print ( ",総取引における距離の平均" );
			int KK = 0;
			for ( int i = 0; i < StoreScore.size(); i++ ) {
				KK += StoreScore.get( i );
			}
			
			if ( StoreScore.size() != 0  ) {
				pw.print ( "," + ( double ) ( KK / StoreScore.size() ) + ",合算地," + KK  );
			}
			KK = 0;
				for ( int i = 0; i < RankSellStore.size(); i++ ) {
					KK += RankSellStore.get( i );
				}
			if ( RankSellStore.size() != 0 ) {
				pw.print ( ",何番目に好きな人に売れたの平均," + ( double ) ( KK / RankSellStore.size() ) );
			}
			KK = 0;
				for ( int i = 0; i < RankBuyStore.size(); i++ ) {
					KK += RankBuyStore.get( i );
				}
			if ( RankBuyStore.size() != 0 ) {
				pw.print ( ",何番目に好きな人から買えたの平均," + ( double ) ( KK / RankBuyStore.size() ) );
			}
			
			pw.println();
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用
	}
}

