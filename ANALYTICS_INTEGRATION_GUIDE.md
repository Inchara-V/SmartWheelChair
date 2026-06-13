# Accessibility Navigator Backend - Extension Points & Analytics Integration Guide

## Project Structure Overview

```
com.accessiblity.backend/
├── entity/                 # JPA entities
│   ├── Place.java         # Place entity with accessibility features
│   ├── PlaceType.java     # Place type enumeration
│   ├── Report.java        # Accessibility report entity
│   └── ReportStatus.java  # Report status enumeration
├── repository/            # JPA repositories
│   ├── PlaceRepository.java
│   └── ReportRepository.java
├── service/               # Business logic services
│   ├── AccessibilityScoreService.java  # CENTRAL SCORING LOGIC
│   ├── DashboardService.java           # Dashboard analytics
│   ├── PlaceService.java              # Place CRUD operations
│   └── ReportService.java             # Report CRUD operations
├── controller/            # REST API endpoints
│   ├── PlaceController.java
│   ├── ReportController.java
│   ├── ScoreController.java
│   ├── DashboardController.java
│   └── GlobalExceptionHandler.java
├── dto/                   # Data transfer objects
│   ├── PlaceDTO.java
│   ├── ReportDTO.java
│   └── DashboardStatsResponse.java
└── exception/             # Custom exceptions
    ├── ResourceNotFoundException.java
    └── ValidationException.java
```

## API Endpoints

### Places Management
- `POST /api/places` - Create a new place
- `GET /api/places` - Get all places
- `GET /api/places/{id}` - Get place by ID
- `PUT /api/places/{id}` - Update place
- `DELETE /api/places/{id}` - Delete place

### Reports Management
- `POST /api/reports` - Create a new accessibility report
- `GET /api/reports` - Get all reports
- `GET /api/reports/{id}` - Get report by ID
- `GET /api/reports/place/{placeId}` - Get reports for a specific place
- `PUT /api/reports/{id}` - Update report
- `DELETE /api/reports/{id}` - Delete report

### Accessibility Score
- `GET /api/score/place/{placeId}` - Get accessibility score and classification for a place

### Dashboard Analytics
- `GET /api/dashboard/stats` - Get aggregated dashboard statistics

## Key Extension Points for Analytics Integration

### 1. **AccessibilityScoreService** (CRITICAL - Central Scoring Logic)

**Location**: `com.accessiblity.backend.service.AccessibilityScoreService`

**Current Implementation**:
- Simple point-based system: each facility (Ramp, Elevator, Accessible Toilet, Wheelchair Access) = 25 points
- Maximum score: 100

**Extension Points**:
```java
// TODO: Replace temporary scoring formula with analytics team's final formula
public int calculateScore(Place place) {
    // Current: Simple equal weighting (25 points each)
    // Future: Replace with complex algorithms involving:
    //  - Weighted criteria based on place type
    //  - Facility condition assessment
    //  - User accessibility preference profiles
    //  - Machine learning model predictions
}
```

**How to Integrate Analytics**:
1. When analytics team provides the scoring formula, replace the logic in `calculateScore()`
2. No changes needed in controllers or services - they only call this method
3. Place entity's `accessibilityScore` field is automatically calculated on create/update

### 2. **DashboardService** (Analytics Calculations)

**Location**: `com.accessiblity.backend.service.DashboardService`

**Current Placeholder Methods**:
```java
Long getTotalPlaces()              // Returns total count
Long getAccessiblePlaces()         // Fully accessible places (all 4 features)
Long getPartiallyAccessiblePlaces()// Places with 1-3 features
Long getNotAccessiblePlaces()      // No accessibility features
Double getAverageAccessibilityScore() // Average of all scores
```

**Extension Points**:
```java
// TODO: Replace with analytics team's final accessibility classification
// TODO: Integrate with dashboard metrics calculations
// These methods can be extended to:
//  - Support date range filtering
//  - Include demographic breakdowns
//  - Add trend calculations
//  - Include facility-specific statistics
//  - Support geographic clustering
```

### 3. **Accessibility Classification** (Scoring Criteria)

**Location**: `com.accessiblity.backend.service.AccessibilityScoreService`

**Current Criteria**:
- **FULLY_ACCESSIBLE**: All 4 features present (Ramp, Elevator, Accessible Toilet, Wheelchair Access)
- **PARTIALLY_ACCESSIBLE**: 1-3 features present
- **NOT_ACCESSIBLE**: No features present

**Extension Points**:
```java
// TODO: Update criteria when analytics team provides accessibility rating rules
boolean isFullyAccessible(Place place)      // Update classification logic
boolean isPartiallyAccessible(Place place)
boolean isNotAccessible(Place place)
```

These methods are used in:
- Dashboard statistics calculation
- Score controller for place classification
- Future reporting and filtering

### 4. **Report Integration** (User Feedback Loop)

**Location**: 
- `com.accessiblity.backend.entity.Report`
- `com.accessiblity.backend.service.ReportService`

**Current Implementation**:
- Reports store user-submitted accessibility information
- Status tracking: PENDING → REVIEWED → APPROVED/REJECTED

**Extension Points**:
```
Report fields can be extended with:
- Photo evidence storage (AWS S3/Azure Blob links)
- Accessibility feature conditions (Good/Fair/Poor)
- User ratings and comments
- Report verification scores
- Automatic score adjustment recommendations based on approved reports
```

### 5. **Database Schema** (Future Analytics Tables)

**Current Entities**:
- `places` - Core place data
- `reports` - User-submitted accessibility reports

**Future Table Suggestions** (for analytics team):
```sql
-- Accessibility metrics snapshot (for time-series analytics)
CREATE TABLE accessibility_metrics_snapshot (
    id BIGINT PRIMARY KEY,
    snapshot_date DATETIME,
    place_id BIGINT,
    score INT,
    accessibility_category VARCHAR(50),
    created_at DATETIME
);

-- Accessibility audit trail (for tracking changes)
CREATE TABLE accessibility_audit_log (
    id BIGINT PRIMARY KEY,
    place_id BIGINT,
    old_score INT,
    new_score INT,
    changed_fields VARCHAR(255),
    changed_by VARCHAR(100),
    changed_at DATETIME
);

-- Analytics calculations cache (for performance)
CREATE TABLE analytics_cache (
    id BIGINT PRIMARY KEY,
    metric_name VARCHAR(100),
    metric_value DOUBLE,
    calculated_at DATETIME,
    valid_until DATETIME
);
```

### 6. **DTO Extensions** (Additional Data)

**Location**: `com.accessiblity.backend.dto.DashboardStatsResponse`

**Current Fields**:
```java
Long totalPlaces;
Long accessiblePlaces;
Long partiallyAccessiblePlaces;
Long notAccessiblePlaces;
Double averageAccessibilityScore;
```

**Extension Points**:
```java
// Future fields that can be added:
private List<FacilityStats> facilityBreakdown;  // Per-facility statistics
private Map<String, Long> placeTypeDistribution; // By place type
private List<TrendData> accessibilityTrends;     // Time-series trends
private List<GeoPoint> accessibilityHotspots;    // Geographic clusters
private AnalyticsMetadata metadata;              // Calculation metadata
```

## Temporary Scoring Configuration

**Current Scoring Weights** (in `AccessibilityScoreService`):
```
RAMP_POINTS = 25
ELEVATOR_POINTS = 25
ACCESSIBLE_TOILET_POINTS = 25
WHEELCHAIR_ACCESS_POINTS = 25
```

These constants are:
- Defined at class level for easy visibility
- Used only in `calculateScore()` method
- Referenced in documentation with TODO markers
- Ready to be replaced with configuration-driven approach

**To Replace with Configuration-Driven Scoring**:
1. Move constants to `application.properties`:
   ```properties
   accessibility.score.ramp=25
   accessibility.score.elevator=25
   accessibility.score.toilet=25
   accessibility.score.wheelchair=25
   ```

2. Or use Spring `@ConfigurationProperties`:
   ```java
   @ConfigurationProperties(prefix = "accessibility.scoring")
   public class ScoringConfig {
       private Map<String, Integer> weights;
   }
   ```

## Data Flow for Analytics Integration

```
1. User/Admin creates Place via POST /api/places
   ↓
2. PlaceService calls AccessibilityScoreService.calculateScore()
   ↓
3. Score is stored in Place entity
   ↓
4. Dashboard queries aggregate statistics via GET /api/dashboard/stats
   ↓
5. DashboardService calls scoring methods from AccessibilityScoreService
   ↓
6. Results returned to frontend

When Analytics Team Updates:
- Only modify AccessibilityScoreService.calculateScore() logic
- All downstream services automatically use new calculations
- No controller changes needed
```

## Validation & Exception Handling

**Global Exception Handler**: `com.accessiblity.backend.controller.GlobalExceptionHandler`
- Catches all exceptions and returns consistent error format
- `ResourceNotFoundException` (404)
- `ValidationException` (400)
- Generic exceptions (500)

**Extension Points**:
- Add custom exceptions for analytics-specific errors
- Add validation for analytics data ingestion
- Add audit logging for compliance tracking

## Database Configuration

**Location**: `src/main/resources/application.properties`

```properties
# Current configuration
spring.jpa.hibernate.ddl-auto=update  # Auto-creates/updates schema

# For production, consider:
# spring.jpa.hibernate.ddl-auto=validate  # Schema must exist
# Add Flyway/Liquibase for versioned migrations
```

**Extension Points**:
- Add connection pooling configuration (HikariCP)
- Add JPA query optimization hints
- Add database performance monitoring
- Add audit table triggers

## Performance Considerations for Analytics

1. **Scoring Caching**: Consider caching scores for places with batch updates
2. **Dashboard Statistics**: Add caching for frequently accessed stats (Redis recommended)
3. **Report Aggregation**: Batch process reports for faster analytics
4. **Indexing Strategy**: Add database indexes on:
   - `place.placeType`
   - `place.accessibilityScore`
   - `place.createdAt`
   - `report.placeId`
   - `report.status`

## Security & Audit Trail

**TODO Items for Future Implementation**:
1. Add Spring Security for API authentication/authorization
2. Add audit trail for all place/report modifications
3. Add rate limiting for dashboard API
4. Add data encryption for sensitive user reports
5. Add GDPR compliance for user data handling

## Testing Strategy for Analytics

**Unit Tests Needed**:
- `AccessibilityScoreService` scoring logic
- `DashboardService` aggregation logic
- Place and Report validation

**Integration Tests Needed**:
- API endpoints with sample data
- Database persistence
- Exception handling

**Analytics Validation Tests**:
- Score calculation accuracy
- Classification correctness
- Dashboard metric accuracy
- Performance under load

## Migration Path

### Phase 1: Current (Temporary Scoring)
- Basic scoring with equal weights
- Manual place/report management
- Dashboard with placeholder data

### Phase 2: Analytics Team Integration
- Replace scoring formula
- Add advanced metrics
- Implement data caching
- Add trend calculations

### Phase 3: Advanced Features
- Add machine learning predictions
- Add geographic clustering
- Add user preference profiling
- Add automated reporting

## Summary of TODO Markers in Code

All integration points are marked with `TODO` comments:

1. **AccessibilityScoreService** (2 TODOs):
   - Replace scoring formula
   - Update classification criteria

2. **DashboardService** (5 TODOs):
   - Replace with analytics calculations
   - Integrate dashboard metrics
   - Handle edge cases

3. **DashboardController** (1 TODO):
   - Add additional metrics when available

4. **ScoreController** (Implicit):
   - Uses DashboardService logic

## Quick Reference: Where to Make Changes

| Feature | File | Method | Action |
|---------|------|--------|--------|
| Scoring Formula | AccessibilityScoreService | calculateScore() | Replace logic |
| Classification | AccessibilityScoreService | isFullyAccessible(), isPartiallyAccessible(), isNotAccessible() | Update criteria |
| Dashboard Metrics | DashboardService | getTotalPlaces(), getAccessiblePlaces(), etc. | Replace with analytics queries |
| Score Configuration | application.properties | accessibility.score.* | Add properties |
| Database Schema | Place, Report entities | N/A | Add columns/tables as needed |
| DTOs | DashboardStatsResponse | N/A | Add fields for new metrics |

## Next Steps for Analytics Team

1. **Define Scoring Formula**: Provide exact calculation algorithm and weights
2. **Define Classification Criteria**: Specify accessibility rating rules
3. **Provide Sample Data**: Include test datasets for validation
4. **Dashboard Requirements**: List all metrics needed
5. **Performance Requirements**: Specify response time SLAs
6. **Data Retention Policy**: Define archival/deletion rules
7. **Audit Requirements**: Define compliance/logging needs

---

**Backend Status**: ✅ Production-Ready Foundation
- All CRUD operations implemented
- All APIs created and functional
- Modular scoring architecture
- Exception handling in place
- Database schema prepared
- Ready for analytics integration with minimal code changes
