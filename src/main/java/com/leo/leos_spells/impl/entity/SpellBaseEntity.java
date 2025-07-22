package com.leo.leos_spells.impl.entity;

import com.leo.leos_spells.api.spell.SpellHolder;
import com.leo.leos_spells.api.spell.SpellType;
import com.leo.leos_spells.util.ListUtil;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.syncher.EntityDataAccessor;
import net.minecraft.network.syncher.EntityDataSerializers;
import net.minecraft.network.syncher.SynchedEntityData;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.ai.attributes.AttributeSupplier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.ProjectileUtil;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.EntityHitResult;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class SpellBaseEntity extends Mob {
    private static final EntityDataAccessor<Integer> COLOR = SynchedEntityData.defineId(SpellBaseEntity.class, EntityDataSerializers.INT);
    private static final EntityDataAccessor<List<SpellHolder>> SPELLS = SynchedEntityData.defineId(SpellBaseEntity.class, SpellHolder.SPELL_HOLDER_LIST);

    private int color = 0;
    private List<SpellHolder> spells = ListUtil.of();
    private UUID owner;
    private Vec3 direction;

    public SpellBaseEntity(EntityType<? extends Mob> type, Level level) {
        super(type, level);
    }

    public void shoot(Player player, float speed) {
        setRot(player.getYRot(), player.getXRot());
        setDeltaMovement(getMovementToShoot(speed, 0));
    }

    @Override
    public void tick() {
        super.tick();
        if (!(level() instanceof ServerLevel sLevel)) return;

        List<SpellType> types = getSpellTypes();

        if(sLevel.getPlayerByUUID(owner) instanceof ServerPlayer sPlayer) {
            for (SpellType spellType : types) {
                spellType.onSpellTick(sPlayer, sPlayer.getMainHandItem(), this);
            }
        }

        HitResult result = ProjectileUtil.getHitResultOnMoveVector(this, (e) -> true);

        if (result instanceof BlockHitResult bhr) {
            onBlockHit(bhr);
        }

        if (result instanceof EntityHitResult ehr) {
            onEntityHit(ehr.getEntity());
        }
    }

    public Vec3 getMoveDirection() {
        return direction;
    }

    public Vec3 getMovementToShoot(float velocity, double inaccuracy) {
        direction = getForward()
            .normalize()
            .add(
                this.random.triangle(0.0, 0.0172275 * inaccuracy),
                this.random.triangle(0.0, 0.0172275 * inaccuracy),
                this.random.triangle(0.0, 0.0172275 * inaccuracy)
            )
            .scale(velocity);

        return direction;
    }

    public void setColor(int color) {
        entityData.set(COLOR, color);
        this.color = color;
    }

    public int getColor() {
        return entityData.get(COLOR);
    }

    public void setOwner(UUID owner) {
        this.owner = owner;
    }

    public void addSpell(SpellHolder spell) {
        List<SpellHolder> oldList = this.entityData.get(SPELLS);
        oldList.add(spell);
        this.entityData.set(SPELLS, oldList);

        this.spells.add(spell);
    }

    public List<SpellHolder> getSpells() {
        return entityData.get(SPELLS);
    }

    public List<SpellType> getSpellTypes() {
        List<SpellType> list = new ArrayList<>();

        for (SpellHolder holder : entityData.get(SPELLS)) {
            SpellType type = SpellType.fromHolder(holder);

            if(type != null) {
                list.add(type);
            }
        }

        return list;
    }

    public void onEntityHit(Entity entity) {
        if (!(entity instanceof LivingEntity e)) return;
        if (entity instanceof SpellBaseEntity) return;
        if (!(e.level() instanceof ServerLevel sLevel)) return;
        if (e.getUUID().equals(owner)) return;

        List<SpellType> types = getSpellTypes();

        if(sLevel.getPlayerByUUID(owner) instanceof ServerPlayer sPlayer) {
            for (SpellType spellType : types) {
                spellType.entityHit(sPlayer, sPlayer.getMainHandItem(), e, this);
            }
        }
    }

    protected void onBlockHit(BlockHitResult result) {
        if (!(level() instanceof ServerLevel sLevel)) return;

        BlockState state = sLevel.getBlockState(result.getBlockPos());
        if (state.isAir()) return;
        if (!state.getFluidState().isEmpty()) return;
        if (state.getCollisionShape(sLevel, result.getBlockPos()).isEmpty()) return;

        List<SpellType> types = getSpellTypes();

        if(sLevel.getPlayerByUUID(owner) instanceof ServerPlayer sPlayer) {
            for (SpellType spellType : types) {
                spellType.blockHit(sPlayer, sPlayer.getMainHandItem(), result, this);
            }
        }
    }

    @Override
    public void push(Entity entity) {
    }

    @Override
    public void push(Vec3 vector) {
    }

    @Override
    public void push(double x, double y, double z) {
    }

    @Override
    protected void pushEntities() {
    }


    @Override
    public boolean canCollideWith(Entity entity) {
        return false;
    }

    @Override
    public boolean shouldRender(double x, double y, double z) {
        return true;
    }

    @Override
    protected boolean isAffectedByFluids() {
        return false;
    }

    @Override
    protected void applyGravity() {
    }

    @Override
    protected int calculateFallDamage(float fallDistance, float damageMultiplier) {
        return 0;
    }

    @Override
    protected void defineSynchedData(SynchedEntityData.Builder builder) {
        super.defineSynchedData(builder);
        builder.define(COLOR, color);

        builder.define(SPELLS, new ArrayList<>());
    }

    public static AttributeSupplier setAttributes() {
        AttributeSupplier.Builder builder = Mob.createMobAttributes();
        builder.add(Attributes.ATTACK_DAMAGE, 0);
        builder.add(Attributes.MAX_HEALTH, 1);
        builder.add(Attributes.ARMOR, 0);
        builder.add(Attributes.MOVEMENT_SPEED, 0);
        return builder.build();
    }

    @Override
    public void addAdditionalSaveData(CompoundTag compound) {
        super.addAdditionalSaveData(compound);
        compound.putInt("color", color);
        if (owner != null) compound.putUUID("owner", owner);

        ListTag tag = new ListTag();

        for (SpellHolder spell : getSpells()) {
            tag.add(spell.serialize());
        }

        compound.put("spells", tag);
    }

    @Override
    public void readAdditionalSaveData(CompoundTag compound) {
        super.readAdditionalSaveData(compound);

        if (compound.contains("color")) color = compound.getInt("color");
        if (compound.contains("owner")) owner = compound.getUUID("owner");
        if (compound.contains("spells")) {
            ListTag tag = ((ListTag) compound.get("spells"));

            spells = new ArrayList<>();
            for (Tag spellTag : tag) {
                SpellHolder holder = SpellHolder.deserialize((CompoundTag) spellTag);
                spells.add(holder);
            }
        }
    }

    @Override
    public Iterable<ItemStack> getArmorSlots() {
        return List.of();
    }

    @Override
    public ItemStack getItemBySlot(EquipmentSlot slot) {
        return ItemStack.EMPTY;
    }

    @Override
    public void setItemSlot(EquipmentSlot slot, ItemStack stack) {

    }

    @Override
    public HumanoidArm getMainArm() {
        return HumanoidArm.RIGHT;
    }
}
