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
public class Template {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_SRV_SurveyTemplate";
    // Specify the fields that are used by this model
    public static final String FIELD_ANONYMOUS_URL = "700002489";
    public static final String FIELD_CATALOG = "600000500";
    public static final String FIELD_DESCRIPTION = "700001010";
    public static final String FIELD_ID = "179";
    public static final String FIELD_NAME = "700001000";
    public static final String FIELD_DISPLAY_NAME = "700002298";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[]{
        FIELD_CATALOG, FIELD_ID, FIELD_DESCRIPTION, FIELD_NAME,
        FIELD_ANONYMOUS_URL, FIELD_DISPLAY_NAME};
    // Specify the fields that should be used for default sorting (the model
    // will use the Remedy form default sort order if this array is empty).
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[] {};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

    // Declare the associations for this record
    private List<TemplateAttribute> attributes;
    private Catalog catalog;
    private List<Category> categories;

    // Declare the memoized variables
    private String categorizationString;

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
    public Template(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Template> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Template> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Template> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Template(context, entry));
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
    public static Template findSingle(HelperContext context, String qualification) {
        // Declare the result
        Template result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Template(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static List<Template> findByCatalogName(HelperContext context, String catalogName) {
        // Build the qualification
        String qualification = "'"+FIELD_CATALOG+"' = \""+catalogName+"\"";
        // Return all results that match the qualification
        return find(context, qualification);
    }

    public static Template findByDisplayName(HelperContext context, String catalogName, String displayName) {
        // Build the qualification
        String qualification =
            "'"+FIELD_CATALOG+"' = \""+catalogName+"\" AND "+
            "'"+FIELD_DISPLAY_NAME+"' = \""+displayName+"\"";
        // Return all results that match the qualification
        return findSingle(context, qualification);
    }

    public static Template findById(HelperContext context, String templateId) {
        // Build the qualification
        String qualification = "'"+FIELD_ID+"' = \""+templateId+"\"";
        // Return all results that match the qualification
        return findSingle(context, qualification);
    }

    public static Template findByName(HelperContext context, String catalogName, String templateName) {
        // Build the qualification
        String qualification =
            "'"+FIELD_CATALOG+"' = \""+catalogName+"\" AND "+
            "'"+FIELD_NAME+"' = \""+templateName+"\"";
        // Return all results that match the qualification
        return findSingle(context, qualification);
    }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public List<TemplateAttribute> getAttributes() {
        // If the association has not been retrieved yet
        if (attributes == null) {
            throw new UnsupportedOperationException("Not implemented.");
        }
        // Return the association
        return attributes;
    }

    public Catalog getCatalog() {
        // If the association has not been retrieved yet
        if (catalog == null) {
            // Load the association
            catalog = Catalog.findByName(context, getCatalogName());
        }
        // Return the associated object
        return catalog;
    }

    public List<Category> getCategories() {
        // If the association has not been retrieved yet
        if (categories == null) {
            throw new UnsupportedOperationException("Not implemented.");
        }
        // Return the association
        return categories;
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/
    
    public String getAnonymousUrl() {
        String anonymousUrl = entry.getEntryFieldValue(FIELD_ANONYMOUS_URL);

        // If there is a display name
        if (getDisplayName() != null && !"".equals(getDisplayName())) {
            // Replace the srv={ID} with name={DISPLAY_NAME}
            anonymousUrl = anonymousUrl.replaceFirst("srv=.*$", "name="+getDisplayName());
        }

        return anonymousUrl;
    }
    public String getCatalogName() {return entry.getEntryFieldValue(FIELD_CATALOG);}
    public String getDescription() {return entry.getEntryFieldValue(FIELD_DESCRIPTION);}
    public String getDisplayName() {return entry.getEntryFieldValue(FIELD_DISPLAY_NAME);}
    public String getId() {return entry.getEntryFieldValue(FIELD_ID);}
    public String getName() {return entry.getEntryFieldValue(FIELD_NAME);}

    /***************************************************************************
     * HELPER METHODS
     **************************************************************************/
    
    public String getCategorizationString() {
        if (categorizationString == null) {
            // Initialize the result
            StringBuilder result = new StringBuilder();
            // For each of the categories
            for(Category category : getCategories()) {
                // If this is not the first category, append a comma
                if (result.length() != 0) {result.append(",");}
                // Append the category name
                result.append(category.getName());
            }
            // Set the categorizationString
            categorizationString = result.toString();
        }
        return categorizationString;
    }

    public boolean hasCategories() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
