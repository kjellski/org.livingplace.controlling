package org.livingplace.controlling.informations.registry.commands.internal;

import org.apache.felix.service.command.Descriptor;
import org.apache.log4j.Logger;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationListener;
import org.livingplace.controlling.informations.api.IInformationQualifier;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;
import org.livingplace.controlling.informations.api.providers.InformationQualifierParser;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;

import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class InformationRegistryCommands {
  private IInformationRegistryFactory registry;
  private static Logger logger = Logger.getLogger(InformationRegistryCommands.class);
  
  /**
   * Functions available to the gogo shell from this registry
   */
  public static String[] commands = {"show", "last", "registerInformationPrinter", "unregisterInformationPrinter"};

  public InformationRegistryCommands(IInformationRegistryFactory registry) {
    this.registry = registry;

    for (String command : commands) {
      logger.info(command + "() added.");
    }
  }

  @Descriptor("shows the last seen informations for the qualified information")
  public void last(
          @Descriptor("full qualified information in the form: \"<namespace>.<name>:<version>\"")
          String fullQualifier) {

    IInformationQualifier qualifier = new InformationQualifier(InformationQualifierParser.parseInformationQualifier(fullQualifier));
    StringBuilder b = new StringBuilder("Last Information with qualifier: " + qualifier.getFullQualifier());

    logger.info(b.toString());
    System.out.println(b.toString());

    throw new IllegalAccessError("Not implemented :(");
  }

  @Descriptor("shows all registered informations")
  public void show() {
    StringBuilder b = new StringBuilder("Looking up registered informations in Registry... ");
    b.append(" found " + registry.getInstance().getAllRegistered().size() + " informations.\n");
    List<IInformation> informations = registry.getInstance().getAllRegistered();
    if (informations.size() > 0) {
      for (IInformation information : informations) {
        b.append(information.getQualifier() + "\n");
      }
    } else {
      b.append("No informations Registered.\n");
    }

    logger.info(b.toString());
    System.out.println(b.toString());
  }

  /**
   * map to hold the already registered listeners.
   */
  private static Map<IInformationQualifier, IInformationListener> registeredListeners =
          new ConcurrentHashMap<IInformationQualifier, IInformationListener>();

  @Descriptor("registeres an Console Printing Listener to the given InformationQualifier in order to see the sensing of this information")
  public void registerInformationPrinter(
          @Descriptor("full qualified information in the form: \"<namespace>.<name>:<version>\"")
          String fullQualifier) {
    //TODO: implement this !
  }

  @Descriptor("unregisteres an Console Printing Listener to the given InformationQualifier")
  public void unregisterInformationPrinter(
          @Descriptor("full qualified information in the form: \"<namespace>.<name>:<version>\"")
          String fullQualifier) {
    //TODO: implement this !
  }

  public static void unregisterAllListeners() {
    //TODO: Unregister all Listeners when this bundle is shut down!
  }
}
