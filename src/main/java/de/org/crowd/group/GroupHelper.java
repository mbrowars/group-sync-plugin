package de.org.crowd.group;

import com.atlassian.crowd.embedded.api.Directory;
import com.atlassian.crowd.model.group.Group;
import com.atlassian.crowd.model.group.GroupTemplate;

public interface GroupHelper {

    /**
     * Adds a Group from a Template to a Directory
     * @param group GroupTemplate to be added
     * @param directory Directory to store the Group
     */
    void addGroup(GroupTemplate group, Directory directory);

    /**
     * Adds a Group to a Directory
     * @param group Group to be added
     * @param directory Directory to store the Group
     */
    void addGroup(Group group, Directory directory);

    /**
     * Deletes a Group in a given Directory
     * @param groupName Name of the Group
     * @param directory Directory to delete the Group in
     */
    void deleteGroup(String groupName, Directory directory);

}
