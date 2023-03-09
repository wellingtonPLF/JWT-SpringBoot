package project.br.useAuthentication;

public class Test {
	public static void main(String[] args) {
		var milliseconds1 = System.currentTimeMillis();
		var milliseconds2 = System.currentTimeMillis() + 1000 * 3600 * 23.9999;
		var milliseconds = milliseconds2 - milliseconds1;
		int seconds = (int) (milliseconds / 1000) % 60 ;
		int minutes = (int) ((milliseconds / (1000*60)) % 60);
		int hours   = (int) ((milliseconds / (1000*60*60)) % 24);
		System.out.println(String.format("Hora:%d\nMinuto:%d\nSegundo:%d", hours, minutes, seconds));
	}
}
