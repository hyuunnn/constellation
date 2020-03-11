/*
 * Copyright 2010-2019 Australian Signals Directorate
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package au.gov.asd.tac.constellation.views.scripting.calculator;

import au.gov.asd.tac.constellation.views.JavaFxTopComponent;
import au.gov.asd.tac.constellation.graph.Graph;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
        preferredID = "AttributeCalculatorTopComponent",
        iconBase = "au/gov/asd/tac/constellation/views/scripting/calculator/resources/attribute-calculator.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
        mode = "output",
        openAtStartup = false
)
@ActionID(
        category = "Window",
        id = "au.gov.asd.tac.constellation.views.scripting.calculator.AttributeCalculatorTopComponent"
)
@ActionReferences({
    @ActionReference(path = "Menu/Views", position = 100),
    @ActionReference(path = "Shortcuts", name = "CS-A")
})
@TopComponent.OpenActionRegistration(
        displayName = "#CTL_AttributeCalculatorAction",
        preferredID = "AttributeCalculatorTopComponent"
)
@Messages({
    "CTL_AttributeCalculatorAction=Attribute Calculator",
    "CTL_AttributeCalculatorTopComponent=Attribute Calculator",
    "HINT_AttributeCalculatorTopComponent=Attribute Calculator"
})
public final class AttributeCalculatorTopComponent extends JavaFxTopComponent<AttributeCalculatorPane> {

    private final AttributeCalculatorController attributeCalculatorController;
    private final AttributeCalculatorPane attributeCalculatorPane;

    public AttributeCalculatorTopComponent() {
        setName(Bundle.CTL_AttributeCalculatorTopComponent());
        setToolTipText(Bundle.HINT_AttributeCalculatorTopComponent());
        initComponents();

        attributeCalculatorController = new AttributeCalculatorController(AttributeCalculatorTopComponent.this);
        attributeCalculatorPane = new AttributeCalculatorPane(attributeCalculatorController);
        initContent();

        addAttributeCountChangeHandler(graph -> {
            if (attributeCalculatorPane != null && graph != null) {
                attributeCalculatorPane.updateAttributes(graph);
            }
        });
    }

    @Override
    protected AttributeCalculatorPane createContent() {
        return attributeCalculatorPane;
    }

    @Override
    protected String createStyle() {
        return "resources/attribute-calculator.css";
    }

    @Override
    protected void handleNewGraph(Graph graph) {
        if (attributeCalculatorPane != null && graph != null) {
            attributeCalculatorPane.updateAttributes(graph);
        }
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(this);
        this.setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 400, Short.MAX_VALUE)
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGap(0, 300, Short.MAX_VALUE)
        );
    }// </editor-fold>//GEN-END:initComponents

    // Variables declaration - do not modify//GEN-BEGIN:variables
    // End of variables declaration//GEN-END:variables
}
