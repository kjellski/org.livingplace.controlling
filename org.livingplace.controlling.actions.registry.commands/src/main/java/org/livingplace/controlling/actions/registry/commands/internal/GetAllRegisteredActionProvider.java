package org.livingplace.controlling.actions.registry.commands.internal;

import org.apache.felix.scr.annotations.*;
import org.apache.felix.service.command.CommandProcessor;
import org.livingplace.controlling.actions.registry.api.IActionRegistry;
import org.osgi.framework.BundleContext;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.log.LogService;

import java.util.HashSet;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Set;

@Component(immediate = true)
public class GetAllRegisteredActionProvider {

  private Set<ServiceRegistration> regs = new HashSet<ServiceRegistration>();
  protected BundleContext context;

  @Reference
  protected IActionRegistry registry;

  @Reference
  protected LogService log;

  @Activate
  void start(BundleContext context) {
    this.context = context;

    Hashtable dict = new Hashtable();
    dict.put(CommandProcessor.COMMAND_SCOPE, "lp:act:reg");
    dict.put(CommandProcessor.COMMAND_FUNCTION, ActionRegistryCommands.commands);

    regs.add(context.registerService(ActionRegistryCommands.class.getName(), new ActionRegistryCommands(registry, log), dict));

    this.log.log(LogService.LOG_INFO, "Added Command.");
  }

  @Deactivate
  void stop() {
    this.log.log(LogService.LOG_INFO, "Removed Command.");

    // from apache gogo shell sources
    Iterator<ServiceRegistration> iterator = regs.iterator();
    while (iterator.hasNext())
    {
      ServiceRegistration reg = iterator.next();
      reg.unregister();
      iterator.remove();
    }
  }
}
