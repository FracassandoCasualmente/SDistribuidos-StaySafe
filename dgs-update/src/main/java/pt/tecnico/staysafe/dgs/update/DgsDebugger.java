package pt.tecnico.staysafe.dgs.update;

public interface DgsDebugger {
	public static final Boolean _debug = true;

	public static void debug(String s) {
		if (_debug) {
			System.out.println(s);
		}
	}
}