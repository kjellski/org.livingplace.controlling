package org.livingplace.controlling.informations.registry.commands.internal;

import org.apache.felix.service.command.Descriptor;
import org.livingplace.controlling.informations.api.IInformation;
import org.livingplace.controlling.informations.api.IInformationQualifier;
import org.livingplace.controlling.informations.api.providers.InformationQualifier;
import org.livingplace.controlling.informations.api.providers.InformationQualifierParser;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.osgi.service.log.LogService;

import java.util.List;

public class InformationRegistryCommands {
  private IInformationRegistry registry;
  private LogService log;

  /**
   * Functions available to the gogo shell from this registry
   */
  public static String[] commands = {"show", "last"};

  public InformationRegistryCommands(IInformationRegistry registry, LogService log) {
    this.registry = registry;
    this.log = log;

    for (String command : commands) {
      log.log(LogService.LOG_INFO, command + "() added.");
    }
  }

  @Descriptor("executes an action qualified by the parameters")
  public void last(
          @Descriptor("full qualified action in the form: \"<namespace>.<name>:<version>\"")
          String fullQualifier) {

    IInformationQualifier qualifier = new InformationQualifier(InformationQualifierParser.parseInformationQualifier(fullQualifier));
    StringBuilder b = new StringBuilder("Last Information with qualifier: " + qualifier.getFullQualifier());

    log.log(LogService.LOG_INFO, b.toString());
    System.out.println(b.toString());
  }

  @Descriptor("shows all registered informations")
  public void show() {
    StringBuilder b = new StringBuilder("Looking up registered informations in Registry... ");
    b.append(" found " + registry.getAllRegistered().size() + " informations.\n");

    List<IInformation> informations = registry.getAllRegistered();
    if (informations.size() > 0) {
      for (IInformation information : informations) {
        b.append(information.getQualifier() + "\n");
      }
    } else {
      b.append("No informations Registered.\n");
    }

    log.log(LogService.LOG_INFO, b.toString());
    System.out.println(b.toString());
  }
}
