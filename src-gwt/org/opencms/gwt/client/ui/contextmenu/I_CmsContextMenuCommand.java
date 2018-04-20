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
 * For further information about Alkacon Software, please see the
 * company website: http://www.alkacon.com
 *
 * For further information about OpenCms, please see the
 * project website: http://www.opencms.org
 * 
 * You should have received a copy of the GNU Lesser General Public
 * License along with this library; if not, write to the Free Software
 * Foundation, Inc., 59 Temple Place, Suite 330, Boston, MA  02111-1307  USA
 */

package org.opencms.gwt.client.ui.contextmenu;

import org.opencms.gwt.shared.CmsContextMenuEntryBean;
import org.opencms.util.CmsUUID;

/**
 * Interface for context menu commands.<p>
 * 
 * @since version 8.0.1
 */
public interface I_CmsContextMenuCommand {

    /**
     * Executed on context menu item click.<p>
     * 
     * @param structureId the structure id of the resource
     * @param handler the context menu handler
     * @param bean the context menu entry bean 
     */
    void execute(CmsUUID structureId, I_CmsContextMenuHandler handler, CmsContextMenuEntryBean bean);

    /**
     * Returns the special menu item widget for this command.<p>
     * 
     * @param structureId the structure id of the resource
     * @param handler the context menu handler
     * @param bean the context menu entry bean 
     * 
     * @return the special menu item widget for this command
     */
    A_CmsContextMenuItem getItemWidget(
        CmsUUID structureId,
        I_CmsContextMenuHandler handler,
        CmsContextMenuEntryBean bean);

    /**
     * Returns if this command provides it's own menu item widget.<p>
     * 
     * @return <code>true</code> if this command provides it's own menu item widget
     */
    boolean hasItemWidget();
}
