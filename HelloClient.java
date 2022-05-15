import HelloApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;

import org.omg.CORBA.*;

public class HelloClient
{
  static Hello helloImpl;

  public static void main(String args[])
    {
      try{
        ORB orb = ORB.init(args, null);

        org.omg.CORBA.Object objRef = 
            orb.resolve_initial_references("NameService");
        
        NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);
 
        String name = "Hello";
        helloImpl = HelloHelper.narrow(ncRef.resolve_str(name));
        
        System.out.println("Obtained a handle on server object: " + helloImpl);
        //select language
        Map<Integer, String> languageMap = new HashMap<Integer, String>();
        languageMap.put(1, "English");
        languageMap.put(2, "French");
        languageMap.put(3, "German");
        languageMap.put(4, "Italian");
        languageMap.put(5, "Spanish");
        System.out.println("Select a language: 1. English, 2. French, 3. German, 4. Italian, 5. Spanish");
        Scanner sc = new Scanner(System.in);
        int lang = sc.nextInt();
        System.out.println(helloImpl.sayHello(languageMap.get(lang)));
        //get name
        System.out.println("Enter your name: ");
        String name1 = sc.next();
        System.out.println(helloImpl.sayBye(name1 + languageMap.get(lang)));
        helloImpl.shutdown();

        } catch (Exception e) {
          System.out.println("ERROR : " + e) ;
          e.printStackTrace(System.out);
          }
    }

}