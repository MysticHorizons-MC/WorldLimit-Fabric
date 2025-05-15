package com.mystichorizonsmc.worldlimit.util;

import net.minecraft.text.Text;

public class MessageUtil {
    public static Text colorize(String input) {
        return Text.literal(input.replace("&", "§"));
    }
}
