/*
 * This library is part of OpenCms -
 * the Open Source Content Management System
 *
 * Copyright (c) Alkacon Software GmbH (http://www.alkacon.com)
 *
 * This library is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 2.1 of the License, or (at your option) any later version.
 *
 * This library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the GNU
 * Lesser General Public License for more details.
 *
 * For further information about Alkacon Software GmbH, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.report;

import org.opencms.file.CmsRequestContext;
import org.opencms.i18n.CmsMessageContainer;
import org.opencms.i18n.CmsMessages;
import org.opencms.util.CmsStringUtil;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

/**
 * Base report class.<p> 
 * 
 * @since 6.0.0 
 */
public abstract class A_CmsReport implements I_CmsReport {

    /** Contains all error messages generated by the report. */
    private List<Object> m_errors = new ArrayList<Object>();

    /** Time of last report entry. */
    private long m_lastEntryTime;

    /** The locale this report is written in. */
    private Locale m_locale;

    /** The default report message bundle. */
    private CmsMessages m_messages;

    /** The original site root of the user who started this report. */
    private String m_siteRoot;

    /** Runtime of the report. */
    private long m_starttime;

    /** Contains all warning messages generated by the report. */
    private List<Object> m_warnings = new ArrayList<Object>();

    /**
     * @see org.opencms.report.I_CmsReport#addError(java.lang.Object)
     */
    public void addError(Object obj) {

        m_errors.add(obj);
    }

    /**
     * @see org.opencms.report.I_CmsReport#addWarning(java.lang.Object)
     */
    public void addWarning(Object obj) {

        m_warnings.add(obj);
    }

    /**
     * @see org.opencms.report.I_CmsReport#formatRuntime()
     */
    public String formatRuntime() {

        return CmsStringUtil.formatRuntime(getRuntime());
    }

    /**
     * @see org.opencms.report.I_CmsReport#getErrors()
     */
    public List<Object> getErrors() {

        return m_errors;
    }

    /**
     * @see org.opencms.report.I_CmsReport#getLastEntryTime()
     */
    public long getLastEntryTime() {

        return m_lastEntryTime;
    }

    /**
     * @see org.opencms.report.I_CmsReport#getLocale()
     */
    public Locale getLocale() {

        return m_locale;
    }

    /**
     * @see org.opencms.report.I_CmsReport#getRuntime()
     */
    public long getRuntime() {

        return System.currentTimeMillis() - m_starttime;
    }

    /**
     * Returns the original site root of the user who started this report,
     * or <code>null</code> if the original site root has not been set.<p>
     * 
     * @return the original site root of the user who started this report
     */
    public String getSiteRoot() {

        return m_siteRoot;
    }

    /**
     * @see org.opencms.report.I_CmsReport#getWarnings()
     */
    public List<Object> getWarnings() {

        return m_warnings;
    }

    /**
     * @see org.opencms.report.I_CmsReport#hasError()
     */
    public boolean hasError() {

        return (m_errors.size() > 0);
    }

    /**
     * @see org.opencms.report.I_CmsReport#hasWarning()
     */
    public boolean hasWarning() {

        return (m_warnings.size() > 0);
    }

    /**
     * @see org.opencms.report.I_CmsReport#print(org.opencms.i18n.CmsMessageContainer)
     */
    public void print(CmsMessageContainer container) {

        print(container.key(getLocale()), FORMAT_DEFAULT);
    }

    /**
     * @see org.opencms.report.I_CmsReport#print(org.opencms.i18n.CmsMessageContainer, int)
     */
    public void print(CmsMessageContainer container, int format) {

        print(container.key(getLocale()), format);
    }

    /**
     * @see org.opencms.report.I_CmsReport#println(org.opencms.i18n.CmsMessageContainer)
     */
    public void println(CmsMessageContainer container) {

        println(container.key(getLocale()), FORMAT_DEFAULT);
    }

    /**
     * @see org.opencms.report.I_CmsReport#println(org.opencms.i18n.CmsMessageContainer, int)
     */
    public void println(CmsMessageContainer container, int format) {

        println(container.key(getLocale()), format);
    }

    /**
     * @see org.opencms.report.I_CmsReport#printMessageWithParam(org.opencms.i18n.CmsMessageContainer,Object)
     */
    public void printMessageWithParam(CmsMessageContainer container, Object param) {

        print(container, I_CmsReport.FORMAT_NOTE);
        print(Messages.get().container(Messages.RPT_ARGUMENT_1, param));
        print(Messages.get().container(Messages.RPT_DOTS_0));
    }

    /**
     * @see org.opencms.report.I_CmsReport#printMessageWithParam(int,int,org.opencms.i18n.CmsMessageContainer,Object)
     */
    public void printMessageWithParam(int m, int n, CmsMessageContainer container, Object param) {

        print(
            Messages.get().container(Messages.RPT_SUCCESSION_2, String.valueOf(m), String.valueOf(n)),
            I_CmsReport.FORMAT_NOTE);
        printMessageWithParam(container, param);
    }

    /**
     * Removes the report site root prefix from the absolute path in the resource name,
     * that is adjusts the resource name for the report site root.<p> 
     * 
     * If the site root for this report has not been set,
     * or the resource name does not start with the report site root,
     * the name it is left untouched.<p>
     * 
     * @param resourcename the resource name (full path)
     * 
     * @return the resource name adjusted for the report site root
     * 
     * @see CmsRequestContext#removeSiteRoot(String)
     */
    public String removeSiteRoot(String resourcename) {

        if (m_siteRoot == null) {
            // site root has not been set
            return resourcename;
        }

        String siteRoot = CmsRequestContext.getAdjustedSiteRoot(m_siteRoot, resourcename);
        if ((siteRoot.equals(m_siteRoot)) && resourcename.startsWith(siteRoot)) {
            resourcename = resourcename.substring(siteRoot.length());
        }
        return resourcename;
    }

    /**
     * @see org.opencms.report.I_CmsReport#resetRuntime()
     */
    public void resetRuntime() {

        m_starttime = System.currentTimeMillis();
    }

    /**
     * Returns the default report message bundle.<p>
     * 
     * @return the default report message bundle
     */
    protected CmsMessages getMessages() {

        return m_messages;
    }

    /**
     * Initializes some member variables for this report.<p>
     * 
     * @param locale the locale for this report
     * @param siteRoot the site root of the user who started this report (may be <code>null</code>)
     */
    protected void init(Locale locale, String siteRoot) {

        m_starttime = System.currentTimeMillis();
        m_locale = locale;
        m_siteRoot = siteRoot;
        m_messages = Messages.get().getBundle(locale);
    }

    /**
     * Prints a String to the report.<p>
     *
     * @param value the String to add
     */
    protected void print(String value) {

        print(value, FORMAT_DEFAULT);
    }

    /**
     * Prints a String to the report, using the indicated formatting.<p>
     * 
     * Use the constants starting with <code>FORMAT</code> from this interface
     * to indicate which formatting to use.<p>
     *
     * @param value the message container to add
     * @param format the formatting to use for the output
     */
    protected abstract void print(String value, int format);

    /**
     * Prints a String with line break to the report.<p>
     * 
     * @param value the message container to add
     */
    protected void println(String value) {

        println(value, FORMAT_DEFAULT);
    }

    /**
     * Prints a String with line break to the report, using the indicated formatting.<p>
     * 
     * Use the constants starting with <code>C_FORMAT</code> from this interface
     * to indicate which formatting to use.<p>
     *
     * @param value the String to add
     * @param format the formatting to use for the output
     */
    protected void println(String value, int format) {

        print(value, format);
        println();
    }

    /**
     * Sets the time of the last report entry.<p>
     * 
     * @param time the time of the actual entry
     */
    protected void setLastEntryTime(long time) {

        m_lastEntryTime = time;
    }
}