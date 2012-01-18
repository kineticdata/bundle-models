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
public class Catalog {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "KS_RQT_ServiceCatalog_base";
    // Specify the fields that are used by this model
    public static final String FIELD_DESCRIPTION = "702020006";
    public static final String FIELD_ID = "179";
    public static final String FIELD_LOGOUT_ACTION = "700073100";
    public static final String FIELD_LOGOUT_DESTINATION = "700073102";
    public static final String FIELD_NAME = "600000500";
    public static final String FIELD_STATUS = "7";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[] {
        FIELD_DESCRIPTION, FIELD_ID, FIELD_LOGOUT_ACTION,
        FIELD_LOGOUT_DESTINATION, FIELD_NAME
    };
    // Specify the fields that should be used for default sorting (the model
    // will use the Remedy form default sort order if this array is empty).
    public static final String[] DEFAULT_SORT_FIELD_IDS = new String[] {};

    // Declare the HelperContext that was used to retrieve the record
    private HelperContext context;
    // Declare the SimpleEntry that represents the retrieved record
    private SimpleEntry entry;
    
    // Declare the associations for this record
    private List<CatalogAttribute> attributes;
    private List<Category> categories;
    private List<Categorization> categorizations;
    private List<Template> templates;

    // Memoized variables
    private Map<String,Category> categoriesMap;
    private String defaultLogoutUrl;
    private List<Category> rootCategories;
    private Map<String,Template> templatesMap;

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
    public Catalog(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Catalog> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Catalog> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Catalog> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Catalog(context, entry));
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
    public static Catalog findSingle(HelperContext context, String qualification) {
        // Declare the result
        Catalog result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Catalog(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/

    public static Catalog findById(HelperContext context, String catalogId) {
        // Build the qualification String
        String qualification ="'"+FIELD_ID+"'=\""+catalogId+"\"";
        // Return all results that match the qualification
        return findSingle(context, qualification);
    }

    public static Catalog findByName(HelperContext context, String catalogName) {
        // Build the qualification String
        String qualification ="'"+FIELD_NAME+"'=\""+catalogName+"\"";
        // Return all results that match the qualification
        return findSingle(context, qualification);
    }

    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/

    public List<CatalogAttribute> getAttributes() {
        // If the association has not been retrieved yet
        if (attributes == null) {
            // Build the association associated records
            attributes = CatalogAttribute.findByCatalogId(context, this.getId());
        }
        // Return the associated records
        return attributes;
    }

    public List<Category> getCategories() {
        // If the association has not been retrieved yet
        if (categories == null) {
            // Initialize the list of root categories
            rootCategories = new ArrayList();
            // Build the association associated records
            categories = Category.findByCatalogId(context, this.getId());

            // Build a map of subcategories
            Map<String,List<Category>> subcategoryMap = new LinkedHashMap();

            // For each association record
            for (Category category : categories) {
                // Set the catalog
                category.setCatalog(this);
                // If this is a root category, add it to the list of root categories
                if (category.getNameTrail().size() == 1) {
                    rootCategories.add(category);
                }
                // If this is not a root category
                else {
                    // Retrieve the list of siblings from the subcategory map
                    List<Category> siblings = subcategoryMap.get(category.getParentFullName());
                    // If the sibling list does not exist
                    if (siblings == null) {
                        // Create it and add it to the map
                        siblings = new ArrayList();
                        subcategoryMap.put(category.getParentFullName(), siblings);
                    }
                    // Add the current category to the siblings list
                    siblings.add(category);
                }
            }

            // For each association record (loop again now that the
            // subcategories map has been generated).
            for (Category category : categories) {
                // Map the subcategories for the current category
                category.setSubcategories(subcategoryMap.get(category.getName()));
            }
        }
        // Return the associated records
        return categories;
    }

    public List<Template> getTemplates() {
        // If the association has not been retrieved yet
        if (templates == null) {
            // Build the association associated records
            templates = Template.findByCatalogName(context, this.getName());
            // For each association record
            for (Template template : templates) {
                // Set the catalog
                template.setCatalog(this);
            }
        }
        // Return the associated records
        return templates;
    }

    /***************************************************************************
     * ACCESSORS
     **************************************************************************/
    /**
     * Returns the description of the catalog.
     */
    public String getDescription() {return entry.getEntryFieldValue(FIELD_DESCRIPTION);}
    /**
     * Returns the Id (instanceId) of the catalog.
     */
    public String getId() {return entry.getEntryFieldValue(FIELD_ID);}
    /**
     * Returns the logout action for the catalog (either "Go to URL" or "Go to
     * Template").
     */
    public String getLogoutAction() {return entry.getEntryFieldValue(FIELD_LOGOUT_ACTION);}
    /**
     * If the logout action for the catalog is "Go to URL", this will return the
     * URL provided.  If the logout action for the catalog is "Go to Template",
     * this will return the Id (instanceId) of the template.
     */
    public String getLogoutDestination() {return entry.getEntryFieldValue(FIELD_LOGOUT_DESTINATION);}
    /**
     * Returns the name of the catalog.
     */
    public String getName() {return entry.getEntryFieldValue(FIELD_NAME);}
    /**
     * Returns the status ("Active" or "Inactive") of the catalog.
     */
    public String getStatus() {return entry.getEntryFieldValue(FIELD_STATUS);}

    /***************************************************************************
     * HELPER METHODS
     **************************************************************************/

    public Map<String,Category> getCategoriesMap() {
        // If the collection has not been memoized yet
        if (categoriesMap == null) {
            // Initialize the collection
            categoriesMap = new LinkedHashMap();
            // For each of the associated items
            for (Category category : getCategories()) {
                // Add the item to the collection
                categoriesMap.put(category.getId(), category);
            }
        }
        // Return the memoized collection
        return categoriesMap;
    }

    /**
     * Returns the default URL that should redirected to upon logout.
     */
    public String getDefaultLogoutUrl() {
        // If the default logout url has not been calculated yet
        if (defaultLogoutUrl == null) {
            // If the catalog has a URL default logout destination
            if ("Go to URL".equals(getLogoutAction())) {
                // Redirect to the default logout action
                defaultLogoutUrl = getLogoutDestination();
            }
            // If the catalog has a Template default logout destination
            if ("Go to Template".equals(getLogoutAction())) {
                // Retrieve the template (this is done rather than manually
                // building the URL so that the template DisplayName property is
                // observed).
                Template template = Template.findById(context, getLogoutDestination());
                // If the template was not found
                if (template == null) {
                    // Throw an error
                    throw new RuntimeException("The template ("+
                        getLogoutDestination()+") was not found.  "+
                        "Unable to determine default logout action.");
                }
                // If the template was found
                else {
                    defaultLogoutUrl = template.getAnonymousUrl();
                }
            }
        }
        // Return the result
        return defaultLogoutUrl;
    }

    public List<Category> getRootCategories() {
        // If the collection has not been memoized yet
        if (rootCategories == null) {
            // Define the qualification
            String qualification =
                "'"+Category.FIELD_CATALOG_ID+"' = \""+getId()+"\" AND "+
                "NOT ('"+Category.FIELD_NAME+"' LIKE \"%"+Category.DELIMITER+"%\")";
            // Initialize the collection
            rootCategories = Category.find(context, qualification);
        }
        // Return the memoized collection
        return rootCategories;
    }

    public Map<String,Template> getTemplatesMap() {
        // If the collection has not been memoized yet
        if (templatesMap == null) {
            // Initialize the collection
            templatesMap = new LinkedHashMap();
            // For each of the associated items
            for (Template template : getTemplates()) {
                // Add the item to the collection
                templatesMap.put(template.getId(), template);
            }
        }
        // Return the memoized collection
        return templatesMap;
    }

    /**
     * TODO: Document
     * * Load:
     *   * Associated categorizations
     *   * Associated categories
     *   * Associated templates
     *   * Template attributes for templates associated to this catalog
     * * Set
     *   * Set each of the categories sub-categories
     *   * Set each of the categories associated templates
     *   * Set each of the templates associated categories
     *   * Set each of the templates attribute lists
     */
    public void preload() {
        // Retrieve the map of category ids to category objects
        Map<String,Category> categoriesMap = getCategoriesMap();
        // Retrieve the map of template ids to template objects
        Map<String,Template> templatesMap = getTemplatesMap();

        // For each of the categorizations
        for (Categorization categorization : getCategorizations()) {
            // Retrieve the associated category from the map
            Category category = categoriesMap.get(categorization.getCategoryId());
            // Retrieve the associated template from the map
            Template template = templatesMap.get(categorization.getTemplateId());
            // If the category and template both exist, associate the two objects
            if (category != null && template != null) {
                category.addTemplate(template);
                template.addCategory(category);
            }
        }

        // Retrieve all attributes for templates that belong to this catalog
        List<TemplateAttribute> templateAttributes = TemplateAttribute.findByCatalogId(context, getId());
        // For each of the template attribute lists
        for (TemplateAttribute attribute : templateAttributes) {
            // Set the template attributes for the template if it exists
            Template template = templatesMap.get(attribute.getTemplateId());
            if (template != null) {
                template.addAttribute(attribute);
            }
        }
    }

    private List<Categorization> getCategorizations() {
        // If the association has not been retrieved yet
        if (categorizations == null) {
            // Build the association associated records
            categorizations = Categorization.findByCatalogId(context, this.getId());
        }
        // Return the associated records
        return categorizations;
    }
}
