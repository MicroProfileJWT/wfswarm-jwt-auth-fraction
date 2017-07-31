package org.wildfly.swarm.mpjwtauth.detect;

import org.jboss.shrinkwrap.descriptor.api.webapp31.WebAppDescriptor;
import org.jboss.shrinkwrap.descriptor.api.webcommon31.LoginConfigType;
import org.wildfly.swarm.spi.meta.WebXmlFractionDetector;

/**
 * Created by starksm on 7/28/17.
 */
public class MPJWTAuthWebXmlDetector extends WebXmlFractionDetector {

    @Override
    public String artifactId() {
        return "org/wildfly/swarm/mpjwtauth";
    }

    @Override
    protected boolean doDetect() {
        super.doDetect();
        boolean isMPJWTAuth = false;
        if(webXMl.getAllLoginConfig().size() > 0) {
            LoginConfigType<WebAppDescriptor> lc = webXMl.getOrCreateLoginConfig();
            isMPJWTAuth = lc.getAuthMethod() != null && lc.getAuthMethod().equalsIgnoreCase("MP-JWT");
        }
        return isMPJWTAuth;
    }
}
