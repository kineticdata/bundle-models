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
    public static final String FIELD_CATALOG_NAME = "600000500";
    public static final String FIELD_CATALOG_ID = "600000500";
    public static final String FIELD_SORT_ORDER = "700061010";
    public static final String FIELD_STATUS = "7";
    // Declare the delimiter that is used to separate nested categories
    public static final String DELIMITER = " :: ";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[]{
        FIELD_CATALOG_NAME, FIELD_DESCRIPTION, FIELD_ID, FIELD_IMAGE_TAG,
        FIELD_NAME, FIELD_NUMBER_OF_ITEMS, FIELD_STATUS};
    // Specify the fields that should be used for the default sort order
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[]{
        FIELD_SORT_ORDER, FIELD_NAME};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;

    // Declare the associations for the record
    private Catalog catalog;
    private List<Category> subcategories;
    private List<Template> templates;

    // Memoized variables
    private String name;
    private List<String> nameTrail;
    private String parentFullName;
    private String parentName;

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
         // Build the qualification String
         String qualification = "'"+FIELD_CATALOG_ID+"' = \""+catalogId+"\"";
         // Return all results that match the qualification
         return find(context, qualification);
     }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public void addTemplate(Template template) {
        if (templates == null) { templates = new ArrayList(); }
        templates.add(template);
    }

    public void addSubcategory(Category category) {
        if (subcategories == null) { subcategories = new ArrayList(); }
        subcategories.add(category);
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

    public List<Category> getSubcategories() {
        if (subcategories == null) {
            String qualification =
                "'"+FIELD_CATALOG_ID+"' = \""+getCatalog().getId()+"\" AND "+
                "'"+FIELD_NAME+"' LIKE \""+getFullName()+DELIMITER+"%\" AND "+
                "NOT ('"+FIELD_NAME+"' LIKE \""+getFullName()+DELIMITER+"%"+DELIMITER+"\")";
            subcategories = find(context, qualification);
        }
        return subcategories;
    }

    public List<Template> getTemplates() {
        // If the association has not been memoized yet
        if (templates == null){
            // Initialize the association
            templates = new ArrayList();
            // Retrieve all templates for the current catalog (This is done so
            // that all templates can be retrieved in a single query.  There is
            // no form available that includes all of the necessary Template
            // fields and the category name or id, and it is faster to retrieve
            // all templates for a catalog and filter them via code than to
            // retrieve each template individually).
            Map<String,Template> catalogTemplates = getCatalog().getTemplatesMap();
            // Retrieve all of the categorizations associated to this category
            List<Categorization> categorizations = Categorization.findByCategoryId(context, getId());
            // For each of the categorizations
            for (Categorization categorization : categorizations) {
                // Add the record to the association if it exists
                Template template = catalogTemplates.get(categorization.getTemplateId());
                if (template != null) { templates.add(template); }
            }
        }
        // Return the association
        return templates;
    }

    public void setCatalog(Catalog catalog) {
        this.catalog = catalog;
    }

    public void setSubcategories(List<Category> subcategories) {
        this.subcategories = subcategories;
    }

    public void setTemplates(List<Template> templates) {
        this.templates = templates;
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/

    public String getCatalogName() { return entry.getEntryFieldValue(FIELD_CATALOG_NAME); }
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
        boolean result = false;
        for (Category category : getSubcategories()) {
            if (category.hasTemplates() || category.hasNonEmptySubcategories()) {
                result = true;
                break;
            }
        }
        return result;
    }

    public boolean hasSubcategories() {
        return !getSubcategories().isEmpty();
    }

    public boolean hasTemplates() {
        return !getTemplates().isEmpty();
    }
}
