import java.io.File;
import java.net.URL;

import org.jboss.modules.JarModuleFinder;
import org.jboss.modules.Module;
import org.jboss.modules.ModuleIdentifier;
import org.jboss.modules.ModuleLoader;
import org.jboss.modules.log.ModuleLogger;
import org.jboss.modules.log.StreamModuleLogger;
import org.junit.Test;

/**
 * Created by starksm on 7/29/17.
 */
public class ModuleArtifactTest {
    @Test
    public void simpleClassFromArtifact() throws Exception {
        Module.setModuleLogger(new StreamModuleLogger(System.out));
        URL modules = getClass().getResource("/modules");
        String path = modules.getPath();
        System.setProperty("module.path", path);
        ModuleIdentifier id = ModuleIdentifier.create("test.jaxrs");
        Module testModule = Module.getBootModuleLoader().loadModule(id);
        URL c = testModule.getClassLoader().getResource("test/jaxrs/NoDeps.class");
        System.out.printf("NoDeps.class: %s\n", c);
        URL c1 = testModule.getClassLoader().getResource("javax/ws/rs/GET.class");
        System.out.printf("GET.class: %s\n", c1);
        Class<?> get = testModule.getClassLoader().loadClass("javax.ws.rs.GET");
        System.out.printf("Loaded GET: %s\n", get);

        URL c2 = testModule.getClassLoader().getResource("test/jaxrs/NoDeps.class");
        System.out.printf("NoDeps.class: %s\n", c2);
        Class<?> noDeps = testModule.getClassLoader().loadClass("test.jaxrs.NoDeps");
        System.out.printf("Loaded NoDeps: %s\n", noDeps);
        Class<?> extClass = testModule.getClassLoader().loadClass("test.jaxrs.Endpoint");
        System.out.printf("Loaded Endpoint: %s\n", extClass);
        extClass.newInstance();
    }
    @Test
    public void testClassFromArtifact() throws Exception {
        Module.setModuleLogger(new StreamModuleLogger(System.out));
        File root = new File("target/microprofile-jwt-auth-fraction-1.0.0-SNAPSHOT.jar");
        URL modules = getClass().getResource("/modules");
        String path = modules.getPath();
        System.setProperty("module.path", path);
        ModuleIdentifier id = ModuleIdentifier.create("org.wildfly.swarm.mpjwtauth", "runtime");
        Module mpjwtauthModule = Module.getBootModuleLoader().loadModule(id);
        Class<?> extClass = mpjwtauthModule.getClassLoader().loadClass("org.eclipse.microprofile.jwt.wfswarm.JWTAuthMethodExtension");
        System.out.printf("Loaded JWTAuthMethodExtension: %s\n", extClass);
        extClass.newInstance();
    }
}
