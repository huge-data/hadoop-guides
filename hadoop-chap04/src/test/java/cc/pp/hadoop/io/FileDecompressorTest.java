package cc.pp.hadoop.io;

import static org.hamcrest.CoreMatchers.is;
import static org.junit.Assert.assertThat;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Scanner;

import org.apache.hadoop.io.IOUtils;
import org.junit.Test;

public class FileDecompressorTest {

	@Test
	public void decompressesGzipFile() throws Exception {

		File file = File.createTempFile("file", ".gz");
		file.deleteOnExit();
		InputStream in = getClass().getResourceAsStream("/file.gz");
		IOUtils.copyBytes(in, new FileOutputStream(file), 4096, true);

		String path = file.getAbsolutePath();
		FileDecompressor.main(new String[] { path });

		String decompressedPath = path.substring(0, path.length() - 3);
		assertThat(readFile(new File(decompressedPath)), is("Text\n"));
	}

	@SuppressWarnings("resource")
	private String readFile(File file) throws FileNotFoundException {
		return new Scanner(file).useDelimiter("\\A").next();
	}

}
