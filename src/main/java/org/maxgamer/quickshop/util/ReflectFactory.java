/*
 * This file is a part of project QuickShop, the name is ReflectFactory.java
 *  Copyright (C) PotatoCraft Studio and contributors
 *
 *  This program is free software: you can redistribute it and/or modify it
 *  under the terms of the GNU General Public License as published by the
 *  Free Software Foundation, either version 3 of the License, or
 *  (at your option) any later version.
 *
 *  This program is distributed in the hope that it will be useful, but WITHOUT
 *  ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 *  FITNESS FOR A PARTICULAR PURPOSE. See the GNU General Public License
 *  for more details.
 *
 *  You should have received a copy of the GNU General Public License
 *  along with this program. If not, see <http://www.gnu.org/licenses/>.
 *
 */

package org.maxgamer.quickshop.util;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.Arrays;
import java.util.List;
import java.util.logging.Level;
import java.util.stream.Collectors;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.command.CommandMap;
import org.bukkit.inventory.ItemStack;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.maxgamer.quickshop.QuickShop;

/**
 * ReflectFactory is library builtin QuickShop to get/execute stuff that cannot
 * be access with BukkitAPI with reflect way.
 *
 * @author Ghost_chu
 */
public class ReflectFactory {

    private static Method craftItemStack_asNMSCopyMethod;
    private static Method itemStack_saveMethod;
    private static Class<?> nbtTagCompoundClass;
    private static String nmsVersion;

    static {

        String nmsVersion = getNMSVersion();

        try {

            craftItemStack_asNMSCopyMethod = Class
                    .forName("org.bukkit.craftbukkit." + nmsVersion + ".inventory.CraftItemStack")
                    .getDeclaredMethod("asNMSCopy", ItemStack.class);

            GameVersion gameVersion = GameVersion.get(nmsVersion);
            if (gameVersion.isNewNmsName()) {

                // 1.17+
                nbtTagCompoundClass = Class.forName("net.minecraft.nbt.NBTTagCompound");
                List<Method> methodList = Arrays
                        .stream(Class.forName("net.minecraft.world.item.ItemStack").getDeclaredMethods())
                        .filter(method ->
                        {

                            Class<?> returnType = method.getReturnType();
                            Parameter[] parameters = method.getParameters();
                            return !method.isSynthetic() && !method.isBridge() && parameters.length == 1
                                    && returnType.equals(nbtTagCompoundClass)
                                    && parameters[0].getType().equals(nbtTagCompoundClass);

                        }).collect(Collectors.toList());
                if (methodList.size() == 1) {

                    itemStack_saveMethod = methodList.get(0);

                } else {

                    throw new RuntimeException(
                            "Unable to find correct itemStack save method, got " + methodList + ", please report!");

                }

            } else {

                // Before 1.17
                nbtTagCompoundClass = Class.forName("net.minecraft.server." + nmsVersion + ".NBTTagCompound");
                itemStack_saveMethod = Class.forName("net.minecraft.server." + nmsVersion + ".ItemStack")
                        .getDeclaredMethod("save", nbtTagCompoundClass);

            }

        } catch (Exception t) {

            QuickShop.getInstance().getLogger().log(Level.WARNING,
                    "Failed to loading up net.minecraft.server support module, usually this caused by NMS changes but QuickShop not support yet, Did you have up-to-date?",
                    t);

        }

    }

    @NotNull
    public static String getNMSVersion() {

        if (nmsVersion == null) {

            String name = Bukkit.getServer().getClass().getPackage().getName();
            nmsVersion = name.substring(name.lastIndexOf('.') + 1);

        }

        return nmsVersion;

    }

    /**
     * Get MinecraftServer's TPS
     *
     * @return TPS (e.g 19.92)
     */
    @NotNull
    public static Double getTPS() {

        return QuickShop.getInstance().getTpsWatcher().getAverageTPS();

    }

    @NotNull
    public static String getServerVersion() {

        return Bukkit.getServer().getMinecraftVersion();

    }

    /**
     * Save ItemStack to Json through the NMS.
     *
     * @param bStack ItemStack
     * @return The json for ItemStack.
     */
    @Nullable
    public static String convertBukkitItemStackToJson(@NotNull ItemStack bStack)
            throws InvocationTargetException, IllegalAccessException, NoSuchMethodException, InstantiationException
    {

        if (bStack.getType() == Material.AIR || craftItemStack_asNMSCopyMethod == null || nbtTagCompoundClass == null
                || itemStack_saveMethod == null)
        {

            return null;

        }

        Object mcStack = craftItemStack_asNMSCopyMethod.invoke(null, bStack);
        Object nbtTagCompound = nbtTagCompoundClass.getDeclaredConstructor().newInstance();

        itemStack_saveMethod.invoke(mcStack, nbtTagCompound);
        return nbtTagCompound.toString();

    }

    public static CommandMap getCommandMap() {

        return Bukkit.getServer().getCommandMap();

    }

    @Nullable
    public static String getMaterialMinecraftNamespacedKey(Material material) {

        return material.isItem() ? material.getItemTranslationKey() : material.getBlockTranslationKey();

    }

}
