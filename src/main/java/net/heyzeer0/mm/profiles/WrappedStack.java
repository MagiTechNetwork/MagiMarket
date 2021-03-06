package net.heyzeer0.mm.profiles;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import net.minecraft.server.v1_14_R1.NBTCompressedStreamTools;
import net.minecraft.server.v1_14_R1.NBTTagCompound;
import net.minecraft.server.v1_14_R1.NBTTagList;
import org.bukkit.Material;
import org.bukkit.craftbukkit.v1_14_R1.inventory.CraftItemStack;
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
        NBTTagCompound nbtTagCompoundRoot = null;
        try {
            nbtTagCompoundRoot = NBTCompressedStreamTools.a(new DataInputStream(inputStream));
        } catch (IOException e) {
            e.printStackTrace();
        }
        net.minecraft.server.v1_14_R1.ItemStack nmsItem = net.minecraft.server.v1_14_R1.ItemStack.a(nbtTagCompoundRoot);
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
        net.minecraft.server.v1_14_R1.ItemStack nmsItem = CraftItemStack.asNMSCopy(convert);
        nmsItem.save(nbtTagCompoundItem);
        nbtTagListItems.add(nbtTagCompoundItem);
        try {
            NBTCompressedStreamTools.a(nbtTagCompoundItem, (DataOutput) dataOutput);
        } catch (IOException e) {
            e.printStackTrace();
        }
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
