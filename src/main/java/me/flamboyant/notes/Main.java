package me.flamboyant.notes;

import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.ArrayList;

public final class Main extends JavaPlugin implements CommandExecutor {
    private static final String removeCmd = "-r";
    private NotesData data = new NotesData();

    @Override
    public void onEnable() {
        // Plugin startup logic
        getCommand("txt").setExecutor(this);
    }

    @Override
    public void onDisable() {
        // Plugin shutdown logic
    }

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] args) {
        if (!command.getName().contains("txt")) return false;

        if(commandSender instanceof Player) {
            Player sender = (Player) commandSender;

            if (!data.notes.containsKey(sender.getUniqueId())) {
                data.notes.put(sender.getUniqueId(), new ArrayList<>());
            }

            switch (command.getName()) {
                case "txt" :
                    return doTxtCommand(sender, args);
                case "txt_info" :
                    doTxtInfoCommand(sender);
                    return true;
            }
        }

        return true;
    }

    private boolean doTxtCommand(Player sender, String[] args) {
        if (args.length == 0) {
            int i = 0;
            for (String line : data.notes.get(sender.getUniqueId())) {
                sender.sendMessage(i++ + ". " + line);
            }
        }
        else {
            if (args.length >= 2 && args[0].equals(removeCmd)) {
                try {
                    int lineToDelete = Integer.parseInt(args[1]);
                    ArrayList<String> playerNotes = data.notes.get(sender.getUniqueId());
                    if (playerNotes.size() <= lineToDelete) {
                        printError(sender, "Impossible de supprimer la ligne " + lineToDelete + " car tu n'as pris que " + playerNotes.size() + " notes");
                        return false;
                    }
                    playerNotes.remove(lineToDelete);
                } catch (NumberFormatException e) {
                    printError(sender, "Tu as demandé de supprimer une ligne mais " + args[1] + " n'est pas un numéro de ligne");
                    return false;
                }
            } else {
                String line = "";
                for (String arg : args) {
                    line += arg + " ";
                }
                data.notes.get(sender.getUniqueId()).add(line);
            }
        }

        return true;
    }

    private void doTxtInfoCommand(Player sender) {
        sender.sendMessage(ChatColor.AQUA + "¤ " + ChatColor.RESET + "Utilise /txt suivi de ton texte pour enregistrer une note, " +
                "/txt pour voir tes notes et /txt -r suivi du numéro d'une ligne pour la supprimer. Plugin développé par RaphLeFlamboyant, " +
                "pour accéder à ses modes de jeu tapez son nom sur Patreon !");
    }

    private void printError(Player target, String message) {
        target.sendMessage("" + ChatColor.ITALIC + ChatColor.RED + "¤ " + message);
    }
}
