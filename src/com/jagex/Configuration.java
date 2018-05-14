package com.jagex;

import java.math.BigInteger;
import java.nio.file.Path;
import java.nio.file.Paths;

public final class Configuration {

    private Configuration() {

    }

    public static final BigInteger RSA_MODULUS = new BigInteger(
            "94904992129904410061849432720048295856082621425118273522925386720620318960919649616773860564226013741030211135158797393273808089000770687087538386210551037271884505217469135237269866084874090369313013016228010726263597258760029391951907049483204438424117908438852851618778702170822555894057960542749301583313");

    public static final BigInteger RSA_EXPONENT = new BigInteger("65537");

    /**
     * Sends client-related debug messages to the client output stream
     */
    public static boolean client_debug = true;

    /**
     * The address of the server that the client will be connecting to
     */
    public static String server_address = "localhost";

    public static final Path CACHE_PATH = Paths.get("./Cache/");

    /**
     * The port of the server that the client will be connecting to
     */
    public static int server_port = 43594;

    public static boolean useJaggrab = false;

    /**
     * Toggles a security feature called RSA to prevent packet sniffers
     */
    public static final boolean ENABLE_RSA = true;

    /**
     * The url that the users will get redirected to after clicking "New User"
     */
    public static final String REGISTER_ACCOUNT = "www.google.com";

    /**
     * A string which indicates the Client's name.
     */
    public static final String CLIENT_NAME = "RuneScape";

    /**
     * Dumps map region images when new regions are loaded.
     */
    public static boolean dumpMapRegions = false;

    /**
     * Displays debug messages on loginscreen and in-game
     */
    public static boolean clientData = false;

    /**
     * Enables the use of music played through the client
     */
    public static boolean enableMusic = true;

    /**
     * Toggles the ability for a player to see roofs in-game
     */
    public static boolean enableRoofs = true;

    /**
     * Used for change worlds button on login screen
     */
    public static boolean worldSwitch = false;

    /**
     * Enables extra frames in-between animations to give the animation a smooth
     * look
     * <p>
     * Use false if there are gfx flickering
     */
    public static boolean enableTweening = true;

    /**
     * Shows the ids of items, objects, and npcs on right click
     */
    public static boolean enableIds = false;

    /**
     * Used to merge all the OS Buddy XP Drops so the counter doesn't get too
     * big if you are training a lot of different skills
     */
    public static boolean xp_merge = true;

    /**
     * Enables fog effects
     * <p>
     * Doesn't render properly after HD textures
     */
    public static boolean enableFog = false;

    /**
     * npcBits can be changed to what your server's bits are set to.
     */
    public static final int npcBits = 13;

    /**
     * Displays health above entities heads
     */
    public static boolean hpAboveHeads = false;

    /**
     * Displays names above entities
     */
    public static boolean namesAboveHeads = false;

    /**
     * Displays OS Buddy orbs on HUD
     */
    public static boolean enableOrbs = true;

    /**
     * Enables/Disables Revision 554 hitmarks
     */
    public static boolean hitmarks554 = false;

    /**
     * Enables/Disables Revision 554 health bar
     */
    public static boolean hpBar554 = false;

    /**
     * Enables the HUD to display 10 X the amount of hitpoints
     */
    public static boolean tenXHp = false;

}
