package test;

import javax.servlet.ServletContext;

import io.undertow.servlet.ServletExtension;
import io.undertow.servlet.api.DeploymentInfo;

/**
 * A dummy ServletExtension for testing addAsServiceProvider
 */
public class DummyExtension implements ServletExtension {
    @Override
    public void handleDeployment(DeploymentInfo deploymentInfo, ServletContext servletContext) {

    }
}
