package org.vaadin.wa;

import com.vaadin.flow.component.Text;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dependency.HtmlImport;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.html.Label;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.page.BodySize;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.Theme;
import com.vaadin.flow.theme.lumo.Lumo;
import elemental.html.Window;

/**
 * The main view contains a button and a template element.
 */
@BodySize(height = "100vh", width = "100vw")
@HtmlImport("styles/shared-styles.html")
@Route("")
@Theme(Lumo.class)
public class MainView extends VerticalLayout {

    public MainView() {

        WebDSP dsp = new WebDSP();
        dsp.setImageSrc("frontend/src/images/building.jpg");
        add(dsp);

        Button originalBtn = new Button("Original");
        originalBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.NONE));
        add(originalBtn);

        Button invertBtn = new Button("Invert");
        invertBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.INVERT));
        add(invertBtn);

        Button dewdropBtn = new Button("Dewdrops");
        dewdropBtn.addClickListener(e -> dsp.setFilter(WebDSP.Filter.DEWDROPS));
        add(dewdropBtn);
    }
}
