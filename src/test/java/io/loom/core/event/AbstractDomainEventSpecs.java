package io.loom.core.event;

import io.loom.core.entity.VersionedEntity;

import java.time.ZonedDateTime;
import java.util.Random;
import java.util.UUID;

import org.junit.Assert;
import org.junit.Test;
import org.mockito.Mockito;

public class AbstractDomainEventSpecs {
    public class IssueCreatedForTesting extends AbstractDomainEvent {
        public IssueCreatedForTesting() {
        }

        public IssueCreatedForTesting(
                UUID aggregateId, long version, ZonedDateTime occurrenceTime) {
            super(aggregateId, version, occurrenceTime);
        }
    }

    @Test
    public void constructor_has_guard_clause_for_null_aggregateId() {
        // Arrange
        UUID aggregateId = null;

        // Act
        IllegalArgumentException expected = null;
        try {
            new IssueCreatedForTesting(aggregateId, 1, ZonedDateTime.now());
        } catch (IllegalArgumentException e) {
            expected = e;
        }

        // Assert
        Assert.assertNotNull(expected);
        Assert.assertTrue(
                "The error message should contain the name of the parameter 'aggregateId'.",
                expected.getMessage().contains("'aggregateId'"));
    }

    @Test
    public void constructor_has_guard_clause_for_minimum_value_of_version() {
        // Arrange
        long version = 0;

        // Act
        IllegalArgumentException expected = null;
        try {
            new IssueCreatedForTesting(UUID.randomUUID(), version, ZonedDateTime.now());
        } catch (IllegalArgumentException e) {
            expected = e;
        }

        // Assert
        Assert.assertNotNull(expected);
        Assert.assertTrue(
                "The error message should contain the name of the parameter 'version'.",
                expected.getMessage().contains("'version'"));
    }

    @Test
    public void constructor_has_guard_clause_for_null_occurrenceTime() {
        // Arrange
        ZonedDateTime occurrenceTime = null;

        // Act
        IllegalArgumentException expected = null;
        try {
            new IssueCreatedForTesting(UUID.randomUUID(), 1, occurrenceTime);
        } catch (IllegalArgumentException e) {
            expected = e;
        }

        // Assert
        Assert.assertNotNull(expected);
        Assert.assertTrue(
                "The error message should contain the name of the parameter 'occurrenceTime'.",
                expected.getMessage().contains("'occurrenceTime'"));
    }

    @Test
    public void constructor_sets_header_properties_correctly() {
        // Arrange
        UUID aggregateId = UUID.randomUUID();
        Random random = new Random();
        long version = random.nextInt(Integer.MAX_VALUE) + 1L;
        ZonedDateTime occurrenceTime = ZonedDateTime.now().plusNanos(random.nextInt());

        // Act
        IssueCreatedForTesting sut = new IssueCreatedForTesting(
                aggregateId, version, occurrenceTime);

        // Assert
        Assert.assertEquals(aggregateId, sut.getAggregateId());
        Assert.assertEquals(version, sut.getVersion());
        Assert.assertEquals(occurrenceTime, sut.getOccurrenceTime());
    }

    @Test
    public void setHeaderProperties_canSetHeaderProperties_false() {
        // Arrange
        VersionedEntity versionedEntity = Mockito.mock(VersionedEntity.class);
        IssueCreatedForTesting sut = Mockito.spy(new IssueCreatedForTesting());
        Mockito.when(sut.canSetHeaderProperties()).thenReturn(false);

        // Act
        IllegalStateException expected = null;
        try {
            sut.setHeaderProperties(versionedEntity);
        } catch (IllegalStateException e) {
            expected = e;
        }

        // Assert
        Assert.assertNotNull(expected);
        Assert.assertTrue(expected.getMessage()
                .equals("The state of this instance can not be set to a header properties."));
    }

    @Test
    public void setHeaderProperties_has_guard_clause_for_null_aggregateId() {
        // Arrange
        VersionedEntity versionedEntity = Mockito.mock(VersionedEntity.class);
        Mockito.when(versionedEntity.getId()).thenReturn(null);
        Mockito.when(versionedEntity.getVersion()).thenReturn(1L);

        IssueCreatedForTesting sut = new IssueCreatedForTesting();

        // Act
        IllegalArgumentException expected = null;
        try {
            sut.setHeaderProperties(versionedEntity);
        } catch (IllegalArgumentException e) {
            expected = e;
        }

        // Assert
        Assert.assertNotNull(expected);
        Assert.assertTrue(
                "The error message should contain the name of the parameter 'aggregateId'.",
                expected.getMessage().contains("'aggregateId'"));
    }

    @Test
    public void setHeaderProperties_has_guard_clause_for_minimum_value_of_version() {
        // Arrange
        VersionedEntity versionedEntity = Mockito.mock(VersionedEntity.class);
        Mockito.when(versionedEntity.getId()).thenReturn(UUID.randomUUID());
        Mockito.when(versionedEntity.getVersion()).thenReturn(0L);

        IssueCreatedForTesting sut = new IssueCreatedForTesting();

        // Act
        IllegalArgumentException expected = null;
        try {
            sut.setHeaderProperties(versionedEntity);
        } catch (IllegalArgumentException e) {
            expected = e;
        }

        // Assert
        Assert.assertNotNull(expected);
        Assert.assertTrue(
                "The error message should contain the name of the parameter 'version'.",
                expected.getMessage().contains("'version'"));
    }

    @Test
    public void setHeaderProperties_sets_header_properties_correctly() {
        // Arrange
        UUID aggregateId = UUID.randomUUID();
        Random random = new Random();
        long version = random.nextInt(Integer.MAX_VALUE) + 1L;
        VersionedEntity versionedEntity = Mockito.mock(VersionedEntity.class);
        Mockito.when(versionedEntity.getId()).thenReturn(aggregateId);
        Mockito.when(versionedEntity.getVersion()).thenReturn(version);

        IssueCreatedForTesting sut = new IssueCreatedForTesting();

        // Act
        sut.setHeaderProperties(versionedEntity);

        // Assert
        Assert.assertEquals(aggregateId, sut.getAggregateId());
        Assert.assertEquals(version, sut.getVersion());
        long occurrenceTime = sut.getOccurrenceTime().toInstant().toEpochMilli();
        long after = ZonedDateTime.now().toInstant().toEpochMilli();
        long before = after - 1000;
        Assert.assertTrue(after >= occurrenceTime);
        Assert.assertTrue(before <= occurrenceTime);
    }

    @Test
    public void canSetHeaderProperties_returns_true_constructor_no_args() {
        // Arrange
        IssueCreatedForTesting sut = new IssueCreatedForTesting();

        // Act
        boolean expected = sut.canSetHeaderProperties();

        // Assert
        Assert.assertTrue(expected);
    }

    @Test
    public void canSetHeaderProperties_returns_false_constructor_all_header_properties_args() {
        // Arrange
        IssueCreatedForTesting sut = new IssueCreatedForTesting(
                UUID.randomUUID(), 1L, ZonedDateTime.now());

        // Act
        boolean expected = sut.canSetHeaderProperties();

        // Assert
        Assert.assertFalse(expected);
    }

    @Test
    public void canSetHeaderProperties_returns_false_after_set_header_properties() {
        // Arrange
        VersionedEntity versionedEntity = Mockito.mock(VersionedEntity.class);
        Mockito.when(versionedEntity.getId()).thenReturn(UUID.randomUUID());
        Mockito.when(versionedEntity.getVersion())
                .thenReturn(new Random().nextInt(Integer.MAX_VALUE) + 1L);
        IssueCreatedForTesting sut = new IssueCreatedForTesting();
        sut.setHeaderProperties(versionedEntity);

        // Act
        boolean expected = sut.canSetHeaderProperties();

        // Assert
        Assert.assertFalse(expected);
    }
}
