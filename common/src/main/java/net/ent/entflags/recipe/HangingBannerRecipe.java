package net.ent.entflags.recipe;

import java.util.List;
import java.util.Optional;

import com.mojang.serialization.Codec;
import com.mojang.serialization.MapCodec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.chat.Component;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.BannerItem;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.crafting.CraftingInput;
import net.minecraft.world.item.crafting.CustomRecipe;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.PlacementInfo;
import net.minecraft.world.item.crafting.RecipeSerializer;
import net.minecraft.world.item.crafting.display.RecipeDisplay;
import net.minecraft.world.item.crafting.display.ShapedCraftingRecipeDisplay;
import net.minecraft.world.item.crafting.display.SlotDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BannerPatternLayers;

/**
 * Hanging banner recipe (one per wood and color):
 * <pre>
 * C W C   (iron chain, stripped log/stem, iron chain; "chain" became "iron_chain" in 1.21.9)
 *   B     (banner of the matching color)
 * </pre>
 * Like the flag recipe, a non-special CustomRecipe with its own placement + display so it shows in the recipe book
 * (grouped per wood via "group"); assemble() copies the banner's patterns and custom name.
 * JSON: {"type": "entflags:hanging_banner", "group", "log", "color", "result"}.
 */
public class HangingBannerRecipe extends CustomRecipe {

	public static final MapCodec<HangingBannerRecipe> MAP_CODEC = RecordCodecBuilder.mapCodec(
		instance -> instance.group(
			Codec.STRING.optionalFieldOf("group", "").forGetter(HangingBannerRecipe::group),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("log").forGetter(recipe -> recipe.log),
			DyeColor.CODEC.fieldOf("color").forGetter(recipe -> recipe.color),
			BuiltInRegistries.ITEM.byNameCodec().fieldOf("result").forGetter(recipe -> recipe.result)
		).apply(instance, HangingBannerRecipe::new)
	);
	public static final StreamCodec<RegistryFriendlyByteBuf, HangingBannerRecipe> STREAM_CODEC = StreamCodec.composite(
		ByteBufCodecs.STRING_UTF8, HangingBannerRecipe::group,
		ByteBufCodecs.registry(Registries.ITEM), recipe -> recipe.log,
		DyeColor.STREAM_CODEC, recipe -> recipe.color,
		ByteBufCodecs.registry(Registries.ITEM), recipe -> recipe.result,
		HangingBannerRecipe::new
	);
	public static final RecipeSerializer<HangingBannerRecipe> SERIALIZER = new RecipeSerializer<>(MAP_CODEC, STREAM_CODEC);

	private final String group;
	private final Item log;
	private final DyeColor color;
	private final Item result;
	private PlacementInfo placementInfo;

	public HangingBannerRecipe(String group, Item log, DyeColor color, Item result) {
		this.group = group;
		this.log = log;
		this.color = color;
		this.result = result;
	}

	@Override
	public boolean isSpecial() {
		return false;
	}

	@Override
	public boolean showNotification() {
		return true;
	}

	@Override
	public String group() {
		return this.group;
	}

	@Override
	public PlacementInfo placementInfo() {
		if (this.placementInfo == null) {
			Ingredient chain = Ingredient.of(Items.IRON_CHAIN);
			this.placementInfo = PlacementInfo.createFromOptionals(List.of(
				Optional.of(chain), Optional.of(Ingredient.of(this.log)), Optional.of(chain),
				Optional.empty(), Optional.of(Ingredient.of(Items.BANNER.pick(this.color))), Optional.empty()
			));
		}
		return this.placementInfo;
	}

	// CraftingInput is already trimmed to the filled area, so the shape can sit anywhere in the grid.
	public boolean matches(CraftingInput input, Level level) {
		if (input.width() != 3 || input.height() != 2) {
			return false;
		}
		return input.getItem(0, 0).is(Items.IRON_CHAIN)
			&& input.getItem(1, 0).is(this.log)
			&& input.getItem(2, 0).is(Items.IRON_CHAIN)
			&& input.getItem(0, 1).isEmpty()
			&& input.getItem(1, 1).getItem() instanceof BannerItem banner && banner.getColor() == this.color
			&& input.getItem(2, 1).isEmpty();
	}

	public ItemStack assemble(CraftingInput input) {
		ItemStack bannerStack = input.getItem(1, 1);
		ItemStack hangingBanner = new ItemStack(this.result);

		BannerPatternLayers patterns = bannerStack.get(DataComponents.BANNER_PATTERNS);
		if (patterns != null && !patterns.equals(BannerPatternLayers.EMPTY)) {
			hangingBanner.set(DataComponents.BANNER_PATTERNS, patterns);
		}
		Component customName = bannerStack.get(DataComponents.CUSTOM_NAME);
		if (customName != null) {
			hangingBanner.set(DataComponents.CUSTOM_NAME, customName);
		}
		return hangingBanner;
	}

	@Override
	public List<RecipeDisplay> display() {
		SlotDisplay chain = new SlotDisplay.ItemSlotDisplay(Items.IRON_CHAIN);
		SlotDisplay empty = SlotDisplay.Empty.INSTANCE;
		List<SlotDisplay> ingredients = List.of(
			chain, new SlotDisplay.ItemSlotDisplay(this.log), chain,
			empty, new SlotDisplay.ItemSlotDisplay(Items.BANNER.pick(this.color)), empty
		);
		return List.of(new ShapedCraftingRecipeDisplay(
			3, 2, ingredients, new SlotDisplay.ItemSlotDisplay(this.result), new SlotDisplay.ItemSlotDisplay(Items.CRAFTING_TABLE)
		));
	}

	@Override
	public RecipeSerializer<HangingBannerRecipe> getSerializer() {
		return SERIALIZER;
	}
}
