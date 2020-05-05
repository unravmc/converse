package net.novelmc.util.nbt;

import com.comphenix.protocol.wrappers.nbt.NbtBase;
import com.comphenix.protocol.wrappers.nbt.NbtCompound;
import com.comphenix.protocol.wrappers.nbt.NbtList;
import net.novelmc.util.ConverseBase;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.block.banner.Pattern;
import org.bukkit.block.data.BlockData;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.*;
import org.bukkit.material.MaterialData;
import org.bukkit.potion.PotionEffect;

import java.util.*;
import java.util.stream.Collectors;

// This class is a compact version of ItemFixer#ItemChecker.
// @author leymooo
public class ItemFixer extends ConverseBase {
    private Boolean removeInvalidEnch;
    private Boolean checkEnch;
    private HashSet<String> nbt = new HashSet<>();
    private HashSet<Material> tiles = new HashSet<>();

    public ItemFixer() {
        nbt.addAll(Arrays.asList("ActiveEffects", "Command", "CustomName", "AttributeModifiers", "Unbreakable"));
        tiles.addAll(Arrays.asList(Material.FURNACE, Material.CHEST, Material.TRAPPED_CHEST, Material.DROPPER, Material.DISPENSER,
                Material.COMMAND_BLOCK_MINECART, Material.COMMAND_BLOCK, Material.CHAIN_COMMAND_BLOCK, Material.REPEATING_COMMAND_BLOCK,
                Material.HOPPER, Material.HOPPER_MINECART, Material.BREWING_STAND, Material.BEACON, Material.OAK_SIGN, Material.OAK_WALL_SIGN, Material.DARK_OAK_SIGN,
                Material.DARK_OAK_WALL_SIGN, Material.BIRCH_SIGN, Material.BIRCH_WALL_SIGN, Material.SPRUCE_SIGN,
                Material.SPRUCE_WALL_SIGN, Material.ACACIA_SIGN, Material.ACACIA_WALL_SIGN, Material.JUNGLE_SIGN, Material.JUNGLE_WALL_SIGN,
                Material.SPAWNER, Material.NOTE_BLOCK, Material.JUKEBOX));
        checkEnch = true;
        removeInvalidEnch = true;
    }

    public boolean isSkullItem(NbtCompound tag) {
        if (tag.containsKey("SkullOwner")) {
            NbtCompound a = tag.getCompound("SkullOwner");
            if (a.containsKey("Properties")) {
                NbtCompound b = a.getCompound("Properties");
                if (b.containsKey("textures")) {
                    NbtList<NbtBase> aa = b.getList("textures");
                    for (NbtBase c : aa.asCollection()) {
                        if (c instanceof NbtCompound) {
                            if (((NbtCompound) c).containsKey("Value")) {
                                if (((NbtCompound) c).getString("Value").trim().length() > 0) {
                                    String d = null;
                                    try {
                                        d = new String(Base64.getDecoder().decode(((NbtCompound) c).getString("Value")));
                                    } catch (Exception ex) {
                                        tag.remove("SkullOwner");
                                        return true;
                                    }
                                    if (d == null || d.isEmpty()) {
                                        tag.remove("SkullOwner");
                                        return true;
                                    }
                                    if (d.contains("textures") && d.contains("SKIN")) {
                                        if (d.contains("url")) {
                                            String ee = null;
                                            try {
                                                ee = d.split("url\":")[1].replace("}", "").replace("\"", "");
                                            } catch (ArrayIndexOutOfBoundsException e) {
                                                tag.remove("SkullOwner");
                                                return true;
                                            }
                                            if (ee == null || ee.isEmpty()) {
                                                tag.remove("SkullOwner");
                                                return true;
                                            }
                                            if (ee.startsWith("http://textures.minecraft.net/texture/") || ee.startsWith("https://textures.minecraft.net/texture/")) {
                                                return false;
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                }
                tag.remove("SkullOwner");
                return true;
            }
        }
        return false;
    }

    private boolean checkEnchants(ItemStack stack, Player p) {
        boolean cheat = false;
        if (checkEnch && !p.hasPermission("itemfixer.bypass.enchant") && stack.hasItemMeta() && stack.getItemMeta().hasEnchants()) {
            final ItemMeta meta = stack.getItemMeta();
            Map<Enchantment, Integer> enchantments = null;
            try {
                enchantments = meta.getEnchants();
            } catch (Exception e) {
                clearData(stack);
                p.updateInventory();
                return true;
            }
            for (Map.Entry<Enchantment, Integer> ench : enchantments.entrySet()) {
                Enchantment enchant = ench.getKey();
                String perm = "itemfixer.allow."+stack.getType().toString()+"."+enchant.getName()+"."+ench.getValue();
                if (removeInvalidEnch && !enchant.canEnchantItem(stack) && !p.hasPermission(perm) ) {
                    meta.removeEnchant(enchant);
                    cheat = true;
                }
                if ((ench.getValue() > enchant.getMaxLevel() || ench.getValue() < 0) && !p.hasPermission(perm)) {
                    meta.removeEnchant(enchant);
                    cheat = true;
                }
            }
            if (cheat) stack.setItemMeta(meta);
        }
        return cheat;
    }

    public boolean checkChunk(Chunk c) throws MalformedStateException {
        List<Block> blocks = new ArrayList<>();
        World world = c.getWorld();
        if (world == null) {
            throw new MalformedStateException("World provided returned null!");
        }
        int x = 16*c.getX(); // The chunk's first block.
        int z = 16*c.getZ(); // The chunk's first block.
        int maxX = x+16; // The chunk's end block.
        int maxZ = z+16; // The chunk's end block.
        while (x < maxX) {
            while (z < maxZ) {
                for (int y = 0; y <= 255; y++) {
                    blocks.add(world.getBlockAt(x,y,z));
                }
                z++;
            }
            x++;
        }
        if (blocks.isEmpty() || blocks == null) {
            throw new MalformedStateException("A list which should not be empty or null has returned either empty or null.");
        }
        for (Block block : blocks) {
            for (Material mat : tiles) {
                if (block.getType().equals(mat)) {
                    clearBlock(block);
                }
            }
        }
        return false;
    }

    private boolean checkNbt(ItemStack stack, Player p) {
        boolean cheat = false;
        try {
            if (p.hasPermission("converse.bypass.nbt")) return false;
            Material mat = stack.getType();
            NbtCompound tag = (NbtCompound) MiniFactory.fromItemTag(stack);
            if (tag == null) return false;
            if(this.isCrashItem(stack, tag, mat)) {
                tag.getKeys().clear();
                stack.setAmount(1);
                return true;
            }
            final String tagS = tag.toString();
            for (String nbt1 : nbt) {
                if (tag.containsKey(nbt1)) {
                    tag.remove(nbt1);
                    cheat = true;
                }
            }
            if (tag.containsKey("BlockEntityTag") && !isShulkerBox(stack, stack) && !needIgnore(stack)) {
                tag.remove("BlockEntityTag");
                cheat = true;
            } else if (mat == Material.WRITTEN_BOOK && (tagS.contains("ClickEvent") && tag.containsKey("run_command"))) {
                tag.getKeys().clear();
                cheat = true;
            } else if (mat == Material.LEGACY_MONSTER_EGG && tag.containsKey("EntityTag") && fixEgg(tag)) {
                cheat = true;
            } else if (mat == Material.ARMOR_STAND && tag.containsKey("EntityTag")) {
                tag.remove("EntityTag");
                cheat = true;
            } else if ((mat == Material.LEGACY_SKULL || mat == Material.LEGACY_SKULL_ITEM) && stack.getDurability() == 3) {
                if (isSkullItem(tag)) {
                    cheat = true;
                }
            } else if (mat == Material.FIREWORK_ROCKET && checkFireWork(stack)) {
                cheat = true;
            } else if (mat == Material.LEGACY_BANNER && checkBanner(stack)) {
                cheat = true;
            } else if (isPotion(stack) && tag.containsKey("CustomPotionEffects")
                    && (checkPotion(stack, p) || checkCustomColor(tag.getCompound("CustomPotionEffects")))) {
                cheat = true;
            }
        } catch (Exception e) {
        }
        return cheat;
    }

    private boolean needIgnore(ItemStack stack) {
        Material m = stack.getType();
        return (m == Material.LEGACY_BANNER || (!PlibListener.dd().startsWith("v1_8_R") && (m == Material.SHIELD)));
    }

    private void checkShulkerBox(ItemStack stack, Player p) {
        if (!isShulkerBox(stack, stack)) return;
        BlockStateMeta meta = (BlockStateMeta) stack.getItemMeta();
        ShulkerBox box = (ShulkerBox) meta.getBlockState();
        for (ItemStack is : box.getInventory().getContents()) {
            if (isShulkerBox(is, stack) || isHackedItem(is, p)) {
                box.getInventory().clear();
                meta.setBlockState(box);
                stack.setItemMeta(meta);
                return;
            }
        }
    }

    private boolean isPotion(ItemStack stack) {
        try {
            return stack.hasItemMeta() && stack.getItemMeta() instanceof PotionMeta;
        } catch (IllegalArgumentException e) {
            clearData(stack);
            return false;
        }
    }

    private boolean checkCustomColor(NbtCompound tag) {
        if (tag.containsKey("CustomPotionColor")) {
            int color = tag.getInteger("CustomPotionColor");
            try {
                Color.fromBGR(color);
            } catch (IllegalArgumentException e) {
                tag.remove("CustomPotionColor");
                return true;
            }
        }
        return false;
    }

    private boolean checkPotion(ItemStack stack, Player p) {
        boolean cheat = false;
        if (!p.hasPermission("converse.bypass.potion")) {
            PotionMeta meta = (PotionMeta) stack.getItemMeta();
            for (PotionEffect ef : meta.getCustomEffects()) {
                String perm = "converse.allow.".concat(ef.getType().toString()).concat(".").concat(String.valueOf(ef.getAmplifier()+1));
                if (!p.hasPermission(perm)) {
                    meta.removeCustomEffect(ef.getType());
                    cheat = true;
                }
            }
            if (cheat) {
                stack.setItemMeta(meta);
            }
        }
        return cheat;
    }

    private boolean isShulkerBox(ItemStack stack, ItemStack rootStack) {
        if (stack == null || stack.getType() == Material.AIR) return false;
        if (!PlibListener.d()) return false;
        if (!stack.hasItemMeta()) return false;
        try {
            if (!(stack.getItemMeta() instanceof BlockStateMeta)) return false;
        } catch (IllegalArgumentException e) {
            clearData(rootStack);
            return false;
        }
        BlockStateMeta meta = (BlockStateMeta) stack.getItemMeta();
        return meta.getBlockState() instanceof ShulkerBox;
    }

    public boolean isHackedItem(ItemStack stack, Player p) {
        if (stack == null || stack.getType() == Material.AIR) return false;
        this.checkShulkerBox(stack, p);
        if (this.checkNbt(stack, p)) {
            checkEnchants(stack, p);
            return true;
        }
        return checkEnchants(stack, p);
    }

    private boolean checkBanner(ItemStack stack) {
        ItemMeta meta = stack.getItemMeta();
        boolean cheat = false;
        if (meta instanceof BannerMeta) {
            BannerMeta bmeta = (BannerMeta) meta;
            ArrayList<Pattern> patterns = new ArrayList<Pattern>();
            for (Pattern pattern : bmeta.getPatterns()) {
                if (pattern.getPattern() == null) {
                    cheat = true;
                    continue;
                }
                patterns.add(pattern);
            }
            if (cheat) {
                bmeta.setPatterns(patterns);
                stack.setItemMeta(bmeta);
            }
        }
        return cheat;
    }

    public boolean checkFireWork(ItemStack stack) {
        boolean changed = false;
        FireworkMeta meta = (FireworkMeta) stack.getItemMeta();
        if (meta.getPower() > 3) {
            meta.setPower(3);
            changed = true;
        }
        if (meta.getEffectsSize() > 8) {
            List<FireworkEffect> list = meta.getEffects().stream().limit(8).collect(Collectors.toList());
            meta.clearEffects();
            meta.addEffects(list);
            changed = true;
        }
        if (changed) {
            stack.setItemMeta(meta);
        }
        return changed;
    }

    public void clearBlock(Block block) {
        if (assignable(Chest.class, block.getClass())) {
            Chest chest = (Chest) block;
            ItemStack[] stacks = chest.getBlockInventory().getContents();
            for (ItemStack stack : stacks) {
                Material m = stack.getType();
                NbtCompound compound = (NbtCompound) MiniFactory.fromItemTag(stack);
                if (this.isCrashItem(stack, compound, m)) {
                    chest.getBlockInventory().clear();
                    chest.update(true);
                }
            }
        }
        if (assignable(Furnace.class, block.getClass())) {
            Furnace furnace = (Furnace) block;
            ItemStack[] stacks = furnace.getInventory().getContents();
            for (ItemStack stack : stacks) {
                Material m = stack.getType();
                NbtCompound compound = (NbtCompound) MiniFactory.fromItemTag(stack);
                if (this.isCrashItem(stack, compound, m)) {
                    furnace.getInventory().clear();
                    furnace.update(true);
                }
            }
        }
        if (assignable(Dropper.class, block.getClass())) {
            Dropper dropper = (Dropper) block;
            ItemStack[] stacks = dropper.getInventory().getContents();
            for (ItemStack stack : stacks) {
                Material m = stack.getType();
                NbtCompound compound = (NbtCompound) MiniFactory.fromItemTag(stack);
                if (this.isCrashItem(stack, compound, m)) {
                    dropper.getInventory().clear();
                    dropper.update(true);
                }
            }
        }
        if (assignable(Hopper.class, block.getClass())) {
            Hopper hopper = (Hopper) block;
            ItemStack[] stacks = hopper.getInventory().getContents();
            for (ItemStack stack : stacks) {
                Material m = stack.getType();
                NbtCompound compound = (NbtCompound) MiniFactory.fromItemTag(stack);
                if (this.isCrashItem(stack, compound, m)) {
                    hopper.getInventory().clear();
                    hopper.update(true);
                }
            }
        }
    }

    private boolean assignable(Class<?> c1, Class<?> c2) {
        return c2.isAssignableFrom(c1);
    }

    private boolean isCrashItem(ItemStack stack, NbtCompound tag, Material mat) {
        if (stack.getAmount() <1 || stack.getAmount() > 64 || tag.getKeys().size() > 20) {
            return true;
        }
        int tagL = tag.toString().length();
        if ((mat == Material.NAME_TAG || tiles.contains(mat)) && tagL > 600) {
            return true;
        }
        if (isShulkerBox(stack, stack)) return false;
        return mat == Material.WRITTEN_BOOK ? (tagL >= 22000) : (tagL >= 13000);
    }

    private boolean fixEgg(NbtCompound tag) {
        NbtCompound enttag = tag.getCompound("EntityTag");
        int size = enttag.getKeys().size();
        if (size >= 2 ) {
            Object id = enttag.getObject("id");
            Object color = enttag.getObject("Color");
            enttag.getKeys().clear();
            if (id != null && id instanceof String) {
                enttag.put("id", (String) id);
            }
            if (color != null && color instanceof Byte) {
                enttag.put("Color", (byte) color);
            }
            tag.put("EntityTag", enttag);
            return color==null ? true : size >= 3;
        }
        return false;
    }

    private void clearData(ItemStack stack) {
        NbtCompound tag = (NbtCompound) MiniFactory.fromItemTag(stack);
        if (tag == null) return;
        tag.getKeys().clear();
    }

}
