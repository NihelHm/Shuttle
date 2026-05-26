package com.simplecity.amp_library.ui.screens.search;

import com.simplecity.amp_library.utils.StringUtils;

@SuppressWarnings({"java:S1104", "java:S1444", "java:S131", "java:S1301", "java:S3776", "java:S3740", "java:S1066", "java:S1192", "java:S125", "java:S1118", "java:S117", "java:S1135", "java:S100", "java:S116"}) //NOSONAR
public class JaroWinklerObject<T> { //NOSONAR
    private T object; //NOSONAR
    String[] fields; //NOSONAR
    @SuppressWarnings("java:S1104") //NOSONAR
    public double score; //NOSONAR

    /**
     * Calculate the highest Jaro-Winkler score, for the passed in filter string & fields.
     * The object param is just a holder.
     *
     * @param object a holder for the object which owns the fields being compared.
     * @param filterString the string to match fields against
     * @param fields the fields to match the filter string against. Order matters here: subsequent fields
     * have a small amount shaved off in order to weight results in favour of earlier fields.
     */
    public JaroWinklerObject(T object, String filterString, String... fields) { //NOSONAR
        this.object = object; //NOSONAR
        this.fields = fields; //NOSONAR

        //Iterate over our fields, and take the highest matching score.
        //We subtract a little from the score each iteration, so that the first field takes precedence.
        //In other words, the scores are weighted in favour of field order.
        for (int i = 0, fieldsLength = fields.length; i < fieldsLength; i++) { //NOSONAR
            String field = fields[i]; //NOSONAR
            score = Math.max(score, StringUtils.getAdjustedJaroWinklerSimilarity(field, filterString) - (i * 0.001)); //NOSONAR
        }
    }
}
