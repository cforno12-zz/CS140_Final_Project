package project;

public class Job {
	private int startCodeIndex, codeSize, startMemoryIndex, currentIP, currentAcc;
	private States currentState;

	public int getStartCodeIndex() {
		return startCodeIndex;
	}

	public void setStartCodeIndex(int startcodeIndex) {
		this.startCodeIndex = startcodeIndex;
	}

	public int getCodeSize() {
		return codeSize;
	}

	public void setCodeSize(int codeSize) {
		this.codeSize = codeSize;
	}

	public int getStartMemoryIndex() {
		return startMemoryIndex;
	}

	public void setStartMemoryIndex(int startmemoryIndex) {
		this.startMemoryIndex = startmemoryIndex;
	}

	public int getCurrentIP() {
		return currentIP;
	}

	public void setCurrentIP(int currentIP) {
		this.currentIP = currentIP;
	}

	public int getCurrentAcc() {
		return currentAcc;
	}

	public void setCurrentAcc(int currentAcc) {
		this.currentAcc = currentAcc;
	}

	public States getCurrentState() {
		return currentState;
	}

	public void setCurrentState(States currentState) {
		this.currentState = currentState;
	}
	
	public void reset(){
		codeSize = 0;
		currentAcc = 0;
		currentIP = startCodeIndex;
		currentState = States.NOTHING_LOADED;
	}
}
