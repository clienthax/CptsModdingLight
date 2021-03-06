package coloredlightscore.src.helper;

import java.util.Arrays;

import net.minecraft.client.renderer.Tessellator;
import coloredlightscore.src.types.CLTessellatorInterface;

public class CLTessellatorHelper {

	//private static int nativeBufferSize = 0x200000;
	public static float sunlightBrightness = 1.0f;
	
	public CLTessellatorHelper() {
		// TODO Auto-generated constructor stub
	}
    
	public void setBrightness(Tessellator instance, int par1)
    {
	 	instance.hasBrightness = true;
        instance.brightness = par1;
    }

	
    public static void addVertex(Tessellator instance, double par1, double par3, double par5)
    {
    	int cl_rawBufferSize = ((CLTessellatorInterface)instance).getRawBufferSize();	
    	
        if (instance.rawBufferIndex >= cl_rawBufferSize - 32) 
        {
            if (cl_rawBufferSize == 0)
            {
            	cl_rawBufferSize = 0x10000; 		//65536
            	((CLTessellatorInterface)instance).setRawBufferSize(cl_rawBufferSize);
                instance.rawBuffer = new int[cl_rawBufferSize];
            }
            else
            {
            	cl_rawBufferSize *= 2;
            	((CLTessellatorInterface)instance).setRawBufferSize(cl_rawBufferSize);
                instance.rawBuffer = Arrays.copyOf(instance.rawBuffer, cl_rawBufferSize);
            }
        }
        
        ++instance.addedVertices;

        if (instance.hasTexture)
        {
        	instance.rawBuffer[instance.rawBufferIndex + 3] = Float.floatToRawIntBits((float)instance.textureU);
        	instance.rawBuffer[instance.rawBufferIndex + 4] = Float.floatToRawIntBits((float)instance.textureV);
        }

        if (instance.hasBrightness)
        {
        	
        	/*
        	 Two lightmaps:
        	  RSL
        	  GSB
        	 Three sets of two texture values:
        	  SL
        	  BR
        	  GS
        	
        	 << and >> take precedence over &
        	 
        	*/
        	
        	/* 0000 0000 SSSS 0000 0000 0000 LLLL 0000 */
        	instance.rawBuffer[instance.rawBufferIndex + 7] = instance.brightness & 15728880;
        	/* 0000 0000 BBBB 0000 0000 0000 RRRR 0000 */
        	//instance.rawBuffer[instance.rawBufferIndex + 8] = (instance.brightness<<4 & 15728640) | (instance.brightness>>8 & 240);
        	/* 0000 0000 GGGG 0000 0000 0000 SSSS 0000 */
        	//instance.rawBuffer[instance.rawBufferIndex + 9] = (instance.brightness<<8 & 15728640) | (instance.brightness>>16 & 240);
        	
        }

        if (instance.hasColor)
        {
            instance.rawBuffer[instance.rawBufferIndex + 5] = instance.color;
        }

        if (instance.hasNormals)
        {
            instance.rawBuffer[instance.rawBufferIndex + 6] = instance.normal;
        }

        instance.rawBuffer[instance.rawBufferIndex + 0] = Float.floatToRawIntBits((float)(par1 + instance.xOffset));
        instance.rawBuffer[instance.rawBufferIndex + 1] = Float.floatToRawIntBits((float)(par3 + instance.yOffset));
        instance.rawBuffer[instance.rawBufferIndex + 2] = Float.floatToRawIntBits((float)(par5 + instance.zOffset));
        instance.rawBufferIndex += 10;
        ++instance.vertexCount;
        
        
        return;
        
    }
}
