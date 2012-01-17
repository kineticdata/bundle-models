package com.kineticdata.bundles.models;

// Import the Kinetic ArsHelpers library
import com.kd.arsHelpers.*;
// Import the Remedy ARS api library
import com.remedy.arsys.api.*;

public abstract class ArsBase {
    public static SimpleEntry[] find(HelperContext context, String formName, String qualification, String[] fieldIds) {
        return find(context, formName, qualification, fieldIds, new String[0], 0, 0, 1);
    }

    public static SimpleEntry[] find(HelperContext context, String formName, String qualification, String[] fieldIds, String[] sortFieldIds) {
        return find(context, formName, qualification, fieldIds, sortFieldIds, 0, 0, 1);
    }

    public static SimpleEntry[] find(HelperContext context, String formName, String qualification, String[] fieldIds, Integer count) {
        return find(context, formName, qualification, fieldIds, new String[0], count, 0, 1);
    }

    public static SimpleEntry[] find(HelperContext context, String formName, String qualification, String[] fieldIds, String[] sortFieldIds, Integer chunkSize, Integer recordOffset, Integer order) {
        // Declare the result
        SimpleEntry[] results = new SimpleEntry[0];

        // Verify that context is not null
        if (context == null) {
            throw new IllegalArgumentException("The \"context\" argument can't be null.");
        }

        // Build the helper
        ArsPrecisionHelper helper = null;
        try {
            helper = new ArsPrecisionHelper(context);
        } catch (ARException e) {
            throw new RuntimeException("Unable to initialize an ArsHelper instance.", e);
        }

        // Retrieve the entry records
        SimpleEntry[] entries = new SimpleEntry[0];
        try {
            entries = helper.getSimpleEntryList(formName, qualification, fieldIds, sortFieldIds, chunkSize, recordOffset, order);
        } catch (Exception e) {
            throw new RuntimeException("There was a problem retrieving the "+formName+" records.", e);
        }

        // If there were results
        if (entries != null && entries.length > 0) {
            results = entries;
        }

        // Return the result
        return results;
    }

    public static SimpleEntry findSingle(HelperContext context, String formName, String qualification, String[] fieldIds) {
        SimpleEntry result = null;

        SimpleEntry[] entries = find(context, formName, qualification, fieldIds);

        if (entries.length > 1) {
            throw new RuntimeException("Multiple results matched the "+formName+" find single query: "+qualification);
        } else if (entries.length == 1) {
            result = entries[0];
        }

        return result;
    }

    public static SimpleEntry findByRequestId(HelperContext context, String formName, String id, String[] fieldIds) {
        return findSingle(context, formName, "'1'=\""+id+"\"", fieldIds);
    }

    public static SimpleEntry findByInstanceId(HelperContext context, String formName, String id, String[] fieldIds) {
        return findSingle(context, formName, "'179'=\""+id+"\"", fieldIds);
    }

    public static Integer count(HelperContext context, String formName, String qualification) {
        // Declare the result
        Integer result = new Integer(0);

        // If the list of fields does not exist in the formFields cache
        Field[] fields = new Field[0];
        // Build up the fieldListCriteria
        FieldListCriteria fieldListCriteria = new FieldListCriteria(
            new NameID(new NameID(formName)),
            new Timestamp(0),
            FieldType.AR_DATA_FIELD);
        FieldCriteria fieldCriteria = new com.remedy.arsys.api.FieldCriteria();
        fieldCriteria.setRetrieveAll(true);
        fieldCriteria.setPropertiesToRetrieve(
            fieldCriteria.getPropertiesToRetrieve() &
            ~FieldCriteria.CHANGE_DIARY &
            ~FieldCriteria.HELP_TEXT);

        try {
            fields = FieldFactory.findObjects(context.getContext(), fieldListCriteria, fieldCriteria);
        } catch (ARException e) {
            throw new RuntimeException(e);
        }

        // Build the qualification
        QualifierInfo qualifierInfo = null;
        try {
           qualifierInfo = Util.ARGetQualifier(context.getContext(), qualification, fields, null, Constants.AR_QUALCONTEXT_DEFAULT);
        } catch (ARException e) {
            throw new RuntimeException(e);
        }

        // Build the query criteria
        EntryCriteria entryCriteria = new EntryCriteria();
        EntryListCriteria entryListCriteria = new EntryListCriteria();
        entryListCriteria.setSchemaID(new NameID(formName));
        entryListCriteria.setQualifier(qualifierInfo);
        entryListCriteria.setMaxLimit(1);

        // Do an "empty" retrieval (of no more than 1 record), storing the count in result
        try {
           EntryFactory.findObjects(context.getContext(), entryListCriteria, entryCriteria, false, result);
        } catch (ARException e) {
            throw new RuntimeException(e);
        }

        // Return the result
        return result;
    }
}