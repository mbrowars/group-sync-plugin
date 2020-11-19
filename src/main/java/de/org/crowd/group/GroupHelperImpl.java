package de.org.crowd.group;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.exception.*;
import com.atlassian.crowd.manager.directory.DirectoryManager;
import com.atlassian.crowd.manager.directory.DirectoryPermissionException;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupTemplate;
import com.atlassian.plugin.spring.scanner.annotation.export.ExportAsService;
import com.atlassian.plugin.spring.scanner.annotation.imports.ComponentImport;
import org.springframework.stereotype.Component;

import javax.inject.Inject;

@Component
@ExportAsService
public class GroupHelperImpl implements GroupHelper {

    @ComponentImport
    private final DirectoryManager directoryManager;

    @Inject
    public GroupHelperImpl(final DirectoryManager directoryManager) {
        this.directoryManager = directoryManager;
    }

    @Override
    public void addGroup(GroupTemplate group, Directory directory) {
        try {
            directoryManager.addGroup(directory.getId(), group);
        } catch (InvalidGroupException | DirectoryPermissionException | OperationFailedException | DirectoryNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void addGroup(Group group, Directory directory) {
        try {
            directoryManager.addGroup(directory.getId(), new GroupTemplate(group.getName(), directory.getId()));
        } catch (InvalidGroupException | DirectoryPermissionException | OperationFailedException | DirectoryNotFoundException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void deleteGroup(String groupName, Directory directory) {

        try {
            directoryManager.removeGroup(directory.getId(), groupName);
        } catch (DirectoryPermissionException | DirectoryNotFoundException | OperationFailedException | ReadOnlyGroupException | GroupNotFoundException e) {
            e.printStackTrace();
        }
    }

}
