package io.loom.core.aggregate;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;

import io.loom.core.event.DomainEvent;
import io.loom.core.fixtures.InMemoryEventStore;
import io.loom.core.fixtures.Issue;
import io.loom.core.fixtures.IssueRepository;
import io.loom.core.store.EventStore;
import java.util.Iterator;
import java.util.Optional;
import java.util.UUID;
import org.junit.Test;

/**
 * Created by mhyeon.lee on 2017. 5. 3..
 */
public class RepositoryTest {
    @Test
    public void saveTest() {
        // Given
        EventStore eventStore = new InMemoryEventStore();
        Repository<Issue> repository = new IssueRepository(eventStore);
        Issue issue = new Issue(UUID.randomUUID(), "issue-title", "issue-content");
        issue.changeTitle("changed-title");

        // When
        repository.save(issue);

        // Then
        Iterator<DomainEvent> storedEvents = eventStore.getEvents(issue.getId()).iterator();
        assertEquals(1, storedEvents.next().getVersion());
        assertEquals(2, storedEvents.next().getVersion());
        assertFalse(storedEvents.hasNext());
    }

    @Test
    public void loadTest() {
        // Given
        EventStore eventStore = new InMemoryEventStore();
        Repository<Issue> repository = new IssueRepository(eventStore);
        Issue issue = new Issue(UUID.randomUUID(), "issue-title", "issue-content");
        issue.changeTitle("changed-title");
        issue.changeContent("changed-content");
        repository.save(issue);

        // When
        Optional<Issue> loadIssue = repository.load(issue.getId());

        // Then
        assertTrue(loadIssue.isPresent());
        assertEquals(issue, loadIssue.get());
        assertEquals(issue.getTitle(), loadIssue.get().getTitle());
        assertEquals(issue.getContent(), loadIssue.get().getContent());
    }
}
