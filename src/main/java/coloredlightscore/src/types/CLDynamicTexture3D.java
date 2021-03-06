package coloredlightscore.src.types;

import java.io.IOException;

import org.lwjgl.opengl.GL11;
import org.lwjgl.opengl.GL12;
import org.lwjgl.opengl.GL14;

import net.minecraft.client.renderer.texture.AbstractTexture;
import net.minecraft.client.renderer.texture.TextureUtil;
import net.minecraft.client.resources.IResourceManager;
import cpw.mods.fml.relauncher.Side;
import cpw.mods.fml.relauncher.SideOnly;

@SideOnly(Side.CLIENT)
public class CLDynamicTexture3D extends AbstractTexture {

    private final int[] dynamicTextureData;
    /** width of this icon in pixels */
    public final int width;
    /** height of this icon in pixels */
    public final int height;
    /** height of this icon in pixels */
    public final int depth;
    
    /* I sure hope no one wants this...
    public CLDynamicTexture3D(BufferedImage par1BufferedImage)
    {
        this(par1BufferedImage.getWidth(), par1BufferedImage.getHeight());
        par1BufferedImage.getRGB(0, 0, par1BufferedImage.getWidth(), par1BufferedImage.getHeight(), this.dynamicTextureData, 0, par1BufferedImage.getWidth());
        this.updateDynamicTexture();
    }
    */

    public CLDynamicTexture3D(int par1, int par2, int par3)
    {
        this.width = par1;
        this.height = par2;
        this.depth = par3;
        this.dynamicTextureData = new int[par1 * par2 * par3];
        allocateTextureImpl(this.getGlTextureId(), 0, par1, par2, par3, 1.0F);
    }
    
    public static void allocateTextureImpl(int textureID, int mipmapLevel, int pHeight, int pWidth, int pDepth, float anUnusedFloatFromDynamicTexture)
    {
    	GL11.glDeleteTextures(textureID);
    	GL11.glBindTexture(GL12.GL_TEXTURE_3D, textureID);

        /* Looks to be some sort of wizardry for anisotropic filtering on all 2D textures... Not sure if I need this...
    	if (OpenGlHelper.anisotropicFilteringSupported)
        {
            GL11.glTexParameterf(GL11.GL_TEXTURE_2D, 34046, p_147946_4_);
        }
        */

        if (mipmapLevel > 0)
        {
            GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_MAX_LEVEL, mipmapLevel);
            GL11.glTexParameterf(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_MIN_LOD, 0.0F);
            GL11.glTexParameterf(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_MAX_LOD, (float)mipmapLevel);
            GL11.glTexParameterf(GL12.GL_TEXTURE_3D, GL14.GL_TEXTURE_LOD_BIAS, 0.0F);
        }
        
        //we might want to replace highestMipmapLevel with a 0... not sure...
        GL12.glTexSubImage3D(GL12.GL_TEXTURE_3D, mipmapLevel, 0, 0, 0, pWidth, pHeight, pDepth, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, TextureUtil.dataBuffer);
    }
    
    /* Dynamic Texture had this... I don't even... */
    public void loadTexture(IResourceManager par1ResourceManager) {}

    public void updateDynamicTexture()
    {
        GL11.glBindTexture(GL12.GL_TEXTURE_3D, this.getGlTextureId());
        
        GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MIN_FILTER, GL11.GL_NEAREST);
        GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_MAG_FILTER, GL11.GL_NEAREST);
        
        //Texture wrapping  I shouldn't need these, but we'll leave it in for kicks...
        GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_S, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL11.GL_TEXTURE_WRAP_T, GL11.GL_REPEAT);
        GL11.glTexParameteri(GL12.GL_TEXTURE_3D, GL12.GL_TEXTURE_WRAP_R, GL11.GL_REPEAT);

        /* This probably won't support 3D texture anyway
        int[] aint1 = par0ArrayOfInteger;

        if (Minecraft.getMinecraft().gameSettings.anaglyph)
        {
            aint1 = TextureUtil.updateAnaglyph(par0ArrayOfInteger);
        }
        */
        
        //I don't think I need to slice this up, because I can just generate the texture in the right order back in EntityRenderer
        int textureLength = this.width * this.height * this.depth;
        TextureUtil.dataBuffer.clear();	
        TextureUtil.dataBuffer.put(this.dynamicTextureData, 0, textureLength);
        TextureUtil.dataBuffer.position(0).limit(textureLength);
        
        int mipmapLevel = 0;
        GL12.glTexSubImage3D(GL12.GL_TEXTURE_3D, mipmapLevel, 0, 0, 0, this.width, this.height, this.depth, GL12.GL_BGRA, GL12.GL_UNSIGNED_INT_8_8_8_8_REV, TextureUtil.dataBuffer);
    }
    
    public int[] getTextureData()
    {
        return this.dynamicTextureData;
    }
}