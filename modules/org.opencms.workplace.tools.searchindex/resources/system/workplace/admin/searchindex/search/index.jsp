<%@ page import="
	org.opencms.main.*,
	org.opencms.search.*,
	org.opencms.jsp.*,
	org.opencms.workplace.*,
	java.util.*,
	java.io.*
"%><%!
  
	/**
	 * Helper method to print the stack trace to a String.<p>
     *
     * @param e the Exception to print the stack trace of
     * @return the stack trace as a String
	 */
	public static String getStackTrace(Exception e) {
		Writer result = new StringWriter();
    	PrintWriter printWriter = new PrintWriter(result);
    	e.printStackTrace(printWriter);
    	return result.toString();
	}
	
%><%   

    // initialize the workplace class
    CmsDialog wp = new CmsDialog(pageContext, request, response);
    
    if (wp.getAction()==CmsDialog.ACTION_CANCEL) {
    	wp.actionCloseDialog();
    	return;
    }

    // Create a JSP action element
    CmsJspActionElement cms = wp.getJsp();
    
    // Get the search manager
    CmsSearchManager searchManager = OpenCms.getSearchManager(); 
%>
<jsp:useBean id="search" scope="request" class="org.opencms.search.CmsSearch">
    <jsp:setProperty name = "search" property="*"/>
    <% search.init(cms.getCmsObject()); %>
</jsp:useBean>


<%= wp.htmlStart(null) %>
<%= wp.bodyStart("dialog", null) %>
<%= wp.dialogStart() %>
<%= wp.dialogContentStart(wp.key("icon.updateindex")) %>

<%
    List result = search.getSearchResult();
    if (result == null) {
%>
<%
    if (search.getLastException() != null) { 
%>
<h3>Error:</h3>
<pre>
<%= search.getLastException().toString() %> 
<%= getStackTrace(search.getLastException()) %> 
</pre>
<%
    }
%>
<form method="post">
<table>
<tr><th valign="top">Query:</th>
<td><input type="text" name="query" size="50"></td></tr>

<tr><th valign="top">Index:</th>
<td>
<select name="index">
<%
    for (Iterator i = searchManager.getIndexNames().iterator(); i.hasNext();) {
%>
<option><%=(String)i.next()%></option>
<%
    }
%>
</select>
</td></tr>

<tr><th valign="top">Fields:</th><td>
<input type="checkbox" name="field" value="title" checked>Title<br>
<input type="checkbox" name="field" value="keywords" checked>Keywords<br>
<input type="checkbox" name="field" value="description" checked>Description<br>
<input type="checkbox" name="field" value="content" checked>Content<br>
</td></tr>
</table>
<p>
<input type="submit" value="Submit">
</form>
<%
    } else {
%>

<%
        
        ListIterator iterator = result.listIterator();
%>
<%= result.size() %> Results found for query &lt;<%= search.getQuery() %>&gt; in fields <%= search.getFields() %>.
<%
        int i = 0;
        while (iterator.hasNext()) {
            i++;
            CmsSearchResult entry = (CmsSearchResult)iterator.next();
%>

<h3><%= i %>.&nbsp;<%= entry.getTitle() %>&nbsp;(<%= entry.getScore() %>%)</h3>
<%= entry.getPath() %>
<h6>Keywords</h6>
<%= entry.getKeywords() %>
<h6>Excerpt</h6>
<%= entry.getExcerpt() %>
<h6>Description</h6>
<%= entry.getDescription() %>
<h6>Last modified</h6>
<%= entry.getDateLastModified() %>
<%
    }   }
%> 

<%= wp.dialogContentEnd() %>
<%= wp.dialogEnd() %>
<%= wp.bodyEnd() %>
<%= wp.htmlEnd() %>