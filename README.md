# Schedule-Based Reminder System

A JavaFX application for managing reminders based on schedules.

## Features

- **User Authentication**: Secure login and registration.
- **Reminder Management**: Create, update, and delete reminders.
- **Schedules**: Flexible scheduling for reminders.
- **Tags**: Organize reminders with tags.
- **Notifications**: Automated notifications for upcoming reminders.
- **Database**: Uses H2 database with Flyway migrations.

## Technologies Used

- **Java 17+**
- **JavaFX**: For the graphical user interface.
- **Maven**: For dependency management and building.
- **H2 Database**: Embedded relational database.
- **Flyway**: For database schema migrations.
- **JavaMail API**: For sending email notifications.

## Getting Started

### Prerequisites

- Java Development Kit (JDK) 17 or higher.
- Apache Maven.

### Installation

1. Clone the repository:
   ```bash
   git clone https://github.com/cpolovkamykhailo-prog/schedule-based-reminder-system.git
   ```
2. Navigate to the project directory:
   ```bash
   cd schedule-based-reminder-system
   ```
3. Build the project:
   ```bash
   mvn clean install
   ```

### Running the Application

To run the application, use the following Maven command:
```bash
mvn javafx:run
```

## Project Structure

- `src/main/java`: Source code.
- `src/main/resources`: Configuration files, styles, and SQL migrations.
- `db/`: Local database storage (ignored by Git).
- `docs/`: Project documentation and visualizations.

## License

This project is licensed under the MIT License - see the [LICENSE](LICENSE) file for details.
