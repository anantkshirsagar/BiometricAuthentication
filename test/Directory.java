import java.io.File;
import java.io.IOException;

public class Directory {
	public static void main(String[] args) throws IOException {
		String dir = System.getProperty("user.dir");
		System.out.println(dir);
		File file = new File(dir + "/..");
		String path = file.getCanonicalPath();
		System.out.println(path);
	}
}
