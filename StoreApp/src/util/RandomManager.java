package util;

import java.util.Random;

public class RandomManager {

	//입력한 숫자의 자릿수를 갖는 랜덤 수 반환
	public static String getRandomNumber(int len) {
		Random rand = new Random();
		rand.setSeed(System.currentTimeMillis());
		//0~9까지의 수 발생
		String r = "";
		for(int i = 0; i < len; i++) {
			r += rand.nextInt(10);
		}
		return r;
	}
}
