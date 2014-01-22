package simulation;



public class Main {

	
	public static void main(String[] args) {
		Simulator Sim1 = new Simulator(); //地図と家の読み込みをコンストラクタで行う、以降のシミュはsim1.simstartを動かすと１ターム実行される
		for ( int i = 0; i < 25; i++ ) {
			Sim1.SimulationStart();
		}
	}
}
