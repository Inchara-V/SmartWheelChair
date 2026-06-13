# 🔌 Extension Points Reference - Quick Lookup

## Extension Point Registry

All locations where analytics team needs to integrate are listed below. Each extension point includes file location, current implementation, and what to change.

---

## 1️⃣ CRITICAL: Accessibility Scoring Algorithm

**Priority**: HIGHEST - Affects all calculations

**File**: `src/main/java/com/accessiblity/backend/service/AccessibilityScoreService.java`

**Method**: `calculateScore(Place place)`

**Current Implementation** (Lines 32-48):
```java
// Temporary scoring constants - will be replaced by analytics team
private static final int RAMP_POINTS = 25;
private static final int ELEVATOR_POINTS = 25;
private static final int ACCESSIBLE_TOILET_POINTS = 25;
private static final int WHEELCHAIR_ACCESS_POINTS = 25;

public int calculateScore(Place place) {
    int score = 0;
    if (place.getHasRamp() != null && place.getHasRamp()) {
        score += RAMP_POINTS;
    }
    if (place.getHasElevator() != null && place.getHasElevator()) {
        score += ELEVATOR_POINTS;
    }
    if (place.getHasAccessibleToilet() != null && place.getHasAccessibleToilet()) {
        score += ACCESSIBLE_TOILET_POINTS;
    }
    if (place.getHasWheelchairAccess() != null && place.getHasWheelchairAccess()) {
        score += WHEELCHAIR_ACCESS_POINTS;
    }
    return Math.min(score, 100);
}
```

**What to Change**:
- Remove temporary constants
- Replace entire method body with analytics formula
- Return value must be 0-100 integer
- No changes needed elsewhere - controllers will automatically use new scoring

**TODO Markers**: 2 TODOs in this method

**Integration Steps**:
1. Receive formula from analytics team
2. Implement in `calculateScore()` method
3. Test with sample data
4. Scores recalculate automatically

---

## 2️⃣ CRITICAL: Accessibility Classification

**Priority**: HIGHEST - Used in dashboard and reporting

**File**: `src/main/java/com/accessiblity/backend/service/AccessibilityScoreService.java`

**Methods**:
- `isFullyAccessible(Place place)` (Lines 52-57)
- `isPartiallyAccessible(Place place)` (Lines 64-75)
- `isNotAccessible(Place place)` (Lines 82-88)

**Current Implementation**:
```java
public boolean isFullyAccessible(Place place) {
    return Boolean.TRUE.equals(place.getHasRamp()) &&
           Boolean.TRUE.equals(place.getHasElevator()) &&
           Boolean.TRUE.equals(place.getHasAccessibleToilet()) &&
           Boolean.TRUE.equals(place.getHasWheelchairAccess());
}

public boolean isPartiallyAccessible(Place place) {
    int count = 0;
    if (Boolean.TRUE.equals(place.getHasRamp())) count++;
    if (Boolean.TRUE.equals(place.getHasElevator())) count++;
    if (Boolean.TRUE.equals(place.getHasAccessibleToilet())) count++;
    if (Boolean.TRUE.equals(place.getHasWheelchairAccess())) count++;
    return count > 0 && count < 4;
}

public boolean isNotAccessible(Place place) {
    return !Boolean.TRUE.equals(place.getHasRamp()) &&
           !Boolean.TRUE.equals(place.getHasElevator()) &&
           !Boolean.TRUE.equals(place.getHasAccessibleToilet()) &&
           !Boolean.TRUE.equals(place.getHasWheelchairAccess());
}
```

**What to Change**:
- Update classification criteria based on analytics rules
- Example: `score >= 75` instead of "all features"
- Classification affects dashboard categorization

**Where Used**:
- DashboardService.getAccessiblePlaces()
- DashboardService.getPartiallyAccessiblePlaces()
- DashboardService.getNotAccessiblePlaces()
- ScoreController.getPlaceScore()

**TODO Markers**: 3 TODOs total

---

## 3️⃣ HIGH: Dashboard Statistics Calculations

**Priority**: HIGH - Frontend depends on these

**File**: `src/main/java/com/accessiblity/backend/service/DashboardService.java`

**Methods to Modify**:
1. `getTotalPlaces()` (Lines 22-25)
2. `getAccessiblePlaces()` (Lines 32-42)
3. `getPartiallyAccessiblePlaces()` (Lines 49-59)
4. `getNotAccessiblePlaces()` (Lines 66-76)
5. `getAverageAccessibilityScore()` (Lines 83-89)

**Current Implementation Examples**:
```java
public Long getTotalPlaces() {
    return placeRepository.count();
}

public Long getAccessiblePlaces() {
    return placeRepository.findAll().stream()
            .filter(scoreService::isFullyAccessible)
            .count();
}

public Double getAverageAccessibilityScore() {
    return placeRepository.findAll().stream()
            .mapToInt(place -> place.getAccessibilityScore() != null ? 
                      place.getAccessibilityScore() : 0)
            .average()
            .orElse(0.0);
}
```

**What to Change**:
- Replace with analytics team's calculations
- Can support filtering, date ranges, demographic breakdowns
- Can incorporate machine learning predictions
- Can add geographic clustering

**Performance Note**: 
- Current implementation loads all places into memory
- For production, use database aggregation queries
- Consider caching results (Redis)

**TODO Markers**: 12 TODOs total (3 per method for 4 methods)

**API Endpoint That Uses This**:
```
GET /api/dashboard/stats
```

---

## 4️⃣ MEDIUM: Place Entity Extensions

**Priority**: MEDIUM - Required for new features

**File**: `src/main/java/com/accessiblity/backend/entity/Place.java`

**Current Fields** (Lines 20-55):
```java
private Long id;
private String name;
private String address;
private Double latitude;
private Double longitude;
private String description;
private PlaceType placeType;
private Boolean hasRamp;
private Boolean hasElevator;
private Boolean hasAccessibleToilet;
private Boolean hasWheelchairAccess;
private Integer accessibilityScore;
private Long createdAt;
private Long updatedAt;
```

**What to Add**:
- New accessibility features as identified
- Feature condition ratings (Good/Fair/Poor)
- Metadata fields
- User ratings
- Verification status
- Last updated info

**Example Addition**:
```java
@Column(name = "ramp_condition")
private String rampCondition;  // GOOD, FAIR, POOR

@Column(name = "user_rating")
private Double userRating;  // 1-5 stars

@Column(name = "verified_by")
private String verifiedBy;  // Admin username
```

**Database Impact**: 
- Non-breaking schema changes
- Use `ALTER TABLE` to add new columns
- Hibernate will handle migration with `ddl-auto=update`

---

## 5️⃣ MEDIUM: Report Processing Pipeline

**Priority**: MEDIUM - User feedback integration

**File**: `src/main/java/com/accessiblity/backend/service/ReportService.java`

**Current State**: Reports are stored but not processed

**What to Add**:
1. Automatic place updates based on approved reports
2. Report aggregation for analytics
3. Duplicate report detection
4. Trend analysis
5. Anomaly detection

**Extension Points**:
```java
// Add new methods to ReportService
public void processApprovedReport(Report report) {
    // Auto-update place based on report
}

public void generateReportInsights() {
    // Aggregate reports for trends
}

public boolean isDuplicateReport(Report report) {
    // Detect duplicate submissions
}
```

**Related File**: `src/main/java/com/accessiblity/backend/entity/Report.java`

---

## 6️⃣ MEDIUM: Dashboard Response DTO

**Priority**: MEDIUM - Add new metrics

**File**: `src/main/java/com/accessiblity/backend/dto/DashboardStatsResponse.java`

**Current Fields** (Lines 9-14):
```java
private Long totalPlaces;
private Long accessiblePlaces;
private Long partiallyAccessiblePlaces;
private Long notAccessiblePlaces;
private Double averageAccessibilityScore;
```

**What to Add**:
```java
// Facility-specific statistics
private Map<String, Long> facilityBreakdown;

// Place type distribution
private Map<String, Long> placeTypeDistribution;

// Geographic data
private List<GeoPoint> accessibilityHotspots;

// Time-series trends
private List<TrendData> accessibilityTrends;

// Metadata
private AnalyticsMetadata metadata;
```

**Example New Structure**:
```java
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class DashboardStatsResponse {
    private Long totalPlaces;
    private Long accessiblePlaces;
    private Long partiallyAccessiblePlaces;
    private Long notAccessiblePlaces;
    private Double averageAccessibilityScore;
    
    // New fields
    private FacilityStats facilityStats;
    private Map<String, Long> typeDistribution;
    private List<TrendPoint> trendData;
    private Long lastUpdatedAt;
}
```

---

## 7️⃣ LOW: Database Schema Extensions

**Priority**: LOW - For advanced analytics

**File**: `src/main/resources/application.properties`

**Current Setting** (Line 7):
```properties
spring.jpa.hibernate.ddl-auto=update
```

**Suggested Future Tables**:

```sql
-- Time-series snapshots for trend analysis
CREATE TABLE accessibility_metrics_snapshot (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    snapshot_date DATETIME,
    place_id BIGINT,
    score INT,
    accessibility_category VARCHAR(50),
    created_at DATETIME,
    FOREIGN KEY (place_id) REFERENCES places(id)
);

-- Audit trail for compliance
CREATE TABLE accessibility_audit_log (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    place_id BIGINT,
    old_score INT,
    new_score INT,
    changed_fields VARCHAR(255),
    changed_by VARCHAR(100),
    changed_at DATETIME,
    FOREIGN KEY (place_id) REFERENCES places(id)
);

-- Analytics cache for performance
CREATE TABLE analytics_cache (
    id BIGINT PRIMARY KEY AUTO_INCREMENT,
    metric_name VARCHAR(100),
    metric_value DOUBLE,
    calculated_at DATETIME,
    valid_until DATETIME
);
```

**To Implement**:
1. Create migration scripts
2. Add new entity classes
3. Add new repository interfaces
4. Update DashboardService queries

---

## 8️⃣ LOW: Application Configuration

**Priority**: LOW - Optional enhancements

**File**: `src/main/resources/application.properties`

**Current Configuration** (19 lines):
```properties
spring.application.name=accessiblity_backend
server.port=8080
spring.datasource.url=jdbc:mysql://localhost:3306/accessibility_db
spring.datasource.username=root
spring.datasource.password=root
spring.jpa.database-platform=org.hibernate.dialect.MySQL8Dialect
spring.jpa.hibernate.ddl-auto=update
spring.jpa.show-sql=false
spring.jpa.properties.hibernate.format_sql=true
logging.level.root=INFO
logging.level.com.accessiblity.backend=DEBUG
spring.data.rest.basePath=/api
```

**What to Add**:
```properties
# Scoring Configuration
accessibility.scoring.weights.ramp=25
accessibility.scoring.weights.elevator=25
accessibility.scoring.weights.toilet=25
accessibility.scoring.weights.wheelchair=25

# Dashboard Caching
spring.cache.type=redis
spring.redis.host=localhost
spring.redis.port=6379

# Database Connection Pool
spring.datasource.hikari.maximum-pool-size=20
spring.datasource.hikari.minimum-idle=5

# Logging
logging.level.org.springframework.web=DEBUG
logging.file.name=logs/app.log
```

---

## 9️⃣ Controller Integration Point

**Priority**: LOW - Usually doesn't change

**File**: `src/main/java/com/accessiblity/backend/controller/DashboardController.java`

**Current Implementation** (Lines 21-35):
```java
@GetMapping("/stats")
public ResponseEntity<DashboardStatsResponse> getDashboardStats() {
    DashboardStatsResponse stats = DashboardStatsResponse.builder()
            .totalPlaces(dashboardService.getTotalPlaces())
            .accessiblePlaces(dashboardService.getAccessiblePlaces())
            .partiallyAccessiblePlaces(dashboardService.getPartiallyAccessiblePlaces())
            .notAccessiblePlaces(dashboardService.getNotAccessiblePlaces())
            .averageAccessibilityScore(dashboardService.getAverageAccessibilityScore())
            .build();

    return ResponseEntity.ok(stats);
}
```

**What to Change**:
- Add new fields when DashboardStatsResponse is extended
- Add additional endpoints if new metrics needed
- Usually doesn't need modification if using existing DTO fields

**New Endpoint Example**:
```java
@GetMapping("/trends")
public ResponseEntity<TrendAnalysis> getTrendAnalysis() {
    // Return trend data from DashboardService
}
```

---

## 🔟 Score Controller Extension

**Priority**: LOW - Enhanced scoring display

**File**: `src/main/java/com/accessiblity/backend/controller/ScoreController.java`

**Current Endpoint** (Lines 29-55):
```java
@GetMapping("/place/{placeId}")
public ResponseEntity<Map<String, Object>> getPlaceScore(@PathVariable Long placeId)
```

**What to Add**:
```java
@GetMapping("/place/{placeId}/history")
public ResponseEntity<List<ScoreHistory>> getScoreHistory(@PathVariable Long placeId) {
    // Return historical scores
}

@GetMapping("/place/{placeId}/insights")
public ResponseEntity<ScoringInsights> getScoringInsights(@PathVariable Long placeId) {
    // Return detailed scoring breakdown
}

@GetMapping("/benchmarks")
public ResponseEntity<BenchmarkComparison> getBenchmarks() {
    // Return how place compares to similar places
}
```

---

## 📋 Extension Priority Matrix

| Priority | File | Method | Effort | Impact |
|----------|------|--------|--------|--------|
| 🔴 CRITICAL | AccessibilityScoreService | calculateScore() | 2-4h | HIGHEST |
| 🔴 CRITICAL | AccessibilityScoreService | is*Accessible() | 1-2h | HIGHEST |
| 🟠 HIGH | DashboardService | get*() | 4-6h | HIGH |
| 🟠 HIGH | DashboardStatsResponse | Add fields | 2-3h | HIGH |
| 🟡 MEDIUM | Place | Add columns | 2-3h | MEDIUM |
| 🟡 MEDIUM | Report | Add processing | 4-8h | MEDIUM |
| 🟢 LOW | Configuration | Add properties | 1h | LOW |
| 🟢 LOW | Controllers | Add endpoints | 2-4h | LOW |

---

## 🚀 Quick Integration Checklist

When analytics team is ready to integrate:

- [ ] Scoring algorithm ready
- [ ] Classification criteria defined
- [ ] Dashboard metrics calculated
- [ ] Sample data provided
- [ ] Performance requirements specified
- [ ] Analytics team has access to backend code

**Integration Steps**:
1. Update AccessibilityScoreService
2. Update DashboardService methods
3. Extend Place entity if needed
4. Add new DTOs for additional fields
5. Create database migrations
6. Test with sample data
7. Deploy to staging
8. Verify with real data

---

## 📞 Questions for Analytics Team

Before integration:
1. What is the exact scoring formula?
2. How should accessibility be classified?
3. What metrics are required for dashboard?
4. Are there geographic/demographic breakdowns needed?
5. What's the expected data volume?
6. Should scores be weighted by place type?
7. Should user reports auto-update places?
8. What's the required response time for stats API?
9. Should there be caching?
10. Are there trending/historical requirements?

---

**File**: Extension Points Reference  
**Version**: 1.0  
**Status**: Production Ready  
**Last Updated**: 2026-06-12
