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
public class Category {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_RQT_ServiceItemCategory";
    // Specify the fields that are used by this model
    public static final String FIELD_DESCRIPTION = "700401901";
    public static final String FIELD_ID = "179";
    public static final String FIELD_IMAGE_TAG = "700401930";
    public static final String FIELD_NAME = "700401900";
    public static final String FIELD_NUMBER_OF_ITEMS = "700401940";
    public static final String FIELD_CATALOG = "600000500";
    public static final String FIELD_SORT_ORDER = "700061010";
    public static final String FIELD_STATUS = "7";
    // Declare the delimiter that is used to separate nested categories
    public static final String DELIMITER = " :: ";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[]{
        FIELD_DESCRIPTION, FIELD_ID, FIELD_IMAGE_TAG, FIELD_NAME,
        FIELD_NUMBER_OF_ITEMS, FIELD_STATUS};
    // Specify the fields that should be used for the default sort order
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[]{
        FIELD_SORT_ORDER, FIELD_NAME};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

    // Declare the associations for the record

    // Memoized variables
    String name;
    List<String> nameTrail;
    String parentFullName;
    String parentName;

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
    public Category(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Category> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Category> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Category> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Category(context, entry));
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
    public static Category findSingle(HelperContext context, String qualification) {
        // Declare the result
        Category result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Category(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

     public static List<Category> findByCatalogId(HelperContext context, String catalogId) {
         throw new UnsupportedOperationException("Not implemented.");
     }

     public static List<Category> findByCatalogName(HelperContext context, String catalogName) {
         throw new UnsupportedOperationException("Not implemented.");
     }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public List<Category> getSubcategories() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public List<Category> getTemplates() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getDescription() { return entry.getEntryFieldValue(FIELD_DESCRIPTION); }
    public String getFullName() {return entry.getEntryFieldValue(FIELD_NAME);}
    public String getId() {return entry.getEntryFieldValue(FIELD_ID);}
    public String getStatus() {return entry.getEntryFieldValue(FIELD_STATUS);}
    public String getImageTag() {return entry.getEntryFieldValue(FIELD_IMAGE_TAG);}

    /***************************************************************************
     * HELPER METHODS
     **************************************************************************/

    public String getName() {
        if (name == null) {
            name = getNameTrail().get(0);
        }
        return name;
    }
    public List<String> getNameTrail() {
        if (nameTrail == null) {
            String[] nestedTrailArray = getFullName().split(DELIMITER);
            nameTrail = Arrays.asList(nestedTrailArray);
        }
        return nameTrail;
    }
    public String getParentFullName() {
        if (parentFullName == null) {
            // Attempt to retrieve the last index of the delimiter
            int lastIndex = getFullName().lastIndexOf(DELIMITER);
            // If the delimiter existed in the full name
            if (lastIndex != -1) {
                // Set the parent full name equal to the full name truncated
                // before the last delimiter
                parentFullName = getFullName().substring(0, lastIndex);
            }
        }
        return parentFullName;
    }
    public String getParentName() {
        if (parentName == null && getNameTrail().size() > 1) {
            parentName = getNameTrail().get(getNameTrail().size()-2);
        }
        return parentName;
    }

    public boolean hasNonEmptySubcategories() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public boolean hasSubcategories() {
        throw new UnsupportedOperationException("Not implemented.");
    }

    public boolean hasTemplates() {
        throw new UnsupportedOperationException("Not implemented.");
    }
}
