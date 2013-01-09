package org.livingplace.controlling.actions.registry.commands.internal;

import org.apache.felix.scr.annotations.Activate;
import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Deactivate;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.service.command.CommandProcessor;
import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.registry.api.IActionRegistryFactory;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

@Component(immediate = true)
public class ActionRegistryCommandsProvider {

  private Set<ServiceRegistration> regs = new HashSet<ServiceRegistration>();
  protected BundleContext context;
  private static Logger logger = Logger.getLogger(ActionRegistryCommandsProvider.class);

  @Reference
  IActionRegistryFactory actionRegistryFactory;

  @Activate
  void start(BundleContext context) {
    this.context = context;

    Hashtable dict = new Hashtable();
    dict.put(CommandProcessor.COMMAND_SCOPE, "lp:act:reg");
    dict.put(CommandProcessor.COMMAND_FUNCTION, ActionRegistryCommands.commands);

    regs.add(context.registerService(ActionRegistryCommands.class.getName(), new ActionRegistryCommands(actionRegistryFactory), dict));

    logger.info("Added Commands.");
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

    logger.info("Removed Commands.");
  }
}
