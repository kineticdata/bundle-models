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
public class TemplateAttribute {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_SRV_SurveyTemplateAttrInstance_join";
    // Specify the fields that are used by this model
    public static final String FIELD_NAME = "710000069";
    public static final String FIELD_CHARACTER_VALUE = "710000040";
    public static final String FIELD_VALUE = "710000073";
    public static final String FIELD_CATALOG_ID = "700001923";
    public static final String FIELD_TEMPLATE_ID = "700001107";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[]{
        FIELD_NAME, FIELD_VALUE, FIELD_CATALOG_ID,FIELD_TEMPLATE_ID};
    // Specify the fields that should be used for default sorting (the model
    // will use the Remedy form default sort order if this array is empty).
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[] {};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

    // Declare the associations for this record


    // Declare the memoized variables


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
    public TemplateAttribute(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<TemplateAttribute> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<TemplateAttribute> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<TemplateAttribute> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new TemplateAttribute(context, entry));
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
    public static TemplateAttribute findSingle(HelperContext context, String qualification) {
        // Declare the result
        TemplateAttribute result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new TemplateAttribute(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    /**
     * Returns a list of Attribute objects that are related to the specified
     * catalog.
     */
    public static List<TemplateAttribute> findByCatalogId(HelperContext context, String catalogId) {
        // Build the qualification String
        String qualification =
            "'"+FIELD_CATALOG_ID+"'=\""+catalogId+"\"";
        // Return all results that match the qualification
        return find(context, qualification);
    }

    /**
     * Returns a list of Attribute objects that are related to the specified
     * template.
     */
    public static List<TemplateAttribute> findByTemplateId(HelperContext context, String templateId) {
        // Build the qualification String
        String qualification =
            "'"+FIELD_TEMPLATE_ID+"'=\""+templateId+"\"";
        // Return all results that match the qualification
        return find(context, qualification);
    }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/


    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    /**
     * Returns the id of the catalog that the associated template belongs to.
     */
    public String getCatalogId() {
        return entry.getEntryFieldValue(FIELD_CATALOG_ID);
    }
    /**
     * Returns the name of the attribute.
     */
    public String getName() {
        return entry.getEntryFieldValue(FIELD_NAME);
    }
    /**
     * Returns the id of the template this attribute is associated to.
     */
    public String getTemplateId() {
        return entry.getEntryFieldValue(FIELD_TEMPLATE_ID);
    }
    /**
     * Returns the value of the attribute.
     */
    public String getValue() {
        // Attempt to retrieve the character value first (this is done because
        // character type attributes have a 4000 character value field and the
        // generic value field is only 255 characters).
        String value = entry.getEntryFieldValue(FIELD_CHARACTER_VALUE);
        // If the character value field does not have a value
        if (value == null) {
            // Set the value to the generic value field
            value = entry.getEntryFieldValue(FIELD_VALUE);
        }
        // Return the value
        return value;
    }
}
