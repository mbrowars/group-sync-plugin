package de.org.crowd.group.event;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.event.group.GroupCreatedEvent;
import com.atlassian.crowd.event.group.GroupDeletedEvent;
import com.atlassian.crowd.event.group.GroupUpdatedEvent;
import com.atlassian.crowd.model.group.GroupTemplate;
import com.atlassian.event.api.EventListener;
import de.org.crowd.directory.CrowdDirectoryService;
import de.org.crowd.group.GroupHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.Collection;

/**
 * GroupEventListener listens to GroupEvents inside Crowd
 * Handles Group creation, modification and deletes
 */
@Component
public class GroupEventListener {

    private final CrowdDirectoryService crowdDirectoryService;

    private final GroupHelper groupHelper;

    private static final Logger log = LoggerFactory.getLogger(GroupEventListener.class);


    @Inject
    public GroupEventListener(final GroupHelper groupHelper,
                              final CrowdDirectoryService crowdDirectoryService) {
        this.crowdDirectoryService = crowdDirectoryService;
        this.groupHelper = groupHelper;
    }

    @EventListener
    public synchronized void handleGroupCreation(GroupCreatedEvent event) {
        Collection<Directory> directoryList = crowdDirectoryService.getAllDirectories();
        directoryList.forEach(directory -> {
            if (!crowdDirectoryService.groupExistInDirectory(directory, event.getGroup().getName())) {
                //TODO: handle restrictions
                groupHelper.addGroup(new GroupTemplate(event.getGroup().getName(), directory.getId()), directory);
                log.info("Group :" + event.getGroup().getName() + " in Directory: " + directory.getName() + " has been created.");
            }
        });
    }

    @EventListener
    public synchronized void handleGroupUpdate(GroupUpdatedEvent event) {
        log.info("Group :" + event.getGroup().getName() + " in Directory: " + event.getDirectory().getName() + " has been updated.");
        // nothing to do here so far
    }

    @EventListener
    public synchronized void handleGroupDeleted(GroupDeletedEvent event) {
        for (Directory directory : crowdDirectoryService.getAllDirectories()) {
            if (crowdDirectoryService.groupExistInDirectory(directory, event.getGroupName())) {
                //TODO: handle restrictions
                groupHelper.deleteGroup(event.getGroupName(), directory);
                log.info("Group " + event.getGroupName() + " in Directory: " + directory.getName() + " has been deleted.");
            }
        }
    }

}
