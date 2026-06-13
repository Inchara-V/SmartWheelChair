package com.accessiblity.backend.entity;

/**
 * Accessibility status classification based on score.
 * ACCESSIBLE: Score >= 80 (Green marker)
 * PARTIAL: Score 50-79 (Yellow marker)
 * NOT_ACCESSIBLE: Score < 50 (Red marker)
 */
public enum AccessibilityStatus {
    ACCESSIBLE,
    PARTIAL,
    NOT_ACCESSIBLE
}
