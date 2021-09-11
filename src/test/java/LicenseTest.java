import de.primeapi.primeplugins.spigotapi.managers.rest.PluginInfo;
import de.primeapi.primeplugins.spigotapi.managers.rest.RestManager;
import org.junit.Assert;
import org.junit.Test;

public class LicenseTest {

    
    
    
    @Test
    public void testLicense(){
    
        String license = "TEST-ZSHK-RLZH-MXFV";
        RestManager restManager = new RestManager();
        
        boolean b = restManager.validateLicense(license, "BungeeSystem");

        Assert.assertTrue(b);
        
    }

    @Test
    public void testPluginInfo(){
        RestManager restManager = new RestManager();

        PluginInfo pluginInfo = restManager.getPlugininfo("BungeeSystem");
        Assert.assertEquals("bungeesystem", pluginInfo.getName());
    }

}
