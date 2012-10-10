package org.livingplace.actions.api.providers;

import org.livingplace.actions.api.IAction;
import org.livingplace.actions.api.IActionProperties;
import org.livingplace.actions.api.IActionQualifier;
import org.livingplace.actions.api.IActionStatus;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractAction implements IAction {

    protected IActionQualifier qualifier;
    protected IActionStatus status;

    // optional parameters
    IActionProperties properties;

    protected AbstractAction(IActionQualifier qualifier) {
        this.qualifier = qualifier;
        this.status = new SimpleActionStatus();
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
    public void setActionProperties(IActionProperties properties) {
        this.properties = properties;
    }

    @Override
    public IActionProperties getActionProperties() {
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
    public IActionQualifier getActionQualifier() {
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
            this.status.setActionState(IActionStatus.EActionState.PROCESSING);
            run();
        } catch (Exception e) {
            StringWriter sw = new StringWriter();
            PrintWriter pw = new PrintWriter(sw);
            e.printStackTrace(pw);
            this.status.setActionState(IActionStatus.EActionState.FAILED);
        }
    }

    @Override
    public String toString() {
        StringBuilder b = new StringBuilder();
        b.append("[" + this.qualifier.getFullQualifier() + "]: ");
        b.append(this.status.toString());
        return b.toString();
    }
}
