package com.yana.PrivateNetWorking.JarCreator;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;

import org.w3c.dom.Document;
import org.w3c.dom.NodeList;

public class ExecuteJarCreator {
	private static final String DIR = "build";
	private static final String SERVICENAME = "privateNetWorking";
	public static void main(String[] args) throws Exception {
		if(!enableCompilerVersion()) {
			System.exit(-1);
		}
		Process process = executeCommand("cmd /c mvn compile");
		if(process == null || process.waitFor() != 0) {
			System.err.println("compile error");
			System.exit(-1);
		}

		File buildRootDir = new File(DIR);
		if(!buildRootDir.exists()) {
			buildRootDir.mkdir();
		}

		long currenttime = System.currentTimeMillis();
		SimpleDateFormat sdf = new SimpleDateFormat("yyyyMMddHHmmSS");
		String dayStr = sdf.format(new Date(currenttime));
		File buildDir = new File(DIR + File.separator + SERVICENAME + "_" + dayStr);
		buildDir.mkdir();

		File buildLib = new File(buildDir.getAbsolutePath() + File.separator + "lib");
		String libAddCommand = "cmd /c mvn dependency:copy-dependencies -DoutputDirectory=" + buildLib.getAbsolutePath();
		process = executeCommand(libAddCommand);
		if(process == null || process.waitFor() != 0) {
			System.err.println("lib add error");
			System.exit(-1);
		}

		process = executeCommand("cmd /c copy lib" + File.separator + "privateNetSocket2-1.0.jar " + buildLib.getAbsolutePath());
		if(process == null || process.waitFor() != 0) {
			System.err.println("lib add privateNetSocket2 errors");
			System.exit(-1);
		}

		createManifestCentralRouterForWindows(buildDir, buildLib);
		createManifestCentralRouterForLinux(buildDir, buildLib);
		createManifestNode(buildDir, buildLib);

		process = executeCommand("cmd /c robocopy html " + buildDir.getAbsolutePath() + File.separator + "html" + " /s /e");
		process = executeCommand("cmd /c robocopy css " + buildDir.getAbsolutePath() + File.separator + "css" + " /s /e");
		process = executeCommand("cmd /c robocopy javascript " + buildDir.getAbsolutePath() + File.separator + "javascript"  + " /s /e");
		process = executeCommand("cmd /c robocopy cert " + buildDir.getAbsolutePath() + File.separator + "cert" +  " /s /e");
		process = executeCommand("cmd /c robocopy keys " + buildDir.getAbsolutePath() + File.separator + "keys" + " /s /e");
		process = executeCommand("cmd /c robocopy conf " + buildDir.getAbsolutePath() + File.separator + "conf" + " /s /e");
		new File(buildDir.getAbsolutePath() + File.separator + "log").mkdir();
		new File(buildDir.getAbsolutePath() + File.separator + "outdir").mkdir();
		new File(buildDir.getAbsolutePath() + File.separator + "collectdir").mkdir();
		File tmpClass = new File(buildDir.getAbsolutePath() + File.separator + "classes");
		executeCommand("cmd /c robocopy " + "target" + File.separator + "classes " + tmpClass.getAbsolutePath() + " /s /e");

		Files.move(Paths.get(buildDir.getAbsolutePath() + File.separator + "cetralRouterForManiFestForWindows.txt"), Paths.get(tmpClass.getAbsolutePath() + File.separator + "manifest.txt"));
		executeCommand("cmd /c exexJar.bat " + tmpClass.getAbsolutePath() + " " + "centralRouterForWindows.jar");
		Files.move(Paths.get(tmpClass.getAbsolutePath() + File.separator + "centralRouterForWindows.jar"), Paths.get(buildDir.getAbsolutePath() + File.separator + "centralRouterForWindows.jar"));
		Files.delete(Paths.get(tmpClass.getAbsolutePath() + File.separator + "manifest.txt"));

		Files.move(Paths.get(buildDir.getAbsolutePath() + File.separator + "cetralRouterForManiFestForLinux.txt"), Paths.get(tmpClass.getAbsolutePath() + File.separator + "manifest.txt"));
		executeCommand("cmd /c exexJar.bat " + tmpClass.getAbsolutePath() + " " + "centralRouterForLinux.jar");
		Files.move(Paths.get(tmpClass.getAbsolutePath() + File.separator + "centralRouterForLinux.jar"), Paths.get(buildDir.getAbsolutePath() + File.separator + "centralRouterForLinux.jar"));
		Files.delete(Paths.get(tmpClass.getAbsolutePath() + File.separator + "manifest.txt"));

		Files.move(Paths.get(buildDir.getAbsolutePath() + File.separator + "nodeManiFest.txt"), Paths.get(tmpClass.getAbsolutePath() + File.separator + "manifest.txt"));
		executeCommand("cmd /c exexJar.bat " + tmpClass.getAbsolutePath() + " " + "nodeEntry.jar");
		Files.move(Paths.get(tmpClass.getAbsolutePath() + File.separator + "nodeEntry.jar"), Paths.get(buildDir.getAbsolutePath() + File.separator + "nodeEntry.jar"));
		Files.delete(Paths.get(tmpClass.getAbsolutePath() + File.separator + "manifest.txt"));

		compressedBuildDir(buildRootDir, buildDir);
		executeCommand("cmd /c rmdir /s /q " + buildDir.getAbsolutePath());

		System.out.println("exit");
	}

	private static void compressedBuildDir(File rootDir, File buildDir) throws Exception {
		try(FileOutputStream fos = new FileOutputStream(rootDir.getAbsoluteFile() + File.separator + buildDir.getName() + ".zip");
			ZipOutputStream zos = new ZipOutputStream(fos)) {
			addZipEntry(zos, buildDir.getName(), buildDir);
		}
	}

	private static void addZipEntry(ZipOutputStream zos, String parentDirName, File targetDir) throws Exception {
		for(File f : targetDir.listFiles()) {
			if(f.isDirectory()) {
				if("classes".equals(f.getName())) {
					continue;
				}
				addZipEntry(zos, parentDirName + "/" + f.getName(), f);
			} else {
				ZipEntry zipEntry = new ZipEntry(parentDirName + "/" + f.getName());
				zos.putNextEntry(zipEntry);
				FileInputStream fis = new FileInputStream(f);
	            byte[] buffer = new byte[1024];
	            int length;
	            while ((length = fis.read(buffer)) >= 0) {
	                zos.write(buffer, 0, length);
	            }
	            fis.close();
			}
		}
	}

	private static Process executeCommand(String commandLine) throws Exception{
		System.out.println(commandLine);
		Process process = null;
		try {
			process = Runtime.getRuntime().exec(commandLine);
			BufferedReader buf = new BufferedReader(new InputStreamReader(process.getInputStream()));
			String result = null;
			while((result = buf.readLine()) != null) {
				
			}
			return process;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	private static void createManifestNode(File buildDir, File libDir) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Manifest-Version: 1.0");
		sb.append("\r\n");
		sb.append("Class-Path: .");
		for(File f : libDir.listFiles()) {
			sb.append(" lib\\" + f.getName());
		}
		sb.append("\r\n");
		sb.append("Main-Class: com.yana.PrivateNetWorking.Node.NodeEntry");
		sb.append("\r\n");
		String fileName = buildDir.getAbsolutePath() + File.separator + "nodeManiFest.txt";
		try(FileOutputStream fos = new FileOutputStream(fileName)) {
			fos.write(sb.toString().getBytes());
			fos.flush();
		}
	}

	private static void createManifestCentralRouterForWindows(File buildDir, File libDir) throws IOException {
		StringBuilder sb = new StringBuilder();
		sb.append("Manifest-Version: 1.0");
		sb.append("\r\n");
		sb.append("Class-Path: .");
		for(File f : libDir.listFiles()) {
			sb.append(" lib\\" + f.getName());
		}
		sb.append("\r\n");
		sb.append("Main-Class: com.yana.PrivateNetWorking.CentralRouter.CentralRouterEntry");
		sb.append("\r\n");
		String fileName = buildDir.getAbsolutePath() + File.separator + "cetralRouterForManiFestForWindows.txt";
		try(FileOutputStream fos = new FileOutputStream(fileName)) {
			fos.write(sb.toString().getBytes());
			fos.flush();
		}
	}

	private static void createManifestCentralRouterForLinux(File buildDir, File libDir) throws IOException{
		StringBuilder sb = new StringBuilder();
		sb.append("Manifest-Version: 1.0");
		sb.append("\n");
		sb.append("Class-Path: .");
		for(File f : libDir.listFiles()) {
			sb.append(" lib/" + f.getName());
		}
		sb.append("\n");
		sb.append("Main-Class: com.yana.PrivateNetWorking.CentralRouter.CentralRouterEntry");
		sb.append("\n");
		String fileName = buildDir.getAbsolutePath() + File.separator + "cetralRouterForManiFestForLinux.txt";
		try(FileOutputStream fos = new FileOutputStream(fileName)) {
			fos.write(sb.toString().getBytes());
			fos.flush();
		}
	}

	private static boolean enableCompilerVersion() {
		String parentTag = "properties";
		String complieSrcTag = "maven.compiler.source";
		String complieTargetTag = "maven.compiler.target";
		try(FileInputStream fis = new FileInputStream("pom.xml")) {
			DocumentBuilderFactory documentBuilderFactory = DocumentBuilderFactory.newInstance();
			DocumentBuilder documentBuilder = documentBuilderFactory.newDocumentBuilder();
			Document document = documentBuilder.parse(fis);
			NodeList nodeList = document.getElementsByTagName(complieSrcTag);
			boolean compileCheck = false;
			for(int i = 0; i < nodeList.getLength(); i++) {
				if(nodeList.item(i).getTextContent().equals("1.8")) {
					compileCheck = true;
				}
			}
			if(!compileCheck) {
				System.out.println("Please add compiler version to pom.xml. parentTag is properties. tag is maven.compiler.source and maven.compiler.target");
				return false;
			}
			
			nodeList = document.getElementsByTagName(complieTargetTag);
			compileCheck = false;
			for(int i = 0; i < nodeList.getLength(); i++) {
				if(nodeList.item(i).getTextContent().equals("1.8")) {
					compileCheck = true;
				}
			}
			if(!compileCheck) {
				System.out.println("Please add compiler version to pom.xml. parentTag is properties. tag is maven.compiler.source and maven.compiler.target");
				return false;
			}
			return true;
		} catch(Exception e) {
			e.printStackTrace();
		}
		return false;
	}
}
