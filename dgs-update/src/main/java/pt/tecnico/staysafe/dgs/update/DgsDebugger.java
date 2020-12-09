package pt.tecnico.staysafe.dgs.update;

public interface DgsDebugger {
	public static final Boolean _debug = false;

	public static void debug(String s) {
		if (_debug) {
			System.out.println(s);
		}
	}
}