import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class readDat {

	private String fileName;
	private byte[] fileData; //file Binary

	public readDat(String fileName) {
		this.fileName = fileName;

		try {
			this.fileOpen();
		} catch (IOException e) {
			System.out.println("No such file!");
		}
	}

	public readDat() {
		System.out.println("No file input!");
	}

	private void fileOpen() throws IOException {
		Path F = Paths.get(fileName); //get file Path
		fileData = Files.readAllBytes(F); // set binary to fileData
	}

	public byte[] getFileData() {
		return fileData;
	}

	public void printData() {
		if (fileData.length != 0) {
			for (int i = 0; i < 16; i++)
				System.out.printf("%02X ", i);
			System.out.println("\n");
			for (int i = 0; i < fileData.length; i++) {
				if ((i + 1) % 16 != 0)
					System.out.printf("%02X ", fileData[i]);
				else
					System.out.printf("%02X\n", fileData[i]);
			}
		}
	}
}
