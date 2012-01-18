package com.kineticdata.bundles.models.request;

// Import the necessary Java core classes
import java.util.*;
// Import the Kinetic ArsHelpers library
import com.kd.arsHelpers.*;
// Import the ArsBase class
import com.kineticdata.bundles.models.ArsBase;
// Import the Task association classes
import com.kineticdata.bundles.models.task.*;

/**
 *
 */
public class Submission {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_SRV_CustomerSurvey";
    // Specify the fields that are used by this model
    public static final String FIELD_CATALOG_NAME = "600000500";
    public static final String FIELD_CREATE_DATE = "3";
    public static final String FIELD_ID = "179";
    public static final String FIELD_NOTES = "600003021";
    public static final String FIELD_REQUEST_ID = "536870913";
    public static final String FIELD_SUBMIT_TYPE = "700088475";
    public static final String FIELD_SUBMITTER = "2";
    public static final String FIELD_TEMPLATE_ID = "700000800";
    public static final String FIELD_TEMPLATE_NAME = "700001000";
    public static final String FIELD_REQUEST_CLOSED_DATE = "700088489";
    public static final String FIELD_LOOKUP_ID = "700002450";
    public static final String FIELD_ORIGINATING_ID = "600000310";
    // Represents the status of the Submission.
    public static final String FIELD_STATUS = "7";
    // Represents the status of the Request including task workflow (This is
    // a Enumeration field that allows values of 'Open' or 'Closed' and is
    // typically set by the KineticRequest_Submission_Close handler).
    public static final String FIELD_REQUEST_STATUS = "700089541";
    // Represents the state of the Request (This is a free text field that
    // is intended to communicate the overall progress of the Request.  This
    // value is typically set by the KineticRequest_Submission_UpdateStatus
    // task handler).
    public static final String FIELD_VALIDATION_STATUS = "700002400";

    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[] {
        FIELD_CATALOG_NAME, FIELD_CREATE_DATE, FIELD_ID, FIELD_REQUEST_ID,
        FIELD_NOTES, FIELD_SUBMIT_TYPE, FIELD_SUBMITTER, FIELD_TEMPLATE_ID,
        FIELD_TEMPLATE_NAME, FIELD_STATUS, FIELD_REQUEST_STATUS,
        FIELD_REQUEST_CLOSED_DATE, FIELD_VALIDATION_STATUS};
    // Specify the fields that should be used for default sorting (the model
    // will use the Remedy form default sort order if this array is empty).
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[] {};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

    // Declare the associations for this record
    private List<Submission> children;
    private List<Submission> decendents;
    private Template template;
    private List<TaskTraversal> traversals;

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
    public Submission(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Submission> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Submission> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Submission> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Submission(context, entry));
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
    public static Submission findSingle(HelperContext context, String qualification) {
        // Declare the result
        Submission result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Submission(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static Submission findById(HelperContext context, String templateId) {
        // Build the qualification
        String qualification = "'"+FIELD_ID+"' = \""+templateId+"\"";
        // Return all results that match the qualification
        return findSingle(context, qualification);
    }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public List<Submission> getChildren() {
        // If the association has not been retrieved yet
        if (children == null) {
            // Build the qualification
            String qualification =
                "'"+FIELD_LOOKUP_ID+"' = \""+getId()+"\" AND "+
                "'"+FIELD_ID+"' != '"+FIELD_LOOKUP_ID+"'";
            // Build the association associated records
            children = find(context, qualification);
        }
        // Return the associated records
        return children;
    }

    public List<Submission> getDescendents() {
        // If the association has not been retrieved yet
        if (decendents == null) {
            // Build the qualification
            String qualification =
                "'"+FIELD_ORIGINATING_ID+"' = \""+getId()+"\" AND "+
                "'"+FIELD_ID+"' != '"+FIELD_ORIGINATING_ID+"'";
            // Build the association associated records
            decendents = find(context, qualification);
        }
        // Return the associated records
        return decendents;
    }

    public Template getTemplate() {
        // If the association has not been retrieved yet
        if (template == null) {
            // Load the association
            template = Template.findById(context, getTemplateId());
        }
        // Return the associated object
        return template;
    }

    public List<TaskTraversal> getTaskTraversals() {
        // If the association has not been retrieved yet
        if (traversals == null) {
            // Build a map of tree names to trees
            Map<String,TaskTree> treeMap = new LinkedHashMap();
            for (TaskTree tree : getTemplate().getTaskTrees()) {
                treeMap.put(tree.getName(), tree);
            }
            // Load the association
            traversals = TaskTraversal.findBySource(context, "Kinetic Request", getId());
            // Set the task trees
            for (TaskTraversal traversal : traversals) {
                traversal.setTree(treeMap.get(traversal.getTreeName()));
            }
        }
        // Return the associated object
        return traversals;
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getCatalogName() {return entry.getEntryFieldValue(FIELD_CATALOG_NAME);}
    public String getCreateDate() {return entry.getEntryFieldValue(FIELD_CREATE_DATE);}
    public String getId() {return entry.getEntryFieldValue(FIELD_ID);}
    public String getNotes() {return entry.getEntryFieldValue(FIELD_NOTES);}
    public String getRequestId() {return entry.getEntryFieldValue(FIELD_REQUEST_ID);}
    public String getSubmitType() {return entry.getEntryFieldValue(FIELD_SUBMIT_TYPE);}
    public String getTemplateId() {return entry.getEntryFieldValue(FIELD_TEMPLATE_ID);}
    public String getTemplateName() {return entry.getEntryFieldValue(FIELD_TEMPLATE_NAME);}
    public String getStatus() {return entry.getEntryFieldValue(FIELD_STATUS);}
    public String getRequestStatus() {return entry.getEntryFieldValue(FIELD_REQUEST_STATUS);}
    public String getRequestClosedDate() {return entry.getEntryFieldValue(FIELD_REQUEST_CLOSED_DATE);}
    public String getValiationStatus() {return entry.getEntryFieldValue(FIELD_VALIDATION_STATUS);}
}
