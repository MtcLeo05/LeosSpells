package com.leo.leos_spells.impl.client.entity.model;

// Made with Blockbench 4.11.2
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.leo.leos_spells.LeosSpells;
import com.leo.leos_spells.impl.entity.SpellBaseEntity;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.EntityModel;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.resources.ResourceLocation;

public class SpellBaseModel<T extends SpellBaseEntity> extends EntityModel<T> {
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(ResourceLocation.fromNamespaceAndPath(LeosSpells.MODID, "spell_base"), "main");

	private final ModelPart main;
	private final ModelPart trail;

	public int color = 0;

	public SpellBaseModel(ModelPart root) {
		this.main = root.getChild("main");
		this.trail = this.main.getChild("trail");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition main = partdefinition.addOrReplaceChild("main", CubeListBuilder.create().texOffs(0, 0).addBox(-2.0F, -2.0F, -2.0F, 4.0F, 4.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 22.0F, 0.0F));

		PartDefinition trail = main.addOrReplaceChild("trail", CubeListBuilder.create().texOffs(12, 12).addBox(-0.25F, -1.5F, 2.0F, 0.0F, 1.0F, 1.0F, new CubeDeformation(0.0F))
			.texOffs(8, 12).addBox(-0.75F, -0.25F, 2.0F, 0.0F, 1.0F, 2.0F, new CubeDeformation(0.0F))
			.texOffs(8, 8).addBox(1.0F, -1.25F, 2.0F, 0.0F, 1.0F, 3.0F, new CubeDeformation(0.0F))
			.texOffs(0, 8).addBox(0.5F, 0.0F, 2.0F, 0.0F, 1.0F, 4.0F, new CubeDeformation(0.0F)), PartPose.offset(0.0F, 0.0F, 0.0F));

		return LayerDefinition.create(meshdefinition, 16, 16);
	}

	@Override
	public void setupAnim(SpellBaseEntity entity, float limbSwing, float limbSwingAmount, float ageInTicks, float netHeadYaw, float headPitch) {
		main.getAllParts().forEach(ModelPart::resetPose);
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer buffer, int packedLight, int packedOverlay, int color) {
		main.render(poseStack, buffer, packedLight, packedOverlay, this.color);
	}
}
