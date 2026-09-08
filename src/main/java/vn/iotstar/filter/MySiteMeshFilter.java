package vn.iotstar.filter;

import org.sitemesh.builder.SiteMeshFilterBuilder;
import org.sitemesh.config.ConfigurableSiteMeshFilter;
import org.sitemesh.webapp.DispatchMode;

public class MySiteMeshFilter extends ConfigurableSiteMeshFilter {

    @Override
    protected void applyCustomConfiguration(SiteMeshFilterBuilder builder) {
        // Cấu hình tiền tố đường dẫn decorator và chế độ INCLUDE an toàn cho Tomcat 11
        builder.setDecoratorPrefix("/WEB-INF/decorators/");
        builder.setDispatchMode(DispatchMode.INCLUDE);

        // Áp dụng duy nhất 1 Bootstrap decorator cho toàn bộ ứng dụng (bao gồm các trang người dùng và admin)
        builder.addDecoratorPath("/*", "default.jsp");

        // Loại trừ các tài nguyên tĩnh, hình ảnh, file nhị phân
        builder.addExcludedPath("/image*");
        builder.addExcludedPath("/images/*");
        builder.addExcludedPath("/css/*");
        builder.addExcludedPath("/js/*");
        builder.addExcludedPath("/static/*");
        builder.addExcludedPath("*.css");
        builder.addExcludedPath("*.js");
        builder.addExcludedPath("*.png");
        builder.addExcludedPath("*.jpg");
        builder.addExcludedPath("*.jpeg");
        builder.addExcludedPath("*.gif");
        builder.addExcludedPath("*.ico");
        builder.addExcludedPath("*.webp");
        builder.addExcludedPath("/WEB-INF/*");
    }
}

