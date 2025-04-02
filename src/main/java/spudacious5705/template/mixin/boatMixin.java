package spudacious5705.template.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.IceBlock;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.entity.vehicle.BoatEntity;
import net.minecraft.particle.ParticleEffect;
import net.minecraft.particle.ParticleTypes;
import net.minecraft.server.world.ServerWorld;
import net.minecraft.state.property.Properties;
import net.minecraft.text.Text;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.hit.HitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.util.math.Direction;
import net.minecraft.util.math.Vec3d;
import net.minecraft.world.RaycastContext;
import net.minecraft.world.World;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(BoatEntity.class)
public class boatMixin {
    @Inject(at = @At("HEAD"), method = "tick")
    private void tick(CallbackInfo info) {
        BoatEntity boat = (BoatEntity)(Object)this;
        if(boat.getControllingPassenger() instanceof PlayerEntity){
            if(boat.getWorld() instanceof ServerWorld world){
                Vec3d pos = boat.getPos();
                Vec3d direction = boat.getRotationVector();

                Vec3d[] points = new Vec3d[5];


                direction = direction.multiply(1.8);
                points[2] = direction.add(pos);

                direction = direction.rotateY(0.698132f);
                points[3] = direction.add(pos);

                direction = direction.rotateY(-1.396264f);
                points[1] = direction.add(pos);

                direction = direction.multiply(0.88888d);
                direction = direction.rotateY(-0.610865f);
                points[0] = direction.add(pos);

                direction = direction.rotateY(2.61799f);
                points[4] = direction.add(pos);


                for(int i = 0; i < 4; i++){
                    BlockHitResult result = world.raycast(new RaycastContext(
                            points[i],points[i+1], RaycastContext.ShapeType.OUTLINE, RaycastContext.FluidHandling.NONE,boat
                    ));
                    world.spawnParticles(ParticleTypes.CRIT,points[i].x,points[i].y,points[i].z,1,0,0,0,0);

                    if(result.getType() == HitResult.Type.BLOCK){
                        BlockPos blockPos = result.getBlockPos();
                        BlockState blockState = world.getBlockState(blockPos);

                        if(blockState.getBlock() instanceof IceBlock){
                            world.setBlockState(blockPos, IceBlock.getMeltedState());
                        }
                    }
                }

            }
        }
    }
}
