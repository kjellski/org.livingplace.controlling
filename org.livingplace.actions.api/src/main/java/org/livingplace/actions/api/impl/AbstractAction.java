package org.livingplace.actions.api.impl;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionStatus;
import org.osgi.service.log.LogService;
import org.osgi.util.tracker.ServiceTracker;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractAction implements IAction {

    protected ActionQualifier qualifier;
    protected IActionStatus status;
    protected ServiceTracker logServiceTracker;
    protected LogService logService;

    // optional parameters
    ActionProperties properties;

    protected AbstractAction(ActionQualifier qualifier) {
        this.qualifier = qualifier;
        this.status = new ActionStatus();

//        ServiceTracker logServiceTracker = new ServiceTracker(context, org.osgi.service.log.LogService.class.getName(), null);
//        logServiceTracker.open();
//        LogService logservice = (LogService) logServiceTracker.getService();

//        this.log.log(LogService.LOG_DEBUG, "[Created  ] Action: " + qualifier);
    }

    /*
     * this one should never ever be used.
     */
    @SuppressWarnings("unused")
    private AbstractAction() {
        throw new IllegalAccessError("A childclass of the " + AbstractAction.class.getName()
                        + " was enforced to be instantiated in the wrong way.");
    }

    @Override
    public void setActionProperties(ActionProperties properties) {
        this.properties = properties;
    }

    @Override
    public ActionProperties getActionProperties() {
        return this.properties;
    }

    @Override
    public IActionStatus getStatus() {
        return this.status;
    }

    @Override
    public void setStatus(IActionStatus status) {
        this.status = status;
    }

    @Override
    public ActionQualifier getActionQualifier() {
        return qualifier;
    };

    /*
     * implement this method in order to implement the thing you want to get
     * done with your action. (non-Javadoc)
     * 
     * @see org.livingplace.controlling.intelligence.action.IAction#act()
     */
    @Override
    public void execute() {
        try {
//            this.log.log(LogService.LOG_DEBUG, "[Executing] Action: " + qualifier);
            run();
//            this.log.log(LogService.LOG_DEBUG, "[Executed ] Action: " + qualifier);
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            
//            this.log.log(LogService.LOG_ERROR, "[Exception] Action: " + qualifier + "\nMessage: " + e.getMessage()
//                            + "\nException: " + sw.toString());
            this.status.setActionState(EActionState.FAILED);
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[" + this.qualifier.toString() + "]: ");
        b.append(this.status.toString());
        return b.toString();
    }
}
