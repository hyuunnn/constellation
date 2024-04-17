/*
 * Copyright 2010-2024 Australian Signals Directorate
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
package au.gov.asd.tac.constellation.views.scripting;

import au.gov.asd.tac.constellation.graph.Graph;
import au.gov.asd.tac.constellation.graph.manager.GraphManager;
import au.gov.asd.tac.constellation.views.SwingTopComponent;
import javax.swing.ScrollPaneConstants;
import org.openide.awt.ActionID;
import org.openide.awt.ActionReference;
import org.openide.awt.ActionReferences;
import org.openide.util.NbBundle.Messages;
import org.openide.windows.TopComponent;

/**
 * Top component which displays something.
 */
@TopComponent.Description(
        preferredID = "ScriptingViewTopComponent",
        iconBase = "au/gov/asd/tac/constellation/views/scripting/scripting_view.png",
        persistenceType = TopComponent.PERSISTENCE_ALWAYS
)
@TopComponent.Registration(
        mode = "explorer",
        openAtStartup = false)
@ActionID(
        category = "Window",
        id = "au.gov.asd.tac.constellation.views.scripting.ScriptingViewTopComponent")
@ActionReferences({
    @ActionReference(path = "Menu/Views", position = 1400),
    @ActionReference(path = "Shortcuts", name = "CS-X")
})

@TopComponent.OpenActionRegistration(
        displayName = "#CTL_ScriptingViewAction",
        preferredID = "ScriptingViewTopComponent")
@Messages({
    "CTL_ScriptingViewAction=Scripting View",
    "CTL_ScriptingViewTopComponent=Scripting View",
    "HINT_ScriptingViewTopComponent=Scripting View"})
public final class ScriptingViewTopComponent extends SwingTopComponent {

    private final ScriptingViewPane scriptingViewPane;

    public ScriptingViewTopComponent() {
        setName(Bundle.CTL_ScriptingViewTopComponent());
        setToolTipText(Bundle.HINT_ScriptingViewTopComponent());
        initComponents();
        initContent();

        this.scriptingViewPane = new ScriptingViewPane(this);
    }

    @Override
    protected Object createContent() {
        return scriptingViewPane;
    }

    @Override
    protected int getHorizontalScrollPolicy() {
        return ScrollPaneConstants.HORIZONTAL_SCROLLBAR_NEVER;
    }

    @Override
    protected int getVerticalScrollPolicy() {
        return ScrollPaneConstants.VERTICAL_SCROLLBAR_NEVER;
    }

    @Override
    protected void handleNewGraph(final Graph graph) {
        if (needsUpdate()) {
            scriptingViewPane.update(graph);
        }
    }

    @Override
    protected void componentShowing() {
        super.componentShowing();
        handleNewGraph(GraphManager.getDefault().getActiveGraph());
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
