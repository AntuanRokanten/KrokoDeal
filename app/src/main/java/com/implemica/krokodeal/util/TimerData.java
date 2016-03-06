package com.implemica.krokodeal.util;

import android.os.Parcel;
import android.os.Parcelable;

/**
 * @author ant
 */
public class TimerData implements Parcelable{

   private int minutes;
   private int seconds;

   public TimerData(int minutes, int seconds) {
      this.minutes = minutes;
      this.seconds = seconds;
   }

   protected TimerData(Parcel in) {
      minutes = in.readInt();
      seconds = in.readInt();
   }

   public static final Creator<TimerData> CREATOR = new Creator<TimerData>() {
      @Override
      public TimerData createFromParcel(Parcel in) {
         return new TimerData(in);
      }

      @Override
      public TimerData[] newArray(int size) {
         return new TimerData[size];
      }
   };

   public int getMinutes() {
      return minutes;
   }

   public int getSeconds() {
      return seconds;
   }

   @Override
   public int describeContents() {
      return 0;
   }

   @Override
   public void writeToParcel(Parcel dest, int flags) {
      dest.writeInt(minutes);
      dest.writeInt(seconds);
   }
}
