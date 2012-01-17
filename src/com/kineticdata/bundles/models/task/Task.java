package com.kineticdata.bundles.models.task;

// Import the necessary Java core classes
import java.util.*;
// Import the Kinetic ArsHelpers library
import com.kd.arsHelpers.*;
// Import the ArsBase class
import com.kineticdata.bundles.models.ArsBase;

/**
 *
 */
public class Task {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_TSK_Instance";
    // Specify the fields that are used by this model
    public static final String FIELD_CREATE_DATE = "3";
    public static final String FIELD_ID = "179";
    public static final String FIELD_MODIFIED_DATE = "6";
    public static final String FIELD_NAME = "700000810";
    public static final String FIELD_SOURCE = "700000840";
    public static final String FIELD_SOURCE_ID = "700000830";
    public static final String FIELD_STATUS = "7";
    public static final String FIELD_TREE_NAME = "700066802";
    public static final String FIELD_VISIBLE = "700000914";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[] {
        FIELD_CREATE_DATE, FIELD_ID, FIELD_MODIFIED_DATE, FIELD_NAME,
        FIELD_SOURCE_ID, FIELD_STATUS, FIELD_TREE_NAME};
    // Specify the fields that should be used for default sorting (the model
    // will use the Remedy form default sort order if this array is empty).
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[] {
        FIELD_CREATE_DATE};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

    // Declare the associations for this record
    private List<TaskMessage> messages;

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
     * @param entry
     */
    public Task(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Task> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Task> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Task> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Task(context, entry));
        }
        // Return the results
        return results;
    }

    /**
     * Generic find method to retrieve a single record using an arbitrary
     * qualification.  This method will return null (if no matching records were
     * found), the record, or raise a RuntimeException if there was more than
     * one record that matched the qualification.
     * @throws RuntimeException
     */
    public static Task findSingle(HelperContext context, String qualification) {
        // Declare the result
        Task result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Task(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static List<Task> findBySource(HelperContext context, String source, String sourceId) {
        // Build the qualification
        String qualification =
            "'"+FIELD_SOURCE+"' = \""+source+"\" AND "+
            "'"+FIELD_SOURCE_ID+"' = \""+sourceId+"\" AND"+
            "'"+FIELD_VISIBLE+"' = \"Yes\"";
        // Return the results of the query
        return find(context, qualification);
    }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public List<TaskMessage> getMessages() {
        // If the association has not been retrieved yet
        if (messages == null) {
            // Build the association associated records
            messages = TaskMessage.findByTaskId(context, this.getId());
        }
        // Return the associated records
        return messages;
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getCreateDate() {return entry.getEntryFieldValue(FIELD_CREATE_DATE);}
    public String getId() {return entry.getEntryFieldValue(FIELD_ID);}
    public String getModifiedDate() {return entry.getEntryFieldValue(FIELD_MODIFIED_DATE);}
    public String getName() {return entry.getEntryFieldValue(FIELD_NAME);}
    public String getSourceId() {return entry.getEntryFieldValue(FIELD_SOURCE_ID);}
    public String getStatus() {return entry.getEntryFieldValue(FIELD_STATUS);}
    public String getTreeName() {return entry.getEntryFieldValue(FIELD_TREE_NAME);}
}
