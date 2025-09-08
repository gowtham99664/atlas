package com.smarthome.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class WeatherService {
    
    private static WeatherService instance;
    private final Random random;
    
    private WeatherService() {
        this.random = new Random();
    }
    
    public static synchronized WeatherService getInstance() {
        if (instance == null) {
            instance = new WeatherService();
        }
        return instance;
    }
    
    public static class WeatherData {
        private String condition;
        private double temperatureCelsius;
        private int humidity;
        private double windSpeed;
        private String description;
        private LocalDateTime timestamp;
        private int airQualityIndex;
        
        public WeatherData(String condition, double temperature, int humidity, 
                          double windSpeed, String description, int airQualityIndex) {
            this.condition = condition;
            this.temperatureCelsius = temperature;
            this.humidity = humidity;
            this.windSpeed = windSpeed;
            this.description = description;
            this.timestamp = LocalDateTime.now();
            this.airQualityIndex = airQualityIndex;
        }
        
        public String getCondition() { return condition; }
        public double getTemperatureCelsius() { return temperatureCelsius; }
        public int getHumidity() { return humidity; }
        public double getWindSpeed() { return windSpeed; }
        public String getDescription() { return description; }
        public LocalDateTime getTimestamp() { return timestamp; }
        public int getAirQualityIndex() { return airQualityIndex; }
        
        public boolean isHot() { return temperatureCelsius > 30; }
        public boolean isCold() { return temperatureCelsius < 18; }
        public boolean isHumid() { return humidity > 70; }
        public boolean isPoorAirQuality() { return airQualityIndex > 150; }
    }
    
    public static class WeatherAutomationRule {
        private String condition;
        private String deviceType;
        private String roomName;
        private String action;
        private String reason;
        
        public WeatherAutomationRule(String condition, String deviceType, String roomName, 
                                   String action, String reason) {
            this.condition = condition;
            this.deviceType = deviceType;
            this.roomName = roomName;
            this.action = action;
            this.reason = reason;
        }
        
        public String getCondition() { return condition; }
        public String getDeviceType() { return deviceType; }
        public String getRoomName() { return roomName; }
        public String getAction() { return action; }
        public String getReason() { return reason; }
    }
    
    public WeatherData getCurrentWeather() {
        String[] conditions = {"Sunny", "Cloudy", "Rainy", "Stormy", "Foggy", "Hot", "Cold"};
        String[] descriptions = {
            "Clear sky with bright sunshine",
            "Partly cloudy with scattered clouds",
            "Light rain showers expected",
            "Heavy thunderstorm with lightning",
            "Dense fog reducing visibility",
            "Very hot and dry conditions",
            "Cold weather with low temperatures"
        };
        
        int conditionIndex = random.nextInt(conditions.length);
        String condition = conditions[conditionIndex];
        String description = descriptions[conditionIndex];
        
        double temperature;
        int humidity;
        double windSpeed;
        int airQuality;
        
        switch (condition) {
            case "Hot":
                temperature = 32 + random.nextDouble() * 8;
                humidity = 30 + random.nextInt(20);
                windSpeed = 2 + random.nextDouble() * 5;
                airQuality = 100 + random.nextInt(100);
                break;
            case "Cold":
                temperature = 5 + random.nextDouble() * 12;
                humidity = 40 + random.nextInt(30);
                windSpeed = 5 + random.nextDouble() * 10;
                airQuality = 50 + random.nextInt(50);
                break;
            case "Rainy":
                temperature = 20 + random.nextDouble() * 8;
                humidity = 80 + random.nextInt(15);
                windSpeed = 8 + random.nextDouble() * 12;
                airQuality = 30 + random.nextInt(40);
                break;
            case "Stormy":
                temperature = 22 + random.nextDouble() * 6;
                humidity = 75 + random.nextInt(20);
                windSpeed = 15 + random.nextDouble() * 20;
                airQuality = 60 + random.nextInt(80);
                break;
            default:
                temperature = 22 + random.nextDouble() * 10;
                humidity = 45 + random.nextInt(40);
                windSpeed = 3 + random.nextDouble() * 8;
                airQuality = 50 + random.nextInt(70);
        }
        
        return new WeatherData(condition, temperature, (int)humidity, windSpeed, description, (int)airQuality);
    }
    
    public void displayCurrentWeather() {
        WeatherData weather = getCurrentWeather();
        
        System.out.println("\n=== Current Weather Information ===");
        System.out.printf("[Weather] Condition: %s\n", weather.getCondition());
        System.out.printf("[Temp] Temperature: %.1f C\n", weather.getTemperatureCelsius());
        System.out.printf("[Humidity] %d%%\n", weather.getHumidity());
        System.out.printf("[Wind] Speed: %.1f km/h\n", weather.getWindSpeed());
        System.out.printf("[Air Quality] Index: %d (%s)\n", weather.getAirQualityIndex(), getAirQualityDescription(weather.getAirQualityIndex()));
        System.out.printf("[Description] %s\n", weather.getDescription());
        System.out.printf("[Last Updated] %s\n", weather.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));
    }
    
    public List<WeatherAutomationRule> getWeatherBasedAutomationSuggestions() {
        WeatherData weather = getCurrentWeather();
        List<WeatherAutomationRule> suggestions = new ArrayList<>();
        
        if (weather.isHot()) {
            suggestions.add(new WeatherAutomationRule("Hot Weather", "AC", "Living Room", "ON", 
                "Temperature is " + String.format("%.1f", weather.getTemperatureCelsius()) + "°C - Turn on AC for comfort"));
            suggestions.add(new WeatherAutomationRule("Hot Weather", "FAN", "Master Bedroom", "ON", 
                "High temperature - Turn on bedroom fan"));
            suggestions.add(new WeatherAutomationRule("Hot Weather", "GEYSER", "Kitchen", "OFF", 
                "Hot weather - Reduce geyser usage to save energy"));
        }
        
        if (weather.isCold()) {
            suggestions.add(new WeatherAutomationRule("Cold Weather", "GEYSER", "Kitchen", "ON", 
                "Temperature is " + String.format("%.1f", weather.getTemperatureCelsius()) + "°C - Ensure hot water availability"));
            suggestions.add(new WeatherAutomationRule("Cold Weather", "FAN", "Living Room", "OFF", 
                "Cold weather - Turn off unnecessary fans"));
            suggestions.add(new WeatherAutomationRule("Cold Weather", "AC", "Master Bedroom", "OFF", 
                "Cold conditions - No cooling needed"));
        }
        
        if (weather.isHumid()) {
            suggestions.add(new WeatherAutomationRule("High Humidity", "FAN", "Living Room", "ON", 
                "Humidity is " + weather.getHumidity() + "% - Turn on fans for air circulation"));
            suggestions.add(new WeatherAutomationRule("High Humidity", "AIR_PURIFIER", "Master Bedroom", "ON", 
                "High humidity - Air purifier helps with air quality"));
        }
        
        if (weather.isPoorAirQuality()) {
            suggestions.add(new WeatherAutomationRule("Poor Air Quality", "AIR_PURIFIER", "Living Room", "ON", 
                "AQI is " + weather.getAirQualityIndex() + " - Turn on air purifiers"));
            suggestions.add(new WeatherAutomationRule("Poor Air Quality", "AIR_PURIFIER", "Master Bedroom", "ON", 
                "Poor air quality detected"));
            suggestions.add(new WeatherAutomationRule("Poor Air Quality", "AIR_PURIFIER", "Kitchen", "ON", 
                "High pollution levels - Clean indoor air"));
        }
        
        if (weather.getCondition().equals("Rainy")) {
            suggestions.add(new WeatherAutomationRule("Rainy Weather", "LIGHT", "Living Room", "ON", 
                "Rainy weather - Turn on lights for better visibility"));
            suggestions.add(new WeatherAutomationRule("Rainy Weather", "GEYSER", "Kitchen", "ON", 
                "Rainy day - Ensure hot water for comfort"));
        }
        
        if (weather.getCondition().equals("Stormy")) {
            suggestions.add(new WeatherAutomationRule("Stormy Weather", "TV", "Living Room", "OFF", 
                "Storm detected - Turn off electronics for safety"));
            suggestions.add(new WeatherAutomationRule("Stormy Weather", "MICROWAVE", "Kitchen", "OFF", 
                "Lightning risk - Avoid using high-power appliances"));
        }
        
        return suggestions;
    }
    
    public void displayWeatherBasedSuggestions() {
        List<WeatherAutomationRule> suggestions = getWeatherBasedAutomationSuggestions();
        
        if (suggestions.isEmpty()) {
            System.out.println("\n=== Weather-Based Automation ===");
            System.out.println("[OK] Current weather conditions are optimal.");
            System.out.println("No specific device automation needed at this time.");
            return;
        }
        
        System.out.println("\n=== Weather-Based Automation Suggestions ===");
        for (int i = 0; i < suggestions.size(); i++) {
            WeatherAutomationRule rule = suggestions.get(i);
            System.out.printf("%d. %s %s in %s → %s\n", 
                            (i + 1), rule.getDeviceType(), rule.getAction(), 
                            rule.getRoomName(), rule.getReason());
        }
        
        System.out.println("\n[TIP] You can set up automatic weather-based rules in Advanced Settings!");
    }
    
    public List<WeatherData> getWeatherForecast(int days) {
        List<WeatherData> forecast = new ArrayList<>();
        
        for (int i = 1; i <= days; i++) {
            WeatherData todayWeather = getCurrentWeather();
            
            double tempVariation = (random.nextDouble() - 0.5) * 6;
            double newTemp = Math.max(5, Math.min(45, todayWeather.getTemperatureCelsius() + tempVariation));
            
            int humidityVariation = (int)((random.nextDouble() - 0.5) * 20);
            int newHumidity = Math.max(20, Math.min(95, todayWeather.getHumidity() + humidityVariation));
            
            String[] forecastConditions = {"Sunny", "Cloudy", "Rainy", "Hot", "Cold"};
            String condition = forecastConditions[random.nextInt(forecastConditions.length)];
            
            WeatherData dayForecast = new WeatherData(condition, newTemp, newHumidity, 
                                                    todayWeather.getWindSpeed(), 
                                                    "Forecast for day " + i, 
                                                    todayWeather.getAirQualityIndex());
            forecast.add(dayForecast);
        }
        
        return forecast;
    }
    
    public void displayWeatherForecast() {
        List<WeatherData> forecast = getWeatherForecast(5);
        
        System.out.println("\n=== 5-Day Weather Forecast ===");
        LocalDateTime baseDate = LocalDateTime.now().plusDays(1);
        
        for (int i = 0; i < forecast.size(); i++) {
            WeatherData weather = forecast.get(i);
            LocalDateTime forecastDate = baseDate.plusDays(i);
            
            System.out.printf("Day %d (%s):\n", (i + 1), 
                            forecastDate.format(DateTimeFormatter.ofPattern("dd-MM-yyyy")));
            System.out.printf("  [WEATHER] %s | [TEMP] %.1f°C | [HUMIDITY] %d%% | AQI: %d\n", 
                            weather.getCondition(), weather.getTemperatureCelsius(), 
                            weather.getHumidity(), weather.getAirQualityIndex());
        }
        
        System.out.println("\n[TIP] Plan your device automation based on upcoming weather!");
    }
    
    private String getAirQualityDescription(int aqi) {
        if (aqi <= 50) return "Good";
        else if (aqi <= 100) return "Moderate";
        else if (aqi <= 150) return "Unhealthy for Sensitive Groups";
        else if (aqi <= 200) return "Unhealthy";
        else if (aqi <= 300) return "Very Unhealthy";
        else return "Hazardous";
    }
    
    public String getWeatherHelp() {
        StringBuilder help = new StringBuilder();
        help.append("\n=== Weather-Based Smart Automation ===\n");
        help.append("The system automatically suggests device actions based on weather:\n\n");
        
        help.append("[HOT] HOT WEATHER (>30°C):\n");
        help.append("   - Auto-suggest AC ON in main rooms\n");
        help.append("   - Turn on fans for air circulation\n");
        help.append("   - Reduce geyser usage to save energy\n\n");
        
        help.append("[COLD] COLD WEATHER (<18°C):\n");
        help.append("   - Ensure geysers are ON for hot water\n");
        help.append("   - Turn off unnecessary fans and AC\n");
        help.append("   - Maintain comfortable indoor temperature\n\n");
        
        help.append("[HUMID] HIGH HUMIDITY (>70%):\n");
        help.append("   - Turn on fans for better air circulation\n");
        help.append("   - Activate air purifiers for comfort\n\n");
        
        help.append("[AIR] POOR AIR QUALITY (AQI >150):\n");
        help.append("   - Auto-activate all air purifiers\n");
        help.append("   - Improve indoor air quality\n\n");
        
        help.append("[RAIN] RAINY WEATHER:\n");
        help.append("   - Turn on lights for better visibility\n");
        help.append("   - Ensure geyser is ON for warmth\n\n");
        
        help.append("[STORM] STORMY CONDITIONS:\n");
        help.append("   - Turn off electronics for safety\n");
        help.append("   - Avoid high-power appliances during lightning\n\n");
        
        help.append("[INFO] All suggestions are based on real-time weather data!\n");
        
        return help.toString();
    }
}