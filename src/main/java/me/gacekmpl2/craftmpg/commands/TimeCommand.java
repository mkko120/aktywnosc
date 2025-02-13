package me.gacekmpl2.craftmpg.commands;

import me.gacekmpl2.craftmpg.GlobalVariables;
import me.gacekmpl2.craftmpg.chatutil.ChatUtil;
import me.gacekmpl2.craftmpg.utils.TimeCountUtils;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

import java.sql.SQLException;

public class TimeCommand implements CommandExecutor {
    public boolean onCommand(@NotNull CommandSender sender, @NotNull Command command, @NotNull String label, String[] args) {
        Player player = (Player)sender;
        switch (args.length) {
            case 0:
                if (player.hasPermission(GlobalVariables.getTime_count_own()))
                    try {
                        long seconds = TimeCountUtils.getPlayerTimeInDataBase(player) + TimeCountUtils.getSecondsOnServer(player);
                        int hours = (int) (seconds / 3600L);
                        int minutes = (int) (seconds % 3600L / 60L);
                        ChatUtil.sendMessage(player, "&b&lTwoj czas: &e" + hours + " h " + minutes + " m ");
                    } catch (SQLException throwables) {
                        ChatUtil.sendMessage(player, "&cNie ma cie w bazie danych! Sprobuj sie przelogowac");
                        throwables.printStackTrace();
                    }
                return true;
            case 1:
                if (args[0].equalsIgnoreCase("resetall")) {
                    if (player.hasPermission(GlobalVariables.getTime_count_reset())) {
                        try {
                            ChatUtil.sendMessage(player, "&4&lWyczyszczono wszystkich uzytkownikow!");
                            TimeCountUtils.resetAllPlayers();
                        } catch (SQLException throwables) {
                            ChatUtil.sendMessage(player, "&cBlad polaczenia z baza danych!");
                            throwables.printStackTrace();
                        }
                    } else {
                        ChatUtil.sendMessage(player, "&cNie masz dostepu do tej komendy!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("top")) {
                    if (player.hasPermission(GlobalVariables.getTime_count_top()))
                        try {
                            TimeCountUtils.showTop(player);
                        } catch (SQLException throwables) {
                            ChatUtil.sendMessage(player, "&cBlad polaczenia!");
                            throwables.printStackTrace();
                        }
                    return true;
                }
                if (args[0].equalsIgnoreCase("showall")) {
                    if (player.hasPermission(GlobalVariables.getTime_count_other()))
                        try {
                            TimeCountUtils.showAllResults(player);
                        } catch (SQLException throwables) {
                            ChatUtil.sendMessage(player, "&cBlad polaczenia!");
                            throwables.printStackTrace();
                        }
                    return true;
                }
                break;
            case 2:
                if (args[0].equalsIgnoreCase("zobacz")) {
                    if (player.hasPermission(GlobalVariables.getTime_count_other())) {
                        long seconds;
                        try {
                            Player target = Bukkit.getPlayer(args[1]);
                            if (target == null) throw new SQLException("Player not found!");
                            seconds = TimeCountUtils.getPlayerTimeInDataBase(target);
                            int hours = (int) (seconds / 3600L);
                            int minutes = (int) (seconds % 3600L / 60L);
                            ChatUtil.sendMessage(player, "&7Czas uzytkownika " + args[1] + ": &e" + hours + " h " + minutes + " m ");
                        } catch (SQLException throwables) {
                            ChatUtil.sendMessage(player, "&cTego gracza nie ma w bazie!");
                            throwables.printStackTrace();
                        }
                    } else {
                        ChatUtil.sendMessage(player, "&cNie masz dostepu do tej komendy!");
                    }
                    return true;
                }
                if (args[0].equalsIgnoreCase("reset")) {
                    if (player.hasPermission(GlobalVariables.getTime_count_reset())) {
                        try {
                            TimeCountUtils.resetPlayerTime(args[1]);
                            ChatUtil.sendMessage(player, "&7Usunieto dane gracza:" + args[1]);
                        } catch (SQLException throwables) {
                            ChatUtil.sendMessage(player, "&cTego gracza nie ma w bazie!");
                            throwables.printStackTrace();
                        }
                    } else {
                        ChatUtil.sendMessage(player, "&cNie masz dostepu do tej komendy!");
                    }
                    return true;
                }
                break;

            default: {
                ChatUtil.sendMessage(player, "&b/" + label + " zobacz <nick>");
                ChatUtil.sendMessage(player, "&b/" + label + " showall");
                ChatUtil.sendMessage(player, "&b/" + label + " reset <nick>");
                ChatUtil.sendMessage(player, "&b/" + label + " resetall");
            }
        }
        return true;
    }
}