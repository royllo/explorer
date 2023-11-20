package org.royllo.explorer.web.util.page;

import lombok.Getter;

/**
 * Defines a page and its corresponding fragment.
 */
@Getter
public class Page {

    /** Page and fragment separator. */
    private static final String PAGE_AND_FRAGMENT_SEPARATOR = " :: ";

    /** Page fragment suffix. */
    private static final String PAGE_FRAGMENT_SUFFIX = "-fragment";

    /** Page name. */
    private final String pageName;

    /** Page fragment. */
    private final String pageFragment;

    /**
     * Constructor.
     *
     * @param newPageName page name
     */
    public Page(final String newPageName) {
        this.pageName = newPageName;
        pageFragment = newPageName + PAGE_AND_FRAGMENT_SEPARATOR + newPageName.replace("/", "-") + PAGE_FRAGMENT_SUFFIX;
    }

}
