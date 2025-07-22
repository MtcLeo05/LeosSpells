package com.leo.leos_spells.impl.client.entity.render;

import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.client.entity.model.SpellBaseModel;
import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.LivingEntityRenderer;
import net.minecraft.resources.ResourceLocation;

public class SpellBaseRender extends LivingEntityRenderer<SpellBaseEntity, SpellBaseModel<SpellBaseEntity>> {
    public SpellBaseRender(EntityRendererProvider.Context context) {
        super(context, new SpellBaseModel<>(context.bakeLayer(SpellBaseModel.LAYER_LOCATION)), 0.0f);
    }

    @Override
    public ResourceLocation getTextureLocation(SpellBaseEntity entity) {
        return ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "textures/entity/spell_base.png");
    }

    @Override
    public void render(SpellBaseEntity entity, float entityYaw, float partialTick, PoseStack poseStack, MultiBufferSource bufferSource, int packedLight) {
        model.color = entity.getColor();
        super.render(entity, entityYaw, partialTick, poseStack, bufferSource, packedLight);
    }

    @Override
    protected boolean shouldShowName(SpellBaseEntity entity) {
        return false;
    }
}
