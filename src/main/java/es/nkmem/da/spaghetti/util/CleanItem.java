package es.nkmem.da.spaghetti.util;

import org.apache.commons.lang.Validate;
import org.bukkit.Material;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Represents an {@link ItemStack} builder with methods that return the instance for chaining. Useful for naming and
 * giving item a lore.
 */
public class CleanItem {

    private Material material;
    private int amount = -1;
    private String name;
    private List<String> lores = new ArrayList<>();
    private List<Enchant> enchs = new ArrayList<>();
    private short durability = -101;
    private ItemStack item;
    protected boolean dirty = true;

    public CleanItem(Material type) {
        material = type;
    }

    public CleanItem(Material type, int amount) {
        material = type;
        this.amount = amount;
    }

    public CleanItem(Material type, int amount, int durability) {
        material = type;
        this.amount = amount;
        this.durability = (short) durability;
    }

    public CleanItem(Material type, short durability) {
        material = type;
        this.durability = durability;
    }

    /**
     * Sets this {@link CleanItem}'s {@link Material}.
     *
     * @param material {@link Material} to set
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem type(Material material) {
        Validate.notNull(material);
        this.material = material;
        this.dirty = true;
        return this;
    }

    /**
     * Sets this {@link CleanItem}'s durability.
     *
     * @param durability durability to set
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem durability(int durability) {
        this.durability = (short) durability;
        this.dirty = true;
        return this;
    }

    /**
     * Sets this {@link CleanItem}'s amount.
     *
     * @param amount amount to set
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem amount(int amount) {
        this.amount = amount;
        this.dirty = true;
        return this;
    }

    /**
     * Sets this {@link CleanItem}'s display name.
     *
     * @param name display name to set, set to null to get rid of display name
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem name(String name) {
        if (name != null) Validate.isTrue(!name.isEmpty());
        this.name = name;
        this.dirty = true;
        return this;
    }

    /**
     * Sets this {@link CleanItem}'s lore.
     *
     * @param lores lores to set, every element is a line
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem lore(String... lores) {
        Validate.notNull(lores);
        Validate.isTrue(lores.length > 0);
        Collections.addAll(this.lores, lores);
        this.dirty = true;
        return this;
    }

    /**
     * Removes lines in this {@link CleanItem}'s lore.
     *
     * @param lores lores to remove
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem removeLore(String... lores) {
        Validate.notNull(lores);
        Validate.isTrue(lores.length > 0);
        for (String lore : lores) {
            if (this.lores.remove(lore)) {
                this.dirty = true;
            }
        }
        return this;
    }

    /**
     * Removes a lore by index in this {@link CleanItem}.
     *
     * @param index index to remove
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem removeLore(int index) {
        Validate.isTrue(index > 0);
        if (this.lores.remove(index) != null) {
            this.dirty = true;
        }
        return this;
    }

    /**
     * Gets this {@link CleanItem}'s lore.
     *
     * @return List of lores
     */
    public List<String> getLores() {
        return lores;
    }

    /**
     * Adds an enchantment to this {@link CleanItem}.
     *
     * @param enchantment enchantment to add
     * @param level       of the {@code enchantment}
     * @param glow        whether the item should glow
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem enchant(Enchantment enchantment, int level, boolean glow) {
        Validate.notNull(enchantment);
        enchs.add(new Enchant(enchantment, level, glow));
        this.dirty = true;
        return this;
    }

    /**
     * Removes an enchantment.
     *
     * @param enchantment enchantment to remove
     * @return this {@link CleanItem} object for chaining
     */
    public CleanItem removeEnchantment(Enchantment enchantment) {
        Validate.notNull(enchantment);
        for (Enchant ench : enchs) {
            if (ench.getEnchantment().equals(enchantment)) {
                this.enchs.remove(ench);
                this.dirty = true;
                break;
            }
        }
        return this;
    }

    public ItemStack toItemStack() {
        if (!dirty) { // Same item as before
            return this.item.clone();
        }
        ItemStack item = new ItemStack(material);
        if (amount != -1) {
            item.setAmount(amount);
        }
        ItemMeta meta = item.getItemMeta();
        if (name != null) {
            meta.setDisplayName(name);
        }
        if (!lores.isEmpty()) {
            meta.setLore(lores);
        }
        for (Enchant e : enchs) {
            meta.addEnchant(e.getEnchantment(), e.getLevel(), e.getGlow());
        }
        item.setItemMeta(meta);
        if (durability != -101) {
            item.setDurability(durability);
        }
        this.item = item.clone(); // In case item is modified
        this.dirty = false;
        return item;
    }

    private static class Enchant {

        private Enchantment ench;
        private int level;
        private boolean glow;

        public Enchant(Enchantment enchantment, int level, boolean glow) {
            Validate.notNull(enchantment);
            ench = enchantment;
            this.level = level;
            this.glow = glow;
        }

        public Enchantment getEnchantment() {
            return ench;
        }

        public int getLevel() {
            return level;
        }

        public boolean getGlow() {
            return glow;
        }
    }

}
