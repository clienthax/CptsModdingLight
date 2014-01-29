package kovukore.coloredlights.src.asm;

import java.util.Map;

import net.minecraft.launchwrapper.LaunchClassLoader;
import cpw.mods.fml.relauncher.IFMLLoadingPlugin;

public class ColoredLightsCoreLoadingPlugin implements IFMLLoadingPlugin
{
	public static LaunchClassLoader CLASSLOADER;
	public static boolean MCP_ENVIRONMENT;
	
	@Override
	public String[] getASMTransformerClass()
	{
		return new String[] { };
	}

	@Override
	public String getModContainerClass()
	{
		return ColoredLightsCoreDummyContainer.class.getName();
	}

	@Override
	public String getSetupClass()
	{
		return null;
	}

	@Override
	public void injectData(Map<String, Object> data)
	{
		MCP_ENVIRONMENT = !((Boolean)data.get("runtimeDeobfuscationEnabled")).booleanValue();
		CLASSLOADER = (LaunchClassLoader)data.get("classLoader");
	}

	@Override
	public String getAccessTransformerClass()
	{
		return null;
	}
}