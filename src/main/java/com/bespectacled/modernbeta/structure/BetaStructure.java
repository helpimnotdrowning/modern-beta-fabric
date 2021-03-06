package com.bespectacled.modernbeta.structure;

import org.apache.logging.log4j.Level;

import com.bespectacled.modernbeta.ModernBeta;

import net.fabricmc.fabric.api.structure.v1.FabricStructureBuilder;
import net.minecraft.structure.StructurePieceType;
import net.minecraft.util.Identifier;
import net.minecraft.util.registry.BuiltinRegistries;
import net.minecraft.util.registry.Registry;
import net.minecraft.world.gen.GenerationStep;
import net.minecraft.world.gen.feature.ConfiguredStructureFeature;
import net.minecraft.world.gen.feature.DefaultFeatureConfig;
import net.minecraft.world.gen.feature.StructureFeature;

public class BetaStructure {
    private static final String INDEV_HOUSE_ID = "indev_house";
    public static final StructurePieceType HOUSE_PIECE = IndevHouseGenerator.HousePiece::new;
    public static final StructureFeature<DefaultFeatureConfig> HOUSE_STRUCTURE = 
        new IndevHouseStructure(DefaultFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<?, ?> CONF_HOUSE_STRUCTURE = 
        HOUSE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);
    
    private static final String OCEAN_SHRINE_ID = "ocean_shrine";
    private static final String OCEAN_SHRINE_BASE_ID = "ocean_shrine/base";
    public static final StructurePieceType OCEAN_SHRINE_PIECE = OceanShrineGenerator.Piece::new;
    public static final StructureFeature<DefaultFeatureConfig> OCEAN_SHRINE_STRUCTURE = 
        new OceanShrineStructure(DefaultFeatureConfig.CODEC);
    public static final ConfiguredStructureFeature<?, ?> CONF_OCEAN_SHRINE_STRUCTURE = 
            OCEAN_SHRINE_STRUCTURE.configure(DefaultFeatureConfig.DEFAULT);
    
    public static void register() {
        Registry.register(
            Registry.STRUCTURE_PIECE, 
            new Identifier(ModernBeta.ID, INDEV_HOUSE_ID),
            HOUSE_PIECE
        );
        
        FabricStructureBuilder.create(
            new Identifier(ModernBeta.ID, INDEV_HOUSE_ID), 
            HOUSE_STRUCTURE)
            .step(GenerationStep.Feature.SURFACE_STRUCTURES)
            .defaultConfig(32, 8, 12345)
            .adjustsSurface()
            .register();
        
        BuiltinRegistries.add(
            BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, 
            new Identifier(ModernBeta.ID, INDEV_HOUSE_ID), 
            CONF_HOUSE_STRUCTURE
        );
        
        Registry.register(
                Registry.STRUCTURE_PIECE, 
                new Identifier(ModernBeta.ID, OCEAN_SHRINE_BASE_ID),
                OCEAN_SHRINE_PIECE
            );
            
            FabricStructureBuilder.create(
                new Identifier(ModernBeta.ID, OCEAN_SHRINE_ID), 
                OCEAN_SHRINE_STRUCTURE)
                .step(GenerationStep.Feature.SURFACE_STRUCTURES)
                .defaultConfig(32, 8, 12345)
                .adjustsSurface()
                .register();
            
            BuiltinRegistries.add(
                BuiltinRegistries.CONFIGURED_STRUCTURE_FEATURE, 
                new Identifier(ModernBeta.ID, OCEAN_SHRINE_ID), 
                CONF_OCEAN_SHRINE_STRUCTURE
            );
        
        ModernBeta.LOGGER.log(Level.INFO, "Registered structures.");
    }
}
