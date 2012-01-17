package com.kineticdata.bundles.models;

// Import the necessary Java core classes
import java.util.*;
// Import the Kinetic ArsHelpers library
import com.kd.arsHelpers.*;
// Import the ArsBase class
import com.kineticdata.bundles.models.ArsBase;

/**
 *
 */
public class Scaffold {
    // Specify the name of the form associated to this model
    public static final String FORM_NAME = "";
    // Specify the fields that are used by this model
    public static final String FIELD_ = "";
    // Specify the fields that should be retrieved from form records
    public static final String[] FIELD_IDS = new String[] {};
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
    public Scaffold(HelperContext context, SimpleEntry entry) {
        this.context = context;
        this.entry = entry;
    }

    /***************************************************************************
     * GENERIC FIND METHODS
     **************************************************************************/

    /**
     * Generic find method to retrieve entries using an arbitrary qualification.
     */
    public static List<Scaffold> find(HelperContext context, String qualification) {
        // Return all matching records in the default sort order
        return find(context, qualification, DEFAULT_SORT_FIELD_IDS, 0, 0, 1);
    }

    /**
     * Generic find method to retrieve a subset of sorted entries using an
     * arbitrary qualification.
     */
    public static List<Scaffold> find(
        HelperContext context,
        String qualification,
        String[] sortFieldIds,
        Integer chunkSize,
        Integer recordOffset,
        Integer order
    ) {
        // Declare the results
        List<Scaffold> results = new ArrayList();
        // Retrieve all records
        SimpleEntry[] entries = ArsBase.find(
            context, FORM_NAME, qualification, FIELD_IDS, sortFieldIds, chunkSize, recordOffset, order);
        // For each of the entries
        for (SimpleEntry entry : entries) {
            // Add an instance of this class to the results
            results.add(new Scaffold(context, entry));
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
    public static Scaffold findSingle(HelperContext context, String qualification) {
        // Declare the result
        Scaffold result = null;
        // Attempt to retrieve the SimpleEntry record
        SimpleEntry entry = ArsBase.findSingle(context, FORM_NAME, qualification, FIELD_IDS);
        // If a SimpleEntry was found, instantiate an instance of this class
        if (entry != null) {
            result = new Scaffold(context, entry);
        }
        // Return the result
        return result;
    }

    /***************************************************************************
     * MODEL RETRIEVAL METHODS
     **************************************************************************/


    /***************************************************************************
     * ASSOCIATION METHODS
     **************************************************************************/


    /***************************************************************************
     * ACCESSORS
     **************************************************************************/


    /***************************************************************************
     * HELPER METHODS
     **************************************************************************/

}
