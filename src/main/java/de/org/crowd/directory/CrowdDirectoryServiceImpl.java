package de.org.crowd.directory;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.embedded.api.SearchRestriction;
import com.atlassian.crowd.exception.DirectoryNotFoundException;
import com.atlassian.crowd.exception.GroupNotFoundException;
import com.atlassian.crowd.exception.OperationFailedException;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupType;
import com.atlassian.crowd.search.EntityDescriptor;
import com.atlassian.crowd.search.builder.QueryBuilder;
import com.atlassian.crowd.search.query.entity.EntityQuery;
import com.atlassian.crowd.search.query.entity.GroupQuery;
import com.atlassian.crowd.search.query.entity.restriction.NullRestrictionImpl;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

import javax.inject.Inject;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

@Component
@ExportAsService
public class CrowdDirectoryServiceImpl implements CrowdDirectoryService {

    private static final Logger log = LoggerFactory.getLogger(CrowdDirectoryService.class);

    @ComponentImport
    private final DirectoryManager directoryManager;

    @Inject
    public CrowdDirectoryServiceImpl(final DirectoryManager directoryManager) {
        this.directoryManager = directoryManager;
    }

    @Override
    public Collection<Directory> getAllDirectories() {
        final EntityQuery<Directory> directoryEntityQuery = QueryBuilder.queryFor(Directory.class, EntityDescriptor.directory())
                .returningAtMost(EntityQuery.ALL_RESULTS);
        // for each directory
        List<Directory> directoryList = directoryManager.searchDirectories(directoryEntityQuery);
        return directoryManager.searchDirectories(directoryEntityQuery);
    }

    @Override
    @Deprecated
    public Group findGroupInDirectory(Directory directory, String groupName) {
        Group group = null;
        // EntityQuery<DirectoryGroup> query = QueryBuilder.queryFor(DirectoryGroup.class, EntityDescriptor.group()).returningAtMost(EntityQuery.MAX_MAX_RESULTS);
        try {
            group = directoryManager.findGroupByName(directory.getId(), groupName);
        } catch (GroupNotFoundException | DirectoryNotFoundException | OperationFailedException e) {
            e.printStackTrace();
        }
        return group;
    }

    @Override
    public boolean groupExistInDirectory(Directory directory, String groupName) {
        ArrayList<Group> groupList = new ArrayList<>(getAllGroupsInDirectory(directory.getId()));
        boolean contained = false;
        for (Group group : groupList) {
            if (group.getName().equals(groupName)) {
                log.debug("Directory: " + directory.getName() + " Groupsearch: " + groupName + " Group Name: " + group.getName());
                contained = true;
                break;
            }
        }
        return contained;
    }

    @Override
    public Collection<Group> getAllGroupsInDirectory(long id) {
        SearchRestriction restriction = NullRestrictionImpl.INSTANCE;
        final EntityQuery<Group> query = new GroupQuery<>(Group.class, GroupType.GROUP, restriction, 0, -1);
        try {
            return directoryManager.searchGroups(id, query);
        } catch (DirectoryNotFoundException | OperationFailedException e) {
            e.printStackTrace();
            return new ArrayList<>();
        }
    }
}