// HelloServer.java
import HelloApp.*;
import org.omg.CosNaming.*;
import org.omg.CosNaming.NamingContextPackage.*;
import org.omg.CORBA.*;
import org.omg.PortableServer.*;
import org.omg.PortableServer.POA;

import java.util.HashMap;
import java.util.Map;
import java.util.Properties;



class HelloImpl extends HelloPOA {
  private ORB orb;
  private Map<String, String> HelloMap = new HashMap<String, String>();
  private Map<String, String> goodbyeMap = new HashMap<String, String>();


  public void setORB(ORB orb_val) {
    orb = orb_val; 
    HelloMap.put("English", "Hello world");
    HelloMap.put("Spanish", "Hola mundo");
    HelloMap.put("French",  "Bonjour monde");
    HelloMap.put("German", "Hallo Welt");
    HelloMap.put("Italian", "Ciao mondo");
    goodbyeMap.put("English", "Goodbye");
    goodbyeMap.put("Spanish", "Adios");
    goodbyeMap.put("French",  "Au revoir");
    goodbyeMap.put("German", "Auf Wiedersehen");
    goodbyeMap.put("Italian", "Arrivederci");
  }
    
  // implement sayHello() method
  public String sayHello(String name) {
    return HelloMap.get(name);
  }
  public String sayBye(String name) {
    return goodbyeMap.get(name);
  }
    
  // implement shutdown() method
  public void shutdown() {
    orb.shutdown(false);
  }
}


public class HelloServer {

  public static void main(String args[]) {
    try{
      // create and initialize the ORB
      ORB orb = ORB.init(args, null);

      // get reference to rootpoa and activate the POAManager
      POA rootpoa = POAHelper.narrow(orb.resolve_initial_references("RootPOA"));
      rootpoa.the_POAManager().activate();

      // create servant and register it with the ORB
      HelloImpl helloImpl = new HelloImpl();
      helloImpl.setORB(orb); 

      // get object reference from the servant
      org.omg.CORBA.Object ref = rootpoa.servant_to_reference(helloImpl);
      Hello href = HelloHelper.narrow(ref);
          
      // get the root naming context
      org.omg.CORBA.Object objRef =
          orb.resolve_initial_references("RootPOA");
      // Use NamingContextExt which is part of the Interoperable
      // Naming Service (INS) specification.
      NamingContextExt ncRef = NamingContextExtHelper.narrow(objRef);

      // bind the Object Reference in Naming
      String name = "Hello";
      NameComponent path[] = ncRef.to_name( name );
      ncRef.rebind(path, href);

      System.out.println("HelloServer ready and waiting ...");

      // wait for invocations from clients
      orb.run();
    } 
        
      catch (Exception e) {
        System.err.println("ERROR: " + e);
        e.printStackTrace(System.out);
      }
          
      System.out.println("HelloServer Exiting ...");
        
  }
}