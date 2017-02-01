package project;

import java.io.File;
import java.io.FileNotFoundException;
import java.util.NoSuchElementException;
import java.util.Scanner;

public class Loader {

	public static String load(MachineModel model, File file, int codeOffset, int memoryOffset) {
		int codeSize = 0;
		if (model == null || file == null) {
			return null;
		}

		try {
			Scanner input = new Scanner(file);
			boolean incode = true;
			while (input.hasNextLine()) {
				String line = input.nextLine();
				Scanner parser = new Scanner(line);
				int firstInt = parser.nextInt(16);
				if (incode && firstInt == -1) {
					incode = false;
				} else if (incode && firstInt != -1) {
					int arg = parser.nextInt(16);
					model.setCode(codeOffset + codeSize, firstInt, arg);
					codeSize++;
				} else if (incode == false) {
					int memloc = firstInt + memoryOffset;
					int value = parser.nextInt(16);
					model.setData(memloc, value);
				}
				parser.close();
			}
			input.close();
			return "" + codeSize;
		} catch (ArrayIndexOutOfBoundsException e) {
			return "Array Index " + e.getMessage();
		} catch (NoSuchElementException e) {
			return "From Scanner: NoSuchElementException";
		} catch (FileNotFoundException e) {
			return "File " + file.getName() + " Not Found";
		}

	}

	public static void main(String[] args) {
		MachineModel model = new MachineModel();
		String s = Loader.load(model, new File("factorial.pexe"), 100, 200);
		for (int i = 100; i < 100 + Integer.parseInt(s); i++) {
			System.out.println(model.getCode().getText(i));
		}
		System.out.println(200 + " " + model.getData(200));
	}
}
