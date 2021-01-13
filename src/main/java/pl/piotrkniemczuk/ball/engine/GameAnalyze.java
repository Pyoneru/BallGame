package pl.piotrkniemczuk.ball.engine;

import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

public class GameAnalyze {

    private static GameAnalyze instance;

    public static GameAnalyze getInstance(){
        if(instance == null)
            instance = new GameAnalyze();
        return instance;
    }

    private Map<Integer, FpsCounter> fpsCounterMap;
    private Map<Integer, MemoryCounter> memoryCounterMap;
    private Runtime runtime;

    public int minFPS;
    public int avgFPS;
    public int maxFPS;
    public int avg;
    private long maxMemory;
    private long minAllocatedMemory;
    private long avgAllocatedMemory;
    private long maxAllocatedMemory;
    private long minFreeMemory;
    private long avgFreeMemory;
    private long maxFreeMemory;
    private int avgCounter;
    private int lvl;

    public long timeLight;
    public long timeAvgFields;
    public long timeAllFields;
    public long timeAvgBorders;
    public long timeAllBorders;
    public long timeMeter;

    GameAnalyze(){
        fpsCounterMap = new HashMap<>();
        memoryCounterMap = new HashMap<>();
        runtime = Runtime.getRuntime();
        lvl = 1;
        minFPS = 100000;
        avgFPS = 0;
        maxFPS = -1;
        avg = 0;
        maxMemory = runtime.maxMemory();
        minAllocatedMemory = runtime.totalMemory();
        avgAllocatedMemory = runtime.totalMemory();
        maxAllocatedMemory = runtime.totalMemory();
        minFreeMemory = runtime.freeMemory();
        avgFreeMemory = runtime.freeMemory();
        maxFreeMemory = runtime.freeMemory();
        avgCounter = 1;
    }

    @Override
    public String toString() {
        return "GameAnalyze{" +
                "minFPS=" + minFPS +
                ", avgFPS=" + avgFPS/avg +
                ", maxFPS=" + maxFPS +
                '}';
    }

    public int getLvl(){
        return lvl;
    }

    public void nextLvl(){
        fpsCounterMap.put(lvl, new FpsCounter(minFPS, avgFPS, maxFPS, avg));
        memoryCounterMap.put(lvl, new MemoryCounter(maxMemory,
                minAllocatedMemory, avgAllocatedMemory, maxAllocatedMemory,
                minFreeMemory, avgFreeMemory, maxFreeMemory, avgCounter));
        minFPS = 100000;
        avgFPS = 0;
        maxFPS = -1;
        avg = 1;
        lvl++;
    }

    public void update(){
        long allocatedMemory = runtime.totalMemory();
        long freeMemory = runtime.freeMemory();

        avgAllocatedMemory += allocatedMemory;
        avgFreeMemory += freeMemory;
        avgCounter++;

        if(allocatedMemory < minAllocatedMemory) minAllocatedMemory = allocatedMemory;
        else if(allocatedMemory >  maxAllocatedMemory) maxAllocatedMemory = allocatedMemory;

        if(freeMemory < minFreeMemory) minFreeMemory = freeMemory;
        else if(freeMemory > maxFreeMemory) maxFreeMemory = freeMemory;
    }

    public void save(){
        try {
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyyMMdd HHmmss");
            FileWriter writer = new FileWriter(LocalDateTime.now().format(formatter) + ".txt");
            writer.write(runtime.maxMemory()/1024 + "\n");
            for(int i = 1; fpsCounterMap.containsKey(i); i++){
                FpsCounter fc = fpsCounterMap.get(i);
                MemoryCounter mc = memoryCounterMap.get(i);
                writer.write("--" + i + "--"+ "\n");
                writer.write(fc.minFPS + "\n");
                writer.write(fc.avgFPS/fc.avg + "\n");
                writer.write(fc.maxFPS + "\n");
                writer.write(mc.minAllocatedMemory/1024 + "\n");
                writer.write(mc.avgAllocatedMemory/1024/mc.avgCounter + "\n");
                writer.write(mc.maxAllocatedMemory/1024 + "\n");
                writer.write(mc.minFreeMemory/1024 + "\n");
                writer.write(mc.avgFreeMemory/1024/mc.avgCounter + "\n");
                writer.write(mc.maxFreeMemory/1024 + "\n");
                writer.write("======================"+ "\n");
            }
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public FpsCounter getFpsCounter(int key){
        return fpsCounterMap.get(key);
    }

    public static class FpsCounter{
        public int minFPS;
        public int avgFPS;
        public int maxFPS;
        public int avg;

        public FpsCounter(int minFPS, int avgFPS, int maxFPS, int avg) {
            this.minFPS = minFPS;
            this.avgFPS = avgFPS;
            this.maxFPS = maxFPS;
            this.avg = avg;
        }
    }

    public static class MemoryCounter{
        private long maxMemory;
        private long minAllocatedMemory;
        private long avgAllocatedMemory;
        private long maxAllocatedMemory;
        private long minFreeMemory;
        private long avgFreeMemory;
        private long maxFreeMemory;
        private int avgCounter;

        public MemoryCounter(long maxMemory,
                             long minAllocatedMemory, long avgAllocatedMemory,
                             long maxAllocatedMemory,
                             long minFreeMemory, long avgFreeMemory, long maxFreeMemory,
                             int avgCounter) {
            this.maxMemory = maxMemory;
            this.minAllocatedMemory = minAllocatedMemory;
            this.avgAllocatedMemory = avgAllocatedMemory;
            this.maxAllocatedMemory = maxAllocatedMemory;
            this.minFreeMemory = minFreeMemory;
            this.avgFreeMemory = avgFreeMemory;
            this.maxFreeMemory = maxFreeMemory;
            this.avgCounter = avgCounter;
        }

        public long getMaxMemory() {
            return maxMemory;
        }

        public long getMinAllocatedMemory() {
            return minAllocatedMemory;
        }

        public long getAvgAllocatedMemory() {
            return avgAllocatedMemory;
        }

        public long getMaxAllocatedMemory() {
            return maxAllocatedMemory;
        }

        public long getMinFreeMemory() {
            return minFreeMemory;
        }

        public long getAvgFreeMemory() {
            return avgFreeMemory;
        }

        public long getMaxFreeMemory() {
            return maxFreeMemory;
        }

        public int avgCounter() {
            return avgCounter;
        }
    }
}
