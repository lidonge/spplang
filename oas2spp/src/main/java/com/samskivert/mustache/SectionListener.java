package com.samskivert.mustache;

/**
 * @author lidong@date 2023-11-27@version 1.0
 */
public interface SectionListener {
    Object onGetSectionValue(Object value, Template tmpl, String curSeg);

    Object onSectionNext(Object elem, Template tmpl, String curSeg);
}
