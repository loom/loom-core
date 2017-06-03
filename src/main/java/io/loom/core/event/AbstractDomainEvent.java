package io.loom.core.event;

import java.time.ZonedDateTime;
import java.util.UUID;

public abstract class AbstractDomainEvent implements DomainEvent {
    private final UUID aggregateId;
    private final long version;
    private final ZonedDateTime occurrenceTime;

    protected AbstractDomainEvent(UUID aggregateId, long version) {
        this(aggregateId, version, ZonedDateTime.now());
    }

    protected AbstractDomainEvent(UUID aggregateId, long version, ZonedDateTime occurrenceTime) {
        if (aggregateId == null) {
            throw new IllegalArgumentException("The parameter 'aggregateId' cannot be null.");
        }
        if (version < 1) {
            throw new IllegalArgumentException("The parameter 'version' must be greater than 0.");
        }
        if (occurrenceTime == null) {
            throw new IllegalArgumentException("The parameter 'occurrenceTime' cannot be null.");
        }

        this.aggregateId = aggregateId;
        this.version = version;
        this.occurrenceTime = occurrenceTime;
    }

    @Override
    public final UUID getAggregateId() {
        return this.aggregateId;
    }

    @Override
    public final long getVersion() {
        return this.version;
    }

    @Override
    public final ZonedDateTime getOccurrenceTime() {
        return this.occurrenceTime;
    }
}
