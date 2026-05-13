# Database Visualizations

## 1. Conceptual Schema (Peter Chen Notation)

In this notation:
- **Rectangles**: Entities
- **Diamonds**: Relationships
- **Ovals**: Attributes (Simulated with nodes in this diagram)

```mermaid
flowchart TD
    %% Entities
    User[("User")]
    Reminder[("Reminder")]
    Schedule[("Schedule")]
    Tag[("Tag")]
    Log[("Notification Log")]

    %% Relationships
    User -- "Owns (1:N)" --- Owns{{"Owns"}}
    Owns --- Reminder

    Reminder -- "Has (1:1)" --- HasSchedule{{"Has"}}
    HasSchedule --- Schedule

    Reminder -- "Categorized (M:N)" --- Categorized{{"Categorized"}}
    Categorized --- Tag

    Reminder -- "Generates (1:N)" --- Generates{{"Generates"}}
    Generates --- Log

    %% Attributes for User
    User --- U_ID([ID])
    User --- U_Name([Username])
    User --- U_Email([Email])

    %% Attributes for Reminder
    Reminder --- R_ID([ID])
    Reminder --- R_Title([Title])
    Reminder --- R_Desc([Description])

    %% Attributes for Tag
    Tag --- T_ID([ID])
    Tag --- T_Name([Name])
```

---

## 2. Logical Schema (Crow's Foot Notation)

This diagram shows the exact table structures, primary keys, and foreign keys.

```mermaid
erDiagram
    USERS ||--o{ REMINDERS : "creates"
    REMINDERS ||--|| SCHEDULES : "follows"
    REMINDERS ||--o{ NOTIFICATION_LOGS : "triggers"
    REMINDERS }|--|{ TAGS : "labeled with"

    USERS {
        int id PK
        string username
        string email
        string password_hash
        string timezone
    }

    REMINDERS {
        int id PK
        int user_id FK
        string title
        string description
        boolean is_enabled
        datetime created_at
    }

    SCHEDULES {
        int id PK
        int reminder_id FK
        string frequency
        int interval
        datetime start_time
        datetime next_run
    }

    TAGS {
        int id PK
        string name
        string color
    }

    REMINDER_TAGS {
        int reminder_id FK
        int tag_id FK
    }

    NOTIFICATION_LOGS {
        int id PK
        int reminder_id FK
        datetime sent_at
        string status
        string channel_type
    }
```
