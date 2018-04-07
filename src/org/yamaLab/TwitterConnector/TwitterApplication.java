package org.yamaLab.TwitterConnector;

public interface TwitterApplication {
	public String getOutput();
	public boolean parseCommand(String cmd, String v);
}
