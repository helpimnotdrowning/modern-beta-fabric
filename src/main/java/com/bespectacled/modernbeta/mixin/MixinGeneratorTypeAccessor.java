package com.bespectacled.modernbeta.mixin;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.gen.Accessor;
import org.spongepowered.asm.mixin.gen.Invoker;

import net.fabricmc.api.EnvType;
import net.fabricmc.api.Environment;
import net.minecraft.client.world.GeneratorType;
import net.minecraft.client.world.GeneratorType.ScreenProvider;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.gen.GeneratorOptions;

@Environment(EnvType.CLIENT)
@Mixin(GeneratorType.class)
public interface MixinGeneratorTypeAccessor {
    @Accessor("SCREEN_PROVIDERS")
    public static Map<Optional<GeneratorType>, ScreenProvider> getScreenProviders() {
        throw new AssertionError();
    }
    
    @Accessor("SCREEN_PROVIDERS")
    public static void setScreenProviders(Map<Optional<GeneratorType>, ScreenProvider> providers) {
        throw new AssertionError();
    }
    
}
