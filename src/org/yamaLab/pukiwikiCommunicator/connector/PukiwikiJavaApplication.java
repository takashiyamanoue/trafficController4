package org.yamaLab.pukiwikiCommunicator.connector;

public interface PukiwikiJavaApplication {
	public String getOutput();
	public void setInput(String x);
	public void setVisible(boolean f);
	public void setSaveButtonDebugFrame(SaveButtonDebugFrame f);
	public void error(String x);
}
