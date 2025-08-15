# Flight Management CLI

Simple Java CLI that calls the Flight Management API over HTTP and answers 4 questions (GET-only).

## Prerequisites
- Java 21+ (recommended; Java 17 may also work)
- Maven (or use the Maven wrapper if included)
- Running API backend (default assumed at `http://localhost:8080`)

Backend repo (API): https://github.com/SteveMorrison101/flight_management_api

## Configure BASE_URL
The CLI resolves the API base URL in this order:

1. **Command-line arg**  
   ```bash
   java -jar target/flight_management_cli-1.0-SNAPSHOT.jar http://18.220.153.136
   ```

2. **Env var**  
   ```bash
   BASE_URL=http://18.220.153.136
   ```

3. **Default**  
   ```
   http://localhost:8080
   ```

## Build & Test

```bash
mvn clean verify
```

During tests, the CLI runs in non-interactive test mode so CI doesn’t hang.

## Run (interactive)

With a running backend at localhost:

```bash
mvn -q -DskipTests clean package
java -jar target/flight_management_cli-1.0-SNAPSHOT.jar
```

Point to a different backend:

```bash
java -jar target/flight_management_cli-1.0-SNAPSHOT.jar http://18.220.153.136
# or
set BASE_URL=http://18.220.153.136 && java -jar target/flight_management_cli-1.0-SNAPSHOT.jar
```

## What it answers

1. Airports in each city (GET /cities)  
2. Aircraft each passenger has flown on (GET /aircraft-with-passengers)  
3. Airports aircraft use (takeoff/landing) (GET /aircraft-with-airports)  
4. Airports passengers have used (GET /passengers-with-airports)  

## Notes

- CLI is read-only; the API implements full CRUD.  
- Tests mock HTTP so they’re fast and predictable.

---

## `run-cli.ps1` helper script

Create this file in the project root to build and run with an optional BASE_URL:

```powershell
# runs the CLI with optional BASE_URL
param(
  [string]$BaseUrl = ""
)

Set-Location -Path $PSScriptRoot

# build (skip tests for speed)
mvn -q -DskipTests clean package

if ([string]::IsNullOrWhiteSpace($BaseUrl)) {
  # default localhost:8080 inside the app
  java -jar "target\flight_management_cli-1.0-SNAPSHOT.jar"
} else {
  java -jar "target\flight_management_cli-1.0-SNAPSHOT.jar" $BaseUrl
}
```

### Usage
```powershell
# Default localhost
.un-cli.ps1

# Specify backend
.un-cli.ps1 http://18.220.153.136
```
