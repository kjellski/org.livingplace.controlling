package org.livingplace.actions.registry.commands.internal;

import org.apache.felix.scr.annotations.*;

import org.apache.felix.shell.Command;
import org.livingplace.actions.api.IAction;
import org.livingplace.actions.registry.api.IActionRegistry;
import org.osgi.service.log.LogService;
import java.io.PrintStream;

import java.util.List;
import java.util.StringTokenizer;


@Component(immediate = true)
@Service
public class GetAllRegisteredActionProvider implements Command {
  @Reference
  protected IActionRegistry actionRegistrySvc;

  @Reference
  protected LogService log;

  @Activate
  void start(){
    this.log.log(LogService.LOG_INFO, "Added Command.");
  }

  @Deactivate
  void stop(){
    this.log.log(LogService.LOG_INFO, "Removed Command.");
  }

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

//  @Override
//  protected Object doExecute() throws Exception {
//    List<IAction> actions = actionRegistrySvc.getAllRegisteredActions();
//    if (actions.size() > 0) {
//      for (IAction action : actions) {
//        String msg = action.toString();
//        this.log.log(LogService.LOG_INFO, msg);
//        System.out.println(msg);
//      }
//    } else {
//      String msg = "No Actions Registered.";
//      this.log.log(LogService.LOG_INFO, msg);
//      System.out.println(msg);
//    }
//    return null;
//  }
}
