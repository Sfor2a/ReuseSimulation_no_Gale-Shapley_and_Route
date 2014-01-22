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
	private List < Housedata > HouseList = new ArrayList <> (); //�Ƃ̃��X�g 
	private List < Point > PointList = new ArrayList <> (); //�|�C���g���̂��̂̃��X�g
	private List < Routedata > RouteList = new ArrayList <> (); //���̃��X�g
	private int[][] RouteArray; //�n�}�s��
	private List < Housedata > BuyHouse = new ArrayList <> (); //���������Ƃ̃��X�g
	private List < Housedata > SellHouse = new ArrayList <> (); //���肽���Ƃ̃��X�g
	private List < Score > ScoreList = new ArrayList <> (); //�I�l�\�쐬�̃^���̃��X�g
	private List < Integer > ScoreCheckerBuy = new ArrayList <> (); //�_�u��X�R�A�̃`�F�b�N�p���X�g
	private List < Integer > ScoreCheckerSell = new ArrayList <> ();
    private Random rnd = new Random(); //Random�N���X�̃C���X�^���X��
	//�Z�b�^�[
	public void setHouseList ( Housedata Hus ) { //�n�E�X���X�g�Z�b�^�[
		HouseList.add ( Hus ); 
	}
	public void setPoint ( Point Pit ) { //�|�C���g���X�g�̃Z�b�^�[
		PointList.add ( Pit );
	}
	public void setRoute ( Routedata Rte ) { //�����X�g�̃Z�b�^�[
		RouteList.add ( Rte );
	}
	public void setRouteArraysize ( int size ) {
		RouteArray = new int [size][size];
	}
	public void setScoreList ( Score sco ) { //�X�R�A���X�g�Z�b�^�[
		ScoreList.add ( sco ); 
	}
	//�Q�b�^�[	
	public List < Housedata > getHouseList () { //House���X�g���̂��̂̃Q�b�^�[
		return HouseList;
	}
	public Housedata getHouseData ( int num ) { //�w��ԍ��̉Ƃ̃Q�b�^�[
		Housedata Hse = HouseList.get ( num );
		return Hse;
	}
	public List < Point > getPitList () { //PointList���̂��̂������Ă���Q�b�^�[
		List < Point > PitList = PointList; 
		return PitList;	
	}
	public List < Routedata > getRteList () { //RouteList���̂��̂������Ă���Q�b�^�[
		List < Routedata > RteList = RouteList; 
		return RteList;	
	}
	public Point getPoint ( int num ) { //�w��ԍ��̃|�C���g�̃Q�b�^�[
		Point Pit = PointList.get ( num );
		return Pit;
	}
	public Routedata getRoute ( int num ) { //�w��ԍ��̓��̃Q�b�^�[
		Routedata Rte = RouteList.get ( num );
		return Rte;
	}
	public List < Score > getScoreList () { //�X�R�AؽẴQ�b�^�[
		return ScoreList;
	}
	
	public Simulator() { //�f�[�^�̓ǂݍ���
		//�n�}�ƉƂ̍쐬
		String HouseListData = ".\\recycle\\Houselist.txt";
		String MapListData = ".\\recycle\\SqMap.txt";
		createHousedatafromFile ( HouseListData );
		createMapfromFile ( MapListData );
		CreateMapMatrix(); //�n�}�s��쐬�@������RouteArray[][]���n�}�s��ɂȂ�
	}
	private void Flug ( int num, int lineNum, String str ) { //�t���O�ɂ��G���[����
		switch ( num ) {
			case 1:
				System.out.print ( "Error: �����̃��C�t�T�C�N�������L�q����Ă��܂��A�ǂꂩ��ɓ��ꂵ�Ă�������" );
				break;
			case 2:
				System.out.print ( "Error: �������O�̃X�e�[�W�����łɑ��݂��Ă��܂�" );
				break;
			case 3:
				System.out.print ( "Error: �����o�H�̃p�X�����łɑ��݂��Ă��܂�" );
				break;
			case 4:
				System.out.print ( "Error: ���݂��Ȃ��X�e�[�W���Q�Ƃ��悤�Ƃ���p�X�ł��B�@�p�X�̖��O���C�����邩�A�Ԃ���m�F���Ă�������" );
				break;
			case 5:
				System.out.print ( "Error: �ǂݍ��݂Ɋ֌W�̂Ȃ��s���}������Ă��܂��B�@���̍s�͓ǂݔ�΂��܂��B�@�X�y���~�X�A�������`�F�b�N���Ă�������" );
				break;
			case 6:
				System.out.println ( "Error: �p�X�̋L�q�����ɖ�肪����܂� �Ȃ�����X�e�[�W�̓p�X��{�ɂ��A��ł�" );
				break;
			default:
				System.out.print ( "Error: ���̑��̃G���[���������Ă��܂��B�@�Y������s���m�F���邩�A�Ǘ��҂ɖ₢���킹�Ă�������" );
				break;
		}
		System.out.println ( "( �s�ԍ�" + lineNum + ": " + str + " )" ); //�G���[���b�Z�[�W���o������́A�K�����̃G���[�̂���s�����߂�
	}
	//�n�}��`��
	private Point findNamedPoint ( List < Point > Pit, String Name, String str, int CountLine ) { //�������O�̃|�C���g�������郁�\�b�h
		Point C = null; //�p�ӂ��Ă����Ȃ���Eclipse���G���[�f��
		for ( Iterator < Point > i = Pit.iterator (); i.hasNext (); ) { //PointList�̒��g�S���ɑ΂��Č�����������
			Point B = i.next ();
			if ( Name.equals ( B.getName () ) ) return C = B; //�����|�C���g����������C�֑���������Ԃ�
		}
		if ( C == null ) Flug ( 4, CountLine, str ); //null�̏ꍇ�̃G���[�����A�t���O���G���[�������\�b�h�֓�����
		return C;
	}	
	private int PointDuplicationSearch ( String PitDupName ) { //�|�C���g���̏d������
		List < Point > SearchList = getPitList (); //�����̃|�C���g���X�g��ǂݍ���
		int findExistingName = -1; //�������O����t���O
		if ( SearchList.size () != 0 ) { //��ԍŏ��Ɍ��o�����|�C���g���̓f�t�H���g�Œǉ�
			for ( int i = 0; i < SearchList.size (); i++ ) { //���ׂẴ|�C���g�̖��O�Ɋւ��đ�������Ō�����������
				if ( SearchList.get ( i ).getName ().equals ( PitDupName ) ) {
					findExistingName = 1; //�����̖��O�������
					break;
				}
				else findExistingName = -1; //�Ȃ����-1��Ԃ�
			}
		}
		return findExistingName;
	}
	private int RouteDuplicationSearch( String RteDupNameIn, String RteDupNameOut ) { //�����̏d������
		List < Routedata > SearchList = getRteList (); //�����̃|�C���g���X�g��ǂݍ���
		int findExistingName = -1; //�������O����t���O
		if ( SearchList.size () != 0 ) { //��ԍŏ��Ɍ��o���������̓f�t�H���g�Œǉ�
			for ( int i = 0; i < SearchList.size (); i++ ) { //���ׂẴ|�C���g�̖��O�Ɋւ��đ�������Ō�����������
				if ( SearchList.get ( i ).getName ().equals ( RteDupNameIn + ", " + RteDupNameOut ) ) {
					findExistingName = 1; //�����̖��O�������
					break;
				}
				else findExistingName = -1; //�Ȃ����-1��Ԃ�
			}
		}
		return findExistingName;
	}
	private void reedingMapName ( String str, int CountLine) { //�n�}�̖��O�ǂݍ���
		String[] strAry = str.split ( " ", 2 );
		if ( getName () != null ) Flug ( 1, CountLine, str ); //���łɖ��O���͂����Ă���i�K��LifeCycleName�s����񂾂�G���[���o��
		else setName ( strAry[1] ); //�s����"MapName "�����O���A�k���̏ꍇ���O�֑��
	}
	private void reedingPointName ( String str, int CountLine ) { //�|�C���g���̓ǂݍ���
		String[] strAry = str.split( ", ", 5 );
		if (  PointDuplicationSearch ( strAry[1] ) == -1 )
			new Point ( this, strAry[1], Integer.parseInt( strAry[2] ), Integer.parseInt( strAry[3] ), Integer.parseInt( strAry[4] ) ); //�s����"Point "�����O���|�C���g���O���擾���A�|�C���g�����̖��O�ƍ��W�ō쐬
		else Flug ( 2, CountLine, str ); //�G���[����
	}
	private void reedingRouteName ( String str, int CountLine ) { //�����̓ǂݍ���
		String[] strAry = str.split( " ", 3 );
		String strRoute = strAry[1]; //�s����"Route "�����O
		String[] RouteName = strRoute.split ( ",", 0 ); //�z��RouteName��","�ŕ����������̂���
		if ( RouteName.length < 3 ) { //���͓�̃|�C���g�����ނ��΂Ȃ���
			Point fromPoint = findNamedPoint ( PointList, RouteName[0], str, CountLine ); //�����������đ��
			Point toPoint = findNamedPoint ( PointList, RouteName[1], str, CountLine ); //�����������đ��				
			if ( fromPoint != null & toPoint != null ){ //null�łȂ��ꍇ�ɓ������ null�悯
				if ( RouteDuplicationSearch( RouteName[0], RouteName[1] ) == -1 ) { //�d������
					Routedata A = new Routedata (this, fromPoint, toPoint, Integer.parseInt( strAry[2] ) ); //�����쐬
					fromPoint.setRoutedataOut ( A ); //RouteOut�֑��
					toPoint.setRoutedataIn ( A ); //RouteIn�֑��
				}
				else Flug (3, CountLine, str); //�d�����𔭌��̏ꍇ�̃G���[
			}
		}
		else Flug ( 6, CountLine, str ); //���̏����G���[�r��
	}
	public void createMapfromFile ( String DataFolder ) { //�����Ƀt�@�C�����Ȃ��Ēn�}���쐬
		try {
			//�t�@�C�����[�_�[
			 File file = new File ( DataFolder );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//�����܂Ńt�@�C�����[�_�[
			 
			 String str = "0" ; //�ǂݎ��pString�֐� �������k���ɂ���Ǝ~�܂�i������O���j
			 int CountLine = 0; //�s�ԍ����Z�ϐ��i�G���[�����p�j
			
			//�ǂݍ��݃��\�b�h
			while ( str != null ) { //EOF��null�Ƃ��Č��o�A����܂ł͓ǂݍ��ݑ�����
				str = br.readLine (); CountLine++; //��s��ǂݍ��� �ǂݍ��ނ��тɎ���if���V�[�P���X�Ŕ���A�����ɍs�ԍ����Z
				if (str != null) { //�ǂݍ��ݍs��null�łȂ���΂��̃V�[�P���X�����s�A�s���Ŕ��ʂ���
					if ( str.startsWith ( "MapName" ) ) reedingMapName ( str, CountLine ); //���C�t�T�C�N�����̓ǂݍ���
					else if ( str.startsWith ( "Point" ) ) reedingPointName ( str, CountLine ); //�|�C���g�̓ǂݍ���
					else if ( str.startsWith ( "Route" ) ) reedingRouteName ( str, CountLine ); //���̓ǂݍ���
					else if ( str.startsWith ( "//" ) ); //�R�����g�������o����Ɖ������Ȃ�
					else Flug ( 5, CountLine, str ); //��̕��͂�ǂݍ��ނƃG���[��\�����Ė���
				}			
			}
			//�����܂œǂݍ��݃��\�b�h
			
			br.close(); //�t�@�C�������			
		} catch ( FileNotFoundException e ) { //��O����
			System.err.println ( e.getMessage () ); //�t�@�C���Ȃ��̗�O	
			System.exit ( 0 );
		} catch ( IOException e ) {
		    System.err.println ( e.getMessage () ); //IO�G���[��O
		    System.exit ( 0 );
		}
	}
	//�Ƃ̃f�[�^��ǂݍ��݉Ɠd�̂�肠�Ă镔��
	public void createHousedatafromFile ( String DataFolder ) { //�����Ƀt�@�C���̈ʒu�𓊂���ƃt�@�C�����烉�C�t�T�C�N���𐶐� �����ɓn�����͉̂Ƃ̃��X�g
		try {
			//�t�@�C�����[�_�[
			 File file = new File ( DataFolder );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//�����܂Ńt�@�C�����[�_�[
			 
			 String str = "0" ; //�ǂݎ��pString�֐� �������k���ɂ���Ǝ~�܂�i������O���j
			 int CountLine = 0; //�s�ԍ����Z�ϐ��i�G���[�����p�j
			
			//�ǂݍ��݃��\�b�h
			while ( str != null ) { //EOF��null�Ƃ��Č��o�A����܂ł͓ǂݍ��ݑ�����
				str = br.readLine (); CountLine++; //��s��ǂݍ��� �ǂݍ��ނ��тɎ���if���V�[�P���X�Ŕ���A�����ɍs�ԍ����Z
				if (str != null) { //�ǂݍ��ݍs��null�łȂ���΂��̃V�[�P���X�����s�A�s���Ŕ��ʂ���
					if ( str.startsWith ( "HouseName " ) ) reedingHouseName ( str, CountLine ); //�Ƃ̖��O�̓ǂݍ���
					else if ( str.startsWith ( "//" ) ); //�R�����g�������o����Ɖ������Ȃ�
					else Flug ( 5, CountLine, str ); //��̕��͂�ǂݍ��ނƃG���[��\�����Ė���
				}			
			}
			//�����܂œǂݍ��݃��\�b�h
			
			br.close(); //�t�@�C�������			
		} catch ( FileNotFoundException e ) { //��O����
			System.err.println ( e.getMessage () ); //�t�@�C���Ȃ��̗�O	
			System.exit ( 0 );
		} catch ( IOException e ) {
		    System.err.println ( e.getMessage () ); //IO�G���[��O
		    System.exit ( 0 );
		}
	}
	private void reedingHouseName ( String str, int countLine ) { //�Ƃ̖��O��ǂݍ��񂾂�@�Ƃ������
		try {
			String StAry[] = str.split ( " ", 2 ); //"Housename "������
			String Housename= ".\\recycle\\housedata\\" + StAry[1] + "_data.txt";
			//�t�@�C�����[�_�[
			 File file = new File ( Housename );
			 BufferedReader br = new BufferedReader ( new FileReader ( file ) );
			//�����܂Ńt�@�C�����[�_�[
			 
			 String str1 = "0" ; //�ǂݎ��pString�֐� �������k���ɂ���Ǝ~�܂�i������O���j
			 int CountLine_1 = 0; //�s�ԍ����Z�ϐ��i�G���[�����p�j
			 String[] HouseAry = new String[2];
			
			//�ǂݍ��݃��\�b�h
			while ( str1 != null ) { //EOF��null�Ƃ��Č��o�A����܂ł͓ǂݍ��ݑ�����
				str1 = br.readLine (); CountLine_1++; //��s��ǂݍ��� �ǂݍ��ނ��тɎ���if���V�[�P���X�Ŕ���A�����ɍs�ԍ����Z
				if (str1 != null) { //�ǂݍ��ݍs��null�łȂ���΂��̃V�[�P���X�����s�A�s���Ŕ��ʂ���
					if ( str1.startsWith ( "Name " ) ) {
						String[] strAry = str1.split ( " ", 2 );
						HouseAry[0] = strAry[1];
					}
					else if ( str1.startsWith ( "Coin " ) ) {
						String[] strAry = str1.split ( " ", 2 );
						HouseAry[1] = strAry[1];		
					}
					else if ( str1.startsWith ( "//" ) ); //�R�����g�������o����Ɖ������Ȃ�
					else Flug ( 5, CountLine_1, str1 ); //��̕��͂�ǂݍ��ނƃG���[��\�����Ė���
				}			
			}
			//�����܂œǂݍ��݃��\�b�h
			br.close (); //�t�@�C�������
			new Housedata ( HouseAry[0], Integer.parseInt ( HouseAry[1] ), this ); //�Ƃ̍쐬
		} catch ( FileNotFoundException e ) { //��O����
			System.err.println ( e.getMessage () ); //�t�@�C���Ȃ��̗�O	
			System.exit ( 0 );
		} catch ( IOException e ) {
		    System.err.println ( e.getMessage () ); //IO�G���[��O
		    System.exit ( 0 );
		}
	}
	//�n�}�s��쐬��
	private void CreateMapMatrix () { //�n�}�s��쐬
		int PointNum = getPitList ().size (); //�n�_�̐�
		setRouteArraysize ( PointNum );
		for ( int i=0; i< RouteArray.length; i++ ) { //�n�}�s��̏�����
			for ( int j =0; j< RouteArray[0].length; j++) {
				RouteArray[i][j] = 0;
			}
		}
		for ( int i = 0; i < PointNum; i++ ) { //�n�_1���炭�肩����
			int RouteOutNum = getPitList ().get ( i ).getRteOutList ().size ();
			for ( int j = 0; j < RouteOutNum; j++ ) {
				double x1 = getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getX ();
				double y1 = getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getY ();
				double x2 = getPitList ().get ( i ).getX ();
				double y2 = getPitList ().get ( i ).getY ();
				double Length = Math.sqrt ( ( x2 - x1 ) * ( x2 - x1 ) + ( y2 - y1 ) * ( y2 - y1 ) ); //���΂�Ă���2�_�Ԃ̋������v�Z
				
				String AiteName = getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getName ();
				int l = 0;
				
				for ( int k = 0; k < PointNum; k++ ) { //�Ȃ����̃|�C���g���������āA���X�g�̔ԍ�����ɓ����
					if ( AiteName.equals( getPitList ().get ( k ).getName () ) ){
						l = k; 
						break;
					}
				}
				
				int Costs = getRouteCosts ( getPitList ().get ( i ).getRoutedataOut ( j ).getTo ().getName (),
						getPitList ().get ( i ).getName() ); //���H�̃R�X�g�擾
				
				RouteArray[i][l] = (int) Length * Costs; //���������i������
				RouteArray[l][i] = (int) Length * Costs ; //���������i����
			}
			
		}
	}
	private int getRouteCosts ( String nam1, String nam2 ) { //���̃R�X�g�𔭌�
		int ret = 0;
		for ( int i=0; i < getRteList().size(); i++ ) {
			if ( getRteList().get(i).getTo().getName().equals ( nam1 ) && getRteList().get(i).getFrom().getName().equals ( nam2 ) ) {
				ret = getRteList().get(i).getMoveCosts();
			}
		}
		return ret;
	}
	//�ŒZ�o�H��������
	private int MARange ( Housedata B1, Housedata B2 ) { //�Ƃ̃f�[�^����ŒZ������Ԃ��� ���邢�݂����̃Q�b�^�[
		int PointNum = getPitList().size(); //�n�}���f���̍s��
		
		String Name1 = B1.getName(); //�Ƃ̖��O���擾
		String Name2 = B2.getName();
		
		for ( int i = 0; i < getPitList().size(); i++ ) { //�Ƃ̖��O���烋�[�g�𒲂ׂ�
			if ( Name1.equals ( getPitList().get(i).getName() ) ) {
				for ( int j = 0; j < getPitList().size(); j++ ) {
					if ( Name2.equals ( getPitList().get(j).getName() ) ) {
						return MinimumAccessProblem ( RouteArray, PointNum, i, j ); //�s��ƂȂ��������āA�����ƃ��[�g��Ԃ�
					}
				}
			}
		}
		return 0;
	}
 	private int MinimumAccessProblem ( int[][] RouteArray, int N, int Start, int Goal ) { //�ŒZ�o�H�����s��
 		boolean Visited[] = new boolean[N]; //�ŒZ�������m�肵���n�_
		int Dist[] = new int[N]; //�ŏ����炻���܂ł̋���
		int prev[] = new int[N]; //�܂��ɂ����n�_
		int nodes[] = new int[N]; //�X�ԍ�
		for ( int i = 0; i < N; i++ ) {
			Visited[i] = false; //�S�Ă̊X�������ĂȂ���Ԃɂ���
			Dist[i] = Integer.MAX_VALUE; //�S�Ă̊X�܂ł̋����𖳌��ɂ���(2147483647
			nodes[i] = i; //�X�ԍ�����			
		}
		Dist[Start] = 0; //�ŏ��̒n�_�Ȃ̂œ��R������0�ɂȂ�
		prev[Start] = Start; // �ŏ��ɑI���������̑O�̒��͖����̂ŁA�Ƃ肠���������̒������Ă���
		int pos = Start; //���ݒn��ݒ�
		MinimumRouteSearch ( Visited, RouteArray, Dist, prev, pos, N, nodes ); //�ŏ��o�H�T��
		
		//�C�J�R���X�g���N�^�I���܂ŕ\���̃R�}���h�A�Ԃ����Ⴏ�ꕔ�̂����Ă������@�܂�o�H�̕\���@�������̂�Dist�Ɋi�[����Ă�̂Ŗ��Ȃ�
		@SuppressWarnings("unused")
		String str = Start + "����" + Goal + "�܂ł̍ŒZ���[�g��";
		int node = prev[Goal];
		String track = Integer.toString ( Goal );
		for ( int i = 0; i < N; i++ ) {
			track += node;
			if ( node == Start ) break;
			node = prev[node];
		}
		for ( int i = track.length () - 1; i >= 0; i--) str += " => " + track.charAt ( i );
		//if ( node == Start ) System.out.println ( str );
		//else System.out.println ( "���[�g�Ȃ�" );
		//System.out.println ( "����"+ Dist[Goal] );
		return Dist[Goal]; //�����Q�Ɨp
	}
	private void MinimumRouteSearch ( boolean[] Visited, int[][] RouteArray, int[] Dist, int[] prev, int pos, int N, int[] nodes ) { //�ŏ��o�H�T����
		while (true) {
			Visited[pos] = true; //���̏����������Visited�t���O�𗧂Ă�
			for ( int x = 0; x < N; x++ ) {
				if ( Visited[x] ) continue; //���łɂ������X�Ȃ炱�̏����̓X���[
				if ( RouteArray[pos][x] > 0) { //�o�H������ꍇ���̏��������
					int d = Dist[pos] + RouteArray[pos][x]; //���ݒn���玟�̒n�_�܂ł̋���
					if ( d < Dist[x] ) { //���̒n�_ + ���̒n�_�����X�����Ă���������菬������΍X�V
						Dist[x] = d;
						prev[x] = pos;
					}	
				}
			}
			//�܂��K��Ă��Ȃ����̒��ŁA�X�^�[�g�n�_����̋������ŏ��ɂȂ钬��I��
			int next = -1; //���̊X�T���t���O
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
	//���������G���g���[��
	private void SellHouseSelection ( int HouseNum ) { //���肽���Ǝv���Ƃ�I�яo��
		for ( int i = 0; i < HouseNum; i++ ) { //���ׂẲƂɂ���
			Housedata Target = getHouseList ().get ( i );
			if ( Target.getHAList().size() >= 2 ) SellHouse.add ( Target ); //���p�����P�F�^�[�Q�b�g���①�ɂ��Q�����Ă���Ƃ�
			else {
				if ( Target.getHAList().size() != 0 ) { //�Ɠd���������Ă���ꍇ�ɂ̂ݎ��s
					for ( int j = 0; j < Target.getHAList().size(); j++ ) {
						int Count = Target.getHAList().get(j).getUseTernCount();
						if ( Count >= 5 ) { //����2�Fn�^�[���ȏ��������O����@����5
							int ran = rnd.nextInt(100); //0�`99�܂ł̂����P�𐶐�
							if ( ran > 0 && ran < 49 ) { //����2����:n%�̊m���Ń^�[�Q�b�g�𔄂肽���Ǝv���ƂɎw�� ����50%
								if ( SellHouse.size() == 0 ) SellHouse.add(Target); //BuyHouse����Ȃ�ǉ�
								else { //�����łȂ����
									if ( SellHouse.indexOf ( Target ) == -1 ) SellHouse.add(Target); //���łɃ^�[�Q�b�g���ǉ�����Ă��Ȃ��ꍇ�Ɍ���ǉ�
								}
							}
						}
						else break;
					}
				}
			}
		}
	}
	private void BuyHouseSelection ( int HouseNum ) { //���������Ƃ�I�т���
		for ( int i = 0; i < HouseNum; i++ ) { //���̉Ɠd�ɖO�������ǂ����𔻒f
			Housedata Target = getHouseList ().get ( i );
			if ( SellHouse.indexOf ( Target ) == -1 ) { //Target�����łɔ��肽���ƂɂȂ��Ă��Ȃ����Ƃ�����
				if ( Target.getHAList().size() == 0 ) BuyHouse.add ( Target ); //��������P�F�^�[�Q�b�g���①�ɂ�0�����Ă���Ƃ�
				else {
					for ( int j = 0; j < Target.getHAList().size(); j++ ) {
						int Count = Target.getHAList().get(j).getUseTernCount();
						if ( Count >= 5 ) { //����2�Fn�^�[���ȏ��������O����@����5
							int ran = rnd.nextInt(100); //0�`99�܂ł̂����P�𐶐�
							if ( ran > 0 && ran < 49 ) { //����2����:n%�̊m���Ń^�[�Q�b�g�����������Ǝv���ƂɎw�� ����50%
								if ( BuyHouse.size() == 0 ) {
									BuyHouse.add(Target); //BuyHouse����Ȃ�ǉ�
								}
								else { //�����łȂ����
									if ( BuyHouse.indexOf ( Target ) == -1 ) BuyHouse.add(Target); //���łɃ^�[�Q�b�g���ǉ�����Ă��Ȃ��ꍇ�Ɍ���ǉ�
								}
							}
						}
						else break;
					}
				}
			}
		}
	}

	//�X�R�A�쐬��
	private void ScoreCreator( Housedata Buy1, Housedata Sell1 ) { //�X�R�A���쐬����
		int Mem_i = 0; //i�̋L���p
		if ( Sell1.getHAList ().size () >= 2 ) { //�①�ɂ�2�������Ă���Αϋv�x�̂Ђ�������������悤�ɂ���
			int LowDur = Integer.MAX_VALUE;
			for( int i = 0; i < Sell1.getHAList().size(); i++ ) 
				if ( LowDur >= Sell1.getHAList ().get ( i ).getDurability () ) Mem_i = i; //���ϋv�x�̒Ⴂ�ق����擾����	
		}
		//��r�p�f�[�^�i���鑤�j
		HAdata setSellTargetHA = Sell1.getHAList ().get ( Mem_i ); //2�Ȃ���΂P�ځA�Q������Ώ�ł��肩�����ǂ��炩���g���B
		int setSellDur = setSellTargetHA.getDurability();
		int setSellSpec = Integer.parseInt( setSellTargetHA.getSpec() );
		int setSellValue = setSellTargetHA.getTermValue();
		//��r�p�f�[�^�i�������j
		int setBuySpec = 1;
		int setBuyCost = 1;
		HAdata setBuyTargetHA = null;
		if ( Buy1.getHAList ().size () > 0 ) setBuyTargetHA = Buy1.getHAList ().get ( 0 ); //���������Ƃ̔�r�p�Ɠd��p�Ӂi�Ɠd�����Ă���΁@�����Ă��Ȃ��ƂP�C�P�C���K�p
		for ( int i = 0; i < Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList ().size (); i++ ) {
			Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList ().get ( i );
			if ( setSellDur > Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList().get ( i ).getDurability () ) 
				setBuyCost = Buy1.getHousesWishList ().get ( 0 ).getHAsWishValList ().get ( i ).getCost (); //����̑ϋv�x�ɂӂ��킵��������i����������
		}
		if ( setBuyTargetHA != null ) setBuySpec = Integer.parseInt ( setBuyTargetHA.getSpec () ); //�Ɠd�����݂��Ă���΃X�y�b�N���Ăяo��
		
		//�����̌Ăяo��
		int Dist = MARange ( Buy1, Sell1 );
		//�X�R�A�쐬 �i�X�y�b�N���̊������ϋv�x��������i�̊����������j
		double ValuePercent = ( ( double ) Math.abs( ( ( double ) setSellValue - (double) setBuyCost ) ) / ( double ) setBuyCost );
		double SpecPercent = ( ( double ) Math.abs ( ( double ) setSellSpec -  ( double )setBuySpec )  / ( double ) setBuySpec );
		int ScoreBuy = ( int ) ( ( double ) ( 1 / ValuePercent ) + ( double ) ( 1 / SpecPercent ) + ( 100 - setSellDur ) ); //�������̑���ւ̃X�R�A
		if ( ScoreCheckerBuy.size() == 0 ) {
			ScoreCheckerBuy.add ( ScoreBuy ); //�_�u���Ă�X�R�A���Ȃ��悤�ɉ��Z���镔���@�X�R�A�`�F�b�J�[����Ȃ�`�F�b�J�[�ɒǉ�
		}
		else { //�Ȃɂ��͂����Ă���Ƃ�
			if ( ScoreCheckerBuy.indexOf( ScoreBuy ) == -1 ) ScoreCheckerBuy.add ( ScoreBuy ); //�`�F�b�N���X�g���m�F���A�Y�����Ȃ���Βǉ�
			else { //�Y���������1���Z���āA�ǉ�����
				ScoreBuy = ScoreBuy + 1;
				ScoreCheckerBuy.add ( ScoreBuy );
			}	
		}
		int ScoreSell = setBuyCost; //���鑤�̑���ւ̃X�R�A
		if ( ScoreCheckerSell.size() == 0 ) ScoreCheckerSell.add ( ScoreSell ); //�_�u���Ă�X�R�A���Ȃ��悤�ɉ��Z���镔���@�X�R�A�`�F�b�J�[����Ȃ�`�F�b�J�[�ɒǉ�
		else { //�Ȃɂ��͂����Ă���Ƃ�
			if ( ScoreCheckerSell.indexOf( ScoreSell ) == -1 ) ScoreCheckerSell.add ( ScoreSell ); //�`�F�b�N���X�g���m�F���A�Y�����Ȃ���Βǉ�
			else { //�Y���������1���Z���āA�ǉ�����
				ScoreSell = ScoreSell + 1;
				ScoreCheckerBuy.add ( ScoreSell );
			}	
		}
		new Score ( ScoreBuy, ScoreSell, Buy1, Sell1, setSellTargetHA, this, Dist ); //�I�l�\���쐬���邽�߂̃X�R�A�f�[�^�̍쐬
	}
	//�����[�X���s��
	@SuppressWarnings("unchecked")
	private void DoReuse ( WriteOutData WOD ) { //�����[�X���s��
		System.out.println(getHouseList().get(2).getHAList().size());
		List < Score > BuyScoreSortor = getScoreList(); //�������̕��ёւ��悤���X�g
		Collections.sort ( BuyScoreSortor, new BuyScoreComparator () ); //�������̃X�R�A�ŕ��ёւ������Ԃ悢��
		Score ReuseTarget = null;
		for ( int i = 0; i < BuyHouse.size(); i++ ) { //�ꌬ�������Ă���
			out: {for ( int j = 0; j < getScoreList().size(); j++ ) { //�X�R�A���X�g����^�[�Q�b�g��T��
				if ( getScoreList ().get ( j ).getBuyHouse() == BuyHouse.get(i) ) {
					ReuseTarget = getScoreList ().get ( j ); //���񂴂������ԍŏ��ɂԂ����������炻���Ɨe�͂Ȃ������[�X
					int SellHouseNum = getHouseList ().indexOf ( ReuseTarget.getSellHouse () ); //����Ƃ�T��
					int SellHANum = getHouseList ().get ( SellHouseNum ).getHAList ().indexOf ( ReuseTarget.getSellHA() ); //���p�Ɠd��T��
					if ( SellHANum != -1 ) {
						getHouseList ().get ( SellHouseNum ).getHAList ().get ( SellHANum ).setUseTernCount ( 0 ); //�g�p�Ώۂ��ړ�����̂Ŏg�p�񐔂����Z�b�g
						getHouseList ().get ( SellHouseNum ).getHAList ().get( SellHANum ).setExchangecount ( getHouseList ().get ( SellHouseNum ).getHAList ().get( SellHANum ).getExchangecount () + 1 ); //�����[�X�񐔂𑝂₷
						getHouseList ().get( i ).setHAdata( getHouseList ().get ( SellHouseNum ).getHAList ().get( SellHANum ) ); //�����[�X�ΏۉƓd���ړ�
						getHouseList ().get( SellHouseNum ).getHAList ().remove ( SellHANum ); //�ړ��I��
						getHouseList ().get( i ).setCoin ( getHouseList ().get( i ).getCoin() - ReuseTarget.getScoreforSell() ); //�����̎x����
						getHouseList ().get( SellHouseNum ).setCoin ( getHouseList ().get( SellHouseNum ).getCoin() + ReuseTarget.getScoreforSell() ); //�����̎󂯎��
						//�f�[�^�����o����
						WOD.WriteOut( ReuseTarget );
						break out;
					}
					
				}
			}
		}
		}
	}
	//�^�[���I��������
	private void MinusDurAllHA () { //�S�ẲƓd�̑ϋv�x�������A�V���Ȍ��݉��i��ݒ肵�A�g�p�񐔂𑝂₷
		for ( int i = 0 ; i < getHouseList ().size (); i++ ) { //�S�ẲƂ�
			if ( getHouseList ().get ( i ).getHAList ().size () != 0 ) { //�����Ɠd��0�łȂ�����
				for ( int j = 0; j <getHouseList ().get ( i ).getHAList ().size (); j++ ) { //��������S�Ɠd�ɂ�����
					getHouseList ().get ( i ).getHAList ().get( j ).setDurability ( getHouseList ().get ( i ).getHAList ().get( j ).getDurability ()
							- getHouseList ().get ( i ).getHAList ().get( j ).getMinusDur () ) ; //�ϋv�x�����Y
					double TermValue = ( double ) getHouseList ().get ( i ).getHAList ().get( j ).getMaxValue() *
							( ( double ) getHouseList ().get ( i ).getHAList ().get( j ).getDurability () / ( double ) 100  ); //���݉��i�v�Z
					getHouseList ().get ( i ).getHAList ().get( j ).setTermValue( ( int ) TermValue ); //�V�������݉��i��ݒ�					
					getHouseList ().get ( i ).getHAList ().get( j ).setUseTernCount( getHouseList ().get ( i ).getHAList ().get( j ).getUseTernCount() + 1 ); //�g�p�񐔂𑝂₷�B
				}
			}
		}
	}
	private void ClearList () { //���X�g��z�����ɂ���
		BuyHouse.clear(); //��������ƃ��X�g���N���A
		SellHouse.clear();
		ScoreList.clear(); //�X�R�A���X�g���N���A
	}
	
	@SuppressWarnings("unused")
	private void QView ( int[][] View ) { //�I�l�\�̕\��
		for ( int i = 0; i < View.length; i++ ) {
			System.out.print ( i + " {\t" );
			for ( int j = 0; j < View[0].length; j++ ) {
				System.out.print( View[i][j] + "\t" );
			}
			System.out.println("}");
		}
		System.out.println();
	}
	
	//�V�~�����[�^�{��
	public void SimulationStart () { //�①�ɂɌ��肵���V�~�����[�V�������s��
		//���������G���g���[
		int HouseNumber = getHouseList ().size (); //���ׂẲƂ̐�
		SellHouseSelection ( HouseNumber ); //���肽���ƑI��
		BuyHouseSelection ( HouseNumber ); //���������ƑI��
		WriteOutData WOD = new WriteOutData( SellHouse.size(), BuyHouse.size() );
		WOD.Adjust( SellHouse.size(), BuyHouse.size() );
		if ( BuyHouse.size() != 0 && SellHouse.size() != 0 ) {
			//�X�R�A�쐬
			for ( int i = 0; i < BuyHouse.size(); i++ ) { //������������ɑ΂���X�R�A���쐬
				for ( int j = 0; j < SellHouse.size(); j++ ) {
					ScoreCreator( BuyHouse.get ( i ), SellHouse.get ( j ) ); //i�ɂ������Ă��ׂĂ�j�̃X�R�A���m�F
				}
			}
			DoReuse( WOD ); //�����[�X
			System.out.print("�����[�X���� ");
		}		
		else {
			System.out.print("�����[�X�Ȃ� ");
			WOD.None();
		}
		
		//�^�[���I������
		MinusDurAllHA(); //�ϋv�x���ւ炵�g�p�񐔂𑝂₷�Ȃ�
		ClearList(); //���X�g����ɂ���
		System.out.println("�^�[���G���h");
	}
}