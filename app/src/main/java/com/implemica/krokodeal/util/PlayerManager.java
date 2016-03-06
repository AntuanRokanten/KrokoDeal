package com.implemica.krokodeal.util;

import com.implemica.krokodeal.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @author ant
 */
public abstract class PlayerManager {

   private static List<Player> players = new ArrayList<>();

   public void addPlayer(Player newPlayer) {
      players.add(newPlayer);
   }



}
