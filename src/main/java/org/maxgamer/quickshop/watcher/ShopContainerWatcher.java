/*
 * This file is a part of project QuickShop, the name is ShopContainerWatcher.java
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

package org.maxgamer.quickshop.watcher;

import io.papermc.paper.threadedregions.scheduler.ScheduledTask;
import org.jetbrains.annotations.NotNull;
import org.maxgamer.quickshop.QuickShop;
import org.maxgamer.quickshop.api.shop.Shop;
import org.maxgamer.quickshop.shop.ContainerShop;

import java.util.LinkedList;
import java.util.Queue;
import java.util.function.Consumer;

/**
 * Check the shops after server booted up, make sure shop can correct self-deleted when container
 * lost.
 */
public class ShopContainerWatcher implements Consumer<ScheduledTask> {
    private final Queue<Shop> checkQueue = new LinkedList<>();
    private final QuickShop plugin = QuickShop.getInstance();

    public void scheduleCheck(@NotNull Shop shop) {
        checkQueue.add(shop);
    }

    @Override
    public void accept(ScheduledTask scheduledTask) {
        Shop shop;
        while ((shop = checkQueue.poll()) != null && !shop.isDeleted()) {
            if (shop instanceof ContainerShop cs) {
                plugin.getServer().getRegionScheduler().run(plugin, cs.getLocation(), task -> cs.checkContainer());
            }
        }
    }

}
