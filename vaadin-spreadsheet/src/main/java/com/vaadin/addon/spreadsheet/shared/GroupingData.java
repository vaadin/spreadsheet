package com.vaadin.addon.spreadsheet.shared;

/*
 * #%L
 * Vaadin Spreadsheet
 * %%
 * Copyright (C) 2013 - 2015 Vaadin Ltd
 * %%
 * This program is available under Commercial Vaadin Add-On License 3.0
 * (CVALv3).
 * 
 * See the file license.html distributed with this software for more
 * information about licensing.
 * 
 * You should have received a copy of the CVALv3 along with this program.
 * If not, see <http://vaadin.com/license/cval-3>.
 * #L%
 */

import java.io.Serializable;

/**
 * Shared state for the grouping feature
 */
public class GroupingData implements Serializable {
	private static final long serialVersionUID = 1L;
	
	/**
     * startIndex
     */
    public int startIndex;
    /**
     * endIndex
     */
    public int endIndex;
    /**
     * level
     */
    public int level;
    /**
     * index unique for this group, for collapse/expand
     */
    public int uniqueIndex;
    /**
     * collapsed
     */
    public boolean collapsed;

    /**
     * constructor
     */
    public GroupingData() {
    }

    /**
     * @param start
     * @param end
     * @param level
     * @param unique
     * @param coll
     */
    public GroupingData(long start, long end, short level, long unique,
            boolean coll) {
        this((int) start, (int) end, (int) level, (int) unique, coll);
    }

    /**
     * @param start
     * @param end
     * @param level
     * @param unique
     * @param coll
     */
    public GroupingData(int start, int end, int level, int unique, boolean coll) {
        startIndex = start;
        endIndex = end;
        this.level = level;
        uniqueIndex = unique;
        collapsed = coll;
    }

}
