package de.org.crowd.directory.event;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.event.directory.DirectoryCreatedEvent;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.event.api.EventListener;
import de.org.crowd.directory.CrowdDirectoryService;
import de.org.crowd.group.GroupHelper;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;

/**
 * DirectoryEventListener listening for Directory Events
 * handles Directory creation
 */
@Component
public class DirectoryEventListener {

    private static final Logger log = LoggerFactory.getLogger(DirectoryEventListener.class);

    private final CrowdDirectoryService crowdDirectoryService;

    private final GroupHelper groupHelper;

    @Inject
    public DirectoryEventListener(final CrowdDirectoryService crowdDirectoryService, final GroupHelper groupHelper) {
        this.crowdDirectoryService = crowdDirectoryService;
        this.groupHelper = groupHelper;
    }

    @EventListener
    public synchronized void handleDirectoryCreation(DirectoryCreatedEvent event) {
        log.info("Directory :" + event.getDirectory().getName() + " has been created.");
        //get directory id for getting groups
        long directoryId;
        ArrayList<Group> groups = null;
        for ( Directory directory : crowdDirectoryService.getAllDirectories()){
            if(!event.getDirectoryId().equals(directory.getId())) {
                directoryId = directory.getId();
                groups = new ArrayList<>(crowdDirectoryService.getAllGroupsInDirectory(directoryId));
                break;
            }
        }
        if(groups != null && !groups.isEmpty()){
            groups.forEach(group -> groupHelper.addGroup(group, event.getDirectory()));
            log.info(groups.size() + " Groups added to Directory: " + event.getDirectory().getName());
        }

    }
}
