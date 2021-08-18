package me.Danker.handlers;

import io.netty.channel.ChannelDuplexHandler;
import io.netty.channel.ChannelHandlerContext;
import me.Danker.commands.ToggleNoRotateCommand;
import me.Danker.utils.Utils;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.item.ItemStack;
import net.minecraft.network.Packet;
import net.minecraft.network.play.server.S08PacketPlayerPosLook;
import net.minecraft.network.play.server.S04PacketEntityEquipment;

import java.lang.reflect.Field;

public class PacketHandler extends ChannelDuplexHandler {
	
	// Spirit boots fix
	@Override
	public void channelRead(ChannelHandlerContext ctx, Object msg) throws Exception {
		if (msg instanceof net.minecraft.network.Packet && msg.getClass().getName().endsWith("S08PacketPlayerPosLook") && ToggleNoRotateCommand.norotatetoggled) {
            EntityPlayerSP player = (Minecraft.getMinecraft()).thePlayer;
            if (player != null) {
                ItemStack item = player.getHeldItem();
                if ((item != null && ((ItemStack) item).getDisplayName().contains("Hyperion")) || (item != null && item.getDisplayName().contains("Aspect of the End"))) {
                    S08PacketPlayerPosLook packet = (S08PacketPlayerPosLook)msg;
                    Field packetYaw = packet.getClass().getDeclaredField("field_148936_d");
                    Field packetPitch = packet.getClass().getDeclaredField("field_148937_e");
                    packetYaw.setAccessible(true);
                    packetPitch.setAccessible(true);
                    packetYaw.setFloat(packet, player.rotationYaw);
                    packetPitch.setFloat(packet, player.rotationPitch);
                    msg = packet;
                }
            }
		}
		
		super.channelRead(ctx, msg);
	}
	
}
