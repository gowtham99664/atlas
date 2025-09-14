package com.smarthome.service;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

public class WeatherService {

    private static WeatherService instance;
    private final Random random;
    private WeatherData currentUserWeather;
    private final Scanner scanner;
    
    private WeatherService() {
        this.random = new Random();
        this.scanner = new Scanner(System.in);
        this.currentUserWeather = null;
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
    
    public WeatherData collectWeatherFromUser() {
        System.out.println("\n=== Current Weather Input ===");
        System.out.println("Please provide current weather information for better automation suggestions:");
        System.out.println();

        // Get weather condition
        String condition = getWeatherConditionFromUser();

        // Get temperature
        double temperature = getTemperatureFromUser();

        // Get humidity
        int humidity = getHumidityFromUser();

        // Get wind speed
        double windSpeed = getWindSpeedFromUser();

        // Get air quality
        int airQuality = getAirQualityFromUser();

        String description = generateDescription(condition, temperature, humidity);

        currentUserWeather = new WeatherData(condition, temperature, humidity, windSpeed, description, airQuality);

        System.out.println("\n[SUCCESS] Weather information collected successfully!");
        return currentUserWeather;
    }

    private String getWeatherConditionFromUser() {
        while (true) {
            System.out.println("Current Weather Condition:");
            System.out.println("1. Sunny/Clear");
            System.out.println("2. Cloudy/Overcast");
            System.out.println("3. Rainy/Drizzling");
            System.out.println("4. Stormy/Thunderstorm");
            System.out.println("5. Foggy/Misty");
            System.out.println("6. Very Hot");
            System.out.println("7. Very Cold");
            System.out.print("Select weather condition (1-7): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                switch (choice) {
                    case 1: return "Sunny";
                    case 2: return "Cloudy";
                    case 3: return "Rainy";
                    case 4: return "Stormy";
                    case 5: return "Foggy";
                    case 6: return "Hot";
                    case 7: return "Cold";
                    default:
                        System.out.println("Invalid choice! Please select 1-7.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid number (1-7).");
            }
        }
    }

    private double getTemperatureFromUser() {
        while (true) {
            System.out.print("Current Temperature (in Celsius, e.g., 25.5): ");
            try {
                double temp = Double.parseDouble(scanner.nextLine().trim());
                if (temp >= -20 && temp <= 50) {
                    return temp;
                } else {
                    System.out.println("Please enter a realistic temperature between -20°C and 50°C.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid temperature number.");
            }
        }
    }

    private int getHumidityFromUser() {
        while (true) {
            System.out.print("Current Humidity (0-100%, e.g., 65): ");
            try {
                int humidity = Integer.parseInt(scanner.nextLine().trim());
                if (humidity >= 0 && humidity <= 100) {
                    return humidity;
                } else {
                    System.out.println("Humidity must be between 0% and 100%.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid humidity percentage.");
            }
        }
    }

    private double getWindSpeedFromUser() {
        while (true) {
            System.out.print("Wind Speed (km/h, e.g., 15.5): ");
            try {
                double windSpeed = Double.parseDouble(scanner.nextLine().trim());
                if (windSpeed >= 0 && windSpeed <= 200) {
                    return windSpeed;
                } else {
                    System.out.println("Please enter a realistic wind speed between 0 and 200 km/h.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid wind speed number.");
            }
        }
    }

    private int getAirQualityFromUser() {
        while (true) {
            System.out.println("Air Quality Index (AQI):");
            System.out.println("0-50: Good | 51-100: Moderate | 101-150: Unhealthy for Sensitive");
            System.out.println("151-200: Unhealthy | 201-300: Very Unhealthy | 300+: Hazardous");
            System.out.print("Enter AQI value (0-500): ");
            try {
                int aqi = Integer.parseInt(scanner.nextLine().trim());
                if (aqi >= 0 && aqi <= 500) {
                    return aqi;
                } else {
                    System.out.println("AQI should be between 0 and 500.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Please enter a valid AQI number.");
            }
        }
    }

    private String generateDescription(String condition, double temperature, int humidity) {
        switch (condition) {
            case "Sunny":
                return "Clear sunny weather with bright sunshine";
            case "Cloudy":
                return "Overcast sky with clouds covering the sun";
            case "Rainy":
                return "Wet weather with rain showers";
            case "Stormy":
                return "Severe weather with thunderstorms and lightning";
            case "Foggy":
                return "Reduced visibility due to fog or mist";
            case "Hot":
                return "Very hot and warm conditions";
            case "Cold":
                return "Cold weather with low temperatures";
            default:
                return "Current weather conditions as reported";
        }
    }

    public WeatherData getCurrentWeather() {
        // If user has provided weather data, use it
        if (currentUserWeather != null) {
            return currentUserWeather;
        }

        // Otherwise, return simulated data for demo purposes
        return getSimulatedWeather();
    }

    private WeatherData getSimulatedWeather() {
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
        System.out.println("\n=== Current Weather & Suggestions ===");

        // Check if user wants to input current weather or use simulated data
        if (currentUserWeather == null) {
            System.out.println("Choose weather data source:");
            System.out.println("1. Enter current weather manually (Recommended for accurate suggestions)");
            System.out.println("2. Use simulated weather data (Demo mode)");
            System.out.print("Select option (1-2): ");

            try {
                int choice = Integer.parseInt(scanner.nextLine().trim());
                if (choice == 1) {
                    collectWeatherFromUser();
                } else if (choice == 2) {
                    System.out.println("[INFO] Using simulated weather data for demonstration.");
                } else {
                    System.out.println("Invalid choice. Using simulated data.");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid input. Using simulated data.");
            }
        }

        WeatherData weather = getCurrentWeather();

        System.out.println("\n=== Weather Information ===");
        System.out.printf("[Weather] Condition: %s\n", weather.getCondition());
        System.out.printf("[Temp] Temperature: %.1f°C\n", weather.getTemperatureCelsius());
        System.out.printf("[Humidity] %d%%\n", weather.getHumidity());
        System.out.printf("[Wind] Speed: %.1f km/h\n", weather.getWindSpeed());
        System.out.printf("[Air Quality] Index: %d (%s)\n", weather.getAirQualityIndex(), getAirQualityDescription(weather.getAirQualityIndex()));
        System.out.printf("[Description] %s\n", weather.getDescription());
        System.out.printf("[Data Source] %s\n", currentUserWeather != null ? "User Input" : "Simulated Data");
        System.out.printf("[Last Updated] %s\n", weather.getTimestamp().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")));

        // Automatically show weather-based suggestions
        displayWeatherBasedSuggestions();
    }

    public void updateWeatherData() {
        System.out.println("\n=== Update Weather Information ===");
        collectWeatherFromUser();
        System.out.println("[SUCCESS] Weather data updated! New suggestions will be generated based on current conditions.");
    }

    public boolean hasUserWeatherData() {
        return currentUserWeather != null;
    }

    public void clearWeatherData() {
        currentUserWeather = null;
        System.out.println("[INFO] Weather data cleared. Will use simulated data until new input is provided.");
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
        help.append("\n=== Weather-Based Smart Automation Help ===\n");
        help.append("Get personalized device automation suggestions based on weather conditions:\n\n");

        help.append("[DATA SOURCE] Weather Data Options:\n");
        help.append("   • Manual Input: Enter actual weather for accurate suggestions\n");
        help.append("   • Simulated Data: Use demo mode for testing features\n");
        help.append("   • Update Anytime: Refresh weather data as conditions change\n\n");

        help.append("[HOT] HOT WEATHER (>30°C):\n");
        help.append("   • Auto-suggest AC ON in main rooms\n");
        help.append("   • Turn on fans for air circulation\n");
        help.append("   • Reduce geyser usage to save energy\n");
        help.append("   • Smart scenes for cooling optimization\n\n");

        help.append("[COLD] COLD WEATHER (<18°C):\n");
        help.append("   • Ensure geysers are ON for hot water\n");
        help.append("   • Turn off unnecessary fans and AC\n");
        help.append("   • Maintain comfortable indoor temperature\n");
        help.append("   • Heating device optimization\n\n");

        help.append("[HUMID] HIGH HUMIDITY (>70%):\n");
        help.append("   • Turn on fans for better air circulation\n");
        help.append("   • Activate air purifiers for comfort\n");
        help.append("   • Dehumidification suggestions\n\n");

        help.append("[AIR] POOR AIR QUALITY (AQI >150):\n");
        help.append("   • Auto-activate all air purifiers\n");
        help.append("   • Indoor air quality improvement\n");
        help.append("   • Ventilation management recommendations\n\n");

        help.append("[RAIN] RAINY WEATHER:\n");
        help.append("   • Turn on lights for better visibility\n");
        help.append("   • Ensure geyser is ON for warmth\n");
        help.append("   • Moisture control suggestions\n\n");

        help.append("[STORM] STORMY CONDITIONS:\n");
        help.append("   • Turn off electronics for safety\n");
        help.append("   • Avoid high-power appliances during lightning\n");
        help.append("   • Emergency preparedness mode\n\n");

        help.append("[FEATURES] Advanced Features:\n");
        help.append("   • Real-time weather input for accurate suggestions\n");
        help.append("   • Personalized automation based on your devices\n");
        help.append("   • Energy-saving recommendations\n");
        help.append("   • Safety-first approach during severe weather\n\n");

        help.append("[TIP] For best results, update weather data regularly!\n");

        return help.toString();
    }
}