package com.smarthome.service;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
public class CalendarEventService {
    private static CalendarEventService instance;
    private final Map<String, List<CalendarEvent>> userEvents;
    private CalendarEventService() {
        this.userEvents = new HashMap<>();
    }
    public static synchronized CalendarEventService getInstance() {
        if (instance == null) {
            instance = new CalendarEventService();
        }
        return instance;
    }
    public static class CalendarEvent {
        private String eventId;
        private String title;
        private String description;
        private LocalDateTime startTime;
        private LocalDateTime endTime;
        private String eventType;
        private List<AutomationAction> automationActions;
        private boolean isRecurring;
        private String recurrencePattern;
        public CalendarEvent(String eventId, String title, String description, 
                           LocalDateTime startTime, LocalDateTime endTime, String eventType) {
            this.eventId = eventId;
            this.title = title;
            this.description = description;
            this.startTime = startTime;
            this.endTime = endTime;
            this.eventType = eventType;
            this.automationActions = new ArrayList<>();
            this.isRecurring = false;
        }
        public String getEventId() { return eventId; }
        public String getTitle() { return title; }
        public String getDescription() { return description; }
        public LocalDateTime getStartTime() { return startTime; }
        public LocalDateTime getEndTime() { return endTime; }
        public String getEventType() { return eventType; }
        public List<AutomationAction> getAutomationActions() { return automationActions; }
        public boolean isRecurring() { return isRecurring; }
        public String getRecurrencePattern() { return recurrencePattern; }
        public void setRecurring(boolean recurring) { this.isRecurring = recurring; }
        public void setRecurrencePattern(String recurrencePattern) { this.recurrencePattern = recurrencePattern; }
        public void addAutomationAction(AutomationAction action) {
            this.automationActions.add(action);
        }
    }
    public static class AutomationAction {
        private String deviceType;
        private String roomName;
        private String action;
        private int minutesOffset;
        public AutomationAction(String deviceType, String roomName, String action, int minutesOffset) {
            this.deviceType = deviceType;
            this.roomName = roomName;
            this.action = action;
            this.minutesOffset = minutesOffset;
        }
        public String getDeviceType() { return deviceType; }
        public String getRoomName() { return roomName; }
        public String getAction() { return action; }
        public int getMinutesOffset() { return minutesOffset; }
    }
    public boolean createEvent(String userEmail, String title, String description, 
                             LocalDateTime startTime, LocalDateTime endTime, String eventType) {
        try {
            String eventId = generateEventId(userEmail, startTime);
            CalendarEvent event = new CalendarEvent(eventId, title, description, startTime, endTime, eventType);
            userEvents.computeIfAbsent(userEmail, k -> new ArrayList<>()).add(event);
            addDefaultAutomationForEventType(event, eventType);
            System.out.println("[SUCCESS] Calendar event created: " + title);
            System.out.println("Event Date: " + startTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + 
                             " to " + endTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            return true;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to create calendar event: " + e.getMessage());
            return false;
        }
    }
    private void addDefaultAutomationForEventType(CalendarEvent event, String eventType) {
        switch (eventType.toLowerCase()) {
            case "meeting":
            case "conference":
                event.addAutomationAction(new AutomationAction("LIGHT", "Study Room", "ON", -5));
                event.addAutomationAction(new AutomationAction("AC", "Study Room", "ON", -10));
                event.addAutomationAction(new AutomationAction("LIGHT", "Study Room", "OFF", 15));
                break;
            case "movie":
            case "entertainment":
                event.addAutomationAction(new AutomationAction("TV", "Living Room", "ON", -2));
                event.addAutomationAction(new AutomationAction("LIGHT", "Living Room", "OFF", 0));
                event.addAutomationAction(new AutomationAction("AC", "Living Room", "ON", -15));
                break;
            case "sleep":
            case "bedtime":
                event.addAutomationAction(new AutomationAction("LIGHT", "Master Bedroom", "OFF", 0));
                event.addAutomationAction(new AutomationAction("TV", "Master Bedroom", "OFF", 0));
                event.addAutomationAction(new AutomationAction("AC", "Master Bedroom", "ON", -5));
                break;
            case "cooking":
            case "meal":
                event.addAutomationAction(new AutomationAction("LIGHT", "Kitchen", "ON", -5));
                event.addAutomationAction(new AutomationAction("AIR_PURIFIER", "Kitchen", "ON", 0));
                event.addAutomationAction(new AutomationAction("MICROWAVE", "Kitchen", "OFF", 30));
                break;
            case "workout":
            case "exercise":
                event.addAutomationAction(new AutomationAction("FAN", "Living Room", "ON", -5));
                event.addAutomationAction(new AutomationAction("SPEAKER", "Living Room", "ON", 0));
                event.addAutomationAction(new AutomationAction("AIR_PURIFIER", "Living Room", "ON", 0));
                break;
            case "arrival":
            case "home":
                event.addAutomationAction(new AutomationAction("LIGHT", "Living Room", "ON", 0));
                event.addAutomationAction(new AutomationAction("AC", "Living Room", "ON", 0));
                event.addAutomationAction(new AutomationAction("GEYSER", "Kitchen", "ON", 0));
                break;
            case "departure":
            case "leaving":
                event.addAutomationAction(new AutomationAction("LIGHT", "Living Room", "OFF", 0));
                event.addAutomationAction(new AutomationAction("FAN", "Living Room", "OFF", 0));
                event.addAutomationAction(new AutomationAction("TV", "Living Room", "OFF", 0));
                break;
        }
    }
    public List<CalendarEvent> getUpcomingEvents(String userEmail) {
        List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
        LocalDateTime now = LocalDateTime.now();
        return events.stream()
            .filter(event -> event.getStartTime().isAfter(now))
            .sorted((e1, e2) -> e1.getStartTime().compareTo(e2.getStartTime()))
            .limit(10)
            .toList();
    }

    public void displayUpcomingEvents(String userEmail) {
        System.out.println("\n=== Upcoming Calendar Events ===");
        List<CalendarEvent> upcomingEvents = getUpcomingEvents(userEmail);
        if (upcomingEvents.isEmpty()) {
            System.out.println("No upcoming events scheduled.");
            return;
        }
        for (int i = 0; i < upcomingEvents.size(); i++) {
            CalendarEvent event = upcomingEvents.get(i);
            System.out.printf("%d. %s (%s)\n", (i + 1), event.getTitle(), event.getEventType());
            System.out.printf("   [DATE] %s - %s\n",
                            event.getStartTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")),
                            event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
            if (!event.getAutomationActions().isEmpty()) {
                System.out.printf("   [AUTO] %d automation actions configured\n", event.getAutomationActions().size());
            }
            if (!event.getDescription().isEmpty()) {
                System.out.printf("   [INFO] %s\n", event.getDescription());
            }
            System.out.println();
        }
    }
    public void displayEventAutomation(String userEmail, String eventTitle) {
        List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
        CalendarEvent event = events.stream()
            .filter(e -> e.getTitle().equalsIgnoreCase(eventTitle))
            .findFirst()
            .orElse(null);
        if (event == null) {
            System.out.println("[ERROR] Event not found: " + eventTitle);
            return;
        }
        System.out.println("\n=== Event Automation Details ===");
        System.out.println("Event: " + event.getTitle() + " (" + event.getEventType() + ")");
        System.out.println("Time: " + event.getStartTime().format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) + 
                         " to " + event.getEndTime().format(DateTimeFormatter.ofPattern("HH:mm")));
        if (event.getAutomationActions().isEmpty()) {
            System.out.println("No automation actions configured for this event.");
            return;
        }
        System.out.println("\nAutomation Actions:");
        for (AutomationAction action : event.getAutomationActions()) {
            String timing = action.getMinutesOffset() == 0 ? "at event start" :
                          action.getMinutesOffset() < 0 ? (Math.abs(action.getMinutesOffset()) + " min before") :
                          (action.getMinutesOffset() + " min after");
            System.out.printf("- %s %s in %s -> %s (%s)\n", 
                            action.getDeviceType(), action.getAction(), action.getRoomName(), 
                            timing, action.getAction());
        }
    }
    public List<String> getEventTypes() {
        return List.of("Meeting", "Conference", "Movie", "Entertainment", "Sleep", "Bedtime", 
                      "Cooking", "Meal", "Workout", "Exercise", "Arrival", "Home", 
                      "Departure", "Leaving", "Custom");
    }
    public CalendarEvent getEventByTitle(String userEmail, String eventTitle) {
        List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
        return events.stream()
            .filter(e -> e.getTitle().equalsIgnoreCase(eventTitle))
            .findFirst()
            .orElse(null);
    }

    public boolean editEvent(String userEmail, String originalTitle, String newTitle, String newDescription,
                           LocalDateTime newStartTime, LocalDateTime newEndTime, String newEventType) {
        try {
            List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
            CalendarEvent eventToEdit = events.stream()
                .filter(e -> e.getTitle().equalsIgnoreCase(originalTitle))
                .findFirst()
                .orElse(null);

            if (eventToEdit == null) {
                System.out.println("[ERROR] Event not found: " + originalTitle);
                return false;
            }

            events.remove(eventToEdit);

            String eventId = generateEventId(userEmail, newStartTime);
            CalendarEvent updatedEvent = new CalendarEvent(eventId, newTitle, newDescription, newStartTime, newEndTime, newEventType);
            events.add(updatedEvent);

            addDefaultAutomationForEventType(updatedEvent, newEventType);

            System.out.println("[SUCCESS] Event updated: " + newTitle);
            System.out.println("Event Date: " + newStartTime.format(DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm")) +
                             " to " + newEndTime.format(DateTimeFormatter.ofPattern("HH:mm")));
            return true;
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to edit event: " + e.getMessage());
            return false;
        }
    }

    public boolean deleteEvent(String userEmail, String eventTitle) {
        try {
            List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
            CalendarEvent eventToDelete = events.stream()
                .filter(event -> event.getTitle().equalsIgnoreCase(eventTitle))
                .findFirst()
                .orElse(null);

            if (eventToDelete == null) {
                System.out.println("[ERROR] Event not found: " + eventTitle);
                return false;
            }

            boolean removed = events.removeIf(event -> event.getTitle().equalsIgnoreCase(eventTitle));
            if (removed) {
                System.out.println("[SUCCESS] Event deleted: " + eventTitle);

                if (!eventToDelete.getAutomationActions().isEmpty()) {
                    System.out.println("[INFO] Automation actions cancelled:");
                    for (AutomationAction action : eventToDelete.getAutomationActions()) {
                        String timing = action.getMinutesOffset() == 0 ? "at event start" :
                                      action.getMinutesOffset() < 0 ? (Math.abs(action.getMinutesOffset()) + " min before") :
                                      (action.getMinutesOffset() + " min after");
                        System.out.printf("   - %s %s in %s -> %s (%s) - CANCELLED\n",
                                        action.getDeviceType(), action.getAction(), action.getRoomName(),
                                        action.getAction(), timing);
                    }
                    System.out.println("[INFO] All scheduled device automation for this event has been cleared.");
                }
                return true;
            } else {
                System.out.println("[ERROR] Event not found: " + eventTitle);
                return false;
            }
        } catch (Exception e) {
            System.out.println("[ERROR] Failed to delete event: " + e.getMessage());
            return false;
        }
    }

    public List<AutomationAction> getEventAutomationActions(String userEmail, String eventTitle) {
        List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
        CalendarEvent event = events.stream()
            .filter(e -> e.getTitle().equalsIgnoreCase(eventTitle))
            .findFirst()
            .orElse(null);

        if (event != null) {
            return new ArrayList<>(event.getAutomationActions());
        }
        return new ArrayList<>();
    }
    public List<CalendarEvent> getEventsForAutomation(String userEmail, LocalDateTime checkTime) {
        List<CalendarEvent> events = userEvents.getOrDefault(userEmail, new ArrayList<>());
        List<CalendarEvent> triggeredEvents = new ArrayList<>();
        for (CalendarEvent event : events) {
            for (AutomationAction action : event.getAutomationActions()) {
                LocalDateTime triggerTime = event.getStartTime().plusMinutes(action.getMinutesOffset());
                if (Math.abs(java.time.Duration.between(checkTime, triggerTime).toMinutes()) <= 1) {
                    triggeredEvents.add(event);
                    break;
                }
            }
        }
        return triggeredEvents;
    }
    private String generateEventId(String userEmail, LocalDateTime startTime) {
        return userEmail.split("@")[0] + "_" + startTime.format(DateTimeFormatter.ofPattern("yyyyMMddHHmm"));
    }
    public String getCalendarHelp() {
        StringBuilder help = new StringBuilder();
        help.append("\n=== Calendar Events Help ===\n");
        help.append("Available Event Types and Their Auto-Automation:\n\n");
        help.append("[1] MEETING/CONFERENCE:\n");
        help.append("   - Lights ON in Study Room (5 min before)\n");
        help.append("   - AC ON in Study Room (10 min before)\n");
        help.append("   - Lights OFF in Study Room (15 min after)\n\n");
        help.append("[2] MOVIE/ENTERTAINMENT:\n");
        help.append("   - TV ON in Living Room (2 min before)\n");
        help.append("   - Lights OFF in Living Room (at start)\n");
        help.append("   - AC ON in Living Room (15 min before)\n\n");
        help.append("[3] SLEEP/BEDTIME:\n");
        help.append("   - All lights OFF in Master Bedroom\n");
        help.append("   - TV OFF in Master Bedroom\n");
        help.append("   - AC ON in Master Bedroom (5 min before)\n\n");
        help.append("[4] COOKING/MEAL:\n");
        help.append("   - Lights ON in Kitchen (5 min before)\n");
        help.append("   - Air Purifier ON in Kitchen\n");
        help.append("   - Microwave OFF (30 min after)\n\n");
        help.append("[5] WORKOUT/EXERCISE:\n");
        help.append("   - Fan ON in Living Room (5 min before)\n");
        help.append("   - Speaker ON in Living Room\n");
        help.append("   - Air Purifier ON in Living Room\n\n");
        help.append("[6] ARRIVAL/HOME:\n");
        help.append("   - Lights ON in Living Room\n");
        help.append("   - AC ON in Living Room\n");
        help.append("   - Geyser ON in Kitchen\n\n");
        help.append("[7] DEPARTURE/LEAVING:\n");
        help.append("   - All devices OFF in Living Room\n");
        return help.toString();
    }
}
