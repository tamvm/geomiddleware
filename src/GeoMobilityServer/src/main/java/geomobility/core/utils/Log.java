package geomobility.core.utils;

/**
 * @author <a href="mailto:vo.mita.ov@gmail.com">VoMinhTam</a>
 */
public class Log {
	public static void d(String tag, String s) {
		System.out.println("[" + tag + "] " + s);
	}

	public static void e(String tag, String s) {
		System.err.println("[" + tag + "] " + s);
	}
}
