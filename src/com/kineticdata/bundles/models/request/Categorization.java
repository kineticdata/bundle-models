package com.kineticdata.bundles.models.request;

// Import the necessary Java core classes
import java.util.*;
// Import the Kinetic ArsHelpers library
import com.kd.arsHelpers.*;
// Import the ArsBase class
import com.kineticdata.bundles.models.ArsBase;

/**
 *
 */
public class Categorization {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_RQT_SurveyTemplateAttrInst_Category_join";
    // Specify the fields that are used by this model
    public static final String FIELD_CATEGORY_ID = "700401990";
    public static final String FIELD_CATALOG_ID = "600000500";
    public static final String FIELD_TEMPLATE_ID = "179";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[] {
        FIELD_CATEGORY_ID, FIELD_TEMPLATE_ID};
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
    public Categorization(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Categorization> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Categorization> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Categorization> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Categorization(context, entry));
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
    public static Categorization findSingle(HelperContext context, String qualification) {
        // Declare the result
        Categorization result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Categorization(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static List<Categorization> findByCategoryId(HelperContext context, String categoryId) {
        // Build the qualification
        String qualification = "'"+FIELD_CATEGORY_ID+"' = \""+categoryId+"\"";
        // Return all results that match the qualification
        return find(context, qualification);
    }

    public static List<Categorization> findByCatalogId(HelperContext context, String catalogId) {
        // Build the qualification
        String qualification = "'"+FIELD_CATALOG_ID+"' = \""+catalogId+"\"";
        // Return all results that match the qualification
        return find(context, qualification);
    }

    public static List<Categorization> findByTemplateId(HelperContext context, String templateId) {
        // Build the qualification
        String qualification = "'"+FIELD_TEMPLATE_ID+"' = \""+templateId+"\"";
        // Return all results that match the qualification
        return find(context, qualification);
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getCategoryId() {return entry.getEntryFieldValue(FIELD_CATEGORY_ID);}
    public String getTemplateId() {return entry.getEntryFieldValue(FIELD_TEMPLATE_ID);}
}
