package com.implemica.krokodeal;

/**
 * @author ant
 */
public interface ChooseWinnerListener {

   /**
    * Invoked when player chooses player who guessed the word on {@link com.implemica.krokodeal.ui.dialogs.ChoosePlayerDialog} dialog
    *
    * @param winner player who guessed the word
    */
   void onWinnerChosen(Player winner);

}
