package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.PrintWriter;
import java.util.ArrayList;
import java.util.Scanner;

public class Assembler {

	public static String assemble(File input, File output) throws FileNotFoundException {
		String returnValue = "success";
		Scanner sc = new Scanner(input);
		ArrayList<String> inText = new ArrayList<>();
		ArrayList<String> errors = new ArrayList<>();
		while (sc.hasNextLine()) {
			String line = sc.nextLine();
			inText.add(line);
		}
		sc.close();
		// copied all the lines in the file into inText array

		// checking for the first three errors
		int lineNum1 = 0, lineNum2 = 0, lineNum3 = 0;

		// first error if there is a blank line
		int blankLine = 0;
		boolean error1 = false;
		boolean blank = false;
		for (int i = 0; i < inText.size(); i++) {
			lineNum1 = i;
			String line = inText.get(i);
			if (line.trim().length() == 0) {
				// there is a blank line
				blank = true;
				blankLine = lineNum1;
			}
			if (line.trim().length() > 0 && blank == true) {
				returnValue = "Error: line " + (blankLine + 1) + " is a blank line";
				errors.add(returnValue);
				error1 = true;
				lineNum1 = blankLine;
				break;
			}
		}
		boolean error2 = false;
		if (error1) {
			// the first error occurred, try to find errors before that
			int errorLine = lineNum1;
			for (int i = 0; i < errorLine; i++) {
				lineNum2 = i;
				String line = inText.get(i);
				if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
					returnValue = "Error: line " + (lineNum2 + 1) + " starts with white space";
					errors.add(returnValue);
					error2 = true;
					break;
				}
			}
		} else {
			// error1 didn't occur
			for (int i = 0; i < inText.size(); i++) {
				lineNum2 = i;
				String line = inText.get(i);
				if (!blank) {
					if (line.charAt(0) == ' ' || line.charAt(0) == '\t') {
						returnValue = "Error: line " + (lineNum2 + 1) + " starts with white space";
						errors.add(returnValue);
						error2 = true;
						break;
					}
				}
			}
		}

		// checking for error3
		boolean error3 = false;
		int errorLine = 0;
		boolean error1o2 = false;
		if (error2) {
			error1o2 = true;
		} else if (error1) {
			error1o2 = true;
		}
		if (error1o2) {
			if (error2) {
				errorLine = lineNum2;
			} else if (error1) {
				errorLine = lineNum1;
			}
			for (int i = 0; i < errorLine; i++) {
				lineNum3 = i;
				String line = inText.get(i);
				if (line.trim().toUpperCase().equals("DATA")) {
					if (!line.trim().equals("DATA")) {
						returnValue = "Error: line " + (lineNum3 + 1) + " does not have DATA in upper case";
						errors.add(returnValue);
						error3 = true;
						break;
					}
				}
			}
		} else {
			for (int i = 0; i < inText.size(); i++) {
				lineNum3 = i;
				String line = inText.get(i);
				if (line.trim().toUpperCase().equals("DATA")) {
					if (!line.trim().equals("DATA")) {
						returnValue = "Error: line " + (lineNum3 + 1) + " does not have DATA in upper case";
						errors.add(returnValue);
						error3 = true;
						break;
					}
				}
			}
		}

		// check if there are no error to inText

		boolean inError = false;
		if (error1) {
			inError = true;
		} else if (error2) {
			inError = true;
		} else if (error3) {
			inError = true;
		}
		ArrayList<String> code = new ArrayList<>();
		ArrayList<String> data = new ArrayList<>();
		if (!inError) {
			// there are no errors so far...
			int dataLine = 0;
			// populating code array
			for (int i = 0; i < inText.size(); i++) {
				dataLine = i;
				String line = inText.get(i);
				if (line.trim().equals("DATA")) {
					break;
				} else {
					if (line.trim().length() > 0) {
						code.add(line);
					}
				}
			}
			// populating data array
			for (int i = dataLine + 1; i < inText.size(); i++) {
				String line = inText.get(i);
				if (line.trim().length() > 0) {
					data.add(line);
				}
			}
			ArrayList<String> outText = new ArrayList<>();

			// checking for errors in code
			boolean codeError = false;
			int codeNum = 0;
			for (int i = 0; i < code.size(); i++) {
				codeNum = i;
				String line = code.get(i);
				String[] parts = line.trim().split("\\s+");
				if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase())) {
					if (!(InstructionMap.sourceCodes.contains(parts[0]))) {
						returnValue = "Error: line " + (codeNum + 1)
								+ " does not have the instruction mnemonic in upper case";
						errors.add(returnValue);
						codeError = true;
						break;
					}
				}
				// if there is no error ...
				if (!codeError) {
					if (!InstructionMap.sourceCodes.contains(parts[0].toUpperCase())) {
						returnValue = "Error: line " + (codeNum + 1) + " has a bad mnemonic instruction";
						errors.add(returnValue);
						codeError = true;
						break;
					}
					if (InstructionMap.noArgument.contains(parts[0])) {
						if (parts.length != 1) {
							returnValue = "Error: line " + (codeNum + 1) + " has an illegal argument";
							errors.add(returnValue);
							codeError = true;
							break;
						}
					} else {
						if (parts.length == 1) {
							if (line.trim().length() != 0) {
								returnValue = "Error: line " + (codeNum + 1) + " is missing an argument";
								errors.add(returnValue);
								codeError = true;
								break;
							}
						}
						if (parts.length >= 3) {
							returnValue = "Error: line " + (codeNum + 1) + " has more than one argument";
							errors.add(returnValue);
							codeError = true;
							break;
						}
						if (parts.length == 2) {
							if (parts[1].startsWith("#")) {
								if (!(InstructionMap.immediateOK.contains(parts[0]))) {
									returnValue = "Error: line " + (codeNum + 1) + " argument is not immediate.";
									errors.add(returnValue);
									codeError = true;
									break;
								} else {
									parts[0] = parts[0] + "I";
									parts[1] = parts[1].substring(1);
									int arg = 0;
									try {
										arg = Integer.parseInt(parts[1], 16);
									} catch (NumberFormatException e) {
										returnValue = "Error: line " + (codeNum + 1)
												+ " does not have a numeric argument";
										errors.add(returnValue);
										codeError = true;
										break;
									}
								}
							} else if (parts[1].startsWith("&")) {
								if (!(InstructionMap.indirectOK.contains(parts[0]))) {
									returnValue = "Error: line " + (codeNum + 1) + " argument is not indirect.";
									errors.add(returnValue);
									codeError = true;
									break;
								} else {
									parts[0] = parts[0] + "N";
									parts[1] = parts[1].substring(1);
									int arg = 0;
									try {
										arg = Integer.parseInt(parts[1], 16);
									} catch (NumberFormatException e) {
										returnValue = "Error: line " + (codeNum + 1)
												+ " does not have a numeric argument";
										errors.add(returnValue);
										codeError = true;
										break;
									}
								}
							}
						}
					}
				}
			}

			boolean dataError = false;
			if (!codeError) {
				int dataNum = 0;
				for (int i = 0; i < data.size(); i++) {
					dataNum = i + dataLine;
					String line = data.get(i);
					String[] parts = line.trim().split("\\s+");
					if (parts.length != 2) {
						if (parts.length == 1) {
							if (!line.trim().equals("DATA")) {
								returnValue = "Error: line " + (dataNum + 1) + " is missing an argument";
								errors.add(returnValue);
								dataError = true;
								break;
							}
						} else {
							returnValue = "Error: line " + (dataNum + 1) + " has more than one argument";
							errors.add(returnValue);
							dataError = true;
							break;
						}
					} else {
						if (!line.trim().equals("DATA")) {
							int arg = 0;
							try {
								arg = Integer.parseInt(parts[0], 16);
								arg = Integer.parseInt(parts[1], 16);
							} catch (NumberFormatException e) {
								returnValue = "Error: line " + (dataNum + 1) + " does not have a numeric argument";
								errors.add(returnValue);
								dataError = true;
								break;
							}
						}
					}
				}
			}

			// check if there is no error in code
			boolean perfect = true;
			if (codeError) {
				perfect = false;
			} else if (dataError) {
				perfect = false;
			} else if (error1) {
				perfect = false;
			} else if (error2) {
				perfect = false;
			} else if (error3) {
				perfect = false;
			}
			if (perfect) {
				for (String s : code) {
					String[] parts = s.split("\\s+");
					if (parts.length == 1) {
						int opcode = InstructionMap.opcode.get(parts[0]);
						outText.add(Integer.toHexString(opcode).toUpperCase() + " 0");
					} else {
						if (parts[1].startsWith("#")) {
							parts[0] = parts[0] + "I";
							parts[1] = parts[1].substring(1);
							if (parts[0].equals("JUMPI"))
								parts[0] = "JMPI";
							if (parts[0].equals("JMPZI"))
								parts[0] = "JMZI";
						} else if (parts[1].startsWith("&")) {
							parts[0] = parts[0] + "N";
							parts[1] = parts[1].substring(1);
							if (parts[0].equals("JUMPN"))
								parts[0] = "JMPN";
						}
						int opcode = InstructionMap.opcode.get(parts[0]);
						outText.add(Integer.toHexString(opcode).toUpperCase() + " " + parts[1]);
					}

				}

				outText.add("-1");
				outText.addAll(data);

				if (returnValue.equals("success")) {
					try (PrintWriter outp = new PrintWriter(output)) {
						for (String str : outText) {
							outp.println(str);
						}
						outp.close();
					} catch (FileNotFoundException e) {
						returnValue = "Error: unable to open " + output;
					}

				}
			}
			// "you can process them as in Assignment 9"

		} else {
			// there is an error, so get line of where error occurred
			// which line occurred first
			if (lineNum1 < lineNum2 && lineNum1 < lineNum3) {
				errorLine = lineNum1;
			} else if (lineNum2 < lineNum1 && lineNum2 < lineNum3) {
				errorLine = lineNum2;
			} else if (lineNum3 < lineNum1 && lineNum3 < lineNum2) {
				errorLine = lineNum3;
			}
			// copy inText to code up to errorLine
			int dataLine = 0;
			boolean made2Data = false;
			for (int i = 0; i < errorLine; i++) {
				dataLine = i;
				String line = inText.get(i);
				if (line.trim().equals("DATA")) {
					made2Data = true;
					break;
				} else {
					code.add(line);
				}
			}
			if(made2Data){
				for (int i = dataLine; i < errorLine; i++) {
					String line = inText.get(i);
					if (line.trim().length() != 0) {
						data.add(line);
					}
				}
			}
			

			boolean codeError = false;
			int codeNum = 0;
			for (int i = 0; i < code.size(); i++) {
				codeNum = i;
				String line = code.get(i);
				String[] parts = line.trim().split("\\s+");
				if (InstructionMap.sourceCodes.contains(parts[0].toUpperCase())) {
					if (!(InstructionMap.sourceCodes.contains(parts[0]))) {
						returnValue = "Error: line " + (codeNum + 1)
								+ " does not have the instruction mnemonic in upper case";
						errors.add(returnValue);
						codeError = true;
						break;
					}
				}
				// if there is no error ...
				if (!codeError) {
					if (!InstructionMap.sourceCodes.contains(parts[0].toUpperCase())) {
						returnValue = "Error: line " + (codeNum + 1) + " has a bad mnemonic instruction";
						errors.add(returnValue);
						codeError = true;
						break;
					}
					if (InstructionMap.noArgument.contains(parts[0])) {
						if (parts.length != 1) {
							returnValue = "Error: line " + (codeNum + 1) + " has an illegal argument";
							errors.add(returnValue);
							codeError = true;
							break;
						}
					} else {
						if (parts.length == 1) {
							if (!(line.trim().length() == 0)) {
								returnValue = "Error: line " + (codeNum + 1) + " is missing an argument";
								errors.add(returnValue);
								codeError = true;
								break;
							}
						}
						if (parts.length >= 3) {
							returnValue = "Error: line " + (codeNum + 1) + " has more than one argument";
							errors.add(returnValue);
							codeError = true;
							break;
						}
						if (parts.length == 2) {
							if (parts[1].startsWith("#")) {
								if (!(InstructionMap.immediateOK.contains(parts[0]))) {
									returnValue = "Error: line " + (codeNum + 1) + " argument is not immediate.";
									errors.add(returnValue);
									codeError = true;
									break;
								} else {
									parts[0] = parts[0] + "I";
									parts[1] = parts[1].substring(1);
									int arg = 0;
									try {
										arg = Integer.parseInt(parts[1], 16);
									} catch (NumberFormatException e) {

										returnValue = "Error: line " + (codeNum + 1)
												+ " does not have a numeric argument";
										errors.add(returnValue);
										codeError = true;
										break;
									}
								}
							} else if (parts[1].startsWith("&")) {
								if (!(InstructionMap.indirectOK.contains(parts[0]))) {
									returnValue = "Error: line " + (codeNum + 1) + " argument is not indirect.";
									errors.add(returnValue);
									codeError = true;
								} else {
									parts[0] = parts[0] + "N";
									parts[1] = parts[1].substring(1);
									int arg = 0;
									try {
										arg = Integer.parseInt(parts[1], 16);
									} catch (NumberFormatException e) {
										returnValue = "Error: line " + (codeNum + 1)
												+ " does not have a numeric argument";
										errors.add(returnValue);
										codeError = true;
										break;
									}
								}
							}
						}
					}
				}
			}
			boolean dataError = false;
			int dataNum = 0;
			for (int i = 0; i < data.size(); i++) {
				dataNum = i + dataLine;
				String line = data.get(i);
				String[] parts = line.trim().split("\\s+");
				if (parts.length != 2) {
					if (parts.length == 1) {
						if (!line.trim().equals("DATA")) {
							returnValue = "Error: line " + (dataNum + 1) + " is missing an argument";
							errors.add(returnValue);
							dataError = true;
							break;
						}
					} else {
						returnValue = "Error: line " + (dataNum + 1) + " has more than one argument";
						errors.add(returnValue);
						dataError = true;
						break;
					}
				} else {
					int arg = 0;
					try {
						arg = Integer.parseInt(parts[0], 16);
						arg = Integer.parseInt(parts[1], 16);
					} catch (NumberFormatException e) {
						returnValue = "Error: line " + (dataNum + 1) + " does not have a numeric argument";
						errors.add(returnValue);
						dataError = true;
						break;
					}
				}
			}

		}
		return returnValue;
	}

//	public static void main(String[] args) throws FileNotFoundException {
//		System.out.println("Actual " + assemble(new File("05e.pasm"), new File("101rt.pexe")));
//	}

	public static void main(String[] args) throws FileNotFoundException {
		for (int i = 3; i <= 26; i++) {
			String s = "";
			if (i < 10) {
				s += "0";
			}
			s += i + "e";
			System.out.println(s + " Actual " + assemble(new File(s + ".pasm"), new File(s + ".pexe")));
		}
	}
}
