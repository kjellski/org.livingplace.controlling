package org.livingplace.controlling.actions.api.providers;

import org.apache.log4j.Logger;
import org.livingplace.controlling.actions.api.*;

import java.io.PrintWriter;
import java.io.StringWriter;

public abstract class AbstractAction implements IAction {

  protected final IActionQualifier qualifier;
  protected final IActionStatus status;

  private static final Logger logger = Logger.getLogger(AbstractAction.class);

  // optional parameters
  IActionProperties properties;

  protected AbstractAction(IActionQualifier actionQualifier) {
    this.qualifier = actionQualifier;
    this.status = new SimpleActionStatus();

    // adding a state change listener while debugging
    this.status.addActionStateChangedListener(new IActionStateChangedListener() {
      @Override
      public void actionStateChanged(IActionStatus.EActionState state) {
        logger.debug(qualifier.getFullQualifier() + " State changed to: " + state);
      }
    });
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
  public IActionQualifier getQualifier() {
    return qualifier;
  }

  /*
   * implement this method in order to implement the thing you want to get
   * done with your action. (non-Javadoc)
   *
   * @see org.livingplace.controlling.intelligence.action.IAction#act()
   */
  @Override
  public void run() {
    try {
      this.status.setActionState(IActionStatus.EActionState.PROCESSING);
      execute();
    } catch (Exception e) {
      StringWriter sw = new StringWriter();
      PrintWriter pw = new PrintWriter(sw);
      e.printStackTrace(pw);
    } finally {
      this.status.setActionState(IActionStatus.EActionState.FINISHED);
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
