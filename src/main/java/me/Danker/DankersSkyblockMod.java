package me.Danker;

import me.Danker.commands.*;
import me.Danker.events.ChestSlotClickedEvent;
import me.Danker.events.GuiChestBackgroundDrawnEvent;
import me.Danker.events.RenderOverlay;
import me.Danker.features.*;
import me.Danker.features.loot.LootDisplay;
import me.Danker.features.loot.LootTracker;
import me.Danker.features.puzzlesolvers.*;
import me.Danker.gui.*;
import me.Danker.handlers.ConfigHandler;
import me.Danker.handlers.PacketHandler;
import me.Danker.handlers.TextRenderer;
import me.Danker.utils.Utils;
import net.minecraft.block.Block;
import net.minecraft.client.Minecraft;
import net.minecraft.client.entity.EntityPlayerSP;
import net.minecraft.client.gui.GuiMainMenu;
import net.minecraft.client.gui.inventory.GuiChest;
import net.minecraft.client.settings.KeyBinding;
import net.minecraft.command.ICommand;
import net.minecraft.event.ClickEvent;
import net.minecraft.event.ClickEvent.Action;
import net.minecraft.event.HoverEvent;
import net.minecraft.init.Blocks;
import net.minecraft.inventory.Container;
import net.minecraft.inventory.ContainerChest;
import net.minecraft.inventory.IInventory;
import net.minecraft.inventory.Slot;
import net.minecraft.item.Item;
import net.minecraft.item.ItemStack;
import net.minecraft.util.ChatComponentText;
import net.minecraft.util.EnumChatFormatting;
import net.minecraft.util.IChatComponent;
import net.minecraft.util.StringUtils;
import net.minecraft.world.World;
import net.minecraftforge.client.ClientCommandHandler;
import net.minecraftforge.client.GuiIngameForge;
import net.minecraftforge.client.event.ClientChatReceivedEvent;
import net.minecraftforge.client.event.GuiOpenEvent;
import net.minecraftforge.client.event.GuiScreenEvent;
import net.minecraftforge.client.event.RenderGameOverlayEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.event.entity.EntityJoinWorldEvent;
import net.minecraftforge.fml.client.registry.ClientRegistry;
import net.minecraftforge.fml.common.Loader;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.fml.common.Mod.EventHandler;
import net.minecraftforge.fml.common.event.FMLInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPostInitializationEvent;
import net.minecraftforge.fml.common.event.FMLPreInitializationEvent;
import net.minecraftforge.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.fml.common.gameevent.InputEvent.KeyInputEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent;
import net.minecraftforge.fml.common.gameevent.TickEvent.Phase;
import net.minecraftforge.fml.common.network.FMLNetworkEvent.ClientConnectedToServerEvent;
import org.lwjgl.input.Keyboard;
import org.lwjgl.input.Mouse;
import java.lang.reflect.Method;
import me.Danker.events.GuiChestBackgroundDrawnEvent;

import javax.sound.sampled.LineUnavailableException;
import javax.sound.sampled.UnsupportedAudioFileException;
import java.io.IOException;
import java.util.*;

@Mod(modid = DankersSkyblockMod.MODID, version = DankersSkyblockMod.VERSION, clientSideOnly = true)
public class DankersSkyblockMod {
    public static final String MODID = "Danker's Skyblock Mod";
    public static final String VERSION = "1.8.6";
    public static int titleTimer = -1;
    public static boolean showTitle = false;
    public static String titleText = "";
    public static int tickAmount = 1;
    public static KeyBinding[] keyBindings = new KeyBinding[5];
    public static boolean usingLabymod = false;
    public static boolean usingOAM = false;
    static boolean OAMWarning = false;
    public static String guiToOpen = null;
    public static boolean firstLaunch = false;
    public static String configDirectory;
    public static int until = 0;
    public static int lastSlot = -1;
    public static int slotIn = -1;
    static String[] harpInv = new String[54];
    public static int chestOpen = 0;
    public static long lastInteractTime;
    static int[] terminalNumberNeeded = new int[13];
    static int[] chest = new int[54];
    public int mazeId = 0;
    public int sword = 10;
    public int bow = 10;
    static int lastUltraSequencerClicked = 0;
    static Slot[] clickInOrderSlots = new Slot[36];
    public int chestSize;
    static int lastChronomatronRound = 0;
    static List<String> chronomatronPattern = new ArrayList<>();
    static int chronomatronMouseClicks = 0;
    static ItemStack[] experimentTableSlots = new ItemStack[54];

    public static String MAIN_COLOUR;
    public static String SECONDARY_COLOUR;
    public static String ERROR_COLOUR;
    public static String DELIMITER_COLOUR;
    public static String TYPE_COLOUR;
    public static String VALUE_COLOUR;
    public static String SKILL_AVERAGE_COLOUR;
    public static String ANSWER_COLOUR;


    @EventHandler
    public void init(FMLInitializationEvent event) throws UnsupportedAudioFileException, IOException, LineUnavailableException {
        MinecraftForge.EVENT_BUS.register(this);
        MinecraftForge.EVENT_BUS.register(new ArachneESP());
        MinecraftForge.EVENT_BUS.register(new AutoDisplay());
        MinecraftForge.EVENT_BUS.register(new AutoSwapToPickBlock());
        MinecraftForge.EVENT_BUS.register(new BlazeSolver());
        MinecraftForge.EVENT_BUS.register(new BonzoMaskTimer());
        MinecraftForge.EVENT_BUS.register(new BoulderSolver());
        MinecraftForge.EVENT_BUS.register(new CakeTimer());
        MinecraftForge.EVENT_BUS.register(new ChronomatronSolver());
        MinecraftForge.EVENT_BUS.register(new ClickInOrderSolver());
        MinecraftForge.EVENT_BUS.register(new CreeperSolver());
        MinecraftForge.EVENT_BUS.register(new CustomMusic());
        MinecraftForge.EVENT_BUS.register(new DungeonTimer());
        MinecraftForge.EVENT_BUS.register(new ExpertiseLore());
        MinecraftForge.EVENT_BUS.register(new FasterMaddoxCalling());
        MinecraftForge.EVENT_BUS.register(new GoldenEnchants());
        MinecraftForge.EVENT_BUS.register(new GolemSpawningAlert());
        MinecraftForge.EVENT_BUS.register(new GpartyNotifications());
        MinecraftForge.EVENT_BUS.register(new HideTooltipsInExperiments());
        MinecraftForge.EVENT_BUS.register(new IceWalkSolver());
        MinecraftForge.EVENT_BUS.register(new LividSolver());
        MinecraftForge.EVENT_BUS.register(new LootDisplay());
        MinecraftForge.EVENT_BUS.register(new LootTracker());
        MinecraftForge.EVENT_BUS.register(new LowHealthNotifications());
        MinecraftForge.EVENT_BUS.register(new NecronNotifications());
        MinecraftForge.EVENT_BUS.register(new NoF3Coords());
        MinecraftForge.EVENT_BUS.register(new NotifySlayerSlain());
        MinecraftForge.EVENT_BUS.register(new PetColours());
        MinecraftForge.EVENT_BUS.register(new Reparty());
        MinecraftForge.EVENT_BUS.register(new SelectAllColourSolver());
        MinecraftForge.EVENT_BUS.register(new SilverfishSolver());
        MinecraftForge.EVENT_BUS.register(new Skill50Display());
        MinecraftForge.EVENT_BUS.register(new SkillTracker());
        MinecraftForge.EVENT_BUS.register(new SlayerESP());
        MinecraftForge.EVENT_BUS.register(new SpamHider());
        MinecraftForge.EVENT_BUS.register(new SpiritBearAlert());
        MinecraftForge.EVENT_BUS.register(new StartsWithSolver());
        MinecraftForge.EVENT_BUS.register(new StopSalvagingStarredItems());
        MinecraftForge.EVENT_BUS.register(new SuperpairsSolver());
        MinecraftForge.EVENT_BUS.register(new ThreeManSolver());
        MinecraftForge.EVENT_BUS.register(new TicTacToeSolver());
        MinecraftForge.EVENT_BUS.register(new TriviaSolver());
        MinecraftForge.EVENT_BUS.register(new UltrasequencerSolver());
        MinecraftForge.EVENT_BUS.register(new UpdateChecker());
        MinecraftForge.EVENT_BUS.register(new WatcherReadyAlert());
        MinecraftForge.EVENT_BUS.register(new WaterSolver());

        ConfigHandler.reloadConfig();
        GoldenEnchants.init();
        TriviaSolver.init();
        CustomMusic.init(configDirectory);

        keyBindings[0] = new KeyBinding("Open Maddox Menu", Keyboard.KEY_M, "Danker's Skyblock Mod");
        keyBindings[1] = new KeyBinding("Regular Ability", Keyboard.KEY_NUMPAD4, "Danker's Skyblock Mod");
        keyBindings[2] = new KeyBinding("Start/Stop Skill Tracker", Keyboard.KEY_NUMPAD5, "Danker's Skyblock Mod");
        keyBindings[3] = new KeyBinding("Hyperion Bind", Keyboard.KEY_F, "Danker's Skyblock Mod");
        keyBindings[4] = new KeyBinding("Ghost Block Bind", Keyboard.KEY_G, "Danker's Skyblock Mod");

        for (KeyBinding keyBinding : keyBindings) {
            ClientRegistry.registerKeyBinding(keyBinding);
        }
    }
    public void GuiChestBackgroundDrawnEvent(GuiChest chest, String displayName, int chestSize, List<Slot> slots) {
        this.chestSize = chestSize;
    }


    @EventHandler
    public void preInit(final FMLPreInitializationEvent event) {
        ClientCommandHandler.instance.registerCommand(new ArmourCommand());
        ClientCommandHandler.instance.registerCommand(new BankCommand());
        ClientCommandHandler.instance.registerCommand(new CustomMusicCommand());
        ClientCommandHandler.instance.registerCommand(new DHelpCommand());
        ClientCommandHandler.instance.registerCommand(new DankerGuiCommand());
        ClientCommandHandler.instance.registerCommand(new DisplayCommand());
        ClientCommandHandler.instance.registerCommand(new DungeonsCommand());
        ClientCommandHandler.instance.registerCommand(new FairySoulsCommand());
        ClientCommandHandler.instance.registerCommand(new GetkeyCommand());
        ClientCommandHandler.instance.registerCommand(new GuildOfCommand());
        ClientCommandHandler.instance.registerCommand(new ImportFishingCommand());
        ClientCommandHandler.instance.registerCommand(new LobbyBankCommand());
        ClientCommandHandler.instance.registerCommand(new LobbySkillsCommand());
        ClientCommandHandler.instance.registerCommand(new LootCommand());
        ClientCommandHandler.instance.registerCommand(new MoveCommand());
        ClientCommandHandler.instance.registerCommand(new PetsCommand());
        ClientCommandHandler.instance.registerCommand(new ReloadConfigCommand());
        ClientCommandHandler.instance.registerCommand(new ResetLootCommand());
        ClientCommandHandler.instance.registerCommand(new ScaleCommand());
        ClientCommandHandler.instance.registerCommand(new SetkeyCommand());
        ClientCommandHandler.instance.registerCommand(new SkillTrackerCommand());
        ClientCommandHandler.instance.registerCommand(new SkillsCommand());
        ClientCommandHandler.instance.registerCommand(new SkyblockPlayersCommand());
        ClientCommandHandler.instance.registerCommand(new SlayerCommand());
        ClientCommandHandler.instance.registerCommand(new ToggleCommand());
        ClientCommandHandler.instance.registerCommand(new SleepCommand());


        configDirectory = event.getModConfigurationDirectory().toString();
    }

    @EventHandler
    public void postInit(final FMLPostInitializationEvent event) {
		Package[] packages = Package.getPackages();
		for (Package p : packages){
			if (p.getName().startsWith("com.spiderfrog.gadgets") || p.getName().startsWith("com.spiderfrog.oldanimations")){
				usingOAM = true;
				break;
			}
		}
		System.out.println("OAM detection: " + usingOAM);

    	usingLabymod = Loader.isModLoaded("labymod");
    	System.out.println("LabyMod detection: " + usingLabymod);

        if (!ClientCommandHandler.instance.getCommands().containsKey("reparty")) {
            ClientCommandHandler.instance.registerCommand(new RepartyCommand());
        } else if (ConfigHandler.getBoolean("commands", "reparty")) {
            for (Map.Entry<String, ICommand> entry : ClientCommandHandler.instance.getCommands().entrySet()) {
                if (entry.getKey().equals("reparty") || entry.getKey().equals("rp")) {
                    entry.setValue(new RepartyCommand());
                }
            }
        }

    }

    @SubscribeEvent
	public void onGuiOpenEvent(GuiOpenEvent event) {
		if (event.gui instanceof GuiMainMenu && usingOAM && !OAMWarning) {
		    event.gui = new WarningGuiRedirect(new WarningGui());
		    OAMWarning = true;
		}
	}

    @SubscribeEvent
    public void onJoin(EntityJoinWorldEvent event) {
        if (firstLaunch) {
            firstLaunch = false;
            ConfigHandler.writeBooleanConfig("misc", "firstLaunch", false);

            IChatComponent chatComponent = new ChatComponentText(
                    EnumChatFormatting.GOLD + "Thank you for downloading Danker's Skyblock Mod.\n" +
                         "To get started, run the command " + EnumChatFormatting.GOLD + "/dsm" + EnumChatFormatting.RESET + " to view all the mod features."
            );
            chatComponent.setChatStyle(chatComponent.getChatStyle().setChatHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, new ChatComponentText("Click to open the DSM menu."))).setChatClickEvent(new ClickEvent(Action.RUN_COMMAND, "/dsm")));

            new Thread(() -> {
                while (true) {
                    if (Minecraft.getMinecraft().thePlayer == null) {
                        try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        continue;
                    }
                    try {
                        Thread.sleep(2000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    Minecraft.getMinecraft().thePlayer.addChatMessage(chatComponent);
                    break;
                }
            }).start();
        }
    }

    @SubscribeEvent
    public void onChat(ClientChatReceivedEvent event) {
    	String message = StringUtils.stripControlCodes(event.message.getUnformattedText());

        if (message.startsWith("Your new API key is ") && Utils.isOnHypixel()) {
            String apiKey = event.message.getSiblings().get(0).getChatStyle().getChatClickEvent().getValue();
            ConfigHandler.writeStringConfig("api", "APIKey", apiKey);
            Minecraft.getMinecraft().thePlayer.addChatMessage(new ChatComponentText(DankersSkyblockMod.MAIN_COLOUR + "Set API key to " + DankersSkyblockMod.SECONDARY_COLOUR + apiKey));
        }
    }

    @SubscribeEvent
    public void renderPlayerInfo(final RenderGameOverlayEvent.Post event) {
        if (usingLabymod && !(Minecraft.getMinecraft().ingameGUI instanceof GuiIngameForge)) return;
        if (event.type != RenderGameOverlayEvent.ElementType.EXPERIENCE && event.type != RenderGameOverlayEvent.ElementType.JUMPBAR)
            return;
        if (Minecraft.getMinecraft().currentScreen instanceof EditLocationsGui) return;
        MinecraftForge.EVENT_BUS.post(new RenderOverlay());
    }

    // LabyMod Support
    @SubscribeEvent
    public void renderPlayerInfoLabyMod(final RenderGameOverlayEvent event) {
        if (!usingLabymod) return;
        if (event.type != null) return;
        if (Minecraft.getMinecraft().currentScreen instanceof EditLocationsGui) return;
        MinecraftForge.EVENT_BUS.post(new RenderOverlay());
    }

    @SubscribeEvent
    public void renderPlayerInfo(RenderOverlay event) {
        if (showTitle) {
            Utils.drawTitle(titleText);
        }
    }

    @SubscribeEvent
    public void onTick(TickEvent.ClientTickEvent event) {
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;
        EntityPlayerSP player = mc.thePlayer;

        if (mc.currentScreen == null && System.currentTimeMillis() - lastInteractTime >= 250L) {
            slotIn = -1;
            lastSlot = -1;
            mazeId = 0;
        }
         if (keyBindings[3].isKeyDown())
            for (int i = 0; i <= 8; i++) {
                ItemStack item = player.inventory.getStackInSlot(i);
                if ((item != null && item.getDisplayName().contains("Hyperion")) || (item != null && item.getDisplayName().contains("Aspect of the End"))) {
                    player.inventory.currentItem = i;
                    mc.playerController.sendUseItem(mc.thePlayer, world, player.inventory.getStackInSlot(i));
                    break;
                }
            }
        if (keyBindings[4].isKeyDown()) {
            if (mc.objectMouseOver.getBlockPos() == null) return;
            Block block = (Minecraft.getMinecraft()).theWorld.getBlockState(mc.objectMouseOver.getBlockPos()).getBlock();
            ArrayList<Block> interactables = new ArrayList<>(Arrays.asList(new Block[] {
                    Blocks.acacia_door, Blocks.anvil, Blocks.beacon, Blocks.bed, Blocks.birch_door, Blocks.brewing_stand, Blocks.command_block, Blocks.crafting_table, Blocks.chest, Blocks.dark_oak_door,
                    Blocks.daylight_detector, Blocks.daylight_detector_inverted, Blocks.dispenser, Blocks.dropper, Blocks.enchanting_table, Blocks.ender_chest, Blocks.furnace, Blocks.hopper, Blocks.jungle_door, Blocks.lever,
                    Blocks.noteblock, Blocks.powered_comparator, Blocks.unpowered_comparator, Blocks.powered_repeater, Blocks.unpowered_repeater, Blocks.standing_sign, Blocks.wall_sign, Blocks.trapdoor, Blocks.trapped_chest, Blocks.wooden_button,
                    Blocks.stone_button, Blocks.oak_door, Blocks.skull }));
            if (!interactables.contains(block)) {
                world.setBlockToAir(mc.objectMouseOver.getBlockPos());
            }
        }


        if (event.phase != Phase.START) return;

        tickAmount++;
        if (tickAmount % 20 == 0) {
            if (player != null) {
                Utils.checkForSkyblock();
                Utils.checkForDungeons();
            }

            tickAmount = 0;
        }

        if (titleTimer >= 0) {
            if (titleTimer == 0) {
                showTitle = false;
            }
            titleTimer--;
        }
    }

    // Delay GUI by 1 tick
    @SubscribeEvent
    public void onRenderTick(TickEvent.RenderTickEvent event) {
        if (guiToOpen != null) {
            Minecraft mc = Minecraft.getMinecraft();
            if (guiToOpen.startsWith("dankergui")) {
                int page = Character.getNumericValue(guiToOpen.charAt(guiToOpen.length() - 1));
                mc.displayGuiScreen(new DankerGui(page, ""));
            } else {
                switch (guiToOpen) {
                    case "displaygui":
                        mc.displayGuiScreen(new DisplayGui());
                        break;
                    case "editlocations":
                        mc.displayGuiScreen(new EditLocationsGui());
                        break;
                    case "puzzlesolvers":
                        mc.displayGuiScreen(new PuzzleSolversGui(1));
                        break;
                    case "experimentsolvers":
                        mc.displayGuiScreen(new ExperimentsGui());
                        break;
                    case "skilltracker":
                        mc.displayGuiScreen(new SkillTrackerGui());
                        break;
                    case "custommusic":
                        mc.displayGuiScreen(new CustomMusicGui());
                        break;
                }
            }
            guiToOpen = null;
        }
    }

    @SubscribeEvent
    public void onKey(KeyInputEvent event) {
        if (!Utils.inSkyblock) return;
        Minecraft mc = Minecraft.getMinecraft();
        World world = mc.theWorld;

        EntityPlayerSP player = Minecraft.getMinecraft().thePlayer;
        if (keyBindings[1].isPressed()) {
            if (Utils.inDungeons) {
                player.dropOneItem(true);
            }
        }
        /* if (keyBindings[3].isPressed()) {
            int[] order = new int[9];
            for (int i = 0; i <= 8; i++) {
                ItemStack item = player.inventory.getStackInSlot(i);
                if (item != null && item.getDisplayName().contains("Bonemerang"))
                    order[i] = 1;
                if ((item != null && item.getDisplayName().contains("Giant's Sword")) || (item != null && item.getDisplayName().contains("Emerald")))
                    this.sword = i;
                if (item != null && item.getDisplayName().contains("Bow"))
                    this.bow = i;
            }
            new Thread(() -> {
                for (int i = 0; i <= 8; i++) {
                    if (order[i] != 0) {
                        player.inventory.currentItem = i;
                        mc.playerController.sendUseItem(mc.thePlayer, world, player.inventory.getStackInSlot(i));
                        try {
                            Thread.sleep(DelayCommand.boneDelay);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
                if (this.sword != 10 && this.bow != 10 && SwapCommand.swapDelay != 0) {
                    player.inventory.currentItem = this.sword;
                    try {
                        Thread.sleep(SwapCommand.swapDelay);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    player.inventory.currentItem = this.bow;
                }
            }).start();
        }
        if (keyBindings[4].isPressed()) {
            mazeId = 0;
            slotIn = -1;
        }
        if (keyBindings[5].isPressed()) {
            for (int i = 0; i <= SimonCommand.simonAmount; i++) {
                try {
                    Method method = mc.getClass().getDeclaredMethod("func_147121_ag", new Class[0]);
                    method.setAccessible(true);
                    method.invoke(mc, new Object[0]);
                } catch (Exception exception) {
                    exception.printStackTrace();
                }
            }
        }*/
    }

    @SubscribeEvent
    public void onGuiMouseInputPre(GuiScreenEvent.MouseInputEvent.Pre event) {
        if (!Utils.inSkyblock) return;
        if (Mouse.getEventButton() != 0 && Mouse.getEventButton() != 1 && Mouse.getEventButton() != 2)
            return; // Left click, middle click or right click
        if (!Mouse.getEventButtonState()) return;

        if (event.gui instanceof GuiChest) {
            Container containerChest = ((GuiChest) event.gui).inventorySlots;
            if (containerChest instanceof ContainerChest) {
                // a lot of declarations here, if you get scarred, my bad
                GuiChest chest = (GuiChest) event.gui;
                IInventory inventory = ((ContainerChest) containerChest).getLowerChestInventory();
                Slot slot = chest.getSlotUnderMouse();
                if (slot == null) return;
                ItemStack item = slot.getStack();
                String inventoryName = inventory.getDisplayName().getUnformattedText();
                if (item == null) {
                    if (MinecraftForge.EVENT_BUS.post(new ChestSlotClickedEvent(chest, inventory, inventoryName, slot))) event.setCanceled(true);
                } else {
                    if (MinecraftForge.EVENT_BUS.post(new ChestSlotClickedEvent(chest, inventory, inventoryName, slot, item))) event.setCanceled(true);
                }
            }
        }
    }

    @SubscribeEvent
    public void onGuiRender(GuiScreenEvent event) {
        if (event.gui instanceof GuiChest) {
            Minecraft mc = Minecraft.getMinecraft();
            GuiChest inventory = (GuiChest) event.gui;
            List<Slot> invSlots = inventory.inventorySlots.inventorySlots;


        Container containerChest = inventory.inventorySlots;
        String displayName = ((ContainerChest) containerChest).getLowerChestInventory().getDisplayName().getUnformattedText().trim();
        if (ToggleCommand.clickInOrderToggled && Utils.inDungeons && displayName.equals("Chest") && tickAmount != until) {
            if (chestOpen == 0) {
                until = tickAmount;
                chestOpen = 1;
                return;
            }
            mc.thePlayer.closeScreen();
            chestOpen = 0;
        }
        if (displayName.contains("Harp") && ToggleCommand.startsWithToggled) {
            String[] currentInv = new String[54];
            Container playerContainer = mc.thePlayer.openContainer;
            IInventory chestInventory = ((ContainerChest) playerContainer).getLowerChestInventory();
            for (int i = 37; i <= 43; i++) {
                ItemStack itemStack = chestInventory.getStackInSlot(i);
                if (itemStack != null &&
                        itemStack.getUnlocalizedName().toLowerCase().contains("quartz")) {
                    for (int j = 0; j <= 53; j++) {
                        ItemStack name = chestInventory.getStackInSlot(j);
                        if (name != null)
                            currentInv[j] = name.toString();
                    }
                    if (!Arrays.toString(currentInv).equals(Arrays.toString(harpInv))) {
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, i, 2, 0, mc.thePlayer);
                        mc.thePlayer.addChatMessage(new ChatComponentText("clicked"));
                        until = tickAmount;
                        harpInv = currentInv;
                    }
                }
            }
        }
        if (displayName.equals("Navigate the maze!") && invSlots.size() == 90 && ToggleCommand.startsWithToggled && System.currentTimeMillis() - lastInteractTime >= SleepCommand.waitAmount) {
            if (mazeId <= mc.thePlayer.openContainer.windowId) {
                mazeId = mc.thePlayer.openContainer.windowId;
            }
            Container playerContainer = mc.thePlayer.openContainer;
            IInventory chestInventory = ((ContainerChest) playerContainer).getLowerChestInventory();
            if (slotIn == -1) {
                System.out.println("checking chest");
                for (int i = 0; i <= 53; i++) {
                    ItemStack itemStack = chestInventory.getStackInSlot(i);
                    if (itemStack != null) {
                        int type = itemStack.getItemDamage();
                        if (type == 0)
                            chest[i] = 1;
                        if (type == 5) {
                            slotIn = i;
                            chest[i] = 2;
                        }
                    }
                }
            }
            int firstCheck = slotIn + 1;
            int secondCheck = slotIn + 9;
            int thirdCheck = slotIn - 1;
            int fourthCheck = slotIn - 9;
            System.out.println("attempt " + slotIn);
            if (firstCheck % 9 != 0 && firstCheck <= 53 &&
                    chest[firstCheck] == 1) {
                chest[firstCheck] = 0;
                slotIn = firstCheck;
                System.out.println("1");
                mc.playerController.windowClick(mazeId, firstCheck, 0, 0, mc.thePlayer);
                mazeId++;
                lastInteractTime = System.currentTimeMillis();
            } else if (secondCheck <= 53 &&
                    chest[secondCheck] == 1) {
                chest[secondCheck] = 0;
                slotIn = secondCheck;
                System.out.println("2");
                mc.playerController.windowClick(mazeId, secondCheck, 0, 0, mc.thePlayer);
                mazeId++;
                lastInteractTime = System.currentTimeMillis();
            } else if (thirdCheck % 9 != 8 && thirdCheck >= 0 && thirdCheck <= 53 &&
                    chest[thirdCheck] == 1) {
                chest[thirdCheck] = 0;
                slotIn = thirdCheck;
                System.out.println("3");
                mc.playerController.windowClick(mazeId, thirdCheck, 0, 0, mc.thePlayer);
                mazeId++;
                lastInteractTime = System.currentTimeMillis();
            } else if (fourthCheck >= 0 && (Minecraft.getMinecraft()).currentScreen != null &&
                    chest[fourthCheck] == 1) {
                chest[fourthCheck] = 0;
                slotIn = fourthCheck;
                System.out.println("4");
                mc.playerController.windowClick(mazeId, fourthCheck, 0, 0, mc.thePlayer);
                mazeId++;
                lastInteractTime = System.currentTimeMillis();
            }
            if (!Utils.inSkyblock) return;
            if (event.gui instanceof GuiChest) {
                if (containerChest instanceof ContainerChest) {
                    int chestSize = inventory.inventorySlots.inventorySlots.size();

                    MinecraftForge.EVENT_BUS.post(new GuiChestBackgroundDrawnEvent(inventory, displayName, chestSize, invSlots));
                }
            }
        }
        if (ToggleCommand.startsWithToggled && System.currentTimeMillis() - lastInteractTime >= SleepCommand.waitAmount && displayName.startsWith("What starts with:")) {
            char letter = displayName.charAt(displayName.indexOf("'") + 1);
            if (mazeId <= mc.thePlayer.openContainer.windowId)
                mazeId = mc.thePlayer.openContainer.windowId;
            for (Slot startsWith : invSlots) {
                if (startsWith.slotNumber <= lastSlot ||
                        startsWith.getSlotIndex() <= lastSlot)
                    continue;
                ItemStack item = startsWith.getStack();
                if (item == null ||
                        item.isItemEnchanted())
                    continue;
                if (StringUtils.stripControlCodes(item.getDisplayName()).charAt(0) == letter) {
                    mc.playerController.windowClick(mazeId, startsWith.slotNumber, 0, 0, mc.thePlayer);
                    mazeId++;
                    lastSlot = startsWith.getSlotIndex();
                    lastInteractTime = System.currentTimeMillis();
                    break;
                }
                if (mazeId - 15 > mc.thePlayer.openContainer.windowId)
                    break;
            }
        }
        if (ToggleCommand.startsWithToggled && System.currentTimeMillis() - lastInteractTime >= SleepCommand.waitAmount && displayName.equals("Correct all the panes!")) {
            for (Slot startsWith : invSlots) {
                if (startsWith.getSlotIndex() > 53 ||
                        startsWith.getSlotIndex() <= lastSlot)
                    continue;
                ItemStack item = startsWith.getStack();
                if (item == null ||
                        item.isItemEnchanted())
                    continue;
                if (item.getDisplayName().contains("Off") && (Minecraft.getMinecraft()).currentScreen != null) {
                    if (mazeId <= mc.thePlayer.openContainer.windowId)
                        mazeId = mc.thePlayer.openContainer.windowId;
                    mc.playerController.windowClick(mazeId, startsWith.slotNumber, 0, 0, mc.thePlayer);
                    mazeId++;
                    lastSlot = startsWith.getSlotIndex();
                    lastInteractTime = System.currentTimeMillis();
                    break;
                }
                if (mazeId - 15 > mc.thePlayer.openContainer.windowId)
                    break;
            }
        }
        if (ToggleCommand.startsWithToggled && System.currentTimeMillis() - lastInteractTime >= SleepCommand.waitAmount && displayName.startsWith("Select all the")) {
            for (Slot slot : invSlots) {
                String colour = displayName.split(" ")[3];
                if (slot.getSlotIndex() > 53 || slot.getSlotIndex() <= lastSlot) continue;
                ItemStack item = slot.getStack();
                if (item == null || item.isItemEnchanted()) continue;
                String itemName = StringUtils.stripControlCodes(item.getDisplayName()).toUpperCase();
                if (item.getDisplayName().toUpperCase().contains(colour) || (colour.equals("SILVER") && itemName.contains("LIGHT GRAY")) || (colour.equals("WHITE") && itemName.equals("WOOL")) || (colour.equals("BLACK") && itemName.contains("INK")) || (colour.equals("BROWN") && itemName.contains("COCOA")) || (colour.equals("BLUE") && itemName.contains("LAPIS")) || (colour.equals("WHITE") && itemName.contains("BONE"))) {
                    if (mazeId <= mc.thePlayer.openContainer.windowId)
                        mazeId = mc.thePlayer.openContainer.windowId;
                    lastInteractTime = System.currentTimeMillis();
                    mc.playerController.windowClick(mazeId, slot.slotNumber, 2, 0, mc.thePlayer);
                    lastSlot = slot.getSlotIndex();
                    mazeId++;
                    break;
                }
                if (mazeId - 15 > mc.thePlayer.openContainer.windowId)
                    break;
            }
        }
        if (displayName.equals("Click in order!") && System.currentTimeMillis() - lastInteractTime >= SleepCommand.waitAmount && ToggleCommand.startsWithToggled) {
            Container playerContainer = mc.thePlayer.openContainer;
            IInventory chestInventory = ((ContainerChest) playerContainer).getLowerChestInventory();
            if (mazeId <= mc.thePlayer.openContainer.windowId)
                mazeId = mc.thePlayer.openContainer.windowId;
            int[] order = new int[14];
            int i;
            for (i = 10; i <= 25; i++) {
                if (i != 17 && i != 18) {
                    ItemStack click = chestInventory.getStackInSlot(i);
                    if (click == null)
                        break;
                    order[(chestInventory.getStackInSlot(i)).stackSize - 1] = i;
                }
            }
            for (i = 0; i < order.length &&
                    order[i] != 0; i++) {
                if (i > lastSlot) {
                    ItemStack check = chestInventory.getStackInSlot(order[i]);
                    if (order[i] != 0 && check != null && check.getItemDamage() == 14) {
                        mc.playerController.windowClick(mazeId, order[i], 2, 0, mc.thePlayer);
                        mazeId++;
                        lastSlot = i;
                        lastInteractTime = System.currentTimeMillis();
                        break;
                    }
                    if (mazeId - 15 > mc.thePlayer.openContainer.windowId)
                        break;
                }
            }
        }
        if (ToggleCommand.ultrasequencerToggled && displayName.startsWith("Ultrasequencer (")) {
            EntityPlayerSP player = mc.thePlayer;
            if (invSlots.size() > 48 && invSlots.get(49).getStack() != null) {
                if (invSlots.get(49).getStack().getDisplayName().startsWith("§7Timer: §a")) {
                    lastUltraSequencerClicked = 0;
                    for (Slot slot : clickInOrderSlots) {
                        if (slot != null && slot.getStack() != null && StringUtils.stripControlCodes(slot.getStack().getDisplayName()).matches("\\d+")) {
                            int number = Integer.parseInt(StringUtils.stripControlCodes(slot.getStack().getDisplayName()));
                            if (number > lastUltraSequencerClicked) {
                                lastUltraSequencerClicked = number;
                            }
                        }
                    }
                    if (clickInOrderSlots[lastUltraSequencerClicked] != null && player.inventory.getItemStack() == null && tickAmount % 2 == 0 && lastUltraSequencerClicked != 0 && until == lastUltraSequencerClicked) {
                        Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked];
                        new TextRenderer(mc, String.valueOf(mc.thePlayer.openContainer.windowId), 50, 50, 1.0D);
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, nextSlot.slotNumber, 0, 0, mc.thePlayer);
                        Utils.drawOnSlot(chestSize, nextSlot.xDisplayPosition, nextSlot.yDisplayPosition, -448725184);
                        until = lastUltraSequencerClicked + 1;
                        tickAmount = 0;
                    }
                    if (clickInOrderSlots[lastUltraSequencerClicked] != null && player.inventory.getItemStack() == null && tickAmount == 18 && lastUltraSequencerClicked < 1) {
                        Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked];
                        new TextRenderer(mc, String.valueOf(mc.thePlayer.openContainer.windowId), 50, 50, 1.0D);
                        mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, nextSlot.slotNumber, 0, 0, mc.thePlayer);
                        Utils.drawOnSlot(chestSize, nextSlot.xDisplayPosition, nextSlot.yDisplayPosition, -448725184);
                        tickAmount = 0;
                        until = 1;
                    }
                    if (lastUltraSequencerClicked + 1 < clickInOrderSlots.length && clickInOrderSlots[lastUltraSequencerClicked + 1] != null) {
                        Slot nextSlot = clickInOrderSlots[lastUltraSequencerClicked + 1];
                        Utils.drawOnSlot(chestSize, nextSlot.xDisplayPosition, nextSlot.yDisplayPosition, -683615514);
                    }
                }
            }
        }
        if (ToggleCommand.chronomatronToggled && displayName.startsWith("Chronomatron (")) {
            EntityPlayerSP player = mc.thePlayer;
            if (player.inventory.getItemStack() == null && invSlots.size() > 48 && invSlots.get(49).getStack() != null) {
                if (invSlots.get(49).getStack().getDisplayName().startsWith("§7Timer: §a") && invSlots.get(4).getStack() != null) {
                    int round = invSlots.get(4).getStack().stackSize;
                    int timerSeconds = Integer.parseInt(StringUtils.stripControlCodes(invSlots.get(49).getStack().getDisplayName()).replaceAll("[^\\d]", ""));
                    if (round != lastChronomatronRound && timerSeconds == round + 2) {
                        lastChronomatronRound = round;
                        for (int i = 10; i <= 43; i++) {
                            ItemStack stack = invSlots.get(i).getStack();
                            if (stack == null) continue;
                            if (stack.getItem() == Item.getItemFromBlock(Blocks.stained_hardened_clay)) {
                                chronomatronPattern.add(stack.getDisplayName());
                                break;
                            }
                        }
                    }
                }
            }
            if (chronomatronMouseClicks < chronomatronPattern.size() && player.inventory.getItemStack() == null) {
                for (int i = 10; i <= 43; i++) {
                    ItemStack glass = (invSlots.get(i)).getStack();
                    if (glass != null && player.inventory.getItemStack() == null && tickAmount % 5 == 0) {
                        Slot glassSlot = invSlots.get(i);
                        if (glass.getDisplayName().equals(chronomatronPattern.get(chronomatronMouseClicks))) {
                            Utils.drawOnSlot(chestSize, glassSlot.xDisplayPosition, glassSlot.yDisplayPosition, -448725184);
                            mc.playerController.windowClick(mc.thePlayer.openContainer.windowId, glassSlot.slotNumber, 0, 0, mc.thePlayer);
                            chronomatronMouseClicks++;
                            break;
                        }
                    }
                }
            }
        }
        if (ToggleCommand.superpairsToggled && displayName.contains("Superpairs (")) {
            new TextRenderer(mc, String.valueOf(mc.thePlayer.openContainer.windowId), 50, 50, 1.0D);
            HashMap<String, HashSet<Integer>> matches = new HashMap<>();
            for (int i = 0; i < 53; i++) {
                ItemStack itemStack = experimentTableSlots[i];
            }
        }
    }
    }

    @SubscribeEvent
    public void onServerConnect(ClientConnectedToServerEvent event) {
        event.manager.channel().pipeline().addBefore("packet_handler", "danker_packet_handler", new PacketHandler());
        System.out.println("Added packet handler to channel pipeline.");
    }

}
