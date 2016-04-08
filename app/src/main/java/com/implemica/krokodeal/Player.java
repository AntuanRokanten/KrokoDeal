package com.implemica.krokodeal;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author user
 */
public class Player implements Parcelable {

   private String name;

   private boolean isHost;

   private int guesses;

   private int showSuccess;

   private int showFailure;

   public Player(String name) {
      this.name = name;
   }

   public Player(String name, boolean isHost) {
      this.name = name;
      this.isHost = isHost;
   }

   public Player(String name, int guesses, int showSuccess, int showFailure) {
      this.name = name;
      this.guesses = guesses;
      this.showSuccess = showSuccess;
      this.showFailure = showFailure;
   }

   protected Player(Parcel in) {
      name = in.readString();
      isHost = in.readByte() != 0;
   }

   public static final Creator<Player> CREATOR = new Creator<Player>() {
      @Override
      public Player createFromParcel(Parcel in) {
         return new Player(in);
      }

      @Override
      public Player[] newArray(int size) {
         return new Player[size];
      }
   };

   public String getName() {
      return name;
   }

   public void setName(String name) {
      this.name = name;
   }

   public boolean isHost() {
      return isHost;
   }

   public void setIsHost(boolean isHost) {
      this.isHost = isHost;
   }

   public int getGuesses() {
      return guesses;
   }

   public int getShowSuccess() {
      return showSuccess;
   }

   public int getShowFailure() {
      return showFailure;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeString(name);
      dest.writeByte((byte) (isHost ? 1 : 0));
   }
}
