package io.titan.cache.def;

import io.titan.entity.model.Model;
import io.titan.cache.FileArchive;
import io.titan.cache.graphics.Sprite;
import io.titan.collection.ReferenceCache;
import io.titan.draw.Rasterizer2D;
import io.titan.draw.Rasterizer3D;
import io.titan.io.Buffer;
import io.titan.sign.SignLink;
import io.titan.util.FileUtils;

public final class ItemDefinition {

    public static void init(FileArchive archive) {
        Buffer stream = null;

        item_data = new Buffer(FileUtils.readFile(SignLink.findcachedir() + "obj.dat"));
        stream = new Buffer(FileUtils.readFile(SignLink.findcachedir() + "obj.idx"));

        itemCount = stream.readUShort() + 21;
        streamIndices = new int[itemCount];
        int offset = 2;

        for (int _ctr = 0; _ctr < itemCount; _ctr++) {
            streamIndices[_ctr] = offset;
            offset += stream.readUShort();
        }

        cache = new ItemDefinition[10];

        for (int _ctr = 0; _ctr < 10; _ctr++) {
            cache[_ctr] = new ItemDefinition();
        }
    }

    private void decode(Buffer buffer) {
        do {
            int opCode = buffer.readUnsignedByte();
            if (opCode == 0)
                return;
            if (opCode == 1)
                inventory_model = buffer.readUShort();
            else if (opCode == 2)
                name = buffer.readString();
            else if (opCode == 3)
                description = buffer.readString();
            else if (opCode == 4)
                modelZoom = buffer.readUShort();
            else if (opCode == 5)
                rotationY = buffer.readUShort();
            else if (opCode == 6)
                rotation_x = buffer.readUShort();
            else if (opCode == 7) {
                translate_x = buffer.readUShort();
                if (translate_x > 32767)
                    translate_x -= 0x10000;
            } else if (opCode == 8) {
                translate_yz = buffer.readUShort();
                if (translate_yz > 32767)
                    translate_yz -= 0x10000;
            } else if (opCode == 10)
                buffer.readUShort();
            else if (opCode == 11)
                stackable = true;
            else if (opCode == 12) {
                value = buffer.readInt();
            } else if (opCode == 16)
                isMembers = true;
            else if (opCode == 23) {
                equipped_model_male_1 = buffer.readUShort();
                equipped_model_male_translation_y = buffer.readSignedByte();
            } else if (opCode == 24)
                equipped_model_male_2 = buffer.readUShort();
            else if (opCode == 25) {
                equipped_model_female_1 = buffer.readUShort();
                equipped_model_female_translation_y = buffer.readSignedByte();
            } else if (opCode == 26)
                equipped_model_female_2 = buffer.readUShort();
            else if (opCode >= 30 && opCode < 35) {
                if (groundActions == null)
                    groundActions = new String[5];
                groundActions[opCode - 30] = buffer.readString();
                if (groundActions[opCode - 30].equalsIgnoreCase("hidden"))
                    groundActions[opCode - 30] = null;
            } else if (opCode >= 35 && opCode < 40) {
                if (actions == null)
                    actions = new String[5];
                actions[opCode - 35] = buffer.readString();
            } else if (opCode == 40) {
                int j = buffer.readUnsignedByte();
                modified_model_colors = new int[j];
                original_model_colors = new int[j];
                for (int k = 0; k < j; k++) {
                    original_model_colors[k] = buffer.readUShort();
                    modified_model_colors[k] = buffer.readUShort();
                }
            } else if (opCode == 78)
                equipped_model_male_3 = buffer.readUShort();
            else if (opCode == 79)
                equipped_model_female_3 = buffer.readUShort();
            else if (opCode == 90)
                equipped_model_male_dialogue_1 = buffer.readUShort();
            else if (opCode == 91)
                equipped_model_female_dialogue_1 = buffer.readUShort();
            else if (opCode == 92)
                equipped_model_male_dialogue_2 = buffer.readUShort();
            else if (opCode == 93)
                equipped_model_female_dialogue_2 = buffer.readUShort();
            else if (opCode == 95)
                rotation_z = buffer.readUShort();
            else if (opCode == 97)
                unnoted_item_id = buffer.readUShort();
            else if (opCode == 98)
                noted_item_id = buffer.readUShort();
            else if (opCode >= 100 && opCode < 110) {

                if (stack_variant_id == null) {
                    stack_variant_id = new int[10];
                    stack_variant_size = new int[10];
                }
                stack_variant_id[opCode - 100] = buffer.readUShort();
                stack_variant_size[opCode - 100] = buffer.readUShort();

				/*int length = buffer.readUnsignedByte();
                stack_variant_id = new int [length];
				stack_variant_size = new int[length];
				for (int i2 = 0; i2< length; i2++) {
					stack_variant_id[i2] = buffer.readUShort();
					stack_variant_size[i2] = buffer.readUShort();
				}*/
            } else if (opCode == 110)
                model_scale_x = buffer.readUShort();
            else if (opCode == 111)
                model_scale_y = buffer.readUShort();
            else if (opCode == 112)
                model_scale_z = buffer.readUShort();
            else if (opCode == 113)
                light_intensity = buffer.readSignedByte();
            else if (opCode == 114)
                light_mag = buffer.readSignedByte() * 5;
            else if (opCode == 115)
                team = buffer.readUnsignedByte();
        } while (true);
    }

    public Model getChatEquipModel(int gender) {
        int dialogueModel = equipped_model_male_dialogue_1;
        int dialogueHatModel = equipped_model_male_dialogue_2;
        if (gender == 1) {
            dialogueModel = equipped_model_female_dialogue_1;
            dialogueHatModel = equipped_model_female_dialogue_2;
        }
        if (dialogueModel == -1)
            return null;
        Model dialogueModel_ = Model.getModel(dialogueModel);
        if (dialogueHatModel != -1) {
            Model hatModel_ = Model.getModel(dialogueHatModel);
            Model models[] = {dialogueModel_, hatModel_};
            dialogueModel_ = new Model(2, models);
        }
        if (modified_model_colors != null) {
            for (int i1 = 0; i1 < modified_model_colors.length; i1++)
                dialogueModel_.recolor(modified_model_colors[i1], original_model_colors[i1]);

        }
        return dialogueModel_;
    }

    public boolean isEquippedModelCached(int gender) {
        int primaryModel = equipped_model_male_1;
        int secondaryModel = equipped_model_male_2;
        int emblem = equipped_model_male_3;
        if (gender == 1) {
            primaryModel = equipped_model_female_1;
            secondaryModel = equipped_model_female_2;
            emblem = equipped_model_female_3;
        }
        if (primaryModel == -1)
            return true;
        boolean cached = true;
        if (!Model.isCached(primaryModel))
            cached = false;
        if (secondaryModel != -1 && !Model.isCached(secondaryModel))
            cached = false;
        if (emblem != -1 && !Model.isCached(emblem))
            cached = false;
        return cached;
    }

    public Model getEquippedModel(int gender) {
        int primaryModel = equipped_model_male_1;
        int secondaryModel = equipped_model_male_2;
        int emblem = equipped_model_male_3;
        if (gender == 1) {
            primaryModel = equipped_model_female_1;
            secondaryModel = equipped_model_female_2;
            emblem = equipped_model_female_3;
        }
        if (primaryModel == -1)
            return null;
        Model primaryModel_ = Model.getModel(primaryModel);
        if (secondaryModel != -1)
            if (emblem != -1) {
                Model secondaryModel_ = Model.getModel(secondaryModel);
                Model emblemModel = Model.getModel(emblem);
                Model models[] = {primaryModel_, secondaryModel_, emblemModel};
                primaryModel_ = new Model(3, models);
            } else {
                Model model_2 = Model.getModel(secondaryModel);
                Model models[] = {primaryModel_, model_2};
                primaryModel_ = new Model(2, models);
            }
        if (gender == 0 && equipped_model_male_translation_y != 0)
            primaryModel_.translate(0, equipped_model_male_translation_y, 0);
        if (gender == 1 && equipped_model_female_translation_y != 0)
            primaryModel_.translate(0, equipped_model_female_translation_y, 0);
        if (modified_model_colors != null) {
            for (int i1 = 0; i1 < modified_model_colors.length; i1++)
                primaryModel_.recolor(modified_model_colors[i1], original_model_colors[i1]);

        }
        return primaryModel_;
    }

    private void setDefaults() {
        inventory_model = 0;
        name = "Dwarf Remains";
        description = null;
        modified_model_colors = null;
        original_model_colors = null;
        modelZoom = 2000;
        rotationY = 0;
        rotation_x = 0;
        rotation_z = 0;
        translate_x = 0;
        translate_yz = 0;
        stackable = false;
        value = 1;
        members = false;
        groundActions = null;
        actions = null;
        equipped_model_male_1 = -1;
        equipped_model_male_2 = -1;
        equipped_model_male_translation_y = 0;
        equipped_model_female_1 = -1;
        equipped_model_female_2 = -1;
        equipped_model_female_translation_y = 0;
        equipped_model_male_3 = -1;
        equipped_model_female_3 = -1;
        equipped_model_male_dialogue_1 = -1;
        equipped_model_male_dialogue_2 = -1;
        equipped_model_female_dialogue_1 = -1;
        equipped_model_female_dialogue_2 = -1;
        stack_variant_id = null;
        stack_variant_size = null;
        unnoted_item_id = -1;
        noted_item_id = -1;
        model_scale_x = 128;
        model_scale_y = 128;
        model_scale_z = 128;
        light_intensity = 0;
        light_mag = 0;
        team = 0;
    }

    public static ItemDefinition lookup(int itemId) {
        for (int count = 0; count < 10; count++) {
            if (cache[count].id == itemId) {
                return cache[count];
            }
        }

        cacheIndex = (cacheIndex + 1) % 10;
        ItemDefinition itemDef = cache[cacheIndex];
        item_data.currentPosition = streamIndices[itemId];
        itemDef.id = itemId;
        itemDef.setDefaults();
        itemDef.decode(item_data);

        if (itemDef.noted_item_id != -1) {
            itemDef.toNote();
        }

        return itemDef;
    }

    private void toNote() {
        ItemDefinition itemDef = lookup(noted_item_id);
        inventory_model = itemDef.inventory_model;
        modelZoom = itemDef.modelZoom;
        rotationY = itemDef.rotationY;
        rotation_x = itemDef.rotation_x;

        rotation_z = itemDef.rotation_z;
        translate_x = itemDef.translate_x;
        translate_yz = itemDef.translate_yz;
        modified_model_colors = itemDef.modified_model_colors;
        original_model_colors = itemDef.original_model_colors;
        ItemDefinition itemDef_1 = lookup(unnoted_item_id);
        name = itemDef_1.name;
        members = itemDef_1.members;
        value = itemDef_1.value;
        String s = "a";
        char c = itemDef_1.name.charAt(0);
        if (c == 'A' || c == 'E' || c == 'I' || c == 'O' || c == 'U')
            s = "an";
        description = ("Swap this note at any bank for " + s + " " + itemDef_1.name + ".");
        stackable = true;
    }

    public static Sprite getSprite(int itemId, int stackSize, int outlineColor) {
        if (outlineColor == 0) {
            Sprite sprite = (Sprite) sprites.get(itemId);
            if (sprite != null && sprite.getMaxHeight() != stackSize && sprite.getMaxHeight() != -1) {

                sprite.unlink();
                sprite = null;
            }
            if (sprite != null)
                return sprite;
        }
        ItemDefinition itemDef = lookup(itemId);
        if (itemDef.stack_variant_id == null)
            stackSize = -1;
        if (stackSize > 1) {
            int stack_item_id = -1;
            for (int j1 = 0; j1 < 10; j1++)
                if (stackSize >= itemDef.stack_variant_size[j1] && itemDef.stack_variant_size[j1] != 0)
                    stack_item_id = itemDef.stack_variant_id[j1];

            if (stack_item_id != -1)
                itemDef = lookup(stack_item_id);
        }
        Model model = itemDef.getModel(1);
        if (model == null)
            return null;
        Sprite sprite = null;
        if (itemDef.noted_item_id != -1) {
            sprite = getSprite(itemDef.unnoted_item_id, 10, -1);
            if (sprite == null)
                return null;
        }
        Sprite enabledSprite = new Sprite(32, 32);
        int centerX = Rasterizer3D.originViewX;
        int centerY = Rasterizer3D.originViewY;
        int lineOffsets[] = Rasterizer3D.scanOffsets;
        int pixels[] = Rasterizer2D.pixels;
        float depthBuffer[] = Rasterizer2D.depthBuffer;
        int width = Rasterizer2D.width;
        int height = Rasterizer2D.height;
        int vp_left = Rasterizer2D.leftX;
        int vp_right = Rasterizer2D.bottomX;
        int vp_top = Rasterizer2D.topY;
        int vp_bottom = Rasterizer2D.bottomY;
        Rasterizer3D.aBoolean1464 = false;
        Rasterizer2D.initDrawingArea(32, 32, enabledSprite.getMyPixels(), new float[32 * 32]);
        Rasterizer2D.drawBox(0, 0, 32, 32, 0);
        Rasterizer3D.useViewport();
        int k3 = itemDef.modelZoom;
        if (outlineColor == -1)
            k3 = (int) ((double) k3 * 1.5D);
        if (outlineColor > 0)
            k3 = (int) ((double) k3 * 1.04D);
        int l3 = Rasterizer3D.anIntArray1470[itemDef.rotationY] * k3 >> 16;
        int i4 = Rasterizer3D.COSINE[itemDef.rotationY] * k3 >> 16;
        model.method482(itemDef.rotation_x, itemDef.rotation_z, itemDef.rotationY, itemDef.translate_x,
                l3 + model.modelBaseY / 2 + itemDef.translate_yz, i4 + itemDef.translate_yz);
        for (int i5 = 31; i5 >= 0; i5--) {
            for (int j4 = 31; j4 >= 0; j4--)
                if (enabledSprite.getMyPixels()[i5 + j4 * 32] == 0)
                    if (i5 > 0 && enabledSprite.getMyPixels()[(i5 - 1) + j4 * 32] > 1)
                        enabledSprite.getMyPixels()[i5 + j4 * 32] = 1;
                    else if (j4 > 0 && enabledSprite.getMyPixels()[i5 + (j4 - 1) * 32] > 1)
                        enabledSprite.getMyPixels()[i5 + j4 * 32] = 1;
                    else if (i5 < 31 && enabledSprite.getMyPixels()[i5 + 1 + j4 * 32] > 1)
                        enabledSprite.getMyPixels()[i5 + j4 * 32] = 1;
                    else if (j4 < 31 && enabledSprite.getMyPixels()[i5 + (j4 + 1) * 32] > 1)
                        enabledSprite.getMyPixels()[i5 + j4 * 32] = 1;

        }

        if (outlineColor > 0) {
            for (int j5 = 31; j5 >= 0; j5--) {
                for (int k4 = 31; k4 >= 0; k4--)
                    if (enabledSprite.getMyPixels()[j5 + k4 * 32] == 0)
                        if (j5 > 0 && enabledSprite.getMyPixels()[(j5 - 1) + k4 * 32] == 1)
                            enabledSprite.getMyPixels()[j5 + k4 * 32] = outlineColor;
                        else if (k4 > 0 && enabledSprite.getMyPixels()[j5 + (k4 - 1) * 32] == 1)
                            enabledSprite.getMyPixels()[j5 + k4 * 32] = outlineColor;
                        else if (j5 < 31 && enabledSprite.getMyPixels()[j5 + 1 + k4 * 32] == 1)
                            enabledSprite.getMyPixels()[j5 + k4 * 32] = outlineColor;
                        else if (k4 < 31 && enabledSprite.getMyPixels()[j5 + (k4 + 1) * 32] == 1)
                            enabledSprite.getMyPixels()[j5 + k4 * 32] = outlineColor;

            }

        } else if (outlineColor == 0) {
            for (int k5 = 31; k5 >= 0; k5--) {
                for (int l4 = 31; l4 >= 0; l4--)
                    if (enabledSprite.getMyPixels()[k5 + l4 * 32] == 0 && k5 > 0 && l4 > 0
                            && enabledSprite.getMyPixels()[(k5 - 1) + (l4 - 1) * 32] > 0)
                        enabledSprite.getMyPixels()[k5 + l4 * 32] = 0x302020;

            }

        }
        if (itemDef.noted_item_id != -1) {
            int old_w = sprite.getMaxWidth();
            int old_h = sprite.getMaxHeight();
            sprite.setMaxWidth(32);
            sprite.setMaxHeight(32);
            sprite.drawSprite(0, 0);
            sprite.setMaxWidth(old_w);
            sprite.setMaxHeight(old_h);
        }
        if (outlineColor == 0)
            sprites.put(enabledSprite, itemId);
        Rasterizer2D.initDrawingArea(height, width, pixels, depthBuffer);
        Rasterizer2D.setDrawingArea(vp_bottom, vp_left, vp_right, vp_top);
        Rasterizer3D.originViewX = centerX;
        Rasterizer3D.originViewY = centerY;
        Rasterizer3D.scanOffsets = lineOffsets;
        Rasterizer3D.aBoolean1464 = true;
        if (itemDef.stackable)
            enabledSprite.setMaxWidth(33);
        else
            enabledSprite.setMaxWidth(32);
        enabledSprite.setMaxHeight(stackSize);
        return enabledSprite;
    }

    public Model getModel(int stack_size) {
        if (stack_variant_id != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int k = 0; k < 10; k++)
                if (stack_size >= stack_variant_size[k] && stack_variant_size[k] != 0)
                    stack_item_id = stack_variant_id[k];

            if (stack_item_id != -1)
                return lookup(stack_item_id).getModel(1);
        }
        Model model = (Model) models.get(id);
        if (model != null)
            return model;
        model = Model.getModel(inventory_model);
        if (model == null)
            return null;
        if (model_scale_x != 128 || model_scale_y != 128 || model_scale_z != 128)
            model.scale(model_scale_x, model_scale_z, model_scale_y);
        if (modified_model_colors != null) {
            for (int l = 0; l < modified_model_colors.length; l++)
                model.recolor(modified_model_colors[l], original_model_colors[l]);

        }
        model.light(64 + light_intensity, 768 + light_mag, -50, -10, -50, true);
        model.fits_on_single_square = true;
        models.put(model, id);
        return model;
    }

    public Model getUnshadedModel(int stack_size) {
        if (stack_variant_id != null && stack_size > 1) {
            int stack_item_id = -1;
            for (int count = 0; count < 10; count++)
                if (stack_size >= stack_variant_size[count] && stack_variant_size[count] != 0)
                    stack_item_id = stack_variant_id[count];

            if (stack_item_id != -1)
                return lookup(stack_item_id).getUnshadedModel(1);
        }
        Model model = Model.getModel(inventory_model);
        if (model == null)
            return null;
        if (modified_model_colors != null) {
            for (int colorPtr = 0; colorPtr < modified_model_colors.length; colorPtr++)
                model.recolor(modified_model_colors[colorPtr], original_model_colors[colorPtr]);

        }
        return model;
    }

    public static void clear() {
        models = null;
        sprites = null;
        streamIndices = null;
        cache = null;
        item_data = null;
    }

    public boolean isDialogueModelCached(int gender) {
        int model_1 = equipped_model_male_dialogue_1;
        int model_2 = equipped_model_male_dialogue_2;
        if (gender == 1) {
            model_1 = equipped_model_female_dialogue_1;
            model_2 = equipped_model_female_dialogue_2;
        }
        if (model_1 == -1)
            return true;
        boolean cached = true;
        if (!Model.isCached(model_1))
            cached = false;
        if (model_2 != -1 && !Model.isCached(model_2))
            cached = false;
        return cached;
    }

    private ItemDefinition() {
        id = -1;
    }

    private byte equipped_model_female_translation_y;
    public int value;
    public int[] modified_model_colors;
    public int id;
    public static ReferenceCache sprites = new ReferenceCache(100);
    public static ReferenceCache models = new ReferenceCache(50);
    public int[] original_model_colors;
    public boolean members;
    private int equipped_model_female_3;
    private int noted_item_id;
    public int equipped_model_female_2;
    public int equipped_model_male_1;
    private int equipped_model_male_dialogue_2;
    private int model_scale_x;
    public String groundActions[];
    public int translate_x;
    public String name;
    private static ItemDefinition[] cache;
    private int equipped_model_female_dialogue_2;
    public int inventory_model;
    public int equipped_model_male_dialogue_1;
    public boolean stackable;
    public String description;
    public int unnoted_item_id;
    private static int cacheIndex;
    public int modelZoom;
    public static boolean isMembers = true;
    private static Buffer item_data;
    private int light_mag;
    private int equipped_model_male_3;
    public int equipped_model_male_2;
    public String actions[];
    public int rotationY;
    private int model_scale_z;
    private int model_scale_y;
    public int[] stack_variant_id;
    public int translate_yz;//
    private static int[] streamIndices;
    private int light_intensity;
    public int equipped_model_female_dialogue_1;
    public int rotation_x;
    public int equipped_model_female_1;
    public int[] stack_variant_size;
    public int team;
    public static int itemCount;
    public int rotation_z;
    private byte equipped_model_male_translation_y;
}