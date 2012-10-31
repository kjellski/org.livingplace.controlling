package org.livingplace.controlling.informations.registry.commands.internal;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.service.command.CommandProcessor;
import org.livingplace.controlling.informations.registry.api.IInformationRegistry;
import org.livingplace.controlling.informations.registry.api.IInformationRegistryFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

@Component(immediate = true)
public class InformationRegistryCommandsProvider {

  private Set<ServiceRegistration> regs = new HashSet<ServiceRegistration>();
  protected BundleContext context;

  private IInformationRegistry registry;

  @Reference
  protected IInformationRegistryFactory informationRegistryFactory;

  @Reference
  protected LogService log;

  @Activate
  void start(BundleContext context) {
    this.context = context;

    Hashtable dict = new Hashtable();
    dict.put(CommandProcessor.COMMAND_SCOPE, "lp:inf:reg");
    dict.put(CommandProcessor.COMMAND_FUNCTION, InformationRegistryCommands.commands);

    regs.add(context.registerService(InformationRegistryCommands.class.getName(), new InformationRegistryCommands(informationRegistryFactory, log), dict));

    this.log.log(LogService.LOG_INFO, "Added Commands.");
  }

  @Deactivate
  void stop() {
    // from apache gogo shell sources
    Iterator<ServiceRegistration> iterator = regs.iterator();
    while (iterator.hasNext())
    {
      ServiceRegistration reg = iterator.next();
      reg.unregister();
      iterator.remove();
    }

    this.log.log(LogService.LOG_INFO, "Removed Commands.");
  }
}
