package vn.iotstar.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        // Set decorator prefix and INCLUDE dispatch mode for Tomcat 11
        builder.setDecoratorPrefix("/WEB-INF/decorators/");
        builder.setDispatchMode(DispatchMode.INCLUDE);

        // Map decorator for profile paths
        builder.addDecoratorPath("/profile", "default.jsp");
        builder.addDecoratorPath("/profile/*", "default.jsp");
        builder.addDecoratorPath("/profile.jsp", "default.jsp");

        // Exclude static resources and admin pages
        builder.addExcludedPath("/image*");
        builder.addExcludedPath("/admin/*");
    }
}
