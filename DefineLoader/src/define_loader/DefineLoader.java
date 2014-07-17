package define_loader;

import java.lang.reflect.Field;
import java.util.ArrayList;

import xml_parser.XML_Parser;

public class DefineLoader {
	String xmlPath;
	Field[] class_fields;
	String Project;
	
	/**
	 * Initializes DefineLoader object
	 * @param Project - name of project. Used to check XML file.
	 */
	public DefineLoader(String Project){
		this.Project = Project;
	}
	
	/**
	 * Loads the define values. Loads all primitive types and Strings
	 * @param xmlPath
	 * @param definesClass
	 * @throws IllegalArgumentException
	 * @throws IllegalAccessException
	 */
	@SuppressWarnings("rawtypes")
	public boolean loadDefines(String xmlPath, Class definesClass){
		this.xmlPath = xmlPath;
		class_fields = definesClass.getFields();
		try {
			parseDefines();
		} catch (IllegalArgumentException | IllegalAccessException e) {
			System.out.println("[ERROR] cantrip.define_loader.DefineLoader error:: Illegal argument or access! Possibly resulting from a faulty xml file. Define Load failed.");
			return false;
		}
		return true;
	}
	
	private void parseDefines() throws IllegalArgumentException, IllegalAccessException{
		XML_Parser xml = new XML_Parser();
		xml.parseXML(xmlPath);
		ArrayList<String> searchParameters = new ArrayList<String>();
		ArrayList<ArrayList<String>> parameter_values = new ArrayList<ArrayList<String>>();
		searchParameters.add("is_root");
		parameter_values.add(new ArrayList<String>());
		ArrayList<ArrayList<String>> results = xml.searchForLeaves("project", searchParameters, parameter_values);
		if(results.size() != 1 || results.get(0).size() != 1){
			System.out.println("[ERROR] cantrip.define_loader.DefineLoader error:: error in define xml file. Mulitple <project> fields");
			throw new IllegalArgumentException();
		} else{
			if(!results.get(0).get(0).equals(Project)){
				System.out.println("[ERROR]  cantrip.define_loader.DefineLoader error:: error in define xml file. Project field does not match defined project");
				throw new IllegalArgumentException();
			}
			else{
				for(int ii = 0; ii < class_fields.length; ii++){
					searchParameters.clear();
					parameter_values.clear();
					Field f = class_fields[ii];
					String type = getType(f.getType().toString());
					String name = f.getName();
					searchParameters.add("has_parent");
					parameter_values.add(new ArrayList<String>());
					parameter_values.get(0).add(type);
					if(!type.equals("Object")){
						results = xml.searchForLeaves(name, searchParameters, parameter_values);
						if(results.size() > 1){
							System.out.println(String.format("[ERROR]  cantrip.define_loader.DefineLoader error:: error in define xml file. Define value returns multiple entries. type = %s name = %s", type, name));
							throw new IllegalArgumentException();
						} else if(results.size() == 0){
							System.out.println(String.format("[WARNING]  cantrip.define_loader.DefineLoader error:: error in define xml file. Define value returns no entries. type = %s name = %s", type, name));
						} else{
							if(results.get(0).size() != 1){
								System.out.println(String.format("[ERROR]  cantrip.define_loader.DefineLoader error:: error in define xml file. Define value returns multiple or no values. type = %s name = %s", type, name));
								throw new IllegalArgumentException();
							} else{
								if(!results.get(0).get(0).equals("")){
									setValue(f, type, results.get(0).get(0));
								}
							}
						}
					}
				}
			}
		}
	}
	
	private String getType(String type){
		if(type.contains("String"))
			return "String";
		else if(type.contains("int"))
			return "int";
		else if(type.contains("byte"))
			return "byte";
		else if(type.contains("short"))
			return "short";
		else if(type.contains("long"))
			return "long";
		else if(type.contains("float"))
			return "float";
		else if(type.contains("double"))
			return "double";
		else if(type.contains("char"))
			return "char";
		else if(type.contains("boolean"))
			return "boolean";
		else
			return "Object";
	}
	
	private void setValue(Field f, String type, String value) throws IllegalArgumentException, IllegalAccessException{
		if(type.contains("String")){
			f.set(f, value);
		}
		else if(type.contains("int")){
			f.setInt(f, Integer.parseInt(value));
		}
		else if(type.contains("byte")){
			f.setByte(f, Byte.parseByte(value));
		}
		else if(type.contains("short")){
			f.setShort(f, Short.parseShort(value));
		}
		else if(type.contains("long")){
			f.setLong(f, Long.parseLong(value));
		}
		else if(type.contains("float")){
			f.setFloat(f, Float.parseFloat(value));
		}
		else if(type.contains("double")){
			f.setDouble(f, Double.parseDouble(value));
		}
		else if(type.contains("char")){
			f.setChar(f, value.charAt(0));
		}
		else if(type.contains("boolean")){
			f.setBoolean(f, Boolean.parseBoolean(value));
		}
	}
}
