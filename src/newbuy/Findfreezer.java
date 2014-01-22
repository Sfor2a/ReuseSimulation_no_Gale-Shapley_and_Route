package newbuy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class Findfreezer {
	List < freezer > FFL = new ArrayList <> (); //絞り込み終了後のあたらしい冷蔵庫リスト
	List < freezer > CFFL = new ArrayList <> (); //コストでも絞り込み終了後の新しいリスト
	
	public void setFFL ( freezer freezer ) {
		FFL.add ( freezer );
	}
	public void setCFFL ( freezer freezer ) {
		CFFL.add ( freezer );
	}
	public List < freezer > getFFL () {
		return FFL;
	}
	public List < freezer > getCFFL () {
		return CFFL;
	}
	
	public Findfreezer () {
	}
	public int personselector () {
		Random rnd = new Random();
		int people = rnd.nextInt( 8 ) + 1;
		return people;
	}
	
	public int personcountspec ( int P ) { //何人家族かをいれるとそれに必要な範囲のスペックを教えてくれる
		int spec = 0;
		if ( P == 1 ) spec = 140;
		else spec = ( 70 * P ) + 170;
		return spec; 
	}
	
	public void findfreezerspec ( int spec, ReadFileFreezer RF ) { //スペックで冷蔵庫絞り込み
		List < freezer > FL = RF.getfreezerlist(); //冷蔵庫リストを引っ張ってくる
		int minspec = ( int ) ( spec * 0.91 ); //検索範囲の下限上限をせってい
		int maxspec = ( int ) ( spec * 1.09 ); //この範囲のスペックで冷蔵庫を探す
		for ( int i = 0; i < FL.size(); i++ ) {
			int targetspec = FL.get(i).getSpec();
			if ( targetspec > minspec && targetspec < maxspec ) {
				setFFL ( FL.get(i) ); //絞り込んだ家具をリストに追加
				//System.out.println("スペック上限"+maxspec+"スペック下限"+minspec+"選んだスペック"+FL.get(i).getSpec());
			}
		}
	}
	
	@SuppressWarnings("unchecked")
	public void findfreezercost ( int cost ) {
		List < freezer > CFL = getFFL();
		Collections.sort(CFL, new FreezerComparator());
		for( int i = 0; i < CFL.size(); i++ ) {
			if ( CFL.get(i).getCost() > ( int ) ( cost * 0.9 ) && CFL.get(i).getCost() < ( int ) ( cost * 1.1 ) ){
				setCFFL ( CFL.get(i) );
				//System.out.println( "コスト上限"+ ( cost * 1.1 ) +"コスト下限"+ ( cost * 0.9 ) +"選んだコスト"+CFL.get(i).getCost() );
			}
		}
	}
	
	public freezer FindFreezerLast () { //freezerを選んで返す
		freezer CFL = null;
		List < freezer > LFL = getCFFL();
		Random rnd = new Random();
		int i = rnd.nextInt( LFL.size() );
		CFL = LFL.get(i);		
		return CFL;
	}
}

@SuppressWarnings("rawtypes")
class FreezerComparator implements java.util.Comparator {
	public int compare ( Object s, Object t ) {
		return ( ( freezer ) s ).getCost() - ( ( freezer ) t ).getCost();
	}
}

