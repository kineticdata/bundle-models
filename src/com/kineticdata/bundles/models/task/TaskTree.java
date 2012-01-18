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
public class TaskTree {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_TSK_TREE";
    // Specify the fields that are used by this model
    public static final String FIELD_ID = "179";
    public static final String FIELD_NAME = "700001000";
    public static final String FIELD_SOURCE_ID = "700000800";
    public static final String FIELD_TYPE = "700000999";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[] {
        FIELD_NAME, FIELD_SOURCE_ID, FIELD_TYPE};
    // Specify the fields that should be used for default sorting (the model 
    // will use the Remedy form default sort order if this array is empty).
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[] {};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

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
    public TaskTree(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<TaskTree> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<TaskTree> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<TaskTree> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new TaskTree(context, entry));
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
    public static TaskTree findSingle(HelperContext context, String qualification) {
        // Declare the result
        TaskTree result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new TaskTree(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static TaskTree findById(HelperContext context, String id) {
        // Build the qualification
        String qualification = "'"+FIELD_ID+"' = \""+id+"\"";
        // Return the results of the query
        return findSingle(context, qualification);
    }

    public static List<TaskTree> findBySource(HelperContext context, String source, String templateId) {
        // Build the qualification
        String qualification = "'"+FIELD_SOURCE_ID+"' = \""+templateId+"\"";
        // Return the results of the query
        return find(context, qualification);
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getId() {return entry.getEntryFieldValue(FIELD_ID);}
    public String getName() {return entry.getEntryFieldValue(FIELD_NAME);}
    public String getSourceId() {return entry.getEntryFieldValue(FIELD_SOURCE_ID);}
    public String getType() {
        String type = entry.getEntryFieldValue(FIELD_TYPE);
        // If the tree was created prior to 5.0.3 and has a blank type, manually set it
        if (type == null || type.isEmpty()) {type = "Complete";}
        // Return the type
        return type;
    }
}
