package test;

import java.lang.reflect.Field;

import define_loader.DefineLoader;

public class Test {
	public static void main(String[] args) throws IllegalArgumentException, IllegalAccessException{
		System.out.println("Start Test");
		System.out.println(Defines.value);
		System.out.println(Defines.text);
		DefineLoader dl = new DefineLoader("Define Loader");
		dl.loadDefines("example.xml", Defines.class);
		System.out.println(Defines.value);
		System.out.println(Defines.text);
		/*Field[] fields = Defines.class.getFields();
		System.out.println(fields[0].getName());
		System.out.println(Defines.value);
		fields[0].setInt(fields[0], 5);
		System.out.println(Defines.value); */
		//fields[0] = 5;
		System.out.println("Finish Test");
	}
}
