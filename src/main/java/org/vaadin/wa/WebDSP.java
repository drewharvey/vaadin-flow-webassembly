package org.vaadin.wa;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.polymertemplate.PolymerTemplate;
import com.vaadin.flow.templatemodel.TemplateModel;


@Tag("webdsp-element")
@HtmlImport("src/webdsp-element.html")
public class WebDSP extends PolymerTemplate<WebDSP.WebDspModel> {

    public interface WebDspModel extends TemplateModel {
        void setImageSrc(String imageSrc);
        void setFilter(String filter);
    }

    public enum Filter {
        NONE,
        INVERT,
        DEWDROPS
    }

    public void setImageSrc(String imageSrc) {
        getModel().setImageSrc(imageSrc);
    }

    public void setFilter(Filter filter) {
        getModel().setFilter(filter.name());
    }
}
