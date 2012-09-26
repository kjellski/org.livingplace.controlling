package org.livingplace.actions.api.internal;

import org.livingplace.actions.api.IActionProperties;

import java.util.Properties;

/*
 * Superclass for action properties that could be special for
 * specific actions. 
 * 
 * @author kjellski
 */
@SuppressWarnings("serial")
public abstract class ActionProperties extends Properties implements IActionProperties {
    public abstract Properties getDefaultProperties();
}
