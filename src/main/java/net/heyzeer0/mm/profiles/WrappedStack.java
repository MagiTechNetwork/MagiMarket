package net.heyzeer0.mm.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.minecraft.server.v1_7_R4.NBTCompressedStreamTools;
import net.minecraft.server.v1_7_R4.NBTTagCompound;
import net.minecraft.server.v1_7_R4.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_7_R4.inventory.CraftItemStack;
import org.bukkit.inventory.ItemStack;

import java.beans.ConstructorProperties;
import java.io.*;
import java.math.BigInteger;

/**
 * Created by HeyZeer0 on 15/08/2017.
 * Copyright © HeyZeer0 - 2016
 */
public class WrappedStack {

    ItemStack main;

    @Getter
    String base64;
    @Getter
    String material;

    @ConstructorProperties({"material", "base64"})
    public WrappedStack(String material, String base64) {
        ByteArrayInputStream inputStream = new ByteArrayInputStream(new BigInteger(base64, 32).toByteArray());
        NBTTagCompound nbtTagCompoundRoot = NBTCompressedStreamTools.a(new DataInputStream(inputStream));
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = net.minecraft.server.v1_7_R4.ItemStack.createStack(nbtTagCompoundRoot);
        main = CraftItemStack.asBukkitCopy(nmsItem);
        main.setType(Material.valueOf(material));

        this.base64 = base64;
        this.material = material;
    }

    public WrappedStack(ItemStack convert) {
        main = convert;
        material = convert.getType().toString();

        ByteArrayOutputStream outputStream = new ByteArrayOutputStream();
        DataOutputStream dataOutput = new DataOutputStream(outputStream);
        NBTTagList nbtTagListItems = new NBTTagList();
        NBTTagCompound nbtTagCompoundItem = new NBTTagCompound();
        net.minecraft.server.v1_7_R4.ItemStack nmsItem = CraftItemStack.asNMSCopy(convert);
        nmsItem.save(nbtTagCompoundItem);
        nbtTagListItems.add(nbtTagCompoundItem);
        NBTCompressedStreamTools.a(nbtTagCompoundItem, (DataOutput) dataOutput);
        base64 = new BigInteger(1, outputStream.toByteArray()).toString(32);
    }

    @JsonIgnore
    public ItemStack getItemStack() {
        return main;
    }

    @JsonIgnore
    public int getAmount() {
        return main.getAmount();
    }

    public WrappedStack clone() {
        return new WrappedStack(main);
    }

}
