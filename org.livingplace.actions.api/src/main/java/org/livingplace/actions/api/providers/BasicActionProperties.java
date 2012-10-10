package org.livingplace.actions.api.providers;

import java.util.Properties;

public class BasicActionProperties extends ActionProperties {

    /*
     * java 1.1.x compatibility :(
     */
    private static final long serialVersionUID = -7125247956024357737L;

    @Override
    public Properties getDefaultProperties() {
        return new Properties();
    }
}
