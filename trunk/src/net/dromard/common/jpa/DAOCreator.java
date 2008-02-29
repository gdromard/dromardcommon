package net.dromard.common.jpa;

import java.io.File;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import net.dromard.common.io.FileHelper;
import net.dromard.common.io.StreamHelper;
import net.dromard.common.templating.GString;
import net.dromard.common.util.system.Console;

public class DAOCreator {
	public static void main(String[] args) throws IOException {
		String daoImplementationTemplate = StreamHelper.getStreamContent(DAOCreator.class.getResourceAsStream("JPASkeletonDAO.template"));
		String daoInterfaceTemplate = StreamHelper.getStreamContent(DAOCreator.class.getResourceAsStream("ISkeletonDAO.template"));
		String daoInterfacePackage = null;
		if (args.length > 0) daoInterfacePackage = args[0];
		
		String modelClassname = Console.ask("Name of the Model Class", null);
		daoInterfacePackage = Console.ask("Package of the DAO Interface", daoInterfacePackage);
		String daoInterfaceClassname = Console.ask("Name of the DAO Interface Class", "I" + modelClassname + "DAO");
		String daoImplementationPackage = Console.ask("Package of the DAO Interface", daoInterfacePackage);
		String daoImplementationClassname = Console.ask("Name of the DAO implementation Class", "JPA" + modelClassname + "DAO");
		String sourceFolderPath = Console.ask("Path of your source folder", new File(".").getAbsolutePath());
		
		
		Map<String, String> attributes = new HashMap<String, String>();
		attributes.put("modelClassname", modelClassname);
		attributes.put("daoInterfacePackage", daoInterfacePackage);
		attributes.put("daoInterfaceClassname", daoInterfaceClassname);
		attributes.put("daoImplementationPackage", daoImplementationPackage);
		attributes.put("daoImplementationClassname", daoImplementationClassname);
		
		String daoImplementationPath = sourceFolderPath + "/" + daoImplementationPackage.replaceAll("\\.", "/");
		File folder = new File(daoImplementationPath);
		if (!folder.exists()) folder.mkdirs();
		String daoImplementationFilePath = daoImplementationPath + "/" + daoImplementationClassname + ".java";
		GString gDaoImplementation = new GString(daoImplementationTemplate, attributes);
		FileHelper.saveToFile(daoImplementationFilePath, gDaoImplementation.toString());
		System.out.println("Dao implementation class has been generated into " + daoImplementationFilePath);
		
		String daoInterfacePath = sourceFolderPath + "/" + daoInterfacePackage.replaceAll("\\.", "/");
		folder = new File(daoInterfacePath);
		if (!folder.exists()) folder.mkdirs();
		String daoInterfaceFilePath = daoInterfacePath + "/" + daoInterfaceClassname + ".java";
		GString gDaoInterface = new GString(daoInterfaceTemplate, attributes);
		FileHelper.saveToFile(daoInterfaceFilePath, gDaoInterface.toString());
		System.out.println("Dao interface class has been generated into " + daoInterfaceFilePath);
		
		// DAO Creator
		boolean generateGenericDAO = (args.length > 1) && (args[1].equalsIgnoreCase("true"));
		if (generateGenericDAO) {
			String genericDAOPackage = Console.ask("Package of DAOCreator util class", daoInterfacePackage);
			String genericDAOTemplate = StreamHelper.getStreamContent(DAOCreator.class.getResourceAsStream("GenericDAO.template"));
			
			String genericDAOPath = sourceFolderPath + "/" + genericDAOPackage.replaceAll("\\.", "/");
			folder = new File(genericDAOPath);
			if (!folder.exists()) folder.mkdirs();
			String genericDAOFilePath = genericDAOPath + "/GenericDAO.java";
			GString gGenericDAO = new GString(genericDAOTemplate, attributes);
			FileHelper.saveToFile(genericDAOFilePath, gGenericDAO.toString());
			System.out.println("Generic DAO class has been generated into " + genericDAOFilePath);
		}
	}
}
