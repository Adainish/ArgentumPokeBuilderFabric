package io.github.adainish.argentumpokebuilderfabric.obj;

import ca.landonjw.gooeylibs2.api.UIManager;
import ca.landonjw.gooeylibs2.api.button.Button;
import ca.landonjw.gooeylibs2.api.button.GooeyButton;
import ca.landonjw.gooeylibs2.api.button.PlaceholderButton;
import ca.landonjw.gooeylibs2.api.button.linked.LinkType;
import ca.landonjw.gooeylibs2.api.button.linked.LinkedPageButton;
import ca.landonjw.gooeylibs2.api.helpers.PaginationHelper;
import ca.landonjw.gooeylibs2.api.page.GooeyPage;
import ca.landonjw.gooeylibs2.api.page.LinkedPage;
import ca.landonjw.gooeylibs2.api.template.types.ChestTemplate;
import com.cobblemon.mod.common.Cobblemon;
import com.cobblemon.mod.common.CobblemonItems;
import com.cobblemon.mod.common.api.pokeball.PokeBalls;
import com.cobblemon.mod.common.api.pokemon.Natures;
import com.cobblemon.mod.common.api.storage.NoPokemonStoreException;
import com.cobblemon.mod.common.api.storage.party.PartyStore;
import com.cobblemon.mod.common.pokeball.PokeBall;
import com.cobblemon.mod.common.pokemon.Gender;
import com.cobblemon.mod.common.pokemon.Pokemon;
import io.github.adainish.argentumpokebuilderfabric.ArgentumPokeBuilderFabric;
import io.github.adainish.argentumpokebuilderfabric.enumerations.BuilderType;
import io.github.adainish.argentumpokebuilderfabric.util.Util;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PokeBuilder
{
    public Player assignedPlayer;
    public BuilderType builderType = BuilderType.UNDECIDED;
    public Pokemon selectedPokemon = null;

    public String selectedAction;


    public PokeBuilder(Player player)
    {
        this.assignedPlayer = player;
    }

    public boolean canAfford()
    {
        return assignedPlayer.tokenCount >= getCost(builderType);
    }

    public int getCost(BuilderType builderType)
    {
        int amount = 0;
        if (ArgentumPokeBuilderFabric.config.pokeBuilderDataManager.baseAttributeCosts.containsKey(builderType))
            amount = ArgentumPokeBuilderFabric.config.pokeBuilderDataManager.baseAttributeCosts.get(builderType);
        return amount;
    }

    public ItemStack getIcon(BuilderType builderType)
    {
        ItemStack stack = new ItemStack(Items.PAPER);
        if (ArgentumPokeBuilderFabric.config.pokeBuilderDataManager.attributeIcons.containsKey(builderType))
            stack = ArgentumPokeBuilderFabric.config.pokeBuilderDataManager.attributeIcons.get(builderType).copy();
        return stack;
    }

    public void open(ServerPlayer serverPlayer)
    {
        UIManager.openUIForcefully(serverPlayer, mainGUI());
    }

    public GooeyButton filler() {
        return GooeyButton.builder()
                .display(new ItemStack(Items.GRAY_STAINED_GLASS_PANE))
                .build();
    }

    public List<Button> editGUIButtons()
    {
        List<Button> buttons = new ArrayList<>();

        switch (this.builderType)
        {
            case EVS -> {

                break;
            }

            case IVS -> {

                break;
            }

            case FORM -> {
                selectedPokemon.getSpecies().getForms().forEach(formData -> {
                    GooeyButton button = GooeyButton.builder()
                            .title(Util.formattedString("&b" + formData.getName()))
                            .display(new ItemStack(CobblemonItems.NEST_BALL.get()))
                            .onClick(b -> {
                                //go to purchase
                                this.selectedAction = formData.getName();
                            })
                            .build();
                    if (selectedPokemon.getForm().equals(formData))
                        button = GooeyButton.builder()
                                .title(Util.formattedString("&cAlready on this form"))
                                .display(new ItemStack(Items.TNT))
                                .build();
                    buttons.add(button);
                });
                break;
            }

            case SHINY -> {
                GooeyButton button;
                if (selectedPokemon.getShiny())
                {
                    button = GooeyButton.builder()
                            .title(Util.formattedString("&cUnshiny"))
                            .display(new ItemStack(Items.ENDER_PEARL))
                            .onClick(b -> {
                                //open purchase
                                this.selectedAction = "false";
                            })
                            .build();
                } else {
                    button = GooeyButton.builder()
                            .title(Util.formattedString("&aShiny"))
                            .display(new ItemStack(CobblemonItems.SHINY_STONE.get()))
                            .onClick(b -> {
                                //open purchase
                                this.selectedAction = "true";
                            })
                            .build();
                }
                buttons.add(button);
                break;
            }
            case GENDER -> {
                //do gender check
                if (selectedPokemon.getGender().equals(Gender.GENDERLESS))
                    return buttons;
                for (Gender gender:Gender.values()) {
                    if (gender.equals(Gender.GENDERLESS))
                        continue;
                    GooeyButton button = GooeyButton.builder()
                            .title(Util.formattedString("&e" + gender.name()))
                            .onClick(b -> {
                                //do purchase
                                this.selectedAction = gender.name();
                            })
                            .display(new ItemStack(Items.LIME_DYE))
                            .build();

                    if (selectedPokemon.getGender().equals(gender))
                        button = GooeyButton.builder()
                                .title(Util.formattedString("&cAlready this gender"))
                                .display(new ItemStack(Items.TNT))
                                .build();
                    buttons.add(button);
                }
                break;
            }
            case NATURE -> {
                Natures.INSTANCE.all().forEach(nature -> {
                    GooeyButton button = GooeyButton.builder()
                            .title(Util.formattedString("") + nature.getDisplayName())
                            .lore(Util.formattedArrayList(Arrays.asList("&a+" + nature.getIncreasedStat(), "&c-" + nature.getDecreasedStat())))
                            .display(new ItemStack(CobblemonItems.MIRACLE_SEED.get()))
                            .onClick(b -> {
                                //open purchase
                                this.selectedAction = nature.getName().toString();
                            })
                            .build();
                    if (selectedPokemon.getNature().equals(nature))
                        button = GooeyButton.builder()
                                .title(Util.formattedString("&cAlready has this nature"))
                                .display(new ItemStack(Items.TNT))
                                .build();
                    buttons.add(button);
                });
                break;
            }
            case POKEBALL -> {
                PokeBalls.INSTANCE.all().forEach(pokeBall -> {
                    GooeyButton button = GooeyButton.builder()
                            .title(Util.formattedString("") + pokeBall.getName().getPath())
                            .display(new ItemStack(pokeBall.item()))
                            .onClick(b -> {
                                //open purchase
                                selectedAction = pokeBall.getName().toString();
                            })
                            .build();
                    if (selectedPokemon.getCaughtBall().equals(pokeBall))
                        button = GooeyButton.builder()
                                .title(Util.formattedString("&cAlready has this poke ball"))
                                .display(new ItemStack(Items.TNT))
                                .build();
                    buttons.add(button);
                });
                break;
            }
            case FRIENDSHIP -> {

                break;
            }
        }

        return buttons;
    }

    public List<Button> builderTypeButtonList()
    {
        List<Button> buttons = new ArrayList<>();
        for (BuilderType builderType:BuilderType.values()) {
            if (builderType.equals(BuilderType.UNDECIDED))
                continue;
            GooeyButton button = GooeyButton.builder()
                    .title(Util.formattedString("&b" + builderType.name().toLowerCase()))
                    .display(getIcon(builderType))
                    .onClick(b -> {
                        this.builderType = builderType;
                        //open adaptable edit menu
                        UIManager.openUIForcefully(b.getPlayer(), editGUI());
                    })
                    .build();
            buttons.add(button);
        }
        return buttons;
    }

    public List<Button> partyMemberButtonList()
    {
        List<Button> buttons = new ArrayList<>();

        try {
            PartyStore partyStore = Cobblemon.INSTANCE.getStorage().getParty(assignedPlayer.uuid);
            partyStore.forEach(pokemon -> {
                GooeyButton button = GooeyButton.builder()
                        .title(Util.formattedString(pokemon.getSpecies().getName()))
                        .display(Util.returnIcon(pokemon))
                        .lore(Util.formattedArrayList(Util.pokemonLore(pokemon)))
                        .onClick(b -> {
                            this.selectedPokemon = pokemon;
                            //open builder type selection GUI
                            UIManager.openUIForcefully(b.getPlayer(), builderTypeGUI());
                        })
                        .build();
                buttons.add(button);
            });
        } catch (NoPokemonStoreException e) {

        }
        return buttons;
    }

    public GooeyPage purchaseGUI()
    {
        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        GooeyButton back = GooeyButton.builder()
                .display(new ItemStack(Items.ARROW))
                .title(Util.formattedString("&eGo Back"))
                .onClick(b -> {
                    UIManager.openUIForcefully(b.getPlayer(), builderTypeGUI());
                })
                .build();

        GooeyButton confirm = GooeyButton.builder()
                .title(Util.formattedString("&aConfirm"))
                .display(new ItemStack(Items.GREEN_DYE))
                .onClick(b -> {
                    if (canAfford())
                    {
                        //convert selected action to convertable and applicable data then transform the pokemon info
                    }
                })
                .build();

        builder.set(0, 3, back);
        builder.set(0, 5, confirm);


        return GooeyPage.builder().build();
    }

    public LinkedPage editGUI()
    {

        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        PlaceholderButton placeHolderButton = new PlaceholderButton();
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Previous Page"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Next Page"))
                .linkType(LinkType.Next)
                .build();

        GooeyButton back = GooeyButton.builder()
                .display(new ItemStack(Items.ARROW))
                .title(Util.formattedString("&eGo Back"))
                .onClick(b -> {
                    UIManager.openUIForcefully(b.getPlayer(), builderTypeGUI());
                })
                .build();

        builder.set(0, 3, previous)
                .set(0, 5, next)
                .set(0, 0, back)
                .rectangle(1, 1, 3, 7, placeHolderButton);

        return PaginationHelper.createPagesFromPlaceholders(builder.build(), editGUIButtons(), LinkedPage.builder().template(builder.build()));
    }

    public LinkedPage builderTypeGUI()
    {

        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        PlaceholderButton placeHolderButton = new PlaceholderButton();
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Previous Page"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Next Page"))
                .linkType(LinkType.Next)
                .build();


        GooeyButton back = GooeyButton.builder()
                .display(new ItemStack(Items.ARROW))
                .title(Util.formattedString("&eGo Back"))
                .onClick(b -> {
                    UIManager.openUIForcefully(b.getPlayer(), mainGUI());
                })
                .build();

        builder.set(0, 3, previous)
                .set(0, 5, next)
                .set(0, 0, back)
                .rectangle(1, 1, 3, 7, placeHolderButton);

        return PaginationHelper.createPagesFromPlaceholders(builder.build(), builderTypeButtonList(), LinkedPage.builder().template(builder.build()));
    }


    public LinkedPage mainGUI()
    {

        ChestTemplate.Builder builder = ChestTemplate.builder(5);
        builder.fill(filler());

        PlaceholderButton placeHolderButton = new PlaceholderButton();
        LinkedPageButton previous = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Previous Page"))
                .linkType(LinkType.Previous)
                .build();

        LinkedPageButton next = LinkedPageButton.builder()
                .display(new ItemStack(Items.SPECTRAL_ARROW))
                .title(Util.formattedString("Next Page"))
                .linkType(LinkType.Next)
                .build();


        builder.set(0, 3, previous)
                .set(0, 5, next)
                .rectangle(1, 1, 3, 7, placeHolderButton);

        return PaginationHelper.createPagesFromPlaceholders(builder.build(), partyMemberButtonList(), LinkedPage.builder().template(builder.build()));
    }
}
