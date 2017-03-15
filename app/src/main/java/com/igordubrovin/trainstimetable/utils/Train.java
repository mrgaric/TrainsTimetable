package com.igordubrovin.trainstimetable.utils;

/**
 * Created by Игорь on 15.03.2017.
 */

public class Train {
    private final String timeDeparture;
    private final String timeArrival;
    private final String specialTrain;
    private final String stations;
    private final String travelTime;
    private final String price;
    private final String timeBeforeDeparture;

    private Train(final String timeDeparture,
                  final String timeArrival ,
                  final String specialTrain,
                  final String stations,
                  final String travelTime,
                  final String price,
                  final String timeBeforeDeparture){
        this.timeDeparture = timeDeparture;
        this.timeArrival = timeArrival;
        this.specialTrain = specialTrain;
        this.stations = stations;
        this.travelTime = travelTime;
        this.price = price;
        this.timeBeforeDeparture = timeBeforeDeparture;
    }

    public static TrainBuilder getTrainBuilder(){
        return new TrainBuilder();
    }

    public String getTimeDeparture(){
        return timeDeparture;
    }

    public String getTimeArrival(){
        return timeArrival;
    }

    public String getSpecialTrain(){
        return specialTrain;
    }

    public String getStations(){
        return stations;
    }

    public String getTravelTime(){
        return travelTime;
    }

    public String getPrice(){
        return price;
    }

    public String getTimeBeforeDeparture(){
        return timeBeforeDeparture;
    }

    public static class TrainBuilder{
        private String timeDeparture;
        private String timeArrival;
        private String specialTrain;
        private String stations;
        private String travelTime;
        private String price;
        private String timeBeforeDeparture;

        public TrainBuilder setTimeDeparture(String timeDeparture){
            this.timeDeparture = timeDeparture;
            return this;
        }

        public TrainBuilder setTimeArrival(String timeArrival){
            this.timeArrival = timeArrival;
            return this;
        }

        public TrainBuilder setSpecialTrain(String specialTrain){
            this.specialTrain = specialTrain;
            return this;
        }

        public TrainBuilder setStations(String stations){
            this.stations = stations;
            return this;
        }

        public TrainBuilder setTravelTime(String travelTime){
            this.travelTime = travelTime;
            return this;
        }

        public TrainBuilder setPrice(String price){
            this.price = price;
            /*for (int i = 0; i <5; i++)
            {
                Log.d("myLog", String.valueOf(i));
                try {
                    TimeUnit.SECONDS.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
            Log.d("myLog", this.price);*/
            return this;

        }

        public TrainBuilder setTimeBeforeDeparture(String timeBeforeDeparture){
            this.timeBeforeDeparture = timeBeforeDeparture;
            return this;
        }

        public Train createTrain(){
            return new Train(
                    timeDeparture,
                    timeArrival,
                    specialTrain,
                    stations,
                    travelTime,
                    price,
                    timeBeforeDeparture);
        }
    }
}
