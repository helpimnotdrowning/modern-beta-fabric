package com.bespectacled.modernbeta.mixin;

import com.bespectacled.modernbeta.ModernBeta;
import com.bespectacled.modernbeta.biome.AlphaBiomeSource;
import com.bespectacled.modernbeta.biome.BetaBiomeSource;
import com.bespectacled.modernbeta.biome.IndevBiomeSource;
import com.bespectacled.modernbeta.biome.InfdevBiomeSource;
import com.bespectacled.modernbeta.config.ModernBetaConfig;
import com.bespectacled.modernbeta.gen.AlphaChunkGenerator;
import com.bespectacled.modernbeta.gen.BetaChunkGenerator;
import com.bespectacled.modernbeta.gen.IndevChunkGenerator;
import com.bespectacled.modernbeta.gen.InfdevChunkGenerator;
import com.bespectacled.modernbeta.gen.SkylandsChunkGenerator;
import com.bespectacled.modernbeta.gen.settings.AlphaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.BetaGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.IndevGeneratorSettings;
import com.bespectacled.modernbeta.gen.settings.InfdevGeneratorSettings;
import com.bespectacled.modernbeta.gen.type.BetaGeneratorType;
import com.google.common.base.MoreObjects;
import com.google.common.collect.Maps;

import net.minecraft.block.Blocks;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.registry.DynamicRegistryManager;
import net.minecraft.util.registry.Registry;
import net.minecraft.util.registry.SimpleRegistry;
import net.minecraft.world.biome.Biome;
import net.minecraft.world.dimension.DimensionOptions;
import net.minecraft.world.dimension.DimensionType;
import net.minecraft.world.gen.GeneratorOptions;
import net.minecraft.world.gen.chunk.ChunkGenerator;
import net.minecraft.world.gen.chunk.ChunkGeneratorSettings;
import net.minecraft.world.gen.chunk.GenerationShapeConfig;
import net.minecraft.world.gen.chunk.NoiseSamplingConfig;
import net.minecraft.world.gen.chunk.SlideConfig;
import net.minecraft.world.gen.chunk.StrongholdConfig;
import net.minecraft.world.gen.chunk.StructuresConfig;

import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

import org.apache.logging.log4j.Level;

import java.util.Optional;
import java.util.Properties;
import java.util.Random;

/*
 * Thanks SuperCoder7979!
 */
@Mixin(GeneratorOptions.class)
public class MixinGeneratorOptions {
    @Inject(method = "fromProperties", at = @At("HEAD"), cancellable = true)
    private static void injectOverworldTwo(DynamicRegistryManager dynamicRegistryManager, Properties properties,
            CallbackInfoReturnable<GeneratorOptions> cir) {

        // no server.properties file generated
        if (properties.get("level-type") == null) {
            return;
        }

        String levelType = properties.get("level-type").toString().trim().toLowerCase();
        
        // check for our world type and return if so
        if (levelType.equals("beta") ||
            levelType.equals("skylands") ||
            levelType.equals("alpha") ||
            levelType.equals("infdev") ||
            levelType.equals("indev")) {
            // get or generate seed
            String seedField = (String) MoreObjects.firstNonNull(properties.get("level-seed"), "");
            long seed = new Random().nextLong();
            if (!seedField.isEmpty()) {
                try {
                    long parsedSeed = Long.parseLong(seedField);
                    if (parsedSeed != 0L) {
                        seed = parsedSeed;
                    }
                } catch (NumberFormatException var14) {
                    seed = seedField.hashCode();
                }
            }

            // get other misc data
            Registry<DimensionType> dimensions = dynamicRegistryManager.get(Registry.DIMENSION_TYPE_KEY);
            Registry<Biome> biomes = dynamicRegistryManager.get(Registry.BIOME_KEY);
            Registry<ChunkGeneratorSettings> chunkgens = dynamicRegistryManager.get(Registry.NOISE_SETTINGS_WORLDGEN);
            SimpleRegistry<DimensionOptions> dimensionOptions = DimensionType.createDefaultDimensionOptions(dimensions,
                    biomes, chunkgens, seed);

            String generate_structures = (String) properties.get("generate-structures");
            boolean generateStructures = generate_structures == null || Boolean.parseBoolean(generate_structures);

            Optional<StrongholdConfig> guaranteedStronghold = Optional.of(new StrongholdConfig(0, 0, 1));            
            StructuresConfig structures = (levelType.equals("indev")) ?
                new StructuresConfig(guaranteedStronghold, Maps.newHashMap(StructuresConfig.DEFAULT_STRUCTURES)) : new StructuresConfig(true);
            
            NoiseSamplingConfig noiseSampler = new NoiseSamplingConfig(1.0, 1.0, 40.0, 22.0);
            GenerationShapeConfig noise = new GenerationShapeConfig(256, noiseSampler, new SlideConfig(-10, 3, 0),
                    new SlideConfig(-30, 0, 0), 1, 2, 1.0, -60.0 / (256.0 / 2.0), true, true, false, false);

            ChunkGeneratorSettings type = new ChunkGeneratorSettings(structures, noise, Blocks.STONE.getDefaultState(),
                    Blocks.WATER.getDefaultState(), -10, 0, 64, false);

            
            ChunkGenerator generator;
            
            CompoundTag betaSettings = BetaGeneratorSettings.createSettings();
            CompoundTag alphaSettings = AlphaGeneratorSettings.createSettings();
            CompoundTag indevSettings = IndevGeneratorSettings.createSettings();
            CompoundTag infdevSettings = InfdevGeneratorSettings.createSettings();

            BetaGeneratorSettings betaGenSettings = new BetaGeneratorSettings(type, betaSettings);
            AlphaGeneratorSettings alphaGenSettings = new AlphaGeneratorSettings(type, alphaSettings);
            IndevGeneratorSettings indevGenSettings = new IndevGeneratorSettings(type, indevSettings);
            InfdevGeneratorSettings infdevGenSettings = new InfdevGeneratorSettings(type, infdevSettings);

            switch (levelType) {
                case "beta":
                    generator = new BetaChunkGenerator(new BetaBiomeSource(seed, biomes, betaGenSettings.settings), seed, betaGenSettings);
                    break;
                case "skylands":
                    generator = new SkylandsChunkGenerator(new BetaBiomeSource(seed, biomes, betaGenSettings.settings), seed, betaGenSettings);
                    break;
                case "alpha":
                    generator = new AlphaChunkGenerator(new AlphaBiomeSource(seed, biomes, alphaGenSettings.settings), seed, alphaGenSettings);
                    break;
                case "indev":
                    generator = new IndevChunkGenerator(new IndevBiomeSource(seed, biomes, indevGenSettings.settings), seed, indevGenSettings);
                    break;
                case "infdev":
                    generator = new InfdevChunkGenerator(new InfdevBiomeSource(seed, biomes, infdevGenSettings.settings), seed, infdevGenSettings);
                    break;
                default:
                    generator = new BetaChunkGenerator(new BetaBiomeSource(seed, biomes, betaGenSettings.settings), seed, betaGenSettings);
            }

            // return our chunk generator
            cir.setReturnValue(new GeneratorOptions(seed, generateStructures, false,
                    GeneratorOptions.method_28608(dimensions, dimensionOptions, generator)));
        }
    }
}