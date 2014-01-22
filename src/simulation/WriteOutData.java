package simulation;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;

public class WriteOutData {
	
	File file = new File ( ".\\recycle\\WriteOut\\WritedOut_data.csv" ); //書き込むファイル名
	
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
	public void WriteOut ( Score ReuseTarget ) {
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( "," + ReuseTarget.getBuyHouse().getName() + "," +ReuseTarget.getSellHouse().getName() + "," + ReuseTarget.getDist() + "," + ReuseTarget.getScoreforSell()+ "," );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用意
	}
	public void Adjust( int SellNum, int BuyNum ) { //お見合い参加人数
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( ",売れた家の数," + SellNum+ ",買えた家の数," + BuyNum + "," );
			pw.println ( ",買い取りする家,売る家,そこまでの距離,売買価格," );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用意
	}
	public void None() { //取引なしの書き出し
		PrintWriter pw;
		try {
			pw = new PrintWriter (new BufferedWriter ( new FileWriter ( file, true ) ) );
			pw.println ( "取引なし" );
			pw.close();
		} catch ( IOException e ) {
			System.err.println ( "File cannot be Writed." );
		} //printlnの用
	}
}

