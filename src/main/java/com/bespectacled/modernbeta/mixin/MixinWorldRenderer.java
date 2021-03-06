package com.bespectacled.modernbeta.mixin;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import org.spongepowered.asm.mixin.injection.ModifyVariable;
import org.spongepowered.asm.mixin.injection.At;
import net.minecraft.client.render.WorldRenderer;

@Mixin(value = WorldRenderer.class, priority = 1)
public class MixinWorldRenderer {
    
    @Unique
    private ModernBetaConfig BETA_CONFIG = ModernBeta.BETA_CONFIG;
	
	@ModifyVariable(
        method = "renderSky(Lnet/minecraft/client/util/math/MatrixStack;F)V",
        at = @At(value = "INVOKE_ASSIGN", target = "Lnet/minecraft/client/render/SkyProperties;getSkyColor(FF)[F")
    )
    private float[] modifySkySunsetCols(float[] skyCols) {
	    return BETA_CONFIG.renderAlphaSunset ? null : skyCols;
    }
	
}
