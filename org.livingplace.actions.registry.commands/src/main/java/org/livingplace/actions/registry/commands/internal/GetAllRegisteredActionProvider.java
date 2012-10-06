package org.livingplace.actions.registry.commands.internal;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.shell.Command;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;

import java.io.PrintStream;
import java.util.List;
import java.util.StringTokenizer;

@Component
public class GetAllRegisteredActionProvider implements Command {
  @Reference
  private IActionRegistry actionRegistrySvc;

  @Reference
  protected LogService log;

  @Override
  public void execute(String line, PrintStream out, PrintStream err) {
    StringTokenizer tokenizer = new StringTokenizer(line);
    tokenizer.nextToken(); // discard first token

    List<IAction> actions = actionRegistrySvc.getAllRegisteredActions();
    if (actions.size() > 0) {
      for (IAction action : actions) {
        String msg = action.toString();
        this.log.log(LogService.LOG_INFO, msg);
        System.out.println(msg);
      }
    } else {
      String msg = "No Actions Registered.";
      this.log.log(LogService.LOG_INFO, msg);
      System.out.println(msg);
    }
  }

  @Override
  public String getName() {
    return "act:reg:show";
  }

  @Override
  public String getShortDescription() {
    return "Lists all registered actions.";
  }

  @Override
  public String getUsage() {
    return "act:reg:show";
  }
}
