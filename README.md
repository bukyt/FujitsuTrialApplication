# FujitsuTrialApplication
A repository to store my Fujitsu trial task application
Run with gradlew bootRun

- The login details are stored in SecurityConfig.java with InMemoryDetailsManager, this could be replaced with a database but I haven't currently implemented a database user details manager.
- The database has one table, Weather, which has the following creation script: 
```
CREATE TABLE PUBLIC.WEATHER[
ID numeric NOT NULL AUTO_INCREMENT,
NAME varchar(50) NOT NULL,
WMOCODE numeric(5) NOT NULL,
AIR numeric(3,1) NOT NULL,
WIND numeric(3,1) NOT NULL,
PHENOMENON varchar(50) NOT NULL,
QUERYTIME TIMESTAMP NOT NULL default CURRENT_TIMESTAMP,
PRIMARY KEY ID
];
```
- The importing of data from https://www.ilmateenistus.ee/ilma_andmed/xml/observations.php is handled by QueryExecutionTask.java, it runs every hour on the 15th minute scheduled with cron
- Data is imported from the database with DataManagerDAO, it creates a connection with the parameters "jdbc:h2:~/Fujitsu", "user", "password"
