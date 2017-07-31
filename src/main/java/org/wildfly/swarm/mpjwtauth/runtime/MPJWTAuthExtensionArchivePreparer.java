package org.wildfly.swarm.mpjwtauth.runtime;

import java.io.InputStream;

import javax.inject.Inject;

import io.undertow.servlet.ServletExtension;
import org.eclipse.microprofile.jwt.impl.DefaultJWTCallerPrincipal;
import org.eclipse.microprofile.jwt.impl.DefaultJWTCallerPrincipalFactory;
import org.eclipse.microprofile.jwt.impl.MPAccessToken;
import org.eclipse.microprofile.jwt.principal.JWTAuthContextInfo;
import org.eclipse.microprofile.jwt.principal.JWTCallerPrincipal;
import org.eclipse.microprofile.jwt.principal.JWTCallerPrincipalFactory;
import org.eclipse.microprofile.jwt.principal.ParseException;
import org.eclipse.microprofile.jwt.wfswarm.JWTAccount;
import org.eclipse.microprofile.jwt.wfswarm.JWTAuthMechanism;
import org.eclipse.microprofile.jwt.wfswarm.JWTAuthMechanismFactory;
import org.eclipse.microprofile.jwt.wfswarm.JWTAuthMethodExtension;
import org.jboss.shrinkwrap.api.Archive;
import org.jboss.shrinkwrap.api.Node;
import org.jboss.shrinkwrap.api.ShrinkWrap;
import org.jboss.shrinkwrap.api.spec.JavaArchive;
import org.jboss.shrinkwrap.impl.base.asset.ServiceProviderAsset;
import org.wildfly.swarm.spi.api.DeploymentProcessor;
import org.wildfly.swarm.spi.runtime.annotations.DeploymentScoped;
import org.wildfly.swarm.undertow.WARArchive;

/**
 * Created by starksm on 7/29/17.
 */
@DeploymentScoped
public class MPJWTAuthExtensionArchivePreparer implements DeploymentProcessor {
    private final Archive archive;

    @Inject
    public MPJWTAuthExtensionArchivePreparer(Archive archive) {
        this.archive = archive;
    }

    @Override
    public void process() throws Exception {
        Class classes[] = {
                /*
                JWTAuthMethodExtension.class, JWTAuthMechanism.class, JWTAuthMechanismFactory.class,
                // principal package
                JWTAccount.class, JWTAuthContextInfo.class, JWTCallerPrincipal.class, JWTCallerPrincipalFactory.class, ParseException.class,
                // impl package
                DefaultJWTCallerPrincipal.class, DefaultJWTCallerPrincipalFactory.class, MPAccessToken.class
                */
        };
        JavaArchive jwtAuthJar = ShrinkWrap.create(JavaArchive.class, "jwt-auth-wfswarm.jar")
                .addClasses(classes)
                .addAsServiceProvider(ServletExtension.class, JWTAuthMethodExtension.class)
                .addAsServiceProvider(JWTCallerPrincipalFactory.class, DefaultJWTCallerPrincipalFactory.class);
        WARArchive war = archive.as(WARArchive.class);
        war.addModule("org.eclipse.microprofile.jwt");
        war.addAsLibraries(jwtAuthJar);
        System.out.println("MPJWTAuthExtensionArchivePreparer, jar: "+jwtAuthJar.toString(true));
        System.out.println("MPJWTAuthExtensionArchivePreparer, war: "+war.toString(true));
        /*
        war.addAsServiceProvider(ServletExtension.class, JWTAuthMethodExtension.class);
        // debugging
        Node extNode = war.get("WEB-INF/classes/META-INF/services/io.undertow.servlet.ServletExtension");
        ServiceProviderAsset asset = (ServiceProviderAsset) extNode.getAsset();
        InputStream is = asset.openStream();
        byte[] tmp = new byte[1024];
        int length = is.read(tmp);
        is.close();
        System.out.printf("MPJWTAuthExtensionArchivePreparer, set ServletExtension: %s", new String(tmp, 0, length));
        */
    }
}
