package util;


//앞으로 파일과 관련된 여러 작업을 전담하게 될 파일 제어클래스 정의

public class FileManager {

	//넘겨받은 경로를 통해 확장자만 추출해보기
	
	public static String getExtend(String path) {
		String ext = path.substring(path.lastIndexOf(".")+1, path.length());
		return ext;
	}
}