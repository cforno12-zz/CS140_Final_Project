package project;

public class CPU {
	
	private int accumulator, instructionPointer, memoryBase;

	public int getAccumulator() {
		return accumulator;
	}

	public int getInstructionPointer() {
		return instructionPointer;
	}

	public int getMemoryBase() {
		return memoryBase;
	}
	
	public void incrementIP(){
		instructionPointer++;
	}

	public void setAccumulator(int accumulator) {
		this.accumulator = accumulator;
	}

	public void setInstructionPointer(int instructionPointer) {
		this.instructionPointer = instructionPointer;
	}

	public void setMemoryBase(int memoryBase) {
		this.memoryBase = memoryBase;
	}
	
}
