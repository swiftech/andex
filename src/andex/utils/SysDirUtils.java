package andex.utils;

import android.os.Environment;

import java.io.File;
import java.io.FileNotFoundException;

/**
 * Experimental
 * Created by yuxing on 17-1-28.
 */
public class SysDirUtils {

	/**
	 * External storage dir
	 * @return
	 */
	public static File getStorageDir() {
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		return externalStorageDirectory;
	}

	/**
	 * External storage dir with sub relative path
	 * @param subDirRelativePath Path like '/foo/bar/'
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getStorageDir(String subDirRelativePath) throws FileNotFoundException {
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		File subDir = new File(externalStorageDirectory, subDirRelativePath);
		if (!subDir.exists()) {
			throw new FileNotFoundException("No dir in external storage: " + subDirRelativePath);
		}
		return subDir;
	}

	/**
	 * External storage dir with sub relative path
	 * @param subDirRelativePath  Path like '/foo/bar/file.txt'
	 * @return
	 * @throws FileNotFoundException
	 */
	public static File getStorageFile(String subDirRelativePath) throws FileNotFoundException {
		File externalStorageDirectory = Environment.getExternalStorageDirectory();
		File subDir = new File(externalStorageDirectory, subDirRelativePath);
		if (!subDir.getParentFile().exists()) {
			throw new FileNotFoundException("No dir for file: " + subDirRelativePath);
		}
		return subDir;
	}

}
