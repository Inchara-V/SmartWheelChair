# Quick Start Guide - Accessibility Navigator Backend

## 🚀 Getting Started

### Prerequisites
- Java 17+
- MySQL 5.7+
- Maven 3.6+ (bundled with project via mvnw)

### Database Setup

1. **Start MySQL**
   ```bash
   # Windows
   net start MySQL80
   
   # Or use MySQL CLI
   mysql -u root -p
   ```

2. **Create Database** (auto-created on first run, but you can pre-create):
   ```sql
   CREATE DATABASE accessibility_db;
   USE accessibility_db;
   ```

### Running the Backend

1. **Navigate to project directory**
   ```bash
   cd c:\Users\Divya\OneDrive\Documents\Accessiblity_Navigator\accessiblity_backend
   ```

2. **Build the project**
   ```bash
   mvnw clean compile
   ```

3. **Run the application**
   ```bash
   mvnw spring-boot:run
   ```

4. **Verify it's running**
   ```bash
   # Should return 200 OK
   curl http://localhost:8080/api/places
   ```

## 📊 API Endpoints

### Base URL: `http://localhost:8080/api`

### Places Management

#### Create Place
```bash
POST /places
Content-Type: application/json

{
  "name": "City Hospital",
  "address": "456 Health Ave",
  "latitude": 40.7128,
  "longitude": -74.0060,
  "description": "Modern hospital with accessibility features",
  "placeType": "HOSPITAL",
  "hasRamp": true,
  "hasElevator": true,
  "hasAccessibleToilet": true,
  "hasWheelchairAccess": true
}
```

#### Get All Places
```bash
GET /places
```

#### Get Place by ID
```bash
GET /places/1
```

#### Update Place
```bash
PUT /places/1
Content-Type: application/json

{
  "name": "Updated Name",
  "address": "789 New Street",
  "hasRamp": false
}
```

#### Delete Place
```bash
DELETE /places/1
```

### Reports Management

#### Create Report
```bash
POST /reports
Content-Type: application/json

{
  "placeId": 1,
  "reporterName": "John Doe",
  "reporterEmail": "john@example.com",
  "description": "Place has newly installed ramp",
  "hasRamp": true,
  "hasElevator": true,
  "hasAccessibleToilet": false,
  "hasWheelchairAccess": true
}
```

#### Get All Reports
```bash
GET /reports
```

#### Get Reports for a Place
```bash
GET /reports/place/1
```

#### Update Report
```bash
PUT /reports/1
Content-Type: application/json

{
  "status": "APPROVED"
}
```

#### Delete Report
```bash
DELETE /reports/1
```

### Accessibility Score

#### Get Place Score & Classification
```bash
GET /score/place/1

Response:
{
  "placeId": 1,
  "placeName": "City Hospital",
  "score": 100,
  "classification": "FULLY_ACCESSIBLE",
  "features": {
    "hasRamp": true,
    "hasElevator": true,
    "hasAccessibleToilet": true,
    "hasWheelchairAccess": true
  }
}
```

### Dashboard Analytics

#### Get Dashboard Statistics
```bash
GET /dashboard/stats

Response:
{
  "totalPlaces": 50,
  "accessiblePlaces": 15,
  "partiallyAccessiblePlaces": 25,
  "notAccessiblePlaces": 10,
  "averageAccessibilityScore": 62.5
}
```

## 🧪 Sample Data Creation Script

Create `sample-data.sh` to load test data:

```bash
#!/bin/bash

BASE_URL="http://localhost:8080/api"

# Create Places
curl -X POST "$BASE_URL/places" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Central Library",
    "address": "123 Main St",
    "latitude": 40.7580,
    "longitude": -73.9855,
    "placeType": "EDUCATIONAL_INSTITUTION",
    "hasRamp": true,
    "hasElevator": true,
    "hasAccessibleToilet": true,
    "hasWheelchairAccess": false
  }'

curl -X POST "$BASE_URL/places" \
  -H "Content-Type: application/json" \
  -d '{
    "name": "Downtown Cafe",
    "address": "456 Park Ave",
    "latitude": 40.7489,
    "longitude": -73.9680,
    "placeType": "RESTAURANT",
    "hasRamp": false,
    "hasElevator": false,
    "hasAccessibleToilet": true,
    "hasWheelchairAccess": false
  }'

# Get all places
curl -X GET "$BASE_URL/places"

# Get dashboard stats
curl -X GET "$BASE_URL/dashboard/stats"
```

Run it:
```bash
bash sample-data.sh
```

## 📁 Project Structure

```
accessibility_backend/
├── src/
│   ├── main/
│   │   ├── java/com/accessiblity/backend/
│   │   │   ├── entity/              # Database entities
│   │   │   ├── repository/          # JPA repositories
│   │   │   ├── service/             # Business logic
│   │   │   ├── controller/          # REST endpoints
│   │   │   ├── dto/                 # Data transfer objects
│   │   │   ├── exception/           # Custom exceptions
│   │   │   └── AccessiblityBackendApplication.java
│   │   └── resources/
│   │       └── application.properties
│   └── test/
├── pom.xml                          # Maven configuration
├── mvnw & mvnw.cmd                 # Maven wrapper
├── IMPLEMENTATION_SUMMARY.md        # What was built
├── ANALYTICS_INTEGRATION_GUIDE.md   # How to extend it
└── README.md
```

## ⚙️ Configuration

**File**: `src/main/resources/application.properties`

Key settings:
```properties
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/accessibility_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.hibernate.ddl-auto=update
```

To change settings:
1. Edit `application.properties`
2. Restart the application

## 🐛 Troubleshooting

### "Connection refused" (Database error)
- Ensure MySQL is running
- Check database URL in `application.properties`
- Default: `localhost:3306` with username `root`

### "Build failed" 
- Run: `mvnw clean compile -X` for debug output
- Check Java version: `java -version` (needs 17+)

### Port already in use
- Change port in `application.properties`: `server.port=8081`
- Or kill process on port 8080: `netstat -ano | findstr :8080`

### Scores not updating
- Scores are calculated automatically on place creation/update
- If not seeing updated scores, verify place was updated successfully

## 📈 Accessing Logs

Logs are printed to console by default. To file:

Add to `application.properties`:
```properties
logging.file.name=logs/app.log
```

View logs:
```bash
tail -f logs/app.log
```

## 🔒 Security Notes

⚠️ **Current State**: No authentication/authorization
- All endpoints are public
- Add Spring Security before production

**TODO for Production**:
- [ ] Add JWT authentication
- [ ] Add role-based access control
- [ ] Add HTTPS/SSL
- [ ] Add rate limiting
- [ ] Add request validation
- [ ] Add input sanitization

## 📊 Database Schema

Automatically created on startup. Tables created:
- `places` - Core place data
- `reports` - User accessibility reports

View schema:
```sql
mysql> use accessibility_db;
mysql> SHOW TABLES;
mysql> DESCRIBE places;
mysql> DESCRIBE reports;
```

## 🧩 Integration Points for Analytics

See `ANALYTICS_INTEGRATION_GUIDE.md` for detailed information on:
1. Where to modify scoring logic
2. Where to add new metrics
3. How to integrate analytics team's formula
4. Database schema extensions

Quick reference:
- **Scoring**: Modify `AccessibilityScoreService.calculateScore()`
- **Dashboard**: Extend `DashboardService` methods
- **Classification**: Update `AccessibilityScoreService` is* methods
- **Fields**: Add to `Place` entity as needed

## 📝 API Response Format

### Success Response
```json
{
  "id": 1,
  "name": "Place Name",
  "accessibilityScore": 75
}
```

### Error Response
```json
{
  "timestamp": "2026-06-12T19:37:42.911+05:30",
  "status": 404,
  "error": "Not Found",
  "message": "Place not found with id: 999"
}
```

## 🎯 Next Steps

1. **Start the backend**: `mvnw spring-boot:run`
2. **Create test places**: Use the API examples above
3. **View dashboard**: Call `GET /api/dashboard/stats`
4. **Review analytics guide**: See `ANALYTICS_INTEGRATION_GUIDE.md`
5. **Prepare for analytics**: Provide scoring formula when ready

## 📞 Support

For issues:
1. Check error message in console logs
2. Verify MySQL is running
3. Check `application.properties` configuration
4. Review `ANALYTICS_INTEGRATION_GUIDE.md` for architecture details
5. Review test scripts for API usage examples

## ✅ Verification Checklist

After starting the backend:
- [ ] No compilation errors
- [ ] Application starts without errors
- [ ] Can create a place via POST /api/places
- [ ] Can retrieve place via GET /api/places/1
- [ ] Can get dashboard stats via GET /api/dashboard/stats
- [ ] Score is calculated correctly
- [ ] Errors return proper JSON format

---

**Backend Ready**: ✅ All systems operational
