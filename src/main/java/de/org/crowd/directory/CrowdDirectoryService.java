package de.org.crowd.directory;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.model.group.Group;

import java.util.Collection;

public interface CrowdDirectoryService {
    Collection<Directory> getAllDirectories();

    /**
     * Deprecated - find Group in Directory might cause nullpointer exception
     * @param directory Directory to search in
     * @param groupName Name of the searched Group
     * @return Group if found
     */
    Group findGroupInDirectory(Directory directory, String groupName);

    /**
     * Checks if Group exists in a given Directory
     * @param directory  Directory to search in
     * @param groupName Name of the searched Group
     * @return True of Group is found, False if not
     */
    boolean groupExistInDirectory(Directory directory, String groupName);

    /**
     * Get all Groups of a Directory
     * @param id Id of the Directory
     * @return Collection of Groups inside the Directory
     */
    Collection<Group> getAllGroupsInDirectory(long id);
}

