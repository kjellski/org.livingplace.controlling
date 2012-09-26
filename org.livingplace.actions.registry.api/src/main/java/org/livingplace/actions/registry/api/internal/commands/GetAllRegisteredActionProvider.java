package org.livingplace.actions.registry.api.internal.commands;

import org.apache.felix.scr.annotations.Component;
import org.apache.felix.scr.annotations.Reference;
import org.apache.felix.shell.*;
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

    public String getName() {
        return "act:reg:show";
    }

    public String getShortDescription() {
        return "Lists all registered actions.";
    }

    public String getUsage() {
        return "act:reg:show";
    }
}
