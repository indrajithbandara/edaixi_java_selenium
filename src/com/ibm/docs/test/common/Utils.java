package com.ibm.docs.test.common;

import java.awt.image.BufferedImage;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.UUID;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import javax.imageio.ImageIO;

import org.apache.commons.codec.binary.Base64;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.ExecuteException;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.io.FilenameUtils;
import org.openqa.selenium.io.IOUtils;

/**
 * Useful utilities
 * 
 * @author liuzhe@cn.ibm.com
 * 
 */
public class Utils {
	static Base64 base64 = new Base64();

	public static void sleep(double seconds) {
		try {
			Thread.sleep((int)seconds * 1000);
		} catch (InterruptedException e) {
		}
	}

	public static String getStringResource(Object obj, String path) {
		try {
			Class<?> clazz = obj instanceof Class<?> ? (Class<?>) obj : obj.getClass();
			return IOUtils.readFully(clazz.getResourceAsStream(path));
		} catch (IOException e) {
			throw new IllegalArgumentException("Can not found the javascript code!", e);
		}
	}
	
	public static String uuid() {
		UUID uuid = UUID.randomUUID();
		byte[] bytes = ByteBuffer.allocate(16).putLong(0, uuid.getLeastSignificantBits()).putLong(8, uuid.getMostSignificantBits()).array();
		return Base64.encodeBase64URLSafeString(bytes);
	}

	public static List<String> getClassesInPackage(String packageName) {
		ArrayList<String> classes = new ArrayList<String>();
		if (packageName == null || packageName.isEmpty())
			return classes;
		ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
		String path = packageName.replace('.', '/');
		try {
			Enumeration<URL> urls = classLoader.getResources(path);
			while (urls.hasMoreElements()) {
				URL url = urls.nextElement();
				if ("file".equals(url.getProtocol())) {
					findClasses(packageName, new File(url.toURI()), classes);
				} else if ("jar".equals(url.getProtocol())) {
					String urlStr = url.toString();
					int i = urlStr.indexOf('!');
					if (i > 0)
						findClasses(packageName, new URL(urlStr.substring(4, i)), classes);
				}
			}
		} catch (Exception e) {
			// ignore
		}
		return classes;
	}

	public static void findClasses(String packageName, File dir, List<String> classes) {
		if (!dir.isDirectory())
			return;
		File[] files = dir.listFiles();
		for (File file : files) {
			String name = file.getName();
			if (file.isDirectory()) {
				findClasses(packageName + '.' + name, file, classes);
			} else if (name.endsWith(".class")) {
				String className = packageName + '.' + name.substring(0, name.length() - 6);
				classes.add(className);
			}
		}
	}

	public static void findClasses(String packageName, URL jar, List<String> classes) {
		try {
			ZipInputStream zip = new ZipInputStream(jar.openStream());
			ZipEntry entry;
			while ((entry = zip.getNextEntry()) != null) {
				String name = entry.getName();
				if (name.endsWith(".class")) {
					name = name.replace('/', '.').substring(0, name.length() - 6);
					if (name.startsWith(packageName)) {
						classes.add(name);
					}
				}
			}

		} catch (Exception e) {

		}
	}
	
	public static String exec(String executable, String... arguments) throws ExecuteException, IOException {
		CommandLine commandLine = new CommandLine(executable);
		commandLine.addArguments(arguments);
		Executor executor = new DefaultExecutor();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(output);
		executor.setStreamHandler(psh);
		executor.execute(commandLine);
		return output.toString();
	}
	
	public static String exec(String command) throws ExecuteException, IOException {
		CommandLine commandLine =CommandLine.parse(command);
		Executor executor = new DefaultExecutor();
		ByteArrayOutputStream output = new ByteArrayOutputStream();
		PumpStreamHandler psh = new PumpStreamHandler(output);
		executor.setStreamHandler(psh);
		executor.execute(commandLine);
		return output.toString();
	}
	
	public static BufferedImage loadImage(File file) {
		BufferedImage image = null;
		FileInputStream in = null;
		try {
			in = new FileInputStream(file);
			image = ImageIO.read(in);
		} catch (Exception e) {
			
		} finally {
			IOUtils.closeQuietly(in);
		}
		return image;
	}
	
	public static boolean imageEquals(File file1, File file2) {
		return imageEquals(loadImage(file1), loadImage(file2));
	}
	
	public static boolean imageEquals(BufferedImage image1, BufferedImage image2) {
		if (image1 == null || image2 == null)
			return false;

		if (image1.getHeight() != image2.getHeight()
				|| image1.getWidth() != image2.getWidth())
			return false;

		for (int y = 0; y < image1.getHeight(); ++y) {
			for (int x = 0; x < image1.getWidth(); ++x) {
				if (image1.getRGB(x, y) != image2.getRGB(x, y))
					return false;
			}
		}

		return true;
	}
	
	public static String uniqueBaseName(File file) {
		return FilenameUtils.getBaseName(file.getName()) + "-" + uuid();
	}
	
	public static String uniqueBaseName(String file) {
		return uniqueBaseName(new File(file));
	}
}
