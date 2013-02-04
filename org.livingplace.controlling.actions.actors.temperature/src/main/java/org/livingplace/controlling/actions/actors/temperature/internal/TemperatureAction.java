package org.livingplace.controlling.actions.actors.temperature.internal;

import org.livingplace.controlling.actions.api.IAction;
import org.livingplace.controlling.actions.api.providers.AbstractAction;
import org.livingplace.controlling.actions.api.providers.ActionQualifier;

import java.util.Random;

public class TemperatureAction extends AbstractAction implements IAction {

  public TemperatureAction() {
    super(new ActionQualifier("temperature","rise","1.0"));
  }

  @Override
  public void execute() {
    System.out.println(this.getQualifier() + " - Temperature rise: " + (new Random().nextInt() % 25) + "C");
  }
}
