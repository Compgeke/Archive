package kenta.game.logic;

import org.lwjgl.Sys;

public class Timer {

   private long startTime = 0;
   private long stopTime = 0;
   private long totalPauseTime = 0, timeElapsedAtPause = 0, pauseStart = 0;
   private boolean running = false, paused = false;

   public void start() {
      this.startTime = Sys.getTime();
      this.running = true;
   }

   public void stop() {
      this.stopTime = Sys.getTime();
      this.running = false;
   }
   
   public void pause(){
       timeElapsedAtPause = getElapsedTime();
       pauseStart = Sys.getTime();
       paused = true;
   }
   
   public void resume(){
       totalPauseTime += Sys.getTime() - pauseStart;
       pauseStart = 0;
       paused = false;
   }

   // elaspsed time in milliseconds
   public long getElapsedTime() {
      long elapsed;
      if(paused){
          elapsed = timeElapsedAtPause;
      }else if (running) {
         elapsed = (Sys.getTime() - startTime - totalPauseTime);
      } else {
         elapsed = (stopTime - startTime - totalPauseTime);
      }
      return elapsed;
   }

   // elaspsed time in seconds
   public long getElapsedTimeSecs() {
      long elapsed;
      if(paused){
          elapsed = timeElapsedAtPause / 1000;
      }else if (running) {
         elapsed = ((Sys.getTime() - startTime - totalPauseTime) / 1000);
      } else {
         elapsed = ((stopTime - startTime - totalPauseTime) / 1000);
      }
      return elapsed;
   }
   
   //elpsed time in MM:SS format as String
   public String getRemainingTimeMMSS(int totalTime){
       long time = totalTime - getElapsedTimeSecs();
       long minutes = time / 60;
       long seconds = time % 60;
       return ((minutes < 10 ? "0" : "") + minutes
               + ":" + (seconds< 10 ? "0" : "") + seconds );
   }

   public void reset() {
      stop();
      running = false;
      paused = false;
      startTime = 0;
      stopTime = 0;
      totalPauseTime = 0;
      timeElapsedAtPause = 0;
      pauseStart = 0;
   }

   public boolean isRunning() {
      return running;
   }
   
   public boolean isPaused(){
       return paused;
   }
   // sample usage
   /*
    * public static void main(String[] args) { StopWatch s = new StopWatch();
    * s.start(); //code you want to time goes here s.stop();
    * System.out.println("elapsed time in milliseconds: " +
    * s.getElapsedTime()); }
    */
}