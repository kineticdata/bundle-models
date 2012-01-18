package com.kineticdata.bundles.models.task;

// Import the necessary Java core classes
import java.util.*;
// Import the Kinetic ArsHelpers library
import com.kd.arsHelpers.*;
// Import the ArsBase class
import com.kineticdata.bundles.models.ArsBase;

/**
 * This is a special model that does not actually have an associated data
 * record.  It is used to represent the collection of Task instance records that
 * represent a single execution of a task tree.
 */
public class TaskTraversal {
    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the instance variables
    private String source;
    private String sourceId;
    private String treeId;
    private String treeName;
    // Declare the associations for this record
    private TaskTree tree;
    private List<Task> tasks;

    /***************************************************************************
     * CONSTRUCTORS
     **************************************************************************/

    /**
     * Create a new instance of the record and store the HelperContext that was
     * used to retrieve it (so that the same context can be used for any methods
     * that get records from associations) and the SimpleEntry object that
     * represents the Ars record.
     *
     * @param context
     */
    public TaskTraversal(HelperContext context, String source, String sourceId, String treeId, String treeName) {
        this.context = context;
        this.source = source;
        this.sourceId = sourceId;
        this.treeId = treeId;
        this.treeName = treeName;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static List<TaskTraversal> findBySource(HelperContext context, String source, String sourceId) {
        // Initialize a map to keep track of Traversals (by tree id)
        Map<String,TaskTraversal> traversalMap = new LinkedHashMap();
        // Retrieve a full list of tasks
        List<Task> tasks = Task.findBySource(context, source, sourceId);
        // For each task associated with the source
        for (Task task : tasks) {
            // Retrieve the traversal associated to this instance from the map
            TaskTraversal traversal = traversalMap.get(task.getTreeId());
            // If a traversal for this task doesn't exist yet
            if (traversal == null) {
                // Create the traversal
                traversal = new TaskTraversal(context, source, sourceId, task.getTreeId(), task.getTreeName());
                // Add the traversal to the map
                traversalMap.put(traversal.getTreeId(), traversal);
            }
            // Add the task to the traversal
            traversal.addTask(task);
        }
        // Returnt the list ot Task traversals
        return new ArrayList(traversalMap.values());
    }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public void addTask(Task task) {
        if (tasks == null) {tasks = new ArrayList();}
        tasks.add(task);
    }

    public List<Task> getTasks() {
        // If the association has not been retrieved yet
        if (tasks == null) {
            // Build the association associated records
            tasks = Task.findBySource(context, source, sourceId);
        }
        // Return the associated records
        return tasks;
    }

    public TaskTree getTree() {
        // If the association has not been retrieved yet
        if (tree == null) {
            // Build the association associated records
            tree = TaskTree.findById(context, treeId);
        }
        // Return the associated records
        return tree;
    }

    public void setTree(TaskTree tree) {
        this.tree = tree;
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getSource() {return source;}
    public String getSourceId() {return sourceId;}
    public String getTreeId() {return treeId;}
    public String getTreeName() {return treeName;}
    public String getTreeType() {return getTree().getType();}
}
