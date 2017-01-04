package com.example.exampleproject.util;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;

import android.content.Context;
import android.text.TextUtils;

/**
 * 文件，文件夹工具类
 * 
 * @author chang
 * 
 */
public class FileUtils {

	/**
	 * 创建文件
	 * 
	 * @param fileName
	 * @return
	 * @throws IOException
	 */
	public static boolean createFile(String fileName) throws IOException {
		File file = new File(fileName);
		return file.createNewFile();
	}

	/**
	 * 判断文件是否存在
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean isFileExists(String fileName) {
		File file = new File(fileName);
		return file.exists();
	}

	/**
	 * 判断文件夹是否存在
	 * 
	 * @param directoryPath
	 * @return
	 */
	public static boolean isFolderExist(String directoryPath) {
		if (TextUtils.isEmpty(directoryPath)) {
			return false;
		}
		File dire = new File(directoryPath);
		return (dire.exists() && dire.isDirectory());
	}

	/**
	 * 创建文件夹
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean makeDirs(String filePath) {
		String folderName = getFolderName(filePath);
		if (TextUtils.isEmpty(folderName)) {
			return false;
		}

		File folder = new File(folderName);
		return (folder.exists() && folder.isDirectory()) ? true : folder
				.mkdirs();
	}

	/**
	 * 获取文件夹名称
	 * 
	 * @param filePath
	 * @return
	 */
	public static String getFolderName(String filePath) {

		if (TextUtils.isEmpty(filePath)) {
			return filePath;
		}

		int filePosi = filePath.lastIndexOf(File.separator);
		return (filePosi == -1) ? "" : filePath.substring(0, filePosi);
	}

	/**
	 * 删除文件
	 * 
	 * @param fileName
	 * @return
	 */
	public static boolean deleteFile(String fileName) {
		File file = new File(fileName);
		return file.delete();
	}

	/**
	 * 递归删除文件和文件夹
	 * 
	 * @param file
	 *            要删除的根目录
	 */
	public static boolean recursionDeleteFile(File file) {
		if (file.isFile()) {
			return file.delete();
		}
		if (file.isDirectory()) {
			File[] childFile = file.listFiles();
			if (childFile == null || childFile.length == 0) {
				return file.delete();
			}
			for (File f : childFile) {
				recursionDeleteFile(f);
			}
			return file.delete();
		}
		return false;
	}

	/**
	 * 重命名文件
	 * 
	 * @param oldFileName
	 * @param newFileName
	 * @return
	 */
	public static boolean renameFile(String oldFileName, String newFileName) {
		File newFile = new File(newFileName);
		File oldFile = new File(oldFileName);
		return oldFile.renameTo(newFile);
	}

	/**
	 * 判断文件夹里是否有文件
	 * 
	 * @param filePath
	 * @return
	 */
	public static boolean hasFileExists(String filePath) {
		File file = new File(filePath);
		if (file.exists()) {
			File[] files = file.listFiles();
			if (files.length > 0) {
				return true;
			}
		}
		return false;
	}

	/**
	 * 复制文件夹
	 * 
	 * @param fromFile
	 *            源文件夹
	 * @param toFile
	 *            目标文件夹
	 * @return
	 */
	public static int copyDir(String fromFile, String toFile) {
		// 要复制的文件目录
		File[] currentFiles;
		File root = new File(fromFile);
		// 如同判断SD卡是否存在或者文件是否存在
		// 如果不存在则 return出去
		if (!root.exists()) {
			return -1;
		}

		// 如果存在则获取当前目录下的全部文件 填充数组
		currentFiles = root.listFiles();

		// 目标目录
		File targetDir = new File(toFile);
		// 创建目录
		if (!targetDir.exists()) {
			targetDir.mkdirs();
		}

		// 遍历要复制该目录下的全部文件
		for (int i = 0; i < currentFiles.length; i++) {
			if (currentFiles[i].isDirectory())// 如果当前项为子目录 进行递归
			{
				copyDir(currentFiles[i].getPath() + "/", toFile
						+ currentFiles[i].getName() + "/");
			} else// 如果当前项为文件则进行文件拷贝
			{
				copyFile(currentFiles[i].getPath(),
						toFile + currentFiles[i].getName());
			}
		}
		return 0;
	}

	/**
	 * 文件拷贝 要复制的目录下的所有非子目录(文件夹)文件拷贝
	 * 
	 * @param fromFile
	 *            源文件
	 * @param toFile
	 *            目标文件
	 * @return
	 */
	public static int copyFile(String fromFile, String toFile) {
		try {
			InputStream fosfrom = new FileInputStream(fromFile);
			OutputStream fosto = new FileOutputStream(toFile);
			byte bt[] = new byte[1024];
			int c;
			while ((c = fosfrom.read(bt)) > 0) {
				fosto.write(bt, 0, c);
			}
			fosfrom.close();
			fosto.close();
			return 0;
		} catch (Exception ex) {
			return -1;
		}
	}

	/**
	 * 写入文件
	 * 
	 * @param filePath
	 * @param content
	 * @return
	 */
	public static boolean writeFile(String filePath, byte[] content) {
		FileOutputStream fop = null;
		try {
			fop = new FileOutputStream(filePath);
			fop.write(content);
			fop.flush();
			fop.close();
			return true;
		} catch (IOException e) {
			e.printStackTrace();
		} finally {
			try {
				if (fop != null) {
					fop.close();
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return false;
	}

	/**
	 * 保存文件到SD卡或内部存储
	 * 
	 * @param filePath
	 *            文件名路径
	 * @param stream
	 *            文件输入流
	 * @param append
	 *            是否添加已有文件
	 * @return
	 */
	public static boolean writeFile(String filePath, InputStream stream,
			boolean append) {
		FileOutputStream fos = null;
		try {
			fos = new FileOutputStream(filePath, append);
			byte data[] = new byte[1024];
			int length = -1;
			while ((length = stream.read(data)) != -1) {
				fos.write(data, 0, length);
			}
			fos.flush();
			return true;
		} catch (FileNotFoundException e) {
			throw new RuntimeException("FileNotFoundException occurred. ", e);
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (fos != null) {
				try {
					fos.close();
					stream.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * 
	 * 保存文件到内部存储
	 * 
	 * @param context
	 * @param fileName
	 *            文件名
	 * @param fileContent
	 *            文件内容
	 * @param mode
	 *            操作模式
	 *            Context.MODE_PRIVATE：为默认操作模式，代表该文件是私有数据，只能被应用本身访问，在该模式下，写入的内容会覆盖原文件的内容
	 *            ，如果想把新写入的内容追加到原文件中。可以使用Context.MODE_APPEND
	 *            Context.MODE_APPEND：模式会检查文件是否存在，存在就往文件追加内容，否则就创建新文件。
	 *            Context.MODE_WORLD_READABLE和Context
	 *            .MODE_WORLD_WRITEABLE用来控制其他应用是否有权限读写该文件。
	 *            MODE_WORLD_READABLE：表示当前文件可以被其他应用读取
	 *            MODE_WORLD_WRITEABLE：表示当前文件可以被其他应用写入。
	 * @return
	 */
	public static boolean writeFileToInternal(Context context, String fileName,
			byte[] fileContent, int mode) {
		FileOutputStream fos = null;
		try {
			fos = context.openFileOutput(fileName, mode);
			fos.write(fileContent);
			fos.flush();
			return true;
		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			if (fos != null) {
				try {
					fos.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
		return false;
	}

	/**
	 * 读取SD卡文件
	 * 
	 * @param filePath
	 * @param charsetName
	 * @return
	 */
	public StringBuilder readFileFromSDCard(String filePath, String charsetName) {
		File file = new File(filePath);
		StringBuilder fileContent = new StringBuilder("");
		if (file == null || !file.isFile()) {
			return null;
		}

		BufferedReader reader = null;
		try {
			InputStreamReader is = new InputStreamReader(new FileInputStream(
					file), charsetName);
			reader = new BufferedReader(is);
			String line = null;
			while ((line = reader.readLine()) != null) {
				if (!fileContent.toString().equals("")) {
					fileContent.append("\r\n");
				}
				fileContent.append(line);
			}
			reader.close();
			return fileContent;
		} catch (IOException e) {
			throw new RuntimeException("IOException occurred. ", e);
		} finally {
			if (reader != null) {
				try {
					reader.close();
				} catch (IOException e) {
					throw new RuntimeException("IOException occurred. ", e);
				}
			}
		}
	}

	/**
	 * 读取内部存储文件
	 * 
	 * @param context
	 * @param fileName
	 *            文件名
	 * @return
	 */
	public static String readFileFromInternal(Context context, String fileName) {
		String content = "";
		try {
			FileInputStream fis = context.openFileInput(fileName);
			ByteArrayOutputStream bos = new ByteArrayOutputStream();
			int len = 0;
			byte buf[] = new byte[1024];
			while ((len = fis.read(buf)) != -1) {
				bos.write(buf, 0, len);
			}
			fis.close();
			content = bos.toString();
			bos.close();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return content;
	}

}
