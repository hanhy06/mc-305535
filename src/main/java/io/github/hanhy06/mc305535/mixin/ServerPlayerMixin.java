package io.github.hanhy06.mc305535.mixin;

import com.mojang.authlib.GameProfile;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Input;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Shadow;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ServerPlayer.class)
public abstract class ServerPlayerMixin extends Player {
	protected ServerPlayerMixin(Level level, GameProfile gameProfile) {
		super(level, gameProfile);
	}

	@Shadow
	public abstract Input getLastClientInput();

	@Inject(method = "setLastClientInput", at = @At("HEAD"))
	private void mc305535$startGlidingInLava(Input input, CallbackInfo info) {
		// Vanilla clients do not request fall flying in lava, but still send jump input.
		if (input.jump() && !this.getLastClientInput().jump()
				&& this.isInLava() && !this.isInWater() && !this.onClimbable()
				&& !this.isFallFlying() && this.canGlide()) {
			this.startFallFlying();
		}
	}
}
