package org.yamaLab.pukiwikiCommunicator.language;

public interface InterpreterInterface
{
	public StringBuffer getOutputText();
	public boolean isTracing();
	public StringBuffer parseCommand(String x);
	public StringBuffer parseCommandWithReturn(String x, String y);
	public InterpreterInterface lookUp(String x);
}
